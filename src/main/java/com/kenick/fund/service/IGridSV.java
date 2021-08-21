package com.kenick.fund.service;


import com.alibaba.fastjson.JSONObject;

/**
 * author: zhanggw
 * 创建时间:  2021/7/23
 */
public interface IGridSV {

    JSONObject backTest(String fundCode, Integer dayNum, Double initPrice, Double gridInterval, Integer gridQuantity, Integer gridMode) throws Exception;

    JSONObject gridRank(int rankMode) throws Exception;

}
