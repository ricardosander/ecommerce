package io.github.ricardosander.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.ExecutionException;

interface ConsumerFunction<T> {
    void consume(ConsumerRecord<String, T> message) throws ExecutionException, InterruptedException;
    String getGroupId();
    Class getType();
}
