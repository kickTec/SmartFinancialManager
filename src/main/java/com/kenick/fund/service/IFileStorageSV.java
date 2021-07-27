package com.kenick.fund.service;


import com.kenick.fund.bean.Fund;

import java.io.File;
import java.util.List;

/**
 * author: zhanggw
 * 创建时间:  2021/7/20
 */
public interface IFileStorageSV {

    List<Fund> getFundListFromFile();

    void writeFundList2File(List<Fund> fundList);

    String getStorageHomePath();

    String getFullPathByName(String fundCode, String fileName);

    File getHistoryFileByName(String fundCode, String fundRecordFile);

    boolean getStorageEnable();

}
