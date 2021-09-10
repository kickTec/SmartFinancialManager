package com.kenick.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kenick.fund.bean.Fund;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author: zhanggw
 * 创建时间:  2020/3/10
 */
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    public static void main(String[] args) {
        String filePath = "D:/tmp/history/600036/2021-07-21.txt";
        List<String> dataList = getTextListFromFile(new File(filePath));
        logger.debug("dataList:{}", dataList);
    }


    public static void downloadFileConcurrent(String url, String localPatch) {
        fixedThreadPool.execute(() -> download(url, localPatch));
    }

    public static void download(String _url, String path) {
        OutputStream os = null;
        File sf = null;
        try {
            URL url = new URL(_url);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(5000);
            InputStream is = con.getInputStream();
            byte[] bs = new byte[1024];
            int len;
            sf = new File(path);
            os = new FileOutputStream(sf);
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            os.close();
            is.close();
        } catch (IOException e) {
            logger.debug("下载异常!", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static Properties getProperties(String location) {
        Properties properties = null;
        try {
            properties = PropertiesLoaderUtils.loadProperties(new EncodedResource(new ClassPathResource(location), "UTF-8"));
        } catch (Exception e) {
            logger.error("加载配置失败!", e);
        }
        return properties;
    }

    public static String getProperty(String location, String key) {
        String ret = null;
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(new EncodedResource(new ClassPathResource(location), "UTF-8"));
            ret = properties.get(key).toString();
            if(ret.contains("ENC(")){
                String encryKey = System.getProperty("jasypt.encryptor.password");
                if(StringUtils.isNotBlank(encryKey)){
                    ret = ret.substring(4, ret.length()-1);
                    ret = EncryUtil.decryptByJasypt(encryKey, ret);
                }
            }
        } catch (Exception e) {
            logger.error("加载配置失败!", e);
        }
        return ret;
    }

    public static String getPropertyByEnv(String key) {
        String ret = null;
        try {
            String env = System.getProperty("spring.profiles.active");
            if(StringUtils.isBlank(env)){
                env = "local";
            }
            Properties properties = PropertiesLoaderUtils.loadProperties(new EncodedResource(new ClassPathResource("application-"+env+".properties"), "UTF-8"));
            if("cloud".equals(env)){
                properties = PropertiesLoaderUtils.loadProperties(new EncodedResource(new FileSystemResource("/home/kenick/smartFinancial-manager/config/application-"+env+".properties"), "UTF-8"));
            }
            ret = properties.get(key).toString();
            if(ret.contains("ENC(")){
                String encryKey = System.getProperty("jasypt.encryptor.password");
                if(StringUtils.isBlank(encryKey)){
                    encryKey = "kenick@2020";
                }
                if(StringUtils.isNotBlank(encryKey)){
                    ret = ret.substring(4, ret.length()-1);
                    ret = EncryUtil.decryptByJasypt(encryKey, ret);
                }
            }
        } catch (Exception e) {
            logger.error("加载配置失败!", e);
        }
        return ret;
    }
    
    /**
     * <一句话功能简述> 从文件中读取json对象
     * <功能详细描述> 
     * author: zhanggw
     * 创建时间:  2021/7/20
     */
    public static JSONObject getJsonFromFile(String filePath){
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            String contentLine;
            StringBuilder stringBuilder = new StringBuilder();
            while((contentLine = bufferedReader.readLine()) != null){
                stringBuilder.append(contentLine);
            }
            bufferedReader.close();
            return JSON.parseObject(stringBuilder.toString());
        }catch (Exception e){
            logger.error("读取文件异常!", e);
        }
        return null;
    }

    public static List<Fund> getFundFromFile(String filePath){
        try{
            JSONObject ret = getJsonFromFile(filePath);
            if(ret == null){
                return null;
            }

            JSONArray fundArray = ret.getJSONArray("fundList");
            if(fundArray == null){
                return null;
            }

            List<Fund> fundList = new ArrayList<>();
            for(int i=0; i < fundArray.size(); i++){
                JSONObject jsonObject = fundArray.getJSONObject(i);
                fundList.add(JsonUtils.copyObjToBean(jsonObject, Fund.class));
            }

            return fundList;
        }catch (Exception e){
            logger.error("读取文件异常!", e);
        }
        return null;
    }
    
    /**
     * <一句话功能简述> 将json对象写入到文件中
     * <功能详细描述> 
     * author: zhanggw
     * 创建时间:  2021/7/20
     */
    public static void writeJson2File(String filePath,JSONObject jsonObject){
        try{
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
            bufferedWriter.write(jsonObject.toJSONString());
            bufferedWriter.flush();
            bufferedWriter.close();
        }catch (Exception e){
            logger.error("读取文件异常!", e);
        }
    }

    public static void writeFund2File(String filePath, List<Fund> fundList){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fundList", fundList);
        writeJson2File(filePath, jsonObject);
    }

    public static void persistentText(File storeFile, List<String> storeList) throws Exception{
        if(!storeFile.exists()){
            storeFile.createNewFile();
        }

        if(storeList == null || storeList.size() == 0){
            return;
        }

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(storeFile,true));
        for(String storeInfo:storeList){
            bufferedWriter.append(storeInfo);
            bufferedWriter.write("\r\n");
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    public static List<String> getTextListFromFile(File storeFile){
        List<String> retList = new ArrayList<>();
        try{
            if(storeFile == null){
                return null;
            }

            String line;

            BufferedReader bufferedReader = new BufferedReader(new FileReader(storeFile));
            while((line=bufferedReader.readLine()) != null){
                retList.add(line);
            }
            bufferedReader.close();
        }catch (Exception e){
            logger.debug("读取文本内容异常!", e);
        }
        return retList;
    }

}
