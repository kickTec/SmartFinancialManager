package com.kenick.util;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * author: zhanggw
 * 创建时间:  2020/11/23
 */
public class EncryUtil {
    public static void main(String[] args) {
        String encryptKey = "kenick@2020";
        String data = "123456";

        System.out.println(encryByJasypt(encryptKey, data));
        System.out.println(decryptByJasypt(encryptKey,"PC8X1FRCVZauoRxEr/1BYFLAuQo/udonAPIrTxnLxahn6Hz+a8czlw=="));
    }

    private static String encryByJasypt(String key,String data){
        return generateEncryptor(key).encrypt(data);
    }

    public static String decryptByJasypt(String key,String encryData){
        return generateEncryptor(key).decrypt(encryData);
    }

    public static PooledPBEStringEncryptor generateEncryptor(String encryKey){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(encryKey);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName(null);
        config.setProviderClassName(null);
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        return encryptor;
    }

}
