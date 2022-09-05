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
	 * <功能详细描述>
	 * author: zhanggw
	 * 创建时间:  2020/1/11
	 * @return java.util.List<com.kenick.generate.bean.Fund>
	 * @see [类、类#方法、类#成员]
	 */
	List<Fund> findAllFundByCondition(Fund fundCondition, String orderBy);

	/**
	 * <一句话功能简述> 根据条件查询用户基金
	 * <功能详细描述> 
	 * author: zhanggw
	 * 创建时间:  2020/1/11
	 * @return java.util.List<com.kenick.generate.bean.UserFund>
	 * @see [类、类#方法、类#成员]
	 */
	List<UserFund> findAllUserFundByCondition(UserFund userFundCondition);
	
	/**
	 * <一句话功能简述> 获取展示理财信息
	 * <功能详细描述> 
	 * author: zhanggw
	 * 创建时间:  2022/1/4
	 */
    List<Fund> getShowFundList();
	
    /**
	 * <一句话功能简述> 获取所有理财信息
	 * <功能详细描述> 
	 * author: zhanggw
	 * 创建时间:  2022/1/4
	 */
	List<Fund> getAllFundList();

	/**
	 * <一句话功能简述> 热加载理财变动情况
	 * <功能详细描述> 
	 * author: zhanggw
	 * 创建时间:  2022/1/4
	 */
	void loadFundChangeHot();
	
	/**
	 * <一句话功能简述> 开始压缩理财数据
	 * <功能详细描述> 
	 * author: zhanggw
	 * 创建时间:  2022/1/5
	 * @param dayNum 最近多少天
	 * @param zipName 压缩文件夹名称
	 */
	void zipFundData(Integer dayNum, String zipName);

    JSONArray getShowFundJsonArray();

    JSONObject queryDetail(Integer fundType, String fundCode);

	JSONObject generateDayList(String fundCode);

    JSONObject queryDayDetail(Integer fundType, String fundCode, Date date);

	void saveFundJsonBackup();

}
