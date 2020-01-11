package com.kenick.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class JsonUtils 
{
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
	
	public static Map<String, Object> bean2Map(Object obj)
	{
		if (null == obj)
		{
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		String json = gson.toJson(obj);
		if (StringUtils.isEmpty(json))
		{
			map.put("errorCode", "sys_data_serialize_error");
			map.put("errorMessage", "数据序列化异常！");
			return map;
		}
		JSONObject jsonObj = JSON.parseObject(json);

		return jsonObj;
	}
	
	public static Map<String, String> getStrMap(JSONObject jsonObj)
	{
		
		Map<String, String> map = new HashMap<String, String>();
		for (String key : jsonObj.keySet())
		{
			if (null == jsonObj.get(key))
			{
				map.put(key, "");
			}
			else
			{
				map.put(key, jsonObj.get(key).toString());
			}
		}
		return map;
	}
	
	public static Map<String, Object> getMap(JSONObject jsonObj)
	{
		
		Map<String, Object> map = new HashMap<String, Object>();
		for (String key : jsonObj.keySet())
		{
			if (null == jsonObj.get(key))
			{
				map.put(key, "");
			}
			else
			{
				map.put(key, jsonObj.get(key));
			}
		}
		return map;
	}
	
	public static Map<String, String> bean2StrMap(Object obj)
	{
		if (null == obj)
		{
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		String json = gson.toJson(obj);
		if (StringUtils.isEmpty(json))
		{
			map.put("errorCode", "sys_data_serialize_error");
			map.put("errorMessage", "数据序列化异常！");
			return map;
		}
		JSONObject jsonObj = JSON.parseObject(json);
		map = getStrMap(jsonObj);
		
		return map;
	}
	public static Object map2Bean(Class<?> clazz, Map<String, String> map)
	{
		String json = gson.toJson(map);
		return JSON.parseObject(json, clazz);
		
	}
	
	public static JSONObject bean2JSON(Object obj)
	{
		return JSON.parseObject(gson.toJson(obj));
	}
	
	public static Object beanCopy(Object obj, Class<?> clazz)
	{
		return JSON.parseObject(gson.toJson(obj), clazz);
	}

	public static <T>T copyObjToBean(Object obj, Class<T> clazz){
		return JSON.parseObject(gson.toJson(obj), clazz);
	}
	
	public static String bean2JsonStr(Object obj)
	{
		return gson.toJson(obj);
	}

	
	public static JSONArray sortJsonArrayByDate(JSONArray jsonArr, String dateName){
		int size = jsonArr.size();
		 for(int i = 0; i < size; i++)
		 {
			 for (int j = 0; j < size -i -1; j++)
			 {
				 JSONObject jObj = (JSONObject) jsonArr.get(j);
				 JSONObject j_Obj = (JSONObject) jsonArr.get(j + 1);
				 if (jObj.getString(dateName).compareTo(j_Obj.getString(dateName)) < 0)
				 {
					 jsonArr.set(j, j_Obj);
					 jsonArr.set(j+1, jObj);
				 }
			 }
		 }
		
	    return jsonArr;
	}
	
	@SuppressWarnings("unchecked")
	public static  Map<String, Object> string2Map(String str)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map = gson.fromJson(str, map.getClass());
		return map;
		
	}
	
	
	//判断json格式字符串是否由"["开头，"]"结尾，如果不是就加上
	public static String judgeJson(String json){
		
		char c = json.charAt(0);
		
		String str = String.valueOf(c);
		
		if(!str.equals("[")){
			
			json = "["+json+"]";
			return json;
		}
		return json;
	}

	// 从json对象中获取当前页码
	public static Integer getPageNumFromJson(JSONObject jsonObject, String key){
		Integer pageNumber = jsonObject.getInteger(key);
		pageNumber = null == pageNumber || pageNumber < 1 ? 1 : pageNumber;
		return pageNumber;
	}

	// 从json对象中获取每页大小
	public static Integer getPageSizeFromJson(JSONObject jsonObject, String key){
		Integer pageSize = jsonObject.getInteger(key);
		pageSize = null == pageSize || pageSize < 0 ? 20 : pageSize;
		return pageSize;
	}
}
