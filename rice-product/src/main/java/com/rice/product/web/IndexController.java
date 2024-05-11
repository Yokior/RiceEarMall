package com.rice.product.web;

import com.rice.product.entity.CategoryEntity;
import com.rice.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author Yokior
 * @description
 * @date 2024/5/6 13:33
 */
@Controller
public class IndexController
{

    @Autowired
    private CategoryService categoryService;

    @GetMapping({"/","/index.html"})
    public String indexPage(Model model)
    {
        // 查询所有的一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categories();
        model.addAttribute("categories", categoryEntities);

        return "index";
    }
}
