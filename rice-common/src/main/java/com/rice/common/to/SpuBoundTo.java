package com.rice.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Yokior
 * @description
 * @date 2024/4/17 14:14
 */
@Data
public class SpuBoundTo
{
    private Long spuId;

    private BigDecimal buyBounds;

    private BigDecimal growBounds;
}
