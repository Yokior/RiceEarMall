package com.rice.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rice.product.entity.BrandEntity;
import com.rice.product.service.BrandService;
import com.rice.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Arrays;

@SpringBootTest
class RiceProductApplicationTests
{

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;


    @Test
    public void testRedis()
    {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

        ops.set("hello", "world");

        String word = ops.get("hello");
        System.out.println(word);
    }


    @Test
    public void testRedisson()
    {
        System.out.println(redissonClient);
    }


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
