package com.kenick.fund.controller;

import com.alibaba.fastjson.JSONObject;
import com.kenick.fund.service.IGridSV;
import com.kenick.util.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <一句话功能简述> 网格
 * author: zhanggw
 * 创建时间:  2021/7/23
 */
@Controller
@RequestMapping("/grid")
public class GridController {

    private final static Logger logger = LoggerFactory.getLogger(GridController.class);

    @Autowired
    private IGridSV gridSV;

    @RequestMapping("/backTest")
    @ResponseBody
    public String backTest(@RequestParam(value = "data",required = false) String data){
        logger.debug("GridController.backTest in, param:{}",data);
        try{
            if(StringUtils.isBlank(data)){
                return HttpUtils.showSuccess();
            }
            String[] dataArray = data.split(",");
            String fundCode = dataArray[0];
            int dayNum = Integer.parseInt(dataArray[1]); // 最近几天
            Double initPrice = Double.parseDouble(dataArray[2]); // 初始基准价
            Double gridInterval = Double.parseDouble(dataArray[3]); // 网格差价
            int gridQuantity = Integer.parseInt(dataArray[4]); // 网格数量
            Integer gridMode = null; // 网格模式
            if(dataArray.length >= 6){
                gridMode = Integer.parseInt(dataArray[5]);
            }

            JSONObject ret = gridSV.backTest(fundCode, dayNum, initPrice, gridInterval, gridQuantity, gridMode);
            return ret.toJSONString();
        }catch (Exception e){
            logger.error("grid_backTest_exception 回测异常", e);
            return HttpUtils.showException("grid_backTest_exception","回测异常!", e);
        }
    }

}
