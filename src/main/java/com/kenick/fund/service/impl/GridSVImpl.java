package com.kenick.fund.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kenick.constant.TableStaticConstData;
import com.kenick.fund.bean.Fund;
import com.kenick.fund.bean.GridCondition;
import com.kenick.fund.service.IFileStorageSV;
import com.kenick.fund.service.IFundService;
import com.kenick.fund.service.IGridSV;
import com.kenick.util.DateUtils;
import com.kenick.util.FileUtil;
import com.kenick.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * author: zhanggw
 * 创建时间:  2021/7/23
 */
@Service
public class GridSVImpl implements IGridSV {

    private static final Logger logger = LoggerFactory.getLogger(GridSVImpl.class);

    @Autowired
    private IFileStorageSV fileStorageSV;

    @Autowired
    private IFundService fundService;

    public static void main(String[] args) {
    }

    /**
     * <一句话功能简述>
     * <功能详细描述>
     * author: zhanggw
     * 创建时间:  2021/7/24
     */
    private static JSONObject gridBackTest(GridCondition gridCondition, String fundCode, Double initPrice, double interval, int tradeQuantity, List<String> fundList, Integer gridMode) {
        JSONObject retJson = new JSONObject();

        // 设置网格条件
        if(gridCondition == null){
            gridCondition = new GridCondition();
            gridCondition.setFundCode(fundCode);
            if(initPrice != null && initPrice > 0){
                gridCondition.setBenchmarkPriceInit(new BigDecimal(initPrice));
            }
            gridCondition.setGridInterval(new BigDecimal(interval));
            gridCondition.setTradeQuantity(tradeQuantity);
            if(gridMode != null && gridMode < 10){ // 预设持仓量模式 gridMode倍数
                gridCondition.setBuyQuantity(tradeQuantity*gridMode);
                gridCondition.setHoldQuantity(tradeQuantity*gridMode);
            }
        }

        Date fundDate; // 最新时间
        BigDecimal fundCurrentPrice; // 最新价格
        BigDecimal gridBuyPrice; // 网格买入价
        BigDecimal gridSellPrice; // 网格卖出价
        for(String fundData:fundList){
            String[] fundDataArray = fundData.split(",");
            fundDate = DateUtils.tranToDate(fundDataArray[0], "yyyy-MM-dd hh:mm:ss");
            fundCurrentPrice = new BigDecimal(fundDataArray[1]);
            if(fundCurrentPrice.compareTo(BigDecimal.ZERO) == 0){
                continue;
            }

            gridCondition.setFundPrice(fundCurrentPrice);

            // 初始化基准价
            if(gridCondition.getBenchmarkPriceInit() == null || gridCondition.getBenchmarkPriceInit().compareTo(BigDecimal.ZERO) == 0){
                gridCondition.setBenchmarkPriceInit(fundCurrentPrice);
            }
            if(gridCondition.getBenchmarkPriceNew() == null || gridCondition.getBenchmarkPriceNew().compareTo(BigDecimal.ZERO) == 0){
                gridCondition.setBenchmarkPriceNew(gridCondition.getBenchmarkPriceInit());
            }
            // 预设持仓量模式 gridMode倍数初始化买入平均价
            if(gridMode != null && gridMode < 10 && gridCondition.getHoldQuantity() > 0 && gridCondition.getBuyAvgPrice() == null){
                gridCondition.setBuyAvgPrice(fundCurrentPrice);
                gridCondition.setHoldPrice(fundCurrentPrice);
            }

            // 最新买入、卖出价
            gridBuyPrice = gridCondition.getBenchmarkPriceNew().subtract(gridCondition.getGridInterval());
            gridSellPrice = gridCondition.getBenchmarkPriceNew().add(gridCondition.getGridInterval());

            if(fundCurrentPrice.compareTo(gridBuyPrice) <= 0){ // 低于买入价
                // 撤掉最近委托卖出单，回测不需要处理
                // 记录买入
                gridCondition.setTriggerTime(fundDate);
                recordGridBuy(gridBuyPrice,gridCondition);
            }

            if(fundCurrentPrice.compareTo(gridSellPrice) >= 0){ // 高于卖出价
                // 撤掉最近委托买入单，回测不需要处理
                // 记录卖出
                gridCondition.setTriggerTime(fundDate);
                recordGridSell(gridSellPrice, gridCondition);
            }
        }

        calcGainMoney(null, gridCondition);
        retJson.put("gridCondition", gridCondition);
        return retJson;
    }

    private static void recordGridSell(BigDecimal gridSellPrice, GridCondition gridCondition) {
        // 委托单次交易数量
        int tradeQuantity = gridCondition.getTradeQuantity();
        // 持有量小于单次委托数量，无法成交
        int holdQuantityOld = gridCondition.getHoldQuantity();
        if(holdQuantityOld < tradeQuantity){
            return;
        }

        // 卖出计算
        BigDecimal sellAvgPrice = gridCondition.getSellAvgPrice() == null ? gridSellPrice: gridCondition.getSellAvgPrice();
        int sellQuantity = gridCondition.getSellQuantity();
        int sellQuantityNew = sellQuantity + tradeQuantity;
        if(sellQuantity > 0){
            sellAvgPrice = sellAvgPrice.multiply(new BigDecimal(sellQuantity))
                    .add(gridSellPrice.multiply(new BigDecimal(tradeQuantity)))
                    .divide(new BigDecimal(sellQuantityNew), 3, RoundingMode.HALF_UP);
        }else{
            sellAvgPrice = gridSellPrice;
        }

        // 持仓计算
        BigDecimal holdPrice = gridCondition.getHoldPrice() == null ? gridSellPrice : gridCondition.getHoldPrice();
        int holdQuantityNew = holdQuantityOld - tradeQuantity;
        if(holdQuantityNew == 0){
            holdPrice = BigDecimal.ZERO;
        }else{
            holdPrice = holdPrice.multiply(new BigDecimal(holdQuantityOld)).subtract(gridSellPrice.multiply(new BigDecimal(tradeQuantity)))
                    .divide(new BigDecimal(holdQuantityNew), 3, RoundingMode.HALF_UP);
        }
        gridCondition.setHoldQuantity(holdQuantityNew);
        gridCondition.setHoldPrice(holdPrice);

        // 卖出总次数、卖出总数量、卖出平均价、交易总次数、当前持有量、最新基准价
        gridCondition.setSellTotal(gridCondition.getSellTotal()+1);
        gridCondition.setSellQuantity(sellQuantityNew);
        gridCondition.setSellAvgPrice(sellAvgPrice);
        gridCondition.setTradeTotal(gridCondition.getTradeTotal()+1);
        gridCondition.setBenchmarkPriceNew(gridSellPrice);

        calcGainMoney("卖",gridCondition);
    }

    // 网格买入
    private static void recordGridBuy(BigDecimal gridBuyPrice, GridCondition gridCondition) {
        // 委托单次交易数量
        int tradeQuantity = gridCondition.getTradeQuantity();

        // 买入计算
        BigDecimal buyAvgPrice = gridCondition.getBuyAvgPrice() == null ? gridBuyPrice: gridCondition.getBuyAvgPrice();
        int buyQuantityOld = gridCondition.getBuyQuantity();
        int buyQuantityNew = buyQuantityOld + tradeQuantity;
        if(buyQuantityOld > 0){
            buyAvgPrice = buyAvgPrice.multiply(new BigDecimal(buyQuantityOld))
                    .add(gridBuyPrice.multiply(new BigDecimal(tradeQuantity)))
                    .divide(new BigDecimal(buyQuantityNew), 3, RoundingMode.HALF_UP);
        }else{
            buyAvgPrice = gridBuyPrice;
        }
        gridCondition.setBuyTotal(gridCondition.getBuyTotal()+1);
        gridCondition.setTradeTotal(gridCondition.getTradeTotal()+1);
        gridCondition.setBuyQuantity(buyQuantityNew);
        gridCondition.setBuyAvgPrice(buyAvgPrice);

        // 持仓计算
        int holdQuantityOld = gridCondition.getHoldQuantity();
        int holdQuantityNew = holdQuantityOld + tradeQuantity;
        BigDecimal holdPrice = gridCondition.getHoldPrice() == null ? gridBuyPrice : gridCondition.getHoldPrice();
        if(holdQuantityOld > 0){
            holdPrice = holdPrice.multiply(new BigDecimal(holdQuantityOld))
                    .add(gridBuyPrice.multiply(new BigDecimal(tradeQuantity)))
                    .divide(new BigDecimal(holdQuantityNew), 3, RoundingMode.HALF_UP);
        }else{
            holdPrice = gridBuyPrice;
        }
        int maxQuantity = gridCondition.getMaxHoldQuantity();
        if(holdQuantityNew > maxQuantity){
            maxQuantity = holdQuantityNew;

        }
        BigDecimal holdMoneyNew = holdPrice.multiply(new BigDecimal(holdQuantityNew));
        BigDecimal maxHoldMoney = gridCondition.getMaxHoldMoney();
        if(maxHoldMoney == null){
            maxHoldMoney = holdMoneyNew;
        }else{
            if(holdMoneyNew.compareTo(maxHoldMoney) > 0){
                maxHoldMoney = holdMoneyNew;
            }
        }
        gridCondition.setHoldQuantity(holdQuantityNew);
        gridCondition.setHoldPrice(holdPrice);
        gridCondition.setMaxHoldQuantity(maxQuantity);
        gridCondition.setMaxHoldMoney(maxHoldMoney);
        gridCondition.setBenchmarkPriceNew(gridBuyPrice);

        // 计算盈利
        calcGainMoney("买",gridCondition);
    }

    private static void calcGainMoney(String directDesc,GridCondition gridCondition) {
        // 计算盈利
        // 网格盈利 买、卖相互抵消赚的钱
        BigDecimal gridGainMoney = BigDecimal.ZERO;
        int sellQuantity = gridCondition.getSellQuantity();
        if(sellQuantity > 0){
            gridGainMoney = gridCondition.getSellAvgPrice().subtract(gridCondition.getBuyAvgPrice()).multiply(new BigDecimal(sellQuantity));
        }
        // 持仓盈利，不能使用持仓价计算，过程中可能会清仓
        BigDecimal fundPrice = gridCondition.getFundPrice();
        BigDecimal holdGainMoney = BigDecimal.ZERO;
        if(gridCondition.getBuyQuantity() > 0){
            holdGainMoney = fundPrice.subtract(gridCondition.getBuyAvgPrice()).multiply(new BigDecimal(gridCondition.getBuyQuantity() - sellQuantity));
        }
        // 手续费
        BigDecimal serviceFee = gridCondition.getServiceFee() == null ? BigDecimal.ZERO : gridCondition.getServiceFee();
        BigDecimal currentFee;
        // 当前手续费 上海债百万分之5 0.000005，深圳十万分之5 0.00005
        if(StringUtils.isNotBlank(directDesc)){
            currentFee = new BigDecimal(0.01);
            BigDecimal dealFee = fundPrice.multiply(new BigDecimal(gridCondition.getTradeQuantity())).multiply(new BigDecimal(0.000005));
            currentFee = dealFee.compareTo(currentFee) > 0 ? dealFee : currentFee;
            currentFee = currentFee.setScale(2,RoundingMode.HALF_UP);
            serviceFee = serviceFee.add(currentFee);
        }

        gridCondition.setGainMoney(gridGainMoney.add(holdGainMoney).subtract(serviceFee));
        gridCondition.setServiceFee(serviceFee);

        if(StringUtils.isNotBlank(directDesc)){
            logger.trace("{} {}:{},买入均价:{},网格盈利:{},持仓盈利:{},总盈利:{},当前持仓:{}，最大持仓:{}"
                    , DateUtils.getStrDate(gridCondition.getTriggerTime()),directDesc, fundPrice,
                    gridCondition.getBuyAvgPrice(), gridGainMoney,holdGainMoney, gridCondition.getGainMoney(),
                    gridCondition.getHoldQuantity(),gridCondition.getMaxHoldQuantity());

            StringBuilder tradeDetailSB = new StringBuilder();
            tradeDetailSB.append(DateUtils.getStrDate(gridCondition.getTriggerTime())).append(" ")
                    .append(directDesc).append(":").append(gridCondition.getBenchmarkPriceNew().setScale(2, RoundingMode.HALF_UP)).append(",买入均价:")
                    .append(gridCondition.getBuyAvgPrice()).append(",网格盈利:").append(gridGainMoney.setScale(2, RoundingMode.HALF_UP))
                    .append(",持仓盈利:").append(holdGainMoney).append(",总盈利:").append(gridCondition.getGainMoney().setScale(2, RoundingMode.HALF_UP))
                    .append(",当前持仓:").append(gridCondition.getHoldQuantity())
                    .append(",最大持仓:").append(gridCondition.getMaxHoldQuantity());
            List<String> tradeList = gridCondition.getTradeDetail() == null ? new ArrayList<>() : gridCondition.getTradeDetail();
            tradeList.add(tradeDetailSB.toString());
            gridCondition.setTradeDetail(tradeList);
        }
    }

    @Override
    public JSONObject backTest(String fundCode, Integer dayNum, Double initPrice, Double gridInterval, Integer gridQuantity, Integer gridMode) throws Exception{
        JSONObject retJson = new JSONObject();

        // 找到最近几天文件
        List<String> fundRecordFileList = getLastRecordFile(fundCode, dayNum);
        if(fundRecordFileList == null || fundRecordFileList.size() == 0){
            retJson.put("fundRecordFileList", fundRecordFileList);
            return retJson;
        }

        // 回测每天数据
        File curFile;
        GridCondition gridCondition = null;
        for(String fundRecordFile:fundRecordFileList){
            curFile = fileStorageSV.getHistoryFileByName(fundCode, fundRecordFile);
            List<String> fundList = FileUtil.getTextListFromFile(curFile);
            JSONObject ret = gridBackTest(gridCondition, fundCode, initPrice, gridInterval, gridQuantity, fundList, gridMode);
            gridCondition = JsonUtils.copyObjToBean(ret.getJSONObject("gridCondition"), GridCondition.class);
            retJson.put(fundRecordFile, gridCondition.getDisplay());
        }
        retJson.put("tradeDetail", gridCondition.getTradeDetail());

        return retJson;
    }

    @Override
    public JSONObject gridRank(int rankMode) throws Exception {
        JSONObject retJson = new JSONObject();
        try{
            if(rankMode >= 101 && rankMode <= 110){ // 转债模式,101 最近1周 102 最近2周
                return gridRankSummary(rankMode,5, 0.5,10);
            }
            return gridRankSummary(rankMode,5, 0.5,100);
        }catch (Exception e){
            logger.error("网格排行计算异常!", e);
        }

        return retJson;
    }

    @Override
    public JSONObject findOutBad(int findMode) throws Exception {
        JSONObject retJson = new JSONObject();
        int rankModeMax = findMode - 100;
        Map<String, JSONObject> badFundCountMap = new HashMap<>();
        for(int rankMode=101; rankMode <= rankModeMax; rankMode++){
            JSONObject rankJson = gridRankSummary(rankMode, 5, 0.5, 10);
            JSONArray badList = rankJson.getJSONArray("badList");
            if(badList != null && badList.size()>0){
                for(int i=0; i<badList.size(); i++){
                    JSONObject tmpJson = badList.getJSONObject(i);
                    String key = tmpJson.getString("name")+tmpJson.getString("code");
                    BigDecimal gainMoney = tmpJson.getBigDecimal("gainMoney");

                    JSONObject badJson = badFundCountMap.get(key);
                    if(badJson == null){
                        badJson = new JSONObject();
                        badJson.put("gainMoney", gainMoney);
                        badJson.put("time", 1);
                    }else{
                        BigDecimal oldGain = badJson.getBigDecimal("gainMoney") == null ? BigDecimal.ZERO : badJson.getBigDecimal("gainMoney");
                        int time = badJson.getInteger("time") == null ? 0 : badJson.getInteger("time");
                        badJson.put("gainMoney", oldGain.add(gainMoney));
                        badJson.put("time", time+1);
                    }
                    badFundCountMap.put(key, badJson);
                }
            }
        }
        retJson.put("badFundCountMap", badFundCountMap);
        return retJson;
    }

    @Override
    public JSONObject findOutGood(int findMode) throws Exception {
        JSONObject retJson = new JSONObject();
        int rankModeMax = findMode - 100;
        Map<String, JSONObject> goodFundCountMap = new HashMap<>();
        for(int rankMode=101; rankMode <= rankModeMax; rankMode++){
            int weekNum = rankMode - 100;
            logger.debug("开始回测最近第{}周数据!", weekNum);
            JSONObject rankJson = gridRankSummary(rankMode, 5, 0.5, 10);
            JSONArray goodList = rankJson.getJSONArray("goodList");
            logger.debug("回测最近第{}周数据:{}", weekNum, rankJson);
            if(goodList != null && goodList.size()>0){
                for(int i=0; i<goodList.size(); i++){
                    JSONObject tmpJson = goodList.getJSONObject(i);
                    String key = tmpJson.getString("name")+tmpJson.getString("code");
                    BigDecimal gainMoney = tmpJson.getBigDecimal("gainMoney");

                    JSONObject goodJson = goodFundCountMap.get(key);
                    if(goodJson == null){
                        goodJson = new JSONObject();
                        goodJson.put("gainMoney", gainMoney);
                        goodJson.put("time", 1);
                    }else{
                        BigDecimal oldGain = goodJson.getBigDecimal("gainMoney") == null ? BigDecimal.ZERO : goodJson.getBigDecimal("gainMoney");
                        int time = goodJson.getInteger("time") == null ? 0 : goodJson.getInteger("time");
                        goodJson.put("gainMoney", oldGain.add(gainMoney));
                        goodJson.put("time", time+1);
                    }
                    goodFundCountMap.put(key, goodJson);
                }
            }
        }

        logger.debug("回测最近{}周排名前3数据:{}", findMode-200, goodFundCountMap);
        retJson.put("goodFundCountMap", goodFundCountMap);
        return retJson;
    }

    private JSONObject gridRankSummary(int rankMode, int rankDayNum, double gridInterval, int tradeQuantity) {
        JSONObject retJson = new JSONObject();
        try{
            List<Fund> fundList = fundService.getAllFundList();
            TreeMap<Double, JSONObject> rankMap = new TreeMap<>((o1, o2) -> -o1.compareTo(o2));

            for(Fund fund:fundList){
                String fundCode = fund.getFundCode();
                Integer fundType = fund.getType();

                if(StringUtils.isBlank(fundCode)){
                    continue;
                }

                if(rankMode >= 101 && rankMode <= 110){
                    if(fundType == TableStaticConstData.TABLE_FUND_TYPE_FUND || fundType == TableStaticConstData.TABLE_FUND_TYPE_STOCK){
                        continue;
                    }
                }else{
                    if(rankMode != fundType){
                        continue;
                    }
                }

                // 找到最近几天文件
                List<String> fundRecordFileList = getLastRecordFile(fundCode, rankDayNum, rankMode);
                if(fundRecordFileList == null || fundRecordFileList.size() == 0){
                    continue;
                }

                JSONObject tmpJson = new JSONObject();
                tmpJson.put("name", fund.getFundName());
                tmpJson.put("code", fundCode);

                // 回测每天数据
                File curFile;
                GridCondition gridCondition = null;
                for(String fundRecordFile:fundRecordFileList){
                    curFile = fileStorageSV.getHistoryFileByName(fundCode, fundRecordFile);
                    List<String> fundDayList = FileUtil.getTextListFromFile(curFile);
                    JSONObject ret = gridBackGain(gridCondition, fundCode, gridInterval, tradeQuantity, fundDayList);
                    gridCondition = JsonUtils.copyObjToBean(ret.getJSONObject("gridCondition"), GridCondition.class);
                    fundDayList = null; // 待强制回收
                }

                BigDecimal gainMoney = gridCondition.getGainMoney();
                tmpJson.put("gainMoney", gainMoney);
                tmpJson.put("tradeTotal", gridCondition.getTradeTotal());
                tmpJson.put("holdQuantity", gridCondition.getHoldQuantity());

                if(rankMap.containsKey(gainMoney.doubleValue())){
                    rankMap.put(gainMoney.doubleValue() - 0.01, tmpJson);
                }else{
                    rankMap.put(gainMoney.doubleValue(), tmpJson);
                }
            }
            retJson.put("rankMap", rankMap);
            if(rankMap.size() > 0){
                List<JSONObject> badList = new ArrayList<>();
                List<JSONObject> goodList = new ArrayList<>();

                // 找到排名数据
                int i=0;
                int badStart = rankMap.size() - 3;
                Iterator<Map.Entry<Double, JSONObject>> rankIterator = rankMap.entrySet().iterator();
                while (rankIterator.hasNext()){
                    Map.Entry<Double, JSONObject> next = rankIterator.next();
                    if(i < 3){ // 排名前3位
                        goodList.add(next.getValue());
                    }
                    if(i >= badStart){ // 排名末3位
                        badList.add(next.getValue());
                    }
                    i++;
                }

                retJson.put("badList", badList);
                retJson.put("goodList", goodList);
                badList = null;
                goodList = null;
            }

            fundList = null;
            rankMap = null;
        }catch (Exception e){
            logger.error("转债网格排行计算异常!", e);
        }
        return retJson;
    }

    private List<String> getLastRecordFile(String fundCode, int dayNum, int rankMode) {
        String storageHomePath = fileStorageSV.getStorageHomePath();
        String fundPath = storageHomePath + File.separator + "history" + File.separator + fundCode;
        File fundDir = new File(fundPath);
        if(!fundDir.exists()){
            return null;
        }

        List<String> fundDateList = Arrays.asList(Objects.requireNonNull(fundDir.list((dir, name) -> name.contains(fundCode))));
        int cycleNum = rankMode - 100;
        int startDayNum = fundDateList.size() - 5*cycleNum;
        int endDayNum = fundDateList.size() - 5*(cycleNum-1);
        if(startDayNum >= 0){
            Collections.sort(fundDateList);
            fundDateList = fundDateList.subList(startDayNum, endDayNum);
            logger.debug("数据文件:{}", fundDateList);
        }else{
            return null;
        }
        return fundDateList;
    }

    private JSONObject gridBackGain(GridCondition gridCondition, String fundCode, double gridInterval, int tradeQuantity, List<String> fundDayList) {
        JSONObject retJson = new JSONObject();

        // 设置网格条件
        if(gridCondition == null){
            gridCondition = new GridCondition();
            gridCondition.setFundCode(fundCode);
            gridCondition.setGridInterval(new BigDecimal(gridInterval));
            gridCondition.setTradeQuantity(tradeQuantity);
            gridCondition.setBuyQuantity(tradeQuantity);
            gridCondition.setHoldQuantity(tradeQuantity);
        }

        Date fundDate; // 最新时间
        BigDecimal fundCurrentPrice; // 最新价格
        BigDecimal gridBuyPrice; // 网格买入价
        BigDecimal gridSellPrice; // 网格卖出价
        for(String fundData:fundDayList){
            String[] fundDataArray = fundData.split(",");
            fundDate = DateUtils.tranToDate(fundDataArray[0], "yyyy-MM-dd hh:mm:ss");
            fundCurrentPrice = new BigDecimal(fundDataArray[1]);
            if(fundCurrentPrice.compareTo(BigDecimal.ZERO) == 0){
                continue;
            }

            gridCondition.setFundPrice(fundCurrentPrice);

            // 初始化基准价
            if(gridCondition.getBenchmarkPriceInit() == null || gridCondition.getBenchmarkPriceInit().compareTo(BigDecimal.ZERO) == 0){
                gridCondition.setBenchmarkPriceInit(fundCurrentPrice);
            }
            if(gridCondition.getBenchmarkPriceNew() == null || gridCondition.getBenchmarkPriceNew().compareTo(BigDecimal.ZERO) == 0){
                gridCondition.setBenchmarkPriceNew(gridCondition.getBenchmarkPriceInit());
            }
            // 预设持仓量模式 gridMode倍数初始化买入平均价
            if(gridCondition.getHoldQuantity() > 0 && gridCondition.getBuyAvgPrice() == null){
                gridCondition.setBuyAvgPrice(fundCurrentPrice);
                gridCondition.setHoldPrice(fundCurrentPrice);
            }

            // 最新买入、卖出价
            gridBuyPrice = gridCondition.getBenchmarkPriceNew().subtract(gridCondition.getGridInterval());
            gridSellPrice = gridCondition.getBenchmarkPriceNew().add(gridCondition.getGridInterval());

            if(fundCurrentPrice.compareTo(gridBuyPrice) <= 0){ // 低于买入价
                // 撤掉最近委托卖出单，回测不需要处理
                // 记录买入
                gridCondition.setTriggerTime(fundDate);
                recordGridBuy(gridBuyPrice,gridCondition);
            }

            if(fundCurrentPrice.compareTo(gridSellPrice) >= 0){ // 高于卖出价
                // 撤掉最近委托买入单，回测不需要处理
                // 记录卖出
                gridCondition.setTriggerTime(fundDate);
                recordGridSell(gridSellPrice, gridCondition);
            }
        }

        calcGainMoney(null, gridCondition);
        retJson.put("gridCondition", gridCondition);
        return retJson;
    }

    private List<String> getLastRecordFile(String fundCode, Integer dayNum) {
        String storageHomePath = fileStorageSV.getStorageHomePath();
        String fundPath = storageHomePath + File.separator + "history" + File.separator + fundCode;
        File fundDir = new File(fundPath);
        if(!fundDir.exists()){
            return null;
        }

        List<String> fundDateList = Arrays.asList(Objects.requireNonNull(fundDir.list((dir, name) -> name.contains(fundCode))));
        if(fundDateList.size() > dayNum){
            Collections.sort(fundDateList);
            int diff = fundDateList.size() - dayNum;
            fundDateList = fundDateList.subList(diff, fundDateList.size());
        }
        logger.debug("数据文件:{}", fundDateList);
        return fundDateList;
    }

}
