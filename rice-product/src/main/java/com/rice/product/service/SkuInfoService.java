package com.rice.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rice.common.utils.PageUtils;
import com.rice.product.entity.SkuInfoEntity;

import java.util.Map;

/**
 * sku信息
 *
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-17 16:36:01
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

