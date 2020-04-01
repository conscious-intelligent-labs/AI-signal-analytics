package com.consciousintelligentlabs.helper.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/** Config class for rabbitMQ. */
@Configuration
public class RabbitMqConfig {

  Logger logger = LoggerFactory.getLogger(RabbitMqConfig.class);

  @Value(value = "${spring.rabbitmq.template.exchange}")
  private String exchangeName;

  @Value(value = "${spring.rabbitmq.template.queue}")
  private String queueName;

  @Value(value = "${spring.rabbitmq.routing-key.notification}")
  private String notificationRoutingKey;

  @Value(value = "${spring.rabbitmq.host}")
  private String hostName;

  @Value(value = "${spring.rabbitmq.username}")
  private String userName;

  @Value(value = "${spring.rabbitmq.password}")
  private String password;

  public String getNotificationRoutingKey() {
    return notificationRoutingKey;
  }

  public void setNotificationRoutingKey(String notificationRoutingKey) {
    this.notificationRoutingKey = notificationRoutingKey;
  }

  public String getExchangeName() {
    return exchangeName;
  }

  public void setExchangeName(String exchangeName) {
    this.exchangeName = exchangeName;
  }

  public String getQueueName() {
    return queueName;
  }

  public void setQueueName(String queueName) {
    this.queueName = queueName;
  }

  public String getHostName() {
    return hostName;
  }

  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
