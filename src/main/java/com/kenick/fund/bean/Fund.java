package com.kenick.fund.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class Fund {/* feild added*/

	public static final String S_fundCode = "fund_code";
	public static final String S_fundName = "fund_name";
	public static final String S_type = "type";
	public static final String S_curTime = "cur_time";
	public static final String S_curNetValue = "cur_net_value";
	public static final String S_curGain = "cur_gain";
	public static final String S_curPriceHighest = "cur_price_highest";
	public static final String S_curPriceLowest = "cur_price_lowest";
	public static final String S_lastNetValue = "last_net_value";
	public static final String S_lastGain = "last_gain";
	public static final String S_lastPriceHighest = "last_price_highest";
	public static final String S_lastPriceLowest = "last_price_lowest";
	public static final String S_gainTotal = "gain_total";
	public static final String S_fundState = "fund_state";
	public static final String S_modifyDate = "modify_date";
	public static final String S_createDate = "create_date";

	public static final ArrayList<String> fieldList = new ArrayList<String>() {
		{
			add("fund_code");
			add("fund_name");
			add("type");
			add("cur_time");
			add("cur_net_value");
			add("cur_gain");
			add("cur_price_highest");
			add("cur_price_lowest");
			add("last_net_value");
			add("last_gain");
			add("last_price_highest");
			add("last_price_lowest");
			add("gain_total");
			add("fund_state");
			add("modify_date");
			add("create_date");
		}	};


    /**
     * 基金编码
     */
    private String fundCode;

    /**
     * 基金名称
     */
    private String fundName;

    /**
     * 类型（1：基金 2：股票）
     */
    private Integer type;

    /**
     * 当前时间
     */
    private String curTime;

    /**
     * 当前价
     */
    private Double curNetValue;

    /**
     * 当前涨幅
     */
    private Double curGain;

    /**
     * 当前最高价
     */
    private Double curPriceHighest;

    /**
     * 当前最低价
     */
    private Double curPriceLowest;

    /**
     * 上一日价格
     */
    private Double lastNetValue;

    /**
     * 上一日涨幅
     */
    private Double lastGain;

    /**
     * 上一日最高价
     */
    private Double lastPriceHighest;

    /**
     * 上一日最低价
     */
    private Double lastPriceLowest;

    /**
     * 累计涨幅（连续2天）
     */
    private BigDecimal gainTotal;

    /**
     * 基金状态
     */
    private Integer fundState;

    /**
     * 修改时间
     */
    private Date modifyDate;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 基金编码
     * @return fund_code 基金编码
     */
    public String getFundCode() {
        return fundCode;
    }

    /**
     * 基金编码
     * @param fundCode 基金编码
     */
    public void setFundCode(String fundCode) {
        this.fundCode = fundCode == null ? null : fundCode.trim();
    }

    /**
     * 基金名称
     * @return fund_name 基金名称
     */
    public String getFundName() {
        return fundName;
    }

    /**
     * 基金名称
     * @param fundName 基金名称
     */
    public void setFundName(String fundName) {
        this.fundName = fundName == null ? null : fundName.trim();
    }

    /**
     * 类型（1：基金 2：股票）
     * @return type 类型（1：基金 2：股票）
     */
    public Integer getType() {
        return type;
    }

    /**
     * 类型（1：基金 2：股票）
     * @param type 类型（1：基金 2：股票）
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 当前时间
     * @return cur_time 当前时间
     */
    public String getCurTime() {
        return curTime;
    }

    /**
     * 当前时间
     * @param curTime 当前时间
     */
    public void setCurTime(String curTime) {
        this.curTime = curTime == null ? null : curTime.trim();
    }

    /**
     * 当前价
     * @return cur_net_value 当前价
     */
    public Double getCurNetValue() {
        return curNetValue;
    }

    /**
     * 当前价
     * @param curNetValue 当前价
     */
    public void setCurNetValue(Double curNetValue) {
        this.curNetValue = curNetValue;
    }

    /**
     * 当前涨幅
     * @return cur_gain 当前涨幅
     */
    public Double getCurGain() {
        return curGain;
    }

    /**
     * 当前涨幅
     * @param curGain 当前涨幅
     */
    public void setCurGain(Double curGain) {
        this.curGain = curGain;
    }

    /**
     * 当前最高价
     * @return cur_price_highest 当前最高价
     */
    public Double getCurPriceHighest() {
        return curPriceHighest;
    }

    /**
     * 当前最高价
     * @param curPriceHighest 当前最高价
     */
    public void setCurPriceHighest(Double curPriceHighest) {
        this.curPriceHighest = curPriceHighest;
    }

    /**
     * 当前最低价
     * @return cur_price_lowest 当前最低价
     */
    public Double getCurPriceLowest() {
        return curPriceLowest;
    }

    /**
     * 当前最低价
     * @param curPriceLowest 当前最低价
     */
    public void setCurPriceLowest(Double curPriceLowest) {
        this.curPriceLowest = curPriceLowest;
    }

    /**
     * 上一日价格
     * @return last_net_value 上一日价格
     */
    public Double getLastNetValue() {
        return lastNetValue;
    }

    /**
     * 上一日价格
     * @param lastNetValue 上一日价格
     */
    public void setLastNetValue(Double lastNetValue) {
        this.lastNetValue = lastNetValue;
    }

    /**
     * 上一日涨幅
     * @return last_gain 上一日涨幅
     */
    public Double getLastGain() {
        return lastGain;
    }

    /**
     * 上一日涨幅
     * @param lastGain 上一日涨幅
     */
    public void setLastGain(Double lastGain) {
        this.lastGain = lastGain;
    }

    /**
     * 上一日最高价
     * @return last_price_highest 上一日最高价
     */
    public Double getLastPriceHighest() {
        return lastPriceHighest;
    }

    /**
     * 上一日最高价
     * @param lastPriceHighest 上一日最高价
     */
    public void setLastPriceHighest(Double lastPriceHighest) {
        this.lastPriceHighest = lastPriceHighest;
    }

    /**
     * 上一日最低价
     * @return last_price_lowest 上一日最低价
     */
    public Double getLastPriceLowest() {
        return lastPriceLowest;
    }

    /**
     * 上一日最低价
     * @param lastPriceLowest 上一日最低价
     */
    public void setLastPriceLowest(Double lastPriceLowest) {
        this.lastPriceLowest = lastPriceLowest;
    }

    /**
     * 累计涨幅（连续2天）
     * @return gain_total 累计涨幅（连续2天）
     */
    public BigDecimal getGainTotal() {
        return gainTotal;
    }

    /**
     * 累计涨幅（连续2天）
     * @param gainTotal 累计涨幅（连续2天）
     */
    public void setGainTotal(BigDecimal gainTotal) {
        this.gainTotal = gainTotal;
    }

    /**
     * 基金状态
     * @return fund_state 基金状态
     */
    public Integer getFundState() {
        return fundState;
    }

    /**
     * 基金状态
     * @param fundState 基金状态
     */
    public void setFundState(Integer fundState) {
        this.fundState = fundState;
    }

    /**
     * 修改时间
     * @return modify_date 修改时间
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * 修改时间
     * @param modifyDate 修改时间
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * 创建时间
     * @return create_date 创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 创建时间
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Fund{" +
                "fundCode='" + fundCode + '\'' +
                ", fundName='" + fundName + '\'' +
                ", type=" + type +
                ", curTime='" + curTime + '\'' +
                ", curNetValue=" + curNetValue +
                ", curGain=" + curGain +
                ", curPriceHighest=" + curPriceHighest +
                ", curPriceLowest=" + curPriceLowest +
                ", lastNetValue=" + lastNetValue +
                ", lastGain=" + lastGain +
                ", lastPriceHighest=" + lastPriceHighest +
                ", lastPriceLowest=" + lastPriceLowest +
                ", gainTotal=" + gainTotal +
                ", fundState=" + fundState +
                ", modifyDate=" + modifyDate +
                ", createDate=" + createDate +
                '}';
    }
}
