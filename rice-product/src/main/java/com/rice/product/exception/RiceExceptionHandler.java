package com.rice.product.exception;

import com.rice.common.exception.BizCodeEnum;
import com.rice.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * @Description：
 * @Auther：Yokior
 * @Date：2024/4/3 14:11
 */
@ControllerAdvice
@Slf4j
public class RiceExceptionHandler
{

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public R validExceptionHandler(MethodArgumentNotValidException e)
    {
        log.error("数据校验异常>>>{},{}", e.getClass(), e.getMessage());

        BindingResult bindingResult = e.getBindingResult();
        HashMap<String, String> map = new HashMap<>();

        bindingResult.getFieldErrors().forEach(error ->
        {
            map.put(error.getField(), error.getDefaultMessage());
        });

        return R.error(BizCodeEnum.VAILD_EXCEPTION).put("data", map);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public R exceptionHandler(Exception e)
    {
        log.error("系统异常>>>{},{}", e.getClass(), e.getMessage());
        return R.error(BizCodeEnum.UNKNOW_EXCEPTION);
    }

}
