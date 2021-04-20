package com.mydemo.project.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;

/**
 * 拦截器
 * @author allen
 */
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取URL
        String requestURI = request.getRequestURI();
        String substring = requestURI.substring(1);

        int index = substring.indexOf("/");
        if(index != -1){
            substring = substring.substring(0,index);

        }

        HashSet<String> urls = (HashSet<String>)request.getSession().getAttribute("module");
        //account是否存在该用户的所有权限中
        boolean result = urls.stream().anyMatch(substring::equals);
        if(!result){
            response.sendRedirect("/");
        }
        return result;
    }
}
