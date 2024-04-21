package com.rice.ware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.rice"})
public class RiceWareApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(RiceWareApplication.class, args);
    }

}
