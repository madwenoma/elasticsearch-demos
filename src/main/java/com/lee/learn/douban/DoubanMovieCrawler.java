package com.lee.learn.douban;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.ram.RamCrawler;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/7/16.
 */
@Component
public class DoubanMovieCrawler extends RamCrawler {

    //https://movie.douban.com/explore#!type=movie&tag=%E7%83%AD%E9%97%A8&sort=recommend&page_limit=20&page_start=20
    //https://movie.douban.com/j/search_subjects?type=movie&tag=%E7%83%AD%E9%97%A8&sort=recommend&page_limit=20&page_start=20
    //https://movie.douban.com/j/search_subjects?type=movie&tag=%E7%83%AD%E9%97%A8&sort=recommend&page_limit=20&page_start=40
    //https://movie.douban.com/subject/20495023/?tag=%E7%83%AD%E9%97%A8&from=gaia_video

    private AtomicInteger houseIndexId = new AtomicInteger(1);

    public DoubanMovieCrawler() {
        this.getConf().setExecuteInterval(new Random().nextInt(3) * 1000);
        this.getConf().setDefaultUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.933.400 QQBrowser/9.4.8699.400");
        this.setThreads(1);
        this.setMaxExecuteCount(2);
        /*do not fetch jpg|png|gif*/
        this.addRegex("-.*\\.(jpg|png|gif).*");
        /*do not fetch url contains #*/
        this.addRegex("-.*#.*");
        for (int i = 1; i < 4; i++) {
            this.addSeed("https://movie.douban.com/j/search_subjects?type=movie&tag=%E7%83%AD%E9%97%A8&sort=recommend&page_limit=20&page_start=" + i * 20, "movieList");
        }
    }

    public static void main(String[] args) throws Exception {
        DoubanMovieCrawler crawler = new DoubanMovieCrawler();
        crawler.start(2);
    }


    @Override
    public void visit(Page page, CrawlDatums next) {

        if (page.matchType("movieList")) {
            //如果是列表页，抽取内容页链接
            //将内容页链接的type设置为content，并添加到后续任务中
            next.add(page.links("#content > div > div.article > div > div.list-wp > div a"), "movieDetail");
        } else if (page.matchType("movieDetail")) {
            System.out.println(page.url());

        }

    }


}
