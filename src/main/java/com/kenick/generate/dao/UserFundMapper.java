package com.kenick.generate.dao;

import com.kenick.generate.bean.UserFund;
import com.kenick.generate.bean.UserFundExample;
import com.kenick.generate.bean.UserFundKey;

import java.util.List;

public interface UserFundMapper {
    long countByExample(UserFundExample example);

    int deleteByExample(UserFundExample example);

    int deleteByPrimaryKey(UserFundKey key);

    int insert(UserFund record);

    int insertSelective(UserFund record);

    List<UserFund> selectByExample(UserFundExample example);

    UserFund selectByPrimaryKey(UserFundKey key);


/* field added */}
