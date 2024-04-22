package com.rice.ware.vo;

import lombok.Data;

/**
 * @author Yokior
 * @description
 * @date 2024/4/22 13:57
 */
@Data
public class PurchaseItemDoneVo
{
    private Long itemId;

    private Integer status;

    private String reason;
}
