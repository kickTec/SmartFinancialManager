package com.kenick.user.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class UserFund extends UserFundKey {/* feild added*/

	public static final String S_userId = "user_id";
	public static final String S_username = "username";
	public static final String S_wxCode = "wx_code";
	public static final String S_wxName = "wx_name";
	public static final String S_fundCode = "fund_code";
	public static final String S_fundName = "fund_name";
	public static final String S_curTime = "cur_time";
	public static final String S_curNetValue = "cur_net_value";
	public static final String S_curGain = "cur_gain";
	public static final String S_lastNetValue = "last_net_value";
	public static final String S_lastGain = "last_gain";
	public static final String S_gainTotal = "gain_total";
	public static final String S_createDate = "create_date";

	public static final ArrayList<String> fieldList = new ArrayList<String>() {
		{
			add("user_id");
			add("username");
			add("wx_code");
			add("wx_name");
			add("fund_code");
			add("fund_name");
			add("cur_time");
			add("cur_net_value");
			add("cur_gain");
			add("last_net_value");
			add("last_gain");
			add("gain_total");
			add("create_date");
		}	};


    /**
     * 用户姓名
     */
    private String username;

    /**
     * 微信编码
     */
    private String wxCode;

    /**
     * 微信昵称
     */
    private String wxName;

    /**
     * 基金名称
     */
    private String fundName;

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
     * 创建时间
     */
    private Date createDate;

    /**
     * 用户姓名
     * @return username 用户姓名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 用户姓名
     * @param username 用户姓名
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 微信编码
     * @return wx_code 微信编码
     */
    public String getWxCode() {
        return wxCode;
    }

    /**
     * 微信编码
     * @param wxCode 微信编码
     */
    public void setWxCode(String wxCode) {
        this.wxCode = wxCode == null ? null : wxCode.trim();
    }

    /**
     * 微信昵称
     * @return wx_name 微信昵称
     */
    public String getWxName() {
        return wxName;
    }

    /**
     * 微信昵称
     * @param wxName 微信昵称
     */
    public void setWxName(String wxName) {
        this.wxName = wxName == null ? null : wxName.trim();
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
}
