package com.rice.riceauth.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author Yokior
 * @description
 * @date 2024/5/27 17:08
 */
@Data
public class UserRegistVo
{
    @NotEmpty(message = "用户名不能为空")
    @Length(min = 4, max = 20, message = "用户名长度必须在4到20之间")
    private String userName;
    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度必须在6到20之间")
    private String password;
    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
//    @NotEmpty(message = "验证码不能为空")
    private String code;

    @Override
    public String toString()
    {
        return "UserRegistVo{" +
                "username='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
