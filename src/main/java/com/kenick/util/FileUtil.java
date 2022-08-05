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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author: zhanggw
 * 创建时间:  2020/3/10
 */
public class FileUtil {
    public static final JSONObject JSON_OBJECT = new JSONObject();
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    public static void main(String[] args) throws Exception{
        // generateDayHistory("E:\\tmp\\600036");
        String name = "600036_2022-07-20.txt";
        name = name.split("_")[1];
        name = name.split("\\.")[0];
        System.out.println(name);
    }

    public static void fixFundRepeat(String path) throws Exception{
        File dir = new File(path);
        File szDir = new File(path + File.separator + "sz");
        File shDir = new File(path + File.separator + "sh");
        if(!szDir.exists()){
            szDir.mkdirs();
        }
        if(!shDir.exists()){
            shDir.mkdirs();
        }

        File[] files = dir.listFiles((dir1, name) -> name.contains("000001"));
        for(File tmp:files){
            List<String> szList = new ArrayList<>();
            List<String> shList = new ArrayList<>();
            List<String> textList = getTextListFromFile(tmp);
            for(String text:textList){
                BigDecimal data = new BigDecimal(text.split(",")[1]);
                if(data.compareTo(new BigDecimal(1000)) > 0){
                    shList.add(text);
                }else{
                    szList.add(text);
                }
            }

            String szName = szDir.getAbsolutePath() + File.separator + tmp.getName();
            String shName = shDir.getAbsolutePath() + File.separator + tmp.getName();
            System.out.println(szName);
            if(szList.size() > 0){
                persistentText(new File(szName), szList);
            }
            if(shList.size() > 0){
                persistentText(new File(shName), shList);
            }
        }
    }

    public static void generateDayHistory(String fundPath) throws Exception{
        File dir = new File(fundPath);
        File[] files = dir.listFiles((dir1, name) -> !name.contains("day.txt"));
        if(files == null){
            return;
        }

        List<File> fileList = Arrays.asList(files);
        fileList.sort((o1, o2) -> {
            if (o1.isDirectory() && o2.isFile())
                return -1;
            if (o1.isFile() && o2.isDirectory())
                return 1;
            return o1.getName().compareTo(o2.getName());
        });

        // 保存文件路径
        File dayFile = new File(fundPath + File.separator + "day.txt"); // 保存文件
        if(dayFile.exists()){
            dayFile.delete();
        }
        for(File tmp:fileList){
            try{
                JSONObject tmpJson = getNumFromFund(tmp);
                List<BigDecimal> numList = (List<BigDecimal>)tmpJson.get("numList");
                BigDecimal lastData = tmpJson.getBigDecimal("lastData");
                String fileName = tmp.getName();
                JSONObject retJson = getLowHigh(numList, 10);
                BigDecimal lowAvg = retJson.getBigDecimal("lowAvg" + 10);
                BigDecimal highAvg = retJson.getBigDecimal("highAvg" + 10);

                String content = fileName+","+lastData.toString()+","+lowAvg.setScale(2,RoundingMode.HALF_UP).toString()
                        +","+highAvg.setScale(2,RoundingMode.HALF_UP).toString();
                FileUtil.persistentTextSingle(dayFile, content);
            }catch (Exception e){
                logger.error(tmp.getName()+"异常!", e);
            }
        }
    }

    public static JSONObject getNumFromFund(File file){
        JSONObject retJson = new JSONObject();
        List<BigDecimal> numList = new ArrayList<>();
        List<String> dayList = getTextListFromFile(file);
        if(dayList == null){
            return retJson;
        }
        for(String tmp:dayList){
            numList.add(new BigDecimal(tmp.split(",")[1]));
        }
        BigDecimal lastData = numList.get(numList.size() - 1);
        retJson.put("lastData", lastData);
        numList.sort(BigDecimal::compareTo);
        retJson.put("numList", numList);
        return retJson;
    }

    /**
     * <一句话功能简述> 获取平均值
     * <功能详细描述> 
     * author: zhanggw
     * 创建时间:  2022/8/2
     * @param fundPath 每日数据存储位置
     * @param lastNum 最近几天
     * @return com.alibaba.fastjson.JSONObject avg lowAvg10 highAvg10
     */
    public static JSONObject getAvgByCode(String fundPath, int lastNum){
        JSONObject retJson = new JSONObject();
        List<File> lastFileList = getLastFile(fundPath, lastNum);
        if(lastFileList == null){
            return retJson;
        }

        // 平均数
        BigDecimal avgBd = null;
        List<BigDecimal> allList = new ArrayList<>();
        for(int i=0; i<lastFileList.size(); i++){
            File file = lastFileList.get(i);
            List<String> dayList = getTextListFromFile(file);
            if(dayList == null){
                continue;
            }
            for(String tmp:dayList){
                BigDecimal curNet = new BigDecimal(tmp.split(",")[1]);
                allList.add(curNet);
                if(avgBd == null){
                    avgBd = curNet;
                }else{
                    avgBd = avgBd.add(curNet).divide(new BigDecimal(2),2, RoundingMode.HALF_UP);
                }
            }
        }

        JSONObject lowHigh10 = getLowHigh(allList, 10);
        JSONObject tmp = new JSONObject();
        tmp.put("avg", avgBd);
        tmp.putAll(lowHigh10);

        retJson.put("avg"+lastNum, tmp);
        return retJson;
    }

    public static JSONObject getLowHigh(List<BigDecimal> dataList, int preNum) {
        JSONObject retJson = new JSONObject();
        if(dataList == null || dataList.size() == 0){
            return null;
        }
        dataList.sort(BigDecimal::compareTo);
        int preNumAmount = new BigDecimal(dataList.size() * preNum/100).intValue();
        List<BigDecimal> lowList = dataList.subList(0, preNumAmount);
        List<BigDecimal> highList = dataList.subList(dataList.size()-preNumAmount, dataList.size());
        retJson.put("lowAvg"+preNum, getAvgFromList(lowList));
        retJson.put("highAvg"+preNum, getAvgFromList(highList));
        return retJson;
    }

    public static BigDecimal getAvgFromList(List<BigDecimal> lowList){
        BigDecimal avgBd = null;
        for(BigDecimal tmp:lowList){
            if(avgBd == null){
                avgBd = tmp;
            }else{
                avgBd = avgBd.add(tmp).divide(new BigDecimal(2),2, RoundingMode.HALF_UP);
            }
        }
        return avgBd;
    }

    public static List<File> getLastFile(String path, int lastNum){
        if(StringUtils.isBlank(path)){
            return null;
        }

        File dir = new File(path);
        if(!dir.exists()){
            return null;
        }

        File[] files = dir.listFiles();
        if(files == null){
            return null;
        }

        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, (o1, o2) -> {
            if (o1.isDirectory() && o2.isFile())
                return -1;
            if (o1.isFile() && o2.isDirectory())
                return 1;
            return o2.getName().compareTo(o1.getName());
        });
        return fileList.subList(0, lastNum);
    }

    public static void downloadFileConcurrent(final String url,final String localPatch) {
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                download(url, localPatch);
            }
        });
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

    public static String getPropertyByPath(String location, String key) {
        String property = null;
        try {
            FileSystemResource fileSystemResource = new FileSystemResource(location);
            if(!fileSystemResource.exists()){
                return null;
            }

            Properties properties = PropertiesLoaderUtils.loadProperties(new EncodedResource(fileSystemResource, "UTF-8"));
            property = properties.getProperty(key);
            if(StringUtils.isNotBlank(property) && property.contains("ENC(")){
                String encryKey = System.getProperty("jasypt.encryptor.password");
                if(StringUtils.isNotBlank(encryKey)){
                    property = property.substring(4, property.length()-1);
                    property = EncryUtil.decryptByJasypt(encryKey, property);
                }
            }
        } catch (Exception e) {
            logger.error("加载配置失败!", e);
        }
        return property;
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

    public static String getLastTextFromFile(File storeFile){
        String lastLine = null;
        try{
            if(storeFile == null || !storeFile.exists()){
                return null;
            }

            String line;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(storeFile));
            while((line=bufferedReader.readLine()) != null){
                lastLine = line; // 最后line为null
            }
            bufferedReader.close();
        }catch (Exception e){
            logger.debug("读取文本内容异常!", e);
        }
        return lastLine;
    }

    /**
     *  删除文件夹
     * @param dir 文件夹路径
     * @param forceFlag 是否强制删除
     */
    public static boolean deleteDir(String dir, boolean forceFlag) {
        try{
            logger.debug("开始删除文件夹:{}", dir);
            File dirFile = new File(dir);
            if(!dirFile.exists()){
                return true;
            }

            File[] files = dirFile.listFiles();

            // 空目录直接删除
            if(files == null || files.length == 0){
                dirFile.delete();
                return true;
            }

            // 非强制，有内容直接返回false
            if(!forceFlag){
                return false;
            }

            // 强制删除
            for(File subFile:files){
                if(subFile.isFile()){
                    subFile.delete();
                }else{
                    deleteDir(subFile.getAbsolutePath(), true);
                }
            }
            return true;
        }catch (Exception e){
            logger.error("强制删除文件夹异常!", e);
        }
        return false;
    }

    public static void printJVMInfo(Logger logger) throws Exception{
        logger.debug("=======================================================================");
        int freeMemMin = 90; // 最低内存要求
        Runtime runtime = Runtime.getRuntime();
        long memoryMax = runtime.maxMemory()/1024/1024;
        long memoryJVM = runtime.totalMemory()/1024/1024; // jvm占用内存
        long memoryFree = runtime.freeMemory()/1024/1024; // jvm空闲内存
        long useMem = memoryJVM - memoryFree; // jvm使用内存
        logger.debug("本地可用内存:{} MB,JVM总内存:{} MB,使用内存:{} MB,空闲内存:{} MB", memoryMax, memoryJVM, useMem, memoryFree);
        if(memoryFree < freeMemMin && useMem > freeMemMin){ // 空闲内存小于最低内存要求，使用内存大于最低内存
            System.gc();
            Thread.sleep(100);

            memoryJVM = runtime.totalMemory()/1024/1024; // jvm占用内存
            memoryFree = runtime.freeMemory()/1024/1024; // jvm空闲内存
            useMem = memoryJVM - memoryFree; // jvm使用内存
            logger.debug("JVM可用内存不足{}MB，强制垃圾清理后，JVM总内存:{} MB,使用内存:{} MB,空闲内存:{} MB",
                    freeMemMin, memoryJVM, useMem, memoryFree);
        }
    }

    public static void persistentTextSingle(File storeFile, String content) throws Exception {
        if(!storeFile.exists()){
            storeFile.createNewFile();
        }

        if(StringUtils.isBlank(content)){
            return;
        }

        FileWriter fileWriter = new FileWriter(storeFile, true);
        fileWriter.append(content);
        fileWriter.append("\r\n");
        fileWriter.flush();
        fileWriter.close();
    }

    /**
     * <一句话功能简述> 获取近dayNum日 均价,10%低位均价,10%高位均价
     * <功能详细描述> 
     * author: zhanggw
     * 创建时间:  2022/8/4
     * @param dayNum 近几日
     * @param historyList 历史数据 600036_2022-07-20.txt,35.85,35.78,36.09
     */
    public static JSONObject getAvgHighLow(int dayNum, List<String> historyList) {
        JSONObject retJson = new JSONObject();
        try{
            if(historyList == null || historyList.size() == 0){
                return retJson;
            }

            Collections.reverse(historyList);
            BigDecimal avgBd = null;
            BigDecimal avgLow10 = null;
            BigDecimal avgHigh10 = null;
            for(int i=0; i<dayNum && i<historyList.size(); i++){
                String stockData = historyList.get(i);
                String[] stockDataArray = stockData.split(",");
                if(avgBd == null){
                    avgBd = new BigDecimal(stockDataArray[1]);
                }else{
                    avgBd = avgBd.add(new BigDecimal(stockDataArray[1])).divide(new BigDecimal(2));
                }
                if(avgLow10 == null){
                    avgLow10 = new BigDecimal(stockDataArray[2]);
                }else{
                    avgLow10 = avgLow10.add(new BigDecimal(stockDataArray[2])).divide(new BigDecimal(2));
                }
                if(avgHigh10 == null){
                    avgHigh10 = new BigDecimal(stockDataArray[3]);
                }else{
                    avgHigh10 = avgHigh10.add(new BigDecimal(stockDataArray[3])).divide(new BigDecimal(2));
                }
            }
            Collections.reverse(historyList);
            logger.debug("getAvgHighLow:{} out,historyList.size:{}", dayNum, historyList.size());
            retJson.put("avgBd", avgBd.setScale(2, RoundingMode.HALF_UP));
            retJson.put("avgLow10", avgLow10.setScale(2, RoundingMode.HALF_UP));
            retJson.put("avgHigh10", avgHigh10.setScale(2, RoundingMode.HALF_UP));
        }catch (Exception e){
            logger.error("getAvgHighLow异常!", e);
        }
        return retJson;
    }


    public static JSONObject getLastDataByNum(int dayNum, List<String> historyList) {
        JSONObject retJson = new JSONObject();
        try{
            if(historyList == null || historyList.size() == 0){
                return retJson;
            }

            Collections.reverse(historyList);
            List<BigDecimal> stockValueList = new ArrayList<>();
            List<String> dateList = new ArrayList<>();
            for(int i=0; i<dayNum && i<historyList.size(); i++){
                String stockData = historyList.get(i);
                String[] stockDataArray = stockData.split(",");
                String name = stockDataArray[0];
                name = name.split("_")[1];
                name = name.split("\\.")[0];
                stockValueList.add(new BigDecimal(stockDataArray[1]));
                dateList.add(name);
            }
            Collections.reverse(dateList);
            Collections.reverse(stockValueList);
            Collections.reverse(historyList);
            retJson.put("dateList", dateList);
            retJson.put("stockValueList", stockValueList);
            logger.debug("historyList.size:{}", historyList.size());
        }catch (Exception e){
            logger.error("getLastDataByNum异常!", e);
        }
        return retJson;
    }

}
