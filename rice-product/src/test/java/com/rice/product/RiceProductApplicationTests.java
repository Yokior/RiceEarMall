package com.rice.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rice.product.entity.BrandEntity;
import com.rice.product.service.BrandService;
import com.rice.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class RiceProductApplicationTests
{

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;


    @Test
    void testPath()
    {
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        System.out.println(Arrays.asList(catelogPath));
    }


    @Test
    void contextLoads()
    {
        BrandEntity brand = brandService.getById(9);
        System.out.println(brand);
    }

}
