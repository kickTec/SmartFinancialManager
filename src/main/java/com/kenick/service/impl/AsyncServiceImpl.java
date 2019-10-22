package com.kenick.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.kenick.service.AsyncService;
import com.kenick.util.AliSmsUtil;

@Service("asyncService")
public class AsyncServiceImpl implements AsyncService{
	private final static Logger logger = LoggerFactory.getLogger(AsyncServiceImpl.class);
	
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
