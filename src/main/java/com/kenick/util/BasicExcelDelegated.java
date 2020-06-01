package com.kenick.util;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * author: zhanggw
 * 创建时间:  2020/3/26
 */
public abstract class BasicExcelDelegated {

    public abstract void initExcel(SXSSFWorkbook workbook) throws Exception;

    public abstract void writeExcelData(SXSSFWorkbook workbook, SXSSFSheet sxssfSheet) throws Exception;

}
