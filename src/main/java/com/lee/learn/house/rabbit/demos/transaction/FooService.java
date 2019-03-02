package com.lee.learn.house.rabbit.demos.transaction;

import com.lee.learn.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FooService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EventManager eventManager;


    @Transactional
    public void insertFoo(String name) {
        String fooId = UUID.randomUUID().toString();
        try {
            jdbcTemplate.update("INSERT INTO foo(id,name) values(?,?)", fooId, name);
        } finally {
            Event event = new Event(EventType.CREATE, "Foo", fooId);
            eventManager.insertEvent(event);
            eventManager.sendEventQueue(RabbitMQConfig.TRAN_FOO_SUCC_QUEUE, event);
        }
    }

    //与上面业务方法逆向的操作，分布式回滚
    @Transactional
    @RabbitListener(queues = RabbitMQConfig.TRAN_BAR_FAIL_QUEUE)
    public void handleBarFailEvent(Event event) {
        String modelId = eventManager.queryModelId(event.getId());
        jdbcTemplate.update("DELETE FROM foo where id = ?", modelId);
    }


}
