package com.lee.learn.house;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.learn.house.domain.HouseIndexTemplate;
import com.lee.learn.house.domain.HouseSuggest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/16.
 */
@Component
public class HouseIndexOperator {
    public static final String INDEX_NAME = "house";
    public static final String INDEX_TYPE = "ershoufang";

    private Logger logger = LoggerFactory.getLogger(HouseIndexOperator.class);

    @Autowired
    private TransportClient client;
    @Autowired
    private ObjectMapper objectMapper;

    public boolean createIndex(HouseIndexTemplate house) {
        try {
            this.updateSuggest(house);
//            XContentBuilder content = XContentFactory.jsonBuilder().startObject();
//            Class<?> houseTemClass = HouseIndexTemplate.class;
//            List<Field> fieldList = new ArrayList<>() ;
//            while (houseTemClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
//                fieldList.addAll(Arrays.asList(houseTemClass .getDeclaredFields()));
//                houseTemClass = houseTemClass.getSuperclass(); //得到父类,然后赋给自己
//            }
//            for (Field field : fieldList) {
//                field.setAccessible(true);
//                String name = field.getName();
//                Object value = field.get(house);
//                content.field(name, value);
//            }
//            content.endObject();
//            IndexResponse response = client.prepareIndex(INDEX_NAME, INDEX_TYPE).setSource(content).get();
            IndexResponse response = this.client.prepareIndex(INDEX_NAME, INDEX_TYPE)
                    .setSource(objectMapper.writeValueAsBytes(house), XContentType.JSON).get();

            System.out.println(response.getId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


    public boolean updateSuggest(HouseIndexTemplate template) {
        AnalyzeRequestBuilder arBuilder = new AnalyzeRequestBuilder(
                this.client, AnalyzeAction.INSTANCE, INDEX_NAME, template.getTitle(), template.getXiaoQu(), template.getHuXing(),
                template.getJingJiRen());
        arBuilder.setAnalyzer("ik_smart");
        AnalyzeResponse response = arBuilder.get();
        List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();
        if (tokens == null) {
            logger.info("house-{} suggest tokes is null", template.getHouseId());
            return false;
        }
        List<HouseSuggest> houseSuggests = new ArrayList<>();
        tokens.forEach(token -> {
            if (token.getType().equals("<NUM>") || token.getTerm().length() < 2) {
                return;
            }
            HouseSuggest houseSuggest = new HouseSuggest();
            houseSuggest.setInput(token.getTerm());
            houseSuggests.add(houseSuggest);
        });

        template.setSuggest(houseSuggests);
        return true;
    }


}
