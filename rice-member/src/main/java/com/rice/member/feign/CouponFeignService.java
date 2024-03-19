package com.rice.member.feign;

import com.rice.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description：
 * @Auther：Yokior
 * @Date：2024/3/19 19:55
 */
@FeignClient("rice-coupon")
public interface CouponFeignService
{
    @RequestMapping("/coupon/coupon/member/list")
    public R memberCoupons();
}
