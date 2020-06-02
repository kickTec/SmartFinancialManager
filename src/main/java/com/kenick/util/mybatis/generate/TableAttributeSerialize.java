package com.kenick.util.mybatis.generate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class TableAttributeSerialize 
{
	/**
	 * 数据连接对象
	 */
	private Connection conn;

	/**
	 * 预编译的SQL命令执行对象
	 */
	private PreparedStatement ps;

	/**
	 * 结果集合
	 */
	private ResultSet rs;
	
	/**
	 * 根据sql 语句获取数据表列名
	 * @param sql
	 * @return
	 */
	public ArrayList<String> serializeTableAttr(String sql/*String tableName*/)
	{
		ArrayList<String> attributeList = new ArrayList<String>();
		try
		{
			
			this.conn = DBHelper.getConnection();
			this.ps = this.conn.prepareStatement(sql);
			this.rs = this.ps.executeQuery();
			
			// 获取ResultSet 对象中列的类型和属性信息 ResultSetMateDate对象
			ResultSetMetaData rsmd = rs.getMetaData();
			for(int i = 1; i <= rsmd.getColumnCount(); i++)
			{
				//			取第i列列名 从1开始
				String setMethodName = rsmd.getColumnName(i).toLowerCase();
				attributeList.add(setMethodName);
			}
			
			return attributeList;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DBHelper.closeALL(this.rs, this.ps, this.conn);
		}
		return attributeList;
	}
	
	public ArrayList<String> getCamelAttri(ArrayList<String> attributeList)
	{
		ArrayList<String> camelAttri = new ArrayList<String>();
		for(String ss : attributeList)
		{
			;
			camelAttri.add(fieldToCamel(ss));
		}
		
		return camelAttri;
	}
	
	public static String fieldToCamel(String field)
	{
		StringBuilder addStr = new StringBuilder();
		String[] columnNames = field.split("_");
		for (String columnName : columnNames)
		{
			addStr.append(columnName.substring(0,1).toUpperCase()).append(columnName.substring(1));
		}
		
		String ss = addStr.toString();
		addStr.delete(0, addStr.length());
		addStr.append(ss.substring(0,1).toLowerCase()).append(ss.substring(1));
		return addStr.toString();
	}
	
	public ArrayList<String> getMysqlTableAttri(String tableName)
	{
		String sql = "select * from " + tableName + " limit 1";
		return serializeTableAttr(sql);
	}
	
	public ArrayList<String> getOracleTableAttri(String tableName)
	{
		String sql = "select TOP 1 * from " + tableName;
		return serializeTableAttr(sql);
	}
	
	
	public ArrayList<String> getCamelAttriMysql(String tableName)
	{
		String sql = "select * from " + tableName + " limit 1";
		return getCamelAttri(serializeTableAttr(sql));
	}
	
	public ArrayList<String> getCamelAttriOracle(String tableName)
	{
		String sql = "select TOP 1 * from " + tableName;
		return getCamelAttri(serializeTableAttr(sql));
	}
	
    /**
	 * 根据数据库连接和表明获取主键名
	 * @param con 传进来一个数据库连接对象
	 * @param table 数据库中的表名
	 * @return  执行成功返回一个主键名的字符数组，否则返回null或抛出一个异常
	 * @exception 抛出sql执行异常
	 * @author yuyu
	 */
	public String[] getPrimaryKey(String table) throws Exception{
		
		String sql="SHOW CREATE TABLE "+table;
		
		try {
			this.conn = DBHelper.getConnection();
			PreparedStatement pre= this.conn.prepareStatement(sql);
			ResultSet rs=pre.executeQuery();
			if(rs.next()){

				//正则匹配数据
				Pattern pattern = Pattern.compile("PRIMARY KEY \\(\\`(.*)\\`\\)");
				Matcher matcher =pattern.matcher(rs.getString(2));
				matcher.find();
				String data=matcher.group();
				//过滤对于字符
				data=data.replaceAll("\\`|PRIMARY KEY \\(|\\)", "");
				//拆分字符
				String [] stringArr= data.split(",");
				
				return stringArr;
			}
			
		}catch(Exception e){
			throw e;
		}
		return null;
	}
	
	// 获取建表语句 并构造成接口文档表格
	public void getTable(String tableName) 
	{
		String tableCreate = "";
		try
		{
			String sql = "show create table " + tableName + ";";
			this.conn = DBHelper.getConnection();
			this.ps = this.conn.prepareStatement(sql);
			this.rs = this.ps.executeQuery();
			
			// 获取ResultSet 对象中
			while(rs.next()) {
				tableCreate = rs.getString(2);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DBHelper.closeALL(this.rs, this.ps, this.conn);
		}
		//System.out.println(tableCreate);
		tableCreate = StringUtils.replaceAll(tableCreate, "\\(\\d*,?\\d*\\).*COMMENT ", " COMMENT");
		tableCreate = StringUtils.replaceAll(tableCreate, " DEFAULT.*COMMENT ", " COMMENT");
		tableCreate = StringUtils.replaceAll(tableCreate, "  `", "");
		tableCreate = StringUtils.replaceAll(tableCreate, "`", "");
		tableCreate = StringUtils.substringBetween(tableCreate, " (", "  PRIMARY KEY");
		tableCreate = StringUtils.replaceAll(tableCreate, "',", "");
		tableCreate = StringUtils.replaceAll(tableCreate, "COMMENT'", "");
		tableCreate = StringUtils.replaceAll(tableCreate, "varchar", "String");
		tableCreate = StringUtils.replaceAll(tableCreate, "decimal", "dobule");
		String[] rows = StringUtils.split(tableCreate, "\n");
		for (String row : rows) {
			String rowName = StringUtils.substringBefore(row, " ");
			rowName = fieldToCamel(rowName);
			String rest = StringUtils.substringAfter(row, " ");
			String type = StringUtils.substringBefore(rest, " ");
			rest = StringUtils.substringAfter(rest, " ");
			System.out.println(rowName+ "\t" + type + "\t" + rest);
		}
	}
	
	public static void main(String[] args) {
		(new TableAttributeSerialize()).getTable("item_theme_relate");
	}
}
