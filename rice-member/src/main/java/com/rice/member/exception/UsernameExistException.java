package com.rice.member.exception;

/**
 * @author Yokior
 * @description
 * @date 2024/5/28 13:17
 */
public class UsernameExistException extends Exception
{
    public UsernameExistException()
    {
        super("用户名已存在");
    }
}
