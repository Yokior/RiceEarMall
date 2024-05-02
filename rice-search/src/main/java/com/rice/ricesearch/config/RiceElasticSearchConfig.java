package com.rice.ricesearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yokior
 * @description
 * @date 2024/4/27 14:27
 */
@Configuration
public class RiceElasticSearchConfig
{

    public static final RequestOptions COMMON_OPTIONS;

    static
    {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        COMMON_OPTIONS = builder.build();
    }


    @Bean
    public RestHighLevelClient esRestClient()
    {
        return new RestHighLevelClient(RestClient.builder(new HttpHost("192.168.223.128", 9200, "http")));
    }

}
