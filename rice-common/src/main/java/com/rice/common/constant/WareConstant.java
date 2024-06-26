package com.rice.common.constant;

/**
 * @author Yokior
 * @description
 * @date 2024/4/21 14:15
 */
public class WareConstant
{
    public enum PurchaseStatusEnum
    {
        CREATED(0, "新建"),
        ASSIGNED(1, "已分配"),
        RECEIVE(2, "已领取"),
        FINISHED(3, "已完成"),
        HASERROR(4, "有异常");


        private Integer code;

        private String msg;

        PurchaseStatusEnum(Integer code, String msg)
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

    public enum PurchaseDetailEnum
    {
        CREATED(0, "新建"),
        ASSIGNED(1, "已分配"),
        BUYING(2, "正在采购"),
        FINISHED(3, "已完成"),
        HASERROR(4, "采购失败");


        private Integer code;

        private String msg;

        PurchaseDetailEnum(Integer code, String msg)
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
