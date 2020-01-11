package com.kenick.generate.bean;

import java.util.Date;

import java.util.ArrayList;

public class ConstantData {/* feild added*/

	public static final String S_constantId = "constant_id";
	public static final String S_constantName = "constant_name";
	public static final String S_constantValue = "constant_value";
	public static final String S_constantDesc = "constant_desc";
	public static final String S_constantState = "constant_state";
	public static final String S_modifyDate = "modify_date";
	public static final String S_createDate = "create_date";

	public static final ArrayList<String> fieldList = new ArrayList<String>() {
		{
			add("constant_id");
			add("constant_name");
			add("constant_value");
			add("constant_desc");
			add("constant_state");
			add("modify_date");
			add("create_date");
		}	};


    /**
     * 常量id
     */
    private String constantId;

    /**
     * 常量名
     */
    private String constantName;

    /**
     * 常量值
     */
    private String constantValue;

    /**
     * 常量说明
     */
    private String constantDesc;

    /**
     * 常量状态(0.失效1.正常)
     */
    private Integer constantState;

    /**
     * 修改时间
     */
    private Date modifyDate;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 常量id
     * @return constant_id 常量id
     */
    public String getConstantId() {
        return constantId;
    }

    /**
     * 常量id
     * @param constantId 常量id
     */
    public void setConstantId(String constantId) {
        this.constantId = constantId == null ? null : constantId.trim();
    }

    /**
     * 常量名
     * @return constant_name 常量名
     */
    public String getConstantName() {
        return constantName;
    }

    /**
     * 常量名
     * @param constantName 常量名
     */
    public void setConstantName(String constantName) {
        this.constantName = constantName == null ? null : constantName.trim();
    }

    /**
     * 常量值
     * @return constant_value 常量值
     */
    public String getConstantValue() {
        return constantValue;
    }

    /**
     * 常量值
     * @param constantValue 常量值
     */
    public void setConstantValue(String constantValue) {
        this.constantValue = constantValue == null ? null : constantValue.trim();
    }

    /**
     * 常量说明
     * @return constant_desc 常量说明
     */
    public String getConstantDesc() {
        return constantDesc;
    }

    /**
     * 常量说明
     * @param constantDesc 常量说明
     */
    public void setConstantDesc(String constantDesc) {
        this.constantDesc = constantDesc == null ? null : constantDesc.trim();
    }

    /**
     * 常量状态(0.失效1.正常)
     * @return constant_state 常量状态(0.失效1.正常)
     */
    public Integer getConstantState() {
        return constantState;
    }

    /**
     * 常量状态(0.失效1.正常)
     * @param constantState 常量状态(0.失效1.正常)
     */
    public void setConstantState(Integer constantState) {
        this.constantState = constantState;
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
}
