package com.kenick.fund.service.impl;

import com.kenick.fund.bean.Fund;
import com.kenick.fund.service.IFileStorageSV;
import com.kenick.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * author: zhanggw
 * 创建时间:  2021/7/20
 */
@Service("fileStorageService")
public class FileStorageSVImpl implements IFileStorageSV {

    public final static String history = "history";

    @Value("${storage.home.path}")
    String storageHomePath;

    @Value("${storage.enable}")
    boolean storageEnable;

    @Override
    public List<Fund> getFundListFromFile() {
        String storageHomePath = getStorageHomePath();
        return FileUtil.getFundFromFile(storageHomePath + File.separator+"fund.json");
    }

    @Override
    public void writeFundList2File(List<Fund> fundList) {
        String storageHomePath = getStorageHomePath();
        FileUtil.writeFund2File(storageHomePath + File.separator+"fund.json", fundList);
    }

    @Override
    public String getStorageHomePath() {
        if(StringUtils.isBlank(storageHomePath)){
            storageHomePath = "/home/kenick/smartFinancial-manager/storage";
        }
        return storageHomePath;
    }

    @Override
    public String getFullPathByName(String fundCode, String fileName) {
        String storageHomePath = getStorageHomePath();
        return storageHomePath + File.separator + history + File.separator + fundCode + File.separator + fileName;
    }

    @Override
    public File getHistoryFileByName(String fundCode, String fundRecordFile) {
        String fullPath = getFullPathByName(fundCode, fundRecordFile);
        File file = new File(fullPath);
        if(!file.exists()){
            return null;
        }

        return file;
    }

    @Override
    public boolean getStorageEnable() {
        return storageEnable;
    }

}
