package com.lee.learn;

import org.springframework.amqp.core.*;
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

    public static final String RPC_EXCHANGE = "rpc-exchange";
    public static final String RPC_ROUTINGKEY = "rpc";
    public static final String RPC_QUEUE = "rpc-queue";


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


    ///////////////////////////////////Rpc 相关 start/////////////////////////////////////

    @Bean
    public DirectExchange rpcExchanger() {
        return new DirectExchange(RPC_EXCHANGE);
    }

    @Bean
    public Queue rpcQueue() {
        return new Queue(RPC_QUEUE, false);
    }

    @Bean
    public Binding rpcBinding() {
        return BindingBuilder.bind(rpcQueue()).to(rpcExchanger()).with(RPC_ROUTINGKEY);
    }

    ///////////////////////////////////Rpc 相关   end/////////////////////////////////////


    ///////////////////////////////////死信队列 相关 start/////////////////////////////////////

    public static String delayQueue = "delay_queue";
    public static String delayExchange = "delay_exchange";
    public static String delayRoutingKey = "delay_routing_key";

    public static final String deadLetterQueue = "dead_letter_queue";
    public static String deadLetterExchange = "dead_letter_exchange";
    public static String deadLetterRoutingKey = "dead_letter_routing_key";

    @Bean
    public DirectExchange delayExchange() {
        DirectExchange ret = new DirectExchange(delayExchange, true, false);
        ret.setInternal(false);
        return ret;
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(deadLetterExchange, true, false);
    }

    /**
     * 延迟队列没有consumer,只是作为向dead letter queue转发消息的存储中介
     * 消息开始是发送给延迟队列，30s后被转发向死信队列，消费者监听死信队列收到消息，实现定时延迟消息。
     */
    @Bean
    public Queue delayQueue() {
        Map<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("x-message-ttl", 30 * 1000); // Message TTL = 30s
        arguments.put("x-max-length", 1000000); // Max length = 1million
        // 死信路由到死信交换器DLX
        arguments.put("x-dead-letter-exchange", deadLetterExchange);
        arguments.put("x-dead-letter-routing-key", deadLetterRoutingKey);
        return new Queue(delayQueue, true, false, false, arguments);
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(deadLetterQueue, true, false, false);
    }

    @Bean
    public Binding bindingDelay() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(delayRoutingKey);
    }

    @Bean
    public Binding bindingDeadLetter() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(deadLetterRoutingKey);
    }

    ///////////////////////////////////死信队列 相关   end/////////////////////////////////////


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
