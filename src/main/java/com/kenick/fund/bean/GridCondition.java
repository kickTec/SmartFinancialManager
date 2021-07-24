package com.kenick.fund.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <一句话功能简述> 网格条件
 * <功能详细描述> 
 * author: zhanggw
 * 创建时间:  2021/7/23
 */
public class GridCondition {

    private String fundCode; // 股票或基金代码
    private BigDecimal fundPrice; // 股票或基金价格
    private int holdQuantity; // 当前持有数量
    private int maxHoldQuantity; // 最大持有数量
    private BigDecimal maxHoldMoney; // 最大持有金额
    private BigDecimal benchmarkPriceInit; // 初始基准价
    private BigDecimal benchmarkPriceNew; // 最新基准价
    private BigDecimal GridInterval; // 网格间隔
    private int tradeQuantity; // 委托交易数量
    private int buyTotal; // 买入总次数
    private int sellTotal; // 卖出总次数
    private int tradeTotal; // 交易总次数
    private BigDecimal buyAvgPrice; // 买入均价
    private int buyQuantity; // 买入总数量
    private BigDecimal sellAvgPrice; // 卖出均价
    private int sellQuantity; // 卖出总数量
    private BigDecimal gainMoney; // 盈利金额
    private int gainQuantity; // 盈利数量
    private String extJson; // 扩展json
    private BigDecimal serviceFee; // 手续费
    private Date triggerTime; // 触发时间

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public BigDecimal getFundPrice() {
        return fundPrice;
    }

    public void setFundPrice(BigDecimal fundPrice) {
        this.fundPrice = fundPrice;
    }

    public int getHoldQuantity() {
        return holdQuantity;
    }

    public void setHoldQuantity(int holdQuantity) {
        this.holdQuantity = holdQuantity;
    }

    public int getMaxHoldQuantity() {
        return maxHoldQuantity;
    }

    public void setMaxHoldQuantity(int maxHoldQuantity) {
        this.maxHoldQuantity = maxHoldQuantity;
    }

    public BigDecimal getMaxHoldMoney() {
        return maxHoldMoney;
    }

    public void setMaxHoldMoney(BigDecimal maxHoldMoney) {
        this.maxHoldMoney = maxHoldMoney;
    }

    public BigDecimal getBenchmarkPriceInit() {
        return benchmarkPriceInit;
    }

    public void setBenchmarkPriceInit(BigDecimal benchmarkPriceInit) {
        this.benchmarkPriceInit = benchmarkPriceInit;
    }

    public BigDecimal getBenchmarkPriceNew() {
        return benchmarkPriceNew;
    }

    public void setBenchmarkPriceNew(BigDecimal benchmarkPriceNew) {
        this.benchmarkPriceNew = benchmarkPriceNew;
    }

    public BigDecimal getGridInterval() {
        return GridInterval;
    }

    public void setGridInterval(BigDecimal gridInterval) {
        GridInterval = gridInterval;
    }

    public int getTradeQuantity() {
        return tradeQuantity;
    }

    public void setTradeQuantity(int tradeQuantity) {
        this.tradeQuantity = tradeQuantity;
    }

    public int getBuyTotal() {
        return buyTotal;
    }

    public void setBuyTotal(int buyTotal) {
        this.buyTotal = buyTotal;
    }

    public int getSellTotal() {
        return sellTotal;
    }

    public void setSellTotal(int sellTotal) {
        this.sellTotal = sellTotal;
    }

    public int getTradeTotal() {
        return tradeTotal;
    }

    public void setTradeTotal(int tradeTotal) {
        this.tradeTotal = tradeTotal;
    }

    public BigDecimal getBuyAvgPrice() {
        return buyAvgPrice;
    }

    public void setBuyAvgPrice(BigDecimal buyAvgPrice) {
        this.buyAvgPrice = buyAvgPrice;
    }

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public void setBuyQuantity(int buyQuantity) {
        this.buyQuantity = buyQuantity;
    }

    public BigDecimal getSellAvgPrice() {
        return sellAvgPrice;
    }

    public void setSellAvgPrice(BigDecimal sellAvgPrice) {
        this.sellAvgPrice = sellAvgPrice;
    }

    public int getSellQuantity() {
        return sellQuantity;
    }

    public void setSellQuantity(int sellQuantity) {
        this.sellQuantity = sellQuantity;
    }

    public BigDecimal getGainMoney() {
        return gainMoney;
    }

    public void setGainMoney(BigDecimal gainMoney) {
        this.gainMoney = gainMoney;
    }

    public int getGainQuantity() {
        return gainQuantity;
    }

    public void setGainQuantity(int gainQuantity) {
        this.gainQuantity = gainQuantity;
    }

    public String getExtJson() {
        return extJson;
    }

    public void setExtJson(String extJson) {
        this.extJson = extJson;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    @Override
    public String toString() {
        return "GridCondition{" +
                "fundCode='" + fundCode + '\'' +
                ", fundPrice=" + fundPrice +
                ", holdQuantity=" + holdQuantity +
                ", maxHoldQuantity=" + maxHoldQuantity +
                ", maxHoldMoney=" + maxHoldMoney +
                ", benchmarkPriceInit=" + benchmarkPriceInit +
                ", benchmarkPriceNew=" + benchmarkPriceNew +
                ", GridInterval=" + GridInterval +
                ", tradeQuantity=" + tradeQuantity +
                ", buyTotal=" + buyTotal +
                ", sellTotal=" + sellTotal +
                ", tradeTotal=" + tradeTotal +
                ", buyAvgPrice=" + buyAvgPrice +
                ", buyQuantity=" + buyQuantity +
                ", sellAvgPrice=" + sellAvgPrice +
                ", sellQuantity=" + sellQuantity +
                ", gainMoney=" + gainMoney +
                ", gainQuantity=" + gainQuantity +
                ", extJson='" + extJson + '\'' +
                ", serviceFee=" + serviceFee +
                ", triggerTime=" + triggerTime +
                '}';
    }

}
