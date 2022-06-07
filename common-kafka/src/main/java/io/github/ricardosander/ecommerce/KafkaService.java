package io.github.ricardosander.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

class KafkaService<T> implements Closeable {

    private final KafkaConsumer<String, T> consumer;
    private final ConsumerFunction consumerService;

    KafkaService(String topicName, ConsumerFunction consumerService) {
        this(consumerService);
        this.consumer.subscribe(Collections.singletonList(topicName));
    }

    KafkaService(Pattern pattern, ConsumerFunction consumerService) {
        this(consumerService);
        this.consumer.subscribe(pattern);
    }

    private KafkaService(ConsumerFunction consumerService) {
        this.consumer = new KafkaConsumer<>(properties(consumerService));
        this.consumerService = consumerService;
    }

    void run() {
        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));
            if (records.isEmpty()) {
                continue;
            }
            System.out.println(records.count() + " records found");
            for (var record : records) {
                try {
                    consumerService.consume(record);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void close() {
        this.consumer.close();
    }

    private static Properties properties(ConsumerFunction consumer) {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        if (String.class == consumer.getType()) {
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        } else {
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        }
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumer.getGroupId());
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, consumer.getType().getName());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        return properties;
    }
}
