package com.kenick.fund.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kenick.fund.service.AsyncService;
import com.kenick.util.AliSmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("asyncService")
public class AsyncServiceImpl implements AsyncService{
	private final Logger logger = LoggerFactory.getLogger(AsyncServiceImpl.class);
	
	@Async
	@Override
	public JSONObject aliSendSmsCode(String phone, String code) {
		JSONObject ret = null;
		try{
			ret = AliSmsUtil.aliSendSmsCode(phone, code);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return ret;
	}
	
}
