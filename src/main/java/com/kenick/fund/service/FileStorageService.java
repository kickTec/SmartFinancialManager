package com.kenick.fund.service;

import com.kenick.generate.bean.Fund;

import java.util.List;

/**
 * author: zhanggw
 * 创建时间:  2021/7/20
 */
public interface FileStorageService {

    String getStorageType();

    List<Fund> getFundListFromFile();

    void writeFundList2File(List<Fund> fundList);

}
