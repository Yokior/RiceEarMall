package com.rice.ricesearch.controller;

import com.rice.common.exception.BizCodeEnum;
import com.rice.common.to.es.SkuEsModel;
import com.rice.common.utils.R;
import com.rice.ricesearch.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author Yokior
 * @description
 * @date 2024/5/3 15:06
 */
@Slf4j
@RestController
@RequestMapping("/search/save")
public class ElasticSaveController
{

    @Autowired
    private ProductSaveService productSaveService;

    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels)
    {
        try
        {
            productSaveService.productStatusUp(skuEsModels);
        }
        catch (IOException e)
        {
            log.error("ElasticSaveController.productStatusUp发生异常:{}",e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION);
        }

        return R.ok();
    }
}
