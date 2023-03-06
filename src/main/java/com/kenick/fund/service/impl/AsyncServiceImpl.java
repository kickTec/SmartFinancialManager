package com.kenick.fund.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kenick.fund.service.IAsyncService;
import com.kenick.fund.service.IFileStorageSV;
import com.kenick.util.AliSmsUtil;
import com.kenick.util.DateUtils;
import com.kenick.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service("asyncService")
public class AsyncServiceImpl implements IAsyncService {

	private final Logger logger = LoggerFactory.getLogger(AsyncServiceImpl.class);

	@Autowired
	private IFileStorageSV fileStorageService;
	
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

	@Override
	public void persistentStockInfo(Date now, Integer type, String fundCode, List<String> stockList) {
		try{
			if(StringUtils.isBlank(fundCode) || stockList == null || stockList.size()==0){
				return;
			}

			long startTime = System.currentTimeMillis();
			if(!fileStorageService.getStorageEnable()){
				return;
			}

			int hour = DateUtils.getHour(now);
			int minute = DateUtils.getMinute(now);
			if(hour < 9 || (hour < 10 && minute < 30)){
				return;
			}

			if(hour >= 15 && minute >= 1){
				return;
			}

			// 保存目录
			String storePath = fileStorageService.getStorageHomePath() + File.separator + "history" + File.separator + fundCode;
			if("000001".equals(fundCode) && type == 4){
				storePath = fileStorageService.getStorageHomePath() + File.separator + "history" + File.separator + "sh" + fundCode;
			}

			File storePathFile = new File(storePath);
			if(!storePathFile.exists()){
				storePathFile.mkdirs();
			}

			String day = DateUtils.getStrDate(now, "yyyy-MM-dd");
			File storeFile = new File(storePath + File.separator + fundCode + "_" + day + ".txt");
			FileUtil.persistentText(storeFile, stockList);

			logger.trace("异步保存{}数据花费时间:{}!", fundCode, System.currentTimeMillis()-startTime);
		}catch (Exception e){
			logger.error("持久化历史数据异常!", e);
		}
	}

}
