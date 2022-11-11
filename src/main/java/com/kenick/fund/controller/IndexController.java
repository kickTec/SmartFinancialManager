package com.kenick.fund.controller;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Controller
public class IndexController {

	private final static Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping("/")
	public String index(){
		if(httpServletRequest != null){
			String userAgent = httpServletRequest.getHeader("user-agent");
			if(isPc(userAgent)){
				return "forward:/fund/pcIndex";
			}else{
				return "forward:/fund/phoneIndex";
			}
		}
		return "forward:/fund/indexCache";
	}

	@RequestMapping("/all")
	public String all(){
		return "forward:/fund/indexCacheAll";
	}

	@RequestMapping("/readme")
	public String readme(){
		return "readme";
	}


	private boolean isPc(String userAgent){
		if(StringUtils.isBlank(userAgent)){
			return false;
		}
		String[] androidDescArray = {"Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"};
		for(String androidDesc:androidDescArray){
			if(userAgent.contains(androidDesc)){
				return false;
			}
		}
		return true;
	}

}
