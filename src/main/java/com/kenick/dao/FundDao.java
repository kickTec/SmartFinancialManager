package com.kenick.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kenick.entity.Fund;

public interface FundDao {
	int insert(Fund fund);
	
	int update(Fund fund);
	
	List<Fund> findAll();
	
	Fund getFundByCode(@Param("code") String code);
}
