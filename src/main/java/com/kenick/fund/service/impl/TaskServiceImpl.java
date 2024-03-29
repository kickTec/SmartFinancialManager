package com.kenick.fund.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kenick.constant.SysConstantData;
import com.kenick.constant.TableStaticConstData;
import com.kenick.controller.FundController;
import com.kenick.fund.service.ConstantService;
import com.kenick.fund.service.FileStorageService;
import com.kenick.fund.service.TaskService;
import com.kenick.generate.bean.Fund;
import com.kenick.generate.bean.FundExample;
import com.kenick.generate.bean.UserFund;
import com.kenick.generate.bean.UserFundExample;
import com.kenick.generate.dao.FundMapper;
import com.kenick.generate.dao.UserFundMapper;
import com.kenick.util.BeanUtil;
import com.kenick.util.DateUtils;
import com.kenick.util.HttpRequestUtils;
import com.kenick.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service("taskService")
@Configurable
@EnableScheduling
public class TaskServiceImpl implements TaskService{	
	private final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
	private Date lastSendDate = new Date();

	private final String fundQueryUrl = "http://fundgz.1234567.com.cn/js/";
	private final String stockSzUrl = "http://hq.sinajs.cn/list=sz";
	private final String stockShUrl = "http://hq.sinajs.cn/list=sh";

	@Resource
	private AsyncServiceImpl asyncService;

	@Resource
	private FundMapper fundMapper;

	@Resource
	private UserFundMapper userFundMapper;

	@Resource
	private ConstantService constantService;

	@Autowired
	private FileStorageService fileStorageService;

	/**
	 * <一句话功能简述>  白天更新基金股票信息
	 * <功能详细描述> 
	 * author: zhanggw
	 * 创建时间:  2021/4/27
	 */
    @Scheduled(cron = "${cron.perfectFundInfo}")
    public void perfectFundInfo(){
    	try{
			Date now = new Date();

			// 通过mysql查询更新，较占资源
			// updateThroughMysql();

			// 通过缓存查询更新
			updateThroughCache(now);

        	 logger.debug("遍历理财一轮花费时间:{}", System.currentTimeMillis()-now.getTime());
    	}catch (Exception e) {
    		logger.error("白天更新股票基金信息异常!", e);
		}
    }

	private void updateThroughCache(Date now) {

		String storageType = fileStorageService.getStorageType();
    	if(FundController.fundCacheList == null || FundController.fundCacheList.size()==0){

			if("file".equals(storageType)){
				FundController.fundCacheList = fileStorageService.getFundListFromFile();
			}else{
				FundExample fundExample = new FundExample();
				fundExample.or().andFundStateEqualTo(1);
				FundController.fundCacheList = fundMapper.selectByExample(fundExample);
			}
		}

		for(Fund fund:FundController.fundCacheList){
			perfectInfoByCodeCache(fund, now);

			if(DateUtils.isRightTimeBySecond(now, 30,10)){
				fundMapper.updateByPrimaryKeySelective(fund);
			}
		}
		perfectFundList(FundController.fundCacheList);

		// 本地文件保存方式，每隔3分钟保存一次
        if("file".equals(storageType) && DateUtils.isRightTimeBySecond(now, 1, 4)){
            fileStorageService.writeFundList2File(FundController.fundCacheList);
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

			// 更新基金信息
			if(fund.getType() == null || fund.getType() == TableStaticConstData.TABLE_FUND_TYPE_FUND){
				updateFundInfo(fund, now);
			}

			// 获取股票信息
			if(fund.getType() != null && fund.getType() == TableStaticConstData.TABLE_FUND_TYPE_STOCK){
				updateStockByHttp(fund, now);
			}
		}catch (Exception e) {
			logger.error("完成基金股票信息异常!", e);
		}
	}

	// 通过mysql查询更新，较占资源
	private void updateThroughMysql(){
		// 查询出所有基金编码
		FundExample fundExample = new FundExample();
		fundExample.or().andFundStateEqualTo(1);
		fundExample.setOrderByClause(Fund.S_type);
		List<Fund> fundList = fundMapper.selectByExample(fundExample);
		for(Fund fund:fundList){
			perfectFundInfoByCode(fund);
		}
	}

    /**
	 * <一句话功能简述> 1.晚上更新昨日数据 2.移除当天短信发送记录
	 * <功能详细描述> 
	 * author: zhanggw
	 * 创建时间:  2021/4/27
	 */
    @Scheduled(cron = "${cron.updateStockInfoNight}")
    public void updateStockInfoNight(){
        logger.debug("TaskServiceImpl.updateStockInfo in");
        try{
            // 查询出所有基金股票
			String storageType = fileStorageService.getStorageType();
			List<Fund> fundList;
			if("file".equals(storageType)){
				fundList = fileStorageService.getFundListFromFile();
			}else{
				FundExample fundExample = new FundExample();
				fundExample.or().andFundStateEqualTo(1);
				fundList = fundMapper.selectByExample(fundExample);
			}

            for(Fund fund:fundList){
                perfectStockInfoNight(fund);
            }
			fileStorageService.writeFundList2File(fundList);
            FundController.fundCacheList = null;

            // 晚上移除发送短信记录
			// removeSmsInfo();

		}catch (Exception e) {
            logger.error("晚上更新股票信息异常!", e);
        }
    }

	private void removeSmsInfo() {
		JSONObject smsRuleJson = constantService.getConstantJsonById(SysConstantData.SMS_SEND_RULE);
		Set<String> smsKeySet = smsRuleJson.keySet();
		for(String key:smsKeySet){
			if(key.contains("TodaySendFlag_")){
				smsRuleJson.put(key, false);
			}
		}
		constantService.updateValueById(SysConstantData.SMS_SEND_RULE, smsRuleJson.toJSONString());
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

            if("file".equals(fileStorageService.getStorageType())){
				new BeanUtil().copyProperties(fund, updateFund, false);
			}else{
				fundMapper.updateByPrimaryKeySelective(updateFund);
			}
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
        	fundMapper.updateByPrimaryKeySelective(updateFund);

        	// 更新用户基金信息
			UserFund userFund = JsonUtils.copyObjToBean(updateFund, UserFund.class);
			UserFundExample userFundExample = new UserFundExample();
			userFundExample.or().andFundCodeEqualTo(updateFund.getFundCode());
			userFundMapper.updateByExampleSelective(userFund, userFundExample);

			// 发送短信
            Fund newFund = fundMapper.selectByPrimaryKey(updateFund.getFundCode());
            sendSms(newFund);
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
			if("00".equals(fundCode.substring(0,2)) ||  "200".equals(fundCode.substring(0,3)) || "300".equals(fundCode.substring(0,3))){ // 深圳
				url = stockSzUrl + fundCode;
			}

			if("60".equals(fundCode.substring(0,2)) ||  "900".equals(fundCode.substring(0,3))){ // 上海
				url = stockShUrl + fundCode;
			}

			// 获取最新净值和涨幅
			String retStr = HttpRequestUtils.httpGetString(url, StandardCharsets.UTF_8.name());
			// logger.debug("爬取的最新股票数据为:{}", retStr);
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

	// 更新股票信息 fundName curNetValueNum curPriceHighest curPriceLowest curTime CurGain
	private void updateStockByHttp(Fund fund, Date now) {

		if(fund == null || StringUtils.isBlank(fund.getFundCode())){
			return;
		}
		
		try{
			String url = null;
			String fundCode = fund.getFundCode();

			// 股票类型
			if("00".equals(fundCode.substring(0,2)) ||  "200".equals(fundCode.substring(0,3)) || "300".equals(fundCode.substring(0,3))){ // 深圳
				url = stockSzUrl + fundCode;
			}

			if("60".equals(fundCode.substring(0,2)) ||  "900".equals(fundCode.substring(0,3))){ // 上海
				url = stockShUrl + fundCode;
			}

			// 获取最新净值和涨幅
			String retStr = HttpRequestUtils.httpGetString(url, StandardCharsets.UTF_8.name());
			// logger.trace("最新股票数据为:{}", retStr);
			// var hq_str_sz000876="新 希 望,28.260,28.170,28.960,29.780,28.260,28.960,28.970,41558107,1210395218.230,2000,28.960,5700,28.950,1900,28.940,12100,28.930,1300,28.920,2400,28.970,5600,28.980,4600,28.990,4200,29.000,4100,29.010,2020-06-02,11:30:00,00";
			if(StringUtils.isNotBlank(retStr)){
				retStr = retStr.split("=")[1];
				retStr = retStr.replace("\"","").replace(";","");
				String[] stockInfoArray = retStr.split(",");
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
				fund.setCurTime(DateUtils.getStrDate(now, "MM-dd HH:mm"));
				fund.setModifyDate(now);

				// 现在涨幅
				BigDecimal curGainBd = new BigDecimal(curNetValue).divide(new BigDecimal(curPriceLowest),4,BigDecimal.ROUND_HALF_UP)
						.subtract(new BigDecimal(1.0)).multiply(new BigDecimal(100));
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
			}
		}catch (Exception e) {
			logger.error("更新股票信息失败！", e);
		}
	}

	// 发送短信
    private void sendSms(Fund fund){
		JSONObject smsSendRuleJson = constantService.getConstantJsonById(SysConstantData.SMS_SEND_RULE);

		Integer type = fund.getType();
		String fundCode = fund.getFundCode();

		// 基金股票信息不全，不发送短信
		if(smsSendRuleJson == null || StringUtils.isBlank(fundCode) || type == null || fund.getCurGain() == null || fund.getLastGain() == null){
			return;
		}

		if(!smsSendRuleJson.getBooleanValue("sendFlag")){ // 发送短信总开关
			return;
		}

		String sendPhone = smsSendRuleJson.getString("sendPhone");  // 必须有发送手机号
		if(StringUtils.isBlank(sendPhone)){
			return;
		}

		String fundSendFlagKey = "TodaySendFlag_"+fundCode; 	// 单个基金或股票是否已发送
		boolean fundSendFlag = smsSendRuleJson.getBooleanValue(fundSendFlagKey);
		if(fundSendFlag){
			return;
		}

		// 周末不发送
		boolean sendFlag = false;
		Date now = new Date();
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
		
		// 发送短信间隔
		Integer sendInterval = smsSendRuleJson.getInteger("sendInterval");
		if(sendInterval != null){
			calendar.setTime(lastSendDate);
			calendar.add(Calendar.MINUTE, sendInterval);
			Date intervalAfter = calendar.getTime();
			if(now.before(intervalAfter)){
				return;
			}
		}

    	// 基金两日上涨幅度超过最大值
		double sumGain = fund.getCurGain() + fund.getLastGain();
		Double fundUpperLimit = smsSendRuleJson.getDouble("fund2DayUpperLimit");
		if(type == TableStaticConstData.TABLE_FUND_TYPE_FUND && fundUpperLimit != null && sumGain >= fundUpperLimit){
			sendFlag = true;
		}

		// 基金两日下降幅度超过最小值
		Double fundLowerLimit = smsSendRuleJson.getDouble("fund2DayLowerLimit");
		if(type == TableStaticConstData.TABLE_FUND_TYPE_FUND && fundLowerLimit != null && sumGain <= fundLowerLimit){
			sendFlag = true;
		}

		// 基金或股票单日净值涨幅超过最大值
		Double fundUpMoney = smsSendRuleJson.getDouble(fundCode + "UpperLimit");
		double fundValueChange = fund.getCurNetValue() - fund.getLastNetValue();
		if(fundUpMoney != null && fundValueChange >= fundUpMoney){
			sendFlag = true;
		}

		// 基金或股票单日净值跌幅低过最小值
		Double fundDownMoney = smsSendRuleJson.getDouble(fundCode + "LowerLimit");
		if(fundDownMoney != null && fundValueChange <= fundDownMoney) {
			sendFlag = true;
		}

		if(sendFlag){
			lastSendDate = now;
			logger.debug("向{}发送短信:{}", sendPhone, fundCode);
			asyncService.aliSendSmsCode(sendPhone, fundCode);
			smsSendRuleJson.put(fundSendFlagKey, true);
			constantService.updateValueById(SysConstantData.SMS_SEND_RULE, smsSendRuleJson.toJSONString());
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
			// logger.trace("爬取的最新基金数据为:{}", retStr);
			// {"gztime":"2019-10-30 09:30","gszzl":"-0.66","fundcode":"519727","name":"交银成长30混合","dwjz":"1.4620","jzrq":"2019-10-29","gsz":"1.4524"}

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
		BigDecimal gainTotal = new BigDecimal(0.4547417);
		gainTotal = gainTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
		System.out.println(gainTotal);
	}

}
