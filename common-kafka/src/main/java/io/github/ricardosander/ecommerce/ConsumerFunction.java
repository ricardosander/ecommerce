package io.github.ricardosander.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

interface ConsumerFunction<T> {
    void consume(ConsumerRecord<String, T> message) throws Exception;
    String getGroupId();
    Class getType();
}
