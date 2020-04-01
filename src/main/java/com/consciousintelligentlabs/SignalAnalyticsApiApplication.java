package com.consciousintelligentlabs;

import com.consciousintelligentlabs.helper.amqp.RabbitMqConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SignalAnalyticsApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(SignalAnalyticsApiApplication.class, args);
    // RabbitAMQP rabbit = new RabbitAMQP();
    RabbitMqConfig rabbitMqConfig = new RabbitMqConfig();
  }
}
