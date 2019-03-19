package com.yunji.interceptor;

import com.yunji.model.User;
import com.yunji.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String URI = httpServletRequest.getRequestURI();
        if(URI.indexOf("Login")>0){
            return true;
        }
        String userId = httpServletRequest.getParameter("userId");
        if(!StringUtils.isEmpty(userId)){
            User user = userService.getUserInfo(userId);
            if(null!=user&&user.getUserId().equals(userId)){
                return true;
            }
        }
        httpServletResponse.sendRedirect("/www.baidu.com");

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }


}
