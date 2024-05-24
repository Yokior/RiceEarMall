package com.rice.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Yokior
 * @description
 * @date 2024/5/23 12:46
 */
@ConfigurationProperties(prefix = "ricemall.thread")
@Component
@Data
public class ThreadPoolConfigProperties
{
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;
}
