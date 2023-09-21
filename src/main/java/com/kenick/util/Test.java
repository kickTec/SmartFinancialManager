package com.kenick.util;

import java.util.HashMap;

/**
 * author: zhanggw
 * 创建时间:  2022/11/21
 */
public class Test {

    public static void main(String[] args) {
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

}
