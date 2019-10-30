package com.kenick.service.impl;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
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
import com.alibaba.fastjson.JSONObject;
import com.kenick.dao.FundDao;
import com.kenick.entity.Fund;
import com.kenick.service.TaskService;
import com.kenick.util.HttpRequestUtils;

@Service("taskService")
@Configurable
@EnableScheduling
public class TaskServiceImpl implements TaskService{	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	public final static Double UPPERLIMIT = 1.5D;
	public final static Double LOWERLIMIT = -1.5D;
	
	private static Date lastSendDate = new Date(); 
	private static Map<String, Map<String,Integer>> fundSmsMap = new HashMap<String, Map<String,Integer>>();
	
	@Autowired
	private AsyncServiceImpl asyncService;
	
	@Resource
	private FundDao fundDao;
   
	// 每隔指定时间执行一次，上一次任务必须已完成
    @Scheduled(cron = "0 0/3 9-12,13-16 * * ?")
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
    
    @Scheduled(cron = "0 30 14 * * ?")
    public void clean(){
    	if(fundSmsMap != null){
    		fundSmsMap.clear();
    	}
    }
    
    /**
     * 根据基金编码完善基金信息 更新
     * @param code 基金编码
     */
    private void perfectFundInfoByCode(Fund fund){
    	try{        	
        	// 通过jsoup获取基金信息
        	// Fund updateFund = getFundInfoByJsoup(fund.getCode());
        	
    		// 通过http获取最新基金信息
        	Fund updateFund = getFundByHttp(fund);
        	
        	if(updateFund !=null && !StringUtils.isEmpty(updateFund.getName()) && updateFund.getCurGain() != null){
        		logger.debug("最新基金信息:{}", updateFund.toString());
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
        				// 短信发送间隔1分钟
        				Calendar calendar = Calendar.getInstance();
        				calendar.setTime(lastSendDate);
        				calendar.add(Calendar.MINUTE, 1);
        				Date oneMinuteLater = calendar.getTime();
        				
        				// 当前时间为当天9点35分钟后 下午3点之前
        				calendar.setTime(now);
        				calendar.set(Calendar.HOUR_OF_DAY, 9);
        				calendar.set(Calendar.MINUTE, 32);
        				Date startDate = calendar.getTime();
        				
        				calendar.setTime(now);
        				calendar.set(Calendar.HOUR_OF_DAY, 14);
        				calendar.set(Calendar.MINUTE, 58);
        				Date endDate = calendar.getTime();
        				
        				if(now.after(oneMinuteLater) && now.after(startDate) && now.before(endDate)){
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
    private Fund getFundInfoByJsoup(String fundCode){
    	Fund fund = new Fund();
    	fund.setCode(fundCode);
    	
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
			fund.setName(fundName);
			
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
			fund.setLastGain(lastNetValue);
			
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
    
    private Fund getFundByHttp(Fund databaseFund){
    	Fund fund = new Fund();
    	fund.setCode(databaseFund.getCode());   	
    	try{
    		// 获取最新净值和涨幅
        	Date now = new Date();
        	String url = "http://fundgz.1234567.com.cn/js/"+databaseFund.getCode()+".js?rt="+now.getTime();
        	String retStr = HttpRequestUtils.httpGetString(url, StandardCharsets.UTF_8.name());
        	String retJsonStr = retStr.substring(8, retStr.length()-2);
        	// {"gztime":"2019-10-30 09:30","gszzl":"-0.66","fundcode":"519727","name":"交银成长30混合","dwjz":"1.4620","jzrq":"2019-10-29","gsz":"1.4524"}
        	JSONObject retJson = JSONObject.parseObject(retJsonStr);
        	
        	fund.setCurNetValue(retJson.getDouble("gsz"));
        	fund.setCurGain(retJson.getDouble("gszzl")); 
        	fund.setCurTime(retJson.getString("gztime").substring(5));

        	// 查询数据库记录
        	double lastGain = 0.0;
        	double lastNetValue = retJson.getDouble("dwjz");
        	if(lastNetValue == databaseFund.getLastNetValue()){
        		lastGain = databaseFund.getLastGain();
        	}else{
        		lastGain = databaseFund.getCurGain();
        	}
        	fund.setLastNetValue(lastNetValue);
        	fund.setLastGain(lastGain);
        	return fund;
    	}catch (Exception e) {
    		logger.error("获取基金信息失败", e.getCause());
    		return null;
		}
    }
    
    public static void main(String[] args) {
	}
}