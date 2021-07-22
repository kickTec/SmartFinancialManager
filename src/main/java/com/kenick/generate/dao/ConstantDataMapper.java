package com.kenick.generate.dao;

import com.kenick.generate.bean.ConstantData;
import com.kenick.generate.bean.ConstantDataExample;

import java.util.List;

public interface ConstantDataMapper {
    long countByExample(ConstantDataExample example);

    int deleteByExample(ConstantDataExample example);

    int deleteByPrimaryKey(String constantId);

    int insert(ConstantData record);

    int insertSelective(ConstantData record);

    List<ConstantData> selectByExample(ConstantDataExample example);

    ConstantData selectByPrimaryKey(String constantId);


/* field added */}
