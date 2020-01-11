/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.6.41 : Database - smart_financial
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`smart_financial` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `smart_financial`;

/*Table structure for table `constant_data` */

DROP TABLE IF EXISTS `constant_data`;

CREATE TABLE `constant_data` (
  `constant_id` varchar(30) COLLATE utf8_bin NOT NULL COMMENT '常量id',
  `constant_name` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT '常量名',
  `constant_value` varchar(10240) COLLATE utf8_bin DEFAULT NULL COMMENT '常量值',
  `constant_desc` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT '常量说明',
  `constant_state` int(1) DEFAULT NULL COMMENT '常量状态(0.失效1.正常)',
  `modify_date` datetime DEFAULT NULL COMMENT '修改时间',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`constant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='业务常量表';

/*Data for the table `constant_data` */

/*Table structure for table `fund` */

DROP TABLE IF EXISTS `fund`;

CREATE TABLE `fund` (
  `fund_code` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '基金编码',
  `fund_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '基金名称',
  `cur_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '当前估算时间',
  `cur_net_value` double DEFAULT NULL COMMENT '当前估算净值',
  `cur_gain` double DEFAULT NULL COMMENT '当前涨幅',
  `last_net_value` double DEFAULT NULL COMMENT '上一日净值',
  `last_gain` double DEFAULT NULL COMMENT '上一日涨幅',
  `gain_total` decimal(10,2) DEFAULT NULL COMMENT '总涨幅',
  `fund_state` int(11) DEFAULT NULL COMMENT '基金状态',
  `modify_date` datetime DEFAULT NULL COMMENT '修改时间',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`fund_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `fund` */

insert  into `fund`(`fund_code`,`fund_name`,`cur_time`,`cur_net_value`,`cur_gain`,`last_net_value`,`last_gain`,`gain_total`,`fund_state`,`modify_date`,`create_date`) values ('001645','国泰大健康股票(001645)','01-10 15:00',2.3013,0.36,2.291,-0.09,'0.27',NULL,NULL,NULL),('003096','中欧医疗健康混合C(003096)','01-10 15:00',1.7966,0.42,1.798,0.5,'0.92',NULL,NULL,NULL),('003191','创金合信消费主题股票C(003191)','01-10 15:00',1.6425,-0.08,1.648,0.26,'0.18',NULL,NULL,NULL),('003299','嘉实物流产业股票C(003299)','01-10 15:00',1.5081,-0.46,1.507,-0.53,'-0.99',NULL,NULL,NULL),('003625','创金合信资源股票发起式C(003625)','01-10 15:00',1.0972,-0.73,1.1038,-0.14,'-0.87',NULL,NULL,NULL),('003956','南方教育股票(003956)','01-10 15:00',1.4921,0.67,1.4826,0.03,'0.70',NULL,NULL,NULL),('005240','银华文体娱乐量化股票发起式C(005240)','01-10 15:00',0.9158,-0.15,0.9133,-0.43,'-0.58',NULL,NULL,NULL),('005496','创金合信科技成长股票C(005496)','01-10 15:00',1.3862,0.13,1.3899,0.4,'0.53',NULL,NULL,NULL),('005928','创金合信新能源汽车股票C(005928)','01-10 15:00',1.1331,-0.35,1.138,0.09,'-0.26',NULL,NULL,NULL),('005963','宝盈人工智能股票C(005963)','01-10 15:00',1.7787,0.44,1.7847,0.78,'1.22',NULL,NULL,NULL),('070001','嘉实成长收益混合A(070001)','01-10 15:00',1.1785,0.09,1.1915,1.19,'1.28',NULL,NULL,NULL),('070011','嘉实策略混合(070011)','01-10 15:00',1.2214,0.03,1.216,-0.41,'-0.38',NULL,NULL,NULL),('160136','南方中证国有企业改革指数分级(160136)','01-10 15:00',1.0258,-0.24,1.0257,-0.24,'-0.48',NULL,NULL,NULL),('160222','国泰国证食品饮料行业指数分级(160222)','01-10 15:00',1.1108,1.21,1.1108,1.2,'2.41',NULL,NULL,NULL),('163402','兴全趋势投资混合(LOF)(前端：163402  后端：163403)','01-10 15:00',0.7552,-0.11,0.757,0.13,'0.02',NULL,NULL,NULL);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `user_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '用户id',
  `username` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `password` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '密码',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `gender` int(11) DEFAULT NULL COMMENT '性别',
  `wx_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '微信编码',
  `wx_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '微信昵称',
  `modify_date` datetime DEFAULT NULL COMMENT '修改时间',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `user` */

/*Table structure for table `user_fund` */

DROP TABLE IF EXISTS `user_fund`;

CREATE TABLE `user_fund` (
  `user_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '用户id',
  `username` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '用户姓名',
  `wx_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '微信编码',
  `wx_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '微信昵称',
  `fund_code` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '基金编码',
  `fund_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '基金名称',
  `cur_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '当前估算时间',
  `cur_net_value` double DEFAULT NULL COMMENT '当前估算净值',
  `cur_gain` double DEFAULT NULL COMMENT '当前涨幅',
  `last_net_value` double DEFAULT NULL COMMENT '上一日净值',
  `last_gain` double DEFAULT NULL COMMENT '上一日涨幅',
  `gain_total` decimal(10,2) DEFAULT NULL COMMENT '累计涨幅（连续2天）',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`,`fund_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `user_fund` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
