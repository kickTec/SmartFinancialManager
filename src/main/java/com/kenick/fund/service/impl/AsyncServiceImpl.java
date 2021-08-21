package com.kenick.fund.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kenick.fund.service.IAsyncService;
import com.kenick.fund.service.IFileStorageSV;
import com.kenick.util.AliSmsUtil;
import com.kenick.util.DateUtils;
import com.kenick.util.FileUtil;
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
	public void persistentStockInfo(Date now, String fundCode, List<String> stockList) {
		try{
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

			String storePath = fileStorageService.getStorageHomePath() + File.separator + "history" + File.separator + fundCode; // 保存目录
			File storePathFile = new File(storePath);
			if(!storePathFile.exists()){
				storePathFile.mkdirs();
			}

			String day = DateUtils.getStrDate(now, "yyyy-MM-dd");
			File storeFile = new File(storePath + File.separator + fundCode + "_" + day + ".txt");
			FileUtil.persistentText(storeFile, stockList);
			stockList.clear();

			logger.debug("异步保存{}数据花费时间:{}!", fundCode, System.currentTimeMillis()-startTime);
		}catch (Exception e){
			logger.error("持久化历史数据异常!", e);
		}
	}

}
