package com.lee.learn;

import com.lee.learn.house.crawler.LianjiaCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/7/17.
 */
@Component
public class AppStartEvent implements ApplicationListener<ContextRefreshedEvent>{

    @Autowired
    private LianjiaCrawler lianjiaCrawler;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        String[] names = event.getApplicationContext().getBeanDefinitionNames();
//        for (String name : names) {
//            System.out.println(name);
//        }
//        System.out.println(event.getApplicationContext().getApplicationName());
        System.out.println("onApplicationEvent......");
//        try {
//            lianjiaCrawler.start(3);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
