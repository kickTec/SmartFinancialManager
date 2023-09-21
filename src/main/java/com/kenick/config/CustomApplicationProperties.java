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
        String env = "";
        Iterator<PropertySource<?>> iterator = propertySources.iterator();
        while(iterator.hasNext()){
            PropertySource<?> next = iterator.next();
            String configFileName = next.getName();
            if(configFileName.contains("application-")){ // spring只会加载当前环境配置
                env = configFileName.split("application-")[1].replace(".properties]", "");
            }
        }

        // 获取主配置文件
        String mainName = "applicationConfig: [classpath:/application.properties]";
        MapPropertySource propertySource = (MapPropertySource) propertySources.get(mainName);
        Map<String, Object> source = propertySource.getSource();

        // 获取激活配置文件s
        String activeName = "applicationConfig: [classpath:/application-" + env + ".properties]";
        MapPropertySource activePropertySource = (MapPropertySource) propertySources.get(activeName);
        Map<String, Object> activeSource = activePropertySource.getSource();
        Map<String, Object> newConfigMap = new HashMap<>();
        activeSource.forEach((k,v) -> {
            newConfigMap.put(k, v.toString()); // value必须要放入String格式
        });
        // 可动态修改配置
        // newConfigMap.replace("log.custom.test", "E:\\tmp\\logs");
        propertySources.replace(activeName, new MapPropertySource(activeName , newConfigMap));
    }

}
