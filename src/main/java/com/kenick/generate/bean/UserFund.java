package com.kenick.generate.bean;

import java.util.ArrayList;

public class UserFund {/* feild added*/

	public static final String S_id = "id";
	public static final String S_userid = "userid";
	public static final String S_fundid = "fundid";

	public static final ArrayList<String> fieldList = new ArrayList<String>() {
		{
			add("id");
			add("userid");
			add("fundid");
		}	};


    /**
     * 
     */
    private Integer id;

    /**
     * 用户id
     */
    private String userid;

    /**
     * 基金id
     */
    private String fundid;

    /**
     * 
     * @return id 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 用户id
     * @return userId 用户id
     */
    public String getUserid() {
        return userid;
    }

    /**
     * 用户id
     * @param userid 用户id
     */
    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    /**
     * 基金id
     * @return fundId 基金id
     */
    public String getFundid() {
        return fundid;
    }

    /**
     * 基金id
     * @param fundid 基金id
     */
    public void setFundid(String fundid) {
        this.fundid = fundid == null ? null : fundid.trim();
    }
}
