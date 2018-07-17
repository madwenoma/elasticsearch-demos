package com.lee.learn;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 */
@SpringBootApplication
//@RestController
@ComponentScan(basePackages = {
        "com.lee.learn"
})
public class App {

    @Autowired
    private TransportClient client;

    @PutMapping("/add/people/man")
    public ResponseEntity addMan(@RequestParam(name = "name") String name,
                                 @RequestParam(name = "age") int age,
                                 @RequestParam(name = "country") String country,
                                 @RequestParam(name = "date")
                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date) {
        try {
            XContentBuilder content = XContentFactory.jsonBuilder().startObject().field("name", name)
                    .field("age", age).field("country", country).field("date", date.getTime()).endObject();
            IndexResponse response = client.prepareIndex("people", "man").setSource(content).get();
            return new ResponseEntity(response.getId(), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * AWSDLqgGQVHbBdjDNG52
     * AWSMricY9UnuXhQ3WZ1v
     *
     * @param id
     * @return
     */
    @GetMapping("/get/people/man")
    public ResponseEntity getBook(@RequestParam(name = "id", defaultValue = "") String id) {
        if (id.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        GetResponse rsp = client.prepareGet("people", "man", id).get();

        if (!rsp.isExists()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(rsp.getSource(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/people/man")
    public ResponseEntity deleteBook(@RequestParam(name = "id") String id) {
        DeleteResponse rsp = client.prepareDelete("people", "man", id).get();
        return new ResponseEntity(rsp.getId(), HttpStatus.OK);
    }

    @PutMapping("/update/people/man")
    public ResponseEntity update(@RequestParam(name = "id") String id,
                                 @RequestParam(name = "name") String name,
                                 @RequestParam(name = "age") String age) {
        UpdateRequest updateRequest = new UpdateRequest("people", "man", id);
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
            if (!name.isEmpty()) builder.field("name", name);
            if (!age.isEmpty()) builder.field("age", age);
            builder.endObject();
            updateRequest.doc(builder);
            UpdateResponse rsp = client.update(updateRequest).get();
            return new ResponseEntity(rsp.getIndex(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/query")
    public ResponseEntity query(@RequestParam(name = "name", required = false) String name,
                                @RequestParam(name = "min_age", defaultValue = "1") int minAge,
                                @RequestParam(name = "max_age", required = false) Integer maxAge) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (name != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("name", name));
        }
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age").from(minAge);
        if (maxAge != null && maxAge > 0) {
            rangeQueryBuilder.to(maxAge);
        }
        boolQueryBuilder.filter(rangeQueryBuilder);
        SearchRequestBuilder searchRequestBuilder = this.client.prepareSearch("people").setTypes("man").setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQueryBuilder).setFrom(0).setSize(10);
        System.out.println(searchRequestBuilder);
        SearchResponse rsp = searchRequestBuilder.get();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (SearchHit hit : rsp.getHits()) {
            result.add(hit.getSource());
        }

        return new ResponseEntity(result, HttpStatus.OK);

    }


    @GetMapping("/")
    public String index() {
        return "index";
    }


    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

    }
}
