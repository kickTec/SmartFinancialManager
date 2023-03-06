package com.kenick.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
 
public class HttpRequestUtils {
    private static Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);    //日志记录
    
    public static String httpGetString(String url, String charSet){
        //get请求返回结果
        try {
        	CloseableHttpClient client = HttpClientBuilder.create().build();
            //发送get请求
            HttpGet request = new HttpGet(url);
            
            HttpResponse response = client.execute(request);
 
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity(), charSet);

               // url = URLDecoder.decode(url, "UTF-8");
                return strResult;
            } else {
                logger.error("get请求提交失败:{} ,返回内容：{}", url, EntityUtils.toString(response.getEntity()));
                return null;
            }
        } catch (IOException e) {
            logger.error("get请求提交失败:" + url, e);
            return null;
        }
    }
    
    public static void main(String[] args) {
    	Date now = new Date();
    	String url = "http://fundgz.1234567.com.cn/js/519727.js?rt="+now.getTime();
    	String retStr = HttpRequestUtils.httpGetString(url, StandardCharsets.UTF_8.name());
    	
    	String retJsonStr = retStr.substring(8, retStr.length()-2);
    	JSONObject retJson = JSONObject.parseObject(retJsonStr);
    	System.out.println(retJson.toJSONString());
	}
}
