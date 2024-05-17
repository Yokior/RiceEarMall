package com.rice.product.web;

import com.rice.product.entity.CategoryEntity;
import com.rice.product.service.CategoryService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @Autowired
    private RedissonClient redissonClient;


    @GetMapping({"/","/index.html"})
    public String indexPage(Model model)
    {
        // 查询所有的一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categories();
        model.addAttribute("categories", categoryEntities);

        return "index";
    }

    @GetMapping("/testLock")
    @ResponseBody
    public String testLock()
    {
        // 获取锁对象
        RLock myLock = redissonClient.getLock("myLock");

        // 加锁
        myLock.lock();
        try
        {
            System.out.println("加锁成功");
        }
        finally
        {
            myLock.unlock();
        }


        return "ok";
    }
}
