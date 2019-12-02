package com.kenick.generate.dao;

import com.kenick.generate.bean.Fund;
import com.kenick.generate.bean.FundExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FundMapper {
    long countByExample(FundExample example);

    int deleteByExample(FundExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Fund record);

    int insertSelective(Fund record);

    List<Fund> selectByExample(FundExample example);

    Fund selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Fund record, @Param("example") FundExample example);

    int updateByExample(@Param("record") Fund record, @Param("example") FundExample example);

    int updateByPrimaryKeySelective(Fund record);

    int updateByPrimaryKey(Fund record);
    List<Fund> selectFieldByExample(@Param("filedList") List<String> filedList, @Param("example") FundExample example);

    Fund selectFieldByPrimaryKey(@Param("filedList") List<String> filedList, @Param("id") String id);


/* field added */}
