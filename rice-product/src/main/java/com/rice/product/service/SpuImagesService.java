package com.rice.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rice.common.utils.PageUtils;
import com.rice.product.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:10:00
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveImages(Long spuId, List<String> images);
}

