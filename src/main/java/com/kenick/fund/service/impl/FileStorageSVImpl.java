package com.kenick.fund.service.impl;

import com.kenick.fund.bean.Fund;
import com.kenick.fund.service.FileStorageService;
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
public class FileStorageSVImpl implements FileStorageService {

    @Value("${storage.home.path}")
    String storageHomePath;

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

}
