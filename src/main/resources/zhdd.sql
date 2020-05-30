-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- Server version:               5.7.3-m13 - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL 版本:                  9.5.0.5320
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for zhdd
CREATE DATABASE IF NOT EXISTS `zhdd` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `zhdd`;

-- Dumping structure for table zhdd.sys_config
CREATE TABLE IF NOT EXISTS `sys_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `k` varchar(100) DEFAULT NULL COMMENT '键',
  `v` varchar(1000) DEFAULT NULL COMMENT '值',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `kv_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- Dumping data for table zhdd.sys_config: ~25 rows (approximately)
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` (`id`, `k`, `v`, `remark`, `create_time`, `kv_type`) VALUES
	(2, 'oss_qiniu', '{"AccessKey" : "8-HMj9EgGNIP-xuOCpSzTn-OMyGOFtR3TxLdn4Uu","SecretKey" : "SjpGg3V6PsMdJgn42PeEd5Ik-6aNyuwdqV5CPM6A","bucket" : "ifast","AccessUrl" : "http://p6r7ke2jc.bkt.clouddn.com/"}', '七牛对象存储配置', '2018-04-06 14:31:26', 4300),
	(3, 'author', 'admin', '代码生成器配置', '2018-05-27 19:57:04', 4401),
	(4, 'email', 'anonymous@anonymous.com', '代码生成器配置', '2018-05-27 19:57:04', 4401),
	(5, 'package', 'com.pjb.springbootjwt.zhddkk', '代码生成器配置', '2018-05-27 19:57:04', 4401),
	(6, 'autoRemovePre', 'true', '代码生成器配置', '2018-05-27 19:57:04', 4401),
	(7, 'tablePrefix', 't_', '代码生成器配置', '2018-05-27 19:57:04', 4401),
	(8, 'tinyint', 'Integer', '代码生成器配置', '2018-05-27 19:57:04', 4400),
	(9, 'smallint', 'Integer', '代码生成器配置', '2018-05-27 19:57:04', 4400),
	(10, 'mediumint', 'Integer', '代码生成器配置', '2018-05-27 19:57:04', 4400),
	(11, 'int', 'Integer', '代码生成器配置', '2018-05-27 19:57:04', 4400),
	(12, 'integer', 'Integer', '代码生成器配置', '2018-05-27 19:57:04', 4400),
	(13, 'bigint', 'Long', '代码生成器配置', '2018-05-27 19:57:04', 4400),
	(14, 'float', 'Float', '代码生成器配置', '2018-05-27 19:57:04', 4400),
	(15, 'double', 'Double', '代码生成器配置', '2018-05-27 19:57:04', 4400),
	(16, 'decimal', 'BigDecimal', '代码生成器配置', '2018-05-27 19:57:04', 4400),
	(17, 'bit', 'Boolean', '代码生成器配置', '2018-05-27 19:57:04', 4400),
	(18, 'char', 'String', '代码生成器配置', '2018-05-27 19:57:04', 4400),
	(19, 'varchar', 'String', '代码生成器配置', '2018-05-27 19:57:04', 4400),
	(20, 'tinytext', 'String', '代码生成器配置', '2018-05-27 19:57:04', 4400),
	(21, 'text', 'String', '代码生成器配置', '2018-05-27 19:57:04', 4400),
	(22, 'mediumtext', 'String', '代码生成器配置', '2018-05-27 19:57:04', 4400),
	(23, 'longtext', 'String', '代码生成器配置', '2018-05-27 19:57:04', 4400),
	(24, 'date', 'Date', '代码生成器配置', '2018-05-27 19:57:04', 4400),
	(25, 'datetime', 'Date', '代码生成器配置', '2018-05-27 19:57:04', 4400),
	(26, 'timestamp', 'Date', '代码生成器配置', '2018-05-27 19:57:04', 4400);
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;

-- Dumping structure for table zhdd.t_permission
CREATE TABLE IF NOT EXISTS `t_permission` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(50) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table zhdd.t_permission: ~2 rows (approximately)
/*!40000 ALTER TABLE `t_permission` DISABLE KEYS */;
INSERT INTO `t_permission` (`id`, `permission_name`, `create_time`) VALUES
	(1, 'add', '2020-03-12 15:41:48'),
	(2, 'delete', '2020-03-12 16:00:10');
/*!40000 ALTER TABLE `t_permission` ENABLE KEYS */;

-- Dumping structure for table zhdd.t_role
CREATE TABLE IF NOT EXISTS `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table zhdd.t_role: ~2 rows (approximately)
/*!40000 ALTER TABLE `t_role` DISABLE KEYS */;
INSERT INTO `t_role` (`id`, `rolename`, `create_time`) VALUES
	(1, 'admin', '2020-03-12 14:54:56'),
	(2, 'user', '2020-03-12 14:55:01');
/*!40000 ALTER TABLE `t_role` ENABLE KEYS */;

-- Dumping structure for table zhdd.t_role_permission
CREATE TABLE IF NOT EXISTS `t_role_permission` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(10) unsigned NOT NULL,
  `role_name` varchar(200) NOT NULL DEFAULT '',
  `permission_id` int(10) unsigned NOT NULL,
  `permission_name` varchar(200) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table zhdd.t_role_permission: ~0 rows (approximately)
/*!40000 ALTER TABLE `t_role_permission` DISABLE KEYS */;
INSERT INTO `t_role_permission` (`id`, `role_id`, `role_name`, `permission_id`, `permission_name`) VALUES
	(1, 1, 'admin', 1, 'add');
/*!40000 ALTER TABLE `t_role_permission` ENABLE KEYS */;

-- Dumping structure for table zhdd.t_user
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` int(45) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- Dumping data for table zhdd.t_user: ~3 rows (approximately)
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` (`id`, `username`, `password`, `create_time`, `update_time`) VALUES
	(1, '张三', '123456', '2020-03-12 13:46:32', '2020-03-12 13:46:32'),
	(2, '李四', '123456', '2020-03-12 13:46:32', '2020-03-12 13:46:32'),
	(3, '王五', '123456', '2020-03-12 13:46:32', '2020-03-12 13:46:32');
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;

-- Dumping structure for table zhdd.t_user_role
CREATE TABLE IF NOT EXISTS `t_user_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `username` varchar(50) NOT NULL DEFAULT '',
  `role_id` int(10) unsigned NOT NULL,
  `role_name` varchar(50) NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table zhdd.t_user_role: ~2 rows (approximately)
/*!40000 ALTER TABLE `t_user_role` DISABLE KEYS */;
INSERT INTO `t_user_role` (`id`, `user_id`, `username`, `role_id`, `role_name`, `create_time`) VALUES
	(1, 1, '张三', 1, 'admin', '2020-03-12 14:55:34'),
	(2, 1, '张三', 2, 'user', '2020-03-12 14:55:53');
/*!40000 ALTER TABLE `t_user_role` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_ads
CREATE TABLE IF NOT EXISTS `ws_ads` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) NOT NULL DEFAULT '0' COMMENT '标题',
  `content` varchar(500) NOT NULL DEFAULT '0' COMMENT '内容',
  `receive_list` varchar(1000) NOT NULL DEFAULT '0' COMMENT '接收人列表',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='广告表';

-- Dumping data for table zhdd.ws_ads: ~0 rows (approximately)
/*!40000 ALTER TABLE `ws_ads` DISABLE KEYS */;
/*!40000 ALTER TABLE `ws_ads` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_chatlog
CREATE TABLE IF NOT EXISTS `ws_chatlog` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `time` varchar(50) DEFAULT NULL COMMENT '时间',
  `user` varchar(50) DEFAULT '' COMMENT '发起人',
  `to_user` varchar(50) DEFAULT '' COMMENT '被聊人',
  `msg` varchar(4000) DEFAULT '' COMMENT '消息内容',
  `remark` varchar(400) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='聊天记录表';

-- Dumping data for table zhdd.ws_chatlog: ~0 rows (approximately)
/*!40000 ALTER TABLE `ws_chatlog` DISABLE KEYS */;
/*!40000 ALTER TABLE `ws_chatlog` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_circle
CREATE TABLE IF NOT EXISTS `ws_circle` (
  `id` int(255) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(30) NOT NULL COMMENT '用户名',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `content` varchar(1000) NOT NULL COMMENT '内容',
  `like_num` int(11) DEFAULT '0' COMMENT '点赞数',
  `pic1` varchar(255) DEFAULT NULL COMMENT '图片1',
  `pic2` varchar(255) DEFAULT NULL COMMENT '图片2',
  `pic3` varchar(255) DEFAULT NULL COMMENT '图片3',
  `pic4` varchar(255) DEFAULT NULL COMMENT '图片4',
  `pic5` varchar(255) DEFAULT NULL COMMENT '图片5',
  `pic6` varchar(255) DEFAULT NULL COMMENT '图片6',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发表时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- Dumping data for table zhdd.ws_circle: ~4 rows (approximately)
/*!40000 ALTER TABLE `ws_circle` DISABLE KEYS */;
INSERT INTO `ws_circle` (`id`, `user_name`, `user_id`, `content`, `like_num`, `pic1`, `pic2`, `pic3`, `pic4`, `pic5`, `pic6`, `create_time`) VALUES
	(12, 'aa', 23, '高富帅的广告ff', 8, 'http://localhost:8101/circle\\admin.jpg', '', '', 'http://localhost:8101/circle\\851747d5-4292-44f0-a7a8-f748bb275561.jpg', NULL, NULL, '2020-05-11 12:04:45'),
	(13, 'aa', 23, '房贷发放方式', 0, 'http://localhost:8101/circle\\微信图片_20181225175811.jpg', NULL, 'http://localhost:8101/circle\\linux-back.png', NULL, NULL, NULL, '2020-05-11 12:27:01'),
	(15, 'bb', 24, '西八老妈', 0, 'http://localhost:8101/circle\\微信图片_20181225175811.jpg', NULL, 'http://localhost:8101/circle\\微信图片_20181225180500.jpg', NULL, NULL, NULL, '2020-05-12 11:06:07'),
	(16, 'aa', 23, '今天星期二  有点热', 1, 'http://localhost:8101/circle\\5bd994e3-8aa3-40e6-abec-0a65b36489b4.png', NULL, 'http://localhost:8101/circle\\微信图片_20181225175814.jpg', NULL, NULL, NULL, '2020-05-19 08:53:55');
/*!40000 ALTER TABLE `ws_circle` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_circle_comment
CREATE TABLE IF NOT EXISTS `ws_circle_comment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `circle_id` int(11) DEFAULT NULL COMMENT '朋友圈消息id',
  `user_id` int(11) DEFAULT NULL COMMENT '评论人id',
  `user_name` varchar(255) DEFAULT NULL COMMENT '评论人姓名',
  `comment` varchar(1000) DEFAULT '' COMMENT '评论内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- Dumping data for table zhdd.ws_circle_comment: ~5 rows (approximately)
/*!40000 ALTER TABLE `ws_circle_comment` DISABLE KEYS */;
INSERT INTO `ws_circle_comment` (`id`, `circle_id`, `user_id`, `user_name`, `comment`, `create_time`) VALUES
	(2, 1, 30, 'hch', '484841515', '2019-08-30 14:25:56'),
	(3, 2, 18, '黄朝辉', '21321', '2020-01-06 14:55:56'),
	(4, 3, 1, 'admin', '给对方给对方', '2020-05-09 11:11:09'),
	(7, 12, 23, 'aa', '广东省根', '2020-05-11 12:31:25'),
	(9, 12, 24, 'bb', '范德萨发放', '2020-05-12 11:22:47'),
	(10, 16, 32, 'ymzymz', '哦哦哦哦哦', '2020-05-20 16:51:05');
/*!40000 ALTER TABLE `ws_circle_comment` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_common
CREATE TABLE IF NOT EXISTS `ws_common` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(50) DEFAULT NULL,
  `name` varchar(300) DEFAULT NULL,
  `orderby` int(10) unsigned DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  KEY `Index 1` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8;

-- Dumping data for table zhdd.ws_common: ~39 rows (approximately)
/*!40000 ALTER TABLE `ws_common` DISABLE KEYS */;
INSERT INTO `ws_common` (`id`, `type`, `name`, `orderby`, `remark`) VALUES
	(5, 'zh', '操蛋', 26, NULL),
	(6, 'zh', '滚一边去', 27, NULL),
	(7, 'cyy', '你好', 28, NULL),
	(8, 'zcwt', '你最喜欢的水果是什么?', 29, NULL),
	(48, 'cyy', '晚安', 30, NULL),
	(47, 'cyy', '拜拜', 31, NULL),
	(46, 'cyy', '现在忙，一会联系你', 32, NULL),
	(45, 'cyy', '晚上见', 33, NULL),
	(44, 'cyy', '在吗？', 34, NULL),
	(43, 'cyy', '打扰一下,请教一个问题', 35, NULL),
	(42, 'zcwt', '你父亲生日是什么时候?', 36, NULL),
	(41, 'zcwt', '你最想去哪旅行?', 37, NULL),
	(40, 'zcwt', '你的月薪多少？', 38, NULL),
	(39, 'zcwt', '你的初中在哪里？', 39, NULL),
	(37, 'cyy', 'nice to meet you!!!', 41, NULL),
	(36, 'zh', 'MB', 42, NULL),
	(35, 'zh', 'CNMB', 43, NULL),
	(34, 'zh', 'sb', 45, NULL),
	(33, 'zh', 'SB', 46, NULL),
	(32, 'zh', '傻逼', 47, NULL),
	(31, 'zh', '煞笔', 48, NULL),
	(30, 'zh', 'SX', 49, NULL),
	(29, 'mgc', '江泽民', 50, NULL),
	(28, 'zh', '我日', 52, NULL),
	(27, 'mgc', '文化革命', 53, NULL),
	(26, 'mgc', '金三胖', 54, NULL),
	(25, 'mgc', '王岐山', 56, NULL),
	(24, 'mgc', '薄熙来', 57, NULL),
	(23, 'mgc', '王立军', 58, NULL),
	(22, 'mgc', '反政府', 61, NULL),
	(21, 'cyy', '本人现在不在，有事请留言', 63, NULL),
	(20, 'cyy', '早上好', 64, NULL),
	(19, 'zh', '他妈逼', 66, NULL),
	(17, 'zh', '操', 68, NULL),
	(14, 'zcwt', '你最讨厌什么样的人?', 73, NULL),
	(13, 'zcwt', '你怎么看待中国的房价?', 74, NULL),
	(12, 'cyy', '搞活经济', 76, NULL),
	(82, 'cyy', 'anninghaseiyo', NULL, NULL),
	(84, 'zh', '西八龙码', 1, '等丰富的非');
/*!40000 ALTER TABLE `ws_common` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_dic
CREATE TABLE IF NOT EXISTS `ws_dic` (
  `type` varchar(50) NOT NULL,
  `key` varchar(50) NOT NULL,
  `value` varchar(50) NOT NULL,
  `sort` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `remark` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`type`,`key`),
  KEY `KEY` (`sort`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COMMENT='字典表';

-- Dumping data for table zhdd.ws_dic: ~26 rows (approximately)
/*!40000 ALTER TABLE `ws_dic` DISABLE KEYS */;
INSERT INTO `ws_dic` (`type`, `key`, `value`, `sort`, `remark`) VALUES
	('cyy', '1', '你好', 20, NULL),
	('cyy', '10', '很疑惑', 22, NULL),
	('cyy', '11', '你忙吧', 23, NULL),
	('cyy', '12', '天气不好', 24, NULL),
	('cyy', '13', '好慢', 25, NULL),
	('cyy', '14', 'fuck', 26, NULL),
	('cyy', '15', '你在哪', 27, NULL),
	('cyy', '16', '去哪吃饭', 28, NULL),
	('cyy', '2', '我现在忙,一会儿联系你', 17, NULL),
	('cyy', '3', '待会儿见', 16, NULL),
	('cyy', '4', '拜拜', 15, NULL),
	('cyy', '5', '不好意思,刚不在电脑旁', 14, NULL),
	('cyy', '6', '和你聊天很愉快', 13, NULL),
	('cyy', '7', '挂了', 12, NULL),
	('cyy', '8', '我不在', 19, NULL),
	('cyy', '9', '得得得', 21, NULL),
	('enable', '0', '禁用', 11, NULL),
	('enable', '1', '可用', 10, NULL),
	('question', '1', '你最喜欢的明星是谁?', 9, NULL),
	('question', '2', '你最喜欢的一首歌是?', 8, NULL),
	('question', '3', '你的初恋女友是谁?', 7, NULL),
	('question', '4', '你父亲生日是什么时候?', 6, NULL),
	('question', '5', '你母亲生日是什么时候?', 4, NULL),
	('question', '6', '你故乡在哪里?', 3, NULL),
	('question', '7', '你的生日是什么时候?', 5, NULL),
	('state', '0', '离线', 2, NULL),
	('state', '1', '在线', 1, NULL);
/*!40000 ALTER TABLE `ws_dic` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_file
CREATE TABLE IF NOT EXISTS `ws_file` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `folder` varchar(50) NOT NULL DEFAULT '' COMMENT '文件类型',
  `filename` varchar(50) NOT NULL DEFAULT '' COMMENT '文件名',
  `disk_path` varchar(100) NOT NULL DEFAULT '' COMMENT '存储磁盘目录',
  `url` varchar(100) NOT NULL DEFAULT '' COMMENT 'url',
  `file_size` int(11) NOT NULL DEFAULT '0' COMMENT '文件大小',
  `author` varchar(50) NOT NULL DEFAULT '' COMMENT '未知',
  `track_length` varchar(50) NOT NULL DEFAULT '' COMMENT '文件时长',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0:无效 1:有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8 COMMENT='文件表';

-- Dumping data for table zhdd.ws_file: ~47 rows (approximately)
/*!40000 ALTER TABLE `ws_file` DISABLE KEYS */;
INSERT INTO `ws_file` (`id`, `user`, `folder`, `filename`, `disk_path`, `url`, `file_size`, `author`, `track_length`, `create_time`, `update_time`, `status`) VALUES
	(72, 'aa', 'music', 'As One (애즈 원) - 헤어져 (分手).mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\As One (애즈 원) - 헤어져 (分手).mp3', 4128717, '', '04:17', '2020-05-11 09:29:09', '2020-05-11 12:00:36', 1),
	(73, 'aa', 'music', 'BbAhn - 발걸음.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\BbAhn - 발걸음.mp3', 8425866, '', '03:27', '2020-05-11 09:29:09', '2020-05-11 12:00:35', 1),
	(74, 'aa', 'music', 'Ben (벤) - 끝까지 (到最后).mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\Ben (벤) - 끝까지 (到最后).mp3', 4527563, '', '04:38', '2020-05-11 09:29:09', '2020-05-11 12:00:34', 1),
	(75, 'aa', 'music', 'Ben (벤) - 오늘은 가지마 (今天不要走).mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\Ben (벤) - 오늘은 가지마 (今天不要走).mp3', 3915698, '', '04:00', '2020-05-11 09:29:09', '2020-05-11 12:00:33', 1),
	(91, 'aa', 'music', '郑日英 - 기도.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\郑日英 - 기도.mp3', 4882504, '', '04:42', '2020-05-11 11:55:24', '2020-05-11 11:55:24', 1),
	(93, 'aa', 'circle', 'b5723e24-9607-422f-828f-62b18e7fdc48.jpg', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\b5723e24-9607-422f-828f-62b18e7fdc48.jpg', 169455, '', '', '2020-05-11 12:00:16', '2020-05-11 12:00:16', 1),
	(94, 'aa', 'circle', '4656.jpg', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\4656.jpg', 68874, '', '', '2020-05-11 12:01:57', '2020-05-11 12:01:57', 1),
	(95, 'aa', 'circle', 'b5723e24-9607-422f-828f-62b18e7fdc48.jpg', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\b5723e24-9607-422f-828f-62b18e7fdc48.jpg', 169455, '', '', '2020-05-11 12:01:59', '2020-05-11 12:01:59', 1),
	(96, 'aa', 'circle', 'admin.jpg', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\admin.jpg', 43141, '', '', '2020-05-11 12:04:39', '2020-05-11 12:04:39', 1),
	(97, 'aa', 'circle', '851747d5-4292-44f0-a7a8-f748bb275561.jpg', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\851747d5-4292-44f0-a7a8-f748bb275561.jpg', 259766, '', '', '2020-05-11 12:04:43', '2020-05-11 12:04:43', 1),
	(98, 'aa', 'circle', '微信图片_20181225175811.jpg', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\微信图片_20181225175811.jpg', 551254, '', '', '2020-05-11 12:26:54', '2020-05-11 12:26:54', 1),
	(99, 'aa', 'circle', 'linux-back.png', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\linux-back.png', 95825, '', '', '2020-05-11 12:26:57', '2020-05-11 12:26:57', 1),
	(100, 'aa', 'circle', 'admin.jpg', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\admin.jpg', 43141, '', '', '2020-05-11 13:35:22', '2020-05-11 13:35:22', 1),
	(101, 'aa', 'circle', '4567 - 副本.jpg', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\4567 - 副本.jpg', 81744, '', '', '2020-05-11 13:36:15', '2020-05-11 13:36:15', 1),
	(102, 'aa', 'circle', '微信图片_20181225174944.jpg', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\微信图片_20181225174944.jpg', 105498, '', '', '2020-05-11 13:36:39', '2020-05-11 13:36:39', 1),
	(103, 'aa', 'circle', '4353524.jpg', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\4353524.jpg', 28303, '', '', '2020-05-11 13:38:51', '2020-05-11 13:38:51', 1),
	(104, 'aa', 'circle', '微信图片_20181225175948.jpg', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\微信图片_20181225175948.jpg', 486901, '', '', '2020-05-11 13:38:54', '2020-05-11 13:38:54', 1),
	(105, 'aa', 'headImg', '3123.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\3123.jpg', 247831, '', '', '2020-05-11 14:06:10', '2020-05-11 14:06:10', 1),
	(106, 'aa', 'headImg', '5658767.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\5658767.jpg', 31429, '', '', '2020-05-11 14:08:15', '2020-05-11 14:08:15', 1),
	(107, 'aa', 'headImg', 'b5723e24-9607-422f-828f-62b18e7fdc48.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\b5723e24-9607-422f-828f-62b18e7fdc48.jpg', 169455, '', '', '2020-05-11 14:49:17', '2020-05-11 14:49:17', 1),
	(108, 'aa', 'headImg', 'timg.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\timg.jpg', 388505, '', '', '2020-05-11 14:55:25', '2020-05-11 14:55:25', 1),
	(109, 'admin', 'circle', 'admin.jpg', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\admin.jpg', 43141, '', '', '2020-05-12 10:09:28', '2020-05-12 10:09:28', 1),
	(110, 'bb', 'headImg', 'c5d2978e-63b5-4003-848b-53b144f5e5c1.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\c5d2978e-63b5-4003-848b-53b144f5e5c1.jpg', 53297, '', '', '2020-05-12 11:05:47', '2020-05-12 11:05:47', 1),
	(111, 'bb', 'circle', '微信图片_20181225175811.jpg', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\微信图片_20181225175811.jpg', 551254, '', '', '2020-05-12 11:06:02', '2020-05-12 11:06:02', 1),
	(112, 'bb', 'circle', '微信图片_20181225180500.jpg', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\微信图片_20181225180500.jpg', 378294, '', '', '2020-05-12 11:06:05', '2020-05-12 11:06:05', 1),
	(113, 'bb', 'headImg', 'c5d2978e-63b5-4003-848b-53b144f5e5c1.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\c5d2978e-63b5-4003-848b-53b144f5e5c1.jpg', 53297, '', '', '2020-05-12 11:06:28', '2020-05-12 11:06:28', 1),
	(114, 'bb', 'headImg', 'c5d2978e-63b5-4003-848b-53b144f5e5c1.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\c5d2978e-63b5-4003-848b-53b144f5e5c1.jpg', 53297, '', '', '2020-05-12 11:08:30', '2020-05-12 11:08:30', 1),
	(115, 'bb', 'headImg', 'c5d2978e-63b5-4003-848b-53b144f5e5c1.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\c5d2978e-63b5-4003-848b-53b144f5e5c1.jpg', 53297, '', '', '2020-05-12 11:14:22', '2020-05-12 11:14:22', 1),
	(116, 'bb', 'music', 'MC 梦 - 죽을 만큼 아파서.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\MC 梦 - 죽을 만큼 아파서.mp3', 6630055, '', '04:29', '2020-05-12 11:22:22', '2020-05-12 11:22:22', 1),
	(117, 'hch', 'headImg', '5ce13ad4-7f12-4a56-8d02-e18e6f933b92.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\5ce13ad4-7f12-4a56-8d02-e18e6f933b92.jpg', 188751, '', '', '2020-05-12 14:42:23', '2020-05-12 14:42:23', 1),
	(118, 'bb', 'music', 'The Minions - Another Irish Drinking Song.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\The Minions - Another Irish Drinking Song.mp3', 2715079, '', '00:39', '2020-05-12 16:17:16', '2020-05-12 16:17:16', 1),
	(119, 'aa', 'headImg', '3bf9def0-fdee-46c6-8ccc-45cb82654347.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\3bf9def0-fdee-46c6-8ccc-45cb82654347.jpg', 172237, '', '', '2020-05-12 16:39:55', '2020-05-12 16:39:55', 1),
	(120, 'bb', 'headImg', '89EDE5CF-9CAD-4364-A306-C4F025E2954C.jpeg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\89EDE5CF-9CAD-4364-A306-C4F025E2954C.jpeg', 840160, '', '', '2020-05-12 16:42:30', '2020-05-12 16:42:30', 1),
	(121, 'bb', 'headImg', 'BAD32177-97A6-4AA6-942C-CA7C24F147FD.jpeg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\BAD32177-97A6-4AA6-942C-CA7C24F147FD.jpeg', 840160, '', '', '2020-05-12 16:42:46', '2020-05-12 16:42:46', 1),
	(122, 'bb', 'headImg', 'D41574B2-569D-437F-A1F1-58FC68C14EAE.jpeg', 'D:\\\\temp\\\\headImg', 'http://192.168.31.106:8101/headImg\\D41574B2-569D-437F-A1F1-58FC68C14EAE.jpeg', 840160, '', '', '2020-05-12 16:45:49', '2020-05-12 16:45:49', 1),
	(123, 'aa', 'music', 'T-ara - DAY BY DAY.mp3', 'D:\\\\temp\\\\music', 'http://192.168.31.106:8101/music\\T-ara - DAY BY DAY.mp3', 8416096, '', '03:28', '2020-05-12 16:53:48', '2020-05-12 16:53:48', 1),
	(124, 'aa', 'headImg', '5455.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\5455.jpg', 13139, '', '', '2020-05-14 10:23:16', '2020-05-14 10:23:16', 1),
	(125, '', 'headImg', '4325.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\4325.jpg', 88148, '', '', '2020-05-14 13:19:08', '2020-05-14 13:19:08', 1),
	(126, '', 'headImg', '4567 - 副本.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\4567 - 副本.jpg', 81744, '', '', '2020-05-14 13:23:27', '2020-05-14 13:23:27', 1),
	(127, '', 'headImg', 'player.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\player.jpg', 63244, '', '', '2020-05-14 13:28:10', '2020-05-14 13:28:10', 1),
	(128, '', 'headImg', 'player.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\player.jpg', 63244, '', '', '2020-05-14 13:28:53', '2020-05-14 13:28:53', 1),
	(129, '', 'headImg', 'player.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\player.jpg', 63244, '', '', '2020-05-14 13:29:12', '2020-05-14 13:29:12', 1),
	(130, 'zhuhao', 'headImg', '5bd994e3-8aa3-40e6-abec-0a65b36489b4.png', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\5bd994e3-8aa3-40e6-abec-0a65b36489b4.png', 457492, '', '', '2020-05-15 15:12:45', '2020-05-15 15:12:45', 1),
	(131, 'aa', 'circle', '5bd994e3-8aa3-40e6-abec-0a65b36489b4.png', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\5bd994e3-8aa3-40e6-abec-0a65b36489b4.png', 457492, '', '', '2020-05-19 08:53:46', '2020-05-19 08:53:46', 1),
	(132, 'aa', 'circle', '微信图片_20181225175814.jpg', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\微信图片_20181225175814.jpg', 545317, '', '', '2020-05-19 08:53:53', '2020-05-19 08:53:53', 1),
	(133, '黄朝辉', 'headImg', '5.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg/5.jpg', 35868, '', '', '2020-05-20 10:05:29', '2020-05-20 10:05:29', 1),
	(134, 'admin', 'music', '昭宥 (소유) _ BrotherSu (브라더수) - 모르나봐 (你不懂).mp3', 'D:\\\\temp\\\\music', 'http://192.168.31.106:8101/music/昭宥 (소유) _ BrotherSu (브라더수) - 모르나봐 (你不懂).mp3', 3289736, '', '03:20', '2020-05-20 10:08:57', '2020-05-20 12:18:24', 1),
	(135, '', 'headImg', 'fj2.jpg', 'D:\\\\temp\\\\headImg', 'http://192.168.31.106:8101/headImg/fj2.jpg', 388552, '', '', '2020-05-21 15:06:14', '2020-05-21 15:06:14', 1);
/*!40000 ALTER TABLE `ws_file` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_friends
CREATE TABLE IF NOT EXISTS `ws_friends` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
  `uname` varchar(50) NOT NULL DEFAULT '0' COMMENT '用户姓名',
  `fid` int(11) NOT NULL DEFAULT '0' COMMENT '好友id',
  `fname` varchar(50) NOT NULL DEFAULT '0' COMMENT '好友姓名',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COMMENT='好友列表';

-- Dumping data for table zhdd.ws_friends: ~8 rows (approximately)
/*!40000 ALTER TABLE `ws_friends` DISABLE KEYS */;
INSERT INTO `ws_friends` (`id`, `uid`, `uname`, `fid`, `fname`, `create_time`, `remark`) VALUES
	(21, 23, 'aa', 24, 'bb', '2020-05-18 15:19:02', ''),
	(22, 24, 'bb', 23, 'aa', '2020-05-18 15:19:02', ''),
	(23, 23, 'aa', 25, 'vb', '2020-05-19 10:35:14', ''),
	(24, 25, 'vb', 23, 'aa', '2020-05-19 10:35:14', ''),
	(25, 14, '无名5', 23, 'aa', '2020-05-19 11:03:03', ''),
	(26, 23, 'aa', 14, '无名5', '2020-05-19 11:03:03', ''),
	(27, 11, '无名1', 23, 'aa', '2020-05-19 11:10:02', ''),
	(28, 23, 'aa', 11, '无名1', '2020-05-19 11:10:02', '');
/*!40000 ALTER TABLE `ws_friends` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_friends_apply
CREATE TABLE IF NOT EXISTS `ws_friends_apply` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `from_id` int(10) unsigned NOT NULL COMMENT '申请人id',
  `from_name` varchar(50) NOT NULL DEFAULT '0' COMMENT '申请人姓名',
  `to_id` int(10) unsigned NOT NULL COMMENT '好友id',
  `to_name` varchar(50) NOT NULL DEFAULT '0' COMMENT '好友姓名',
  `process_status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '申请状态 1:申请中 2:被拒绝 3:申请成功',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=168 DEFAULT CHARSET=utf8 COMMENT='好友申请表';

-- Dumping data for table zhdd.ws_friends_apply: ~21 rows (approximately)
/*!40000 ALTER TABLE `ws_friends_apply` DISABLE KEYS */;
INSERT INTO `ws_friends_apply` (`id`, `from_id`, `from_name`, `to_id`, `to_name`, `process_status`, `create_time`) VALUES
	(144, 23, 'aa', 24, 'bb', 3, '2020-05-18 15:12:34'),
	(145, 23, 'aa', 25, 'vb', 2, '2020-05-18 15:21:54'),
	(146, 23, 'aa', 35, 'ee', 1, '2020-05-18 15:25:56'),
	(147, 23, 'aa', 1, 'admin', 1, '2020-05-18 15:32:02'),
	(148, 23, 'aa', 25, 'vb', 3, '2020-05-18 15:34:15'),
	(149, 23, 'aa', 2, 'bbbb', 1, '2020-05-19 10:26:47'),
	(150, 23, 'aa', 3, 'CNMgcd', 1, '2020-05-19 10:26:48'),
	(151, 23, 'aa', 4, 'mb', 1, '2020-05-19 10:26:48'),
	(152, 23, 'aa', 5, '小七', 1, '2020-05-19 10:26:49'),
	(153, 23, 'aa', 6, '小三', 1, '2020-05-19 10:26:50'),
	(154, 23, 'aa', 7, '小五', 1, '2020-05-19 10:26:51'),
	(155, 24, 'bb', 1, 'admin', 1, '2020-05-19 10:28:17'),
	(156, 24, 'bb', 2, 'bbbb', 1, '2020-05-19 10:28:17'),
	(157, 24, 'bb', 3, 'CNMgcd', 1, '2020-05-19 10:28:17'),
	(158, 23, 'aa', 8, '小六', 1, '2020-05-19 10:28:35'),
	(159, 23, 'aa', 9, '小四', 1, '2020-05-19 10:28:36'),
	(160, 23, 'aa', 10, '小散', 1, '2020-05-19 10:28:36'),
	(164, 14, '无名5', 23, 'aa', 3, '2020-05-19 10:36:37'),
	(165, 11, '无名1', 23, 'aa', 2, '2020-05-19 11:08:23'),
	(166, 11, '无名1', 23, 'aa', 2, '2020-05-19 11:08:59'),
	(167, 11, '无名1', 23, 'aa', 3, '2020-05-19 11:09:57');
/*!40000 ALTER TABLE `ws_friends_apply` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_operation_log
CREATE TABLE IF NOT EXISTS `ws_operation_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) unsigned DEFAULT '0' COMMENT '操作人ID',
  `user_name` varchar(50) DEFAULT '' COMMENT '操作人姓名',
  `oper_type` varchar(50) NOT NULL DEFAULT '' COMMENT '操作类型(增|删|改)',
  `oper_module` varchar(50) DEFAULT '' COMMENT '操作模块',
  `oper_submodule` varchar(50) DEFAULT '' COMMENT '操作子模块',
  `oper_describe` varchar(500) NOT NULL DEFAULT '' COMMENT '操作描述',
  `oper_remark` varchar(500) NOT NULL DEFAULT '' COMMENT '操作描述',
  `request_url` varchar(255) NOT NULL DEFAULT '' COMMENT '请求路径',
  `cost_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '耗时(毫秒)',
  `class_name` varchar(200) NOT NULL DEFAULT '' COMMENT '类名',
  `method_name` varchar(100) NOT NULL DEFAULT '' COMMENT '方法名',
  `parameters` varchar(500) DEFAULT '' COMMENT '参数列表',
  `oper_result` text COMMENT '操作结果',
  `error_msg` varchar(800) DEFAULT '' COMMENT '错误信息',
  `access_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作日志,主要记录增删改的日志';

-- Dumping data for table zhdd.ws_operation_log: ~0 rows (approximately)
/*!40000 ALTER TABLE `ws_operation_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `ws_operation_log` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_users
CREATE TABLE IF NOT EXISTS `ws_users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `register_time` varchar(50) DEFAULT NULL COMMENT '注册时间',
  `state` varchar(50) DEFAULT '1' COMMENT '是否在线 0:离线 1:在线',
  `last_login_time` varchar(50) DEFAULT NULL COMMENT '上次登录时间',
  `last_logout_time` varchar(50) DEFAULT NULL COMMENT '上次登出时间',
  `enable` varchar(50) DEFAULT '1' COMMENT '是否可用 0:不可用  1:可用',
  `speak` varchar(50) DEFAULT '1' COMMENT '是否禁言  0:禁言 1：没有禁言',
  `question1` varchar(400) DEFAULT NULL COMMENT '问题1',
  `answer1` varchar(400) DEFAULT NULL COMMENT '答案1',
  `question2` varchar(400) DEFAULT NULL COMMENT '问题2',
  `answer2` varchar(400) DEFAULT NULL COMMENT '答案2',
  `question3` varchar(400) DEFAULT NULL COMMENT '问题3',
  `answer3` varchar(400) DEFAULT NULL COMMENT '答案3',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COMMENT='用户账号表';

-- Dumping data for table zhdd.ws_users: ~33 rows (approximately)
/*!40000 ALTER TABLE `ws_users` DISABLE KEYS */;
INSERT INTO `ws_users` (`id`, `name`, `password`, `register_time`, `state`, `last_login_time`, `last_logout_time`, `enable`, `speak`, `question1`, `answer1`, `question2`, `answer2`, `question3`, `answer3`, `create_time`) VALUES
	(1, 'admin', 'MjQzMTBCNkQxNkM1NkZCNzk2M0ZCNEY1REMxQkM0NDk=', '2018-03-22 22:37:07', '0', '2020-05-21 16:25:50', '2020-05-21 16:15:24', '1', '1', '你最喜欢的明星是谁?', '11', '你最喜欢的一首歌是什么?', '22', '你父亲的生日是什么时候', '33', '2018-12-12 10:10:15'),
	(2, 'bbbb', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-23 14:51:50', '0', '2018-03-25 16:47:14', '2018-03-25 15:50:22', '1', '1', '你最喜欢的明星是谁?', 'vv', '你最喜欢的一首歌是什么?', 'rrr', '你父亲的生日是什么时候', 'nn', '2018-12-12 10:10:15'),
	(3, 'CNMgcd', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-04-01 18:12:40', '0', '2018-04-01 18:15:15', '2018-04-01 18:14:03', '1', '1', '你怎么看待中国的房价?', '奇葩', '你玩了几个女人?', '0个', '为什么那么多中国人都很麻木？', '因为都被洗脑了', '2018-12-12 10:10:15'),
	(4, 'mb', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-31 21:55:17', '0', '2018-12-12 16:07:07', '2018-12-12 16:07:08', '1', '1', '你最喜欢的水果是什么?', 'apple', '你的初中在哪里？', '湖北', '你的第一次是什么时候？', '16', '2018-12-12 10:10:15'),
	(5, '小七', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-24 11:03:07', '0', '2018-03-31 23:35:22', '2018-03-31 23:38:03', '1', '1', '你最喜欢的明星是谁?', '用户', '你最喜欢的一首歌是?', '计划', '你的初恋女友是谁?', '哦了', '2018-12-12 10:10:15'),
	(6, '小三', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-24 10:45:15', '0', '2018-03-25 22:28:51', '2018-03-25 22:27:00', '1', '1', '你最喜欢的明星是谁?', '喜鹊', '你最喜欢的一首歌是?', '我的心', '你的初恋女友是谁?', '曹君怡', '2018-12-12 10:10:15'),
	(7, '小五', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-24 10:59:24', '0', '2018-12-13 20:31:29', '2018-12-13 20:31:30', '1', '1', '你最喜欢的明星是谁?', '广告', '你最喜欢的一首歌是?', '刚刚', '你的初恋女友是谁?', '33', '2018-12-12 10:10:15'),
	(8, '小六', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-24 11:01:33', '0', '2018-03-24 11:11:01', '2018-03-24 11:10:46', '1', '1', '你最喜欢的明星是谁?', '22', '你最喜欢的一首歌是?', '33', '你的初恋女友是谁?', '55', '2018-12-12 10:10:15'),
	(9, '小四', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 22:19:23', '0', '2018-03-25 22:28:48', '2018-03-25 22:26:24', '1', '1', '你最喜欢的明星是谁?', '广告', '你最喜欢的一首歌是?', '22', '你的初恋女友是谁?', '6.0', '2018-12-12 10:10:15'),
	(10, '小散', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-22 21:49:11', '0', '2018-03-24 09:13:55', '2018-03-24 09:13:54', '1', '1', '你最喜欢的明星是谁?', '张娜拉', '你最喜欢的一首歌是什么?', '黄昏', '你父亲的生日是什么时候', '6.5', '2018-12-12 10:10:15'),
	(11, '无名1', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 21:30:19', '0', '2020-05-19 11:08:15', '2019-02-23 12:14:40', '1', '1', '你最喜欢的明星是谁?', '不知道', '你最喜欢的一首歌是?', '微微', '你的初恋女友是谁?', '阿阿', '2018-12-12 10:10:15'),
	(12, '无名2', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 21:31:16', '0', '2018-04-01 00:28:38', '2018-04-01 07:19:46', '1', '1', '你最喜欢的明星是谁?', '方法', '你最喜欢的一首歌是?', '广告', '你的初恋女友是谁?', '黄昏', '2018-12-12 10:10:15'),
	(13, '无名3', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 21:32:59', '0', '2019-01-10 12:28:04', '2019-01-10 12:28:10', '1', '1', '你最喜欢的明星是谁?', '国际化', '你最喜欢的一首歌是?', '5鱼', '你的初恋女友是谁?', '偶爱', '2018-12-12 10:10:15'),
	(14, '无名5', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 21:34:28', '0', '2020-05-19 10:43:10', '2020-05-19 10:42:58', '1', '1', '你最喜欢的明星是谁?', '哦哦', '你最喜欢的一首歌是?', '看了', '你的初恋女友是谁?', '鱼体育', '2018-12-12 10:10:15'),
	(15, '无名7', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 21:41:30', '0', '2019-01-21 16:19:53', '2019-01-21 18:00:23', '1', '1', '你最喜欢的明星是谁?', '开个', '你最喜欢的一首歌是?', '提供', '你的初恋女友是谁?', '吧v大', '2018-12-12 10:10:15'),
	(16, '无名9', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 21:43:22', '0', '2019-01-10 12:28:26', '2019-01-10 12:28:36', '1', '1', '你最喜欢的明星是谁?', '提供', '你最喜欢的一首歌是?', '耳朵', '你的初恋女友是谁?', '而非', '2018-12-12 10:10:15'),
	(17, '陈晓', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-23 14:10:55', '0', '2018-03-23 22:53:36', '2018-03-23 22:17:47', '1', '1', '你的初恋女孩叫什么名字?', '你吗', '你母亲的生日是什么时候?', '0621', '你最喜欢的一首歌是什么?', '喊话', '2018-12-12 10:10:15'),
	(18, '黄朝辉', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-22 22:36:37', '0', '2020-05-20 10:08:04', '2020-05-20 10:19:00', '1', '1', '你最喜欢的明星是谁?', '张娜拉', '你最喜欢的一首歌是什么?', '天天', '你父亲的生日是什么时候', '6.0', '2018-12-12 10:10:15'),
	(19, '黄老板', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 22:18:20', '0', '2018-03-25 22:24:51', '2018-03-25 22:24:26', '1', '1', '你最喜欢的明星是谁?', '张娜拉', '你最喜欢的一首歌是?', '我的心', '你的初恋女友是谁?', '不是你', '2018-12-12 10:10:15'),
	(20, '씨발', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-12 09:58:06', '0', '2018-12-12 16:13:52', '2018-12-12 16:13:54', '1', '1', '你最喜欢的水果是什么?', '사과', '你父亲生日是什么时候?', '유원일십사일', '你最想去哪旅行?', '한국', '2018-12-12 10:10:15'),
	(21, '徐志摩', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-12 10:10:50', '0', '2018-12-12 10:16:02', '2018-12-12 10:16:02', '1', '1', '你最喜欢的水果是什么?', '梨子', '你父亲生日是什么时候?', '元旦节', '你最想去哪旅行?', '非洲', '2018-12-12 10:10:50'),
	(22, 'hhh', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-12 13:55:37', '0', '2018-12-12 14:51:08', '2018-12-12 14:51:08', '1', '1', '你最喜欢的水果是什么?', '梨子', '你父亲生日是什么时候?', 'dd', '你最想去哪旅行?', '한국', '2018-12-12 13:55:37'),
	(23, 'aa', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-12 16:38:58', '0', '2020-05-21 16:36:02', '2020-05-21 16:38:45', '1', '1', '你最喜欢的水果是什么?', '111', '你父亲生日是什么时候?', '111', '你最想去哪旅行?', '111', '2018-12-12 16:38:58'),
	(24, 'bb', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-12 16:39:16', '0', '2020-05-21 09:49:35', '2020-05-19 10:35:05', '1', '1', '你最喜欢的水果是什么?', '的的', '你父亲生日是什么时候?', '都是大神', '你最想去哪旅行?', '都是的', '2018-12-12 16:39:16'),
	(25, 'vb', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-13 15:34:55', '0', '2020-05-19 10:35:09', '2020-05-19 10:35:39', '1', '1', '你最喜欢的水果是什么?', '的的', '你父亲生日是什么时候?', '33', '你最想去哪旅行?', '55', '2018-12-13 15:34:55'),
	(26, 'jkx', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-13 15:35:13', '0', '2018-12-13 15:41:00', '2018-12-13 15:41:00', '0', '1', '你最想去哪旅行?', '中国', '你最想去哪旅行?', '中国', '你怎么看待中国的房价?', '太高了', '2018-12-13 15:35:13'),
	(27, 'zhuhao', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-13 15:35:20', '0', '2020-05-15 15:18:08', '2020-05-15 16:49:49', '1', '1', '你感觉人生操蛋吗?', '操蛋', '你感觉人生操蛋吗?', '操蛋', '你感觉人生操蛋吗?', '操蛋', '2018-12-13 15:35:20'),
	(28, 'good', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-13 15:35:31', '0', '2018-12-13 15:37:52', '2018-12-13 15:37:53', '1', '1', '你的初中在哪里？', '不告诉你', '你父亲生日是什么时候?', '不告诉你', '你最想去哪旅行?', '不告诉你', '2018-12-13 15:35:31'),
	(29, 'gt', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-21 11:42:00', '0', '2018-12-21 11:42:00', NULL, '1', '1', '你最喜欢的水果是什么?', '11', '你父亲生日是什么时候?', '11', '你最想去哪旅行?', '11', '2018-12-21 11:42:01'),
	(30, 'hch', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2019-06-15 16:46:03', '0', '2020-05-12 14:42:08', '2020-05-12 14:42:58', '1', '1', '你最喜欢的水果是什么?', '111', '你父亲生日是什么时候?', '111', '你最想去哪旅行?', '111', '2019-06-15 16:46:03'),
	(31, '9049', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2019-12-03 11:17:40', '0', '2020-05-20 16:50:08', '2020-05-20 16:50:10', '1', '1', '你最喜欢的水果是什么?', '11', '你父亲生日是什么时候?', '11', '你最想去哪旅行?', '11', '2019-12-03 11:17:40'),
	(32, 'ymzymz', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2019-12-03 11:21:37', '0', '2020-05-20 16:51:09', '2020-05-20 16:51:10', '1', '1', '你最喜欢的水果是什么?', '11', '你父亲生日是什么时候?', '11', '你最想去哪旅行?', '11', '2019-12-03 11:21:37'),
	(33, 'gg', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2020-05-11 16:07:46', '0', '2020-05-14 11:10:19', '2020-05-14 11:10:27', '1', '1', '你最喜欢的水果是什么?', '111', '你父亲生日是什么时候?', '111', '你最想去哪旅行?', '111', '2020-05-11 16:07:46'),
	(34, 'dd', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2020-05-14 11:19:25', '0', '2020-05-14 11:19:35', '2020-05-14 11:21:26', '1', '1', '你最喜欢的水果是什么?', '111', '你父亲生日是什么时候?', '111', '你最想去哪旅行?', '111', '2020-05-14 11:19:25'),
	(35, 'ee', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2020-05-14 13:19:16', '0', '2020-05-18 09:38:38', '2020-05-14 13:19:25', '1', '1', '你最喜欢的水果是什么?', '111', '你父亲生日是什么时候?', '111', '你最想去哪旅行?', '111', '2020-05-14 13:19:16'),
	(36, 'hh', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2020-05-14 13:29:26', '0', '2020-05-14 13:29:35', '2020-05-14 14:01:49', '1', '1', '你最喜欢的水果是什么?', '111', '你父亲生日是什么时候?', '111', '你最想去哪旅行?', '111', '2020-05-14 13:29:26'),
	(37, 'shabi', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2020-05-21 15:06:23', '0', '2020-05-21 15:14:25', '2020-05-21 15:14:29', '1', '1', '你最喜欢的水果是什么?', '111', '你父亲生日是什么时候?', '111', '你最想去哪旅行?', '111', '2020-05-21 15:06:23');
/*!40000 ALTER TABLE `ws_users` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_user_profile
CREATE TABLE IF NOT EXISTS `ws_user_profile` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(10) unsigned NOT NULL COMMENT '用户ID',
  `user_name` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `real_name` varchar(50) NOT NULL DEFAULT '' COMMENT '真实姓名',
  `img` varchar(200) NOT NULL DEFAULT '' COMMENT '头像url',
  `sign` varchar(250) NOT NULL DEFAULT '这人很懒,一点也没留下' COMMENT '个性签名',
  `age` int(11) NOT NULL DEFAULT '0' COMMENT '年龄',
  `sex` int(11) NOT NULL DEFAULT '1' COMMENT '性别 1:男 2:女 3:未知',
  `sex_text` varchar(50) NOT NULL DEFAULT '男' COMMENT '性别(文本)',
  `tel` varchar(50) NOT NULL DEFAULT '' COMMENT '电话',
  `address` varchar(150) NOT NULL DEFAULT '' COMMENT '地址',
  `profession` int(11) NOT NULL DEFAULT '99' COMMENT '职业  1:IT 2:建筑  3:金融  4:个体商户 5:旅游 99:其他',
  `profession_text` varchar(50) NOT NULL DEFAULT '其他' COMMENT '职业(文本)',
  `hobby` int(11) NOT NULL DEFAULT '99' COMMENT '爱好 1:篮球 2:足球  3:爬山 4:旅游  5:网游  99:其他',
  `hobby_text` varchar(50) NOT NULL DEFAULT '其他' COMMENT '爱好(文本)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- Dumping data for table zhdd.ws_user_profile: ~9 rows (approximately)
/*!40000 ALTER TABLE `ws_user_profile` DISABLE KEYS */;
INSERT INTO `ws_user_profile` (`id`, `user_id`, `user_name`, `real_name`, `img`, `sign`, `age`, `sex`, `sex_text`, `tel`, `address`, `profession`, `profession_text`, `hobby`, `hobby_text`, `create_time`) VALUES
	(6, 24, 'bb', 'BB', 'http://192.168.31.106:8101/headImg/D41574B2-569D-437F-A1F1-58FC68C14EAE.jpeg', '我很懒   大大 ', 11, 2, '女', '2548745', '韩国首尔', 2, '建筑', 3, '爬山', '2019-01-08 16:22:30'),
	(7, 18, '黄朝辉', '黄CC', 'http://localhost:8101/headImg/5.jpg', '投入和更换一句话', 23, 1, '男', '4344524425', '广东佛山', 5, '旅游', 6, '其他', '2019-01-08 17:47:05'),
	(8, 1, 'admin', 'admin', 'http://localhost:8101/circle/admin.jpg', '这人很懒,一点也没留下', 23, 2, '女', '8888888', '无名岛', 1, 'IT', 1, '篮球', '2019-01-16 11:00:50'),
	(9, 23, 'aa', '我没有名字', 'http://localhost:8101/headImg/5455.jpg', '不喜欢签名', 88, 1, '男', '13888888', '日本东京', 4, '个体商户', 4, '旅游', '2019-01-17 13:17:29'),
	(11, 30, 'hch', '洗吧', 'http://localhost:8101/headImg/5ce13ad4-7f12-4a56-8d02-e18e6f933b92.jpg', 'wfck', 50, 1, '男', '31232412414', '韩国首尔', 2, '建筑', 2, '足球', '2019-06-15 16:57:14'),
	(13, 35, 'ee', '', 'http://localhost:8101/headImg/4325.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', 99, '其他', 99, '其他', '2020-05-14 13:19:17'),
	(14, 36, 'hh', '', 'http://localhost:8101/headImg/player.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', 99, '其他', 99, '其他', '2020-05-14 13:29:27'),
	(15, 27, 'zhuhao', '操蛋', 'http://localhost:8101/headImg/5bd994e3-8aa3-40e6-abec-0a65b36489b4.png', '这人很懒,一点也没留下', 29, 3, '未知', '3111111111', '江苏', 1, 'IT', 3, '爬山', '2020-05-15 15:13:09'),
	(16, 37, 'shabi', '我是傻逼', 'http://192.168.31.106:8101/headImg/fj2.jpg', 'fuck', 25, 2, '女', '17777777777', '韩国江原道', 3, '金融', 4, '旅游', '2020-05-21 15:06:23');
/*!40000 ALTER TABLE `ws_user_profile` ENABLE KEYS */;



CREATE TABLE `ws_feedback` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`user_id` INT(11) NULL DEFAULT NULL COMMENT '用户id',
	`user_name` VARCHAR(50) NULL DEFAULT '' COMMENT '用户名称',
	`type` VARCHAR(2) NULL DEFAULT '1' COMMENT '反馈类型 1:建议 2:问题',
	`title` VARCHAR(100) NULL DEFAULT '' COMMENT '标题',
	`content` VARCHAR(200) NULL DEFAULT NULL COMMENT '问题描述',
	`pic_url` VARCHAR(300) NULL DEFAULT '' COMMENT '图片url',
	`reply_content` VARCHAR(200) NULL DEFAULT '' COMMENT '答复内容',
	`reply_time` DATETIME NULL DEFAULT NULL COMMENT '答复时间',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	`status` TINYINT(2) NOT NULL DEFAULT '1' COMMENT '状态 0:已删除 1:待答复 2:已答复',
	PRIMARY KEY (`id`)
)
COMMENT='问题反馈'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;


/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
