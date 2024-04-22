package com.rice.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rice.common.utils.PageUtils;
import com.rice.ware.entity.PurchaseDetailEntity;
import com.rice.ware.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:53:02
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PurchaseDetailEntity> listDetailByPurchaseId(PurchaseEntity item);
}

