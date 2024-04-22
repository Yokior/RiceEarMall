package com.rice.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rice.common.utils.PageUtils;
import com.rice.ware.entity.WareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:53:02
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);
}

