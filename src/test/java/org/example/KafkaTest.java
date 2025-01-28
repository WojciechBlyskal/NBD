package org.example;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.bson.conversions.Bson;
import org.example.Mgd.GuestMgd;
import org.example.kafka.Consumer;
import org.example.kafka.KafkaConf;
import org.example.kafka.KafkaConnectionManager;
import org.example.kafka.Producer;
import org.example.simpleMgdTypes.UniqueIdMgd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

//import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

//import static jdk.jfr.internal.jfc.model.Constraint.any;
import static org.junit.jupiter.api.Assertions.*;



public class KafkaTest {
    GuestMgd testGuest;

    @BeforeEach
    public void setUp() throws InterruptedException {

        testGuest = new GuestMgd(
                new UniqueIdMgd(UUID.randomUUID()),
                123456,
                "Adam",
                "Kowalski",
                "123456789"
        );
    }

    /*@BeforeEach
    void setupProducerAndConsumer() {
        //String bootstrapServers = kafkaContainer.getBootstrapServers();
        Producer producer = new Producer();
        Consumer consumer = new Consumer(KafkaConf.Topic.GUESTS);
    }*/

    @Test
    public void dataExchange() throws Exception {
        try (KafkaConnectionManager kafkaConnectionManager = new KafkaConnectionManager()){
            Producer producer = new Producer();
            producer.produce(testGuest);
            Consumer consumer = new Consumer(KafkaConf.Topic.GUESTS);
        }


        //System.out.println("Costam");

        /*KafkaConf kafkaConf = new KafkaConf();
        KafkaConf.Topic topic = KafkaConf.Topic.GUESTS;
        Consumer consumer = new Consumer(topic);

        ProducerRecord<UUID, String> record = new ProducerRecord<>(Topics.CLIENT_TOPIC,
                entityId.getid(), jsonClient);
        Future<RecordMetadata> sent = producer.send(record);
        RecordMetadata recordMetadata = sent.get();*/
    }

    /*@Test
    public void sendClientAsync() throws InterruptedException, ExecutionException, JsonProcessingException {
        KafkaProducer<UUID, String> mockProducer = mock(KafkaProducer.class);
        when(mockProducer.send(any())).thenReturn(mock(Future.class));

        // Create Producer instance
        Producer producer = new Producer();
        producer.produce(new GuestMgd(new UniqueIdMgd(UUID.randomUUID()), 123456789, "John", "Doe", "987654321"));

        // Verify that send was called
        verify(mockProducer, atLeastOnce()).send(any());
    }*/

}
