package com.rice.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rice.common.utils.PageUtils;
import com.rice.product.entity.AttrEntity;
import com.rice.product.vo.AttrRespVo;
import com.rice.product.vo.AttrVo;

import java.util.Map;

/**
 * 商品属性
 *
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:10:00
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long categoryId,String attrType);

    void saveAttr(AttrVo attrVo);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attrVo);
}

