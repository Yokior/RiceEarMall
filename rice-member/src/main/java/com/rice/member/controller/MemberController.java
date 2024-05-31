package com.rice.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.rice.common.exception.BizCodeEnum;
import com.rice.member.exception.PhoneExistException;
import com.rice.member.exception.UsernameExistException;
import com.rice.member.feign.CouponFeignService;
import com.rice.member.vo.MemberRegistVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rice.member.entity.MemberEntity;
import com.rice.member.service.MemberService;
import com.rice.common.utils.PageUtils;
import com.rice.common.utils.R;


/**
 * 会员
 *
 * @author yokior
 * @email 123456@123.com
 * @date 2024-03-18 16:42:00
 */
@RestController
@RequestMapping("member/member")
public class MemberController
{
    @Autowired
    private MemberService memberService;

    @Autowired
    private CouponFeignService couponFeignService;


    @PostMapping("/regist")
    public R regist(@RequestBody MemberRegistVo vo)
    {
        try
        {
            memberService.regist(vo);
        }
        catch (PhoneExistException e)
        {
            return R.error(BizCodeEnum.PHONE_EXIST_EXCEPTION);
        }
        catch (UsernameExistException e)
        {
            return R.error(BizCodeEnum.USER_EXIST_EXCEPTION);
        }

        return R.ok();
    }


    @RequestMapping("/coupons")
    public R test()
    {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("张三");

        R coupons = couponFeignService.memberCoupons();
        return R.ok().put("member", memberEntity).put("coupons", coupons);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params)
    {
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id)
    {
        MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member)
    {
        memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member)
    {
        memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids)
    {
        memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
