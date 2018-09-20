package com.kenick.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.annotation.Resource;

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
import org.springframework.stereotype.Component;

import com.kenick.dao.FundDao;

@Component
@Configurable
@EnableScheduling
public class BackgroundTaskService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private FundDao fundDao;
	
	// 每隔30秒执行一次，上一次任务必须已完成
    @Scheduled(fixedDelay = 1000 * 30)
    public void reportCurrentTime(){
    	
    }
    
	// 每隔30秒执行一次，不管上一次任务是否已完成
    @Scheduled(fixedRate = 1000 * 30)
    public void reportFixedRate(){
    	
    }

    //每1分钟执行一次
    @Scheduled(cron = "0 */1 *  * * * ")
    public void reportCurrentByCron(){
    	
    }
    
   
	// 每隔30秒执行一次，上一次任务必须已完成
    @Scheduled(fixedDelay = 1000 * 30)
    public void perfectFundInfo(){
    	// 查询出所有基金编码
    	// 根据基金编码 获取名称、当前净值估算、上一日净值、上一日涨幅
    }
    
    /**
     * 根据基金编码完善基金信息
     * @param code 基金编码
     */
    private void perfectFundInfoByCode(String code){
    	
    }
    
    // 根据基金编码获取基金信息
    private Object[] getFundInfoByJsoup(String code){
    	String fundName = ""; // 基金名称
    	String curTime = ""; // 当前估算时间 
    	Double curNetValue = 0.0; // 当前估算净值
    	Double lastNetValue = 0.0; // 上一日净值
    	Double lastGain = 0.0; // 上一日涨幅
    	try {
			Connection connect = Jsoup.connect("http://fund.eastmoney.com/" + code + ".html?spm=search");
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
			
			// 上一日净值
			Elements lastValueInfos = doc.select(".fundInfoItem .dataOfFund .dataItem02 .dataNums span");
			lastNetValue = Double.valueOf(lastValueInfos.first().text());
			String lastAdd = lastValueInfos.last().text();
			lastGain = Double.valueOf(lastAdd.substring(0,lastAdd.length()-1)) * 0.01;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return new Object[]{fundName, curTime, curNetValue, lastNetValue, lastGain};
    }
    
    public static void main(String[] args) {
    	Object[] fundInfo =  new BackgroundTaskService().getFundInfoByJsoup("161121");
    	System.out.println(Arrays.toString(fundInfo));
	}
}