package com.lee.learn.rabbitmq;

import com.lee.learn.house.rabbit.demos.TopicSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicTest {

    @Autowired
    TopicSender sender;


    @Test
    public void sendTopic() throws IOException, InterruptedException {
//        sender.sendDelete("this is topic delete message");
        for (int i = 0; i < 200; i++) {
            Thread.sleep(1000);
            sender.sendAdd("this is topic add message");
        }
        System.in.read();
    }
}
