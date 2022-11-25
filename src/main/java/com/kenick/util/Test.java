package com.kenick.util;

import java.util.HashMap;

/**
 * author: zhanggw
 * 创建时间:  2022/11/21
 */
public class Test {

    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<>(4);
        hashMap.put("name1", "tome1");
        hashMap.put("name2", "tome2");
        hashMap.put("name3", "tome3");
        hashMap.put("name4", "tome4");
        System.out.println(hash(32));
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

}
