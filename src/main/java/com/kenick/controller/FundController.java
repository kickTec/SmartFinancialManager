package com.kenick.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kenick.fund.service.FundService;
import com.kenick.generate.bean.Fund;

@Controller
@RequestMapping("/fund")
public class FundController {
	
	@Resource
	private FundService fundService;
	
    @RequestMapping("/index.html")
    public String index(Model model){
    	List<Fund> fundList = fundService.findAll();
    	model.addAttribute("fundList", fundList);
        return "fundIndex";
    }
}