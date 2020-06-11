package com.kenick.fund.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kenick.constant.TableStaticConstData;
import com.kenick.fund.service.TaskService;
import com.kenick.generate.bean.Fund;
import com.kenick.generate.bean.FundExample;
import com.kenick.generate.bean.UserFund;
import com.kenick.generate.bean.UserFundExample;
import com.kenick.generate.dao.FundMapper;
import com.kenick.generate.dao.UserFundMapper;
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
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("taskService")
@Configurable
@EnableScheduling
public class TaskServiceImpl implements TaskService{	
	private final static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
	private final static Double upperLimit = 5.0D;
	private final static Double lowerLimit = -5.0D;
	
	private static Date lastSendDate = new Date(); 
	private static Map<String, Map<String,Integer>> fundSmsMap = new HashMap<>();

	@Resource
	private AsyncServiceImpl asyncService;

	@Resource
	private FundMapper fundMapper;

	@Resource
	private UserFundMapper userFundMapper;
   
	// 每隔指定时间执行一次，上一次任务已完成
    @Scheduled(cron = "${fund.query.cron}")
    public void perfectFundInfo(){
    	try{
    		long startTime = System.currentTimeMillis();
        	// 查询出所有基金编码
    		FundExample fundExample = new FundExample();
    		fundExample.setOrderByClause(Fund.S_type);
        	List<Fund> fundList = fundMapper.selectByExample(fundExample);
        	for(Fund fund:fundList){
        		perfectFundInfoByCode(fund);
        	}
        	long endTime = System.currentTimeMillis();
        	logger.debug("遍历理财一轮花费时间:{}", endTime-startTime);
    	}catch (Exception e) {
    		logger.debug(e.getMessage());
		}
    }

    @Scheduled(cron = "${stock.update.cron}")
    public void updateStockInfoNight(){
        logger.debug("TaskServiceImpl.updateStockInfo in");
        try{
            // 查询出所有股票
            FundExample fundExample = new FundExample();
            fundExample.or().andTypeEqualTo(TableStaticConstData.TABLE_FUND_TYPE_STOCK);
            List<Fund> fundList = fundMapper.selectByExample(fundExample);
            for(Fund fund:fundList){
                updateStockInfo(fund);
            }
        }catch (Exception e) {
            logger.error("晚上更新股票信息异常!", e);
        }
    }

    private void updateStockInfo(Fund fund) {
        Fund updateFund = new Fund();
        updateFund.setFundCode(fund.getFundCode());
        updateFund.setLastGain(fund.getCurGain());
        updateFund.setLastNetValue(fund.getCurNetValue());
        updateFund.setLastPriceHighest(fund.getCurPriceHighest());
        updateFund.setLastPriceLowest(fund.getCurPriceLowest());
        updateFund.setModifyDate(new Date());
        fundMapper.updateByPrimaryKeySelective(updateFund);
    }

    @Scheduled(cron = "${fund.cache.clean.cron}")
    public void clean(){
    	if(fundSmsMap != null){
    		fundSmsMap.clear();
    	}
    }
    
    /**
     * 根据基金编码完善基金信息
     * @param fund 基金信息
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
						updateFund.setCurGain(curFund.getCurGain());
						updateFund.setCurNetValue(curFund.getCurNetValue());
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
        	fundMapper.updateByPrimaryKeySelective(updateFund);

        	// 更新用户基金信息
			UserFund userFund = JsonUtils.copyObjToBean(updateFund, UserFund.class);
			UserFundExample userFundExample = new UserFundExample();
			userFundExample.or().andFundCodeEqualTo(updateFund.getFundCode());
			userFundMapper.updateByExampleSelective(userFund, userFundExample);

			// 发送短信
        	sendSms(updateFund);
    	}catch (Exception e) {
    		logger.error(e.getMessage());
		}
    }

    // 获取股票信息
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
				url = "http://hq.sinajs.cn/list=sz"+ fundCode;
			}

			if("60".equals(fundCode.substring(0,2)) ||  "900".equals(fundCode.substring(0,3))){ // 上海
				url = "http://hq.sinajs.cn/list=sh"+ fundCode;
			}

			// 获取最新净值和涨幅
			String retStr = HttpRequestUtils.httpGetString(url, StandardCharsets.UTF_8.name());
			logger.debug("爬取的最新股票数据为:{}", retStr);
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
					Double curGain = (curNetValueNum - lastNetValue) / lastNetValue;
					fund.setCurGain(curGain);

					if(databaseFund.getLastGain() != null){
						fund.setGainTotal(new BigDecimal(curGain + databaseFund.getLastGain()));
					}else{
                        fund.setGainTotal(new BigDecimal(curGain));
                    }
				}
			}
			return fund;
		}catch (Exception e) {
			logger.error("获取股票信息失败！", e.getCause());
			return null;
		}
	}

	// 发送短信
    private void sendSms(Fund fund){
		Date now = new Date();
		boolean sendFlag = true;
		
		// 周末不发送
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		if(calendar.get(Calendar.DAY_OF_WEEK ) == Calendar.SATURDAY  || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
			return;
		}
		
		// 14:30前不发送
		calendar.setTime(now);
		calendar.set(Calendar.HOUR_OF_DAY, 14);
		calendar.set(Calendar.MINUTE, 30);
		Date fourteenHour = calendar.getTime();
		if(now.before(fourteenHour)){
			return;
		}
		
		// 15点后 不发送
		calendar.setTime(now);
		calendar.set(Calendar.HOUR_OF_DAY, 15);
		calendar.set(Calendar.MINUTE, 0);
		Date fifteenDate = calendar.getTime();
		if(now.after(fifteenDate)){
			return;
		}
		
		// 发送短信间隔1分钟
		calendar.setTime(lastSendDate);
		calendar.add(Calendar.MINUTE, 1);
		Date oneMinuteLater = calendar.getTime();
		if(now.before(oneMinuteLater)){
			return;
		}
		
		// 基金信息不全，不发送短信
		if(fund.getFundCode() == null || fund.getCurGain() == null || fund.getLastGain() == null){
			return;
		}
		
    	// 基金变动幅度不超过阈值，不发送短信
		double sumGain = fund.getCurGain() + fund.getLastGain();
		if(sumGain > upperLimit && sumGain < lowerLimit){
			return;
		}
		
		// 当天发送次数超过0次，不再发送
		String dayStr = new SimpleDateFormat("yyyyMMdd").format(now);
		Map<String, Integer> smsMap = fundSmsMap.get(dayStr);
		Integer codeSmsNum = 0;
		if(smsMap != null && smsMap.get(fund.getFundCode()) != null){
			codeSmsNum = smsMap.get(fund.getFundCode());
			if(codeSmsNum > 0){
				sendFlag = false;
			}
		}
		
		if(sendFlag){
			lastSendDate = now;
			if(smsMap == null){
				smsMap = new HashMap<>();
			}
			smsMap.put(fund.getFundCode(), ++codeSmsNum);
			asyncService.aliSendSmsCode("15910761260", fund.getFundCode());
		}
		fundSmsMap.put(dayStr, smsMap);
	}

	// 根据基金编码获取基金信息
    private Fund getFundInfoByJsoup(String fundCode){
    	Fund fund = new Fund();
    	fund.setFundCode(fundCode);
    	
    	String fundName; // 基金名称
    	String curTime; // 当前估算时间
    	Double curNetValue; // 当前估算净值
    	Double curGain;
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
			curNetValue = Double.valueOf(curNetValueStr);
			fund.setCurNetValue(curNetValue);
			
			// 当前估算涨幅
			String curGainStr = doc.getElementById("gz_gszzl").text();
			curGain	= Double.valueOf(curGainStr.substring(0,curGainStr.length()-1));
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
        	String url = "http://fundgz.1234567.com.cn/js/"+databaseFund.getFundCode()+".js?rt="+now.getTime();
        	String retStr = HttpRequestUtils.httpGetString(url, StandardCharsets.UTF_8.name());
        	logger.debug("爬取的最新基金数据为:{}", retStr);
        	// {"gztime":"2019-10-30 09:30","gszzl":"-0.66","fundcode":"519727","name":"交银成长30混合","dwjz":"1.4620","jzrq":"2019-10-29","gsz":"1.4524"}
			if(retStr != null){
				String retJsonStr = retStr.substring(8, retStr.length()-2);
				JSONObject retJson = JSONObject.parseObject(retJsonStr);
				fund.setCurNetValue(retJson.getDouble("gsz"));
				fund.setCurGain(retJson.getDouble("gszzl"));
				fund.setCurTime(retJson.getString("gztime").substring(5));
				fund.setLastNetValue(retJson.getDouble("dwjz"));
			}
        	fund.setLastGain(0.0);
        	fund.setGainTotal(BigDecimal.valueOf(fund.getLastGain()+fund.getCurGain()));
        	return fund;
    	}catch (Exception e) {
    		logger.error("获取基金信息失败", e.getCause());
    		return null;
		}
    }
    
    public static void main(String[] args) {
    	String retStr = "var hq_str_sz000876=\"新 希 望,28.260,28.170,28.960,29.780,28.260,28.960,28.970,41558107,1210395218.230,2000,28.960,5700,28.950,1900,28.940,12100,28.930,1300,28.920,2400,28.970,5600,28.980,4600,28.990,4200,29.000,4100,29.010,2020-06-02,11:30:00,00\";";
		logger.debug("retStr:{}", retStr);
		if(StringUtils.isNotBlank(retStr)){
			retStr = retStr.split("=")[1];
			retStr = retStr.replace("\"","").replace(";","");
			String[] stockInfoArray = retStr.split(",");
			String fundName = stockInfoArray[0]; // 名称
			String curNetValue = stockInfoArray[3]; // 当前价
			String curPriceHighest = stockInfoArray[4]; // 当前最高价
			String curPriceLowest = stockInfoArray[5]; // 当前最低价
			logger.debug("fundName：{}，curNetValue：{}，curPriceHighest：{}，curPriceLowest：{}", fundName, curNetValue, curPriceHighest, curPriceLowest);
		}
	}
}
