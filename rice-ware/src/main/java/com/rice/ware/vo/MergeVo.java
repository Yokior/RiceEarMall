package com.rice.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Yokior
 * @description
 * @date 2024/4/21 14:09
 */
@Data
public class MergeVo
{
    private Long purchaseId;

    private List<Long> items;
}
