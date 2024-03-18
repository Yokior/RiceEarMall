package com.rice.coupon.dao;

import com.rice.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:29:23
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
