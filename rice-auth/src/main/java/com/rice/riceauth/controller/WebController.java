package com.rice.riceauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yokior
 * @description
 * @date 2024/5/24 11:56
 */
@Controller
public class WebController
{
    @RequestMapping("/login")
    public String login()
    {
        return "login";
    }
}
