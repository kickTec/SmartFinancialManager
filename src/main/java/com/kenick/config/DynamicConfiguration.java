package com.kenick.config;

/**
 * author: zhanggw
 * 创建时间:  2023-03-04
 */
public class DynamicConfiguration {

    // 市盈率描述是否显示
    public static String perFlag = "1";

    // 总市值描述是否显示
    public static String capFlag = "1";

    // 总市值描述是否显示
    public static String weekendFlag = "0";

    // 证券时间9-15
    public static String fundTimeFlag = "1";

    // 更新间隔，默认10秒
    public static int updateFundInterval = 10;

    // 加载配置文件间隔，默认20秒
    public static int loadConfigInterval = 20;

}
