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
  `last_net_value` double DEFAULT NULL COMMENT '上一日净值',
  `last_gain` double DEFAULT NULL COMMENT '上一日涨幅',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `fund` */

insert  into `fund`(`id`,`code`,`name`,`cur_time`,`cur_net_value`,`last_net_value`,`last_gain`) values (1,'161121',NULL,NULL,NULL,NULL,NULL);

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
