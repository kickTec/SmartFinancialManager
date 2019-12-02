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

public class AddFeildNameForBean 
{
	private static final String Field_Define = "public static final String S_";
	
	private static final String Added_Sign = "/* feild added*/";

	public void addToBean(String path)
	{
		File file = new File(path);
		//1、递归查询所有需要添加的bean
		List<File> fileList = getFile(file, "bean");
		for (File fl : fileList)
		{
			//2、循环每个文件，进行添加
			//System.out.println(fl.getName());
			addFeild(fl);
		}
		
	}
	// 对要操作的文件进行操作
	public void addFeild(File file)
	{
		//StringUtils.splitByCharacterTypeCamelCase(str)
		//处理文件名，得到表名，查询列名
		if (StringUtils.endsWithIgnoreCase(file.getName(), "Example.java"))
		{
			return ;
		}
		String[] fileNames = StringUtils.splitByCharacterTypeCamelCase(file.getName());
		StringBuilder tableName = new StringBuilder();
		// 例如 BrandsInfo.java 经过拆分，分为Brands Info . java
		for (int i = 0; i < fileNames.length - 2; i++)
		{
			tableName.append(fileNames[i]).append("_");
		}
		// 去除末尾 "_"
		tableName.delete(tableName.length() -1, tableName.length());
		//查询表字段
		ArrayList<String> feids = (new TableAttributeSerialize())
				.getMysqlTableAttri(tableName.toString());
		tableName.delete(0, tableName.length());
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

			// 第一个 { 标识
			boolean flag = true;
			while (true) {
					textinLine = br.readLine();
					if (textinLine == null)
							break;
					if(StringUtils.startsWithIgnoreCase(textinLine, "public class ")) {
						fileString.append("import java.util.ArrayList;")
						.append(System.getProperty("line.separator"))
						.append(System.getProperty("line.separator"));
					}
					//查询第一次以{结尾，在其后面加入列名
					fileString.append(textinLine);
					if (flag && StringUtils.endsWith(textinLine, "{"))
					{
						fileString.append(Added_Sign);
						fileString.append(System.getProperty("line.separator"));
						fileString.append(joinField(feids));
						flag = false;
					}
					
					if (flag && StringUtils.endsWith(textinLine, Added_Sign))
					{
						flag = false;
					}
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
	
	public String joinField(ArrayList<String> feids)
	{
		StringBuilder addStr = new StringBuilder();
		addStr.append(System.getProperty("line.separator"));
		StringBuilder fieldList  = new StringBuilder();
		fieldList.append("\tpublic static final ArrayList<String> fieldList = new ArrayList<String>() {")
		.append(System.getProperty("line.separator"))
		.append("\t\t{").append(System.getProperty("line.separator"));
		for (String field : feids)
		{
			addStr.append("\t").append(Field_Define).append(TableAttributeSerialize.fieldToCamel(field))
			.append(" = \"").append(field).append("\";").append(System.getProperty("line.separator"));
			
			fieldList.append("\t\t\tadd(\"").append(field).append("\");").append(System.getProperty("line.separator"));;
		}
		fieldList.append("\t\t}").append("\t};");
		
		addStr.append(System.getProperty("line.separator"))
		.append(fieldList).append(System.getProperty("line.separator"));
		
		addStr.append(System.getProperty("line.separator"));
		return addStr.toString();
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
		(new AddFeildNameForBean()).addToBean("D:\\yxlm");
	}
}
