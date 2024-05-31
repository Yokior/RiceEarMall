package com.rice.riceauth.feign;

import com.rice.common.utils.R;
import com.rice.riceauth.vo.UserRegistVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Yokior
 * @description
 * @date 2024/5/30 13:14
 */
@FeignClient("rice-member")
public interface MemberFeignService
{
    @PostMapping("/member/member/regist")
    R regist(@RequestBody UserRegistVo vo);
}
