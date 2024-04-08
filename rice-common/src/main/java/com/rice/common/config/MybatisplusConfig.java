package com.rice.common.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description：
 * @Auther：Yokior
 * @Date：2024/4/7 16:33
 */
@Configuration
public class MybatisplusConfig
{
    // 分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor()
    {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setOverflow(false);

        return paginationInterceptor;
    }
}
