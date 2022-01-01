package com.kenick.fund.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/")
	public String index(){
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

}
