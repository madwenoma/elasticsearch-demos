package com.lee.learn.rabbitmq;

import com.lee.learn.house.rabbit.demos.DeadLetterDemoSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeadLetterDemoSenderTest {

    @Autowired
    DeadLetterDemoSender sender;

    @Test
    public void sendDeadLetterMsg() throws IOException {
        String msg = "this is a msg for delay ";
        sender.sendDeadLetterMsg(msg);
        System.in.read();
    }
}