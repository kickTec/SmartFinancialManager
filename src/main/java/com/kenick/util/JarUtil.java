package com.kenick.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        String historyDir = "E:\\storage\\smf_storage\\history";
        compressFundHistory(historyDir, 2021);
    }

    /**
     * <一句话功能简述> 压缩文件夹内容
     * <功能详细描述>
     * author: zhanggw
     * 创建时间:  2021/12/30
     *
     * @param targetDir        压缩文件夹目录全路径
     * @param compressFilePath zip压缩包存储路径，null表示保存在父级目录下，文件名为targetDir的最后名称+".zip"
     */
    public static void compress(String targetDir, String compressFilePath) {
        try {
            File sourceFile = new File(targetDir);
            if (StringUtils.isBlank(compressFilePath)) {
                compressFilePath = sourceFile.getParentFile().getAbsolutePath() + File.separator + sourceFile.getName() + ".zip";
            }
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(compressFilePath));
            compress(sourceFile, sourceFile.getName(), zipOutputStream);
            zipOutputStream.close();
        } catch (Exception e) {
            logger.error("压缩文件异常!", e);
        }
    }

    /**
     * 压缩基金年份数据
     *
     * @param targetDir 基金历史目录
     * @param year      年份
     */
    public static void compressFundHistory(String targetDir, int year) {
        try {
            // 压缩文件名称
            File sourceFile = new File(targetDir);
            String zipName = sourceFile.getName() + "_" + year + ".zip";

            // 新保存压缩文件路径
            String backupPath = sourceFile.getParentFile().getAbsolutePath() + File.separator + "historyBackup";
            File backupDir = new File(backupPath);
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }
            backupPath = backupPath + File.separator + zipName;
            logger.debug("开始备份history,路径:{}", backupPath);

            // 压缩输出
            ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(Paths.get(backupPath)));
            compressFundInner(sourceFile, sourceFile.getName(), zipOutputStream, year);
            zipOutputStream.close();
        } catch (Exception e) {
            logger.error("压缩文件异常!", e);
        }
    }

    /**
     * 压缩基金文件 内部使用
     * 由于递归调用,无法在方法内关闭ZipOutputStream,调用后再close
     *
     * @param historyPath  历史目录 E:\storage\smf_storage\history
     * @param zipFilePath  压缩zip路径 E:\storage\smf_storage\historyBackup\test.zip
     * @param targetZipOut zip输出流 new ZipOutputStream(Files.newOutputStream(Paths.get(zipFilePath)))
     * @param year         年份
     */
    private static void compressFundInner(File historyPath, String zipFilePath, ZipOutputStream targetZipOut, int year) {
        if (!historyPath.exists()) {
            logger.debug("待压缩的目录" + historyPath.getName() + "不存在");
            return;
        }

        File[] files = historyPath.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            for (File file : files) {
                String fileName = file.getName();
                if (file.isFile()) {//每天的明细
                    // 添加年份数据
                    if (!fileName.contains(year + "-")) {
                        continue;
                    }

                    logger.debug("开始添加压缩文件内容:{}", file.getAbsoluteFile());
                    ZipEntry zipEntry = new ZipEntry(zipFilePath + File.separator + fileName);
                    targetZipOut.putNextEntry(zipEntry);
                    //读取待压缩的文件并写进压缩包里
                    int size = 1024 * 8;
                    byte[] byteArray = new byte[size];
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    int read;
                    while ((read = bis.read(byteArray, 0, size)) != -1) {
                        targetZipOut.write(byteArray, 0, read);
                    }
                    //如果需要删除源文件，则需要执行下面2句
                    fis.close();
                } else if (file.isDirectory()) {
                    logger.trace("进入目录:{}", file.getAbsoluteFile());
                    compressFundInner(file, zipFilePath + File.separator + fileName, targetZipOut, year);
                }
            }
        } catch (IOException e) {
            logger.error("打包异常!", e);
        } finally {
            //关闭流
            try {
                if (null != bis) {
                    bis.close();
                }
                if (targetZipOut != null) {
                    targetZipOut.flush();
                }
            } catch (IOException e) {
                logger.error("打包关闭流异常!", e);
            }
        }
    }

    /**
     * <一句话功能简述> 递归压缩zip
     * <功能详细描述>
     * author: zhanggw
     * 创建时间:  2021/12/30
     *
     * @param sourceDir    压缩目标目录
     * @param zipDirName   zip压缩包中当前目录名称  history/fund
     * @param targetZipOut zip压缩包输出流
     */
    private static void compress(File sourceDir, String zipDirName, ZipOutputStream targetZipOut) {
        if (!sourceDir.exists()) {
            logger.debug("待压缩的目录" + sourceDir.getName() + "不存在");
            return;
        }

        File[] files = sourceDir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        byte[] byteArray = new byte[1024 * 10];

        try {
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
        } catch (IOException e) {
            logger.error("打包异常!", e);
        } finally {
            //关闭流
            try {
                if (null != bis) bis.close();
                if (null != fis) fis.close();
            } catch (IOException e) {
                logger.error("打包关闭流异常!", e);
            }
        }
    }

    /**
     * <一句话功能简述> 删除jar包中内容
     * <功能详细描述>
     * author: zhanggw
     * 创建时间:  2021/12/30
     *
     * @param jarPath   jar包路径
     * @param deletes   jar包需要删除的内容
     * @param deleteBak 是否需要删除备份
     */
    public static void delete(String jarPath, List<String> deletes, Boolean deleteBak) {
        JarOutputStream jos = null;
        InputStream inputStream = null;
        JarFile bakJarFile = null;
        File bakFile = null;
        try {
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
                } else {
                    logger.debug("Delete内容" + entry.getName());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (jos != null) {
                try {
                    jos.flush();
                    jos.finish();
                    jos.close();
                } catch (Exception e) {
                    logger.debug(e.getMessage());
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    logger.debug(e.getMessage());
                }
            }

            if (bakJarFile != null) {
                try {
                    bakJarFile.close();
                } catch (Exception e) {
                    logger.debug(e.getMessage());
                }
            }

            if (deleteBak && bakFile != null) {
                boolean delFlag = bakFile.delete();
                if (!delFlag) {
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
