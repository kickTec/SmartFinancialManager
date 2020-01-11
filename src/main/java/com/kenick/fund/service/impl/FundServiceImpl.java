package com.kenick.fund.service.impl;

import com.kenick.fund.service.FundService;
import com.kenick.generate.bean.Fund;
import com.kenick.generate.bean.FundExample;
import com.kenick.generate.bean.UserFund;
import com.kenick.generate.bean.UserFundExample;
import com.kenick.generate.dao.FundMapper;
import com.kenick.generate.dao.UserFundMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("fundService")
public class FundServiceImpl implements FundService{
	@Resource
	private FundMapper fundMapper;

	@Resource
	private UserFundMapper userFundMapper;

	@Override
	public List<Fund> findAllFundByCondition(Fund fundCondition) {
		if(fundCondition == null){
			fundCondition = new Fund();
		}

		FundExample fundExample = new FundExample();
		FundExample.Criteria criteria = fundExample.createCriteria();
		if(StringUtils.isNotBlank(fundCondition.getFundCode())){
			criteria.andFundCodeEqualTo(fundCondition.getFundCode());
		}
		if(StringUtils.isNotBlank(fundCondition.getFundName())){
			criteria.andFundNameLike("%"+fundCondition.getFundName()+"%");
		}
		fundExample.setOrderByClause(Fund.S_gainTotal+","+Fund.S_curGain);
		return fundMapper.selectByExample(fundExample);
	}

	@Override
	public List<UserFund> findAllUserFundByCondition(UserFund userFundCondition) {
		if(userFundCondition == null){
			userFundCondition = new UserFund();
		}

		UserFundExample userFundExample = new UserFundExample();
		UserFundExample.Criteria criteria = userFundExample.createCriteria();
		if(StringUtils.isNotBlank(userFundCondition.getUserId())){
			criteria.andUserIdEqualTo(userFundCondition.getUserId());
		}
		if(StringUtils.isNotBlank(userFundCondition.getFundCode())){
			criteria.andFundCodeEqualTo(userFundCondition.getFundCode());
		}
		if(StringUtils.isNotBlank(userFundCondition.getUsername())){
			criteria.andUsernameLike("%"+userFundCondition.getUsername()+"%");
		}
		if(StringUtils.isNotBlank(userFundCondition.getFundName())){
			criteria.andFundNameLike("%"+userFundCondition.getFundName()+"%");
		}
		userFundExample.setOrderByClause(UserFund.S_gainTotal+","+UserFund.S_curGain+","+UserFund.S_curTime+" desc");
		return userFundMapper.selectByExample(userFundExample);
	}
}
