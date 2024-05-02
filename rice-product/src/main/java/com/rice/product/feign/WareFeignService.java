package com.rice.product.feign;

import com.rice.common.utils.R;
import com.rice.product.vo.SkuHasStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Yokior
 * @description
 * @date 2024/5/2 15:28
 */
@FeignClient("rice-ware")
public interface WareFeignService
{
    @PostMapping("/ware/waresku/hasstock")
    public R<List<SkuHasStockVo>> getSkuHasStock(@RequestBody List<Long> skuIds);
}
