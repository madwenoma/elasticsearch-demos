package com.lee.learn.house.rabbit.demos.transaction;

import com.lee.learn.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

public class EventManager {
    private static final String INSERT_SQL = "insert into event(id,event_type,module_name,module_id,create_time) values(?,?,?,?,?)";
    private static final String SELECT_SQL = "select model_id from event where event_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RabbitTemplate rabbitTemplate;

    public EventManager(JdbcTemplate jdbcTemplate, RabbitTemplate rabbitTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void insertEvent(Event event) {
        jdbcTemplate.update(INSERT_SQL, event.getId(), event.getEventType(),
                event.getModelName(), event.getModelId(), event.getCreateTime());
    }

    public String queryModelId(String eventId) {
        return jdbcTemplate.queryForObject(SELECT_SQL, String.class, eventId);
    }

    public void sendEventQueue(String queue, Event event) {
        this.rabbitTemplate.convertAndSend(queue, event);
    }
}
