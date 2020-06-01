package com.kenick.util;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * author: zhanggw
 * 创建时间:  2020/3/25
 */
public class BasicExcelUtil {
    private static Logger logger = LoggerFactory.getLogger(BasicExcelUtil.class);

    public static void main(String[] args) {
        String filePath = "D:\\tmp\\" + System.currentTimeMillis() + ".xlsx";
        final List<String> supplierNameList = new ArrayList<>();
        supplierNameList.add("自动上架执行结果");
        BasicExcelDelegated basicExcelDelegated = new BasicExcelDelegated(){

            @Override
            public void initExcel(SXSSFWorkbook workbook) throws Exception {
                for(String supplierName:supplierNameList){
                    SXSSFSheet sheet = workbook.createSheet(supplierName);
                    BasicExcelUtil.initItemAutoShelfResult(workbook, sheet);
                }
            }

            @Override
            public void writeExcelData(SXSSFWorkbook workbook, SXSSFSheet sxssfSheet) throws Exception {
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setAlignment(HorizontalAlignment.CENTER);// 居中
                cellStyle.setBorderBottom(BorderStyle.THIN); //下边框    
                cellStyle.setBorderLeft(BorderStyle.THIN); //左边框   
                cellStyle.setBorderTop(BorderStyle.THIN); //上边框    
                cellStyle.setBorderRight(BorderStyle.THIN); //右边框
                for(int i=1; i<10; i++){
                    SXSSFRow row = sxssfSheet.createRow(i);
                    createAndSetCell(row, 0, "11", cellStyle);
                    createAndSetCell(row, 1, "22", cellStyle);
                    createAndSetCell(row, 2, "33", cellStyle);
                }
            }
        };
        BasicExcelUtil.exportExcelToLocalPath(filePath, basicExcelDelegated);
    }

    // 初始化商品自动上架结果表格
    public static void initItemAutoShelfResult(SXSSFWorkbook workbook, SXSSFSheet sheet){
        SXSSFRow row0 = sheet.createRow(0);
        CellStyle cellStyle0 = workbook.createCellStyle();
        Font row0Font0 = workbook.createFont();
        row0Font0.setFontName("宋体");
        row0Font0.setFontHeightInPoints((short) 11);
        row0Font0.setBold(true);
        cellStyle0.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        cellStyle0.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
        cellStyle0.setFont(row0Font0);
        cellStyle0.setWrapText(true);
        cellStyle0.setBorderBottom(BorderStyle.THIN); //下边框    
        cellStyle0.setBorderLeft(BorderStyle.THIN); //左边框   
        cellStyle0.setBorderTop(BorderStyle.THIN); //上边框    
        cellStyle0.setBorderRight(BorderStyle.THIN); //右边框

        String[] headNames = {"供应商名称","供应商款号","商品款号","商品名称","一级品类","二级品类","三级品类","适用年龄","风格",
                "单件重量","颜色","S","M","L","XL","2XL","3XL","F","价格模式","限制库存","档位1价格","档位2价格","档位3价格",
                "货期","面料","拿货价","买手名称","季节","版型","热门元素","展示开始时间","展示结束时间","下单开始时间","下单结束时间",
                "单位","最小起订量","显示供应商","详情图间隙","显示库存","档位1数量","档位2数量","档位3数量","上架结果"};
        Double[] columnWidthArray = { 8.00, 9.00, 10.0, 18.00, 5.00, 5.00, 7.00, 5.00, 9.00, 6.00,
                6.00, 4.00, 4.00, 4.00, 4.00, 4.00, 4.00, 4.0, 5.0, 4.00,
                6.00, 6.00, 6.00, 3.00, 6.00, 5.0, 6.00, 4.00, 5.00, 8.00,
                17.00, 17.00, 17.00, 17.00, 3.00, 3.00, 3.00, 3.00, 3.00, 4.00,
                4.00, 12.00, 40.00};
        for(int i=0; i<headNames.length; i++){
            SXSSFCell tmpCell = row0.createCell(i);
            tmpCell.setCellStyle(cellStyle0);
            tmpCell.setCellValue(headNames[i]);
            sheet.setColumnWidth(i, (int)((columnWidthArray[i]+0.71)*256));
        }
        row0.setHeightInPoints(Float.valueOf("40"));
    }

    public static void initPurchaseOrderSheetStyle(SXSSFWorkbook workbook, SXSSFSheet sheet){
        SXSSFRow row0 = sheet.createRow(0);
        SXSSFCell cell000 = row0.createCell(0);
        CellStyle cellStyle0 = workbook.createCellStyle();
        Font row0Font0 = workbook.createFont();
        row0Font0.setFontName("黑体");
        row0Font0.setFontHeightInPoints((short) 20);
        row0Font0.setBold(true);
        cellStyle0.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        cellStyle0.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
        cellStyle0.setFont(row0Font0);
        cell000.setCellStyle(cellStyle0);
        cell000.setCellValue("广州衣布就到味网络科技有限公司");
        CellRangeAddress cra0 = new CellRangeAddress(0, 0, 0, 20);
        sheet.addMergedRegion(cra0);
        row0.setHeightInPoints(Float.valueOf("31.5"));

        // 第二行
        SXSSFRow row1 = sheet.createRow(1);
        SXSSFCell cell1000 = row1.createCell(0);
        CellStyle cellStyle1 = workbook.createCellStyle();
        Font rowFont1 = workbook.createFont();
        rowFont1.setFontName("黑体");
        rowFont1.setFontHeightInPoints((short) 16);
        rowFont1.setBold(true);
        cellStyle1.setAlignment(HorizontalAlignment.CENTER);// 水平居中
        cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
        cellStyle1.setFont(rowFont1);
        cell1000.setCellStyle(cellStyle1);
        cell1000.setCellValue("采购明细单");
        CellRangeAddress cra1 = new CellRangeAddress(1, 1, 0, 20);
        sheet.addMergedRegion(cra1);
        row1.setHeightInPoints(Float.valueOf("20.25"));

        // 第三行
        SXSSFRow row2 = sheet.createRow(2);
        CellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
        Font rowFont2 = workbook.createFont();
        rowFont2.setFontName("宋体");
        rowFont2.setFontHeightInPoints((short) 12);
        rowFont2.setBold(true);
        cellStyle2.setFont(rowFont2);
        SXSSFCell cell200 = row2.createCell(0);
        cell200.setCellStyle(cellStyle2);
        cell200.setCellValue("批次号:");
        SXSSFCell cell216 = row2.createCell(10);
        cell216.setCellValue("日期:" + DateUtils.getNowDateStr(0,"yyyy年MM月dd日"));
        CellStyle cellStyle22 = workbook.createCellStyle();
        cellStyle22.cloneStyleFrom(cellStyle2);
        cellStyle22.setAlignment(HorizontalAlignment.RIGHT);
        cell216.setCellStyle(cellStyle22);
        CellRangeAddress cra21 = new CellRangeAddress(2, 2, 0, 4);
        CellRangeAddress cra22 = new CellRangeAddress(2, 2, 10, 20);
        sheet.addMergedRegion(cra21);
        sheet.addMergedRegion(cra22);
        row2.setHeightInPoints(Float.valueOf("24.75"));

        // 第四行
        SXSSFRow row3 = sheet.createRow(3);
        CellStyle cellStyle3 = workbook.createCellStyle();
        cellStyle3.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
        cellStyle3.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        Font rowFont3 = workbook.createFont();
        rowFont3.setFontName("宋体");
        rowFont3.setFontHeightInPoints((short) 11);
        rowFont3.setBold(true);
        cellStyle3.setFont(rowFont3);
        cellStyle3.setWrapText(true);

        String[] headNameArray = {"序号","供应商","供应商款号","最小品类","图片","成份","衣脉合成    内部条码","颜色","尺码","数量","供货价（不含税）","金额","收货人","电话","收货地址","交货期"};
        Double[] columnWidthArray = { 4.63, 7.13, 10.6, 8.93, 14.38, 12.13, 13.38, 7.76, 7.71, 7.15, 8.26, 8.26, 8.26, 15.04, 8.26, 8.26};
        for(int i=0; i<headNameArray.length; i++){
            SXSSFCell tmpCell = row3.createCell(i);
            tmpCell.setCellStyle(cellStyle3);
            tmpCell.setCellValue(headNameArray[i]);
            if(i == 10){
                XSSFRichTextString ts = new XSSFRichTextString(tmpCell.getStringCellValue());
                Font tailFont = workbook.createFont();
                tailFont.setFontName("宋体");
                tailFont.setFontHeightInPoints((short) 10);
                tailFont.setBold(false);
                ts.applyFont(0,tmpCell.getStringCellValue().length(),tailFont);
                tmpCell.setCellValue(ts);
            }else{
                CellRangeAddress tmpCRA = new CellRangeAddress(3, 4, i, i);
                sheet.addMergedRegion(tmpCRA);
                sheet.setColumnWidth(i, (int)((columnWidthArray[i]+0.71)*256));
            }
        }
        SXSSFCell cell316 = row3.createCell(16);
        cell316.setCellStyle(cellStyle3);
        cell316.setCellValue("物流信息");
        SXSSFCell cell320 = row3.createCell(20);
        cell320.setCellStyle(cellStyle3);
        cell320.setCellValue("备注");
        row3.setHeightInPoints(Float.valueOf("22.5"));
        CellRangeAddress cra316 = new CellRangeAddress(3, 4, 16, 19);
        sheet.addMergedRegion(cra316);

        // 第5行
        SXSSFRow row4 = sheet.createRow(4);
        CellStyle cellStyle4 = workbook.createCellStyle();
        cellStyle4.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
        cellStyle4.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        Font rowFont4 = workbook.createFont();
        rowFont4.setFontName("宋体");
        rowFont4.setFontHeightInPoints((short) 9);
        cellStyle4.setFont(rowFont4);
        SXSSFCell cell414 = row4.createCell(14);
        cell414.setCellStyle(cellStyle3);
        cell414.setCellValue("支付方");
        row3.setHeightInPoints(Float.valueOf("17.25"));
    }

    public static void exportExcelToLocalPath(String exportPath, BasicExcelDelegated purchaseOrderExcelDelegated){
        try{
            logger.info("开始生成excel,excel路径:{}", exportPath);

            // 在内存当中保持 100 行, 超过的数据放到硬盘中
            SXSSFWorkbook wb = new SXSSFWorkbook(100);

            // 初始化EXCEL
            purchaseOrderExcelDelegated.initExcel(wb);

            int sheetSum = wb.getNumberOfSheets();
            for(int i=0; i<sheetSum; i++){
                SXSSFSheet sxssfSheet = wb.getSheetAt(i);
                purchaseOrderExcelDelegated.writeExcelData(wb, sxssfSheet);
            }
            
            // 下载EXCEL
            downLoadExcelToLocalPath(wb, exportPath);
            logger.info("excel生成完成，路径:{}", exportPath);
        }catch (Exception e){
            logger.debug(e.getMessage());

        }
    }

    /**
     * <一句话功能简述> 下载excel到本地指定文件夹
     * <功能详细描述>
     * author: zhanggw
     * 创建时间:  2019/06/18 14:39
     * @param wb excel对象SXSSFWorkbook
     * @param exportPath 导出路径
     * @return void
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    private static void downLoadExcelToLocalPath(SXSSFWorkbook wb, String exportPath) {
        FileOutputStream fops = null;
        BufferedOutputStream bos = null;
        try {
            fops = new FileOutputStream(exportPath);
            bos = new BufferedOutputStream(fops);
            wb.write(bos);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (null != wb) {
                try {
                    wb.dispose();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
            if (null != bos) {
                try {
                    bos.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
            if (null != fops) {
                try {
                    fops.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    private static void createAndSetCell(Row row, Integer columnIndex, Object value, CellStyle cellStyle){
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value.toString());
        cell.setCellStyle(cellStyle);
    }
}
