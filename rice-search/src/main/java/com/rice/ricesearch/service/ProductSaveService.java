package com.rice.ricesearch.service;

import com.rice.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author Yokior
 * @description
 * @date 2024/5/3 15:08
 */
public interface ProductSaveService
{
    void productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
