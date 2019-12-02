package com.kenick.generate.dao;

import com.kenick.generate.bean.ConstantData;
import com.kenick.generate.bean.ConstantDataExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ConstantDataMapper {
    long countByExample(ConstantDataExample example);

    int deleteByExample(ConstantDataExample example);

    int deleteByPrimaryKey(String constantId);

    int insert(ConstantData record);

    int insertSelective(ConstantData record);

    List<ConstantData> selectByExample(ConstantDataExample example);

    ConstantData selectByPrimaryKey(String constantId);

    int updateByExampleSelective(@Param("record") ConstantData record, @Param("example") ConstantDataExample example);

    int updateByExample(@Param("record") ConstantData record, @Param("example") ConstantDataExample example);

    int updateByPrimaryKeySelective(ConstantData record);

    int updateByPrimaryKey(ConstantData record);
    List<ConstantData> selectFieldByExample(@Param("filedList") List<String> filedList, @Param("example") ConstantDataExample example);

    ConstantData selectFieldByPrimaryKey(@Param("filedList") List<String> filedList, @Param("constantId") String constantId);


/* field added */}
