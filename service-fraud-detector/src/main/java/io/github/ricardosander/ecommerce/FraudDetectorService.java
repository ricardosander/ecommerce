package io.github.ricardosander.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

class FraudDetectorService implements ConsumerFunction<Order> {

    public static void main(String[] args) {
        try (var service = new KafkaService<Order>("ECOMMERCE_NEW_ORDER", new FraudDetectorService())) {
            service.run();
        }
    }

    @Override
    public void consume(ConsumerRecord<String, Order> message) {
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
