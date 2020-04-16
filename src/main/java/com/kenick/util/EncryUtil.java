package com.kenick.util;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * author: zhanggw
 * 创建时间:  2020/1/7
 */
public class EncryUtil {
    public static void main(String[] args) {
        String password = "kenick@2020";
        String data = "zhang417259";

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName(null);
        config.setProviderClassName(null);
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        System.out.println(encryptor.encrypt(data));
        System.out.println(encryptor.decrypt("1gv1JleN/Q4+exio70YZuQ1wVQqEFajY"));
    }
}
