package com.kenick.fund.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kenick.fund.bean.Fund;
import com.kenick.fund.service.IFundService;
import com.kenick.user.bean.UserFund;
import com.kenick.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/fund")
public class FundController {

    private final static Logger logger = LoggerFactory.getLogger(FundController.class);
	
	@Autowired
	private IFundService fundService;

    @RequestMapping("/indexCache")
    public String indexCache(@RequestParam(value = "data",required = false) String data, Model model){
        logger.debug("FundController.indexCache in, param:{}",data);
        try{
            model.addAttribute("fundList", fundService.getShowFundList());
        }catch (Exception e){
            logger.error("获取展示理财信息异常",e);
        }
        return "fundIndex";
    }

    @RequestMapping("/indexCacheAll")
    public String indexCacheAll(@RequestParam(value = "data",required = false) String data, Model model){
        logger.debug("FundController.indexCacheAll in, param:{}",data);
        try{
            model.addAttribute("fundList", fundService.getAllFundList());
        }catch (Exception e){
            logger.error("获取所有理财信息异常",e);
        }
        return "fundIndexAll";
    }

    @RequestMapping("/queryfundinfolist")
    @ResponseBody
    public String queryFundInfoList(@RequestParam(value = "data",required = false) String data){
        logger.debug("FundController.queryFundInfoList in, param:{}",data);
        try{
            JSONObject reqJson = JSON.parseObject(data);
            String orderBy = reqJson.getString("orderBy");
            Fund fundCondition = JSON.parseObject(data, Fund.class);
            return HttpUtils.showSuccess(fundService.findAllFundByCondition(fundCondition, orderBy));
        }catch (Exception e){
            return HttpUtils.showException("fund_queryfundinfolist_exception","查询基金信息异常", e);
        }
    }
    
    @RequestMapping("/queryuserfundlist")
    @ResponseBody
    public String queryUserFundList(@RequestParam(value = "data",required = false) String data){
        logger.debug("FundController.queryUserFundList in, param:{}",data);
        try{
            UserFund userFundCondition = null;
            if(data != null){
                userFundCondition = JSON.parseObject(data, UserFund.class);
            }
            return HttpUtils.showSuccess(fundService.findAllUserFundByCondition(userFundCondition));
        }catch (Exception e){
            return HttpUtils.showException("fund_queryuserfundlist_exception","查询用户基金信息异常", e);
        }
    }

}
