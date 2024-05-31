package com.rice.riceauth.controller;

import com.alibaba.fastjson.JSON;
import com.rice.common.utils.R;
import com.rice.riceauth.feign.MemberFeignService;
import com.rice.riceauth.vo.UserLoginVo;
import com.rice.riceauth.vo.UserRegistVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Yokior
 * @description
 * @date 2024/5/24 11:56
 */
@Controller
@Slf4j
public class LoginController
{

    @Autowired
    private MemberFeignService memberFeignService;


    @PostMapping("/userlogin")
    public String userlogin(UserLoginVo vo, RedirectAttributes redirectAttributes)
    {
        R r = memberFeignService.userlogin(vo);
        if (r.getCode() == 0)
        {
            return "redirect:https://www.baidu.com";
        }
        else
        {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("msg", r.getMsg());
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/login";
        }

    }



    @PostMapping("/regist")
    public String register(@Validated UserRegistVo vo, BindingResult result, RedirectAttributes redirectAttributes, HttpSession session)
    {

        if (result.hasErrors())
        {
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,
                    (oldValue, newValue) -> newValue
                    ));

            redirectAttributes.addFlashAttribute("errors", errors);
            // 校验出错
            return "redirect:/register";
        }

        // 校验验证码

        // 通过后注册
        R r = memberFeignService.regist(vo);
        if (r.getCode() == 0)
        {
            // 注册成功 回到登录页
            return "redirect:/login";
        }
        else
        {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("msg", r.getMsg());
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/register";
        }


    }
}
