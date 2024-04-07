package com.rice.common.exception;

/**
 * @Description：
 * @Auther：Yokior
 * @Date：2024/4/3 14:52
 */
public enum BizCodeEnum
{
    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    VAILD_EXCEPTION(10001,"参数校验异常"),
    TO_MANY_REQUEST(10002,"请求流量过大，请稍后再试"),
    SMS_CODE_EXCEPTION(10003, "验证码获取频率太高，请稍后再试");

    private Integer code;

    private String msg;

    BizCodeEnum(Integer code, String msg)
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
