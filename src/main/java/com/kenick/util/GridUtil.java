package com.kenick.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * author: zhanggw
 * 创建时间:  2022/8/13
 */
public class GridUtil {
    private static BigDecimal defaultFee = new BigDecimal(0.01);

    private final static Logger logger = LoggerFactory.getLogger(GridUtil.class);

    /**
     * <一句话功能简述> 计算股票交易费用
     * <功能详细描述> 
     * author: zhanggw
     * 创建时间:  2022/8/13
     * @param fundCode 股票代码
     * @param fundPrice 股价
     * @param tradeQuantity 交易数量
     * @return java.math.BigDecimal
     */
    public static BigDecimal calcFee(String fundCode, BigDecimal fundPrice, int tradeQuantity) {
        BigDecimal fee = BigDecimal.ZERO;
        try{
            if(StringUtils.isBlank(fundCode) || fundPrice == null || tradeQuantity<=0){
                return fee;
            }

            String stockType = getStockType(fundCode);
            BigDecimal feeRate = new BigDecimal(0.00005); // 默认深债费率 十万分之5
            if(stockType.equals("sh")){
                feeRate = new BigDecimal(0.000005);
            }
            fee = fundPrice.multiply(new BigDecimal(tradeQuantity)).multiply(feeRate);
            if(fee.compareTo(defaultFee)<0){
                fee = defaultFee;
            }
            fee = fee.setScale(2, RoundingMode.HALF_UP);
        }catch (Exception e){
            logger.error("交易费用计算异常!", e);
        }
        return fee;
    }

    /**
     * <一句话功能简述> 获取股票归属市场
     * <功能详细描述>
     * author: zhanggw
     * 创建时间:  2022/8/13
     * @param fundCode 股票代码
     * @return int sz 深圳 sh 上海
     */
    private static String getStockType(String fundCode) {
        if(fundCode.substring(0,2).equals("60")){
            return "sh";
        }
        if(fundCode.substring(0,3).equals("688")){
            return "sh";
        }
        if(fundCode.substring(0,3).equals("900")){
            return "sh";
        }
        return "sz";
    }

}
