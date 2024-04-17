package com.rice.product.feign;

import com.rice.common.to.SkuReductionTo;
import com.rice.common.to.SpuBoundTo;
import com.rice.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Yokior
 * @description
 * @date 2024/4/17 14:11
 */
@FeignClient("rice-coupon")
public interface CouponFeignService
{
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveInfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
