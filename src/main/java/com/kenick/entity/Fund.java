package com.kenick.entity;

import java.math.BigDecimal;

public class Fund {
	private Integer id;
	private String code; // 基金编码
	private String name; // 基金名称
	private String curTime; // 当前估算时间
	private Double curNetValue; // 当前估算净值
	private Double curGain; // 当前估算涨幅
	private Double lastNetValue; // 上一日净值
	private Double lastGain; // 上一日涨幅
	private BigDecimal gainTotal; // 累计涨幅
	
	public Fund(){}
	
	public Fund(Object[] fundInfo){
		this.code = fundInfo[0].toString();
		this.name = fundInfo[1].toString();
		this.curTime = fundInfo[2].toString();
		this.curNetValue = Double.valueOf(fundInfo[3].toString());
		this.curGain = Double.valueOf(fundInfo[4].toString());
		this.lastNetValue = Double.valueOf(fundInfo[5].toString());
		this.lastGain = Double.valueOf(fundInfo[6].toString());
		this.gainTotal = BigDecimal.valueOf(this.curGain+this.lastGain);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurTime() {
		return curTime;
	}

	public void setCurTime(String curTime) {
		this.curTime = curTime;
	}

	public Double getCurNetValue() {
		return curNetValue;
	}

	public void setCurNetValue(Double curNetValue) {
		this.curNetValue = curNetValue;
	}

	public Double getCurGain() {
		return curGain;
	}

	public void setCurGain(Double curGain) {
		this.curGain = curGain;
	}

	public Double getLastNetValue() {
		return lastNetValue;
	}

	public void setLastNetValue(Double lastNetValue) {
		this.lastNetValue = lastNetValue;
	}

	public Double getLastGain() {
		return lastGain;
	}

	public void setLastGain(Double lastGain) {
		this.lastGain = lastGain;
	}
	
	public BigDecimal getGainTotal() {
		return gainTotal;
	}

	public void setGainTotal(BigDecimal gainTotal) {
		this.gainTotal = gainTotal;
	}

	@Override
	public String toString() {
		return "Fund [id=" + id + ", code=" + code + ", name=" + name + ", curTime=" + curTime + ", curNetValue="
				+ curNetValue + ", curGain=" + curGain + ", lastNetValue=" + lastNetValue + ", lastGain=" + lastGain
				+ ", gainTotal=" + gainTotal + "]";
	}
}
