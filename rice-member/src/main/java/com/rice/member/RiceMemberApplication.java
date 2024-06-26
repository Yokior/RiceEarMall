package com.rice.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {"com.rice.member.feign"})
@SpringBootApplication(scanBasePackages = {"com.rice"})
public class RiceMemberApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(RiceMemberApplication.class, args);
    }

}
