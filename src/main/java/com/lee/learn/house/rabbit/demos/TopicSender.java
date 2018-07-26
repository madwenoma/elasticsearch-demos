package com.lee.learn.house.rabbit.demos;

import com.lee.learn.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class TopicSender {


    private RabbitTemplate rabbitTemplate;

    /**
     * @param rabbitTemplate
     */
    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            System.out.println("confirm--:correlationData:" + correlationData + ",ack:" + ack + ",cause:" + cause);
            if (ack) {
                // 处理ack

            } else {
                // 处理nack, 此时cause包含nack的原因。
                // 如当发送消息给一个不存在的Exchange。这种情况Broker会关闭Channel；
                // 当Broker关闭或发生网络故障时，需要重新发送消息。
                // 暂时先日志记录，包括correlationData, cause等。
            }
        });
        this.rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey)
                -> System.out.println("return--message:" + new String(message.getBody())
                + ",replyCode:" + replyCode + ",replyText:" + replyText + ",exchange:" + exchange + ",routingKey:" + routingKey));
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

}
