package com.kenick.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpUtils 
{
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();

	public static String showSuccess()
	{
		return showSuccess("", "操作成功！");
	}
	
	public static String showSuccess(String message)
	{
		return showSuccess("", message);
	}
	
	public static String showSuccessStr(String data)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", true);
		result.put("message", "操作成功！");
		result.put("data", data);
		return gson.toJson(result);
	}
	
	public static String showSuccess(Object data)
	{
		return showSuccess(data, "操作成功！");
	}
	
	public static String showSuccessList(Object data, Long total,Integer pageNumber,Integer pageSize)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", true);
		result.put("message", "操作成功！");
		result.put("data", data);
		result.put("total", total);
		result.put("pageNumber", pageNumber);
		result.put("pageSize", pageSize);
		return gson.toJson(result);
	}
	
	public static String showSuccessList(Object data, Map<String, Object> map)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", true);
		result.put("message", "操作成功！");
		result.put("data", data);
		result.putAll(map);
		return gson.toJson(result);
	}
	
	public static String showSuccessList(PageInfo<?> pageInfo)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", true);
		result.put("message", "操作成功！");
		result.put("data", pageInfo.getList());
		result.put("total", pageInfo.getTotal());
		result.put("pageNumber", pageInfo.getPageNum());
		result.put("pageSize", pageInfo.getPageSize());
		return gson.toJson(result);
	}
	
	public static String showSuccess(Object data, String message)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", true);
		result.put("message", message);
		result.put("data", data);
		return gson.toJson(result);
	}
	
	public static String showFail()
	{
		return showFailNow("", "操作失败！");
	}
	
	public static String showFail(String message)
	{
		return showFailNow("", message);
	}
	public static String showFail(Object data)
	{
		return showFailNow(data, "操作失败！");
	}
	
	
	public static String showFail(Object data, String message)
	{
		return showFailNow(data, message);
	}
	
	// 原为 showFail 由于和 showFail(String errorCode, String errorMessage, String message) 冲突，改
	public static String showFailNow(Object data, String message)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", false);
		result.put("message", message);
		result.put("data", data);
		return gson.toJson(result);
	}

	public static String showException(Throwable t)
	{
		return showFailNow("", StringUtils.isEmpty(t.getMessage()) ? t.toString() : t.getMessage());
	}
	
	public static String showException(Object data, Throwable t)
	{
		StringBuffer messageBuffer = new StringBuffer();
		messageBuffer.append(t.toString()).append(":").append(t.getMessage());
		return showFail(data,  messageBuffer.toString());
	}
	
	public static String showException(String errorCode, String errorMessage, Throwable t)
	{
		HashMap<String,String> retrunMap = new HashMap<String,String>();
		retrunMap.put("errorCode", errorCode);
		retrunMap.put("errorMessage", errorMessage);
		StringBuffer messageBuffer = new StringBuffer();
		messageBuffer.append(t.toString()).append(":").append(t.getMessage());
		return showFail(retrunMap, messageBuffer.toString());
	}
	
	public static String showFail(String errorCode, String errorMessage, String message)
	{
		HashMap<String,String> retrunMap = new HashMap<String,String>();
		retrunMap.put("errorCode", errorCode);
		retrunMap.put("errorMessage", errorMessage);
		return showFail(retrunMap, message);
	}
	
	public static String showFail(String errorCode, String errorMessage)
	{
		HashMap<String,String> retrunMap = new HashMap<String,String>();
		retrunMap.put("errorCode", errorCode);
		retrunMap.put("errorMessage", errorMessage);
		return showFail(retrunMap, "操作失败！");
	}
	
	public static void main(String[] args) {
		/*System.out.println(showSuccess());
		
		UserInfo user = new UserInfo();
		user.setUserId("123456");
		Gson json = new Gson();
		
		System.out.println(json.toJson(user));
		UserInfo user2 = new UserInfo();
		user2.setUserId("5323");
		System.out.println(json.toJson(user2));*/
		
		/*System.out.println(showFail(new HashMap<String,String>()));*/

		/*String data = "{\"ddd\":\"ddd\"}";
		System.out.println(data);
		System.out.println(gson.toJson(data).toString());*/
		
		String body = showFail("gateway_userlogin_verify_busi","Failed to get login information","Failed to get login information");
		System.out.println(body);
		JSONObject data = JSON.parseObject(body);
		
		System.out.println(data.getJSONObject("data"));
		
	}
}
