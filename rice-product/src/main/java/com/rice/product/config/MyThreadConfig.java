package com.rice.product.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author Yokior
 * @description
 * @date 2024/5/23 12:42
 */
@Configuration
public class MyThreadConfig
{
    @Autowired
    private ThreadPoolConfigProperties threadPoolConfigProperties;

    @Bean
    public ThreadPoolExecutor threadPoolExecutor()
    {
        return new ThreadPoolExecutor(threadPoolConfigProperties.getCoreSize(), threadPoolConfigProperties.getMaxSize(),
                threadPoolConfigProperties.getKeepAliveTime(), TimeUnit.SECONDS, new LinkedBlockingDeque<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
                );
    }
}
