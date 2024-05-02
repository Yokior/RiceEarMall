package com.rice.ricesearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.rice"}, exclude = {DataSourceAutoConfiguration.class})
public class RiceSearchApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(RiceSearchApplication.class, args);
    }

}
