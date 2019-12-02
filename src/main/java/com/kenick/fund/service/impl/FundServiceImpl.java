package com.kenick.fund.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kenick.fund.service.FundService;
import com.kenick.generate.bean.Fund;
import com.kenick.generate.bean.FundExample;
import com.kenick.generate.dao.FundMapper;

@Service("fundService")
public class FundServiceImpl implements FundService{
	@Resource
	private FundMapper fundDao;

	@Override
	public List<Fund> findAll() {
		FundExample fundExample = new FundExample();
		fundExample.setOrderByClause(Fund.S_gainTotal+","+Fund.S_curGain);
		return fundDao.selectByExample(fundExample);
	}	
}
