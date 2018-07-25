package com.lee.learn.house.rabbit.demos;

import com.lee.learn.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class TopicSender implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {


    private RabbitTemplate rabbitTemplate;

    /**
     * 单例的消息回执是最后一个。
     * @param rabbitTemplate
     */
    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.rabbitTemplate.setReturnCallback(this);
    }

    public void sendUpdate(String msg) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "boot-routing.update", msg);
    }

    public void sendAdd(String msg) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "boot-routing.add", msg, correlationId);
//        int i = 1 / 0;
    }


    public void sendDelete(String msg) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "boot-routing.delete", msg);
    }


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println("confirm--:correlationData:" + correlationData + ",ack:" + ack + ",cause:" + cause);
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("return--message:" + new String(message.getBody()) + ",replyCode:" + replyCode + ",replyText:" + replyText + ",exchange:" + exchange + ",routingKey:" + routingKey);
    }
}
