package com.rice.product.feign;

import com.rice.common.to.es.SkuEsModel;
import com.rice.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Yokior
 * @description
 * @date 2024/5/3 15:27
 */
@FeignClient("rice-search")
public interface SearchFeignService
{
    @PostMapping("/search/save/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
