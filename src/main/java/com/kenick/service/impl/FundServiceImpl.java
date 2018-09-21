package com.kenick.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kenick.dao.FundDao;
import com.kenick.entity.Fund;
import com.kenick.service.FundService;

@Service("fundService")
public class FundServiceImpl implements FundService{
	@Resource
	private FundDao fundDao;

	@Override
	public List<Fund> findAll() {
		return fundDao.findAll();
	}	
}
