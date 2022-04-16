package io.github.ricardosander.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.regex.Pattern;

public class LogService implements ConsumerFunction<String> {

    public static void main(String[] args) {
        try(var service = new KafkaService(Pattern.compile("ECOMMERCE.*"), new LogService())) {
            service.run();
        }
    }

    @Override
    public void consume(ConsumerRecord<String, String> message) {
        System.out.println("------------------------------------------");
        System.out.println("Log");
        System.out.println(message.topic());
        System.out.println(message.key());
        System.out.println(message.value());
        System.out.println(message.partition());
        System.out.println(message.offset());
        System.out.println("------------------------------------------");
    }

    @Override
    public String getGroupId() {
        return LogService.class.getSimpleName();
    }

    @Override
    public Class getType() {
        return String.class;
    }
}
