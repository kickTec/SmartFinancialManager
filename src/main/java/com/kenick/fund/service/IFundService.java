package com.kenick.fund.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kenick.fund.bean.Fund;
import com.kenick.user.bean.UserFund;

import java.util.Date;
import java.util.List;

public interface IFundService {

    /**
     * <一句话功能简述> 查询所有基金信息
     * author: zhanggw
     * 创建时间:  2020/1/11
     */
    List<Fund> findAllFundByCondition(Fund fundCondition, String orderBy);

    /**
     * <一句话功能简述> 根据条件查询用户基金
     * author: zhanggw
     * 创建时间:  2020/1/11
     */
    List<UserFund> findAllUserFundByCondition(UserFund userFundCondition);

    /**
     * <一句话功能简述> 获取展示理财信息
     * author: zhanggw
     * 创建时间:  2022/1/4
     */
    List<Fund> getShowFundList();

    /**
     * <一句话功能简述> 获取所有理财信息
     * author: zhanggw
     * 创建时间:  2022/1/4
     */
    List<Fund> getAllFundList();

    JSONArray getShowFundJsonArray();

    JSONObject queryDetail(Integer fundType, String fundCode);

    JSONObject generateDayList(String fundCode);

    JSONObject queryDayDetail(Integer fundType, String fundCode, Date date);

    void saveFundJsonBackup();

    /**
     * 动态加载配置
     */
    void loadSmfConfig();

    /**
     * 获取累计收益率排名靠前
     */
    String getHighRank(int rank);

    /**
     * 获取累计收益率排名靠后
     */
    String getLowRank(int rank);

}
