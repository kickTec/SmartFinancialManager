package com.kenick.generate.dao;

import com.kenick.generate.bean.UserFund;
import com.kenick.generate.bean.UserFundExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserFundMapper {
    long countByExample(UserFundExample example);

    int deleteByExample(UserFundExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserFund record);

    int insertSelective(UserFund record);

    List<UserFund> selectByExample(UserFundExample example);

    UserFund selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserFund record, @Param("example") UserFundExample example);

    int updateByExample(@Param("record") UserFund record, @Param("example") UserFundExample example);

    int updateByPrimaryKeySelective(UserFund record);

    int updateByPrimaryKey(UserFund record);
    List<UserFund> selectFieldByExample(@Param("filedList") List<String> filedList, @Param("example") UserFundExample example);

    UserFund selectFieldByPrimaryKey(@Param("filedList") List<String> filedList, @Param("id") String id);


/* field added */}
