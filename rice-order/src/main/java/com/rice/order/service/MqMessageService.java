package com.rice.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rice.common.utils.PageUtils;
import com.rice.order.entity.MqMessageEntity;

import java.util.Map;

/**
 * 
 *
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:49:28
 */
public interface MqMessageService extends IService<MqMessageEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

