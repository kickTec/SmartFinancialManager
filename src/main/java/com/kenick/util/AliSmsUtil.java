package com.kenick.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AliSmsUtil {

	public static String accessKeyIdSms; // 阿里短信ID
	public static String accessKeySecretSms; // 阿里短信密码

	private final static Logger logger = LoggerFactory.getLogger(AliSmsUtil.class);

	static {
		if(StringUtils.isBlank(accessKeyIdSms)){
			accessKeyIdSms = FileUtil.getPropertyByEnv("ali.sms.accessKeyId");
		}
		if(StringUtils.isBlank(accessKeySecretSms)){
			accessKeySecretSms = FileUtil.getPropertyByEnv("ali.sms.accessKeySecret");
		}
		logger.debug("smsId:{},smsSecret:{}", accessKeyIdSms, accessKeySecretSms);
	}
	
	// 阿里发送短信码
	public static JSONObject aliSendSmsCode(String phone, String code)
	{	
		JSONObject rtnJson = new JSONObject();
		rtnJson.put("flag", true);
		
		// 阿里短信配置
		String signName = "智慧仓储";
		String templateCodee = "SMS_175533709";
		JSONObject sendMessageParam = new JSONObject();
		sendMessageParam.put("code", code);
		
		//设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "3000");
		System.setProperty("sun.net.client.defaultReadTimeout", "3000");
		//初始化ascClient需要的几个参数
		final String product = "Dysmsapi"; //短信API产品名称（短信产品名固定，无需修改）
		final String domain = "dysmsapi.aliyuncs.com"; //短信API产品域名（接口地址固定，无需修改）
		
		//初始化ascClient,暂时不支持多region
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyIdSms, accessKeySecretSms);
		try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		} catch (ClientException e) {
			logger.error("errorCode: sys_alisms_send_error, errorMessage: 阿里DefaultProfile.addEndpoint添加失败", e);
			rtnJson.put("flag", false);
			rtnJson.put("errorCode", "sys_alisms_send_error");
			rtnJson.put("errorMessage", "阿里DefaultProfile.addEndpoint添加失败");
			return rtnJson;
		}
		IAcsClient acsClient = new DefaultAcsClient(profile);
		 //组装请求对象
		 SendSmsRequest request = new SendSmsRequest();
		 //使用post提交
		 request.setMethod(MethodType.POST);
		 //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
		 request.setPhoneNumbers(phone);
		 //必填:短信签名-可在短信控制台中找到
		 request.setSignName(signName);
		 //必填:短信模板-可在短信控制台中找到
		 request.setTemplateCode(templateCodee);
		 //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		 //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		 request.setTemplateParam(sendMessageParam.toJSONString());
		 //可选-上行短信扩展码(无特殊需求用户请忽略此字段)
		 //request.setSmsUpExtendCode("90997");
		 //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		 request.setOutId(phone);
		 
		//请求失败这里会抛ClientException异常
		SendSmsResponse sendSmsResponse = null;
		try {
			sendSmsResponse = acsClient.getAcsResponse(request);
			logger.debug("向{}发送{}结果code:{},描述:{}", phone, code, sendSmsResponse.getCode(), sendSmsResponse.getMessage());
			if(!"OK".equals(sendSmsResponse.getCode())){
				rtnJson.put("flag", false);
				rtnJson.put("errorCode", "send_response_noOk");
				rtnJson.put("errorMessage", "阿里验证码发送失败");
			}
		} catch (Exception e) {
			logger.error("errorCode: sys_verifycode_send_error, errorMessage: 阿里验证码发送失败", e);
			rtnJson.put("flag", false);
			rtnJson.put("errorCode", "sys_verifycode_send_error");
			rtnJson.put("errorMessage", "阿里验证码发送失败");
			return rtnJson;
		}
		return rtnJson;
	}

	public static void main(String[] args) {
		JSONObject ret = aliSendSmsCode("15910761260", "123456");
		System.out.println(ret);
	}

}
