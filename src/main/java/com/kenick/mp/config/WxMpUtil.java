package com.kenick.mp.config;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @description 微信公众号工具类
 * @date 2023/12/27
 */
@Component
public class WxMpUtil {

    private final static Logger logger = LoggerFactory.getLogger(WxMpUtil.class);

    //微信公众号id
    @Value("${wechat.mpAppId}")
    private String mpAppId;

    //微信公众号密码
    @Value("${wechat.mpAppSecret}")
    private String mpAppSecret;

    //微信公众号工具服务
    private static WxMpService wxMpService;

    //获取mp服务
    public WxMpService getMpService() {
        if (wxMpService != null) {
            return wxMpService;
        }
        WxMpDefaultConfigImpl wxMpConfigStorage = new WxMpDefaultConfigImpl();
        wxMpConfigStorage.setAppId(mpAppId);
        wxMpConfigStorage.setSecret(mpAppSecret);
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
        return wxMpService;
    }

    //通过WxMpService工具获取accessToken
    public String getAccessToken() {
        try {
            return getMpService().getAccessToken();
        } catch (Exception e) {
            logger.error("获取accessToken异常", e);
        }
        return "";
    }

    //校验心跳消息签名
    public boolean checkSignature(String token, String timestamp, String nonce, String signature) {
        try {
            return signature.equals(gen(token, timestamp, nonce));
        } catch (Exception e) {
            logger.error("校验签名异常", e);
        }
        return false;
    }

    //sha1加密
    public String gen(String... arr) {
        if (StringUtils.isAnyEmpty(arr)) {
            throw new IllegalArgumentException("非法请求参数，有部分参数为空 : " + Arrays.toString(arr));
        } else {
            Arrays.sort(arr);
            StringBuilder sb = new StringBuilder();
            String[] var2 = arr;
            int var3 = arr.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String a = var2[var4];
                sb.append(a);
            }

            return DigestUtils.sha1Hex(sb.toString());
        }
    }

}
