package com.lee.learn.house.rabbit.demos.transaction;

import java.util.UUID;

public class Event {

    private String id;
    private EventType eventType;
    private String modelName;
    private String modelId;
    private long createTime;

    public Event() {
    }

    public Event(EventType eventType, String modelName, String modelId) {
        this.id = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.modelName = modelName;
        this.modelId = modelId;
        this.createTime = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
