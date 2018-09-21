package com.kenick.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kenick.service.FundService;

@Controller
@RequestMapping("/fund")
public class FundController {
	
	@Resource
	private FundService fundService;
	
    @RequestMapping("/index.html")
    public String index(Model model){
        return "fundIndex";
    }
}