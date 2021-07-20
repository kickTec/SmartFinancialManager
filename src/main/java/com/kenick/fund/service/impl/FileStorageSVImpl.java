package com.kenick.fund.service.impl;

import com.kenick.fund.service.FileStorageService;
import com.kenick.generate.bean.Fund;
import com.kenick.util.FileUtil;
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

    @Override
    public String getStorageType() {
        return storageType;
    }

    @Override
    public List<Fund> getFundListFromFile() {
        return FileUtil.getFundFromFile(storageFileFund);
    }

    @Override
    public void writeFundList2File(List<Fund> fundList) {
        FileUtil.writeFund2File(storageFileFund, fundList);
    }

}
