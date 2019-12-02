package com.kenick.fund.service;

import com.alibaba.fastjson.JSONObject;

public interface AsyncService {
	JSONObject aliSendSmsCode(String phone, String code);
}
