package com.lee.learn.house;

import com.lee.learn.house.domain.House;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by Administrator on 2018/7/16.
 */
@Component
public class HouseIndexOperator {

    @Autowired
    private TransportClient client;

    public boolean createIndex(House house) {
        try {
            XContentBuilder content = XContentFactory.jsonBuilder().startObject();
            Field[] fields = house.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                Object value = field.get(house);
                content.field(name, value);
            }
            content.endObject();
            IndexResponse response = client.prepareIndex("house", "ershoufang").setSource(content).get();
            System.out.println(response.getId());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;

    }
}
