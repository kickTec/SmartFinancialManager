package com.kenick.fund.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kenick.constant.TableStaticConstData;
import com.kenick.fund.bean.Fund;
import com.kenick.fund.service.IFileStorageSV;
import com.kenick.fund.service.IFundService;
import com.kenick.user.bean.UserFund;
import com.kenick.util.DateUtils;
import com.kenick.util.FileUtil;
import com.kenick.util.JarUtil;
import com.kenick.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service("fundService")
public class FundServiceImpl implements IFundService {

	private final static Logger logger = LoggerFactory.getLogger(FundServiceImpl.class);

	private List<Fund> fundCacheList = Collections.synchronizedList(new ArrayList<>()); // 使用本地缓存

	@Autowired
	private IFileStorageSV fileStorageService;

	@Override
	public List<Fund> findAllFundByCondition(Fund fundCondition, String orderBy) {
		return null;
	}

	@Override
	public List<UserFund> findAllUserFundByCondition(UserFund userFundCondition) {
		return null;
	}

	@Override
	public List<Fund> getShowFundList() {
		List<Fund> retList = new ArrayList<>();
		try{
			if(fundCacheList == null || fundCacheList.size() == 0){
				fundCacheList = fileStorageService.getFundListFromFile();
			}
			if(fundCacheList != null && fundCacheList.size() > 0){
				for(Fund fund:fundCacheList){
					if(fund.getFundState() == TableStaticConstData.TABLE_FUND_TYPE_STATE_VALID){
						retList.add(fund);
					}
				}
			}
		}catch (Exception e){
			logger.error("获取展示基金异常!", e);
		}

		return retList;
	}

    @Override
    public JSONArray getShowFundJsonArray() {
        JSONArray retArray = new JSONArray();
        try{
            if(fundCacheList == null || fundCacheList.size() == 0){
                fundCacheList = fileStorageService.getFundListFromFile();
            }
            if(fundCacheList != null && fundCacheList.size() > 0){
                for(Fund fund:fundCacheList){
                    if(fund.getFundState() == TableStaticConstData.TABLE_FUND_TYPE_STATE_VALID){
                        JSONObject fundJson = JsonUtils.bean2JSON(fund);
                        if(fund.getGainTotal().compareTo(new BigDecimal(2.0)) >= 0 && fund.getCurGain() >= 1.0){
                            fundJson.put("bgColor", "#E83132");
                        }
                        if(fund.getGainTotal().compareTo(new BigDecimal(-2.0)) <= 0 && fund.getCurGain() <= -1.0){
                            fundJson.put("bgColor", "#009A04");
                        }
                        retArray.add(fundJson);
                    }
                }
            }
        }catch (Exception e){
            logger.error("获取展示基金异常!", e);
        }

        return retArray;
    }

    @Override
    public JSONObject queryDetail(Integer fundType, String fundCode) {
        JSONObject retJson = new JSONObject();
        try{
            // 读取每日数据
            String storageHomePath = fileStorageService.getStorageHomePath();
            if(StringUtils.isBlank(storageHomePath)){
                return retJson;
            }
            String fundHistoryPath = storageHomePath + File.separator + "history" + File.separator + fundCode + File.separator + "day.txt";
            if(fundType != null && fundType==4 && "000001".equals(fundCode)){
                fundHistoryPath = storageHomePath + File.separator + "history" + File.separator + "sh" + fundCode + File.separator + "day.txt";
            }
            List<String> historyList = FileUtil.getTextListFromFile(new File(fundHistoryPath));

            // 获取最近3个月数据
            JSONObject lastData90 = FileUtil.getLastDataByNum(90, historyList);
            retJson.put("lastData90", lastData90);

            // 获取近30天 60天 90天 均价,10%低位均价,10%高位均价
            JSONObject avg1 = FileUtil.getAvgHighLow(30, historyList);
            retJson.put("avg1", avg1);
            JSONObject avg2 = FileUtil.getAvgHighLow(60, historyList);
            retJson.put("avg2", avg2);
            JSONObject avg3 = FileUtil.getAvgHighLow(90, historyList);
            retJson.put("avg3", avg3);

            // 基本信息
            if(fundCacheList != null && fundCacheList.size() > 0){
                for(Fund fund:fundCacheList){
                    if(fund.getFundCode().equals(fundCode)){
                        JSONObject fundJson = JsonUtils.bean2JSON(fund);
                        retJson.put("basic",fundJson);
                        break;
                    }
                }
            }
        }catch (Exception e){
            logger.error("queryDetail异常!", e);
        }
	    return retJson;
    }

    @Override
    public JSONObject generateDayList(String fundCode) {
        JSONObject retJson = new JSONObject();
	    try{
            String storageHomePath = fileStorageService.getStorageHomePath();
            if(StringUtils.isBlank(storageHomePath)){
                return retJson;
            }
            String fundPath = storageHomePath + File.separator + "history" + File.separator + fundCode;
            File dayTxt = new File(fundPath + File.separator + "day.txt");
            if(dayTxt.exists()){
                dayTxt.delete();
            }
            FileUtil.generateDayHistory(fundPath);
        }catch (Exception e){
            logger.error("generateDayList异常", e);
        }
        return retJson;
    }

    @Override
    public JSONObject queryDayDetail(Integer fundType, String fundCode, Date date) {
        JSONObject retJson = new JSONObject();
        try{
            // 存储主目录 /home/kenick/smartFinancial-manager/storage
            String storageHomePath = fileStorageService.getStorageHomePath();
            if(StringUtils.isBlank(storageHomePath) || date == null){
                return retJson;
            }
            // 当天数据存储路径 /home/kenick/smartFinancial-manager/storage/history/113616/113616_2022-08-31.txt
            String dayFilePath = storageHomePath + File.separator + "history" + File.separator + fundCode;
            if(fundType != null && fundType==4 && "000001".equals(fundCode)){
                dayFilePath = storageHomePath + File.separator + "history" + File.separator + "sh" + fundCode;
            }
            dayFilePath = dayFilePath + File.separator + fundCode + "_" + DateUtils.getStrDate(date, "yyyy-MM-dd") + ".txt";
            logger.debug("dayFilePath:{}", dayFilePath);

            // 数据处理
            List<String> allList = FileUtil.getTextListFromFile(new File(dayFilePath));
            if(allList == null){
                return retJson;
            }

            List<String> dateList = new ArrayList<>();
            List<BigDecimal> stockValueList = new ArrayList<>();
            for(int i=0; i<allList.size(); i++){
                if(i % 2 == 0){
                    continue;
                }
                String netVal = allList.get(i);
                String[] netValArray = netVal.split(",");
                String dateStr = netValArray[0].substring(netValArray[0].length() - 6);
                BigDecimal subVal = new BigDecimal(netValArray[1]);
                dateList.add(dateStr);
                stockValueList.add(subVal);
            }
            retJson.put("allList", allList);
            retJson.put("dateList", dateList);
            retJson.put("stockValueList", stockValueList);
        }catch (Exception e){
	        logger.error("queryDayDetail异常!", e);
        }
        return retJson;
    }

    @Override
    public void saveFundJsonBackup() {
	    try{
	        if(fundCacheList == null){
	            return;
            }

            String storageHomePath = fileStorageService.getStorageHomePath();
            FileUtil.writeFund2File(storageHomePath + File.separator+"fund.json."+DateUtils.getNowDateStr("yyyyMMdd"), fundCacheList);
        }catch (Exception e){
            logger.error("saveFundJsonBackup error!", e);
        }
    }

    @Override
	public List<Fund> getAllFundList() {
		if(fundCacheList == null || fundCacheList.size() == 0){
			fundCacheList = fileStorageService.getFundListFromFile();
		}
		return fundCacheList;
	}

    @Override
    public void loadFundChangeHot() {
	    try{
            String storageHomePath = fileStorageService.getStorageHomePath();
            if(StringUtils.isBlank(storageHomePath)){
                return;
            }

            // 热加载新增
            String filePath = storageHomePath + File.separator + "smfConfig.properties";
            String addBondSz = FileUtil.getPropertyByPath(filePath, "addBondSz");
            String addBondSh = FileUtil.getPropertyByPath(filePath, "addBondSh");
            fundListAddElementInner(addBondSz, addBondSh);

            // 热加载删除
            String delElement = FileUtil.getPropertyByPath(filePath, "delElement");
            fundListDelElementInner(delElement);

        }catch (Exception e){
            logger.error("热加载理财标的变动异常!", e);
        }
    }

    @Async
    @Override
    public void zipFundData(Integer dayNum, String zipName) {
        try{
            String storageHomePath = fileStorageService.getStorageHomePath();
            if(StringUtils.isBlank(zipName)){
                zipName = "zipFundData" + DateUtils.getNowDateStr("yyyyMMddHHmm") + ".zip";
            }
            JarUtil.compressFundStorage(storageHomePath + File.separator + "history", dayNum, zipName);
        }catch (Exception e){
            logger.error("压缩理财数据异常!", e);
        }
    }

    private void fundListDelElementInner(String delElement) {
	    if(StringUtils.isBlank(delElement)){
	        return;
        }

        Iterator<Fund> iterator = this.fundCacheList.iterator();
	    while(iterator.hasNext()){
            Fund fund = iterator.next();

            if(StringUtils.isNotBlank(delElement) && delElement.contains(fund.getFundCode())){
                iterator.remove();
            }
        }
    }

    private void fundListAddElementInner(String addbondSz, String addbondSh) {

        // 检测是否已存在
        for(Fund fund:this.fundCacheList){
            if(StringUtils.isNotBlank(addbondSz) && addbondSz.contains(fund.getFundCode())){
                addbondSz = addbondSz.replace(fund.getFundCode(), "");
            }
            if(StringUtils.isNotBlank(addbondSh) && addbondSh.contains(fund.getFundCode())){
                addbondSh = addbondSh.replace(fund.getFundCode(), "");
            }
        }

        // 添加深债
        if(StringUtils.isNotBlank(addbondSz)){
            String[] bondSzArray = addbondSz.split(",");
            for(String bondCode:bondSzArray){
                Fund fund = initFundInner(bondCode, TableStaticConstData.TABLE_FUND_TYPE_STOCK_SZ);
                this.fundCacheList.add(fund);
            }
        }

        // 添加沪债
        if(StringUtils.isNotBlank(addbondSh)){
            String[] bondShArray = addbondSh.split(",");
            for(String bondCode:bondShArray){
                Fund fund = initFundInner(bondCode, TableStaticConstData.TABLE_FUND_TYPE_STOCK_SH);
                this.fundCacheList.add(fund);
            }
        }
    }

    private Fund initFundInner(String bondCode, int type) {
        Fund fund = new Fund();
        fund.setFundCode(bondCode);
        fund.setFundName(bondCode);
        fund.setType(type);
        fund.setFundState(TableStaticConstData.TABLE_FUND_TYPE_STATE_HIDDEN);
        fund.setGainTotal(BigDecimal.ZERO);
        fund.setCurGain(0.0);
        fund.setCurNetValue(0.0);
        fund.setCurPriceLowest(0.0);
        fund.setCurPriceHighest(0.0);
        fund.setLastGain(0.0);
        fund.setLastNetValue(0.0);
        fund.setLastPriceHighest(0.0);
        fund.setLastPriceLowest(0.0);
        return fund;
    }

}
