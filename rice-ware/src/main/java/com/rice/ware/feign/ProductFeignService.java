package com.rice.ware.feign;

import com.rice.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yokior
 * @description
 * @date 2024/4/22 14:19
 */
@FeignClient("rice-product")
public interface ProductFeignService
{
    @RequestMapping("/product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);
}
