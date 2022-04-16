package io.github.ricardosander.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

class KafkaDispatcher<T> implements Closeable {

    private final KafkaProducer<String, T> producer;
    private final Callback callback;

    KafkaDispatcher() {
        this.producer = new KafkaProducer<>(properties());
        this.callback = (data, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
                return;
            }
            System.out.println("Success send: topic: " + data.topic() + "/ partition: " + data.partition() + "/ offset: " + data.offset() + "/ timestamp: " + data.timestamp());
        };
    }

    public void send(String topicName, String key, T value) throws ExecutionException, InterruptedException {
        producer.send(new ProducerRecord<>(topicName, key, value), callback).get();
    }

    @Override
    public void close() {
        this.producer.close();
    }

    private Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
        return properties;
    }
}
