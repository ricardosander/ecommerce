package io.github.ricardosander.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

public class CreateUserService implements ConsumerFunction<Order> {

    public static void main(String[] args) {
        try (var service = new KafkaService<Order>("ECOMMERCE_NEW_ORDER", new CreateUserService())) {
            service.run();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private final Connection connection;

    CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        connection = DriverManager.getConnection(url);
        connection.createStatement().execute("create table Users (" +
                "   uuid varchar(200) primary key, " +
                "   email varchar(200)" +
                ")"
        );
    }

    @Override
    public void consume(ConsumerRecord<String, Order> message) throws SQLException {
        System.out.println("------------------------------------------");
        System.out.println("Processing new order, checking for new user");
        System.out.println(message.value());

        Order order = message.value();
        if (isNewUser(order.getEmail())) {
            insertNewUser(order.getEmail());
            System.out.println("User " + order.getEmail() + " created");
        }

        System.out.println("Order processed");
        System.out.println("------------------------------------------");
    }

    private boolean isNewUser(String email) throws SQLException {
        var sqlQuery = "select uuid from Users where email = ? limit 1";
        var query = connection.prepareStatement(sqlQuery);
        query.setString(1, email);
        var result = query.executeQuery();
        return !result.next();
    }

    private void insertNewUser(String email) throws SQLException {
        String sqlInsert = "insert into Users (uuid, email)  values (?, ?)";
        var insert = connection.prepareStatement(sqlInsert);
        insert.setString(1, UUID.randomUUID().toString());
        insert.setString(2, email);
        insert.execute();
    }

    @Override
    public String getGroupId() {
        return CreateUserService.class.getSimpleName();
    }

    @Override
    public Class getType() {
        return Order.class;
    }

}
