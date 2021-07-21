package com.kenick.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kenick.fund.service.FileStorageService;
import com.kenick.fund.service.FundService;
import com.kenick.generate.bean.Fund;
import com.kenick.generate.bean.UserFund;
import com.kenick.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/fund")
public class FundController {

    private final static Logger logger = LoggerFactory.getLogger(FundController.class);
	
	@Autowired
	private FundService fundService;

	public static List<Fund> fundCacheList = new ArrayList<>(); // 使用缓存

    @Autowired
    private FileStorageService fileStorageService;
	
    @RequestMapping("/index.html")
    public String index(@RequestParam(value = "data",required = false) String data, Model model){
        logger.debug("FundController.index in, param:{}",data);
        try{
            Fund fundCondition = null;
            if(data != null){
                fundCondition = JSON.parseObject(data, Fund.class);
            }
            List<Fund> fundList = fundService.findAllFundByCondition(fundCondition, null);
            model.addAttribute("fundList", fundList);
        }catch (Exception e){
            logger.error("查询基金信息异常",e);
        }
        return "fundIndex";
    }

    @RequestMapping("/indexCache.html")
    public String indexCache(@RequestParam(value = "data",required = false) String data, Model model){
        logger.debug("FundController.index in, param:{}",data);
        try{
            Fund fundCondition = null;
            if(data != null){
                fundCondition = JSON.parseObject(data, Fund.class);
            }

            if(fundCacheList == null || fundCacheList.size() == 0){
                if("file".equals(fileStorageService.getStorageType())){
                    fundCacheList = fileStorageService.getFundListFromFile();
                }else{
                    fundCacheList = fundService.findAllFundByCondition(fundCondition, null);
                }
            }
            model.addAttribute("fundList", fundCacheList);
        }catch (Exception e){
            logger.error("查询基金信息异常",e);
        }
        return "fundIndex";
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
