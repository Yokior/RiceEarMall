package com.rice.common.constant;

/**
 * @Description：
 * @Auther：Yokior
 * @Date：2024/4/10 14:32
 */
public class ProductConstant
{

    public enum AttrEnum
    {
        ATTR_TYPE_BASE(1, "基本属性"),
        ATTR_TYPE_SALE(0, "销售属性");

        private Integer code;

        private String msg;

        AttrEnum(Integer code, String msg)
        {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode()
        {
            return code;
        }

        public String getMsg()
        {
            return msg;
        }
    }

}
