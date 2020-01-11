package com.kenick.util.generate;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GeneratorMain {
	
	public static void main(String[] args) {
		List<String> warnings = new ArrayList<>();
		//读取配置文件
		File configFile = new File("src/main/resources/generatorConfig.xml");
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config;
		try {
			config = cp.parseConfiguration(configFile);

			DefaultShellCallback callback = new DefaultShellCallback(true);
			MyBatisGenerator myBatisGenerator;
			try {
				myBatisGenerator = new MyBatisGenerator(config, callback,
						warnings);
				myBatisGenerator.generate(null);

				//打印结果
				for(String str : warnings){
					System.out.println(str);
				}
				config.getClassPathEntries();
				for (Context str : config.getContexts())
				{
					JavaModelGeneratorConfiguration  modelConfig = str.getJavaModelGeneratorConfiguration();//bean
					JavaClientGeneratorConfiguration clientConfig = str.getJavaClientGeneratorConfiguration();//dao
					SqlMapGeneratorConfiguration sqlmapConfig = str.getSqlMapGeneratorConfiguration();//mapper
					String projectPath = modelConfig.getTargetProject();
					String packagePath = modelConfig.getTargetPackage();
					String fieldPath = projectPath + "/" + StringUtils.replace(packagePath, ".", "/");
					(new AddFeildNameForBean()).addToBean(fieldPath);
					AddSelectFeildForMapper selectFeid = new AddSelectFeildForMapper();

					String daoPath = projectPath + "/" + StringUtils.replace(clientConfig.getTargetPackage(), ".", "/");
					selectFeid.addToDao(daoPath);
					
					String mappingPath = projectPath + "/" + StringUtils.replace(sqlmapConfig.getTargetPackage(), ".", "/");
					selectFeid.addToXML(mappingPath);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("完成!");
	}
}
