package com.kenick.fund.service;


import com.kenick.fund.bean.Fund;
import com.kenick.user.bean.UserFund;

import java.util.List;

public interface FundService {
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
}