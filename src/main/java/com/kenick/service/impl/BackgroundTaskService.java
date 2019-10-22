package com.kenick.service.impl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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

import com.alibaba.druid.util.StringUtils;
import com.kenick.dao.FundDao;
import com.kenick.entity.Fund;

@Service("backService")
@Configurable
@EnableScheduling
public class BackgroundTaskService {	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	public final static Double UPPERLIMIT = 1.5D;
	public final static Double LOWERLIMIT = -1.5D;
	
	private static Date lastSendDate = new Date(); 
	private static Map<String, Map<String,Integer>> fundSmsMap = new HashMap<String, Map<String,Integer>>();
	
	@Autowired
	private AsyncServiceImpl asyncService;
	
	@Resource
	private FundDao fundDao;
   
	// 每隔30秒执行一次，上一次任务必须已完成
    @Scheduled(fixedDelay = 1000 * 10)
    public void perfectFundInfo(){
    	try{
        	// 查询出所有基金编码
        	List<Fund> fundList = fundDao.findAll();
        	for(Fund fund:fundList){    		
        		perfectFundInfoByCode(fund);
        	}
    	}catch (Exception e) {
    		logger.equals(e.getMessage());
		}
    }
    
    /**
     * 根据基金编码完善基金信息 更新
     * @param code 基金编码
     */
    private void perfectFundInfoByCode(Fund fund){
    	try{
    		// 通过爬虫获取基金信息
        	Object[] fundInfo = getFundInfoByJsoup(fund.getCode());
        	logger.debug("fundInfo:{}", Arrays.toString(fundInfo));
        	
        	// 更新基金信息
        	Fund updateFund = new Fund(fundInfo);
        	updateFund.setId(fund.getId());
        	if(!StringUtils.isEmpty(updateFund.getName()) && updateFund.getCurGain() != null){
            	fundDao.update(updateFund);
        	}
        	
        	// 根据近两天幅度 决定是否发送短信
        	if(updateFund.getCode() != null && updateFund.getCurGain() != null && updateFund.getLastGain() != null){
        		double sumGain = updateFund.getCurGain() + updateFund.getLastGain();
        		if(sumGain > UPPERLIMIT || sumGain < LOWERLIMIT){
        			Date now = new Date();
        			String dayStr = new SimpleDateFormat("yyyyMMdd").format(now);
        			Map<String, Integer> smsMap = fundSmsMap.get(dayStr);
        			fundSmsMap.clear();
        			boolean sendFlag = true;
        			Integer smsNum = null;
        			if(smsMap != null){
        				smsNum = smsMap.get(updateFund.getCode());
        				if(smsNum != null && smsNum > 0){
        					sendFlag = false;
        				}
        			}else{
        				smsMap = new HashMap<>();
        			}
        			
        			if(smsNum == null){
        				smsNum = 0;
        			}
        			
        			if(sendFlag){
        				Calendar calendar = Calendar.getInstance();
        				calendar.setTime(lastSendDate);
        				calendar.add(Calendar.MINUTE, 1);
        				Date newLastDate = calendar.getTime();
        				if(now.after(newLastDate)){
            				asyncService.aliSendSmsCode("15910761260", updateFund.getCode());
            				lastSendDate = now;
            				smsMap.put(updateFund.getCode(), ++smsNum);
        				}
        			}
        			fundSmsMap.put(dayStr, smsMap);
        		}
        	}
    	}catch (Exception e) {
    		logger.error(e.getMessage());
		}
    }
    
    // 根据基金编码获取基金信息
    private Object[] getFundInfoByJsoup(String fundCode){
    	String fundName = ""; // 基金名称
    	String curTime = ""; // 当前估算时间 
    	Double curNetValue = 0.0; // 当前估算净值
    	Double curGain = 0.0;
    	Double lastNetValue = 0.0; // 上一日净值
    	Double lastGain = 0.0; // 上一日涨幅
    	try {
			Connection connect = Jsoup.connect("http://fund.eastmoney.com/" + fundCode + ".html?spm=search");
			connect.timeout(500);
			Response response = connect.execute();
			Document doc = response.parse();
			
			// 基金名称
			Elements nameEles = doc.getElementsByClass("fundDetail-tit");
			fundName = nameEles.first().text();
			
			// 当前估算时间
			String timeStr = doc.getElementById("gz_gztime").text();
			curTime = timeStr.substring(1, timeStr.length()-1);
			
			// 当前估算净值
			String curNetValueStr = doc.getElementById("gz_gsz").text();
			curNetValue = Double.valueOf(curNetValueStr);
			
			// 当前估算涨幅
			String curGainStr = doc.getElementById("gz_gszzl").text();
			curGain	= Double.valueOf(curGainStr.substring(0,curGainStr.length()-1));
			
			// 上一日净值
			Elements lastValueInfos = doc.select(".fundInfoItem .dataOfFund .dataItem02 .dataNums span");
			lastNetValue = Double.valueOf(lastValueInfos.first().text());
			String lastAdd = lastValueInfos.last().text();
			lastGain = Double.valueOf(lastAdd.substring(0,lastAdd.length()-1));
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
    	
    	return new Object[]{fundCode,fundName, curTime, curNetValue, curGain, lastNetValue, lastGain};
    }
    
    public static void main(String[] args) {
    	Object[] fundInfo =  new BackgroundTaskService().getFundInfoByJsoup("161121");
    	System.out.println(Arrays.toString(fundInfo));
	}
}