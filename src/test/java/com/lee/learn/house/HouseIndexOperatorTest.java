package com.lee.learn.house;

import com.lee.learn.house.crawler.LianjiaCrawler;
import com.lee.learn.house.domain.House;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2018/7/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HouseIndexOperatorTest extends TestCase {

    @Autowired
    HouseIndexOperator operator;

    @Test
    public void testCreateIndex() throws Exception {
//        House house = LianjiaCrawler.createHouse();
//        System.out.println(operator.createIndex(house));
    }


}