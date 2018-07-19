package com.lee.learn.house;

import com.lee.learn.house.crawler.LianjiaCrawler;
import com.lee.learn.house.domain.House;
import com.lee.learn.house.domain.HouseIndexTemplate;
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
        HouseIndexTemplate house = new HouseIndexTemplate();
        house.setHouseId(1L);
        house.setJingJiRen("xiaowang");
        house.setJunJia(42333);
        house.setPrice(324.0);
        house.setTitle("月季园 通透");
        house.setXiaoQu("月季园");
        house.setChanQuan("70年");
        house.setChaoXiang("南 北");
        house.setDianTi("有");
        house.setGongNuanFangShi("地热");
        house.setHuXing("连体一户");
        house.setJianZhuJieGou("");
        house.setJianZhuLeiXing("");
        house.setJianZhuMianJi(40.0);
        house.setShiNeiMianJi(20.0);
        System.out.println(operator.createIndex(house));
    }


}