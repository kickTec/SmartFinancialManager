package com.kenick.util.generate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DBHelper
{
	/**
	 * 驱类动名
	 */
	private static String driverName = null;

	/**
	 * 数据库地�?
	 */
	private static String url = null;

	/**
	 * 数据库用户名
	 */
	private static String userName = null;

	/**
	 * 用户密码
	 */
	private static String userPasswd = null;

	/**
	 * 加载数据库驱动�?�url、用户名、密码配�?
	 */
	static
	{
		if (null == driverName || null == url || 
				null == userName || null == userPasswd)
		{
			loadDBConfig();
		}
		loadDriver();
	}

	/**
	 * <�?句话功能�?�?>
	 * 加载数据库配置文件：驱动类�?�地�?、用户�?�密�?
	 * <功能详细描述>
	 * @see [类�?�类#方法、类#成员]
	 */
	private static void loadDBConfig()
	{
		Properties dbProperty = PropertiesUtil.JDBC_CONF;
		//读取
		driverName = PropertiesUtil.get(dbProperty, "jdbc_driver");
		url = PropertiesUtil.get(dbProperty, "jdbc_url");
		userName = PropertiesUtil.get(dbProperty, "jdbc_user");
		userPasswd = PropertiesUtil.get(dbProperty, "jdbc_password");
	}

	/**
	 * <�?句话功能�?�?>  加载驱动
	 * <功能详细描述>
	 * @see [类�?�类#方法、类#成员]
	 */
	private static void loadDriver()
	{
		try
		{
			Class.forName(driverName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static synchronized Connection getConnection()
	{
		try
		{
			return DriverManager.getConnection(url,userName,userPasswd);
		} catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static void closeALL(ResultSet rs, PreparedStatement ps,
			Connection conn)
	{
		if (null != rs)
		{
			try
			{
				rs.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		if (null != ps)
		{
			try
			{
				ps.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		if (null != conn)
		{
			try
			{
				conn.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		
	}

	
}
