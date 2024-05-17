package com.rice.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yokior
 * @description
 * @date 2024/5/14 19:04
 */
@Configuration
public class RedissonConfig
{
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson()
    {
        // 创建配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        // 根据配置创建Redisson
        return Redisson.create(config);
    }
}
