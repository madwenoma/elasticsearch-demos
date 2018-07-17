package com.lee.learn.house.crawler;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.ram.RamCrawler;
import com.lee.learn.house.HouseIndexOperator;
import com.lee.learn.house.domain.House;
import org.apache.commons.lang.StringUtils;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Random;

/**
 * Created by Administrator on 2018/7/16.
 */
@Component
public class LianjiaCrawler extends RamCrawler {


    @Autowired
    private HouseIndexOperator indexOperator;

    public LianjiaCrawler() {
        this.getConf().setExecuteInterval(new Random().nextInt(3) * 1000);
        this.getConf().setDefaultUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.933.400 QQBrowser/9.4.8699.400");
        this.setThreads(2);
        this.setMaxExecuteCount(2);
          /*do not fetch jpg|png|gif*/
        this.addRegex("-.*\\.(jpg|png|gif).*");
        /*do not fetch url contains #*/
        this.addRegex("-.*#.*");
        for (int i = 2; i < 5; i++) {
            this.addSeed("https://bj.lianjia.com/ershoufang/pg" + i + "/", "houseList");
        }
    }

    public static void main(String[] args) throws Exception {
        LianjiaCrawler crawler = new LianjiaCrawler();
        crawler.start(2);
    }

    /**
     * 顶秀欣园西苑 /2室1厅/91.39平米/南/简装/有电梯
     * 中楼层(共22层)/2003年建塔楼/赵公口
     */
    public static House createHouse(Page doc) {
        //            Document doc = Jsoup.connect("https://bj.lianjia.com/ershoufang/101103038684.html").get();
        House house = new House();
        String title = doc.select("body > div.sellDetailHeader > div > div > div.title > h1").text();
        house.setTitle(title);
        String price = doc.select("body > div.overview > div.content > div.price > span.total").text();
        house.setPrice(Integer.parseInt(price));
        String xiaoQu = doc.select("body > div.overview > div.content > div.aroundInfo > div.communityName > a.info").text();
        house.setXiaoQu(xiaoQu);
        String jingjiren = doc.select("body > div.overview > div.content > div.brokerInfo.clear > div > div.brokerName > a").text();
        house.setJingJiRen(jingjiren);
        String junJiaStr = doc.select("body > div.overview > div.content > div.price > div.text > div.unitPrice > span").get(0).ownText();
        int junJia = Integer.parseInt(junJiaStr);
        house.setJunJia(junJia);
        Elements es = doc.select("#introduction > div > div > div.base > div.content > ul > li");
        final int[] startField = {6};
        Field[] fields = house.getClass().getDeclaredFields();
        es.forEach(element -> {
                        String key = element.children().text();
                    String value = element.ownText();
                    if (value.contains("暂无数据")) {
                        System.out.println(key + ":无数据");
                        value = "0";
                    }
                    try {
                        Field field = fields[startField[0]++];
                        Type type = field.getType();
                        field.setAccessible(true);
                        if (type == Integer.class) {
                            int intValue = Integer.parseInt(value);
                            field.set(house, intValue);
                        } else if (type == Double.class) {
                            if (value.contains("㎡"))
                                value = StringUtils.substringBeforeLast(value, "㎡");
                            Double doubleValue = Double.parseDouble(value);
                            field.set(house, doubleValue);
                        } else {
                            field.set(house, value);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
        );
        return house;

    }

    @Override
    public void visit(Page page, CrawlDatums next) {

        if (page.matchType("houseList")) {
            //如果是列表页，抽取内容页链接
            //将内容页链接的type设置为content，并添加到后续任务中
            next.add(page.links("div[id=leftContent] a"), "houseDetail");
        } else if (page.matchType("houseDetail")) {
            System.out.println(page.url());
            if (page.url().endsWith(".html")) {
//                System.out.println(page.select("body > div.sellDetailHeader > div > div > div.title > h1").text());
                House house = createHouse(page);
                boolean createIndexResult = indexOperator.createIndex(house);
                System.out.println(createIndexResult);
            }
        }

    }

    /**
     * 房屋户型
     2室1厅1厨1卫
     所在楼层
     顶层 (共6层)
     建筑面积
     87.97㎡
     户型结构
     平层
     套内面积
     77.55㎡
     建筑类型
     板楼
     房屋朝向
     南 北
     建筑结构
     混合结构
     装修情况
     简装
     梯户比例
     一梯两户
     供暖方式
     集中供暖
     配备电梯
     无
     产权年限
     70年
     */


}
