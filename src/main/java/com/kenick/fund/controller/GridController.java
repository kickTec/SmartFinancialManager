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

    /**
     * <一句话功能简述> 网格回测
     * <功能详细描述>
     * author: zhanggw
     * 创建时间:  2021/10/10
     */
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
            Double initPrice = Double.parseDouble(dataArray[2]); // 初始基准价 0 自动适配
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
    
    /**
     * <一句话功能简述> 网格回测周排名
     * <功能详细描述> 
     * author: zhanggw
     * 创建时间:  2021/10/10
     */
    @RequestMapping("/gridRank")
    @ResponseBody
    public String gridRank(@RequestParam(value = "data",required = false) String data){
        logger.debug("GridController.gridRank in, param:{}",data);
        try{
            int rankMode = 101; // 转债模式 101 最近1周 102 倒数第二周 直到
            if(StringUtils.isNotBlank(data)){
                String[] dataArray = data.split(",");
                if(dataArray.length > 0){
                    rankMode = Integer.parseInt(dataArray[0]); // 排行模式
                }
            }

            JSONObject ret = gridSV.gridRank(rankMode);
            return ret.toJSONString();
        }catch (Exception e){
            logger.error("grid_gridrank_exception 回测异常", e);
            return HttpUtils.showException("grid_gridrank_exception","回测异常!", e);
        }
    }
    
    /**
     * <一句话功能简述> 网格回测末位周排名
     * <功能详细描述> 
     * author: zhanggw
     * 创建时间:  2021/10/10
     */
    @RequestMapping("/findOutBad")
    @ResponseBody
    public String findOutBad(@RequestParam(value = "data",required = false) String data){
        logger.debug("GridController.findOutBad in, param:{}",data);
        try{
            int findMode = 202; // 挑选模式 202 最近2周 203 最近3周
            if(StringUtils.isNotBlank(data)){
                String[] dataArray = data.split(",");
                if(dataArray.length > 0){
                    findMode = Integer.parseInt(dataArray[0]);
                }
            }

            JSONObject ret = gridSV.findOutBad(findMode);
            return ret.toJSONString();
        }catch (Exception e){
            logger.error("grid_findoutbad_exception", e);
            return HttpUtils.showException("grid_findoutbad_exception","回测异常!", e);
        }
    }

    /**
     * <一句话功能简述> 网格回测周排名
     * <功能详细描述>
     * author: zhanggw
     * 创建时间:  2021/10/10
     */
    @RequestMapping("/findOutGood")
    @ResponseBody
    public String findOutGood(@RequestParam(value = "data",required = false) String data){
        logger.debug("GridController.findOutGood in, param:{}",data);
        try{
            int findMode = 202; // 挑选模式 202 最近2周 203 最近3周 204 最近4周
            double gridInterval = 1.0;
            int tradeQuantity = 10;
            if(StringUtils.isNotBlank(data)) {
                String[] dataArray = data.split(",");
                for(int i=0; i < dataArray.length; i++){
                    String arrayParam = dataArray[i];
                    if(i == 0){
                        findMode = Integer.parseInt(arrayParam);
                    }
                    if(i == 1){
                        gridInterval = Double.parseDouble(arrayParam);
                    }
                    if(i == 2){
                        gridInterval = Integer.parseInt(arrayParam);
                    }
                }
            }

            JSONObject ret = gridSV.findOutGood(findMode, gridInterval, tradeQuantity);
            return ret.toJSONString();
        }catch (Exception e){
            logger.error("grid_findoutgood_exception", e);
            return HttpUtils.showException("grid_findoutgood_exception","回测异常!", e);
        }
    }

}
