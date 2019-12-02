package com.kenick.util.generate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class AddSelectFeildForMapper 
{
	public void addToDao(String path)
	{
		File file = new File(path);
		//1、递归查询所有需要添加的bean
		List<File> fileList = AddFeildNameForBean.getFile(file, "dao");
		for (File fl : fileList)
		{
			//2、循环每个文件，进行添加
			//System.out.println(fl.getName());
			addDaoFeild(fl);
		}
	}
	// 对要操作的文件进行操作
	public void addDaoFeild(File file)
	{
		//StringUtils.splitByCharacterTypeCamelCase(str)
		/*
		String[] fileNames = StringUtils.splitByCharacterTypeCamelCase(file.getName());
		StringBuilder beanName = new StringBuilder();
		// 例如 BrandsInfoMapper.java 经过拆分，分为Brands Info Mapper . java
		for (int i = 0; i < fileNames.length - 3; i++)
		{
			beanName.append(fileNames[i]);
		}*/
		String[] fileNames = StringUtils.splitByCharacterTypeCamelCase(file.getName());
		StringBuilder tableName = new StringBuilder();
		// 例如 BrandsInfo.java 经过拆分，分为Brands Info . java
		for (int i = 0; i < fileNames.length - 3; i++)
		{
			tableName.append(fileNames[i]).append("_");
		}
		tableName.delete(tableName.length() -1, tableName.length());

		String[] primaryKewArray = null;
 		// 查询表主键
		try {
			primaryKewArray = new TableAttributeSerialize().getPrimaryKey(tableName.toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 读原文件
		FileInputStream fs = null;
		InputStreamReader in = null;
		BufferedReader br = null;
		StringBuilder fileString = new StringBuilder();
		try {
			
			String textinLine;
			fs = new FileInputStream(file);
			in = new InputStreamReader(fs);
			br = new BufferedReader(in);

			StringBuilder exampleLine = new StringBuilder();
			StringBuilder primaryLine = new StringBuilder();
			while (true) {
					textinLine = br.readLine();
					if (textinLine == null)
							break;
					if (StringUtils.contains(textinLine, "> selectByExample(")) {
						exampleLine.append(StringUtils.replace(textinLine, "> selectByExample(",
								"> selectFieldByExample(@Param(\"filedList\") List<String> filedList, @Param(\"example\") "))
						.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
					}
					if (StringUtils.contains(textinLine, " selectByPrimaryKey(") && primaryKewArray != null) {
						//  取Primary Key
						primaryLine.append(StringUtils.substringBefore(textinLine, " selectByPrimaryKey("));
						primaryLine.append(" selectFieldByPrimaryKey(@Param(\"filedList\") List<String> filedList, ");
						
						for (String pK : primaryKewArray) {
							primaryLine.append("@Param(\"")
							.append(TableAttributeSerialize.fieldToCamel(pK))
							.append("\") ").append("String ")
							.append(TableAttributeSerialize.fieldToCamel(pK))
							.append(", ");
						}
						
						primaryLine.delete(primaryLine.length() - 2, primaryLine.length());
						
						primaryLine.append(");")
						.append(System.getProperty("line.separator"))
						.append(System.getProperty("line.separator"));
					}
					
					//判断是否是最后一行
					if (StringUtils.equals(textinLine, "}"))
					{
						// 添加文件内容
						fileString.append(exampleLine).append(primaryLine);
						fileString.append(System.getProperty("line.separator"));
						
						//添加 添加标识
						fileString.append("/* field added */");
					}
					fileString.append(textinLine);
					
					//换行
					fileString.append(System.getProperty("line.separator"));
			}
			//System.out.println(fileString.toString());
			FileWriter fstream = new FileWriter(file);
			BufferedWriter outobj = new BufferedWriter(fstream);
			outobj.write(fileString.toString());
			outobj.close();
		} catch (FileNotFoundException e) {
		e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		}
		finally
		{
			try {
				fs.close();
				in.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void addToXML(String path)
	{
		File file = new File(path);
		//1、递归查询所有需要添加的bean
		List<File> fileList = AddFeildNameForBean.getFile(file, "mapping");
		for (File fl : fileList)
		{
			//2、循环每个文件，进行添加
			//System.out.println(fl.getName());
			addXMLFeild(fl);
		}
	}
	
	// 对要操作的文件进行操作
	public void addXMLFeild(File file)
	{
		//StringUtils.splitByCharacterTypeCamelCase(str)
		
		String[] fileNames = StringUtils.splitByCharacterTypeCamelCase(file.getName());
		StringBuilder tableName = new StringBuilder();
		// 例如 BrandsInfoMapper.xml 经过拆分，分为Brands Info Mapper . xml
		for (int i = 0; i < fileNames.length - 3; i++)
		{
			tableName.append(fileNames[i]).append("_");
		}
		// 去除末尾 "_"
		tableName.deleteCharAt(tableName.length() -1);

		// 读原文件
		FileInputStream fs = null;
		InputStreamReader in = null;
		BufferedReader br = null;
		StringBuilder fileString = new StringBuilder();
		try {
			
			String textinLine;
			fs = new FileInputStream(file);
			in = new InputStreamReader(fs);
			br = new BufferedReader(in);

			int exampleFlag = 0;
			StringBuilder exampleXml = new StringBuilder();
			
			while (true) {
					textinLine = br.readLine();
					if (textinLine == null)
							break;
					// 1、查询并获取 selectByExample 查询块
					exampleFlag = fieldStruct(textinLine, exampleXml, exampleFlag);
					
					// 2、查询并获取 selectByPrimaryKey 查询块
					//primaryFlag = fieldStruct(textinLine, primaryXml, primaryFlag);
					
					//判断是否是最后一行
					if (StringUtils.equals(textinLine, "</mapper>"))
					{
						// 添加文件内容
						fileString.append(exampleXml);
						fileString.append(System.getProperty("line.separator"));
						/*fileString.append(primaryXml);
						fileString.append(System.getProperty("line.separator"));*/
						//添加 添加标识
						fileString.append("<!-- field added -->");
					}
					fileString.append(textinLine);
					
					//换行
					fileString.append(System.getProperty("line.separator"));
			}
			//System.out.println(fileString.toString());
			FileWriter fstream = new FileWriter(file);
			BufferedWriter outobj = new BufferedWriter(fstream);
			outobj.write(fileString.toString());
			outobj.close();
		} catch (FileNotFoundException e) {
		e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		}
		finally
		{
			try {
				fs.close();
				in.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	private int fieldStruct(String textinLine, StringBuilder xml, int flag) {
		if (StringUtils.contains(textinLine, "id=\"selectByExample\"")) {
			// 处理第一行  <select id="selectByExample" parameterType="com.ybjdw.infomanage.info.bean.InfoManageExample" resultMap="BaseResultMap">
			xml.append(StringUtils.replace(
					StringUtils.replaceAll(textinLine, "parameterType=\".*\" ", "parameterType=\"map\" "),
					"selectByExample", "selectFieldByExample"))
			.append(System.getProperty("line.separator"))
			.append("    select");
			
			return 1;
		}
		if (StringUtils.contains(textinLine, "id=\"selectByPrimaryKey\"")) {
			// 处理第一行  
			xml.append(StringUtils.replace(
					StringUtils.replaceAll(textinLine, "parameterType=\".*\" ", "parameterType=\"map\" "), 
					"selectByPrimaryKey", "selectFieldByPrimaryKey"))
			.append(System.getProperty("line.separator"))
			.append("    select");
			
			return 1;
		}
		
		if (flag == 1) {
			if (StringUtils.contains(textinLine, " from ")) {
				xml.append(System.getProperty("line.separator"))
				.append("  	<if test=\"filedList != null\" >")
				.append(System.getProperty("line.separator"))
				.append("      <foreach collection=\"filedList\" item=\"obj\" index=\"index\" separator=\",\" >")
				.append(System.getProperty("line.separator"))
				.append("	  	${obj}")
				.append(System.getProperty("line.separator"))
				.append("	  	</foreach>")
				.append(System.getProperty("line.separator"))
				.append("    </if>")
				.append(System.getProperty("line.separator"))
				.append("	<if test=\"filedList == null\" >")
				.append(System.getProperty("line.separator"))
				.append("		<include refid=\"Base_Column_List\" />")
				.append(System.getProperty("line.separator"))
				.append("	</if>")
				.append(System.getProperty("line.separator"));
				
				xml.append(textinLine);
				
				return 2;
			}
		}
		if (flag == 2) {
			if (StringUtils.contains(textinLine, "</select>")) {
				xml.append(System.getProperty("line.separator"));
				xml.append(textinLine);
				xml.append(System.getProperty("line.separator"));
				
				return 3;
			} else if (StringUtils.contains(textinLine, "_parameter")) {
				xml.append(System.getProperty("line.separator"));
				xml.append(StringUtils.replace(textinLine, "_parameter", "example"));
			} else if (StringUtils.contains(textinLine, "Example_Where_Clause")) {
				xml.append(System.getProperty("line.separator"));
				xml.append(StringUtils.replace(textinLine, "Example_Where_Clause", "Update_By_Example_Where_Clause"));
			} else if (StringUtils.contains(textinLine, "orderByClause")) {
				xml.append(System.getProperty("line.separator"));
				xml.append(StringUtils.replace(textinLine, "orderByClause", "example.orderByClause"));
			} else {
				xml.append(System.getProperty("line.separator"));
				xml.append(textinLine);
			}
		}
		return flag;
	}
	
	public static List<File> getFile(File file, String matched)
	{
		List<File> fileList = new ArrayList<File>();
		
		if (file.isDirectory() && file.getName().equals(matched))
		{
			return Arrays.asList(file.listFiles());
		}
		if (! file.isDirectory())
		{
			return fileList;
		}
		for (File fl : file.listFiles())
		{
			fileList.addAll(getFile(fl,matched));
		}
		
		return fileList;
	}
	
	public static void main(String[] args)
	{
		String exampleXml = "<select id=\"selectByExample\" parameterType=\"com.ybjdw.infomanage.info.bean.InfoManageExample\" resultMap=\"BaseResultMap\">";
		String exampleXml2 = StringUtils.replaceAll(exampleXml, "parameterType=\".*\" ", "parameterType=\"map\" ");
		System.out.println(exampleXml);
		System.out.println(exampleXml2);
	}
}
