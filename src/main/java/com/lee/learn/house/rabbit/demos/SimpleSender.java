package com.lee.learn.house.rabbit.demos;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SimpleSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    @PostConstruct
    public void autoSend() {
        this.send("hello,boot rabbit!");
    }

    public void send(String msg) {
        rabbitTemplate.convertAndSend("boot-rabbit-queue", msg);
    }

}
