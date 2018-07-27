package com.lee.learn.douban;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.ram.RamCrawler;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

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
        for (int i = 1; i < 3; i++) {
            this.addSeed("https://movie.douban.com/j/search_subjects?type=movie&tag=%E7%83%AD%E9%97%A8&sort=recommend&page_limit=20&page_start=" + i * 20, "movieList");
        }
    }

    public static void main(String[] args) throws Exception {

        Stream.of(3, 4, 2, 1).sorted(Comparator.comparingInt(o -> o)).forEach(System.out::println);

        DoubanMovieCrawler crawler = new DoubanMovieCrawler();
        crawler.start(2);
    }

    private Gson gson = new Gson();

    @Override
    public void visit(Page page, CrawlDatums next) {
        System.out.println(page.html());

        MoviePage moviePage = gson.fromJson(page.html(), MoviePage.class);
        moviePage.getSubjects().stream()
                .sorted(Comparator.comparingDouble(movie -> Double.parseDouble(movie.getRate())))
                .forEach(System.out::println);
//        if (page.matchType("movieList")) {
//            next.add(page.links("#content > div > div.article > div > div.list-wp > div a"), "movieDetail");
//        } else if (page.matchType("movieDetail")) {
//            System.out.println(page.url());
//        }

    }


}
