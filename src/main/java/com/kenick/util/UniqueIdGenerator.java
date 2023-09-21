package com.kenick.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;


@Configuration
public class UniqueIdGenerator
{
	/**
	 *  微服务编号: 001 用户管理
	 */
	private static final String sysDfNum = "000";
	
	/**
	 * 机器编码 999
	 */
	@Value("${id.generator.machine}")
	private String machineCode ;
	
	private long sequence = 0L;
	private long lastTimestamp = -1L;
	private Lock lock = new ReentrantLock();
	
	public String generateId(String sysNum) throws Exception
	{
		if( !StringJuge.isNumeric(sysNum, 3))
		{
			sysNum = sysDfNum;
		}
		String id = populateId(sysNum);
		for (int i = 0; i < 7 && StringUtils.isEmpty(id); i++)
		{
			if(i > 5)
			{
				throw new Exception("生成Id失败！");
			}
			id = populateId(sysNum);
		}
		return id;
	}
	
	private String populateId(String sysNum)
	{
		this.lock.lock();
		if(machineCode ==null){
			machineCode = "000";
		}
		String id = sysNum + machineCode;
		try
	    {
	      long timestamp = System.currentTimeMillis();
	      if (timestamp == this.lastTimestamp)
	      {
	        this.sequence += 1L;
	        this.sequence = this.sequence < 0L ? 0L:this.sequence;
	        if (this.sequence == 0L) {
	          timestamp = tillNextTimeUnit(this.lastTimestamp);
	        }
	      }
	      else
	      {
	        this.lastTimestamp = timestamp;
	        this.sequence = 0L;
	      }
	      id += String.format("%013d", timestamp) + String.format("%02d", this.sequence);
	    }
	    finally
	    {
	      this.lock.unlock();
	    }
		return id;
	}
	
	private long tillNextTimeUnit(long lastTimestamp)
	  {
	    long timestamp = System.currentTimeMillis();
	    while (timestamp <= lastTimestamp) {
	      timestamp = System.currentTimeMillis();
	    }
	    return timestamp;
	  }
	
	public static void main(String[] args) {
		try {
			String id = new UniqueIdGenerator().generateId("001");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
