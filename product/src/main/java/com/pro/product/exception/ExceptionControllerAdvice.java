package com.pro.product.exception;

import com.pro.common.exception.BizCodeEnume;
import com.pro.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一的异常处理类
 */
/*@RestControllerAdvice=@ResponseBody+@ControllerAdvice*/
@Slf4j
@RestControllerAdvice(basePackages = "com.pro.product.controller")
public class ExceptionControllerAdvice {
    /**
     * 处理验证异常的方法
     * @ExceptionHandler(value = Exception.class) 获取要处理的异常类
     * @param e
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handlerValidExecption(MethodArgumentNotValidException e){
        Map<String,String> map = new HashMap<>();
        e.getFieldErrors().forEach((fieldError)->{
            map.put(fieldError.getField(),fieldError.getDefaultMessage());
        });
        return R.error(BizCodeEnume.VALID_EXCEPTION.getCode(), BizCodeEnume.VALID_EXCEPTION.getMsg())
                .put("data",map);
    }

    /**
     * 系统其他的异常处理
     * @param throwable
     * @return
     */
    @ExceptionHandler(Throwable.class)
    public R handlerExecption(Throwable throwable){
        log.error("错误信息：",throwable);
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg())
                .put("data",throwable.getMessage());
    }
}
