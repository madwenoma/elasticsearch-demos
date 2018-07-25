package com.lee.learn;

import com.rabbitmq.client.Channel;
import org.junit.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//https://blog.csdn.net/m0_37867405/article/details/80793601

//https://blog.csdn.net/itguangit/article/details/80031595
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "boot-topic-exchange";
    public static final String ROUTINGKEY1 = "boot-routing.update";
    public static final String ROUTINGKEY2 = "boot-routing.#";
    public static final String QUEUE1 = "spring-queue";
    public static final String QUEUE2 = "spring-queue2";

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE1, false);
    }

    @Bean
    public Queue queue2() {
        return new Queue(QUEUE2, false);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(topicExchange()).with(ROUTINGKEY1);
    }
    @Bean
    public Binding binding2() {
        return BindingBuilder.bind(queue2()).to(topicExchange()).with(ROUTINGKEY2);
    }

//    @Bean
//    public SimpleMessageListenerContainer messageContainer() {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
//        container.setQueues(queue());
//        container.setExposeListenerChannel(true);
//        container.setMaxConcurrentConsumers(1);
//        container.setConcurrentConsumers(1);
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
//        container.setMessageListener(new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                byte[] body = message.getBody();
//                System.out.println("receive msg queue: " + new String(body));
//                Thread.sleep(10000);
//
//                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费
//
//            }
//        });
//        return container;
//    }
}
