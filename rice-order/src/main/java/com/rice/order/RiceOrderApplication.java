package com.rice.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.rice"})
public class RiceOrderApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(RiceOrderApplication.class, args);
    }

}
