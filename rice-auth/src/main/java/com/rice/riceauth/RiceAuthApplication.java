package com.rice.riceauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {"com.rice.riceauth.feign"})
@SpringBootApplication
public class RiceAuthApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(RiceAuthApplication.class, args);
    }

}
