package org.example;

import org.bson.conversions.Bson;
import org.example.Mgd.GuestMgd;
import org.example.kafka.KafkaConnectionManager;
import org.example.simpleMgdTypes.UniqueIdMgd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

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

    @Test
    public void dataExchange() throws Exception {
        try (KafkaConnectionManager kafkaConnectionManager = new KafkaConnectionManager()){
        }
        System.out.println("Elo bnc" + "");
    }
}
