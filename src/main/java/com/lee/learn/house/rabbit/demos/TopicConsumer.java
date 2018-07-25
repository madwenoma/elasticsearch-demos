package com.lee.learn.house.rabbit.demos;

import com.lee.learn.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class TopicConsumer {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMQConfig.QUEUE2, durable = "false"),
            exchange = @Exchange(value = RabbitMQConfig.EXCHANGE, ignoreDeclarationExceptions = "true"),
            key = "boot-routing.#"))
    public void process1(@Payload String message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws Exception {
        if (message.equals("1")) {
            int i = 1 / 0;
        }
        channel.basicAck(deliveryTag, false);
        System.out.print("这里是接收者1答应消息： " + message);
        System.out.println("SYS_TOPIC_ORDER_CALCULATE_ZZ_FEE process1  : " + message);
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMQConfig.QUEUE1, durable = "false"),
            exchange = @Exchange(value = RabbitMQConfig.EXCHANGE, ignoreDeclarationExceptions = "true"),
            key = "boot-routing.update"))
    public void exactlyConsume(@Payload String message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws Exception {
        if (message.equals("1")) {
            int i = 1 / 0;
        }
        channel.basicAck(deliveryTag, false);
        System.out.println("这里是接收者2答应消息： " + message);
    }
}
