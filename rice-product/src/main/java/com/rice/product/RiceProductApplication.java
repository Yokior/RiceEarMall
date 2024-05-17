package com.rice.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients(basePackages = {"com.rice.product.feign"})
@SpringBootApplication(scanBasePackages = {"com.rice"})
@EnableTransactionManagement
@EnableCaching
public class RiceProductApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(RiceProductApplication.class, args);
    }

}
