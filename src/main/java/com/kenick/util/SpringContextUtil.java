package com.kenick.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Properties;

/**
 * author: zhanggw
 * 创建时间:  2021/12/6
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.context = applicationContext;
        Environment environment = context.getEnvironment();
    }

    // 传入线程中
    public <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    // 国际化使用
    public String getMessage(String key) {
        return context.getMessage(key, null, Locale.getDefault());
    }

    /// 获取当前环境
    public String getActiveProfile() {
        Environment environment = context.getEnvironment();
        return environment.getActiveProfiles()[0];
    }

    public Resource getResource(String location) {
        try {
            return context.getResource(location);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
