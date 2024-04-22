package com.rice.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rice.common.constant.WareConstant;
import com.rice.ware.entity.PurchaseDetailEntity;
import com.rice.ware.service.PurchaseDetailService;
import com.rice.ware.service.WareSkuService;
import com.rice.ware.vo.MergeVo;
import com.rice.ware.vo.PurchaseDoneVo;
import com.rice.ware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rice.common.utils.PageUtils;
import com.rice.common.utils.Query;

import com.rice.ware.dao.PurchaseDao;
import com.rice.ware.entity.PurchaseEntity;
import com.rice.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService
{

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params)
    {

        LambdaQueryWrapper<PurchaseEntity> lqw = new LambdaQueryWrapper<>();

        lqw.eq(PurchaseEntity::getStatus, 0)
                .or()
                .eq(PurchaseEntity::getStatus, 1);

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params), lqw

        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo)
    {
        Long purchaseId = mergeVo.getPurchaseId();

        if (purchaseId == null)
        {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(LocalDateTime.now());
            purchaseEntity.setUpdateTime(LocalDateTime.now());
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());

            this.save(purchaseEntity);

            purchaseId = purchaseEntity.getId();
        }

        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(i ->
        {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(i);
            detailEntity.setPurchaseId(finalPurchaseId);
            detailEntity.setStatus(WareConstant.PurchaseDetailEnum.ASSIGNED.getCode());

            return detailEntity;
        })
                .collect(Collectors.toList());

        purchaseDetailService.updateBatchById(collect);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(LocalDateTime.now());
        this.updateById(purchaseEntity);
    }

    @Override
    public void receivePurchase(List<Long> ids)
    {
        // 确认当前采购单是新建或者已分配状态
        List<PurchaseEntity> purchaseList = this.listByIds(ids);
        List<PurchaseEntity> filterList = purchaseList.stream()
                .filter(p ->
                {
                    return p.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                            p.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode();
                })
                .peek(p1 -> {
                    p1.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
                    p1.setUpdateTime(LocalDateTime.now());
                })
                .collect(Collectors.toList());

        // 改变采购单的状态
        this.updateBatchById(filterList);

        // 改变采购项的状态
        filterList.forEach(item -> {
            List<PurchaseDetailEntity> entities = purchaseDetailService.listDetailByPurchaseId(item);
            List<PurchaseDetailEntity> collect = entities.stream()
                    .map(e ->
                    {
                        PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
                        detailEntity.setId(e.getId());
                        detailEntity.setStatus(WareConstant.PurchaseDetailEnum.BUYING.getCode());
                        return detailEntity;
                    })
                    .collect(Collectors.toList());

            purchaseDetailService.updateBatchById(collect);
        });



    }

    @Transactional
    @Override
    public void done(PurchaseDoneVo vo)
    {
        // 改变采购单的状态
        Long id = vo.getId();

        // 改变采购项的状态
        Boolean flag = true;
        List<PurchaseItemDoneVo> items = vo.getItems();
        ArrayList<PurchaseDetailEntity> detailList = new ArrayList<>();
        for (PurchaseItemDoneVo item : items)
        {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if (item.getStatus() == WareConstant.PurchaseStatusEnum.HASERROR.getCode())
            {
                flag = false;
                detailEntity.setStatus(item.getStatus());
            }
            else
            {
                detailEntity.setStatus(WareConstant.PurchaseStatusEnum.FINISHED.getCode());
                // 成功采购的进行入库
                PurchaseDetailEntity entity = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(entity.getSkuId(),entity.getWareId(),entity.getSkuNum());
            }

            detailEntity.setId(item.getItemId());
            detailList.add(detailEntity);
        }

        purchaseDetailService.updateBatchById(detailList);

        // 将成功采购的进行入库
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag ? WareConstant.PurchaseStatusEnum.FINISHED.getCode() : WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(LocalDateTime.now());
        this.updateById(purchaseEntity);


    }

}