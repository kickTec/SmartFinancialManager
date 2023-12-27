package com.kenick.mp.controller;

import com.kenick.mp.config.WxMpUtil;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.builder.outxml.TextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @description 微信公众号
 * @date 2023/12/27
 */
@Controller
@RequestMapping("/mp")
public class WxMpController {

    private final static Logger logger = LoggerFactory.getLogger(WxMpController.class);

    @Resource
    private WxMpUtil wxMpUtil;

    /**
     * 与微信服务器保持心跳,同时进行内容校验
     * 必须为get方式
     *
     * @param signature 心跳内容签名,微信服务器配置的token+timestamp+nonce进行sha1加密后的密文
     * @param timestamp 时间戳字符串
     * @param nonce     随机数
     * @param echostr   随机内容,回复时要保持一致
     * @return 校验通过回复对方发送过来的内容, 否则回复空字符串
     */
    @ResponseBody
    @RequestMapping("/heartbeat")
    public String heartbeat(@RequestParam("signature") String signature,
                            @RequestParam("timestamp") String timestamp,
                            @RequestParam("nonce") String nonce,
                            @RequestParam("echostr") String echostr) {
        logger.debug("WxMpController.heartbeat,{},{},{},{}", signature, timestamp, nonce, echostr);
        try {
            if (wxMpUtil.checkSignature("kenick", timestamp, nonce, signature)) {
                return echostr;
            }
        } catch (Exception e) {
            logger.error("公众号心跳异常", e);
        }
        return "";
    }

    /**
     * 接收微信公众号发送过来的消息,与心跳接口路径一样
     * 必须为post方式
     *
     * @return 无自定义回复返回success, 表示服务器已收到;自定义回复xml格式数据
     */
    @ResponseBody
    @PostMapping(value = "/heartbeat")
    public String receive(HttpServletRequest request) {
        try {
            WxMpXmlMessage wxMpXmlMessage = WxMpXmlMessage.fromXml(request.getInputStream());
            logger.debug("WxMpController.receive,{}", wxMpXmlMessage);
            if (wxMpXmlMessage != null && "text".equals(wxMpXmlMessage.getMsgType())) {
                String toUserName = wxMpXmlMessage.getFromUser();
                String fromUserName = wxMpXmlMessage.getToUser();
                String content = wxMpXmlMessage.getContent();
                logger.debug("toUserName:{},fromUserName:{},content:{}", toUserName, fromUserName, content);

                String replyXml = new TextBuilder().fromUser(fromUserName)
                        .toUser(toUserName)
                        .content("replay:" + content + ",\n链接:https://www.baidu.com")
                        .build().toXml();
                logger.debug("回应的xml:{}", replyXml);
                return replyXml;
            }
        } catch (Exception e) {
            logger.error("接收消息异常", e);
        }
        return "success";
    }

    /**
     * 获取微信公众号的accessToken
     *
     * @param passwd 简单的自定义密码验证
     */
    @ResponseBody
    @RequestMapping("/getAccessToken")
    public String getAccessToken(@RequestParam(value = "passwd", required = false) String passwd) {
        logger.debug("WxMpController.getAccessToken");
        try {
            if ("kenick".equals(passwd)) {//简单的密码验证
                return wxMpUtil.getAccessToken();
            }
        } catch (Exception e) {
            logger.error("getAccessToken异常", e);
        }
        return "";
    }

}
