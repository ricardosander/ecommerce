package io.github.ricardosander.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.ExecutionException;

class FraudDetectorService implements ConsumerFunction<Order> {

    public static void main(String[] args) {
        try (var service = new KafkaService<Order>("ECOMMERCE_NEW_ORDER", new FraudDetectorService())) {
            service.run();
        }
    }

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();

    @Override
    public void consume(ConsumerRecord<String, Order> message) throws ExecutionException, InterruptedException {
        System.out.println("------------------------------------------");
        System.out.println("Processing new order, checking for fraud");
        System.out.println(message.key());

        Order order = message.value();

        System.out.println("Order " + order.getOrderId() + " from " + order.getUserId() + " - $ " + order.getAmount());
        System.out.println(message.partition());
        System.out.println(message.offset());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (order.isFraud()) {
            System.out.println("Order is a fraud");
            orderDispatcher.send("ECOMMERCE_ORDER_REJECTED", order.getUserId(), order);
        } else {
            System.out.println("Order approved " + order);
            orderDispatcher.send("ECOMMERCE_ORDER_APPROVED", order.getUserId(), order);
        }

        System.out.println("Order processed");
        System.out.println("------------------------------------------");
    }

    @Override
    public String getGroupId() {
        return FraudDetectorService.class.getSimpleName();
    }

    @Override
    public Class getType() {
        return Order.class;
    }
}
