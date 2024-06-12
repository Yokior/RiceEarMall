package com.rice.ricecart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Yokior
 * @description
 * @date 2024/6/12 20:06
 */
@Controller
public class CartController
{
    @GetMapping("/cart/cartList")
    public String cartList()
    {
        return "cartList";
    }
}
