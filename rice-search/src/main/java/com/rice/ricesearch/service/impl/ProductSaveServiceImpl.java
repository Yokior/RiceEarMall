package com.rice.ricesearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.rice.common.to.es.SkuEsModel;
import com.rice.ricesearch.config.RiceElasticSearchConfig;
import com.rice.ricesearch.constant.EsConstant;
import com.rice.ricesearch.service.ProductSaveService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author Yokior
 * @description
 * @date 2024/5/3 15:10
 */
@Service
public class ProductSaveServiceImpl implements ProductSaveService
{

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void productStatusUp(List<SkuEsModel> skuEsModels) throws IOException
    {
        // 在es中保存数据
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels)
        {
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String jsonString = JSON.toJSONString(skuEsModel);
            indexRequest.source(jsonString, XContentType.JSON);

            bulkRequest.add(indexRequest);
        }

        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RiceElasticSearchConfig.COMMON_OPTIONS);

    }
}
