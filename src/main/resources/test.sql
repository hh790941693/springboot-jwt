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


-- Dumping database structure for test
CREATE DATABASE IF NOT EXISTS `test` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `test`;

-- Dumping structure for table test.sys_config
CREATE TABLE IF NOT EXISTS `sys_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `k` varchar(100) DEFAULT NULL COMMENT '键',
  `v` varchar(1000) DEFAULT NULL COMMENT '值',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `kv_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- Dumping data for table test.sys_config: ~0 rows (approximately)
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` (`id`, `k`, `v`, `remark`, `create_time`, `kv_type`) VALUES
	(2, 'oss_qiniu', '{"AccessKey" : "8-HMj9EgGNIP-xuOCpSzTn-OMyGOFtR3TxLdn4Uu","SecretKey" : "SjpGg3V6PsMdJgn42PeEd5Ik-6aNyuwdqV5CPM6A","bucket" : "ifast","AccessUrl" : "http://p6r7ke2jc.bkt.clouddn.com/"}', '七牛对象存储配置', '2018-04-06 14:31:26', 4300),
	(3, 'author', 'admin', '代码生成器配置', '2018-05-27 19:57:04', 4401),
	(4, 'email', 'anonymous@anonymous.com', '代码生成器配置', '2018-05-27 19:57:04', 4401),
	(5, 'package', 'com.ifast.entryManage', '代码生成器配置', '2018-05-27 19:57:04', 4401),
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

-- Dumping structure for table test.t_permission
CREATE TABLE IF NOT EXISTS `t_permission` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(50) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table test.t_permission: ~2 rows (approximately)
/*!40000 ALTER TABLE `t_permission` DISABLE KEYS */;
INSERT INTO `t_permission` (`id`, `permission_name`, `create_time`) VALUES
	(1, 'add', '2020-03-12 15:41:48'),
	(2, 'delete', '2020-03-12 16:00:10');
/*!40000 ALTER TABLE `t_permission` ENABLE KEYS */;

-- Dumping structure for table test.t_role
CREATE TABLE IF NOT EXISTS `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table test.t_role: ~2 rows (approximately)
/*!40000 ALTER TABLE `t_role` DISABLE KEYS */;
INSERT INTO `t_role` (`id`, `rolename`, `create_time`) VALUES
	(1, 'admin', '2020-03-12 14:54:56'),
	(2, 'user', '2020-03-12 14:55:01');
/*!40000 ALTER TABLE `t_role` ENABLE KEYS */;

-- Dumping structure for table test.t_role_permission
CREATE TABLE IF NOT EXISTS `t_role_permission` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(10) unsigned NOT NULL,
  `role_name` varchar(200) NOT NULL DEFAULT '',
  `permission_id` int(10) unsigned NOT NULL,
  `permission_name` varchar(200) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table test.t_role_permission: ~1 rows (approximately)
/*!40000 ALTER TABLE `t_role_permission` DISABLE KEYS */;
INSERT INTO `t_role_permission` (`id`, `role_id`, `role_name`, `permission_id`, `permission_name`) VALUES
	(1, 1, 'admin', 1, 'add');
/*!40000 ALTER TABLE `t_role_permission` ENABLE KEYS */;

-- Dumping structure for table test.t_user
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` int(45) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- Dumping data for table test.t_user: ~3 rows (approximately)
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` (`id`, `username`, `password`, `create_time`, `update_time`) VALUES
	(1, '张三', '123456', '2020-03-12 13:46:32', '2020-03-12 13:46:32'),
	(2, '李四', '123456', '2020-03-12 13:46:32', '2020-03-12 13:46:32'),
	(3, '王五', '123456', '2020-03-12 13:46:32', '2020-03-12 13:46:32');
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;

-- Dumping structure for table test.t_user_role
CREATE TABLE IF NOT EXISTS `t_user_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `username` varchar(50) NOT NULL DEFAULT '',
  `role_id` int(10) unsigned NOT NULL,
  `role_name` varchar(50) NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table test.t_user_role: ~2 rows (approximately)
/*!40000 ALTER TABLE `t_user_role` DISABLE KEYS */;
INSERT INTO `t_user_role` (`id`, `user_id`, `username`, `role_id`, `role_name`, `create_time`) VALUES
	(1, 1, '张三', 1, 'admin', '2020-03-12 14:55:34'),
	(2, 1, '张三', 2, 'user', '2020-03-12 14:55:53');
/*!40000 ALTER TABLE `t_user_role` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
