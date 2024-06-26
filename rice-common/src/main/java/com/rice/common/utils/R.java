/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.rice.common.utils;

import com.rice.common.exception.BizCodeEnum;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class R<T> extends HashMap<String, Object>
{
    private static final long serialVersionUID = 1L;

    private T data;

    public T getData()
    {
        return data;
    }

    public R setData(T data)
    {
        this.data = data;
        return this;
    }

    public R()
    {
        put("code", 0);
        put("msg", "success");
    }

    public Integer getCode()
    {
        // object转integer
        return (Integer) this.get("code");
    }


    public static R error()
    {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static R error(BizCodeEnum bizCodeEnum)
    {
        return error(bizCodeEnum.getCode(), bizCodeEnum.getMsg());
    }

    public static R error(String msg)
    {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static R error(int code, String msg)
    {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(BizCodeEnum bizCodeEnum)
    {
        return error(bizCodeEnum.getCode(), bizCodeEnum.getMsg());
    }

    public static R ok(String msg)
    {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map)
    {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok()
    {
        return new R();
    }

    public R put(String key, Object value)
    {
        super.put(key, value);
        return this;
    }

    public String getMsg()
    {
        return (String) this.get("msg");
    }
}
