package com.rice.member.exception;

/**
 * @author Yokior
 * @description
 * @date 2024/5/28 13:16
 */
public class PhoneExistException extends Exception
{
    public PhoneExistException()
    {
        super("手机号已存在");
    }
}
