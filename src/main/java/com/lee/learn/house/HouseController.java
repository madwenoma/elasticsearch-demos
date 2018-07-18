package com.lee.learn.house;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/17.
 * add test remote proxy
 */
@RestController
public class HouseController {

    @Autowired
    private TransportClient esClient;

    @GetMapping("/house/ershoufang")
    public ResponseEntity queryHouse(@RequestParam(required = false, defaultValue = "0") int minPrice, @RequestParam(required = false, defaultValue = "0") int maxPrice, @RequestParam(required = false) String title,
                                     @RequestParam(required = false) String xiaoQu, @RequestParam(required = false) String tiHuBiLi,
                                     @RequestParam(required = false) String jingJiRen, @RequestParam(required = false, defaultValue = "0") double jianZhuMianJi) {

        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        if (xiaoQu != null)
            boolBuilder.must(QueryBuilders.matchQuery("xiaoQu", xiaoQu));
        if (jingJiRen != null)
            boolBuilder.must(QueryBuilders.matchQuery("jingJiRen", jingJiRen));
        if (tiHuBiLi != null)
            boolBuilder.must(QueryBuilders.matchQuery("tiHuBiLi", jingJiRen));

        RangeQueryBuilder priceRangeBuilder = null;
        if (minPrice != 0) {
            priceRangeBuilder = QueryBuilders.rangeQuery("price").from(minPrice);
        }
        if (maxPrice != 0) {
            if (priceRangeBuilder == null) {
                priceRangeBuilder = QueryBuilders.rangeQuery("price");
            }
            priceRangeBuilder.to(maxPrice);
        }

        if (jianZhuMianJi != 0) {
            RangeQueryBuilder areaRangeBuilder = QueryBuilders.rangeQuery("jianZhuMianJi").from(jianZhuMianJi);
            boolBuilder.filter(areaRangeBuilder);
        }

        if (priceRangeBuilder != null) {
            boolBuilder.filter(priceRangeBuilder);
        }

        SearchRequestBuilder requestBuilder = esClient.prepareSearch("house").setTypes("ershoufang").setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        requestBuilder.setQuery(boolBuilder).setFrom(1).setSize(1).addSort("price",SortOrder.ASC);
        SearchResponse rsp = requestBuilder.get();
        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit hit : rsp.getHits()) {
            results.add(hit.getSource());
        }

        return new ResponseEntity(results, HttpStatus.OK);

    }


    /**
     *搜索提示词接口
     * @return
     */
    @GetMapping("/house/ershoufang/prompt")
    public ResponseEntity housePrompt(){

        return null;
    }
}



