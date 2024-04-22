package com.rice.ware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {"com.rice.ware.feign"})
@SpringBootApplication(scanBasePackages = {"com.rice"})
public class RiceWareApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(RiceWareApplication.class, args);
    }

}
