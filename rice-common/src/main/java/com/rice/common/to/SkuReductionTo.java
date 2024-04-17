package com.rice.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Yokior
 * @description
 * @date 2024/4/17 14:21
 */
@Data
public class SkuReductionTo
{
    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
