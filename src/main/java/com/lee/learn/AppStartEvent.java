package com.lee.learn;

import com.lee.learn.house.crawler.LianjiaCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/7/17.
 */
@Component
public class AppStartEvent implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private LianjiaCrawler lianjiaCrawler;

    @Override
    @Async
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            System.out.println("onApplicationEvent......");
//            try {
//                lianjiaCrawler.start(2);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        }
//        new Thread(() -> {
//            try {
//                lianjiaCrawler.start(3);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
    }

}
