package com.kenick.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.DownloadFileRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * author: zhanggw
 * 创建时间:  2021/5/22
 */
public class OssUtil {

    private static final Logger logger = LoggerFactory.getLogger(OssUtil.class);
    private static String ENDPOINT = "http://oss-cn-guangzhou.aliyuncs.com"; // 端点
    private static String BUCKETNAME = "kenick-standard"; // 生产环境bucketName
    private static String access_all_key_id; // 生产环境oss访问id
    private static String access_all_key_secret; // 生产环境oss访问密码
    private static OSSClient ossClient = null;

    static {
        if(StringUtils.isBlank(access_all_key_id)){
            access_all_key_id = FileUtil.getPropertyByEnv("ali.oss.accessKeyId");
        }
        if(StringUtils.isBlank(access_all_key_secret)){
            access_all_key_secret = FileUtil.getPropertyByEnv("ali.oss.accessKeySecret");
        }
    }
    
    /**
     * <一句话功能简述> oss文件通用上传
     * <功能详细描述> 云端文件名与本地文件名相同，云端相同文件名会覆盖
     * author: zhanggw
     * 创建时间:  2021/5/22
     * @param localFile 本地文件绝对路径
     * @param ossPath oss文件路径
     */
    public String upload(String localFile, String ossPath){
        File file = new File(localFile);
        String ossFilePath="";
        try {
            if(ossClient == null){
                ossClient = getOssClient();
            }

            // oss文件名
            String suffix = localFile.substring(localFile.lastIndexOf("."));
            String key = ossPath + "/" + System.currentTimeMillis() + suffix;
            logger.debug("key:{}", key);
            ossClient.putObject(BUCKETNAME, key, file);

            Thread.sleep(200);

            boolean ret = ossClient.doesObjectExist(BUCKETNAME, key);
            if(!ret){
                return upload(localFile, ossPath);
            }
            ossFilePath = key;
        } catch (Exception e) {
            logger.error("{}上传发生异常，异常信息:{}", file.getAbsoluteFile(), e.getMessage());
        }
        return ossFilePath;
    }
    
    /**
     * <一句话功能简述> oss文件下载
     * author: zhanggw
     * 创建时间:  2021/5/22
     * @param ossFile oss文件绝对路径
     * @param localFile 本地文件绝对路径
     */
    public String download(String ossFile, String localFile){
        String downloadFilePath = null;
        try{
            OSSClient ossClient = getOssClient();
            DownloadFileRequest downloadFileRequest = new DownloadFileRequest(BUCKETNAME,ossFile,localFile,1000);
            ossClient.downloadFile(downloadFileRequest);
            downloadFilePath = downloadFileRequest.getDownloadFile();
            logger.debug("downloadFilePath:{}", downloadFilePath);
        }catch (Exception e){
            logger.error("oss下载异常",e);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return downloadFilePath;
    }

    private static OSSClient getOssClient(){
        if(ossClient != null){
            return ossClient;
        }

        return getNewOssClient();
    }

    private static OSSClient getNewOssClient(){
        logger.debug("ENDPOINT:{},access_all_key_id:{},access_all_key_secret:{}", ENDPOINT, access_all_key_id, access_all_key_secret);
        return new OSSClient(ENDPOINT, access_all_key_id, access_all_key_secret);
    }

    public static void main(String[] args) {
        String localFile = "d:/tmp/test05.jpg";
        String ossPath = "home/kenick/tmp";
        String ossFile = "home/kenick/tmp/1621678289790.jpg";

        // 下载文件
        OssUtil ossUtil = new OssUtil();
        ossUtil.download(ossFile, localFile);

        // 上传文件
        // String ret = ossUtil.upload(localFile, ossPath);
        // logger.debug("文件上传结果:{}", ret);
    }

}
