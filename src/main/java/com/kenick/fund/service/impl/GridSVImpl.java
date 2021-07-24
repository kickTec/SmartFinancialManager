package com.kenick.fund.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kenick.fund.bean.GridCondition;
import com.kenick.fund.service.IGridSV;
import com.kenick.util.DateUtils;
import com.kenick.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author: zhanggw
 * 创建时间:  2021/7/23
 */
public class GridSVImpl implements IGridSV {

    private static final Logger logger = LoggerFactory.getLogger(GridSVImpl.class);

    public static void main(String[] args) {
        String file = "D:\\tmp\\2021-07-23.txt";
        List<String> fundList = FileUtil.getTextListFromFile(new File(file));
        ArrayList<Double> intervalList = new ArrayList<>();
        intervalList.add(0.5);
        intervalList.add(0.6);
        intervalList.add(0.7);
        intervalList.add(0.8);
        for(Double tmp:intervalList){
            JSONObject ret = gridBackTest("110045", 118.37, tmp, 10, fundList);
            logger.debug("ret:{}", ret);
        }
    }

    /**
     * <一句话功能简述>
     * <功能详细描述>
     * author: zhanggw
     * 创建时间:  2021/7/24
     */
    private static JSONObject gridBackTest(String fundCode, Double initPrice, double interval, int tradeQuantity, List<String> fundList) {
        JSONObject retJson = new JSONObject();

        // 设置网格条件
        GridCondition gridCondition = new GridCondition();
        gridCondition.setFundCode(fundCode);
        if(initPrice != null){
            gridCondition.setBenchmarkPriceInit(new BigDecimal(initPrice));
        }
        gridCondition.setGridInterval(new BigDecimal(interval));
        gridCondition.setTradeQuantity(tradeQuantity);

        Date fundDate; // 最新时间
        BigDecimal fundCurrentPrice; // 最新价格
        BigDecimal gridBuyPrice; // 网格买入价
        BigDecimal gridSellPrice; // 网格卖出价
        for(String fundData:fundList){
            String[] fundDataArray = fundData.split(",");
            fundDate = DateUtils.tranToDate(fundDataArray[0], "yyyy-MM-dd hh:mm:ss");
            fundCurrentPrice = new BigDecimal(fundDataArray[1]);

            // 初始化基准价
            if(gridCondition.getBenchmarkPriceInit() == null){
                gridCondition.setBenchmarkPriceInit(fundCurrentPrice);
            }
            if(gridCondition.getBenchmarkPriceNew() == null){
                gridCondition.setBenchmarkPriceNew(gridCondition.getBenchmarkPriceInit());
            }

            // 最新买入、卖出价
            gridBuyPrice = gridCondition.getBenchmarkPriceNew().subtract(gridCondition.getGridInterval());
            gridSellPrice = gridCondition.getBenchmarkPriceNew().add(gridCondition.getGridInterval());
            gridCondition.setFundPrice(fundCurrentPrice);

            if(fundCurrentPrice.compareTo(gridBuyPrice) <= 0){ // 低于买入价
                // 撤掉最近委托卖出单，回测不需要处理
                // 记录买入
                gridCondition.setTriggerTime(fundDate);
                recordGridBuy(fundCurrentPrice, fundDate, gridCondition);
            }

            if(fundCurrentPrice.compareTo(gridSellPrice) >= 0){ // 高于卖出价
                // 撤掉最近委托买入单，回测不需要处理
                // 记录卖出
                gridCondition.setTriggerTime(fundDate);
                recordGridSell(fundCurrentPrice, fundDate, gridCondition);
            }

        }

        retJson.put("gridCondition", gridCondition);
        return retJson;
    }

    private static void recordGridSell(BigDecimal fundCurrentPrice, Date fundDate, GridCondition gridCondition) {
        // 卖出均价
        BigDecimal sellAvgPrice = gridCondition.getSellAvgPrice() == null ? fundCurrentPrice: gridCondition.getSellAvgPrice();
        // 卖出总数量
        int sellQuantity = gridCondition.getSellQuantity();
        // 委托单次交易数量
        int tradeQuantity = gridCondition.getTradeQuantity();
        // 持有量小于单次委托数量，无法成交
        if(gridCondition.getHoldQuantity() < tradeQuantity){
            return;
        }
        // 最新卖出总数量
        int sellQuantityNew = sellQuantity + tradeQuantity;
        if(sellQuantity > 0){
            sellAvgPrice = sellAvgPrice.multiply(new BigDecimal(sellQuantity))
                    .add(fundCurrentPrice.multiply(new BigDecimal(tradeQuantity)))
                    .divide(new BigDecimal(sellQuantityNew), 3, RoundingMode.HALF_UP);
        }else{
            sellAvgPrice = fundCurrentPrice;
        }

        // 卖出总次数、卖出总数量、卖出平均价、交易总次数、当前持有量、最新基准价
        gridCondition.setSellTotal(gridCondition.getSellTotal()+1);
        gridCondition.setSellQuantity(sellQuantityNew);
        gridCondition.setSellAvgPrice(sellAvgPrice);
        gridCondition.setTradeTotal(gridCondition.getTradeTotal()+1);
        gridCondition.setHoldQuantity(gridCondition.getHoldQuantity() - tradeQuantity);
        gridCondition.setBenchmarkPriceNew(fundCurrentPrice);

        calcGainMoney("卖",gridCondition);
    }

    private static void recordGridBuy(BigDecimal fundCurrentPrice, Date fundDate, GridCondition gridCondition) {
        // 买入均价
        BigDecimal buyAvgPrice = gridCondition.getBuyAvgPrice() == null ? fundCurrentPrice: gridCondition.getBuyAvgPrice();
        // 买入总数量
        int buyQuantity = gridCondition.getBuyQuantity();
        // 委托单次交易数量
        int tradeQuantity = gridCondition.getTradeQuantity();
        // 最新买入总数量
        int buyQuantityNew = buyQuantity + tradeQuantity;
        if(buyQuantity > 0){
            buyAvgPrice = buyAvgPrice.multiply(new BigDecimal(buyQuantity))
                    .add(fundCurrentPrice.multiply(new BigDecimal(tradeQuantity)))
                    .divide(new BigDecimal(buyQuantityNew), 3, RoundingMode.HALF_UP);
        }else{
            buyAvgPrice = fundCurrentPrice;
        }
        int holdQuantity = gridCondition.getHoldQuantity() + tradeQuantity; // 当前持有数量
        int maxQuantity = gridCondition.getMaxHoldQuantity(); // 最大持有数量
        if(holdQuantity > maxQuantity){
            maxQuantity = holdQuantity;
            gridCondition.setMaxHoldMoney(buyAvgPrice.multiply(new BigDecimal(maxQuantity)));
        }

        gridCondition.setBuyTotal(gridCondition.getBuyTotal()+1);
        gridCondition.setTradeTotal(gridCondition.getTradeTotal()+1);
        gridCondition.setBuyQuantity(buyQuantityNew);
        gridCondition.setBuyAvgPrice(buyAvgPrice);
        gridCondition.setHoldQuantity(holdQuantity);
        gridCondition.setMaxHoldQuantity(maxQuantity);
        gridCondition.setBenchmarkPriceNew(fundCurrentPrice);
        calcGainMoney("买",gridCondition);
    }

    private static void calcGainMoney(String directDesc,GridCondition gridCondition) {
        // 计算盈利
        // 买、卖相互抵消赚的钱
        BigDecimal gridGainMoney = BigDecimal.ZERO;
        int sellQuantity = gridCondition.getSellQuantity();
        if(sellQuantity > 0){
            gridGainMoney = gridCondition.getSellAvgPrice().subtract(gridCondition.getBuyAvgPrice()).multiply(new BigDecimal(sellQuantity));
        }
        // 当前价格变动导致的盈利变化
        BigDecimal fundPrice = gridCondition.getFundPrice();
        BigDecimal holdGainMoney = fundPrice.subtract(gridCondition.getBuyAvgPrice()).multiply(new BigDecimal(gridCondition.getBuyQuantity() - sellQuantity));
        // 手续费
        BigDecimal serviceFee = gridCondition.getServiceFee() == null ? BigDecimal.ZERO : gridCondition.getServiceFee();
        // 当前手续费 上海债百万分之5 0.000005，深圳十万分之5 0.00005
        BigDecimal currentFee = new BigDecimal(0.01);
        BigDecimal dealFee = fundPrice.multiply(new BigDecimal(gridCondition.getTradeQuantity())).multiply(new BigDecimal(0.000005));
        currentFee = dealFee.compareTo(currentFee) > 0 ? dealFee : currentFee;
        currentFee = currentFee.setScale(2,RoundingMode.HALF_UP);
        serviceFee = serviceFee.add(currentFee);

        gridCondition.setGainMoney(gridGainMoney.add(holdGainMoney).subtract(serviceFee));
        gridCondition.setGainQuantity(sellQuantity);
        gridCondition.setServiceFee(serviceFee);
        logger.trace("{} {}:{},当前手续费:{},累计手续费:{},网格盈利:{},持仓盈利:{},总盈利:{},当前持仓:{}，最大持仓:{}"
                , DateUtils.getStrDate(gridCondition.getTriggerTime()),directDesc, fundPrice,
                currentFee, serviceFee, gridGainMoney, holdGainMoney, gridCondition.getGainMoney(),
                gridCondition.getHoldQuantity(),gridCondition.getMaxHoldQuantity());
    }

}
