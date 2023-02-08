package com.kenick.fund.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kenick.fund.service.IFundService;
import com.kenick.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping("/fund")
public class FundController {

    private final static Logger logger = LoggerFactory.getLogger(FundController.class);

    @Autowired
    private HttpServletRequest httpServletRequest;
	
	@Autowired
	private IFundService fundService;

    @RequestMapping("/indexCache")
    public String indexCache(@RequestParam(value = "data",required = false) String data, Model model){
        logger.debug("FundController.indexCache in, param:{}",data);
        try{
            model.addAttribute("fundList", fundService.getShowFundJsonArray());
        }catch (Exception e){
            logger.error("获取展示理财信息异常",e);
        }
        return "fundIndex";
    }

    @RequestMapping("/pcIndex")
    public String pcIndex(@RequestParam(value = "data",required = false) String data, Model model){
        logger.debug("FundController.pcIndex in, param:{}",data);
        try{
            model.addAttribute("fundList", fundService.getShowFundJsonArray());
        }catch (Exception e){
            logger.error("获取展示理财信息异常",e);
        }
        return "pcIndex";
    }

    @RequestMapping("/pcFundTable")
    public String fundTable(@RequestParam(value = "data",required = false) String data, Model model){
        logger.debug("FundController.fundTable in, param:{}",data);
        try{
            model.addAttribute("fundList", fundService.getShowFundJsonArray());
        }catch (Exception e){
            logger.error("获取展示理财信息异常",e);
        }
        return "pcFundTable";
    }

    @RequestMapping("/phoneFundTable")
    public String fundTablePhone(@RequestParam(value = "data",required = false) String data, Model model){
        logger.debug("FundController.fundTablePhone in, param:{}",data);
        try{
            model.addAttribute("fundList", fundService.getShowFundJsonArray());
        }catch (Exception e){
            logger.error("获取展示理财信息异常",e);
        }
        return "phoneFundTable";
    }

    @RequestMapping("/phoneIndex")
    public String phoneIndex(@RequestParam(value = "data",required = false) String data, Model model){
        logger.debug("FundController.indexCache in, param:{}",data);
        try{
            model.addAttribute("fundList", fundService.getShowFundJsonArray());
        }catch (Exception e){
            logger.error("获取展示理财信息异常",e);
        }
        return "phoneIndex";
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
            JSONArray showFundJsonArray = fundService.getShowFundJsonArray();
            return HttpUtils.showSuccess(showFundJsonArray);
        }catch (Exception e){
            return HttpUtils.showException("fund_queryfundinfolist_exception","查询基金信息异常", e);
        }
    }

    @RequestMapping("/queryuserfundlist")
    @ResponseBody
    public String queryUserFundList(@RequestParam(value = "data",required = false) String data){
        logger.debug("FundController.queryUserFundList in, param:{}",data);
        try{
            JSONArray showFundJsonArray = fundService.getShowFundJsonArray();
            return HttpUtils.showSuccess(showFundJsonArray);
        }catch (Exception e){
            return HttpUtils.showException("fund_queryuserfundlist_exception","查询用户基金queryfundinfolist信息异常", e);
        }
    }

    @ResponseBody
    @RequestMapping("/zipFundData")
    public String zipFundData(@RequestParam(value = "data",required = false) String data){
        logger.debug("FundController.zipFundData in, param:{}",data);
        try{
            JSONObject paramJson = JSON.parseObject(data);
            Integer dayNum = paramJson.getInteger("dayNum"); // 最近多少天数据
            String zipName = paramJson.getString("zipName"); // zip文件名
            fundService.zipFundData(dayNum, zipName);
            return HttpUtils.showSuccess();
        }catch (Exception e){
            return HttpUtils.showException("fund_queryuserfundlist_exception","查询用户基金信息异常", e);
        }
    }

    @RequestMapping("/queryDetail")
    @ResponseBody
    public String queryDetail(@RequestParam(value = "data",required = false) String data){
        logger.debug("FundController.queryDetail in, param:{}",data);
        try{
            JSONObject paramJson = JSON.parseObject(data);
            String fundCode = paramJson.getString("fundCode");
            Integer fundType = paramJson.getInteger("fundType");
            JSONObject detail = fundService.queryDetail(fundType, fundCode);
            return HttpUtils.showSuccess(detail);
        }catch (Exception e){
            logger.error("queryDetail异常",e);
        }
        return HttpUtils.showSuccess();
    }

    @RequestMapping("/forwardDetail")
    public String forwardDetail(@RequestParam(value = "type",required = false) String type,
                                @RequestParam(value = "fundCode",required = false) String fundCode,
                                Model model){
        logger.debug("FundController.forwardDetail in, type:{},fundCode:{}", type, fundCode);
        try{
            int fundType = Integer.parseInt(type);
            JSONObject detail = fundService.queryDetail(fundType, fundCode);
            model.addAttribute("detail", detail);

            if(httpServletRequest != null){
                String userAgent = httpServletRequest.getHeader("user-agent");
                if(IndexController.isPc(userAgent)){
                    return "pcFundDetail";
                }else{
                    return "phoneFundDetail";
                }
            }
        }catch (Exception e){
            logger.error("forwardDetail异常",e);
        }
        return "pcFundDetail";
    }

    @RequestMapping("/generateDayList")
    @ResponseBody
    public String generateDayList(@RequestParam(value = "data",required = false) String data){
        logger.debug("FundController.generateDayList in, param:{}",data);
        try{
            long startTime = System.currentTimeMillis();
            JSONObject paramJson = JSON.parseObject(data);
            JSONObject detail = fundService.generateDayList(paramJson.getString("fundCode"));
            logger.debug("generateDayList花费时间:{}", System.currentTimeMillis()-startTime);
            return HttpUtils.showSuccess(detail);
        }catch (Exception e){
            logger.error("generateDayList异常",e);
        }
        return HttpUtils.showSuccess();
    }

    @RequestMapping("/queryDayDetail")
    @ResponseBody
    public String queryDayDetail(@RequestParam(value = "data",required = false) String data){
        logger.debug("FundController.queryDayDetail in, param:{}",data);
        try{
            JSONObject paramJson = JSON.parseObject(data);
            Integer fundType = paramJson.getInteger("fundType");
            String fundCode = paramJson.getString("fundCode");
            Date date = paramJson.getDate("date");
            JSONObject detail = fundService.queryDayDetail(fundType, fundCode, date);
            return HttpUtils.showSuccess(detail);
        }catch (Exception e){
            logger.error("queryDayDetail异常",e);
        }
        return HttpUtils.showSuccess();
    }

}
