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
  `constant_id` varchar(30) NOT NULL COMMENT '常量id',
  `constant_desc` varchar(256) DEFAULT NULL COMMENT '常量说明',
  `constant_name` varchar(256) DEFAULT NULL COMMENT '常量名',
  `constant_value` varchar(10240) DEFAULT NULL COMMENT '常量值',
  `constant_state` int(1) DEFAULT NULL COMMENT '常量状态(0.失效1.正常)',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_date` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`constant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务常量表';

/*Data for the table `constant_data` */

/*Table structure for table `fund` */

DROP TABLE IF EXISTS `fund`;

CREATE TABLE `fund` (
  `id` int(50) NOT NULL AUTO_INCREMENT COMMENT '基金id',
  `code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '基金编码',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '基金名称',
  `cur_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '当前估算时间',
  `cur_net_value` double DEFAULT NULL COMMENT '当前估算净值',
  `cur_gain` double DEFAULT NULL COMMENT '当前涨幅',
  `last_net_value` double DEFAULT NULL COMMENT '上一日净值',
  `last_gain` double DEFAULT NULL COMMENT '上一日涨幅',
  `gain_total` decimal(10,2) DEFAULT NULL COMMENT '总涨幅',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `fund` */

insert  into `fund`(`id`,`code`,`name`,`cur_time`,`cur_net_value`,`cur_gain`,`last_net_value`,`last_gain`,`gain_total`) values (1,'005963','宝盈人工智能股票C(005963)','12-13 15:00',1.6679,1.4,1.6608,0.97,'2.37'),(4,'003956','南方教育股票(003956)','12-13 15:00',1.3866,0.94,1.382,0.6,'1.54'),(5,'110025','易方达资源行业混合(110025)','12-13 15:00',0.8045,0.43,0.805,0.5,'0.93'),(7,'003191','创金合信消费主题股票C(003191)','12-13 15:00',1.5688,0.88,1.5789,1.53,'2.41'),(9,'160222','国泰国证食品饮料行业指数分级(160222)','12-13 15:00',1.0909,1.48,1.0902,1.41,'2.89'),(11,'519727','交银成长30混合(前端：519727  后端：519728)','12-13 15:00',1.6053,1.53,1.626,2.85,'4.38'),(12,'163402','兴全趋势投资混合(LOF)(前端：163402  后端：163403)','12-13 15:00',0.7103,1.38,0.7084,1.1,'2.48'),(13,'070001','嘉实成长收益混合A(070001)','12-13 15:00',1.1746,1.23,1.172,1,'2.23'),(14,'003625','创金合信资源股票发起式C(003625)','12-13 15:00',0.9548,0.76,0.9567,0.96,'1.72'),(15,'070011','嘉实策略混合(070011)','12-13 15:00',1.128,1.71,1.137,2.52,'4.23'),(16,'003096','中欧医疗健康混合C(003096)','12-13 15:00',1.7559,1.73,1.764,2.2,'3.93'),(17,'001645','国泰大健康股票(001645)','12-13 15:00',2.0055,1.34,2.016,1.87,'3.21');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '用户id',
  `username` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `age` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `user` */

/*Table structure for table `user_fund` */

DROP TABLE IF EXISTS `user_fund`;

CREATE TABLE `user_fund` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '用户id',
  `fundId` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '基金id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `user_fund` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
