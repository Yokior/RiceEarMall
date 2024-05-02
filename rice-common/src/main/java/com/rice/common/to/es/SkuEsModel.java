package com.rice.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Yokior
 * @description
 * @date 2024/4/30 19:02
 */
@Data
public class SkuEsModel
{
    // sku基本信息
    private Long skuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImg;
    // 销售属性
    private Long spuId;
    private String skuBrand;
    private String skuAttrsVals;
    // 库存信息
    private Integer skuStock;
    private Boolean hasStock;
    // 热度评分
    private Long hotScore;
    // 分类信息
    private Long catalogId;
    private String catalogName;
    // 品牌信息
    private Long brandId;
    private String brandName;
    private String brandImg;

    private List<Attrs> attrs;

    @Data
    public static class Attrs
    {
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
