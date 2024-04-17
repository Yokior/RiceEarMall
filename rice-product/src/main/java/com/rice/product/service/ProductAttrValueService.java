package com.rice.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rice.common.utils.PageUtils;
import com.rice.product.entity.ProductAttrValueEntity;
import com.rice.product.vo.BaseAttrs;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:10:00
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveBaseAttr(Long spuId, List<BaseAttrs> baseAttrs);
}

