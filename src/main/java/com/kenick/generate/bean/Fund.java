package com.kenick.generate.bean;

import java.math.BigDecimal;

import java.util.ArrayList;

import java.util.ArrayList;

public class Fund {/* feild added*/

	public static final String S_id = "id";
	public static final String S_code = "code";
	public static final String S_name = "name";
	public static final String S_curTime = "cur_time";
	public static final String S_curNetValue = "cur_net_value";
	public static final String S_curGain = "cur_gain";
	public static final String S_lastNetValue = "last_net_value";
	public static final String S_lastGain = "last_gain";
	public static final String S_gainTotal = "gain_total";

	public static final ArrayList<String> fieldList = new ArrayList<String>() {
		{
			add("id");
			add("code");
			add("name");
			add("cur_time");
			add("cur_net_value");
			add("cur_gain");
			add("last_net_value");
			add("last_gain");
			add("gain_total");
		}	};


    /**
     * 基金id
     */
    private Integer id;

    /**
     * 基金编码
     */
    private String code;

    /**
     * 基金名称
     */
    private String name;

    /**
     * 当前估算时间
     */
    private String curTime;

    /**
     * 当前估算净值
     */
    private Double curNetValue;

    /**
     * 当前涨幅
     */
    private Double curGain;

    /**
     * 上一日净值
     */
    private Double lastNetValue;

    /**
     * 上一日涨幅
     */
    private Double lastGain;

    /**
     * 累计涨幅（连续2天）
     */
    private BigDecimal gainTotal;

    /**
     * 基金id
     * @return id 基金id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 基金id
     * @param id 基金id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 基金编码
     * @return code 基金编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 基金编码
     * @param code 基金编码
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 基金名称
     * @return name 基金名称
     */
    public String getName() {
        return name;
    }

    /**
     * 基金名称
     * @param name 基金名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 当前估算时间
     * @return cur_time 当前估算时间
     */
    public String getCurTime() {
        return curTime;
    }

    /**
     * 当前估算时间
     * @param curTime 当前估算时间
     */
    public void setCurTime(String curTime) {
        this.curTime = curTime == null ? null : curTime.trim();
    }

    /**
     * 当前估算净值
     * @return cur_net_value 当前估算净值
     */
    public Double getCurNetValue() {
        return curNetValue;
    }

    /**
     * 当前估算净值
     * @param curNetValue 当前估算净值
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
     * 上一日净值
     * @return last_net_value 上一日净值
     */
    public Double getLastNetValue() {
        return lastNetValue;
    }

    /**
     * 上一日净值
     * @param lastNetValue 上一日净值
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
}
