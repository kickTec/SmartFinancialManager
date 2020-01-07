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
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `fund` */

insert  into `fund`(`id`,`code`,`name`,`cur_time`,`cur_net_value`,`cur_gain`,`last_net_value`,`last_gain`,`gain_total`) values (1,'005963','宝盈人工智能股票C(005963)','01-07 11:30',1.7439,0.05,1.743,0.55,'0.60'),(4,'003956','南方教育股票(003956)','01-07 11:30',1.4739,0.12,1.4722,0.88,'1.00'),(7,'003191','创金合信消费主题股票C(003191)','01-07 11:30',1.6378,2.1,1.6041,-0.51,'1.59'),(9,'160222','国泰国证食品饮料行业指数分级(160222)','01-07 11:30',1.0801,1.09,1.0684,-0.99,'0.10'),(12,'163402','兴全趋势投资混合(LOF)(前端：163402  后端：163403)','01-07 11:30',0.7556,0.28,0.7535,0.41,'0.69'),(13,'070001','嘉实成长收益混合A(070001)','01-07 11:30',1.1468,0.53,1.1408,-0.52,'0.01'),(14,'003625','创金合信资源股票发起式C(003625)','01-07 11:30',1.1191,-0.25,1.1219,2.32,'2.07');
insert  into `fund`(`id`,`code`,`name`,`cur_time`,`cur_net_value`,`cur_gain`,`last_net_value`,`last_gain`,`gain_total`) values (15,'070011','嘉实策略混合(070011)','01-07 11:30',1.2112,0.68,1.203,-0.25,'0.43'),(16,'003096','中欧医疗健康混合C(003096)','01-07 11:30',1.7097,0.63,1.699,-0.93,'-0.30'),(17,'001645','国泰大健康股票(001645)','01-07 11:30',2.2285,0.88,2.209,0.41,'1.29'),(18,'005496','创金合信科技成长股票C(005496)','01-07 11:30',1.3688,-0.09,1.3701,1.47,'1.38'),(19,'003299','嘉实物流产业股票C(003299)','01-07 11:30',1.4891,0.75,1.478,0,'0.75'),(51,'160136','南方中证国有企业改革指数分级(160136)','01-07 11:30',1.0221,0.2,1.0201,-0.48,'-0.28'),(52,'005928','创金合信新能源汽车股票C(005928)','01-07 11:30',1.1281,-0.24,1.1308,1.73,'1.49');
insert  into `fund`(`id`,`code`,`name`,`cur_time`,`cur_net_value`,`cur_gain`,`last_net_value`,`last_gain`,`gain_total`) values (53,'005240','银华文体娱乐量化股票发起式C(005240)','01-07 11:30',0.9035,0.55,0.8985,1.03,'1.58');

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
