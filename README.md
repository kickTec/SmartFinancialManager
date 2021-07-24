# SmartFinancialManager 智慧金融管理


智慧、量化、长期的开源基金管理工具
Intelligent, quantitative and long-term open source fund management tool

# update method 更新方式
git pull origin master  
git add .  
git commit -a  
git push -u origin master  

# database 数据库
./database

# index 首页
http://www.kenick.top/

# 不使用mysql,使用本地文件存储  
storage.type=file  
storage.file.fund=/home/kenick/smartFinancial-manager/config/fund.json  

fund.json格式  
{
	"fundList": [{
		{
			"createDate": 1591236422000,
			"curGain": 0.62,
			"curNetValue": 49.92,
			"curPriceHighest": 51.49,
			"curPriceLowest": 49.61,
			"curTime": "07-20 14:22",
			"fundCode": "600036",
			"fundName": "招商银行",
			"fundState": 1,
			"gainTotal": 0.62,
			"lastGain": 0,
			"lastNetValue": 0,
			"lastPriceHighest": 0,
			"lastPriceLowest": 0,
			"modifyDate": 1626762120001,
			"type": 2
		}]
	}
