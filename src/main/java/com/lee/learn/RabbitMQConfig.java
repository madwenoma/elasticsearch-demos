package com.lee.learn;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
//https://blog.csdn.net/m0_37867405/article/details/80793601

//https://blog.csdn.net/itguangit/article/details/80031595
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "boot-topic-exchange";
    public static final String ROUTINGKEY1 = "boot-routing.update";
    public static final String ROUTINGKEY_DELETE = "boot-routing.delete";
    public static final String ROUTINGKEY_All = "boot-routing.#";
    public static final String QUEUE1 = "spring-queue";
    public static final String QUEUE_ALL = "spring-queue-all";

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE1, false);
    }

    @Bean
    public Queue queueForAll() {
        return new Queue(QUEUE_ALL, false);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(topicExchange()).with(ROUTINGKEY1);
    }

    @Bean
    public Binding bindingDel() {
        return BindingBuilder.bind(queue()).to(topicExchange()).with(ROUTINGKEY_DELETE);
    }

    @Bean
    public Binding bindingAll() {
        return BindingBuilder.bind(queueForAll()).to(topicExchange()).with(ROUTINGKEY_All);
    }


    /**
     * 设置队列属性
     * @return
     */
//    @Bean
//    public Queue deadLetterQueue() {
//        Map<String, Object> arguments = new HashMap<>();
//        arguments.put("x-dead-letter-exchange", "");
//        arguments.put("x-dead-letter-routing-key", "");
//        arguments.put("x-message-ttl", 5000);
//        Queue queue = new Queue("", true, false, false, arguments);
//        System.out.println("arguments :" + queue.getArguments());
//        return queue;
//    }



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
