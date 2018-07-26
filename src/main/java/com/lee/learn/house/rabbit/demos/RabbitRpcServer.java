package com.lee.learn.house.rabbit.demos;

import com.lee.learn.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class RabbitRpcServer {

    @RabbitListener(queues = RabbitMQConfig.RPC_QUEUE)
    public String hello(Message message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                        Channel channel) {
        String msg;
        try {
            int x = 1 / 0;
            msg = new String(message.getBody(), "utf-8");
            channel.basicAck(deliveryTag, false);
            System.out.println("rpc server received:" + msg);
            return "hello" + msg;
        } catch (Exception e) {
            e.printStackTrace();
//            throw new AmqpRejectAndDontRequeueException(e); //抛出该异常，消息会进入死信队列
            return "exception";
        }

    }
}
