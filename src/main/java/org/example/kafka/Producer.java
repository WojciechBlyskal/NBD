package org.example.kafka;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
//import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.*;
import org.example.Mgd.*;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.example.simpleMgdTypes.UniqueIdMgd;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Producer {

    private KafkaProducer<UUID, String> producer;
    private PolymorphicTypeValidator ptv;
    private ObjectMapper mapper;

    public static void main(String[] args)
            throws Exception {

        GuestMgd testGuest = new GuestMgd(
                new UniqueIdMgd(UUID.randomUUID()),
                123456789,
                "Adam",
                "Kowalski",
                "123456789"
        );

        RoomMgd testRoom = new MicroSuiteMgd(
                new UniqueIdMgd(UUID.randomUUID()),
                1,
                5,
                25.0,
                100.0,
                0
        );

        RentMgd testRent = new RentMgd(
                new UniqueIdMgd(UUID.randomUUID()),
                "A35",
                LocalDateTime.of(2024, 10, 15, 14, 30, 45),
                testGuest,
                testRoom
        );

        try (KafkaConnectionManager kafkaConnectionManager = new KafkaConnectionManager()) {
            Producer producer = kafkaConnectionManager.createProducer();
            producer.produce(testRent);
        }
    }

    public Producer() {

        producer = initProducer();

        ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("java.util.ArrayList")
                .allowIfSubType("model.GuestMgd")
                .allowIfSubType("model.MicroSuiteMgd")
                .allowIfSubType("model.StudioMgd")
                .allowIfSubType("model.RentMgd")
                .allowIfSubType("simpleMgdTypes.UniqueIdMgd")
                .build();
        mapper = new ObjectMapper()
                .activateDefaultTyping(
                        ptv,
                        ObjectMapper.DefaultTyping.NON_FINAL
                )
                .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    private KafkaProducer<UUID, String> initProducer(){

        Properties producerProperties = new Properties();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "kafka1:9192,kafka2:9292,kafka3:9392");
        producerProperties.put(ProducerConfig.CLIENT_ID_CONFIG,
                "local");
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                UUIDSerializer.class);
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        producerProperties.put(ProducerConfig.ACKS_CONFIG, "all");
        producerProperties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,
                true);

        return new KafkaProducer<>(producerProperties);
    }

    public void produce(IEntity object)
            throws JsonProcessingException,
            InterruptedException,
            ExecutionException {

        //AvroMapper avroMapper = new AvroMapper();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBasic = mapper.writeValueAsString(object);
        JsonNode jsonNode = objectMapper.readTree(jsonBasic);
        ((ObjectNode) jsonNode).put("RentPlace", "PlacyPlace");
        String jsonWithPlace = objectMapper.writeValueAsString(jsonNode);

        ProducerRecord<UUID, String> record;
        GenericRecord genericRecord = null;

        AvroSchema avroSchema = null;
        String topic = null;
        String type = null;
        GenericRecordFiller genericRecordFiller = new GenericRecordFiller();
        switch (object.getClass().getSimpleName()){
            case "GuestMgd":
                topic = KafkaConf.Topic.GUESTS.toString().toLowerCase();
                break;
            case "MicroSuiteMgd":
            case "StudioMgd":
                topic = KafkaConf.Topic.ROOMS.toString().toLowerCase();
                break;
            case "RentMgd":
                topic = KafkaConf.Topic.RENTS.toString().toLowerCase();
                break;
            default:
                record = null;
        }

        if (topic != null){

            record = new ProducerRecord<UUID, String>(topic,
                    object.getEntityId().getUuid(),
                    jsonWithPlace);

            Future<RecordMetadata> sent = producer.send(record);
            RecordMetadata recordMetadata = sent.get();
            System.out.println("Message sent to topic: " + topic + ", Partition: " + recordMetadata.partition() +
                    ", Offset: " + recordMetadata.offset());
        }
    }

    public void closeProducer(){
        producer.close();
    }
}
