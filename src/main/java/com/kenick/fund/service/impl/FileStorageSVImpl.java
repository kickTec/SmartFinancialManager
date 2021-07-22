package com.kenick.fund.service.impl;

import com.kenick.fund.service.FileStorageService;
import com.kenick.generate.bean.Fund;
import com.kenick.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author: zhanggw
 * 创建时间:  2021/7/20
 */
@Service("fileStorageService")
public class FileStorageSVImpl implements FileStorageService {

    @Value("${storage.type}")
    String storageType;

    @Value("${storage.file.fund}")
    String storageFileFund;

    @Value("${storage.file.history.path}")
    String storageFileHistoryPath;

    @Value("${storage.file.history.enable}")
    Boolean storageFileHistoryEnable;

    @Override
    public String getStorageType() {
        if(StringUtils.isBlank(storageType)){
            storageType = "file";
        }

        return storageType;
    }

    @Override
    public List<Fund> getFundListFromFile() {

        if(StringUtils.isBlank(storageFileFund)){
            storageFileFund = "/home/kenick/smartFinancial-manager/config/fund.json";
        }

        return FileUtil.getFundFromFile(storageFileFund);
    }

    @Override
    public void writeFundList2File(List<Fund> fundList) {
        if(StringUtils.isBlank(storageFileFund)){
            storageFileFund = "/home/kenick/smartFinancial-manager/config/fund.json";
        }

        FileUtil.writeFund2File(storageFileFund, fundList);
    }

    @Override
    public String getHistoryPath() {
        if(StringUtils.isBlank(storageFileHistoryPath)){
            storageFileHistoryPath = "/home/kenick/smartFinancial-manager/storage/history";
        }

        return storageFileHistoryPath;
    }

    @Override
    public boolean getStorageFileHistoryEnable() {
        if(storageFileHistoryEnable == null){
            storageFileHistoryEnable = true;
        }

        return storageFileHistoryEnable;
    }

}
