-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- Server version:               5.7.3-m13 - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL 版本:                  10.1.0.5464
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
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `k` varchar(100) DEFAULT NULL COMMENT '键',
  `v` varchar(1000) DEFAULT NULL COMMENT '值',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `kv_type` int(11) unsigned DEFAULT NULL,
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
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
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
  `title` varchar(100) NOT NULL COMMENT '标题',
  `content` varchar(500) NOT NULL COMMENT '内容',
  `receive_list` varchar(1000) NOT NULL DEFAULT '' COMMENT '接收人列表',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8 COMMENT='广告表';

-- Dumping data for table zhdd.ws_ads: ~43 rows (approximately)
/*!40000 ALTER TABLE `ws_ads` DISABLE KEYS */;
INSERT INTO `ws_ads` (`id`, `title`, `content`, `receive_list`, `create_time`) VALUES
	(1, '方法发', '的水管公司给', '[]', '2018-12-11 15:31:07'),
	(2, '法国德国g', ' 哈哈 解放军进军就', '[无名1]', '2018-12-11 15:31:34'),
	(3, '的发射点速度', ' 是德国电视公司的公司 ', '[黄朝辉]', '2018-12-11 15:32:16'),
	(4, '傅海峰高房价j\'g\'j', '经国家飓风九九就就', '[黄朝辉, 无名1]', '2018-12-11 15:33:12'),
	(5, '1115151515', '规划规划规范化共和国和规划绘画风格', '[黄朝辉, 无名1]', '2018-12-11 15:42:41'),
	(6, '3123123', '31232131231', '[黄朝辉]', '2018-12-11 16:16:56'),
	(7, '水管如果r', ' 热土同日而语热液一样', '[黄朝辉]', '2018-12-11 16:17:01'),
	(8, '也也也也e\'y', '恶业爷爷爷爷而已而已', '[黄朝辉]', '2018-12-11 16:17:05'),
	(9, '有恶业爷爷', '恶业恶业也也容易人y\'d\'f', '[黄朝辉]', '2018-12-11 16:17:09'),
	(10, ' 而也让y', '也让也让也让恶业有r', '[黄朝辉]', '2018-12-11 16:17:14'),
	(11, '犹太人和r\'r\'r', '柔软如如如若uu ', '[黄朝辉]', '2018-12-11 16:17:22'),
	(12, '22323525235435433663463464643646436346346', '33他热热也容易热一热也让耶耶耶Eyre也容易有也容易而已而炎热预热也溶液溶液而已也容易也让也容易二爷也让而已恶业也让炎热', '[黄朝辉]', '2018-12-11 16:22:55'),
	(13, '你你你你你你你你你你你你你你你您', '', '[黄朝辉]', '2018-12-11 16:32:48'),
	(14, '23如2让3 让3 让3让 让 人', '', '[黄朝辉]', '2018-12-11 16:37:14'),
	(15, '', '热认为威威二万人认为尔人而扼腕认为人文让人二五为让人', '[黄朝辉]', '2018-12-11 16:37:26'),
	(16, '夫是德国广东省根深蒂固水管公司三国杀公司水管是给', '是的话更好广告国际化记号记号看看看看愉快愉快愉快uu苦苦苦苦啊看', '[黄朝辉]', '2018-12-11 16:38:19'),
	(17, '你好啊  啊 啊啊 啊 啊 ', '312家具的垃圾了肯德基拉科技贷款垃圾考虑到拉萨的垃圾垃圾啊是否离开安居客立法将离开房间里卡司法局理发发送发顺丰安抚 ', '[]', '2018-12-12 09:36:44'),
	(18, '오늘 날씨가 정말 밝아요', '이런 좋은 날씨라서 운동을 해야지.아무든 몸이 제일 중요해여', '[]', '2018-12-12 10:03:24'),
	(19, '3123123123', '21321 deaqs fe af ', '[]', '2018-12-13 11:57:28'),
	(20, 'fdsgsg gwetw', ' tew tdsgg gfd dfh gqwdasd a', '[aa]', '2018-12-13 11:57:41'),
	(21, '真的 吗  我不相信', '你不信就算了', '[无名7]', '2018-12-13 14:03:00'),
	(22, '3213', '312312312', '[aa]', '2018-12-21 17:15:26'),
	(23, '32131', '31233', '[aa, bb]', '2018-12-21 17:15:48'),
	(24, 'rt t', 'rwttw', '[aa, bb]', '2018-12-21 17:17:53'),
	(25, '34', '4134', '[aa, bb]', '2018-12-21 17:18:55'),
	(26, '你好', '好吗', '[bb, aa]', '2019-01-04 20:26:58'),
	(27, '今天下雪了', '雪很大 一直下个不停', '[aa]', '2019-01-09 17:03:42'),
	(28, 'gsdfgsg', 'fdsggdf', '[aa]', '2019-01-10 11:41:21'),
	(29, '111', '11', '[aa]', '2019-01-10 11:42:12'),
	(30, '3213123', '31231233', '[aa]', '2019-01-10 11:42:44'),
	(31, 'ewttew', 'twetwe', '[aa]', '2019-01-10 11:43:17'),
	(32, '1111111111', '122222', '[aa]', '2019-01-10 11:44:35'),
	(33, '321312312', '312312323', '[aa]', '2019-01-10 11:44:53'),
	(34, '31231', '31323', '[aa]', '2019-01-10 11:47:01'),
	(35, '12321', 'fdfswf', '[aa]', '2019-01-10 11:47:33'),
	(36, 'gsgsdg', 'afafafa fa', '[aa]', '2019-01-10 15:23:57'),
	(37, '今天阴天', '妈的', '[aa]', '2019-01-10 16:26:33'),
	(38, '今天的天气不好 雾很大  空气质量差', '开车注意控制速度  注意人身安全', '[aa]', '2019-01-14 11:37:12'),
	(39, '今天1.21', '天气晴朗', '[]', '2019-01-21 13:36:03'),
	(40, '今天垃圾', '辜负他  合法化电话', '[aa]', '2019-02-17 13:13:30'),
	(41, '交换机和韩国', '开机开卷考就', '[无名3]', '2020-05-11 20:12:10'),
	(52, '明天请假一天', '明天有事情需要办理', '[aa]', '2020-05-12 20:23:29'),
	(53, '测试1111111', '福建广东监管机构\n交接给大家根据国际\n据官方机构', '[aa]', '2020-05-12 20:25:20'),
	(55, '今天星期一 阴天 下小雨', '又是连续上6天班', '[]', '2020-05-25 18:42:04'),
	(56, '明天提前半小时上班', '全体员工不能迟到，记得打卡', '[aa]', '2020-05-25 18:42:44');
/*!40000 ALTER TABLE `ws_ads` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_chatlog
CREATE TABLE IF NOT EXISTS `ws_chatlog` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `time` varchar(50) DEFAULT NULL COMMENT '时间',
  `user` varchar(50) DEFAULT '' COMMENT '发起人',
  `to_user` varchar(50) DEFAULT '' COMMENT '被聊人',
  `msg` varchar(4000) DEFAULT '' COMMENT '内容',
  `remark` varchar(400) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='聊天记录表';

-- Dumping data for table zhdd.ws_chatlog: ~9 rows (approximately)
/*!40000 ALTER TABLE `ws_chatlog` DISABLE KEYS */;
INSERT INTO `ws_chatlog` (`id`, `time`, `user`, `to_user`, `msg`, `remark`) VALUES
	(1, '2020-05-30 21:53:30', 'aa', '', '登录成功', 'Windows NT 10.0'),
	(2, '2020-05-30 21:54:38', 'admin', '', '登录成功', 'Windows NT 10.0'),
	(3, '2020-05-30 21:55:23', '씨발', '', '登录成功', 'Windows NT 10.0'),
	(4, '2020-05-30 21:56:25', '씨발', '', '退出服务器', ''),
	(5, '2020-05-30 21:56:34', '씨발', '', '登录成功', 'Windows NT 10.0'),
	(6, '2020-05-30 21:57:34', '씨발', '', '退出服务器', ''),
	(7, '2020-05-31 10:18:20', 'aa', '', '登录成功', 'Windows NT 10.0'),
	(8, '2020-05-31 10:18:37', 'aa', '', '退出服务器', ''),
	(9, '2020-05-31 10:18:47', 'aa', '', '登录成功', 'Windows NT 10.0');
/*!40000 ALTER TABLE `ws_chatlog` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_circle
CREATE TABLE IF NOT EXISTS `ws_circle` (
  `id` int(255) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(30) NOT NULL COMMENT '用户名',
  `user_id` int(11) unsigned NOT NULL COMMENT '用户id',
  `content` varchar(1000) NOT NULL COMMENT '内容',
  `like_num` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '点赞数',
  `pic1` varchar(255) DEFAULT NULL COMMENT '图片1',
  `pic2` varchar(255) DEFAULT NULL COMMENT '图片2',
  `pic3` varchar(255) DEFAULT NULL COMMENT '图片3',
  `pic4` varchar(255) DEFAULT NULL COMMENT '图片4',
  `pic5` varchar(255) DEFAULT NULL COMMENT '图片5',
  `pic6` varchar(255) DEFAULT NULL COMMENT '图片6',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发表时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COMMENT='朋友圈表';

-- Dumping data for table zhdd.ws_circle: ~28 rows (approximately)
/*!40000 ALTER TABLE `ws_circle` DISABLE KEYS */;
INSERT INTO `ws_circle` (`id`, `user_name`, `user_id`, `content`, `like_num`, `pic1`, `pic2`, `pic3`, `pic4`, `pic5`, `pic6`, `create_time`) VALUES
	(11, 'aa', 23, '雪好像小了一点', 1, 'linux-back.png', NULL, NULL, NULL, NULL, NULL, '2019-01-09 11:26:01'),
	(12, 'aa', 23, '哈哈哈哈哈哈哈哈哈哈或或或', 0, '3.jpg', NULL, '4.jpg', NULL, NULL, NULL, '2019-01-09 11:30:25'),
	(13, 'aa', 23, '哈哈哈哈哈哈哈哈哈哈或或', 0, '1.jpg', '3.jpg', NULL, NULL, NULL, NULL, '2019-01-09 11:37:47'),
	(14, 'aa', 23, '哈哈哈哈哈', 0, '1.jpg', '2.jpg', NULL, NULL, NULL, NULL, '2019-01-09 13:06:52'),
	(15, 'aa', 23, '快快快', 0, '3.jpg', NULL, '1.jpg', NULL, NULL, NULL, '2019-01-09 13:08:17'),
	(16, 'aa', 23, '下雪又下雨 好烦', 0, '1.jpg', '2.jpg', '3.jpg', '4.jpg', NULL, NULL, '2019-01-09 13:15:06'),
	(17, 'aa', 23, '才欧查草草哦啊哦草哦啊', 1, '1.jpg', '2.jpg', '3.jpg', '4.jpg', NULL, NULL, '2019-01-09 13:17:15'),
	(18, 'aa', 23, '男孩女孩刚刚好孤鸿寡鹄', 1, '2.jpg', '3.jpg', '4.jpg', '1.jpg', NULL, NULL, '2019-01-09 13:28:54'),
	(19, 'aa', 23, '哈哈哈哈哈哈哈哈哈哈或或或或或或或或', 0, '1.jpg', 'aa_2.jpg', '3.jpg', '4.jpg', NULL, NULL, '2019-01-09 13:38:43'),
	(20, 'bb', 24, '的时光时光', 0, '3.jpg', NULL, '1.jpg', NULL, NULL, NULL, '2019-01-09 13:44:59'),
	(21, 'bb', 24, '发送给大哥g', 0, '3.jpg', NULL, NULL, NULL, NULL, NULL, '2019-01-09 13:47:16'),
	(22, 'bb', 24, '股份的韩国发货呢', 0, '3.jpg', NULL, NULL, '4.jpg', NULL, NULL, '2019-01-09 13:47:28'),
	(23, 'bb', 24, '鬼斧神工回复放大', 0, NULL, '1.jpg', NULL, '4.jpg', NULL, NULL, '2019-01-09 13:47:41'),
	(24, 'bb', 24, '同', 1, '3.jpg', NULL, NULL, '2.jpg', NULL, NULL, '2019-01-09 13:47:52'),
	(25, 'bb', 24, '交换机加工的闪光点', 1, '微信图片_20181225181122.jpg', '2.jpg', 'linux-back.png', '微信图片_20181225175811.jpg', NULL, NULL, '2019-01-09 14:29:06'),
	(28, 'aa', 23, '', 0, '3.jpg', '微信图片_20181225175814.jpg', NULL, NULL, NULL, NULL, '2019-01-09 16:14:24'),
	(29, 'aa', 23, '哈哈大道理可抵扣可大可额饿哦', 5, '微信图片_20181225181122.jpg', NULL, NULL, NULL, NULL, NULL, '2019-01-09 16:15:53'),
	(30, 'aa', 23, '过分的话东方红活动 ', 9, '微信图片_20181225174957.jpg', NULL, NULL, NULL, NULL, NULL, '2019-01-09 16:17:00'),
	(32, 'aa', 23, '你好吗', 0, '2.jpg', NULL, NULL, NULL, NULL, NULL, '2019-01-10 13:06:55'),
	(33, 'aa', 23, '今天下了一天雨', 29, '微信图片_20181225175814.jpg', '微信图片_20181225175816.jpg', '微信图片_20181225180500.jpg', '微信图片_20181225174957.jpg', NULL, NULL, '2019-01-11 15:09:04'),
	(34, 'bb', 24, '好好飞洒发开发', 3, NULL, 'linux-back.png', NULL, NULL, NULL, NULL, '2019-01-11 15:38:04'),
	(35, 'aa', 23, '今天雾很大  可见度不足50米', 1, '微信图片_20181225181122.jpg', '微信图片_20181225180500.jpg', '3.jpg', '微信图片_20181225175816.jpg', NULL, NULL, '2019-01-14 11:27:44'),
	(36, '黄朝辉', 18, '天气很好  很暖和', 8, '微信图片_20181225175816.jpg', 'player.jpg', NULL, 'admin.jpg', NULL, NULL, '2019-01-16 16:15:33'),
	(38, 'admin', 1, '反倒是公司奉公守法好和规范化更符合规范化规范合格机构房价回归好挂号费的活动湖广会馆黑寡妇黑寡妇', 0, '2.jpg', '微信图片_20181225181122.jpg', '微信图片_20181225175816.jpg', '微信图片_20181225174957.jpg', NULL, NULL, '2019-01-21 13:26:42'),
	(39, 'admin', 1, '刚发的时光时光归属感蔬果蔬果十多个十多个第三个十多个多个   多少广东省广东省大公司', 1, 'linux-back.png', 'player.jpg', '3.jpg', '微信图片_20181225175816.jpg', NULL, NULL, '2019-01-21 13:27:05'),
	(40, 'admin', 1, '今天天气好 人少', 0, NULL, '微信图片_20181225175816.jpg', NULL, NULL, NULL, NULL, '2019-01-21 13:36:55'),
	(41, 'aa', 23, '里面可能你究竟 ', 3, NULL, '2.jpg', NULL, NULL, NULL, NULL, '2019-01-26 21:57:31'),
	(42, '无名3', 13, '考虑时间管理会计师的价格快速', 1, 'http://localhost:8101/circle\\8.jpg', NULL, 'http://localhost:8101/circle\\8da770a589874189852b1504361231ae_th.gif', NULL, NULL, NULL, '2020-05-11 18:38:35');
/*!40000 ALTER TABLE `ws_circle` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_circle_comment
CREATE TABLE IF NOT EXISTS `ws_circle_comment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `circle_id` int(11) unsigned DEFAULT NULL COMMENT '朋友圈id',
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '评论人id',
  `user_name` varchar(255) DEFAULT NULL COMMENT '评论人姓名',
  `comment` varchar(1000) DEFAULT '' COMMENT '评论内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COMMENT='朋友圈评论表';

-- Dumping data for table zhdd.ws_circle_comment: ~8 rows (approximately)
/*!40000 ALTER TABLE `ws_circle_comment` DISABLE KEYS */;
INSERT INTO `ws_circle_comment` (`id`, `circle_id`, `user_id`, `user_name`, `comment`, `create_time`) VALUES
	(1, 1, 25, 'vb', '为什么不舒服', '2019-01-04 21:20:28'),
	(2, 1, 28, 'good', '你怎么呢', '2019-01-04 21:20:29'),
	(3, 1, 26, 'jkx', '咋啦', '2019-01-04 21:20:30'),
	(38, 40, 1, 'admin', '真的妈妈们木木木吗木吗', '2019-01-21 13:37:12'),
	(40, 41, 18, '黄朝辉', '121345', '2019-01-26 21:57:47'),
	(41, 41, 15, '无名7', '15/‘了，', '2019-01-26 21:57:56'),
	(43, 11, 1, 'admin', '会更加', '2020-05-11 20:11:47'),
	(44, 11, 1, 'admin', 'fdafaffafa ', '2020-05-13 12:43:48');
/*!40000 ALTER TABLE `ws_circle_comment` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_common
CREATE TABLE IF NOT EXISTS `ws_common` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(50) DEFAULT NULL COMMENT '类型',
  `name` varchar(300) DEFAULT NULL COMMENT '内容',
  `orderby` int(10) unsigned DEFAULT NULL COMMENT '排序号',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  KEY `Index 1` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8 COMMENT='通用配置表';

-- Dumping data for table zhdd.ws_common: ~50 rows (approximately)
/*!40000 ALTER TABLE `ws_common` DISABLE KEYS */;
INSERT INTO `ws_common` (`id`, `type`, `name`, `orderby`, `remark`) VALUES
	(1, 'zh', '操你妈', 1, '233'),
	(2, 'mgc', '习近平', 5, NULL),
	(3, 'mgc', '六四', 6, NULL),
	(4, 'zh', '滚你妈', 25, NULL),
	(5, 'zh', '操蛋', 26, NULL),
	(6, 'zh', '滚一边去', 27, NULL),
	(7, 'cyy', '你好', 28, NULL),
	(8, 'zcwt', '你最喜欢的水果是什么?', 29, NULL),
	(48, 'cyy', '晚安', 30, NULL),
	(47, 'cyy', '拜拜', 31, NULL),
	(46, 'cyy', '现在忙，一会联系你', 32, NULL),
	(45, 'cyy', '晚上见', 33, NULL),
	(44, 'cyy', '在吗？', 34, NULL),
	(43, 'cyy', '打扰一下 ，请教一个问题', 35, NULL),
	(42, 'zcwt', '你父亲生日是什么时候?', 36, NULL),
	(41, 'zcwt', '你最想去哪旅行?', 37, NULL),
	(40, 'zcwt', '你的月薪多少？', 38, NULL),
	(39, 'zcwt', '你的初中在哪里？', 39, NULL),
	(38, 'zcwt', '你的第一次是什么时候？', 40, NULL),
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
	(18, 'zh', '逼', 67, NULL),
	(17, 'zh', '操', 68, NULL),
	(16, 'zh', '鸡巴', 69, NULL),
	(15, 'zcwt', '你感觉人生操蛋吗?', 71, NULL),
	(14, 'zcwt', '你最讨厌什么样的人?', 73, NULL),
	(13, 'zcwt', '你怎么看待中国的房价?', 74, NULL),
	(12, 'cyy', '搞活经济', 76, NULL),
	(11, 'zh', 'What the fuck?', 77, NULL),
	(10, 'mgc', 'rtyt', 78, NULL),
	(9, 'mgc', 'getrty', 79, NULL),
	(82, 'cyy', 'anninghaseiyo', NULL, NULL),
	(83, 'mgc', 'gcd', 88, NULL);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表';

-- Dumping data for table zhdd.ws_dic: ~0 rows (approximately)
/*!40000 ALTER TABLE `ws_dic` DISABLE KEYS */;
/*!40000 ALTER TABLE `ws_dic` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_feedback
CREATE TABLE IF NOT EXISTS `ws_feedback` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(50) DEFAULT '' COMMENT '用户名称',
  `type` varchar(2) DEFAULT '1' COMMENT '反馈类型 1:建议 2:问题',
  `title` varchar(100) DEFAULT '' COMMENT '标题',
  `content` varchar(200) DEFAULT NULL COMMENT '问题描述',
  `pic_url` varchar(300) DEFAULT '' COMMENT '图片url',
  `reply_content` varchar(200) DEFAULT '' COMMENT '答复内容',
  `reply_time` datetime DEFAULT NULL COMMENT '答复时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态 0:已删除 1:待答复 2:已答复',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='问题反馈';

-- Dumping data for table zhdd.ws_feedback: ~0 rows (approximately)
/*!40000 ALTER TABLE `ws_feedback` DISABLE KEYS */;
/*!40000 ALTER TABLE `ws_feedback` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_file
CREATE TABLE IF NOT EXISTS `ws_file` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `folder` varchar(50) NOT NULL DEFAULT '' COMMENT '文件类型',
  `filename` varchar(50) NOT NULL DEFAULT '' COMMENT '文件名',
  `disk_path` varchar(100) DEFAULT '' COMMENT '存储磁盘目录',
  `url` varchar(100) NOT NULL DEFAULT '' COMMENT 'url',
  `file_size` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '文件大小',
  `author` varchar(50) NOT NULL DEFAULT '' COMMENT '未知',
  `track_length` varchar(50) NOT NULL DEFAULT '' COMMENT '文件时长',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '0:无效 1:有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=546 DEFAULT CHARSET=utf8 COMMENT='文件表';

-- Dumping data for table zhdd.ws_file: ~42 rows (approximately)
/*!40000 ALTER TABLE `ws_file` DISABLE KEYS */;
INSERT INTO `ws_file` (`id`, `user`, `folder`, `filename`, `disk_path`, `url`, `file_size`, `author`, `track_length`, `create_time`, `update_time`, `status`) VALUES
	(504, 'aa', '', 'BbAhn - 발걸음.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\BbAhn - 발걸음.mp3', 8554995, '', '03:27', '2020-05-10 16:46:22', '2020-05-10 16:46:22', 1),
	(505, 'aa', '', 'Herz Analog - 내겐 그대만 있으면 돼요.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\Herz Analog - 내겐 그대만 있으면 돼요.mp3', 8738143, '', '03:16', '2020-05-10 16:46:38', '2020-05-10 16:46:38', 1),
	(506, 'aa', '', 'The Film - 함께 걷던 길 (Duet With. April).mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\The Film - 함께 걷던 길 (Duet With. April).mp3', 9610554, '', '03:53', '2020-05-10 16:46:38', '2020-05-10 16:46:38', 1),
	(507, 'aa', '', '安珍京--做不到的人.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\安珍京--做不到的人.mp3', 3129240, '', '03:15', '2020-05-10 16:46:38', '2020-05-10 16:46:38', 1),
	(508, 'aa', '', '그대도 나와 같다면.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\그대도 나와 같다면.mp3', 6283798, '', '04:21', '2020-05-10 16:46:38', '2020-05-10 16:46:38', 1),
	(509, 'aa', '', '高恩 - 눈물따라.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\高恩 - 눈물따라.mp3', 4102091, '', '03:28', '2020-05-10 16:46:38', '2020-05-10 16:46:38', 1),
	(510, 'aa', '', '金俊成 - 세상에서… (Ending).mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\金俊成 - 세상에서… (Ending).mp3', 6176608, '', '03:32', '2020-05-10 16:46:38', '2020-05-10 16:46:38', 1),
	(511, 'aa', '', '더 씨야 - ﻿내 맘은 죽어가요 (feat. Speed).mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\더 씨야 - ﻿내 맘은 죽어가요 (feat. Speed).mp3', 4021412, '', '04:11', '2020-05-10 16:46:38', '2020-05-10 16:46:38', 1),
	(512, 'aa', '', '디데이 (D-day) - 보고싶다 (Feat. 멜로디아).mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\디데이 (D-day) - 보고싶다 (Feat. 멜로디아).mp3', 9718144, '', '04:01', '2020-05-10 16:46:38', '2020-05-10 16:46:38', 1),
	(513, 'aa', '', '张娜拉 - 祈祷 (Last Pray).mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\张娜拉 - 祈祷 (Last Pray).mp3', 6842283, '', '04:37', '2020-05-10 16:46:39', '2020-05-10 16:46:39', 1),
	(514, 'aa', '', '성주 - 미치겠다 (Crazy).mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\성주 - 미치겠다 (Crazy).mp3', 7164161, '', '02:50', '2020-05-10 16:46:39', '2020-05-10 16:46:39', 1),
	(515, 'aa', '', 'BbAhn - 발걸음.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\BbAhn - 발걸음.mp3', 8554995, '', '03:27', '2020-05-10 16:47:47', '2020-05-10 16:47:47', 1),
	(516, 'bb', '', '高恩 - 눈물따라.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\高恩 - 눈물따라.mp3', 4102091, '', '03:28', '2020-05-10 16:54:24', '2020-05-10 16:54:24', 1),
	(517, 'aa', 'headImg', '1.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\1.jpg', 451216, '', '', '2020-05-11 18:29:55', '2020-05-11 18:29:55', 1),
	(518, 'aa', 'music', '그대도 나와 같다면.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\그대도 나와 같다면.mp3', 6283798, '', '04:21', '2020-05-11 18:30:32', '2020-05-11 18:30:32', 1),
	(519, 'aa', 'music', '高恩 - 눈물따라.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\高恩 - 눈물따라.mp3', 4102091, '', '03:28', '2020-05-11 18:30:33', '2020-05-11 18:30:33', 1),
	(520, 'aa', 'music', '金俊成 - 세상에서… (Ending).mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\金俊成 - 세상에서… (Ending).mp3', 6176608, '', '03:32', '2020-05-11 18:30:33', '2020-05-11 18:30:33', 1),
	(521, 'aa', 'music', '더 씨야 - ﻿내 맘은 죽어가요 (feat. Speed).mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\더 씨야 - ﻿내 맘은 죽어가요 (feat. Speed).mp3', 4021412, '', '04:11', '2020-05-11 18:30:33', '2020-05-11 18:30:33', 1),
	(522, 'aa', 'music', '디데이 (D-day) - 보고싶다 (Feat. 멜로디아).mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\디데이 (D-day) - 보고싶다 (Feat. 멜로디아).mp3', 9718144, '', '04:01', '2020-05-11 18:30:33', '2020-05-11 18:30:33', 1),
	(523, 'aa', 'music', '张娜拉 - 祈祷 (Last Pray).mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\张娜拉 - 祈祷 (Last Pray).mp3', 6842283, '', '04:37', '2020-05-11 18:30:33', '2020-05-11 18:30:33', 1),
	(524, 'aa', 'music', '성주 - 미치겠다 (Crazy).mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\성주 - 미치겠다 (Crazy).mp3', 7164161, '', '02:50', '2020-05-11 18:30:33', '2020-05-11 18:30:33', 1),
	(525, '无名3', 'circle', '8.jpg', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\8.jpg', 18612, '', '', '2020-05-11 18:38:30', '2020-05-11 18:38:30', 1),
	(526, '无名3', 'circle', '8da770a589874189852b1504361231ae_th.gif', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\8da770a589874189852b1504361231ae_th.gif', 1903681, '', '', '2020-05-11 18:38:33', '2020-05-11 18:38:33', 1),
	(527, '无名3', 'music', 'BbAhn - 발걸음.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\BbAhn - 발걸음.mp3', 8554995, '', '03:27', '2020-05-11 18:40:03', '2020-05-11 18:40:03', 1),
	(528, '无名3', 'music', 'Herz Analog - 내겐 그대만 있으면 돼요.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\Herz Analog - 내겐 그대만 있으면 돼요.mp3', 8738143, '', '03:16', '2020-05-11 18:40:03', '2020-05-11 18:40:03', 1),
	(529, '无名3', 'music', 'The Film - 함께 걷던 길 (Duet With. April).mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\The Film - 함께 걷던 길 (Duet With. April).mp3', 9610554, '', '03:53', '2020-05-11 18:40:03', '2020-05-11 18:40:03', 1),
	(530, '无名3', 'music', '安珍京--做不到的人.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\安珍京--做不到的人.mp3', 3129240, '', '03:15', '2020-05-11 18:40:03', '2020-05-11 18:40:03', 1),
	(531, 'aa', 'circle', '2.jpg', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\2.jpg', 204422, '', '', '2020-05-11 21:09:02', '2020-05-11 21:09:02', 1),
	(532, 'aa', 'circle', 'admin.jpg', 'D:\\\\temp\\\\circle', 'http://localhost:8101/circle\\admin.jpg', 43141, '', '', '2020-05-11 21:09:09', '2020-05-11 21:09:09', 1),
	(533, 'bb', 'headImg', 'image.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\image.jpg', 2630431, '', '', '2020-05-12 19:07:53', '2020-05-12 19:07:53', 1),
	(534, 'dd', 'headImg', '2.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\2.jpg', 21919, '', '', '2020-05-13 13:36:16', '2020-05-13 13:36:16', 1),
	(535, 'cc', 'headImg', 'u=3337455883,2495110955&fm=214&gp=0.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg\\u=3337455883,2495110955&fm=214&gp=0.jpg', 708505, '', '', '2020-05-13 14:14:51', '2020-05-13 14:14:51', 1),
	(536, 'admin', 'music', '安珍京--做不到的人.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music\\安珍京--做不到的人.mp3', 3129240, '', '03:15', '2020-05-16 09:35:27', '2020-05-16 09:35:27', 1),
	(537, 'aa', 'headImg', '8.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg/8.jpg', 18612, '', '', '2020-05-19 22:14:02', '2020-05-19 22:14:02', 1),
	(538, '', 'headImg', '1.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg/1.jpg', 451216, '', '', '2020-05-21 21:00:43', '2020-05-21 21:00:43', 1),
	(539, '', 'headImg', '1.jpg', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg/1.jpg', 451216, '', '', '2020-05-21 21:00:58', '2020-05-21 21:00:58', 1),
	(540, 'aa', 'music', 'BbAhn - 발걸음.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music/bced1b8d-6661-4925-a381-f2f52c470a87.mp3', 8554995, '', '03:27', '2020-05-22 21:13:05', '2020-05-22 21:13:05', 1),
	(541, 'aa', 'music', 'Herz Analog - 내겐 그대만 있으면 돼요.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music/8cc6e5e5-8bbb-4452-92ad-c0faca60af82.mp3', 8738143, '', '03:16', '2020-05-22 21:13:05', '2020-05-22 21:13:05', 1),
	(542, 'aa', 'music', 'The Film - 함께 걷던 길 (Duet With. April).mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music/b550e8d8-74af-42b0-8e44-dc90a2a7df53.mp3', 9610554, '', '03:53', '2020-05-22 21:13:05', '2020-05-22 21:13:05', 1),
	(543, 'aa', 'music', '安珍京--做不到的人.mp3', 'D:\\\\temp\\\\music', 'http://localhost:8101/music/039b2b07-a593-4f69-8eaa-3249c2a01e8d.mp3', 3129240, '', '03:15', '2020-05-22 21:13:05', '2020-05-22 21:13:05', 1),
	(544, 'aa', 'feedback', '3.jpg', 'D:\\\\temp\\\\feedback', 'http://localhost:8101/feedback/baa51f98-1010-4f6f-b05b-a72be78066f2.jpg', 67363, '', '', '2020-05-30 17:23:01', '2020-05-30 17:23:01', 1),
	(545, '씨발', 'headImg', 'mp55879339_1453404941528_2.gif', 'D:\\\\temp\\\\headImg', 'http://localhost:8101/headImg/6849ae90-3b33-4a9b-ba84-52705a92229d.gif', 1171696, '', '', '2020-05-30 21:55:48', '2020-05-30 21:55:48', 1);
/*!40000 ALTER TABLE `ws_file` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_friends
CREATE TABLE IF NOT EXISTS `ws_friends` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` int(10) unsigned NOT NULL COMMENT '用户id',
  `uname` varchar(50) NOT NULL DEFAULT '' COMMENT '用户姓名',
  `fid` int(11) unsigned NOT NULL COMMENT '好友id',
  `fname` varchar(50) NOT NULL DEFAULT '' COMMENT '好友姓名',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='好友列表';

-- Dumping data for table zhdd.ws_friends: ~20 rows (approximately)
/*!40000 ALTER TABLE `ws_friends` DISABLE KEYS */;
INSERT INTO `ws_friends` (`id`, `uid`, `uname`, `fid`, `fname`, `create_time`, `remark`) VALUES
	(3, 31, 'test', 32, 'kk', '2019-01-21 11:37:40', NULL),
	(4, 32, 'kk', 31, 'test', '2019-01-21 11:37:40', NULL),
	(5, 23, 'aa', 18, '黄朝辉', '2019-01-26 21:54:19', NULL),
	(6, 18, '黄朝辉', 23, 'aa', '2019-01-26 21:54:19', NULL),
	(7, 23, 'aa', 15, '无名7', '2019-01-26 21:54:39', NULL),
	(8, 15, '无名7', 23, 'aa', '2019-01-26 21:54:39', NULL),
	(9, 23, 'aa', 25, 'vb', '2019-01-26 21:54:42', NULL),
	(10, 25, 'vb', 23, 'aa', '2019-01-26 21:54:42', NULL),
	(11, 23, 'aa', 13, '无名3', '2020-05-11 18:37:18', NULL),
	(12, 13, '无名3', 23, 'aa', '2020-05-11 18:37:18', NULL),
	(13, 31, 'dd', 23, 'aa', '2020-05-13 18:12:02', NULL),
	(14, 23, 'aa', 31, 'dd', '2020-05-13 18:12:02', NULL),
	(15, 24, 'bb', 23, 'aa', '2020-05-19 21:07:52', ''),
	(16, 23, 'aa', 24, 'bb', '2020-05-19 21:07:52', ''),
	(17, 31, 'dd', 24, 'bb', '2020-05-19 21:08:07', ''),
	(18, 24, 'bb', 31, 'dd', '2020-05-19 21:08:07', ''),
	(19, 31, 'dd', 14, '无名5', '2020-05-21 20:29:28', ''),
	(20, 14, '无名5', 31, 'dd', '2020-05-21 20:29:28', ''),
	(21, 23, 'aa', 14, '无名5', '2020-05-21 20:29:30', ''),
	(22, 14, '无名5', 23, 'aa', '2020-05-21 20:29:30', '');
/*!40000 ALTER TABLE `ws_friends` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_friends_apply
CREATE TABLE IF NOT EXISTS `ws_friends_apply` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `from_id` int(10) unsigned NOT NULL COMMENT '申请人id',
  `from_name` varchar(50) NOT NULL DEFAULT '' COMMENT '申请人姓名',
  `to_id` int(10) unsigned NOT NULL COMMENT '好友id',
  `to_name` varchar(50) NOT NULL DEFAULT '' COMMENT '好友姓名',
  `process_status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '申请状态 1:申请中 2:被拒绝 3:申请成功',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=153 DEFAULT CHARSET=utf8 COMMENT='好友申请表';

-- Dumping data for table zhdd.ws_friends_apply: ~27 rows (approximately)
/*!40000 ALTER TABLE `ws_friends_apply` DISABLE KEYS */;
INSERT INTO `ws_friends_apply` (`id`, `from_id`, `from_name`, `to_id`, `to_name`, `process_status`, `create_time`) VALUES
	(122, 23, 'aa', 1, 'admin', 1, '2019-01-18 13:49:03'),
	(123, 31, 'test', 32, 'kk', 3, '2019-01-21 11:37:37'),
	(124, 23, 'aa', 18, '黄朝辉', 3, '2019-01-26 21:53:00'),
	(125, 23, 'aa', 25, 'vb', 3, '2019-01-26 21:53:58'),
	(126, 23, 'aa', 15, '无名7', 3, '2019-01-26 21:53:59'),
	(127, 23, 'aa', 16, '无名9', 1, '2019-02-17 14:19:56'),
	(128, 23, 'aa', 14, '无名5', 3, '2019-02-17 14:19:57'),
	(129, 23, 'aa', 13, '无名3', 3, '2020-05-11 18:36:43'),
	(130, 31, 'dd', 23, 'aa', 3, '2020-05-13 17:52:36'),
	(131, 31, 'dd', 30, 'cc', 1, '2020-05-13 18:11:21'),
	(132, 31, 'dd', 24, 'bb', 3, '2020-05-13 18:11:22'),
	(133, 31, 'dd', 13, '无名3', 1, '2020-05-13 18:11:23'),
	(134, 31, 'dd', 25, 'vb', 1, '2020-05-13 18:11:24'),
	(135, 31, 'dd', 15, '无名7', 1, '2020-05-13 18:11:24'),
	(136, 31, 'dd', 18, '黄朝辉', 1, '2020-05-13 18:11:25'),
	(137, 31, 'dd', 16, '无名9', 1, '2020-05-13 18:11:25'),
	(138, 31, 'dd', 14, '无名5', 3, '2020-05-13 18:11:26'),
	(139, 31, 'dd', 3, 'CNMgcd', 1, '2020-05-13 18:11:28'),
	(140, 31, 'dd', 11, '无名1', 1, '2020-05-13 18:11:30'),
	(141, 31, 'dd', 29, 'gt', 1, '2020-05-13 18:11:33'),
	(142, 31, 'dd', 7, '小五', 1, '2020-05-13 18:11:35'),
	(143, 31, 'dd', 27, 'zhuhao', 1, '2020-05-13 18:11:36'),
	(144, 31, 'dd', 26, 'jkx', 1, '2020-05-13 18:11:39'),
	(145, 31, 'dd', 4, 'mb', 1, '2020-05-13 18:11:41'),
	(146, 31, 'dd', 22, 'hhh', 1, '2020-05-13 18:11:43'),
	(147, 31, 'dd', 10, '小散', 1, '2020-05-13 18:11:46'),
	(148, 31, 'dd', 2, 'bbbb', 1, '2020-05-13 18:11:49'),
	(149, 24, 'bb', 23, 'aa', 3, '2020-05-19 21:07:29'),
	(150, 23, 'aa', 2, 'bbbb', 1, '2020-05-21 21:21:47'),
	(151, 23, 'aa', 2, 'bbbb', 1, '2020-05-21 21:21:48'),
	(152, 23, 'aa', 2, 'bbbb', 1, '2020-05-21 21:21:49');
/*!40000 ALTER TABLE `ws_friends_apply` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_operation_log
CREATE TABLE IF NOT EXISTS `ws_operation_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '操作人ID',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作日志表';

-- Dumping data for table zhdd.ws_operation_log: ~0 rows (approximately)
/*!40000 ALTER TABLE `ws_operation_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `ws_operation_log` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_sign
CREATE TABLE IF NOT EXISTS `ws_sign` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(10) unsigned NOT NULL COMMENT '用户id',
  `user_name` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户签到表';

-- Dumping data for table zhdd.ws_sign: ~0 rows (approximately)
/*!40000 ALTER TABLE `ws_sign` DISABLE KEYS */;
/*!40000 ALTER TABLE `ws_sign` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_student_info
CREATE TABLE IF NOT EXISTS `ws_student_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '姓名',
  `age` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '年龄',
  `sex` varchar(50) NOT NULL DEFAULT '' COMMENT '性别',
  KEY `Index 1` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table zhdd.ws_student_info: ~2 rows (approximately)
/*!40000 ALTER TABLE `ws_student_info` DISABLE KEYS */;
INSERT INTO `ws_student_info` (`id`, `name`, `age`, `sex`) VALUES
	(1, 'hch', 30, 'm'),
	(2, 'hyp', 29, 'f');
/*!40000 ALTER TABLE `ws_student_info` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_users
CREATE TABLE IF NOT EXISTS `ws_users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `register_time` varchar(50) DEFAULT NULL COMMENT '注册时间',
  `state` varchar(50) DEFAULT '0' COMMENT '是否在线 0:离线 1:在线',
  `last_login_time` varchar(50) DEFAULT NULL COMMENT '上次登录时间',
  `last_logout_time` varchar(50) DEFAULT NULL COMMENT '上次登出时间',
  `enable` varchar(50) DEFAULT '1' COMMENT '是否可用 0:不可用  1:可用',
  `speak` varchar(50) DEFAULT '1' COMMENT '是否禁言  0:禁言 1：没有禁言',
  `coin_num` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '积分数量',
  `question1` varchar(400) DEFAULT '' COMMENT '问题1',
  `answer1` varchar(400) DEFAULT '' COMMENT '答案1',
  `question2` varchar(400) DEFAULT '' COMMENT '问题2',
  `answer2` varchar(400) DEFAULT '' COMMENT '答案2',
  `question3` varchar(400) DEFAULT '' COMMENT '问题3',
  `answer3` varchar(400) DEFAULT '' COMMENT '答案3',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COMMENT='用户账号表';

-- Dumping data for table zhdd.ws_users: ~30 rows (approximately)
/*!40000 ALTER TABLE `ws_users` DISABLE KEYS */;
INSERT INTO `ws_users` (`id`, `name`, `password`, `register_time`, `state`, `last_login_time`, `last_logout_time`, `enable`, `speak`, `coin_num`, `question1`, `answer1`, `question2`, `answer2`, `question3`, `answer3`, `create_time`) VALUES
	(1, 'admin', 'MjQzMTBCNkQxNkM1NkZCNzk2M0ZCNEY1REMxQkM0NDk=', '2018-03-22 22:37:07', '0', '2020-05-30 21:54:38', '2020-05-30 21:00:12', '1', '1', 0, '你最喜欢的明星是谁?', '11', '你最喜欢的一首歌是什么?', '22', '你父亲的生日是什么时候', '33', '2018-12-12 10:10:15'),
	(2, 'bbbb', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-23 14:51:50', '0', '2018-03-25 16:47:14', '2018-03-25 15:50:22', '1', '1', 0, '你最喜欢的明星是谁?', 'vv', '你最喜欢的一首歌是什么?', 'rrr', '你父亲的生日是什么时候', 'nn', '2018-12-12 10:10:15'),
	(4, 'mb', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-31 21:55:17', '0', '2018-12-12 16:07:07', '2018-12-12 16:07:08', '1', '1', 0, '你最喜欢的水果是什么?', 'apple', '你的初中在哪里？', '湖北', '你的第一次是什么时候？', '16', '2018-12-12 10:10:15'),
	(5, '小七', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-24 11:03:07', '0', '2018-03-31 23:35:22', '2018-03-31 23:38:03', '1', '1', 0, '你最喜欢的明星是谁?', '用户', '你最喜欢的一首歌是?', '计划', '你的初恋女友是谁?', '哦了', '2018-12-12 10:10:15'),
	(6, '小三', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-24 10:45:15', '0', '2018-03-25 22:28:51', '2018-03-25 22:27:00', '1', '1', 0, '你最喜欢的明星是谁?', '喜鹊', '你最喜欢的一首歌是?', '我的心', '你的初恋女友是谁?', '曹君怡', '2018-12-12 10:10:15'),
	(7, '小五', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-24 10:59:24', '0', '2018-12-13 20:31:29', '2018-12-13 20:31:30', '1', '1', 0, '你最喜欢的明星是谁?', '广告', '你最喜欢的一首歌是?', '刚刚', '你的初恋女友是谁?', '33', '2018-12-12 10:10:15'),
	(8, '小六', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-24 11:01:33', '0', '2018-03-24 11:11:01', '2018-03-24 11:10:46', '1', '1', 0, '你最喜欢的明星是谁?', '22', '你最喜欢的一首歌是?', '33', '你的初恋女友是谁?', '55', '2018-12-12 10:10:15'),
	(9, '小四', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 22:19:23', '0', '2018-03-25 22:28:48', '2018-03-25 22:26:24', '1', '1', 0, '你最喜欢的明星是谁?', '广告', '你最喜欢的一首歌是?', '22', '你的初恋女友是谁?', '6.0', '2018-12-12 10:10:15'),
	(10, '小散', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-22 21:49:11', '0', '2018-03-24 09:13:55', '2018-03-24 09:13:54', '1', '1', 0, '你最喜欢的明星是谁?', '张娜拉', '你最喜欢的一首歌是什么?', '黄昏', '你父亲的生日是什么时候', '6.5', '2018-12-12 10:10:15'),
	(11, '无名1', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 21:30:19', '0', '2019-01-10 12:27:52', '2019-01-10 12:28:02', '1', '1', 0, '你最喜欢的明星是谁?', '不知道', '你最喜欢的一首歌是?', '微微', '你的初恋女友是谁?', '阿阿', '2018-12-12 10:10:15'),
	(12, '无名2', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 21:31:16', '0', '2018-04-01 00:28:38', '2018-04-01 07:19:46', '1', '1', 0, '你最喜欢的明星是谁?', '方法', '你最喜欢的一首歌是?', '广告', '你的初恋女友是谁?', '黄昏', '2018-12-12 10:10:15'),
	(13, '无名3', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 21:32:59', '0', '2020-05-11 18:37:11', '2020-05-11 20:19:56', '1', '1', 0, '你最喜欢的明星是谁?', '国际化', '你最喜欢的一首歌是?', '5鱼', '你的初恋女友是谁?', '偶爱', '2018-12-12 10:10:15'),
	(14, '无名5', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 21:34:28', '0', '2020-05-21 20:29:22', '2019-01-10 12:28:17', '1', '1', 0, '你最喜欢的明星是谁?', '哦哦', '你最喜欢的一首歌是?', '看了', '你的初恋女友是谁?', '鱼体育', '2018-12-12 10:10:15'),
	(15, '无名7', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 21:41:30', '0', '2019-01-26 21:53:32', '2019-01-26 22:01:13', '1', '1', 0, '你最喜欢的明星是谁?', '开个', '你最喜欢的一首歌是?', '提供', '你的初恋女友是谁?', '吧v大', '2018-12-12 10:10:15'),
	(16, '无名9', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 21:43:22', '0', '2019-01-10 12:28:26', '2019-01-10 12:28:36', '1', '1', 0, '你最喜欢的明星是谁?', '提供', '你最喜欢的一首歌是?', '耳朵', '你的初恋女友是谁?', '而非', '2018-12-12 10:10:15'),
	(17, '陈晓', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-23 14:10:55', '0', '2018-03-23 22:53:36', '2018-03-23 22:17:47', '1', '1', 0, '你的初恋女孩叫什么名字?', '你吗', '你母亲的生日是什么时候?', '0621', '你最喜欢的一首歌是什么?', '喊话', '2018-12-12 10:10:15'),
	(18, '黄朝辉', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-22 22:36:37', '0', '2019-01-26 21:53:15', '2019-01-26 22:01:15', '1', '1', 0, '你最喜欢的明星是谁?', '张娜拉', '你最喜欢的一首歌是什么?', '天天', '你父亲的生日是什么时候', '6.0', '2018-12-12 10:10:15'),
	(19, '黄老板', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 22:18:20', '0', '2018-03-25 22:24:51', '2018-03-25 22:24:26', '1', '1', 0, '你最喜欢的明星是谁?', '张娜拉', '你最喜欢的一首歌是?', '我的心', '你的初恋女友是谁?', '不是你', '2018-12-12 10:10:15'),
	(20, '씨발', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-12 09:58:06', '0', '2020-05-30 21:56:34', '2020-05-30 21:57:34', '1', '1', 0, '你最喜欢的水果是什么?', '사과', '你父亲生日是什么时候?', '유원일십사일', '你最想去哪旅行?', '한국', '2018-12-12 10:10:15'),
	(21, '徐志摩', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-12 10:10:50', '0', '2018-12-12 10:16:02', '2018-12-12 10:16:02', '1', '1', 0, '你最喜欢的水果是什么?', '梨子', '你父亲生日是什么时候?', '元旦节', '你最想去哪旅行?', '非洲', '2018-12-12 10:10:50'),
	(22, 'hhh', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-12 13:55:37', '0', '2018-12-12 14:51:08', '2018-12-12 14:51:08', '1', '1', 0, '你最喜欢的水果是什么?', '梨子', '你父亲生日是什么时候?', 'dd', '你最想去哪旅行?', '한국', '2018-12-12 13:55:37'),
	(23, 'aa', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-12 16:38:58', '1', '2020-05-31 10:18:48', '2020-05-31 10:18:37', '1', '1', 11, '你最喜欢的水果是什么?', '带到', '你父亲生日是什么时候?', '带到', '你最想去哪旅行?', '的啊', '2018-12-12 16:38:58'),
	(24, 'bb', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-12 16:39:16', '0', '2020-05-30 20:49:42', '2020-05-30 20:54:46', '1', '1', 0, '你最喜欢的水果是什么?', '的的', '你父亲生日是什么时候?', '都是大神', '你最想去哪旅行?', '都是的', '2018-12-12 16:39:16'),
	(25, 'vb', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-13 15:34:55', '0', '2019-01-26 21:53:44', '2019-01-26 22:01:12', '1', '1', 0, '你最喜欢的水果是什么?', '的的', '你父亲生日是什么时候?', '33', '你最想去哪旅行?', '55', '2018-12-13 15:34:55'),
	(26, 'jkx', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-13 15:35:13', '0', '2018-12-13 15:41:00', '2018-12-13 15:41:00', '1', '1', 0, '你最想去哪旅行?', '中国', '你最想去哪旅行?', '中国', '你怎么看待中国的房价?', '太高了', '2018-12-13 15:35:13'),
	(27, 'zhuhao', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-13 15:35:20', '0', '2018-12-13 15:51:29', '2018-12-13 15:52:18', '1', '1', 0, '你感觉人生操蛋吗?', '操蛋', '你感觉人生操蛋吗?', '操蛋', '你感觉人生操蛋吗?', '操蛋', '2018-12-13 15:35:20'),
	(28, 'good', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-13 15:35:31', '0', '2018-12-13 15:37:52', '2018-12-13 15:37:53', '1', '1', 0, '你的初中在哪里？', '不告诉你', '你父亲生日是什么时候?', '不告诉你', '你最想去哪旅行?', '不告诉你', '2018-12-13 15:35:31'),
	(29, 'gt', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-21 11:42:00', '0', '2018-12-21 11:42:00', NULL, '1', '1', 0, '你最喜欢的水果是什么?', '11', '你父亲生日是什么时候?', '11', '你最想去哪旅行?', '11', '2018-12-21 11:42:01'),
	(30, 'cc', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2020-05-13 13:34:23', '0', '2020-05-13 17:39:25', '2020-05-13 17:39:31', '1', '1', 0, '你最喜欢的水果是什么?', '111', '你父亲生日是什么时候?', '111', '你最想去哪旅行?', '111', '2020-05-13 13:34:23'),
	(31, 'dd', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2020-05-13 13:35:41', '0', '2020-05-16 21:28:59', '2020-05-16 20:13:13', '1', '1', 0, '你最喜欢的水果是什么?', '111', '你父亲生日是什么时候?', '111', '你最想去哪旅行?', '111', '2020-05-13 13:35:41');
/*!40000 ALTER TABLE `ws_users` ENABLE KEYS */;

-- Dumping structure for table zhdd.ws_user_profile
CREATE TABLE IF NOT EXISTS `ws_user_profile` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(10) unsigned NOT NULL COMMENT '用户ID',
  `user_name` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `real_name` varchar(50) NOT NULL DEFAULT '' COMMENT '真实姓名',
  `img` varchar(200) NOT NULL DEFAULT '' COMMENT '头像url',
  `sign` varchar(250) NOT NULL DEFAULT '这人很懒,一点也没留下' COMMENT '个性签名',
  `age` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '年龄',
  `sex` int(11) unsigned NOT NULL DEFAULT '1' COMMENT '性别 1:男 2:女 3:未知',
  `sex_text` varchar(50) NOT NULL DEFAULT '男' COMMENT '性别(文本)',
  `tel` varchar(50) NOT NULL DEFAULT '' COMMENT '电话',
  `address` varchar(150) NOT NULL DEFAULT '' COMMENT '地址',
  `profession` int(11) unsigned NOT NULL DEFAULT '99' COMMENT '职业  1:IT 2:建筑  3:金融  4:个体商户 5:旅游 99:其他',
  `profession_text` varchar(50) NOT NULL DEFAULT '其他' COMMENT '职业(文本)',
  `hobby` int(11) unsigned NOT NULL DEFAULT '99' COMMENT '爱好 1:篮球 2:足球  3:爬山 4:旅游  5:网游  99:其他',
  `hobby_text` varchar(50) NOT NULL DEFAULT '其他' COMMENT '爱好(文本)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='用户简历表';

-- Dumping data for table zhdd.ws_user_profile: ~7 rows (approximately)
/*!40000 ALTER TABLE `ws_user_profile` DISABLE KEYS */;
INSERT INTO `ws_user_profile` (`id`, `user_id`, `user_name`, `real_name`, `img`, `sign`, `age`, `sex`, `sex_text`, `tel`, `address`, `profession`, `profession_text`, `hobby`, `hobby_text`, `create_time`) VALUES
	(6, 24, 'bb', 'BB', 'http://192.168.2.111:8101/headImg/image.jpg', '我很懒   大大 ', 11, 2, '女', '2548745', '韩国首尔', 2, '建筑', 3, '爬山', '2019-01-08 16:22:30'),
	(7, 18, '黄朝辉', '黄CC', '黄朝辉.jpg', '投入和更换一句话', 23, 1, '男', '4344524425', '广东佛山', 5, '旅游', 6, '其他', '2019-01-08 17:47:05'),
	(8, 1, 'admin', 'admin', 'http://localhost:8101/circle/admin.jpg', '这人很懒,一点也没留下', 23, 2, '女', '8888888', '无名岛', 2, '建筑', 4, '旅游', '2019-01-16 11:00:50'),
	(9, 23, 'aa', '我没有名字a啊', 'http://192.168.2.111:8101/headImg/8.jpg', '不喜欢签名', 56, 1, '男', '13888888', '日本东京', 4, '个体商户', 4, '旅游', '2019-01-17 13:17:29'),
	(10, 31, 'dd', '蛋蛋', 'http://192.168.2.111:8101/headImg/2.jpg', '这人很懒,一点也没留下', 23, 1, '男', '16605141987', '韩国釜山', 4, '个体商户', 4, '旅游', '2020-05-13 13:35:42'),
	(11, 30, 'cc', '哇卡卡打开', 'http://192.168.2.111:8101/headImg/u=3337455883,2495110955&fm=214&gp=0.jpg', '好累', 45, 1, '男', '7415455', '韩国龙山', 2, '建筑', 6, '其他', '2020-05-13 14:15:20'),
	(12, 20, '씨발', '西巴', 'http://localhost:8101/headImg/6849ae90-3b33-4a9b-ba84-52705a92229d.gif', '这人很懒,一点也没留下', 30, 1, '男', '16605141987', '日本横滨', 2, '建筑', 2, '足球', '2020-05-30 21:56:20');
/*!40000 ALTER TABLE `ws_user_profile` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
