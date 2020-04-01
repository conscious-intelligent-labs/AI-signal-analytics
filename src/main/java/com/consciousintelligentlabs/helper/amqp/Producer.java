package com.consciousintelligentlabs.helper.amqp;

import com.consciousintelligentlabs.helper.Constants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Producer {

  /**
   * Sends message to rabbitMQ.
   *
   * @param message Message to send to queue.
   * @param routingKey Routing Key to use for message.
   * @param exchangeName Exchange to use.
   * @param host The rabbitMQ host.
   */
  public static void sendMessage(
      String message, String routingKey, String exchangeName, String host) {
    Logger logger = LoggerFactory.getLogger(Producer.class);
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(host);

    try (Connection connection = factory.newConnection();
        Channel channel = connection.createChannel()) {
      channel.exchangeDeclare(exchangeName, "topic", true);
      channel.basicPublish(exchangeName, routingKey, null, message.getBytes("UTF-8"));
      logger.trace(Constants.RABBITMQ_EVENT + " Sent '" + routingKey + "\n Message: " + message);
    } catch (Exception e) {
      logger.error(
          Constants.RABBITMQ_EVENT
              + " Error sending to RabbitMQ: "
              + e.getMessage()
              + "\n\n "
              + e.getStackTrace()
              + "\n\n"
              + e.getCause());
    }
  }
}
