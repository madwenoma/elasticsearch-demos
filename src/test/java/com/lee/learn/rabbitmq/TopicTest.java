package com.lee.learn.rabbitmq;

import com.lee.learn.house.rabbit.demos.TopicSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicTest {

    @Autowired
    TopicSender sender;


    @Test
    public void sendTopic() {
        sender.send("this is topic message");
    }
}
