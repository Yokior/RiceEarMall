package com.rice.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rice.common.utils.PageUtils;
import com.rice.product.entity.SkuImagesEntity;

import java.util.Map;

/**
 * sku图片
 *
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-17 16:36:01
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

