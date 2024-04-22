package com.rice.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Yokior
 * @description
 * @date 2024/4/22 13:56
 */
@Data
public class PurchaseDoneVo
{
    private Long id;

    List<PurchaseItemDoneVo> items;
}
