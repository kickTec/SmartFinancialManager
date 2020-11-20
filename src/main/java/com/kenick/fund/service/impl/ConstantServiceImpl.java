package com.kenick.fund.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kenick.fund.service.ConstantService;
import com.kenick.generate.bean.ConstantData;
import com.kenick.generate.dao.ConstantDataMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * author: zhanggw
 * 创建时间:  2020/11/20
 */
@Service("constantService")
public class ConstantServiceImpl implements ConstantService {

    @Resource
    private ConstantDataMapper constantDataMapper;

    @Override
    public JSONObject getConstantJsonById(String constantId) {
        ConstantData constantData = constantDataMapper.selectByPrimaryKey(constantId);

        if(constantData == null || StringUtils.isBlank(constantData.getConstantValue())) {
            return null;
        }

        return JSONObject.parseObject(constantData.getConstantValue());
    }

    @Override
    public void updateValueById(String constantId, String value) {
        ConstantData constantData = new ConstantData();
        constantData.setConstantId(constantId);
        constantData.setConstantValue(value);
        constantData.setModifyDate(new Date());
        constantDataMapper.updateByPrimaryKeySelective(constantData);
    }

}
