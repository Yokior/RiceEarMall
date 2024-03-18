package com.rice.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rice.product.entity.BrandEntity;
import com.rice.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RiceProductApplicationTests
{

    @Autowired
    private BrandService brandService;

    @Test
    void contextLoads()
    {
        BrandEntity brand = brandService.getById(9);
        System.out.println(brand);
    }

}
