package com.kenick.user.bean;

import java.util.ArrayList;

public class UserFundKey {/* feild added*/


	public static final ArrayList<String> fieldList = new ArrayList<String>() {
		{
		}	};


    /**
     * 用户id
     */
    private String userId;

    /**
     * 基金编码
     */
    private String fundCode;

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
}
