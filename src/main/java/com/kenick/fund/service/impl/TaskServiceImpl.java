package com.kenick.fund.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kenick.constant.TableStaticConstData;
import com.kenick.fund.bean.Fund;
import com.kenick.fund.service.IAsyncService;
import com.kenick.fund.service.IFileStorageSV;
import com.kenick.fund.service.ITaskService;
import com.kenick.util.BeanUtil;
import com.kenick.util.DateUtils;
import com.kenick.util.HttpRequestUtils;
import com.kenick.util.JarUtil;
import com.kenick.util.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("taskService")
@EnableScheduling
public class TaskServiceImpl implements ITaskService {
	private final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
	private String fundQueryUrl = "http://fundgz.1234567.com.cn/js/";
	private String stockSzUrl = "http://hq.sinajs.cn/list=sz";
	private String stockShUrl = "http://hq.sinajs.cn/list=sh";

	private Map<String, List<String>> stockHistoryMap = new HashMap<>(); // 历史数据暂存map
	private Map<String, Double> stockLastMap = new HashMap<>(); // 上次记录值
	private Map<String, JSONObject> smsSendDateMap = new HashMap<>();

	@Autowired
	private IAsyncService asyncService;

	@Autowired
	private IFileStorageSV fileStorageService;

	@Autowired
	public SpringContextUtil springContextUtil;

	@Value("${smf.version}")
	private String smfVersion;

	@Value("${storage.home.path}")
	private String storageHomePath;

	/**
	 * <一句话功能简述> 白天更新基金股票信息
	 * <功能详细描述> 
	 * author: zhanggw
	 * 创建时间:  2021/4/27
	 */
    @Scheduled(cron = "${cron.perfectFundInfo}")
    public void perfectFundInfo(){
    	try{
			Date now = new Date();

			// 通过缓存查询更新
			updateThroughCache(now);
			logger.debug("【{}】遍历理财一轮花费时间:{}", smfVersion, System.currentTimeMillis() - now.getTime());

			Runtime runtime = Runtime.getRuntime();
			long memory_max = runtime.maxMemory();
			long memory_total = runtime.totalMemory();
			long memory_free = runtime.freeMemory();
			logger.debug("最大可用内存:{} MB,预占总内存:{} MB,使用内存:{} MB,空闲内存:{} MB", memory_max/1024/1024,
					memory_total/1024/1024, (memory_total-memory_free)/1024/1024,memory_free/1024/1024);

		}catch (Exception e) {
    		logger.error("白天更新股票基金信息异常!", e);
		}
    }

	@Scheduled(cron = "0 0/10 16 * * ?")
	public void fourClockTask(){
		try{
			logger.debug("fourClockTask.in!");
			Date now = new Date();
			int weekNum = DateUtils.getWeekNum(now);
			if(weekNum == 6 || weekNum == 7){ // 周末跳过
				return;
			}

			// 保存个股记录数据
			for(Fund fund: FundServiceImpl.fundCacheList){
				String fundCode = fund.getFundCode();
				List<String> stockList = stockHistoryMap.get(fundCode);

				if(stockList != null && stockList.size() > 0){
					asyncService.persistentStockInfo(now, fundCode, stockList);
					stockList.clear();
					stockHistoryMap.remove(fundCode);
				}
			}

			// 每周5备份最近5天数据
			if(weekNum == 5){
				JarUtil.compressFundStorage(storageHomePath + File.separator + "history", 5);
			}

		}catch (Exception e) {
			logger.error("定时清理缓存异常!", e);
		}
	}

	private void updateThroughCache(Date now) {
		int weekNum = DateUtils.getWeekNum(now);
//		if(weekNum == 6 || weekNum == 7){ // 周末跳过
//			return;
//		}

		if(FundServiceImpl.fundCacheList == null || FundServiceImpl.fundCacheList.size()==0){
			FundServiceImpl.fundCacheList = fileStorageService.getFundListFromFile();
		}

		for(Fund fund:FundServiceImpl.fundCacheList){
			String fundCode = fund.getFundCode();

			// 完善基金股票信息
			perfectInfoByCodeCache(fund, now);

			Double currentValue = fund.getCurNetValue();
			if(currentValue == null || currentValue == 0){
				continue;
			}

			Double lastValue = stockLastMap.get(fundCode);
			if(lastValue != null && lastValue.equals(currentValue) ){
				continue;
			}

			List<String> stockList = stockHistoryMap.get(fundCode);
			stockList = stockList == null ? new ArrayList<>() : stockList;
			String storeInfo = fund.getCurTime() + "," + currentValue;
			stockList.add(storeInfo);
			stockLastMap.put(fundCode, currentValue);

			if(stockList.size() >= 20){
				// 保存个股记录数据
				asyncService.persistentStockInfo(now, fundCode, stockList);
				stockList.clear();
				stockHistoryMap.remove(fundCode);
			}else{
				stockHistoryMap.put(fundCode, stockList);
			}
		}
		perfectFundList(FundServiceImpl.fundCacheList);

		// 周期性保存所有记录
        if(DateUtils.isRightTimeBySecond(now, 5, 2)){
            fileStorageService.writeFundList2File(FundServiceImpl.fundCacheList);
            logger.debug("信息保存到本地完成!");
        }

	}

	// 完善基金信息
	private void perfectFundList(List<Fund> fundCacheList) {
		try{
			if(fundCacheList == null || fundCacheList.size() == 0){
				return;
			}

			for(Fund fund:fundCacheList){
				if(fund.getLastNetValue() == null){
					fund.setLastNetValue(0.0);
				}
				if(fund.getLastGain() == null){
					fund.setLastGain(0.0);
				}
				if(fund.getLastPriceLowest() == null){
					fund.setLastPriceLowest(0.0);
				}
				if(fund.getLastPriceHighest() == null){
					fund.setLastPriceHighest(0.0);
				}
			}
		}catch (Exception e){
			logger.error("完善基金股票信息异常!", e);
		}
	}

	// 只更新当前缓存中的list
	private void perfectInfoByCodeCache(Fund fund, Date now) {
		try{
			if(fund == null || StringUtils.isBlank(fund.getFundCode())){
				return;
			}

			Integer fundType = fund.getType();
			// 更新基金信息
			if(fundType == null || fundType == TableStaticConstData.TABLE_FUND_TYPE_FUND){
				updateFundInfo(fund, now);
			}

			// 获取股票信息
			if(fundType != null && (fundType == TableStaticConstData.TABLE_FUND_TYPE_STOCK
					||fundType == TableStaticConstData.TABLE_FUND_TYPE_STOCK_SH || fundType == TableStaticConstData.TABLE_FUND_TYPE_STOCK_SZ)){
				updateStockByHttp(fund, now);
			}

			// 发送短信
			sendSms(fund);
		}catch (Exception e) {
			logger.error("完成基金股票信息异常!", e);
		}
	}

    /**
	 * <一句话功能简述> 晚上更新昨日数据
	 * <功能详细描述> 
	 * author: zhanggw
	 * 创建时间:  2021/4/27
	 */
    @Scheduled(cron = "${cron.updateStockInfoNight}")
    public void updateStockInfoNight(){
        logger.debug("TaskServiceImpl.updateStockInfo in");
        try{
			int weekNum = DateUtils.getWeekNum(new Date());
			if(weekNum == 6 || weekNum == 7){ // 周末跳过
				return;
			}

            // 查询出所有基金股票
			List<Fund> fundList = fileStorageService.getFundListFromFile();

            for(Fund fund:fundList){
                perfectStockInfoNight(fund);
            }
			fileStorageService.writeFundList2File(fundList);
			FundServiceImpl.fundCacheList = null;
		}catch (Exception e) {
            logger.error("晚上更新股票信息异常!", e);
        }
    }

	private void perfectStockInfoNight(Fund fund) {
        try{
            Fund updateFund = new Fund();
            updateFund.setFundCode(fund.getFundCode());

            // 昨日、今日涨幅
            Double curGain = fund.getCurGain();
            updateFund.setLastGain(curGain);
            updateFund.setCurGain(0.0);

            // 昨日、今日净值
            updateFund.setLastNetValue(fund.getCurNetValue());
            updateFund.setCurNetValue(0.0);

            // 昨日、今日最高价
            updateFund.setLastPriceHighest(fund.getCurPriceHighest());
            updateFund.setCurPriceHighest(0.0);

            // 昨日，今日最低价
            updateFund.setLastPriceLowest(fund.getCurPriceLowest());
            updateFund.setCurPriceLowest(0.0); // 初始值

            // 累计涨幅，第二天
            BigDecimal gainTotal = new BigDecimal(curGain);
            gainTotal = gainTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
            updateFund.setGainTotal(gainTotal);

            if(fund.getType() != null && fund.getType() == 1){
                updateFund.setCurPriceLowest(0.0);
                updateFund.setCurPriceHighest(0.0);
            }
            updateFund.setModifyDate(new Date());

			new BeanUtil().copyProperties(fund, updateFund, false);
        }catch (Exception e){
            logger.error("晚上割接股票信息异常!", e);
        }
    }

    /**
     * 根据基金编码完善基金信息
     * @param fund 基金股票信息
     */
    private void perfectFundInfoByCode(Fund fund){
    	try{
			Fund updateFund = null;

			// 获取基金信息
			if(fund.getType() == null || fund.getType() == TableStaticConstData.TABLE_FUND_TYPE_FUND){
				// 通过jsoup获取昨日基金信息
				updateFund = getFundInfoByJsoup(fund.getFundCode());

				// 通过http获取最新基金信息
				Fund curFund = getFundByHttp(fund);
				if(updateFund !=null){
					if(curFund != null){
						Double curNetValue = curFund.getCurNetValue();
						if(fund.getCurPriceLowest() == null || curNetValue < fund.getCurPriceLowest()){
							updateFund.setCurPriceLowest(curNetValue);
						}
						if(fund.getCurPriceHighest() == null || curNetValue > fund.getCurPriceHighest()){
							updateFund.setCurPriceHighest(curNetValue);
						}
						updateFund.setCurGain(curFund.getCurGain());
						updateFund.setCurNetValue(curNetValue);
						updateFund.setGainTotal(BigDecimal.valueOf(updateFund.getLastGain()+updateFund.getCurGain()));
					}else{
						return;
					}
				}
			}

			// 获取股票信息
			if(fund.getType() != null && fund.getType() == TableStaticConstData.TABLE_FUND_TYPE_STOCK){
				updateFund = getStockInfoByHttp(fund);
			}

			if(updateFund == null){
				return;
			}

        	// 更新基金信息
			updateFund.setModifyDate(new Date());

			// 发送短信
            // sendSms(updateFund);
    	}catch (Exception e) {
    		logger.error(e.getMessage());
		}
    }

    // 获取股票信息 fundName curNetValueNum curPriceHighest curPriceLowest curTime CurGain
	private Fund getStockInfoByHttp(Fund databaseFund) {
		Fund fund = new Fund();
		String fundCode = databaseFund.getFundCode();
		if(StringUtils.isBlank(fundCode)){
			return null;
		}
		fund.setFundCode(fundCode);
		try{
			String url = null;

			// 股票类型
			url = stockShUrl + fundCode; // 默认上海
			if("00".equals(fundCode.substring(0,2)) ||  "200".equals(fundCode.substring(0,3)) || "300".equals(fundCode.substring(0,3))
				|| "123".equals(fundCode.substring(0,3))){ // 深圳
				url = stockSzUrl + fundCode;
			}

			// 获取最新净值和涨幅
			String retStr = HttpRequestUtils.httpGetString(url, StandardCharsets.UTF_8.name());
			logger.trace("爬取的最新股票数据为:{}", retStr);
			// var hq_str_sz000876="新 希 望,28.260,28.170,28.960,29.780,28.260,28.960,28.970,41558107,1210395218.230,2000,28.960,5700,28.950,1900,28.940,12100,28.930,1300,28.920,2400,28.970,5600,28.980,4600,28.990,4200,29.000,4100,29.010,2020-06-02,11:30:00,00";
			if(StringUtils.isNotBlank(retStr)){
				retStr = retStr.split("=")[1];
				retStr = retStr.replace("\"","").replace(";","");
				String[] stockInfoArray = retStr.split(",");
				String fundName = stockInfoArray[0]; // 名称
				String curNetValue = stockInfoArray[3]; // 当前价
				String curPriceHighest = stockInfoArray[4]; // 当前最高价
				String curPriceLowest = stockInfoArray[5]; // 当前最低价
				logger.debug("fundName：{}，curNetValue：{}，curPriceHighest：{}，curPriceLowest：{}", fundName, curNetValue, curPriceHighest, curPriceLowest);

				Double curNetValueNum = Double.valueOf(curNetValue);
				fund.setFundName(fundName);
				fund.setCurNetValue(curNetValueNum);
				fund.setCurPriceHighest(Double.valueOf(curPriceHighest));
				fund.setCurPriceLowest(Double.valueOf(curPriceLowest));
				Date now = new Date();
				fund.setModifyDate(now);
				fund.setCurTime(DateUtils.getStrDate(now, "MM-dd HH:mm"));
				if(databaseFund.getCreateDate() == null){
					fund.setCreateDate(now);
				}

				// 现在涨幅重新计算
				Double lastNetValue = databaseFund.getLastNetValue();
				if(lastNetValue != null){
					BigDecimal lastBd = new BigDecimal(lastNetValue);
					BigDecimal nowBd = new BigDecimal(curNetValueNum);
					BigDecimal retBd = nowBd.subtract(lastBd).multiply(new BigDecimal(100)).divide(lastBd, 2, BigDecimal.ROUND_HALF_UP);
					fund.setCurGain(retBd.doubleValue());

					if(databaseFund.getLastGain() != null){
						fund.setGainTotal(new BigDecimal(retBd.doubleValue() + databaseFund.getLastGain()));
					}else{
                        fund.setGainTotal(new BigDecimal(retBd.doubleValue()));
                    }
				}
			}
			return fund;
		}catch (Exception e) {
			logger.error("获取股票信息失败！", e.getCause());
			return null;
		}
	}

	/**
	 * <一句话功能简述> 
	 * <功能详细描述>
	 *   var hq_str_sz000876="新 希 望,28.260,28.170,28.960,29.780,28.260,28.960,28.970,41558107,1210395218.230,2000,28.960,5700,28.950,1900,28.940,12100,28.930,1300,28.920,2400,28.970,5600,28.980,4600,28.990,4200,29.000,4100,29.010,2020-06-02,11:30:00,00";
	 * author: zhanggw
	 * 创建时间:  2021/9/9
	 */
	private void updateStockByHttp(Fund fund, Date now) {
		try{
			if(fund == null || StringUtils.isBlank(fund.getFundCode())){
				return;
			}

			String fundCode = fund.getFundCode();
			Integer fundType = fund.getType();
			Integer fundState = fund.getFundState();

			// 股票类型
			String url = stockShUrl + fundCode;
			if("00".equals(fundCode.substring(0,2)) ||  "200".equals(fundCode.substring(0,3)) || "300".equals(fundCode.substring(0,3))){ // 深圳
				url = stockSzUrl + fundCode;
			}
			if(fundType == TableStaticConstData.TABLE_FUND_TYPE_STOCK_SZ){
				url = stockSzUrl + fundCode;
			}

			// 获取最新净值和涨幅
			String retStr = HttpRequestUtils.httpGetString(url, StandardCharsets.UTF_8.name());
			if(fundState != null && fundState == TableStaticConstData.TABLE_FUND_TYPE_STATE_VALID){
				logger.debug("{}最新数据为:{}", fund.getFundCode(), retStr);
			}
			if(StringUtils.isNotBlank(retStr)){
				String[] retArray = retStr.split("=");
				if(retArray.length < 2 || StringUtils.isBlank(retArray[1])){
					logger.error("{}最新数据异常:{}", fund.getFundCode(), retStr);
					return;
				}
				retStr = retArray[1];
				retStr = retStr.replace("\"","").replace(";","");
				String[] stockInfoArray = retStr.split(",");
				if(stockInfoArray.length < 32){
					logger.error("{}最新数据异常:{}", fund.getFundCode(), retStr);
					return;
				}
				String fundName = stockInfoArray[0]; // 名称
				String curNetValue = stockInfoArray[3]; // 当前价
				String curPriceHighest = stockInfoArray[4]; // 当前最高价
				String curPriceLowest = stockInfoArray[5]; // 当前最低价

				Double curNetValueNum = Double.valueOf(curNetValue);
				// fundName
				fund.setFundName(fundName);
				// curNetValue
				fund.setCurNetValue(curNetValueNum);
				// curPriceHighest
				fund.setCurPriceHighest(Double.valueOf(curPriceHighest));
				// curPriceLowest
				fund.setCurPriceLowest(Double.valueOf(curPriceLowest));
				// curTime
				fund.setCurTime(stockInfoArray[30] + " " + stockInfoArray[31]);
				fund.setModifyDate(now);

				// 现在涨幅
				BigDecimal curGainBd = BigDecimal.ZERO;
				if(fund.getCurPriceLowest() != null && fund.getCurPriceLowest() > 0){
					curGainBd = new BigDecimal(curNetValue).divide(new BigDecimal(fund.getCurPriceLowest()),4,BigDecimal.ROUND_HALF_UP)
							.subtract(new BigDecimal(1.0)).multiply(new BigDecimal(100));
				}

				Double lastNetValue = fund.getLastNetValue();
				// 现在涨幅重新计算
				if(lastNetValue != null && lastNetValue != 0){
					BigDecimal lastBd = new BigDecimal(lastNetValue);
					BigDecimal nowBd = new BigDecimal(curNetValueNum);
					curGainBd = nowBd.subtract(lastBd).multiply(new BigDecimal(100)).divide(lastBd, 2, BigDecimal.ROUND_HALF_UP);

					if(fund.getLastGain() == null){
						fund.setLastGain(0.0);
					}
				}

				// curGain
				fund.setCurGain(curGainBd.doubleValue());

				// gainTotal
				BigDecimal gainTotal = new BigDecimal(curGainBd.doubleValue() + fund.getLastGain());
				gainTotal = gainTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
				fund.setGainTotal(gainTotal);

				retArray = null; // 等待内存回收
			}
		}catch (Exception e) {
			logger.error("更新股票信息失败！", e);
		}
	}

	// 发送短信
    private void sendSms(Fund fund){
		try{
			if(fund == null){
				return;
			}

			// 基金股票信息不全，不发送短信
			String fundCode = fund.getFundCode();
			Double curNetValue = fund.getCurNetValue();
			Double curPriceLowest = fund.getCurPriceLowest();
			Double curPriceHighest = fund.getCurPriceHighest();
			if(StringUtils.isBlank(fundCode) || fund.getType() == null || fund.getCurGain() == null || fund.getLastGain() == null ||
					curNetValue == null || curNetValue <= 0 || curPriceHighest == null || curPriceHighest <= 0 || curPriceLowest == null || curPriceLowest <= 0){
				return;
			}

			// 更新发送短信时间
			Date now = new Date();
			JSONObject fundCodeJson = smsSendDateMap.get(fundCode);
			if(fundCodeJson == null){ // 初始化，并进入后续逻辑
				fundCodeJson = new JSONObject();
				fundCodeJson.put("lastSmsDate", now);
				fundCodeJson.put("curNetValue", curNetValue);
			}else{
				Date lastSmsDate = fundCodeJson.getDate("lastSmsDate");
				BigDecimal lastNetValueBd = fundCodeJson.getBigDecimal("curNetValue");
				if(now.before(DateUtils.timeCalendar(lastSmsDate, null, 30, null))){ // 短信发送间隔在30分钟内，直接返回，不进行后续逻辑
					return;
				}else{ // 短信发送间隔超过30分钟
					if(now.before(DateUtils.timeCalendar(lastSmsDate, 24, null, null))){ // 短信发送间隔在24小时内
						// 根据净值变动幅度决定是否发送短信
						BigDecimal retBD = lastNetValueBd.subtract(new BigDecimal(curNetValue)).divide(lastNetValueBd, 2, RoundingMode.HALF_UP);
						if(retBD.compareTo(new BigDecimal(0.03)) >= 0 || retBD.compareTo(new BigDecimal(-0.03)) <= 0){ // 净值变动幅度超过3%，更新并进入后续逻辑
							fundCodeJson.put("lastSmsDate", now);
							fundCodeJson.put("curNetValue", curNetValue);
						}else{ // 净值变动幅度未超过10%，直接返回，不进入后续逻辑
							return;
						}
					}else{ // 短信发送间隔超过24小时，更新并进入后续逻辑
						fundCodeJson.put("lastSmsDate", now);
						fundCodeJson.put("curNetValue", curNetValue);
					}
				}
			}
			smsSendDateMap.put(fundCode, fundCodeJson);

			// 周末不发送
			boolean sendFlag = false;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			if(calendar.get(Calendar.DAY_OF_WEEK ) == Calendar.SATURDAY  || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
				return;
			}

			// 9点35前不发送
			calendar.setTime(now);
			calendar.set(Calendar.HOUR_OF_DAY, 9);
			calendar.set(Calendar.MINUTE, 35);
			Date startDate = calendar.getTime();
			if(now.before(startDate)){
				return;
			}

			// 15点后 不发送
			calendar.setTime(now);
			calendar.set(Calendar.HOUR_OF_DAY, 15);
			calendar.set(Calendar.MINUTE, 0);
			Date endDate = calendar.getTime();
			if(now.after(endDate)){
				return;
			}

			// 两日涨幅超过指定间隔
			double sumGain = fund.getCurGain() + fund.getLastGain();
			double twoDayDistance = 5.0;
			if(sumGain >= twoDayDistance){
				sendFlag = true;
			}
			if(sumGain <= -twoDayDistance){
				sendFlag = true;
			}

			// 当前值距离最高值、最低值超过4个点
			double curHighDistance = 0.04;
			if((curPriceHighest - curNetValue)/curPriceHighest >= curHighDistance){
				sendFlag = true;
			}
			if((curNetValue - curPriceLowest)/curPriceLowest >= curHighDistance){
				sendFlag = true;
			}

			String sendPhone = "15910761260";
			if(sendFlag && StringUtils.isNotBlank(sendPhone)){

				String activeProfile = springContextUtil.getActiveProfile();
				if("local".equals(activeProfile) || "dev".equals(activeProfile) || "test".equals(activeProfile)){
					logger.debug("{}触发短信发生规则，当前非生产环境，不发送短信!", fundCode);
					return;
				}

				logger.debug("向{}发送短信:{}", sendPhone, fundCode);
				asyncService.aliSendSmsCode(sendPhone, fundCode);
			}
		}catch (Exception e){
			logger.error("发送短信异常!", e);
		}
	}

	// 根据基金编码获取基金信息
    private Fund getFundInfoByJsoup(String fundCode){
    	Fund fund = new Fund();
    	fund.setFundCode(fundCode);
    	
    	String fundName; // 基金名称
    	String curTime; // 当前估算时间
    	Double curNetValue = 0.0; // 当前估算净值
    	Double curGain = 0.0;
    	Double lastNetValue; // 上一日净值
    	Double lastGain; // 上一日涨幅
    	try {
			Connection connect = Jsoup.connect("http://fund.eastmoney.com/" + fundCode + ".html?spm=search");
			connect.timeout(500);
			Response response = connect.execute();
			Document doc = response.parse();
			
			// 基金名称
			Elements nameEles = doc.getElementsByClass("fundDetail-tit");
			fundName = nameEles.first().text();
			fund.setFundName(fundName);
			
			// 当前估算时间
			String timeStr = doc.getElementById("gz_gztime").text();
			curTime = timeStr.substring(4, timeStr.length()-1);
			fund.setCurTime(curTime);
			
			// 当前估算净值
			String curNetValueStr = doc.getElementById("gz_gsz").text();
			if(StringUtils.isNumeric(curNetValueStr)){
				curNetValue = Double.valueOf(curNetValueStr);
			}
			fund.setCurNetValue(curNetValue);
			
			// 当前估算涨幅
			String curGainStr = doc.getElementById("gz_gszzl").text();
			if(StringUtils.isNotBlank(curGainStr)){
				curGain	= Double.valueOf(curGainStr.substring(0,curGainStr.length()-1));
			}
			fund.setCurGain(curGain);
			
			// 上一日净值
			Elements lastValueInfos = doc.select(".fundInfoItem .dataOfFund .dataItem02 .dataNums span");
			lastNetValue = Double.valueOf(lastValueInfos.first().text());
			fund.setLastNetValue(lastNetValue);
			
			String lastAdd = lastValueInfos.last().text();
			lastGain = Double.valueOf(lastAdd.substring(0,lastAdd.length()-1));
			fund.setLastGain(lastGain);
			
			fund.setGainTotal(BigDecimal.valueOf(curGain + lastGain));
	    	return fund;
		} catch (Exception e) {
			logger.debug(e.getMessage());
			return null;
		}
    }
    
    // 获取最新预估涨幅和净值
    private Fund getFundByHttp(Fund databaseFund){
    	Fund fund = new Fund();
    	fund.setFundCode(databaseFund.getFundCode());
    	try{
    		// 获取最新净值和涨幅
        	Date now = new Date();
        	String url = fundQueryUrl + databaseFund.getFundCode()+".js?rt="+now.getTime();
        	String retStr = HttpRequestUtils.httpGetString(url, StandardCharsets.UTF_8.name());
        	logger.debug("爬取的最新基金数据为:{}", retStr);
        	// {"gztime":"2019-10-30 09:30","gszzl":"-0.66","fundcode":"519727","name":"交银成长30混合","dwjz":"1.4620","jzrq":"2019-10-29","gsz":"1.4524"}
			if(retStr != null){
				// fundName curNetValueNum curPriceHighest curPriceLowest curTime CurGain
				String retJsonStr = retStr.substring(8, retStr.length()-2);
				JSONObject retJson = JSONObject.parseObject(retJsonStr);
				fund.setFundName(retJson.getString("name"));
				fund.setCurNetValue(retJson.getDouble("gsz"));
				fund.setCurGain(retJson.getDouble("gszzl"));
				fund.setCurTime(retJson.getString("gztime").substring(5));
				fund.setLastNetValue(retJson.getDouble("dwjz"));
			}
        	fund.setLastGain(0.0);
        	fund.setGainTotal(BigDecimal.valueOf(fund.getLastGain()+fund.getCurGain()));
        	return fund;
    	}catch (Exception e) {
    		logger.error("获取基金信息失败", e);
    		return null;
		}
    }

	// 更新基金信息
	private void updateFundInfo(Fund fund, Date now){
		try{
			// 获取最新净值和涨幅
			String url = "http://fundgz.1234567.com.cn/js/"+fund.getFundCode()+".js?rt="+now.getTime();
			String retStr = HttpRequestUtils.httpGetString(url, StandardCharsets.UTF_8.name());

			if(retStr != null){
				String retJsonStr = retStr.substring(8, retStr.length()-2);
				JSONObject retJson = JSONObject.parseObject(retJsonStr);

				// fundName
				fund.setFundName(retJson.getString("name"));

				// curNetValue
				Double curNetValue = retJson.getDouble("gsz");
				fund.setCurNetValue(curNetValue);

				// curPriceHighest
				if(fund.getCurPriceHighest() == null || fund.getCurPriceHighest()==0 || curNetValue > fund.getCurPriceHighest()){
					fund.setCurPriceHighest(curNetValue);
				}

				// curPriceLowest
				if(fund.getCurPriceLowest() == null || fund.getCurPriceLowest() == 0 || curNetValue < fund.getCurPriceLowest()){
					fund.setCurPriceLowest(curNetValue);
				}

				// curTime
				fund.setCurTime(retJson.getString("gztime").substring(5));

				// curGain
				Double curGain = retJson.getDouble("gszzl");
				fund.setCurGain(curGain);

				if(fund.getLastGain() == null){
					fund.setLastGain(0.00);
				}

				// gainTotal
				BigDecimal gainTotal = new BigDecimal(fund.getLastGain() + curGain);
				gainTotal = gainTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
				fund.setGainTotal(gainTotal);
			}
		}catch (Exception e) {
			logger.error("获取基金信息失败");
		}
	}

    public static void main(String[] args) {
	}

}
