package com.rice.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rice.ware.entity.PurchaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rice.common.utils.PageUtils;
import com.rice.common.utils.Query;

import com.rice.ware.dao.PurchaseDetailDao;
import com.rice.ware.entity.PurchaseDetailEntity;
import com.rice.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService
{

    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {

        LambdaQueryWrapper<PurchaseDetailEntity> lqw = new LambdaQueryWrapper<>();

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key))
        {
            lqw.and(l ->
            {
                l.eq(PurchaseDetailEntity::getId, key)
                        .or()
                        .eq(PurchaseDetailEntity::getPurchaseId, key);
            });
        }

        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status))
        {
            lqw.eq(PurchaseDetailEntity::getStatus, status);
        }


        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId))
        {
            lqw.eq(PurchaseDetailEntity::getWareId, wareId);
        }


        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                lqw
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseDetailEntity> listDetailByPurchaseId(PurchaseEntity item)
    {
        LambdaQueryWrapper<PurchaseDetailEntity> lqw = new LambdaQueryWrapper<>();

        lqw.eq(PurchaseDetailEntity::getPurchaseId, item.getId());

        List<PurchaseDetailEntity> purchaseDetailEntityList = this.list(lqw);


        return purchaseDetailEntityList;
    }

}