package com.rice.ricecart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {"com.rice.ricecart.feign"})
@SpringBootApplication
public class RiceCartApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(RiceCartApplication.class, args);
    }

}
