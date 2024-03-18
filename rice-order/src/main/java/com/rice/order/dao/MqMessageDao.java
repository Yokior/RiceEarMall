package com.rice.order.dao;

import com.rice.order.entity.MqMessageEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:49:28
 */
@Mapper
public interface MqMessageDao extends BaseMapper<MqMessageEntity> {
	
}
