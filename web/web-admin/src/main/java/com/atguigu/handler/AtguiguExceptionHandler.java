package com.atguigu.handler;

import com.alibaba.fastjson.JSON;
import com.atguigu.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 包名:com.atguigu.handler
 *
 * @author Leevi
 * 日期2022-10-05  15:25
 */
//@ControllerAdvice
public class AtguiguExceptionHandler {

    private static final String PAGE_ERROR = "common/error";

    @ExceptionHandler({RuntimeException.class})
    public String handleRuntimeException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        //1. 区分当前请求是同步请求还是异步请求
        if (request.getHeader("accept").contains("application/json")) {
            //异步请求
            Result<Object> result = Result.build(null, 201, e.getMessage());
            response.getWriter().write(JSON.toJSONString(result));
            return null;
        }else {
            //同步请求
            //异常信息存储到请求域
            request.setAttribute("errorMessage",e.getMessage());
            //返回异常页面的逻辑视图
            return PAGE_ERROR;
        }
    }
}
