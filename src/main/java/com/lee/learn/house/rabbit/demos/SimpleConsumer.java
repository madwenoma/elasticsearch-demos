package com.lee.learn.house.rabbit.demos;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SimpleConsumer {

    @RabbitListener(queues = "boot-rabbit-queue")
    public void receive(String msg) {
        System.out.println(msg);
    }
}
