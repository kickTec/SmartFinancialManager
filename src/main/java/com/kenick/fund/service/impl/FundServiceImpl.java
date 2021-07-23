package com.kenick.fund.service.impl;

import com.kenick.fund.bean.Fund;
import com.kenick.fund.service.FundService;
import com.kenick.user.bean.UserFund;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("fundService")
public class FundServiceImpl implements FundService{

	@Override
	public List<Fund> findAllFundByCondition(Fund fundCondition, String orderBy) {

		return null;
	}

	@Override
	public List<UserFund> findAllUserFundByCondition(UserFund userFundCondition) {

		return null;
	}

}
