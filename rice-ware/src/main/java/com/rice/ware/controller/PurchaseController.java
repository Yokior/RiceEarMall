package com.rice.ware.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

import com.rice.ware.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rice.ware.entity.PurchaseEntity;
import com.rice.ware.service.PurchaseService;
import com.rice.common.utils.PageUtils;
import com.rice.common.utils.R;


/**
 * 采购信息
 *
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:53:02
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController
{
    @Autowired
    private PurchaseService purchaseService;


    @PostMapping("/merge")
    public R merge(@RequestBody MergeVo mergeVo)
    {
        purchaseService.mergePurchase(mergeVo);

        return R.ok();
    }


    /**
     * 查询没有领取的采购单
     *
     * @param params
     * @return
     */
    @RequestMapping("/unreceive/list")
    public R unreceive(@RequestParam Map<String, Object> params)
    {
        PageUtils page = purchaseService.queryPageUnreceive(params);

        return R.ok().put("page", page);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params)
    {
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id)
    {
        PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase)
    {
        purchase.setCreateTime(LocalDateTime.now());
        purchase.setUpdateTime(LocalDateTime.now());
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase)
    {
        purchase.setUpdateTime(LocalDateTime.now());
        purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids)
    {
        purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
