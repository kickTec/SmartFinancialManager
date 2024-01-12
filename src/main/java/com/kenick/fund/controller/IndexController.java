package com.kenick.fund.controller;


import com.kenick.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 根controller
 */
@Controller
public class IndexController {

    @Autowired
    private HttpServletRequest httpServletRequest;

    //首页
    @RequestMapping("/")
    public String index() {
        if (httpServletRequest != null) {
            String userAgent = httpServletRequest.getHeader("user-agent");
            if (HttpUtils.isPc(userAgent)) {
                return "forward:/fund/pcIndex";
            } else {
                return "forward:/fund/phoneIndex";
            }
        }
        return "forward:/fund/indexCache";
    }

    //查询所有信息
    @RequestMapping("/all")
    public String all() {
        return "forward:/fund/indexCacheAll";
    }

    //帮助信息
    @RequestMapping("/help")
    public String help() {
        return "readme";
    }

    //帮助信息
    @RequestMapping("/readme")
    public String readme() {
        return "readme";
    }

}