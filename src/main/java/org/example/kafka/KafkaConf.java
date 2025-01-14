package org.example.kafka;

public class KafkaConf {

    public enum Topic {
        GUESTS,
        ROOMS,
        RENTS
    }

    public static final String CONSUMER_GROUP =
            "consumers";
}
