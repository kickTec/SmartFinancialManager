package com.kenick.generate.bean;

import java.util.Date;

import java.util.ArrayList;

public class User {/* feild added*/

	public static final String S_userId = "user_id";
	public static final String S_username = "username";
	public static final String S_password = "password";
	public static final String S_age = "age";
	public static final String S_gender = "gender";
	public static final String S_wxCode = "wx_code";
	public static final String S_wxName = "wx_name";
	public static final String S_modifyDate = "modify_date";
	public static final String S_createDate = "create_date";

	public static final ArrayList<String> fieldList = new ArrayList<String>() {
		{
			add("user_id");
			add("username");
			add("password");
			add("age");
			add("gender");
			add("wx_code");
			add("wx_name");
			add("modify_date");
			add("create_date");
		}	};


    /**
     * 用户id
     */
    private String userId;

    /**
     * 姓名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 微信编码
     */
    private String wxCode;

    /**
     * 微信昵称
     */
    private String wxName;

    /**
     * 修改时间
     */
    private Date modifyDate;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 用户id
     * @return user_id 用户id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 用户id
     * @param userId 用户id
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 姓名
     * @return username 姓名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 姓名
     * @param username 姓名
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 密码
     * @return password 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 密码
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 年龄
     * @return age 年龄
     */
    public Integer getAge() {
        return age;
    }

    /**
     * 年龄
     * @param age 年龄
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * 性别
     * @return gender 性别
     */
    public Integer getGender() {
        return gender;
    }

    /**
     * 性别
     * @param gender 性别
     */
    public void setGender(Integer gender) {
        this.gender = gender;
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
