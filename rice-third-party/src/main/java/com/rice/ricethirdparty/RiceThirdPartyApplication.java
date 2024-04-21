package com.rice.ricethirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.rice"})
public class RiceThirdPartyApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(RiceThirdPartyApplication.class, args);
    }

}
