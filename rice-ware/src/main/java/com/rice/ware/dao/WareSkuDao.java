package com.rice.ware.dao;

import com.rice.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 * 
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:53:02
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addStock(Long skuId, Long wareId, @Param("skuNum") Integer skuNum);
}
