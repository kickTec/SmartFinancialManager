package com.kenick.generate.bean;

import java.util.ArrayList;

public class User {/* feild added*/

	public static final String S_id = "id";
	public static final String S_username = "username";
	public static final String S_password = "password";
	public static final String S_age = "age";

	public static final ArrayList<String> fieldList = new ArrayList<String>() {
		{
			add("id");
			add("username");
			add("password");
			add("age");
		}	};


    /**
     * 用户id
     */
    private String id;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private Integer age;

    /**
     * 用户id
     * @return id 用户id
     */
    public String getId() {
        return id;
    }

    /**
     * 用户id
     * @param id 用户id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 
     * @return username 
     */
    public String getUsername() {
        return username;
    }

    /**
     * 
     * @param username 
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 
     * @return password 
     */
    public String getPassword() {
        return password;
    }

    /**
     * 
     * @param password 
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 
     * @return age 
     */
    public Integer getAge() {
        return age;
    }

    /**
     * 
     * @param age 
     */
    public void setAge(Integer age) {
        this.age = age;
    }
}
