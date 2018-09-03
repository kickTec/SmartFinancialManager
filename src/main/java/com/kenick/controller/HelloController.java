package com.kenick.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kenick.service.BackgroundTaskService;

@Controller
public class HelloController {
	@Autowired
	private BackgroundTaskService bts;
	
	@RequestMapping(value="/hello",method=RequestMethod.GET)
	public String hello(Model model){
		model.addAttribute("name", "Dear");
		return "helloHtml";
	}
}