package com.kenick.fund.service;

import com.alibaba.fastjson.JSONObject;

/**
 * author: zhanggw
 * 创建时间:  2020/11/20
 */
public interface ConstantService {

    JSONObject getConstantJsonById(String constantId);

    void updateValueById(String smsSendRule, String toJSONString);
}
