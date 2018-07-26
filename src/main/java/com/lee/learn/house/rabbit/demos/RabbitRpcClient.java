package com.lee.learn.house.rabbit.demos;

import com.lee.learn.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitRpcClient {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public String requestHello(String msg) {
        return (String) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.RPC_EXCHANGE,
                RabbitMQConfig.RPC_ROUTINGKEY, msg);
    }

}
