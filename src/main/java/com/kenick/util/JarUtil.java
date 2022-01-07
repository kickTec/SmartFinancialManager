package com.kenick.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * author: zhanggw
 * 创建时间:  2020/4/15
 */
public class JarUtil {
    private static Logger logger = LoggerFactory.getLogger(JarUtil.class);

    public static void main(String[] args) throws Exception {
        // compressFundStorage("D:\\tmp\\smf_storage\\history", 10);

        Date pastDateZero = DateUtils.getPastDateZero(1);
        System.out.println(pastDateZero);
    }

    /**
     * <一句话功能简述> 压缩文件夹内容
     * <功能详细描述> 
     * author: zhanggw
     * 创建时间:  2021/12/30
     * @param targetDir 压缩文件夹目录全路径
     * @param compressFilePath zip压缩包存储路径，null表示保存在父级目录下，文件名为targetDir的最后名称+".zip"
     */
    public static void compress(String targetDir, String compressFilePath){
        try{
            File sourceFile = new File(targetDir);
            if(StringUtils.isBlank(compressFilePath)){
                compressFilePath = sourceFile.getParentFile().getAbsolutePath() + File.separator + sourceFile.getName() + ".zip";
            }
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(compressFilePath));
            compress(sourceFile, sourceFile.getName(), zipOutputStream);
            zipOutputStream.close();
        }catch (Exception e){
            logger.error("压缩文件异常!", e);
        }
    }

    public static void compressFundStorage(String targetDir, int timeoutDay, String zipName){
        try{
            File sourceFile = new File(targetDir);
            if(StringUtils.isBlank(zipName)){
                zipName = sourceFile.getName() + "_" + DateUtils.getNowDateStr("yyyy-MM-dd") + ".zip";
            }

            String backupPath = sourceFile.getParentFile().getAbsolutePath() + File.separator + zipName;
            File backupZipFile = new File(backupPath);
            if(backupZipFile.exists()){
                return;
            }
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(backupPath));
            compressFund(sourceFile, sourceFile.getName(), zipOutputStream, timeoutDay);
            zipOutputStream.close();
        }catch (Exception e){
            logger.error("压缩文件异常!", e);
        }
    }

    private static void compressFund(File sourceDir, String zipDirName, ZipOutputStream targetZipOut, int dayNum) {
        if(!sourceDir.exists()){
            logger.debug("待压缩的目录"+sourceDir.getName()+"不存在");
            return;
        }

        File[] files = sourceDir.listFiles();
        if(files == null || files.length ==0){
            return;
        }

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        byte[] byteArray = new byte[1024*10];

        try{
            for (File file : files) {
                String fileName = file.getName();
                if (file.isFile()) {

                    // 30天以前的文件不压缩
                    String fundTime = fileName.replace(".txt", "").split("_")[1];
                    if(DateUtils.tranToDate(fundTime, "yyyy-MM-dd").before(DateUtils.getPastDateZero(dayNum-1))){
                        continue;
                    }

                    logger.debug("开始压缩:{}", file.getAbsoluteFile());
                    ZipEntry zipEntry = new ZipEntry(zipDirName + File.separator + fileName);
                    targetZipOut.putNextEntry(zipEntry);
                    //读取待压缩的文件并写进压缩包里
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis, 1024 * 10);
                    int read;
                    while ((read = bis.read(byteArray, 0, 1024 * 10)) != -1) {
                        targetZipOut.write(byteArray, 0, read);
                    }
                    //如果需要删除源文件，则需要执行下面2句
                    //fis.close();
                    //fs[i].delete();
                } else if (file.isDirectory()) {
                    logger.debug("进入目录:{}", file.getAbsoluteFile());
                    compressFund(file, zipDirName + File.separator + fileName, targetZipOut, dayNum);
                }
            }//end for
        }catch  (IOException e) {
            logger.error("打包异常!",e);
        } finally{
            //关闭流
            try {
                if(null!=bis) bis.close();
                if(null!=fis) fis.close();
            } catch (IOException e) {
                logger.error("打包关闭流异常!",e);
            }
        }
    }

    /**
     * <一句话功能简述> 递归压缩zip
     * <功能详细描述> 
     * author: zhanggw
     * 创建时间:  2021/12/30
     * @param sourceDir 压缩目标目录
     * @param zipDirName zip压缩包中当前目录名称  history/fund
     * @param targetZipOut zip压缩包输出流
     */
    private static void compress(File sourceDir, String zipDirName, ZipOutputStream targetZipOut){
        if(!sourceDir.exists()){
            logger.debug("待压缩的目录"+sourceDir.getName()+"不存在");
            return;
        }

        File[] files = sourceDir.listFiles();
        if(files == null || files.length ==0){
            return;
        }

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        byte[] byteArray = new byte[1024*10];

        try{
            for (File file : files) {
                if (file.isFile()) {
                    logger.debug("开始压缩:{}", file.getAbsoluteFile());
                    ZipEntry zipEntry = new ZipEntry(zipDirName + File.separator + file.getName());
                    targetZipOut.putNextEntry(zipEntry);
                    //读取待压缩的文件并写进压缩包里
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis, 1024 * 10);
                    int read;
                    while ((read = bis.read(byteArray, 0, 1024 * 10)) != -1) {
                        targetZipOut.write(byteArray, 0, read);
                    }
                    //如果需要删除源文件，则需要执行下面2句
                    //fis.close();
                    //fs[i].delete();
                } else if (file.isDirectory()) {
                    logger.debug("进入目录:{}", file.getAbsoluteFile());
                    compress(file, zipDirName + File.separator + file.getName(), targetZipOut);
                }
            }//end for
        }catch  (IOException e) {
            logger.error("打包异常!",e);
        } finally{
            //关闭流
            try {
                if(null!=bis) bis.close();
                if(null!=fis) fis.close();
            } catch (IOException e) {
                logger.error("打包关闭流异常!",e);
            }
        }
    }

    /**
     * <一句话功能简述> 删除jar包中内容
     * <功能详细描述> 
     * author: zhanggw
     * 创建时间:  2021/12/30
     * @param jarPath jar包路径
     * @param deletes jar包需要删除的内容
     * @param deleteBak 是否需要删除备份
     */
    public static void delete(String jarPath, List<String> deletes, Boolean deleteBak) {
        JarOutputStream jos = null;
        InputStream inputStream = null;
        JarFile bakJarFile = null;
        File bakFile = null;
        try{
            // 先备份
            File oriFile = new File(jarPath);
            if (!oriFile.exists()) {
                logger.error("{}未找到!", jarPath);
                return;
            }
            // 将文件名命名成备份文件
            String bakJarName = jarPath.substring(0, jarPath.length() - 3) + DateUtils.getNowDateStr("yyyyMMddHHmmssSSS") + ".jar";
            bakFile = new File(bakJarName);
            boolean isOK = oriFile.renameTo(bakFile);
            if (!isOK) {
                logger.error("文件重命名失败!");
                return;
            }

            // 创建新jar包（删除指定内容）
            bakJarFile = new JarFile(bakJarName);
            jos = new JarOutputStream(new FileOutputStream(jarPath));
            Enumeration<JarEntry> entries = bakJarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!deletes.contains(entry.getName())) {
                    inputStream = bakJarFile.getInputStream(entry);
                    jos.putNextEntry(entry);
                    byte[] bytes = readStream(inputStream);
                    jos.write(bytes, 0, bytes.length);
                }
                else {
                    logger.debug("Delete内容" + entry.getName());
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            if(jos != null){
                try{
                    jos.flush();
                    jos.finish();
                    jos.close();
                }catch (Exception e){
                    logger.debug(e.getMessage());
                }
            }

            if(inputStream != null){
                try{
                    inputStream.close();
                }catch (Exception e){
                    logger.debug(e.getMessage());
                }
            }

            if(bakJarFile != null){
                try{
                    bakJarFile.close();
                }catch (Exception e){
                    logger.debug(e.getMessage());
                }
            }

            if(deleteBak && bakFile != null){
                boolean delFlag = bakFile.delete();
                if(!delFlag){
                    logger.debug("删除jar包中内容清理备份失败!");
                }
            }
        }
    }

    private static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

}
