package org.example.kafka;

import com.fasterxml.jackson.dataformat.avro.AvroGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class KafkaConnectionManager
        implements AutoCloseable{

    private ArrayList<Producer> producers = new ArrayList<>();

    private ArrayList<Consumer> consumers = new ArrayList<>();

    private HashMap<String, TopicManager> topicManagers = new HashMap<>();

    public KafkaConnectionManager() throws InterruptedException, IOException {

        for (KafkaConf.Topic topic : KafkaConf.Topic.values()){

            topicManagers.put(topic.toString().toLowerCase(),
                    new TopicManager(topic));
        }
    }

    public Producer createProducer(){

        Producer producer = new Producer();
        producers.add(producer);
        return producer;
    }

    public Consumer createConsumer(KafkaConf.Topic topic){

        Consumer consumer = new Consumer(topic);
        consumers.add(consumer);
        return consumer;
    }

    @Override
    public void close() throws Exception {
        for (Producer producer : producers){
            producer.closeProducer();
        }

        for (Consumer consumer : consumers){

            consumer.closeConsumer();
        }
    }
}
