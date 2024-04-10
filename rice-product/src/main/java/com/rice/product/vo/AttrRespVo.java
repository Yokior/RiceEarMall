package com.rice.product.vo;

import lombok.Data;

/**
 * @Description：
 * @Auther：Yokior
 * @Date：2024/4/8 16:52
 */
@Data
public class AttrRespVo extends AttrVo
{
    private String catelogName;

    private Long[] catelogPath;

    private String groupName;
}
