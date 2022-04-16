package io.github.ricardosander.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService implements ConsumerFunction<Email> {

    public static void main(String[] args) {
        try(var service = new KafkaService<Email>("ECOMMERCE_SEND_EMAIL", new EmailService())) {
            service.run();
        }
    }

    @Override
    public void consume(ConsumerRecord<String, Email> message) {
        System.out.println("------------------------------------------");
        System.out.println("Sending email");
        System.out.println(message.key());

        Email email = message.value();

        System.out.println(email.getSubject());
        System.out.println(email.getBody());

        System.out.println(message.partition());
        System.out.println(message.offset());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Email sent");
        System.out.println("------------------------------------------");
    }

    @Override
    public String getGroupId() {
        return EmailService.class.getSimpleName();
    }

    @Override
    public Class getType() {
        return Email.class;
    }
}
