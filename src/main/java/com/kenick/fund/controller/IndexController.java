package com.kenick.fund.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/")
	public String index(){
		return "forward:/fund/indexCache.html";
	}

	@RequestMapping("/readme")
	public String readme(){
		return "readme";
	}

}
