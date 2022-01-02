package com.kenick.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CustomApplicationProperties implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        MutablePropertySources propertySources = environment.getPropertySources();

        // 所有属性文件名
        Iterator<PropertySource<?>> iterator = propertySources.iterator();
        System.out.println(" ================== 所有配置文件名 ==================");
        while(iterator.hasNext()){
            PropertySource<?> next = iterator.next();
            System.out.println(next.getName());
        }

        // 获取主配置文件
        String mainName = "applicationConfig: [classpath:/application.properties]";
        MapPropertySource propertySource = (MapPropertySource) propertySources.get(mainName);
        Map<String, Object> source = propertySource.getSource();
        System.out.println(" ================== 主配置 ==================");
        source.forEach((k,v) -> {
            System.out.println(k+":"+v.toString());
        });
        String env = source.get("spring.profiles.active").toString();

        // 获取激活配置文件
        String activeName = "applicationConfig: [classpath:/application-" + env + ".properties]";
        MapPropertySource activePropertySource = (MapPropertySource) propertySources.get(activeName);
        Map<String, Object> activeSource = activePropertySource.getSource();
        System.out.println(" ================== 当前配置 ==================");
        Map<String, Object> newConfigMap = new HashMap<>();
        activeSource.forEach((k,v) -> {
            System.out.println(k+":"+v.toString());
            newConfigMap.put(k, v.toString()); // value必须要放入String格式
        });
        // 可动态修改配置
        // newConfigMap.replace("log.custom.test", "E:\\tmp\\logs");
        propertySources.replace(activeName, new MapPropertySource(activeName , newConfigMap));
    }

}
