package io.github.ricardosander.ecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderService {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (var orderProducer = new KafkaDispatcher<Order>()) {
            try (var emailProducer = new KafkaDispatcher<Email>()) {
                for (int i = 0; i < 10; i++) {

                    var userId = UUID.randomUUID().toString();
                    var orderId = UUID.randomUUID().toString();
                    var amount = BigDecimal.valueOf(Math.random() * 5000 + 1);

                    var order = new Order(userId, orderId, amount);
                    orderProducer.send("ECOMMERCE_NEW_ORDER", userId, order);

                    var subject = "Thank you for your order!";
                    var body = "We are processing your order!";
                    var email = new Email(subject, body);
                    emailProducer.send("ECOMMERCE_SEND_EMAIL", userId, email);
                }
            }
        }
    }
}
