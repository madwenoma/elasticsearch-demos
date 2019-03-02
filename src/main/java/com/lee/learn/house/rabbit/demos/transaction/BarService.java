package com.lee.learn.house.rabbit.demos.transaction;

import com.lee.learn.RabbitMQConfig;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

//模拟另一个机器上的服务
@Service
public class BarService {

    @Autowired
    private JdbcTemplate jdbcTemplate;//
    @Autowired
    private EventManager eventManager;


    @Transactional
    @RabbitListener(queues = RabbitMQConfig.TRAN_FOO_SUCC_QUEUE)
    public void handleFooSuccEvent(Event event) {//监听队列对象直接转为java bean
        String barId = UUID.randomUUID().toString();
        String barName = "bar1";
        try {
           jdbcTemplate.update("INSERT INTO bar(id, name) values(?,?)", barId, barName);

            //抛出异常，模拟分布式事务失败
            throw new RuntimeException();

        }catch (Exception e) {
            eventManager.sendEventQueue(RabbitMQConfig.TRAN_BAR_FAIL_QUEUE,event);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }


}
