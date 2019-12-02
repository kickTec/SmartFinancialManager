package com.kenick.generate.dao;

import com.kenick.generate.bean.User;
import com.kenick.generate.bean.UserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);
    List<User> selectFieldByExample(@Param("filedList") List<String> filedList, @Param("example") UserExample example);


/* field added */}
