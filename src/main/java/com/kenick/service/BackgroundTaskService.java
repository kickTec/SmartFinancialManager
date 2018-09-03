package com.kenick.service;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kenick.entity.User;

@Component
@Configurable
@EnableScheduling
public class BackgroundTaskService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private UserService userService;
	
    @Scheduled(fixedRate = 1000 * 30)
    public void reportCurrentTime(){
        User user = new User();
        user.setUsername(System.currentTimeMillis()+"");
		userService.addUser(user);
    }

    //每1分钟执行一次
    @Scheduled(cron = "0 */1 *  * * * ")
    public void reportCurrentByCron(){
    	logger.info("Scheduling Tasks Examples By Cron: The time is now " +new Date());
    }
}