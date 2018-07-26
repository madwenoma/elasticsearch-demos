package com.lee.learn.house.rabbit.demos;

import com.lee.learn.RabbitMQConfig;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeadLetterDemoSender {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendDeadLetterMsg(String msg) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        // 声明消息处理器  这个对消息进行处理  可以设置一些参数   对消息进行一些定制化处理
        MessagePostProcessor messagePostProcessor = message -> {
            org.springframework.amqp.core.MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setContentEncoding("utf-8");
//            设置过期时间10*1000毫秒
//            messageProperties.setExpiration("10000");
            return message;
        };
        //向延迟队列发送消息
        rabbitTemplate.convertAndSend(RabbitMQConfig.delayExchange
                , RabbitMQConfig.delayRoutingKey, msg, messagePostProcessor, correlationData);
    }
}
