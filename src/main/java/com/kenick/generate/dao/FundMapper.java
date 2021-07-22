package com.kenick.generate.dao;

import com.kenick.generate.bean.Fund;
import com.kenick.generate.bean.FundExample;

import java.util.List;

public interface FundMapper {
    long countByExample(FundExample example);

    int deleteByExample(FundExample example);

    int deleteByPrimaryKey(String fundCode);

    int insert(Fund record);

    int insertSelective(Fund record);

    List<Fund> selectByExample(FundExample example);

    Fund selectByPrimaryKey(String fundCode);

/* field added */}
