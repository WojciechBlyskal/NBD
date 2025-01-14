package org.example.kafka;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.internals.Topic;

import javax.xml.transform.Result;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class TopicManager {
    private int partitionNumber = 3;
    private short replicationFactor = 3;
    private boolean topicCreated = true;

    public TopicManager(KafkaConf.Topic topicName) throws InterruptedException {

        createTopic(topicName.toString().toLowerCase());
    }

    private void createTopic(String topicName)
            throws InterruptedException {

        Properties topicProperties = new Properties();
        topicProperties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
                "kafka1:9192,kafka2:9292,kafka3:9392");

        HashMap<String, String> topicConfigs = new HashMap<>();
        topicConfigs.put(TopicConfig.DELETE_RETENTION_MS_CONFIG,
                String.valueOf(86400000));
        topicConfigs.put(TopicConfig.RETENTION_BYTES_CONFIG,
                String.valueOf(1000000000));

        try (Admin admin = Admin.create(topicProperties)){

            NewTopic topic = new NewTopic(topicName,
                    partitionNumber,
                    replicationFactor)
                    .configs(topicConfigs);

            CreateTopicsOptions options = new CreateTopicsOptions()
                    .timeoutMs(1000)
                    .validateOnly(false)
                    .retryOnQuotaViolation(true);
            CreateTopicsResult result = admin.createTopics(List.of(topic),
                    options);
            KafkaFuture<Void> futureResult =
                    result.values().get(topicName);
            futureResult.get();
        } catch (ExecutionException e) {

            if (e.getCause().getClass() == TopicExistsException.class){
                System.out.println("Topic already exists");
                topicCreated = false;
            }
        }
    }

    public boolean isTopicCreated() {

        return topicCreated;
    }
}
