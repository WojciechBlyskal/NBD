package org.example;

import org.example.kafka.Producer;
import org.example.Mgd.GuestMgd;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.UUID;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello world!");

        GuestMgd testGuest = new GuestMgd(
                new UniqueIdMgd(UUID.randomUUID()),
                1234567,
                "Adam",
                "Kowalski",
                "123456789"
        );

        Producer producer = new Producer();

        producer.produce(testGuest);
    }
}