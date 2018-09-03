package com.kenick.log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kenick.service.BackgroundTaskService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoggerTest {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private BackgroundTaskService bts;
	
    @Test
    public void contextLoads() {
    	logger.debug("bts:{}", bts);
    	logger.debug("logger:{}", logger);
    }
}
