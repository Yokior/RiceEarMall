package com.rice.ricesearch;

import com.alibaba.fastjson.JSON;
import com.rice.ricesearch.config.RiceElasticSearchConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * @author Yokior
 * @description
 * @date 2024/4/27 14:50
 */
@SpringBootTest
public class SearchTest
{
    @Autowired
    private RestHighLevelClient client;

    @Test
    public void testSearch()
    {
        System.out.println(client);
    }


    @Test
    public void indexData() throws IOException
    {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
        User user = new User("张三", "男", 20);
        indexRequest.source(JSON.toJSONString(user), XContentType.JSON);
        // 执行保存操作
        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);

        System.out.println(response);
    }

    @Data
    @AllArgsConstructor
    class User
    {
        private String name;
        private String gender;
        private Integer age;
    }


    @Test
    public void searchData() throws IOException
    {
        SearchRequest searchRequest = new SearchRequest();
        // 指定索引
        searchRequest.indices("users");
        // 指定DSL
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        sourceBuilder.query(QueryBuilders.matchQuery("name", "张三"));

        // 聚合
        sourceBuilder.aggregation(AggregationBuilders.terms("ageAgg").field("age"));
        sourceBuilder.aggregation(AggregationBuilders.avg("balanceAvg").field("balance" ));

        searchRequest.source(sourceBuilder);

        // 指定检索
        SearchResponse response = client.search(searchRequest, RiceElasticSearchConfig.COMMON_OPTIONS);

        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();

        // 分析结果
        System.out.println(response.toString());
    }




}
