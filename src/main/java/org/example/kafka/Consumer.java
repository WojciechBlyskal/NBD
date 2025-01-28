package org.example.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.example.Mgd.GuestMgd;
import org.example.Mgd.IEntity;
import org.example.Mgd.RentMgd;
import org.example.Mgd.RoomMgd;
import org.example.mongoRepositories.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.example.redisRepositories.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Consumer {
    private KafkaConsumer<UUID, String> consumer;
    private MongoDecoratedRepository mongoDecoratedRepository;
    private RedisRepository redisRepository;
    private ConnectionManager connectionManager;
    private Class<IEntity> findType;
    private PolymorphicTypeValidator ptv;
    private ObjectMapper mapper;
    private Jsonb jsonb;

    public static void main(String[] args)
            throws Exception {

        KafkaConf.Topic topic = KafkaConf.Topic.valueOf(args[0]);
        int consumerNo = Integer.parseInt(args[1]);

        ArrayList<Consumer> consumers = new ArrayList<>();

        try (KafkaConnectionManager kafkaConnectionManager = new KafkaConnectionManager()){

            for (int i=0; i<consumerNo; i++){
                consumers.add(new Consumer(topic));
            }

            ExecutorService executorService = Executors.newFixedThreadPool(2);
            for (Consumer consumer : consumers){
                executorService.execute(() -> {
                    try {
                        consumer.consumeFromOffset();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }

    public Consumer(KafkaConf.Topic topic) {

        consumer = initConsumer(topic);

        ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("java.util.ArrayList")
                .allowIfSubType("model.GuestMgd")
                .allowIfSubType("model.StudioMgd")
                .allowIfSubType("model.MicroSuiteMgd")
                .allowIfSubType("model.RentMgd")
                .allowIfSubType("simpleMgdTypes.UniqueIdMgd")
                .build();
        mapper = new ObjectMapper().activateDefaultTyping(
                ptv,
                ObjectMapper.DefaultTyping.NON_FINAL);
        this.jsonb = JsonbBuilder.create();

        redisRepository
                = new RedisRepository("docker/redisCluster");
        connectionManager
                = new ConnectionManager();

        Class aClass;
        switch (topic){
            case GUESTS:
                aClass = GuestMgd.class;
                findType = aClass;

                mongoDecoratedRepository =
                        new MongoDecoratedRepository(
                                new RedisDecoratedRepository(
                                        new GuestDecorator(
                                                redisRepository
                                        ),
                                        new GuestRepository(connectionManager)
                                )
                        );
                break;
            case ROOMS:
                aClass = RoomMgd.class;
                findType = aClass;

                mongoDecoratedRepository =
                        new MongoDecoratedRepository(
                                new RedisDecoratedRepository(
                                        new RoomDecorator(
                                                redisRepository
                                        ),
                                        new RoomRepository(connectionManager)
                                )
                        );
                break;
            case RENTS:
                aClass = RentMgd.class;
                findType = aClass;

                mongoDecoratedRepository =
                        new MongoDecoratedRepository(
                                new RedisDecoratedRepository(
                                        new RentDecorator(
                                                redisRepository
                                        ),
                                        new RentRepository(connectionManager)
                                )
                        );
                break;

        }
    }

    private KafkaConsumer<UUID, String> initConsumer(KafkaConf.Topic topic){

        Properties consumerProperties = new Properties();
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG,
                KafkaConf.CONSUMER_GROUP);
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "kafka1:9192,kafka2:9292,kafka3:9392");
                //"172.25.0.2:9192,172.25.0.4:9292,172.25.0.3:9392");
        consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
                false);
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                UUIDDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        KafkaConsumer<UUID, String> consumer =
                new KafkaConsumer(consumerProperties);

        consumer.subscribe(List.of(topic.toString().toLowerCase()));
        return consumer;
    }

    public void consumeFromOffset() throws Exception {

        Duration timeout = Duration.of(1, ChronoUnit.SECONDS);

        while (true) {

            ConsumerRecords<UUID, String> records = consumer.poll(timeout);

            for (ConsumerRecord<UUID, String> record : records) {

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(record.value());
                ((ObjectNode) jsonNode).remove("RentPlace");
                String jsonBasic = objectMapper.writeValueAsString(jsonNode);

                IEntity entity = mapper.readValue(jsonBasic,
                        findType);

                mongoDecoratedRepository.addRemote(entity);

                System.out.println("Thread: " + Thread.currentThread().getName() + " | " +
                        "Partition: " + record.partition() + " | " +
                        "Entity consumed: " + record.key());
            }

            consumer.commitAsync();
        }
    }

    public void consumeFromBeginning() throws Exception {

        consumer.poll(0);

        Set<TopicPartition> assignment = consumer.assignment();
        consumer.seekToBeginning(assignment);

        Duration timeout = Duration.of(1, ChronoUnit.SECONDS);

        while (true) {

            ConsumerRecords<UUID, String> records = consumer.poll(timeout);

            for (ConsumerRecord<UUID, String> record : records) {
                IEntity entity = mapper.readValue(record.value(),
                        findType);

                mongoDecoratedRepository.addRemote(entity);

                System.out.println("Thread: " + Thread.currentThread().getName() + " | " +
                        "Partition: " + record.partition() + " | " +
                        "Entity consumed: " + record.key());
            }


            consumer.commitSync();
        }
    }

    public void closeConsumer(){

        redisRepository.close();
        connectionManager.close();
        consumer.close();
    }
}
