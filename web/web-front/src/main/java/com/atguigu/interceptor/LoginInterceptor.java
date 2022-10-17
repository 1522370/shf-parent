package com.atguigu.interceptor;

import com.alibaba.fastjson.JSON;
import com.atguigu.constant.AtguiguConstant;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 包名:com.atguigu.interceptor
 *
 * @author Leevi
 * 日期2022-10-10  10:23
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1. 判断是否已登录
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(AtguiguConstant.SessionConstant.USER_KEY);
        if (userInfo == null) {
            //2. 未登录，则跳转到登录页面
            //2.1 构建result对象
            Result<Object> result = Result.build(null, ResultCodeEnum.LOGIN_AUTH);
            //2.2 将result对象转成JSON字符串响应给前端
            response.getWriter().write(JSON.toJSONString(result));
            return false;
        }
        //3. 已登录
        return true;
    }
}
