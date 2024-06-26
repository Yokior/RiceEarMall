package com.rice.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import com.rice.common.to.SkuReductionTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rice.coupon.entity.SkuFullReductionEntity;
import com.rice.coupon.service.SkuFullReductionService;
import com.rice.common.utils.PageUtils;
import com.rice.common.utils.R;


/**
 * 商品满减信息
 *
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:29:23
 */
@RestController
@RequestMapping("coupon/skufullreduction")
public class SkuFullReductionController
{
    @Autowired
    private SkuFullReductionService skuFullReductionService;

    /**
     * 列表
     */
    @PostMapping("/saveInfo")
    public R saveInfo(@RequestBody SkuReductionTo skuReductionTo)
    {

        skuFullReductionService.saveSkuReduction(skuReductionTo);

        return R.ok();
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params)
    {
        PageUtils page = skuFullReductionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id)
    {
        SkuFullReductionEntity skuFullReduction = skuFullReductionService.getById(id);

        return R.ok().put("skuFullReduction", skuFullReduction);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SkuFullReductionEntity skuFullReduction)
    {
        skuFullReductionService.save(skuFullReduction);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SkuFullReductionEntity skuFullReduction)
    {
        skuFullReductionService.updateById(skuFullReduction);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids)
    {
        skuFullReductionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
