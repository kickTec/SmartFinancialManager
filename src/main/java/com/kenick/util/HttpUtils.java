package com.kenick.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();

    public static String showSuccess() {
        return showSuccess("", "操作成功！");
    }

    public static String showSuccess(String message) {
        return showSuccess("", message);
    }

    public static String showSuccessStr(String data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("flag", true);
        result.put("message", "操作成功！");
        result.put("data", data);
        return gson.toJson(result);
    }

    public static String showSuccess(Object data) {
        return showSuccess(data, "操作成功！");
    }

    public static String showSuccessList(Object data, Long total, Integer pageNumber, Integer pageSize) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("flag", true);
        result.put("message", "操作成功！");
        result.put("data", data);
        result.put("total", total);
        result.put("pageNumber", pageNumber);
        result.put("pageSize", pageSize);
        return gson.toJson(result);
    }

    public static String showSuccessList(Object data, Map<String, Object> map) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("flag", true);
        result.put("message", "操作成功！");
        result.put("data", data);
        result.putAll(map);
        return gson.toJson(result);
    }

    public static String showSuccess(Object data, String message) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("flag", true);
        result.put("message", message);
        result.put("data", data);
        return gson.toJson(result);
    }

    public static String showFail() {
        return showFailNow("", "操作失败！");
    }

    public static String showFail(String message) {
        return showFailNow("", message);
    }

    public static String showFail(Object data) {
        return showFailNow(data, "操作失败！");
    }


    public static String showFail(Object data, String message) {
        return showFailNow(data, message);
    }

    // 原为 showFail 由于和 showFail(String errorCode, String errorMessage, String message) 冲突，改
    public static String showFailNow(Object data, String message) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("flag", false);
        result.put("message", message);
        result.put("data", data);
        return gson.toJson(result);
    }

    public static String showException(Throwable t) {
        return showFailNow("", StringUtils.isEmpty(t.getMessage()) ? t.toString() : t.getMessage());
    }

    public static String showException(Object data, Throwable t) {
        StringBuffer messageBuffer = new StringBuffer();
        messageBuffer.append(t.toString()).append(":").append(t.getMessage());
        return showFail(data, messageBuffer.toString());
    }

    public static String showException(String errorCode, String errorMessage, Throwable t) {
        HashMap<String, String> retrunMap = new HashMap<String, String>();
        retrunMap.put("errorCode", errorCode);
        retrunMap.put("errorMessage", errorMessage);
        StringBuffer messageBuffer = new StringBuffer();
        messageBuffer.append(t.toString()).append(":").append(t.getMessage());
        return showFail(retrunMap, messageBuffer.toString());
    }

    public static String showFail(String errorCode, String errorMessage, String message) {
        HashMap<String, String> retrunMap = new HashMap<String, String>();
        retrunMap.put("errorCode", errorCode);
        retrunMap.put("errorMessage", errorMessage);
        return showFail(retrunMap, message);
    }

    public static String showFail(String errorCode, String errorMessage) {
        HashMap<String, String> retrunMap = new HashMap<String, String>();
        retrunMap.put("errorCode", errorCode);
        retrunMap.put("errorMessage", errorMessage);
        return showFail(retrunMap, "操作失败！");
    }

    //判断请求是否来自桌面端
    public static boolean isPc(String userAgent) {
        if (StringUtils.isBlank(userAgent)) {
            return false;
        }
        String[] androidDescArray = {"Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"};
        for (String androidDesc : androidDescArray) {
            if (userAgent.contains(androidDesc)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
    }

}
