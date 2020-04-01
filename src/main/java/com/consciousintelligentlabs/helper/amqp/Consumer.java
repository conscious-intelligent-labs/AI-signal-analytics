package com.consciousintelligentlabs.helper.amqp;

import com.consciousintelligentlabs.helper.Constants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

  public static void getMessages(
      String queueName, String routingKey, String exchangeName, String host, int prefectCount)
      throws IOException, TimeoutException {
    Logger logger = LoggerFactory.getLogger(Consumer.class);
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(host);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(queueName, true, false, false, null);
    channel.basicQos(prefectCount); // Per consumer limit

    DeliverCallback deliverCallback =
        (consumerTag, delivery) -> {
          try {
            String message = new String(delivery.getBody(), "UTF-8");
            logger.info(Constants.RABBITMQ_EVENT + " Received '" + message + "'");
          } finally {
            System.out.println(" [x] Done");
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
          }
        };

    channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
  }
}
