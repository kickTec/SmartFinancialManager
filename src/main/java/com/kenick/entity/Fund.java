package com.kenick.entity;

public class Fund {
	private Integer id;
	private String code; // 基金编码
	private String name; // 基金名称
	private String curTime; // 当前估算时间
	private Double curNetValue; // 当前估算净值
	private Double lastNetValue; // 上一日净值
	private Double lastGain; // 上一日涨幅
	
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
	
	@Override
	public String toString() {
		return "Fund [id=" + id + ", code=" + code + ", name=" + name
				+ ", curNetValue=" + curNetValue + ", lastNetValue="
				+ lastNetValue + ", lastGain=" + lastGain + "]";
	}
}
