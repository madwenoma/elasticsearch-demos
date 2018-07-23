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
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static com.lee.learn.house.HouseIndexOperator.INDEX_NAME;
import static com.lee.learn.house.HouseIndexOperator.INDEX_TYPE;

/**
 * Created by Administrator on 2018/7/17.
 * add test remote proxy
 */
@RestController
public class HouseController {

    Logger logger = LoggerFactory.getLogger(HouseController.class);

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
        if (title != null)
            boolBuilder.must(QueryBuilders.matchQuery("title", title));
        if (tiHuBiLi != null)
            boolBuilder.must(QueryBuilders.matchQuery("tiHuBiLi", tiHuBiLi));

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

        SearchRequestBuilder requestBuilder = esClient.prepareSearch(INDEX_NAME).setTypes(INDEX_TYPE).setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        requestBuilder.setQuery(boolBuilder).setFrom(1).setSize(10).addSort("price", SortOrder.ASC);
        SearchResponse rsp = requestBuilder.get();
        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit hit : rsp.getHits()) {
            results.add(hit.getSource());
        }

        return new ResponseEntity(results, HttpStatus.OK);

    }


    /**
     * 搜索提示词接口
     *
     * @return
     */
    @GetMapping("/house/ershoufang/prompt")
    public ResponseEntity housePrompt(@RequestParam String keyWord) {
        System.out.println(keyWord);
        CompletionSuggestionBuilder completionBuilder = SuggestBuilders.completionSuggestion("suggest").prefix(keyWord).size(5);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        String suggestName = "autocomplete";
        suggestBuilder.addSuggestion(suggestName, completionBuilder);
        SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(INDEX_NAME).setTypes(INDEX_TYPE).suggest(suggestBuilder);
        logger.info(requestBuilder.toString());
        SearchResponse rsp = requestBuilder.get();
        Suggest.Suggestion suggestion = rsp.getSuggest().getSuggestion(suggestName);
        HashSet<String> suggestSet = new HashSet<>();
        int suggestCount = 0;
        for (Object term : suggestion.getEntries()) {
            if (term instanceof CompletionSuggestion.Entry) {
                CompletionSuggestion.Entry item = (CompletionSuggestion.Entry) term;
                for (CompletionSuggestion.Entry.Option option : item.getOptions()) {
                    String suggestText = option.getText().toString();
                    if (suggestSet.contains(suggestText))
                        continue;
                    suggestSet.add(suggestText);
                    suggestCount++;
                }
            }
            if (suggestCount++ > 5)//最多5个提示词
                break;
        }
        List<Object> suggestTexts = suggestSet.stream().collect(Collectors.toList());;

        return new ResponseEntity(suggestTexts, HttpStatus.OK);
    }
}



