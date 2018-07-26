package com.lee.learn.rabbitmq;

import com.lee.learn.house.rabbit.demos.RabbitRpcClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitRpcClientTest {

    @Autowired
    RabbitRpcClient client;

    @Test
    public void requestHello() {
        String msg = "msg from client";
        String reply = client.requestHello(msg);
        System.out.println(reply);
        Assert.notNull(reply, "not null");
    }
}