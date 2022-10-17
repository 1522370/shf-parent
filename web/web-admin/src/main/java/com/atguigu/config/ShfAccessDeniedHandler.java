package com.atguigu.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 包名:com.atguigu.config
 *
 * @author Leevi
 * 日期2022-10-13  10:31
 */
@Component
public class ShfAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException, ServletException {
        //你可以编写任何代码处理权限不够的情况: httpServletRequest请求对象,httpServletResponse响应对象,e异常对象
        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/auth");
    }
}
