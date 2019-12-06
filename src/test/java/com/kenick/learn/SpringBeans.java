package com.kenick.learn;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import com.kenick.generate.bean.ConstantData;

public class SpringBeans {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		ClassPathResource classPathResource = new ClassPathResource("spring-bean.xml");
		XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(classPathResource);
		ConstantData constantData = xmlBeanFactory.getBean("constantData", ConstantData.class);
		System.out.println(constantData.getConstantId());
	}
}