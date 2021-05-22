package com.kenick.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        String test = getPropertyByEnv("env.dev.root.passwd");
        logger.debug(test);
    }

    public static List<List<String>> getDataFromExcel(String filePath, String sheetName) throws IOException {
        return getDataFromExcel(new File(filePath), sheetName);
    }

    public static List<List<String>> getDataFromExcel(File file, String sheetName) throws IOException {
        long startTime = System.currentTimeMillis();
        Workbook book = null;
        ArrayList<List<String>> rtnList = new ArrayList<>();
        InputStream inputStream = null;
        BufferedInputStream bis = null;
        try {
            inputStream = new FileInputStream(file);
            if (StringUtils.endsWith(file.getName(), ".xlsx")) {
                book = new XSSFWorkbook(inputStream);
            } else {
                book = new HSSFWorkbook(inputStream);
            }

            Sheet sheet = book.getSheet(sheetName);

            try {
                for (int rowIndex = sheet.getFirstRowNum(); rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);

                    if (null == row) {
                        continue;
                    }

                    List<String> dataList = new ArrayList<>();

                    try {
                        for (int cellIndex = row.getFirstCellNum(); cellIndex <= row.getLastCellNum(); cellIndex++) {
                            Cell cell = row.getCell(cellIndex);
                            if (cell == null) {
                                continue;
                            }
                            cell.setCellType(CellType.STRING);
                            dataList.add(cellIndex, cell.getStringCellValue());
                        }
                    } catch (Exception e) {
                        logger.debug("sheetName:{},rowIndex:{},errorMsg:{}", sheetName, rowIndex, e.getMessage());
                    }

                    rtnList.add(dataList);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e.getCause());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (book != null) {
                book.close();
            }
            logger.debug("读取excel文件花费时间:{}", System.currentTimeMillis() - startTime);
        }
        return rtnList;
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

}
