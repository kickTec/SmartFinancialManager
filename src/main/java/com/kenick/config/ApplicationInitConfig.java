package com.kenick.config;

import com.kenick.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class ApplicationInitConfig implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(ApplicationInitConfig.class);

    @Value("${spring.profiles.active}")
    String env;

    @Value("${log.custom.dir}")
    String curLogDir;

    @Autowired
    Environment environment;

    @Autowired
    SpringContextUtil springContextUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.trace("ApplicationInitConfig.in,env:{},curLogDir:{}", env, curLogDir);
    }

}