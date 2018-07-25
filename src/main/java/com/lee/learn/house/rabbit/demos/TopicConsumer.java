package com.lee.learn.house.rabbit.demos;

import com.lee.learn.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TopicConsumer {

//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = RabbitMQConfig.QUEUE2, durable = "false"),
//            exchange = @Exchange(value = RabbitMQConfig.EXCHANGE, type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"),
//            key = "boot-routing.#"))
//    @RabbitHandler
//    public void process1(@Payload String message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws Exception {
//        if (message.equals("1")) {
//            int i = 1 / 0;
//        }
////        channel.basicAck(deliveryTag, false);
//        System.out.println("这里是接收者1答应消息： " + message);
//    }
//
//
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = RabbitMQConfig.QUEUE1, durable = "false"),
//            exchange = @Exchange(value = RabbitMQConfig.EXCHANGE, type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"),
//            key = "boot-routing.update"))
//    @RabbitHandler
//    public void exactlyConsume(@Payload String message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws Exception {
//        if (message.equals("1")) {
//            int i = 1 / 0;
//        }
////        channel.basicAck(deliveryTag, false);
//        System.out.println("这里是接收者2答应消息： " + message);
//    }


    @RabbitListener(queues = RabbitMQConfig.QUEUE1)
    @RabbitHandler
    public void topicReceiver(final Message message, final Channel channel,
                              @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            String msg = new String(message.getBody(), "utf-8");
            System.out.println(msg);
        } catch (Exception ex) {

        } finally {
            channel.basicAck(deliveryTag, false);
            System.out.println("ACK OVER...");
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ALL)
    @RabbitHandler
    public void topicAllReceiver(final Message message, final Channel channel,
                                 @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            String msg = new String(message.getBody(), "utf-8");
            System.out.println("topicAllReceiver: " + msg);
        } catch (Exception ex) {

        } finally {
            channel.basicAck(deliveryTag, false);
            System.out.println("topicAllReceiver ACK OVER...");
        }
    }
}
