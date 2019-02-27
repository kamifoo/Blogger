package me.kamikid.blog.intercept;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        System.out.println(request.getSession().getAttribute("user"));
        if(request.getSession().getAttribute("user")==null){
            response.sendRedirect("/admin");
            return false;
        }
        return true;
    }
}
