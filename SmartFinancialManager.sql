/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.6.22 : Database - smart_financial
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

insert  into `constant_data`(`constant_id`,`constant_name`,`constant_value`,`constant_desc`,`constant_state`,`modify_date`,`create_date`) values ('50002','SMS_SEND_RULE','{\"sendFlag\":true,\"sendPhone\":\"15910761260\",\"000876LowerLimit\":-1.0,\"000876UpperLimit\":1.0,\"fund2DayLowerLimit\":-3.0,\"fund2DayUpperLimit\":3.0,\"512660sendFlag\":true}','短信发送规则',1,'2021-06-15 01:05:02','2020-11-20 13:39:44');

/*Table structure for table `fund` */

DROP TABLE IF EXISTS `fund`;

CREATE TABLE `fund` (
  `fund_code` varchar(150) COLLATE utf8_bin DEFAULT NULL,
  `fund_name` varchar(150) COLLATE utf8_bin DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `cur_time` varchar(150) COLLATE utf8_bin DEFAULT NULL,
  `cur_net_value` double DEFAULT NULL,
  `cur_gain` double DEFAULT NULL,
  `cur_price_highest` double DEFAULT NULL,
  `cur_price_lowest` double DEFAULT NULL,
  `last_net_value` double DEFAULT NULL,
  `last_gain` double DEFAULT NULL,
  `last_price_highest` double DEFAULT NULL,
  `last_price_lowest` double DEFAULT NULL,
  `gain_total` decimal(12,0) DEFAULT NULL,
  `fund_state` int(11) DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  `create_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `fund` */

insert  into `fund`(`fund_code`,`fund_name`,`type`,`cur_time`,`cur_net_value`,`cur_gain`,`cur_price_highest`,`cur_price_lowest`,`last_net_value`,`last_gain`,`last_price_highest`,`last_price_lowest`,`gain_total`,`fund_state`,`modify_date`,`create_date`) values ('159929','汇添富中证医药卫生ETF',1,'04-29 14:27',0,0,0,0,0,0,0,0,'0',1,'2021-06-15 01:05:02','2020-11-04 10:06:46'),('512660','国泰中证军工ETF',1,'04-29 14:28',0,0,0,0,0,0,0,0,'0',1,'2021-06-15 01:05:02','2020-11-04 10:06:49'),('512760','国泰CES半导体芯片ETF',1,'04-29 14:26',0,0,0,0,0,0,0,0,'0',1,'2021-06-15 01:05:02','2020-11-04 10:06:50'),('600036','招商银行',2,'07-20 13:30',49.76,0.12,51.49,49.7,0,0,0,0,'0',1,'2021-07-20 13:30:09','2020-06-04 10:07:02');

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

insert  into `user_fund`(`user_id`,`username`,`wx_code`,`wx_name`,`fund_code`,`fund_name`,`cur_time`,`cur_net_value`,`cur_gain`,`last_net_value`,`last_gain`,`gain_total`,`create_date`) values ('1',NULL,NULL,NULL,'519727','交银成长30混合(前端：519727  后端：519728)','06-02 13:50',1.7645,0.89,1.749,2.88,'3.77',NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
