package com.kenick.fund.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.List;

public interface IAsyncService {

	JSONObject aliSendSmsCode(String phone, String code);

	void persistentStockInfo(Date now, Integer type, String fundCode, List<String> stockList);

}
