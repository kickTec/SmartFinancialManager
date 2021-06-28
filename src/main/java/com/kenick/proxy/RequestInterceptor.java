package com.kenick.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * author: zhanggw
 * 创建时间:  2021/1/26
 */
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private final static Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try{
            logger.debug("RequestInterceptor.in");
            List<String> normalList = new ArrayList<>();
            normalList.add("/");
            normalList.add("/fund/indexCache.html");
            normalList.add("/proxy/google");
            normalList.add("/error");
            String servletPath = request.getServletPath();
            logger.debug("servletPath:{}", servletPath);
            if(!normalList.contains(servletPath)){
                logger.debug("代理后url");
            }
        }catch (Exception e){
            logger.error("灰度拦截器异常!", e);
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

}
