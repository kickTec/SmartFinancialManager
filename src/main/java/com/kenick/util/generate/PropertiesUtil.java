package com.kenick.util.generate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;


public class PropertiesUtil
{

	/**
	 * jdbc 配置
	 */
	public static Properties JDBC_CONF = PropertiesUtil.Load("generator.properties");
	
	private static Properties Load(String fileName)
	{
		Properties proper = new Properties();
		
		ClassLoader clsLoader = Thread.currentThread().getContextClassLoader();
		//System.out.println(clsLoader.getResource("").getPath());
		URL file = clsLoader.getResource(fileName);
		InputStream in = null;
		
		if (null == file)
		{
			return null;
		}
		
		try
		{
			in = file.openStream();
			proper.load(in);
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			if (null != in)
			{
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				in = null;
			}
		}
		return proper;
	}
	public static String get(Properties properties, String key)
	{
		return get(properties, key, null);
	}
	public static String get(Properties properties, String key, String defultValue)
	{
		return properties.getProperty(key, defultValue);
	}
}
