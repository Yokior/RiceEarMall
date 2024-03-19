package com.rice.coupon;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RiceCouponApplicationTests
{

    @Value("${ppp}")
    private String username;

    @Test
    void contextLoads()
    {
        System.out.println(username);
    }

}
