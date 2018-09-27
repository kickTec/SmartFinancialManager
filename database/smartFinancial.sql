/*
SQLyog Ultimate v11.13 (64 bit)
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `fund` */

insert  into `fund`(`id`,`code`,`name`,`cur_time`,`cur_net_value`,`cur_gain`,`last_net_value`,`last_gain`) values (1,'161121','易方达银行分级(161121)','18-09-27 14:06',0.9076,-0.23,0.9096,1.03),(4,'110030','易方达沪深300量化增强(110030)','18-09-27 14:11',2.084,-0.43,2.0929,0.89),(5,'110025','易方达资源行业混合(110025)','18-09-27 14:11',0.9146,0.18,0.913,0.55),(6,'110023','易方达医疗保健行业混合(110023)','18-09-27 14:06',1.5851,0.13,1.583,1.09),(7,'001856','易方达环保主题混合(001856)','18-09-27 14:06',0.9493,-0.29,0.952,0.85),(8,'110022','易方达消费行业(110022)','18-09-27 13:46',2.0988,-0.2,2.103,1.74),(9,'160222','国泰国证食品饮料行业指数分级(160222)','18-09-27 13:50',1.2607,-0.6,1.2683,2.9),(10,'163402','兴全趋势投资混合(LOF)(前端：163402  后端：163403)','18-09-27 14:06',0.6419,-1.02,0.6485,1),(11,'400032','东方主题精选混合(400032)','18-09-27 14:11',0.584,-0.29,0.5857,1),(12,'004683','建信高端医疗股票(004683)','18-09-27 14:01',0.9083,0.02,0.9081,1.82),(13,'005299','万家成长优选混合A(005299)','18-09-27 14:02',0.8564,-1.38,0.8684,0.54),(14,'000083','汇添富消费行业混合(000083)','18-09-27 14:01',3.1443,-0.69,3.166,1.87),(15,'070011','嘉实策略混合(070011)','18-09-27 14:06',0.917,-1.51,0.931,0.65);

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
