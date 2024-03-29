package com.rice.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rice.coupon.entity.CouponEntity;
import com.rice.coupon.service.CouponService;
import com.rice.common.utils.PageUtils;
import com.rice.common.utils.R;


/**
 * 优惠券信息
 *
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:29:23
 */
@RestController
@RequestMapping("coupon/coupon")
public class CouponController
{
    @Autowired
    private CouponService couponService;

    @RequestMapping("/member/list")
    public R memberCoupons()
    {
        CouponEntity coupon = new CouponEntity();
        coupon.setCouponName("啦啦啦");

        return R.ok().put("coupon",coupon);
    }



    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params)
    {
        PageUtils page = couponService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id)
    {
        CouponEntity coupon = couponService.getById(id);

        return R.ok().put("coupon", coupon);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CouponEntity coupon)
    {
        couponService.save(coupon);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CouponEntity coupon)
    {
        couponService.updateById(coupon);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids)
    {
        couponService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
