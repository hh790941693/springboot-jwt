-- --------------------------------------------------------
-- 主机:                           101.132.167.61
-- 服务器版本:                        5.7.35 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 zhdd 的数据库结构
CREATE DATABASE IF NOT EXISTS `zhdd` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `zhdd`;

-- 导出  表 zhdd.sp_favorite 结构
CREATE TABLE IF NOT EXISTS `sp_favorite` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `subject_id` varchar(100) NOT NULL COMMENT '收藏物id',
  `subject_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '收藏物类型 1:商品 2:店铺',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1:已收藏 2:未收藏',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8 COMMENT='收藏表';

-- 正在导出表  zhdd.sp_favorite 的数据：~45 rows (大约)
/*!40000 ALTER TABLE `sp_favorite` DISABLE KEYS */;
INSERT INTO `sp_favorite` (`id`, `user_id`, `subject_id`, `subject_type`, `status`, `create_time`, `update_time`) VALUES
	(1, 23, 'gd_865278eda76e4973ba538a8e25dcaffe', 1, 2, '2021-06-11 20:36:23', '2021-06-26 16:51:50'),
	(2, 23, 'mer_02c3e96c2dc546869b1744910239c0f4', 2, 2, '2021-06-11 20:36:34', '2021-06-27 13:55:18'),
	(3, 23, 'mer_ae7f37e85816499bbb1aa4951bddef58', 2, 1, '2021-06-11 21:28:54', '2021-06-13 16:54:46'),
	(4, 23, 'gd_a9d6933261b9466c8964d5a463ec1175', 1, 1, '2021-06-11 21:29:29', '2021-06-13 15:31:05'),
	(5, 23, 'gd_b8046322522c41bf8bcbe296e2c7c1da', 1, 2, '2021-06-11 21:36:14', '2021-07-17 08:47:58'),
	(6, 23, 'gd_092e6fc1c6b942429da608a76b19006a', 1, 2, '2021-06-13 15:31:12', '2021-06-17 21:58:03'),
	(7, 23, 'gd_e34aecce0a5946dabfa907f4a6a335c8', 1, 1, '2021-06-13 15:31:15', '2021-06-13 15:31:15'),
	(8, 23, 'gd_2cbdfb93bfd940b5a4ac20f8761fd12b', 1, 1, '2021-06-13 15:31:25', '2021-06-13 15:31:25'),
	(9, 23, 'gd_4eabab3c9a304d779aa1e570d8b44762', 1, 1, '2021-06-13 15:31:28', '2021-06-17 18:27:23'),
	(10, 23, 'gd_039a2b8074c34ad8912fe20683225a90', 1, 1, '2021-06-13 15:31:31', '2021-10-13 08:44:54'),
	(11, 23, 'gd_233cc264f0534688ae48181fa1c67bd3', 1, 1, '2021-06-13 15:31:34', '2021-06-13 15:31:34'),
	(12, 23, 'gd_57b38380fcc94571b2f29931f03acb99', 1, 2, '2021-06-13 15:31:37', '2021-07-25 21:00:40'),
	(13, 24, 'gd_092e6fc1c6b942429da608a76b19006a', 1, 1, '2021-06-14 16:04:10', '2021-06-14 16:04:10'),
	(14, 24, 'mer_ae7f37e85816499bbb1aa4951bddef58', 2, 1, '2021-06-14 16:04:23', '2021-06-14 16:04:23'),
	(15, 30, 'gd_4eabab3c9a304d779aa1e570d8b44762', 1, 1, '2021-06-16 15:02:56', '2021-06-16 15:02:56'),
	(16, 30, 'mer_ae7f37e85816499bbb1aa4951bddef58', 2, 1, '2021-06-16 15:03:23', '2021-06-16 15:03:23'),
	(17, 30, 'gd_b8046322522c41bf8bcbe296e2c7c1da', 1, 1, '2021-06-17 11:19:15', '2021-06-17 11:19:15'),
	(18, 23, 'mer_b231ea39d10149b38663d0dc43d5f0c7', 2, 1, '2021-06-27 18:59:35', '2021-06-27 18:59:35'),
	(19, 31, 'mer_b231ea39d10149b38663d0dc43d5f0c7', 2, 1, '2021-06-27 19:29:07', '2021-06-27 19:29:07'),
	(20, 31, 'gd_865278eda76e4973ba538a8e25dcaffe', 1, 1, '2021-06-27 19:29:21', '2021-06-27 19:29:21'),
	(21, 31, 'gd_59342bceeb224bfeb5907cfb97309124', 1, 1, '2021-06-28 22:10:59', '2021-06-28 22:10:59'),
	(22, 31, 'gd_092e6fc1c6b942429da608a76b19006a', 1, 1, '2021-06-28 22:11:02', '2021-06-28 22:11:02'),
	(23, 31, 'gd_b8046322522c41bf8bcbe296e2c7c1da', 1, 1, '2021-06-28 22:11:07', '2021-07-02 22:59:13'),
	(24, 31, 'gd_2cbdfb93bfd940b5a4ac20f8761fd12b', 1, 1, '2021-06-28 22:11:10', '2021-06-28 22:11:10'),
	(25, 31, 'gd_57b38380fcc94571b2f29931f03acb99', 1, 1, '2021-06-28 22:11:12', '2021-06-28 22:11:12'),
	(26, 31, 'gd_ebbc6d586912492b8ad11fef0194d8c6', 1, 1, '2021-06-28 22:11:14', '2021-06-28 22:11:14'),
	(27, 31, 'gd_e34aecce0a5946dabfa907f4a6a335c8', 1, 1, '2021-06-28 22:11:17', '2021-06-28 22:11:17'),
	(28, 31, 'gd_233cc264f0534688ae48181fa1c67bd3', 1, 1, '2021-06-28 22:11:21', '2021-06-28 22:11:21'),
	(29, 31, 'gd_a9d6933261b9466c8964d5a463ec1175', 1, 1, '2021-06-28 22:11:24', '2021-06-28 22:11:24'),
	(30, 31, 'gd_4eabab3c9a304d779aa1e570d8b44762', 1, 1, '2021-06-28 22:11:30', '2021-06-28 22:11:30'),
	(31, 31, 'gd_f24701f40b704157879d499610de5c38', 1, 1, '2021-06-28 22:11:32', '2021-06-28 22:11:32'),
	(32, 31, 'gd_670710e48c23486ab4f16d87538466b0', 1, 1, '2021-06-28 22:11:34', '2021-06-28 22:11:34'),
	(33, 31, 'gd_387601498d174738a2c2f2332e0ccdd8', 1, 1, '2021-06-28 22:11:36', '2021-06-28 22:11:36'),
	(34, 31, 'gd_5fda6d842779448abc18d6993d316564', 1, 1, '2021-06-28 22:11:38', '2021-06-28 22:11:38'),
	(35, 31, 'gd_039a2b8074c34ad8912fe20683225a90', 1, 1, '2021-06-28 22:11:40', '2021-06-28 22:11:40'),
	(36, 31, 'gd_eb6b692874d644e2a717494203036fa8', 1, 1, '2021-06-28 22:11:43', '2021-06-28 22:11:43'),
	(37, 31, 'gd_fb208d124d3f4c369ff85340d3732508', 1, 1, '2021-06-28 22:11:46', '2021-06-28 22:11:46'),
	(38, 31, 'gd_8eee4411aeb342e0bea449ee33395bb2', 1, 1, '2021-06-28 22:11:48', '2021-06-28 22:11:48'),
	(39, 31, 'mer_02c3e96c2dc546869b1744910239c0f4', 2, 1, '2021-06-29 21:21:03', '2021-06-29 21:21:03'),
	(40, 23, 'gd_eb6b692874d644e2a717494203036fa8', 1, 1, '2021-07-04 20:12:23', '2021-07-04 20:12:23'),
	(41, 41, 'gd_233cc264f0534688ae48181fa1c67bd3', 1, 1, '2021-07-19 10:08:16', '2021-07-19 10:08:16'),
	(42, 41, 'mer_02c3e96c2dc546869b1744910239c0f4', 2, 1, '2021-07-19 10:09:03', '2021-07-19 10:09:03'),
	(43, 1, 'mer_1666a982f4e349c290d5c2d1c29c98ad', 2, 1, '2021-07-25 16:53:17', '2021-07-25 16:53:17'),
	(44, 23, 'gd_12fe7e58b7ed4efcafee21268c393497', 1, 1, '2021-07-25 20:42:31', '2021-07-25 20:42:31'),
	(45, 23, 'mer_1666a982f4e349c290d5c2d1c29c98ad', 2, 1, '2021-07-25 20:42:48', '2021-07-25 20:42:48'),
	(46, 1, 'gd_670710e48c23486ab4f16d87538466b0', 1, 2, '2021-11-11 14:43:44', '2021-11-11 14:43:58'),
	(47, 1, 'gd_59342bceeb224bfeb5907cfb97309124', 1, 1, '2021-12-03 17:52:50', '2021-12-03 17:52:50');
/*!40000 ALTER TABLE `sp_favorite` ENABLE KEYS */;

-- 导出  表 zhdd.sp_goods 结构
CREATE TABLE IF NOT EXISTS `sp_goods` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `goods_id` varchar(100) NOT NULL COMMENT '商品id',
  `name` varchar(50) NOT NULL COMMENT '商品名称',
  `brief` varchar(200) NOT NULL COMMENT '商品简介',
  `description` text COMMENT '商品H5描述',
  `place` varchar(20) NOT NULL COMMENT '商品生产地',
  `goods_type_id` varchar(100) NOT NULL COMMENT '商品类型id',
  `merchant_id` varchar(100) NOT NULL COMMENT '归属店铺id',
  `stock_num` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '库存数',
  `sale_number` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '销量',
  `original_price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '原价',
  `sale_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '售价',
  `unit_name` varchar(10) NOT NULL COMMENT '商品单位',
  `back_image` varchar(200) NOT NULL COMMENT '封面图片',
  `image1` varchar(200) NOT NULL COMMENT '商品图片1',
  `image2` varchar(200) NOT NULL COMMENT '商品图片2',
  `image3` varchar(200) NOT NULL COMMENT '商品图片3',
  `image4` varchar(200) NOT NULL COMMENT '商品图片4',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '状态 0:已删除 1:已上架 2:已下架',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sp_goods_unique_index` (`goods_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='商品表';

-- 正在导出表  zhdd.sp_goods 的数据：~24 rows (大约)
/*!40000 ALTER TABLE `sp_goods` DISABLE KEYS */;
INSERT INTO `sp_goods` (`id`, `goods_id`, `name`, `brief`, `description`, `place`, `goods_type_id`, `merchant_id`, `stock_num`, `sale_number`, `original_price`, `sale_price`, `unit_name`, `back_image`, `image1`, `image2`, `image3`, `image4`, `status`, `create_time`, `update_time`) VALUES
	(1, 'gd_b8046322522c41bf8bcbe296e2c7c1da', '红富士苹果', '非常好吃的苹果', '<p style="text-align: center;"><span data-fr-verified="true" style="font-size: 40px;">红富士苹果</span></p><p style="text-align: left;"><span data-fr-verified="true" style="font-size: 15px;">​&nbsp; &nbsp; 很甜很甜的苹果，产自日本富士山，天然的，无任何添加剂。</span><span data-fr-verified="true" style="font-size: 40px;"><br></span></p><p><img class="fr-fin" data-fr-image-preview="false" alt="Image title" src="http://127.0.0.1:8101/goodsDetail/cb8e714a-d34e-49f9-a083-4dbc9542d549.jpg" style="max-width: 100%;" width="595"></p><p style="text-align: left;"><span data-fr-verified="true" style="font-size: 15px;">&nbsp; &nbsp; 传说很久以前，在一个集市上，一位商人路边吃了一个苹果，遂喜欢上了这种味道。<br></span></p><p><img class="fr-fin" data-fr-image-preview="false" alt="Image title" src="http://127.0.0.1:8101/goodsDetail/60e2b9f0-f634-4553-ad3e-e368b5897790.jpg" style="max-width: 100%;"></p><p style="text-align: left;"><span data-fr-verified="true" style="font-size: 15px;"><br></span></p>', '浙江', 'gt_60bd03d5c09743ab82174315d125883b', 'mer_02c3e96c2dc546869b1744910239c0f4', 186, 14, 10.50, 6.00, '斤', 'http://101.132.167.61:8101/goods/7fe2cecb-0308-4d87-ba15-319c9fb72498.jpg', 'http://101.132.167.61:8101/goods/318e4fb6-3a2a-4ff1-a41c-a4397c416a32.jpg', 'http://101.132.167.61:8101/goods/4cc96beb-a768-4430-81d0-d8611a344422.jpg', 'http://101.132.167.61:8101/goods/4981c9fe-241a-4df2-ad24-6400435e03d1.jpg', 'http://101.132.167.61:8101/goods/84000fc2-1e47-4758-b491-d59ff3ef9273.jpg', 1, '2021-06-10 21:01:45', '2021-10-17 16:13:03'),
	(2, 'gd_092e6fc1c6b942429da608a76b19006a', '香蕉', '很嫩很好吃', '<p>很香很嫩的香蕉哦</p>', '云南', 'gt_60bd03d5c09743ab82174315d125883b', 'mer_02c3e96c2dc546869b1744910239c0f4', 92, 8, 8.01, 5.25, '台', 'http://101.132.167.61:8101/goods/31eb9533-7c14-4134-a86b-5aa0f76d1781.jpg', 'http://101.132.167.61:8101/goods/c3cc2b49-4d58-46c3-bcf0-f3aa84721d5b.jpg', 'http://101.132.167.61:8101/goods/34b0bfd0-39a9-491f-b250-bd7f3f42e598.jpg', 'http://101.132.167.61:8101/goods/cca18580-cc79-45c6-83f2-8ebdfb6b059c.jpg', 'http://101.132.167.61:8101/goods/528837ec-8329-4674-ae82-e32d006c36c0.jpg', 1, '2021-06-10 21:04:12', '2021-10-17 16:13:03'),
	(3, 'gd_865278eda76e4973ba538a8e25dcaffe', '8424西瓜', '新疆产甜西瓜', NULL, '新疆', 'gt_60bd03d5c09743ab82174315d125883b', 'mer_02c3e96c2dc546869b1744910239c0f4', 842, 14, 2.50, 2.00, '斤', 'http://101.132.167.61:8101/goods/23a32c2a-c2e9-4456-9c8d-f775d7c9ea51.jpg', 'http://101.132.167.61:8101/goods/b4811278-7557-4ae7-a222-a928423a6b89.jpg', 'http://101.132.167.61:8101/goods/39950454-0bc3-4eea-a0af-8e2d188805b9.jpg', 'http://101.132.167.61:8101/goods/acb3dac4-d7da-4390-a4a4-46962480ad8f.jpg', 'http://101.132.167.61:8101/goods/f4df93af-107f-498c-ae2f-29c037ef5748.jpg', 1, '2021-06-10 21:45:57', '2021-10-17 16:13:03'),
	(4, 'gd_a9d6933261b9466c8964d5a463ec1175', '泰国空运榴莲', '泰国空运过来的榴莲', NULL, '泰国', 'gt_60bd03d5c09743ab82174315d125883b', 'mer_02c3e96c2dc546869b1744910239c0f4', 118, 2, 89.00, 75.00, '斤', 'http://101.132.167.61:8101/goods/f795c425-110e-4d6b-abfd-f3dc4d492e2e.jpg', 'http://101.132.167.61:8101/goods/34c91aa0-17c1-4ac3-a0c4-2ed1c8121445.jpg', 'http://101.132.167.61:8101/goods/56c491a5-8c12-4f8c-a419-1a89b03b6d0d.jpg', 'http://101.132.167.61:8101/goods/66d0a737-ad1c-4422-9145-37c0135b63e7.jpg', 'http://101.132.167.61:8101/goods/8aca2ae4-7461-4eeb-b959-4f660b4b6a52.jpg', 1, '2021-06-10 21:49:04', '2021-10-17 16:13:03'),
	(5, 'gd_e34aecce0a5946dabfa907f4a6a335c8', '火龙果', '非常甜的火龙果', NULL, '湖南', 'gt_60bd03d5c09743ab82174315d125883b', 'mer_02c3e96c2dc546869b1744910239c0f4', 396, 14, 15.00, 8.00, '斤', 'http://101.132.167.61:8101/goods/fd7d0fc7-7aa4-4deb-8686-63774e8c65bd.jpg', 'http://101.132.167.61:8101/goods/db5d54be-0c59-405f-90ae-2a72f17de736.jpg', 'http://101.132.167.61:8101/goods/385eaba6-0107-4e32-8af7-4bb7cd07e5d7.jpg', 'http://101.132.167.61:8101/goods/9557ef2a-a70c-4aaa-94d4-63e585688904.jpg', 'http://101.132.167.61:8101/goods/aa5cc66e-fdae-41f8-8a33-53a07aabecb6.jpg', 1, '2021-06-10 21:52:28', '2021-07-04 17:33:06'),
	(6, 'gd_2cbdfb93bfd940b5a4ac20f8761fd12b', '大鲍鱼', '大鲍鱼很好吃', NULL, '青岛', 'gt_c7364f84714749149e139cbe162e8d29', 'mer_ae7f37e85816499bbb1aa4951bddef58', 88, 12, 68.00, 57.00, '斤', 'http://101.132.167.61:8101/goods/b31ed3ce-835e-4c2e-b66f-d69f05d581db.jpg', 'http://101.132.167.61:8101/goods/a66b2a47-580b-4dd3-93dc-dc291d52e4fc.jpg', 'http://101.132.167.61:8101/goods/a465f4f6-39e4-41c1-8ca3-63711f308b1d.jpg', 'http://101.132.167.61:8101/goods/b35f4f32-ccf8-49de-a469-48cbc9437296.jpg', 'http://101.132.167.61:8101/goods/0c4d7987-9abf-48f6-9782-aebd24be1e12.jpg', 1, '2021-06-10 22:09:58', '2021-10-17 16:13:03'),
	(7, 'gd_4eabab3c9a304d779aa1e570d8b44762', '深海大龙虾', '波士顿大龙虾', NULL, '波士顿', 'gt_c7364f84714749149e139cbe162e8d29', 'mer_ae7f37e85816499bbb1aa4951bddef58', 318, 6, 99.00, 87.00, '斤', 'http://101.132.167.61:8101/goods/1c354f5f-6410-4269-b326-a9e308acc0d5.jpg', 'http://101.132.167.61:8101/goods/46a018ad-f881-47c5-b0f4-45d43391daec.jpg', 'http://101.132.167.61:8101/goods/d5e378d3-ede8-4a88-a628-b30898660f3b.jpg', 'http://101.132.167.61:8101/goods/ec1b99b6-9066-4cda-b43c-36a14044dc0d.jpg', 'http://101.132.167.61:8101/goods/24f57881-eb53-4d0f-80bc-850b5915163b.jpg', 1, '2021-06-10 22:12:01', '2021-10-17 16:13:03'),
	(8, 'gd_039a2b8074c34ad8912fe20683225a90', '帝王蟹', '很大的帝王蟹', NULL, '阿根廷', 'gt_c7364f84714749149e139cbe162e8d29', 'mer_ae7f37e85816499bbb1aa4951bddef58', 299, 4, 110.00, 102.00, '斤', 'http://101.132.167.61:8101/goods/aae1d812-9ca1-4c99-8320-5071def6ebc9.jpg', 'http://101.132.167.61:8101/goods/f9baa7dd-d46c-485c-a89b-66af353cb11e.jpg', 'http://101.132.167.61:8101/goods/556aeae2-cbaf-4179-a57d-f5f599228151.jpg', 'http://101.132.167.61:8101/goods/250d4172-0683-4e9a-9543-b8b6e4f5b9c8.jpg', 'http://101.132.167.61:8101/goods/3e92931e-f4e8-4728-8991-b7924eb60411.jpg', 1, '2021-06-10 22:12:55', '2021-07-06 21:08:14'),
	(9, 'gd_233cc264f0534688ae48181fa1c67bd3', '海参', '软软的海参', NULL, '海南', 'gt_c7364f84714749149e139cbe162e8d29', 'mer_ae7f37e85816499bbb1aa4951bddef58', 124, 16, 65.00, 60.00, '斤', 'http://101.132.167.61:8101/goods/ecca221e-09d7-41e4-a373-5ce865ed5303.jpg', 'http://101.132.167.61:8101/goods/c33c75f9-9e46-4d8d-8633-153a71c9e3e5.jpg', 'http://101.132.167.61:8101/goods/3f7c186c-169b-453b-9b53-0bbf733df904.jpg', 'http://101.132.167.61:8101/goods/15a2473e-e460-41f8-854b-ed4099313998.jpg', 'http://101.132.167.61:8101/goods/96b29c28-eb2a-4195-9cdc-267b2dccfa66.jpg', 1, '2021-06-10 22:13:46', '2021-10-17 16:13:03'),
	(10, 'gd_57b38380fcc94571b2f29931f03acb99', '扇贝', '蒜泥扇贝', NULL, '台湾', 'gt_c7364f84714749149e139cbe162e8d29', 'mer_ae7f37e85816499bbb1aa4951bddef58', 428, 4, 40.00, 27.00, '斤', 'http://101.132.167.61:8101/goods/b9bbc4f4-fa68-4ecd-984b-f86adf494611.jpg', 'http://101.132.167.61:8101/goods/121ec583-c3d2-406c-9c38-82440b158ec7.jpg', 'http://101.132.167.61:8101/goods/81e660eb-ef0f-496d-be75-d08c01addd8c.jpg', 'http://101.132.167.61:8101/goods/0ef573c9-6bb8-43a2-9b59-5f05e7ddb5cb.jpg', 'http://101.132.167.61:8101/goods/9073f2e6-4e7d-413c-8ab7-674e8182918c.jpg', 1, '2021-06-10 22:15:51', '2021-10-17 16:13:03'),
	(11, 'gd_59342bceeb224bfeb5907cfb97309124', '桃子', '海南的桃子', NULL, '海口', 'gt_60bd03d5c09743ab82174315d125883b', 'mer_02c3e96c2dc546869b1744910239c0f4', 489, 11, 10.00, 5.00, '斤', 'http://101.132.167.61:8101/goods/f395e3b0-06e3-482f-8211-4cf6e9d569b3.jpg', 'http://101.132.167.61:8101/goods/b8af7860-f515-4ca3-b480-b70a0fbc5b1a.jpg', 'http://101.132.167.61:8101/goods/3b24b752-02bc-4438-8774-038edfa5b71b.jpg', 'http://101.132.167.61:8101/goods/0fff663c-d3b2-4932-8875-edd4a17f9d63.jpg', 'http://101.132.167.61:8101/goods/01685fb8-5f1c-4fab-8075-47d2d295757a.jpg', 1, '2021-06-27 12:04:17', '2021-10-17 16:13:03'),
	(12, 'gd_ebbc6d586912492b8ad11fef0194d8c6', '橘子', '四川的橘子', NULL, '四川', 'gt_60bd03d5c09743ab82174315d125883b', 'mer_02c3e96c2dc546869b1744910239c0f4', 93, 7, 15.00, 8.00, '斤', 'http://101.132.167.61:8101/goods/751cb36a-0fb8-4796-be97-e94d72b06db4.jpg', 'http://101.132.167.61:8101/goods/a7ce1517-90e2-4dea-bf39-94cfcec5ff81.jpg', 'http://101.132.167.61:8101/goods/51cede01-e673-4289-956a-96ab3248fd5d.jpg', 'http://101.132.167.61:8101/goods/0913d04b-3f44-4261-aad7-86f689ff3aed.jpg', 'http://101.132.167.61:8101/goods/852c19a4-62e3-4b4b-b153-49702584df68.jpg', 1, '2021-06-27 12:05:01', '2021-07-05 21:59:23'),
	(13, 'gd_eb6b692874d644e2a717494203036fa8', '华硕笔记本', '250G固态硬盘+2T机械硬盘', NULL, '台湾', 'gt_6d519bcb156946d48dd35ce130d67ecb', 'mer_b231ea39d10149b38663d0dc43d5f0c7', 0, 5, 5999.00, 4999.00, '台', 'http://101.132.167.61:8101/goods/8fb8a1be-b9f6-4248-9212-a3e211282e7e.jpg', 'http://101.132.167.61:8101/goods/1b8b0d68-6974-43b9-afa4-37f8f331af37.jpg', 'http://101.132.167.61:8101/goods/47db3042-a5cf-4c8f-a8b9-4c9c3a15f77f.jpg', 'http://101.132.167.61:8101/goods/5a6d4561-b2b7-4e56-b316-08168e463d01.jpg', 'http://101.132.167.61:8101/goods/4abd3b36-863b-45a3-8a2c-a7c6aa3b55e8.jpg', 1, '2021-06-27 18:38:10', '2021-07-03 14:39:39'),
	(14, 'gd_670710e48c23486ab4f16d87538466b0', '美的冰箱', '美的冰箱，你值得拥有', NULL, '杭州', 'gt_6d519bcb156946d48dd35ce130d67ecb', 'mer_b231ea39d10149b38663d0dc43d5f0c7', 308, 17, 3999.00, 2998.00, '台', 'http://101.132.167.61:8101/goods/830943a3-0cbd-4ac7-9882-93edba89c636.jpg', 'http://101.132.167.61:8101/goods/9c029850-c04d-4c1e-a081-b783fff3bb3a.jpg', 'http://101.132.167.61:8101/goods/c9c3b445-e8a9-4683-8119-7fdffa036fb1.jpg', 'http://101.132.167.61:8101/goods/37787fcc-f007-4ace-bf77-2dc7ec20e970.jpg', 'http://101.132.167.61:8101/goods/44d4ad5f-0914-4b04-ba1d-b9285efc9be4.jpg', 1, '2021-06-27 18:40:28', '2021-11-20 12:15:17'),
	(15, 'gd_fb208d124d3f4c369ff85340d3732508', '夏普电视', '75寸、2480*2100高清分辨率电视机', NULL, '日本', 'gt_6d519bcb156946d48dd35ce130d67ecb', 'mer_b231ea39d10149b38663d0dc43d5f0c7', 652, 0, 8999.00, 6999.00, '台', 'http://101.132.167.61:8101/goods/fc80acb0-834f-4e07-a093-5b6bdd87c30c.jpg', 'http://101.132.167.61:8101/goods/bc8e8982-6760-47c3-9962-dbe8add842c1.jpg', 'http://101.132.167.61:8101/goods/d73c63ee-d49e-4a76-b41f-1b516a6d7c40.jpg', 'http://101.132.167.61:8101/goods/f6579962-cd30-4133-8caf-b1ad8ce3fc75.jpg', 'http://101.132.167.61:8101/goods/2b7f2d4b-3157-407e-85cb-b39b769088f5.jpg', 1, '2021-06-27 18:41:54', '2021-06-27 18:56:24'),
	(16, 'gd_f24701f40b704157879d499610de5c38', '坐地电风扇', '夏天的必备品', NULL, '湖南', 'gt_6d519bcb156946d48dd35ce130d67ecb', 'mer_b231ea39d10149b38663d0dc43d5f0c7', 994, 6, 299.00, 99.00, '台', 'http://101.132.167.61:8101/goods/5ca83872-2ebe-4f91-acd3-045b699ed8c7.jpg', 'http://101.132.167.61:8101/goods/916d02a1-2178-45b4-90b4-0cbd8eaf40b9.jpg', 'http://101.132.167.61:8101/goods/7acffccb-aff3-403c-8360-ac67384ddd57.jpg', 'http://101.132.167.61:8101/goods/133807ac-899b-46b3-a934-d351ce24b9ff.jpg', 'http://101.132.167.61:8101/goods/7f724254-ea16-4fb4-8c5b-3289a72d37c3.jpg', 1, '2021-06-27 18:42:53', '2021-07-04 19:02:42'),
	(17, 'gd_8eee4411aeb342e0bea449ee33395bb2', '格力空调', '格力造', NULL, '浙江', 'gt_6d519bcb156946d48dd35ce130d67ecb', 'mer_b231ea39d10149b38663d0dc43d5f0c7', 440, 13, 3999.00, 2999.00, '台', 'http://101.132.167.61:8101/goods/92459271-c069-4a40-a8c7-e49bb2fa1d72.jpg', 'http://101.132.167.61:8101/goods/62672e82-3637-4ddb-8f33-c772a6a2faf9.jpg', 'http://101.132.167.61:8101/goods/b6d0b9ee-4268-453e-9b0a-5385183d2bbb.jpg', 'http://101.132.167.61:8101/goods/e07ba59f-f60a-4e24-b4cb-52de56a2b57a.jpg', 'http://101.132.167.61:8101/goods/37840a76-6fef-46e4-a1a3-7429a3602df4.jpg', 1, '2021-06-27 18:43:50', '2022-01-02 01:35:22'),
	(18, 'gd_387601498d174738a2c2f2332e0ccdd8', '美的热水器', '用美的，幸福一生。', NULL, '宁波', 'gt_6d519bcb156946d48dd35ce130d67ecb', 'mer_b231ea39d10149b38663d0dc43d5f0c7', 358, 4, 2999.00, 1999.00, '台', 'http://101.132.167.61:8101/goods/cd284407-2867-4ca1-9fe5-28651aba69f7.jpg', 'http://101.132.167.61:8101/goods/daac65ff-df2a-4734-b8b9-38c895f31ac4.jpg', 'http://101.132.167.61:8101/goods/d7a1d83f-30c8-40cb-91c2-5fcda335aeac.jpg', 'http://101.132.167.61:8101/goods/42a1524f-66b3-4478-83cd-34349040d7ce.jpg', 'http://101.132.167.61:8101/goods/06ec420e-6070-44f6-be8b-64b236fd84f7.jpg', 1, '2021-06-27 18:44:57', '2021-10-17 16:13:03'),
	(19, 'gd_5fda6d842779448abc18d6993d316564', '海尔洗衣机', '解放双手，悠闲人生。', NULL, '江苏', 'gt_6d519bcb156946d48dd35ce130d67ecb', 'mer_b231ea39d10149b38663d0dc43d5f0c7', 540, 2, 1999.00, 998.00, '台', 'http://101.132.167.61:8101/goods/2f91eb22-7be3-4cbe-8aa5-f69ee85f475b.jpg', 'http://101.132.167.61:8101/goods/6469bcba-a1ec-4c7d-ad5b-0dc9266ea187.jpg', 'http://101.132.167.61:8101/goods/08a556cf-5c4b-4174-b578-f3ea9b3be596.jpg', 'http://101.132.167.61:8101/goods/27edc25a-c521-4cee-9d69-be96a47d1af4.jpg', 'http://101.132.167.61:8101/goods/728b2459-8074-493d-9d90-60273e8947b1.jpg', 1, '2021-06-27 18:45:56', '2021-07-04 19:02:42'),
	(20, 'gd_a2bd2f5db86d4056b8dbb386de4ff3dd', '한국 맛있는 불고기', '너무 너무 맛있는 불고기,매주 과 먹으면 좋은 날 누리하자', '<p>이리오세요!!!!!</p>', '서울특별시', 'gt_2d2391cde56b4ce7a349250ade608ada', 'mer_61ec5645d72b41b9808bb4ab60bb3afc', 315, 5, 139.50, 99.50, '千克', 'http://101.132.167.61:8101/goods/8e81e338-53d3-4c34-baff-ecc1055d35f4.jpg', 'http://101.132.167.61:8101/goods/09deb359-4f82-4465-b111-41570c0782c4.jpg', 'http://101.132.167.61:8101/goods/902af47b-f53f-4447-a247-c87535989e4f.jpg', 'http://101.132.167.61:8101/goods/7d1c8fe1-4b0a-4dc1-b11f-a3e17b9d1d96.jpg', 'http://101.132.167.61:8101/goods/1eb1464a-1b80-4f7f-9ad8-91dfd1a6ff48.jpg', 1, '2021-07-20 15:38:55', '2021-10-13 08:47:39'),
	(21, 'gd_45c1d5731d4d43aead553e792583d13a', '方便面', '香脆的方便面。', '<p>非常好吃，值得购买。</p>', '柳州', 'gt_03c6ebc9c8b64f39817decec0909b24f', 'mer_1666a982f4e349c290d5c2d1c29c98ad', 782, 5, 5.50, 3.50, '袋', 'http://101.132.167.61:8101/goods/cdb378a3-ac27-491a-bf67-667401fc2b1a.jpg', 'http://101.132.167.61:8101/goods/41c6f389-5c31-4fab-8b8c-8656582aa3b0.jpg', 'http://101.132.167.61:8101/goods/884be294-8cd1-427c-9ede-4bd96753b4ba.jpg', 'http://101.132.167.61:8101/goods/abfb5824-58de-4955-8d4a-fda676ba21d8.jpg', 'http://101.132.167.61:8101/goods/9ccd3817-c8fc-45d1-bd02-3c187d244e2d.jpg', 1, '2021-07-25 13:55:26', '2021-10-17 16:13:03'),
	(22, 'gd_cf968816371e44ebb1e9ce0530b1275f', '卫龙辣条', '很香狠辣，小朋友的最爱。', '<p>量多优惠，预购从速。</p>', '河南漯河', 'gt_03c6ebc9c8b64f39817decec0909b24f', 'mer_1666a982f4e349c290d5c2d1c29c98ad', 1998, 2, 2.50, 1.50, '包', 'http://101.132.167.61:8101/goods/2323418a-f1ce-43e9-97f5-6a5197a056f1.jpg', 'http://101.132.167.61:8101/goods/8bd0a52b-65ce-40e0-bb60-8b34f061ac7d.jpg', 'http://101.132.167.61:8101/goods/a63a4574-2ae6-4bb9-b796-72bba1fd0951.jpg', 'http://101.132.167.61:8101/goods/690d1ea3-1102-4aa6-9bfd-8e2947efeb1e.jpg', 'http://101.132.167.61:8101/goods/8418375e-b4fc-41e6-8859-1d10d84b0424.jpg', 1, '2021-07-25 13:59:22', '2022-01-02 01:35:22'),
	(23, 'gd_6affdca61d3a4b05ac672435833078bd', '果冻', '软软的果冻哦', '<p>假一罚十。</p>', '广东', 'gt_03c6ebc9c8b64f39817decec0909b24f', 'mer_1666a982f4e349c290d5c2d1c29c98ad', 900, 0, 24.00, 18.00, '斤', 'http://101.132.167.61:8101/goods/d0f5805a-66d6-460c-bd1a-152830e791ed.jpg', 'http://101.132.167.61:8101/goods/a9d77a76-c0d2-4f2e-8c93-ea8975143d19.jpg', 'http://101.132.167.61:8101/goods/7aa3fafd-c903-4871-83d7-1d6e07a3a09c.jpg', 'http://101.132.167.61:8101/goods/9ea21da8-7d49-49b9-8098-f1f2ab91323f.jpg', 'http://101.132.167.61:8101/goods/02c0e10b-1f8f-4ce8-9c71-0f4c8de08c14.jpg', 1, '2021-07-25 14:18:17', '2021-10-12 22:33:16'),
	(24, 'gd_12fe7e58b7ed4efcafee21268c393497', '海苔', '韩国产的海苔', '<p>很脆、有点咸的海苔</p>', '韩国', 'gt_03c6ebc9c8b64f39817decec0909b24f', 'mer_1666a982f4e349c290d5c2d1c29c98ad', 3990, 10, 28.00, 19.00, '袋', 'http://101.132.167.61:8101/goods/c7e36e1f-2f69-4845-b35d-528f79c6d746.jpg', 'http://101.132.167.61:8101/goods/5fda4ac0-10a5-4472-97ea-e2e746a887bc.jpg', 'http://101.132.167.61:8101/goods/4afc3a63-e3ae-41aa-9f01-ab7c5c4f607c.jpg', 'http://101.132.167.61:8101/goods/19d5a9d7-faef-44b7-b641-7780cca0763e.jpg', 'http://101.132.167.61:8101/goods/236c66c7-50cd-44a6-9187-13fd14870506.jpg', 1, '2021-07-25 14:31:02', '2021-10-17 16:13:03');
/*!40000 ALTER TABLE `sp_goods` ENABLE KEYS */;

-- 导出  表 zhdd.sp_goods_type 结构
CREATE TABLE IF NOT EXISTS `sp_goods_type` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `type_id` varchar(100) NOT NULL COMMENT '商品分类id',
  `name` varchar(20) NOT NULL COMMENT '分类名称',
  `image` varchar(200) NOT NULL COMMENT '分类图片',
  `desc` varchar(200) DEFAULT NULL COMMENT '分类描述',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 0:禁用 1:启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sp_goods_type_unique_index` (`type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='商品分类表';

-- 正在导出表  zhdd.sp_goods_type 的数据：~8 rows (大约)
/*!40000 ALTER TABLE `sp_goods_type` DISABLE KEYS */;
INSERT INTO `sp_goods_type` (`id`, `type_id`, `name`, `image`, `desc`, `status`, `create_time`, `update_time`) VALUES
	(1, 'gt_c7364f84714749149e139cbe162e8d29', '海鲜水产', 'http://101.132.167.61:8101/goodsType/3e18804f-9c5c-4ff2-ba04-e77b14b84e9f.jpg', '海鲜水产', 1, '2021-06-10 20:44:50', '2021-06-10 20:44:50'),
	(2, 'gt_60bd03d5c09743ab82174315d125883b', '新鲜水果', 'http://101.132.167.61:8101/goodsType/f8aa627b-3e58-469c-9782-6b2055ec0f0e.jpg', '新鲜水果', 1, '2021-06-10 20:45:26', '2021-06-10 22:01:46'),
	(3, 'gt_6d519bcb156946d48dd35ce130d67ecb', '家用电器', 'http://101.132.167.61:8101/goodsType/bcabfa8b-acc9-41a9-a8ef-5095ac91554d.jpg', '家用电器', 1, '2021-06-10 20:45:36', '2021-06-10 20:45:36'),
	(4, 'gt_2b1ba70953344e53ba4ac1e3cf3635d4', '手机数码', 'http://101.132.167.61:8101/goodsType/acf97a26-303f-46ae-9d3e-0e148d900362.jpg', '手机数码', 1, '2021-06-10 20:45:55', '2021-06-10 20:45:55'),
	(5, 'gt_2fde9df708a548208a1f60f8326d8a75', '男装女装', 'http://101.132.167.61:8101/goodsType/a4a5a57c-eaa6-4980-b1ec-6a68f98e95d6.jpg', '男装女装', 1, '2021-06-10 20:46:06', '2021-06-10 20:46:06'),
	(6, 'gt_a9beb3698a054845afe465e8c684b07b', '蔬菜蛋品', 'http://101.132.167.61:8101/goodsType/0fe8c1bf-e4d7-4116-b14a-31c476b1a46e.jpg', '蔬菜蛋品', 1, '2021-06-10 22:01:10', '2021-06-10 22:01:10'),
	(7, 'gt_2d2391cde56b4ce7a349250ade608ada', '精选肉类', 'http://101.132.167.61:8101/goodsType/9a2266ad-0f68-474f-bd3d-40aa26159c9f.jpg', '精选肉类', 1, '2021-06-10 22:02:39', '2021-06-10 22:02:39'),
	(8, 'gt_03c6ebc9c8b64f39817decec0909b24f', '零食百货', 'http://101.132.167.61:8101/goodsType/a4e23972-5804-4e78-9fe9-eaae15e7c2c6.jpg', '百货商店是指经营包括服装', 1, '2021-07-25 13:53:55', '2021-11-19 19:06:09');
/*!40000 ALTER TABLE `sp_goods_type` ENABLE KEYS */;

-- 导出  表 zhdd.sp_merchant 结构
CREATE TABLE IF NOT EXISTS `sp_merchant` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '商家id',
  `merchant_id` varchar(100) NOT NULL COMMENT '店铺id',
  `name` varchar(20) NOT NULL COMMENT '店铺名称',
  `address` varchar(100) NOT NULL COMMENT '店铺地址',
  `image` varchar(200) NOT NULL COMMENT '店铺图片',
  `desc` varchar(1000) NOT NULL COMMENT '店铺描述',
  `contact` varchar(20) NOT NULL COMMENT '联系电话',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '店铺状态 0:已关闭 1:营业中 2:暂停营业 3:已打烊',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sp_merchant_unique_index` (`merchant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='商家店铺表';

-- 正在导出表  zhdd.sp_merchant 的数据：~6 rows (大约)
/*!40000 ALTER TABLE `sp_merchant` DISABLE KEYS */;
INSERT INTO `sp_merchant` (`id`, `user_id`, `merchant_id`, `name`, `address`, `image`, `desc`, `contact`, `status`, `create_time`, `update_time`) VALUES
	(3, 23, 'mer_02c3e96c2dc546869b1744910239c0f4', 'aa的店铺', '江苏省扬州市某街道', 'http://101.132.167.61:8101/merchant/ef9ba3a9-c2b7-4716-9784-d6adf6357787.jpg', '售卖各种水果。', '16605141987', 1, '2021-06-05 10:50:40', '2021-06-11 21:59:43'),
	(4, 24, 'mer_ae7f37e85816499bbb1aa4951bddef58', 'bb的海鲜店铺', '江苏省扬州市广陵区万福西路99号', 'http://101.132.167.61:8101/merchant/cb1ab333-06f2-4cc7-90be-e1016a26335a.jpg', '海鲜水产', '15874119632', 1, '2021-06-10 21:08:10', '2021-06-10 21:08:10'),
	(5, 30, 'mer_499f07d385504e508ee52456864a0054', 'cc的店铺', '湖北省大冶市还地桥镇119号', 'http://101.132.167.61:8101/merchant/ca37bea9-be3c-4b20-9854-256e9afda234.jpg', '出售烟花炮竹', '17814254563', 1, '2021-06-15 16:52:05', '2021-06-15 16:52:05'),
	(6, 31, 'mer_b231ea39d10149b38663d0dc43d5f0c7', 'dd的店铺', '海南省海口市骑老街55号', 'http://101.132.167.61:8101/merchant/8ce7c4b4-a692-4189-9b71-fe436368a9b7.jpg', '出售各种家用电器，比如空调、洗衣机等。', '15896243214', 1, '2021-06-27 18:36:46', '2021-06-27 18:36:46'),
	(7, 41, 'mer_61ec5645d72b41b9808bb4ab60bb3afc', '한국SBS 의 디지터상점', '한국 서울시 인사동 파늘거리 11-222호', 'http://101.132.167.61:8101/merchant/e3e9e9be-d2f9-4252-b806-12bc0f503ba4.jpg', '한국 유명한 전통상품,핸드혼,가메라등.', '0199-8859332', 1, '2021-07-20 15:30:11', '2021-07-20 15:30:11'),
	(8, 36, 'mer_1666a982f4e349c290d5c2d1c29c98ad', 'ee的百货商店', '湖北武汉江汉区209号', 'http://101.132.167.61:8101/merchant/88c54b52-81a2-47e1-8e0b-11feb26db7f7.jpg', '百货商店是指经营包括服装、鞋帽、首饰、化妆品、装饰品、家电、家庭用品等众多种类商品的大型零售商店。', '15478589654', 1, '2021-07-25 13:48:58', '2021-07-25 13:48:58');
/*!40000 ALTER TABLE `sp_merchant` ENABLE KEYS */;

-- 导出  表 zhdd.sp_merchant_apply 结构
CREATE TABLE IF NOT EXISTS `sp_merchant_apply` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `apply_no` varchar(50) NOT NULL COMMENT '申请编号',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '申请人id',
  `name` varchar(20) NOT NULL COMMENT '店铺名称',
  `address` varchar(100) NOT NULL COMMENT '店铺地址',
  `image` varchar(200) NOT NULL COMMENT '店铺图片',
  `desc` varchar(1000) NOT NULL COMMENT '店铺描述',
  `contact` varchar(20) NOT NULL COMMENT '联系电话',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '审批状态 1:待审批 2:审批通过 3:审批不通过',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`apply_no`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='申请成为商家表';

-- 正在导出表  zhdd.sp_merchant_apply 的数据：~6 rows (大约)
/*!40000 ALTER TABLE `sp_merchant_apply` DISABLE KEYS */;
INSERT INTO `sp_merchant_apply` (`id`, `apply_no`, `user_id`, `name`, `address`, `image`, `desc`, `contact`, `status`, `create_time`, `update_time`) VALUES
	(1, 'MER_APPLY_5e20362a-153f-4c59-adf9-b37a85dd01ff', 23, 'aa的店铺', '江苏省扬州市某街道', 'http://101.132.167.61:8101/merchant/1ea9fed4-1b92-49fc-b495-ab93d60becc9.jpg', '售卖生活用品、猪肉粮油、蔬菜于一体的综合超市。', '16605141987', 2, '2021-06-05 10:09:31', '2021-06-05 10:50:11'),
	(2, 'mer_apy_dd4992a8b33741f09b262052c4bf746e', 24, 'bb的海鲜店铺', '江苏省扬州市广陵区万福西路99号', 'http://101.132.167.61:8101/merchant/cb1ab333-06f2-4cc7-90be-e1016a26335a.jpg', '海鲜水产', '15874119632', 2, '2021-06-10 20:52:49', '2021-06-10 21:08:10'),
	(3, 'mer_apy_5b61a028380549268a2302135cd2879f', 30, 'cc的店铺', '湖北省大冶市还地桥镇119号', 'http://101.132.167.61:8101/merchant/ca37bea9-be3c-4b20-9854-256e9afda234.jpg', '出售烟花炮竹', '17814254563', 2, '2021-06-15 16:51:50', '2021-06-15 16:52:05'),
	(4, 'mer_apy_e0f5d31e72264057a37a75e50f0d388b', 31, 'dd的店铺', '海南省海口市骑老街55号', 'http://101.132.167.61:8101/merchant/8ce7c4b4-a692-4189-9b71-fe436368a9b7.jpg', '出售各种家用电器，比如空调、洗衣机等。', '15896243214', 2, '2021-06-27 18:36:43', '2021-06-27 18:36:46'),
	(5, 'mer_apy_da88e48a9c0241c1bb1bf39cb6e8ca1b', 41, '한국SBS 의 디지터상점', '한국 서울시 인사동 파늘거리 11-222호', 'http://101.132.167.61:8101/merchant/e3e9e9be-d2f9-4252-b806-12bc0f503ba4.jpg', '한국 유명한 전통상품,핸드혼,가메라등.', '0199-8859332', 2, '2021-07-20 15:29:40', '2021-07-20 15:30:11'),
	(6, 'mer_apy_c293f994c44745eea0814447452e7a47', 36, 'ee的百货商店', '湖北武汉江汉区209号', 'http://101.132.167.61:8101/merchant/88c54b52-81a2-47e1-8e0b-11feb26db7f7.jpg', '百货商店是指经营包括服装、鞋帽、首饰、化妆品、装饰品、家电、家庭用品等众多种类商品的大型零售商店。', '15478589654', 2, '2021-07-25 13:48:42', '2021-07-25 13:48:58');
/*!40000 ALTER TABLE `sp_merchant_apply` ENABLE KEYS */;

-- 导出  表 zhdd.sp_order 结构
CREATE TABLE IF NOT EXISTS `sp_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_no` varchar(100) NOT NULL COMMENT '订单号',
  `parent_order_no` varchar(100) DEFAULT NULL COMMENT '父订单号',
  `merchant_id` varchar(100) DEFAULT NULL COMMENT '商家id',
  `total_price` decimal(10,2) unsigned NOT NULL COMMENT '总价',
  `pay_price` decimal(10,2) unsigned NOT NULL COMMENT '支付价格',
  `order_user_id` bigint(20) unsigned NOT NULL COMMENT '下单用户id',
  `pay_user_id` bigint(20) DEFAULT NULL COMMENT '支付用户id',
  `pay_status` tinyint(3) unsigned DEFAULT '1' COMMENT '支付状态 1:待支付 2:已支付',
  `logistics_status` tinyint(3) unsigned DEFAULT '3' COMMENT '物流状态 3:未发货 4:已发货',
  `cancel_status` tinyint(3) unsigned DEFAULT '5' COMMENT '取消状态 5:未取消 6:已取消',
  `pay_way` tinyint(3) unsigned DEFAULT '1' COMMENT '支付方式 1:微信 2:支付宝 3:银行卡 4:现金 9:其他',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态 1：待支付 2:已支付 3:待发货 4:已发货 6:已取消 9:已确认收货',
  `order_time` datetime DEFAULT NULL COMMENT '订单时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消订单时间',
  `deliver_time` datetime DEFAULT NULL COMMENT '发货时间',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认收货时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sp_order_unique_index` (`order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8 COMMENT='订单表';


-- 导出  表 zhdd.sp_order_detail 结构
CREATE TABLE IF NOT EXISTS `sp_order_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_no` varchar(100) NOT NULL COMMENT '订单号',
  `goods_id` varchar(100) NOT NULL COMMENT '商品id',
  `goods_count` int(11) NOT NULL DEFAULT '1' COMMENT '商品数量',
  `goods_original_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品原价',
  `goods_sale_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品售价',
  `merchant_id` varchar(100) NOT NULL COMMENT '商品所属的商家id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COMMENT='订单详情表';


-- 导出  表 zhdd.sp_shopping_cart 结构
CREATE TABLE IF NOT EXISTS `sp_shopping_cart` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `goods_id` varchar(200) NOT NULL COMMENT '商品id',
  `merchant_id` varchar(200) NOT NULL COMMENT '店铺id',
  `goods_count` int(10) unsigned NOT NULL DEFAULT '1' COMMENT '商品数量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;


-- 导出  表 zhdd.sys_config 结构
CREATE TABLE IF NOT EXISTS `sys_config` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `k` varchar(100) DEFAULT NULL COMMENT '键',
  `v` varchar(1000) DEFAULT NULL COMMENT '值',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `kv_type` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 正在导出表  zhdd.sys_config 的数据：~25 rows (大约)
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` (`id`, `k`, `v`, `remark`, `create_time`, `kv_type`) VALUES
	(2, 'oss_qiniu', '{"AccessKey" : "8-HMj9EgGNIP-xuOCpSzTn-OMyGOFtR3TxLdn4Uu","SecretKey" : "SjpGg3V6PsMdJgn42PeEd5Ik-6aNyuwdqV5CPM6A","bucket" : "ifast","AccessUrl" : "http://p6r7ke2jc.bkt.clouddn.com/"}', '七牛对象存储配置', '2018-04-06 14:31:26', 4300),
	(3, 'author', 'admin', '代码生成器配置', '2018-05-27 19:57:04', 4401),
	(4, 'email', 'anonymous@anonymous.com', '代码生成器配置', '2018-05-27 19:57:04', 4401),
	(5, 'package', 'com.pjb.springbootjwt.zhddkk', '代码生成器配置', '2018-05-27 19:57:04', 4401),
	(6, 'autoRemovePre', 'false', '代码生成器配置', '2018-05-27 19:57:04', 4401),
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

-- 导出  表 zhdd.sys_menu 结构
CREATE TABLE IF NOT EXISTS `sys_menu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '菜单名称',
  `parent_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '父菜单id',
  `i18n_key` varchar(50) NOT NULL DEFAULT '' COMMENT '国际化key',
  `url` varchar(300) NOT NULL DEFAULT '' COMMENT 'url',
  `icon` varchar(50) NOT NULL DEFAULT '' COMMENT '图标名称',
  `ext_column1` varchar(300) NOT NULL DEFAULT '' COMMENT '扩展字段1',
  `ext_column2` varchar(300) NOT NULL DEFAULT '' COMMENT '扩展字段2',
  `ext_column3` varchar(300) NOT NULL DEFAULT '' COMMENT '扩展字段3',
  `order_num` int(10) unsigned NOT NULL DEFAULT '99' COMMENT '排序号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8 COMMENT='菜单表';

-- 正在导出表  zhdd.sys_menu 的数据：~41 rows (大约)
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` (`id`, `name`, `parent_id`, `i18n_key`, `url`, `icon`, `ext_column1`, `ext_column2`, `ext_column3`, `order_num`, `create_time`, `update_time`) VALUES
	(1, '系统管理', 0, 'li.pmenu.sysmanage.label', '', 'icon-menu-folder-open', '', '', '', 1, '2021-01-07 21:18:30', '2022-01-14 19:02:52'),
	(2, '娱乐管理', 0, 'li.pmenu.entertaimanage.label', '', 'icon-menu-folder-close', '', '', '', 3, '2021-01-07 21:19:01', '2022-01-14 19:03:02'),
	(3, '游戏娱乐', 2, 'li.game.label', '/game/gameIndex.page', 'icon icon-25-19', 'true', '', '', 99, '2021-01-07 21:19:58', '2021-03-24 21:36:36'),
	(4, '用户管理', 1, 'li.usermanage.label', '/zhddkk/wsUsers/wsUsersForAdmin', 'icon icon-11-19', 'false', '', '', 1, '2021-01-07 21:20:50', '2022-01-14 19:03:21'),
	(6, '角色配置', 1, 'li.roleconfig.label', '/zhddkk/sysRole', 'icon icon-22-5', 'false', '', '', 2, '2021-01-10 12:29:30', '2022-01-14 19:03:22'),
	(8, '菜单配置', 1, 'li.menuconfig.label', '/zhddkk/sysMenu', 'icon icon-17-4', 'false', '', '', 3, '2021-01-10 12:53:39', '2022-01-14 19:03:23'),
	(9, '音乐播放', 1, 'li.music.label', '/file/musicPlayer.page', 'icon icon-15-2', 'true', '', '', 4, '2021-01-10 12:54:26', '2022-01-14 19:03:30'),
	(10, '聊天监控', 1, 'li.chatmonitor.label', '/ws/wsSimpleChat.page?roomId=001', 'icon icon-26-8', 'true', '', '', 6, '2021-01-10 12:54:58', '2022-01-14 19:03:40'),
	(11, '朋友圈', 1, 'li.circle.label', '/ws/wsclientCircle.page', 'icon icon-26-9', 'true', '', '', 7, '2021-01-10 12:55:33', '2022-01-14 19:03:43'),
	(12, '聊天记录', 1, 'li.chatlogmanage.label', '/zhddkk/wsChatlog', 'icon icon-13-5', 'true', '', '', 8, '2021-01-10 12:55:57', '2022-01-14 19:03:45'),
	(13, '操作记录', 1, 'li.operlogmanage.label', '/zhddkk/wsOperationLog', 'icon icon-16-12', 'true', '', '', 9, '2021-01-10 12:56:13', '2022-01-14 19:03:46'),
	(14, '广告发布', 1, 'li.publishad.label', '/zhddkk/wsAds', 'icon icon-1-10', 'true', '', '', 10, '2021-01-10 12:56:33', '2022-01-14 19:03:52'),
	(15, '反馈建议', 1, 'li.feedback.label', '/zhddkk/wsFeedback/adminFeedback', 'icon icon-12-9', 'true', '', '', 11, '2021-01-10 12:56:53', '2022-01-14 19:03:56'),
	(16, '领导驾驶舱', 1, 'li.leaderchart.label', '/ws/wsserverChart.page', 'icon icon-5-13', 'true', '', '', 12, '2021-01-10 12:57:14', '2022-01-14 19:03:58'),
	(18, '生成代码', 1, 'li.generatecode.label', '/common/generator', 'icon icon-16-2', 'true', '', '', 13, '2021-01-10 12:57:51', '2022-01-14 19:04:04'),
	(20, '通用管理', 0, 'li.pmenu.commonmanage.label', '', 'icon-menu-folder-close', '', '', '', 5, '2021-01-10 12:59:00', '2022-01-14 19:03:10'),
	(21, '敏感词配置', 20, 'li.sensitiveword.label', '/zhddkk/wsCommon/mgc', 'icon icon-13-14', 'true', '', '', 99, '2021-01-10 12:59:39', '2021-03-24 21:38:29'),
	(22, '脏话配置', 20, 'li.badword.label', '/zhddkk/wsCommon/zh', 'icon icon-13-14', 'true', '', '', 99, '2021-01-10 13:00:05', '2021-03-24 21:38:34'),
	(23, '常用语配置', 20, 'li.chatword.label', '/zhddkk/wsCommon/cyy', 'icon icon-13-14', 'true', '', '', 99, '2021-01-10 13:00:27', '2021-03-24 21:38:41'),
	(24, '注册问题配置', 20, 'li.registerquestion.label', '/zhddkk/wsCommon/zcwt', 'icon icon-13-14', 'true', '', '', 99, '2021-01-10 13:00:48', '2021-03-24 21:38:46'),
	(25, '客户端管理', 0, 'li.pmenu.clientmanage.label', '', 'icon-menu-folder-open', '', '', '', 2, '2021-01-10 13:02:12', '2022-01-14 19:02:56'),
	(26, '实时聊天', 25, 'li.chat.label', '/ws/wsSimpleChat.page?roomId=002', 'icon icon-6-6', 'true', '', '', 2, '2021-01-10 13:02:34', '2022-01-14 19:19:34'),
	(27, '音乐播放', 25, 'li.music.label', '/file/musicPlayer.page', 'icon icon-15-2', 'true', '', '', 1, '2021-01-10 13:02:54', '2022-01-14 19:19:27'),
	(28, '添加朋友', 25, 'li.addfriend.label', '/zhddkk/wsUsers/wsUsers', 'icon icon-14-5', 'true', '', '', 99, '2021-01-10 13:03:29', '2021-03-24 21:39:10'),
	(29, '我的朋友', 25, 'li.myfriend.label', '/zhddkk/wsFriends', 'icon icon-11-19', 'true', '', '', 99, '2021-01-10 13:03:48', '2021-03-24 21:39:18'),
	(30, '我的申请', 25, 'li.myapply.label', '/zhddkk/wsFriendsApply/myApply', 'icon icon-1-13', 'true', '', '', 99, '2021-01-10 13:04:13', '2021-03-24 21:39:26'),
	(31, '好友申请', 25, 'li.friendapply.label', '/zhddkk/wsFriendsApply/friendApply', 'icon icon-2-1', 'true', '', '', 99, '2021-01-10 13:04:36', '2021-03-24 21:39:33'),
	(32, '朋友圈', 25, 'li.circle.label', '/ws/wsclientCircle.page', 'icon icon-26-9', 'true', '', '', 4, '2021-01-10 13:04:55', '2022-01-14 19:20:01'),
	(33, '反馈建议', 25, 'li.feedback.label', '/zhddkk/wsFeedback/myFeedback', 'icon icon-12-9', 'true', '', '', 99, '2021-01-10 13:05:12', '2021-03-24 21:39:44'),
	(34, '智能助手', 25, 'li.aiassistant.label', '/ws/intelAssistant.page', 'icon icon-25-10', 'true', '', '', 99, '2021-01-10 13:05:37', '2021-03-24 21:39:50'),
	(35, '网易新闻', 25, 'li.wantyinews.label', '/ws/wangyiNews.page', 'icon icon-9-10', 'true', '', '', 99, '2021-01-10 13:05:55', '2021-03-24 21:39:57'),
	(38, 'H5游戏', 2, 'li.h5.label', '/canvas/canvasIndex.page', 'icon icon-12-19', 'true', '', '', 99, '2021-01-10 14:44:17', '2021-03-24 21:39:58'),
	(39, '超市管理', 0, 'li.pmenu.shopping.manage.label', '', 'icon icon-5-11', 'true', '', '', 4, '2021-06-05 09:27:05', '2022-01-14 19:03:06'),
	(40, '申请成为商家', 39, 'li.merchant.apply.label', '/shop/spMerchantApply', 'icon icon-11-13', 'true', '', '', 99, '2021-06-05 09:29:06', '2021-06-14 23:00:52'),
	(42, '商品类型', 39, 'li.goods.type.label', '/shop/spGoodsType', 'icon icon-8-7', 'true', '', '', 99, '2021-06-10 07:33:38', '2021-06-14 23:01:03'),
	(43, '我的商品', 39, 'li.my.goods.label', '/shop/spGoods', 'icon icon-8-12', 'true', '', '', 99, '2021-06-10 07:34:15', '2021-06-14 22:57:02'),
	(44, '我的店铺', 39, 'li.my.merchant.label', '/shop/spMerchant/myMerchant', 'icon icon-6-9', 'true', '', '', 99, '2021-06-10 07:34:57', '2021-06-14 22:57:14'),
	(45, '超市购物', 39, 'li.shopping.center.label', '/shop/shoppingCenter', 'icon icon-12-15', 'true', '', '', 99, '2021-06-10 07:35:33', '2021-06-14 22:57:27'),
	(46, '定时任务', 1, 'li.schedulertask.label', '/zhddkk/sysTask', 'icon icon-6-1', 'true', '', '', 99, '2021-07-18 08:21:07', '2021-07-18 08:21:07'),
	(47, '聊天室', 25, 'li.chatroom.label', '/zhddkk/wsChatroom', 'icon icon-20-8', 'true', '', '', 3, '2021-07-27 08:51:34', '2022-01-14 19:19:44'),
	(50, '文件管理', 1, 'li.file.manage', '/file/fileManage.page', 'icon icon-15-9', 'true', '', '', 5, '2022-01-14 14:30:16', '2022-01-14 19:03:32');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;

-- 导出  表 zhdd.sys_role 结构
CREATE TABLE IF NOT EXISTS `sys_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `name` varchar(50) NOT NULL COMMENT '角色名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- 正在导出表  zhdd.sys_role 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` (`id`, `name`, `create_time`, `update_time`) VALUES
	(1, '管理员', '2021-01-07 20:50:39', '2021-01-07 20:50:39'),
	(2, '普通用户', '2021-01-07 20:51:31', '2021-01-07 20:51:31'),
	(3, '游客', '2021-01-16 09:09:38', '2021-01-16 09:09:38');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;

-- 导出  表 zhdd.sys_role_menu 结构
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(10) NOT NULL COMMENT '角色id',
  `role_name` varchar(50) NOT NULL DEFAULT '' COMMENT '角色名称',
  `menu_id` int(10) NOT NULL COMMENT '菜单id',
  `menu_name` varchar(50) NOT NULL DEFAULT '' COMMENT '菜单名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=707 DEFAULT CHARSET=utf8 COMMENT='角色与菜单关系表';

-- 正在导出表  zhdd.sys_role_menu 的数据：~86 rows (大约)
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` (`id`, `role_id`, `role_name`, `menu_id`, `menu_name`, `create_time`, `update_time`) VALUES
	(621, 1, '管理员', 1, '系统管理', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(622, 1, '管理员', 2, '娱乐管理', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(623, 1, '管理员', 3, '游戏娱乐', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(624, 1, '管理员', 4, '用户管理', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(625, 1, '管理员', 6, '角色配置', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(626, 1, '管理员', 8, '菜单配置', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(627, 1, '管理员', 9, '音乐播放', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(628, 1, '管理员', 10, '聊天监控', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(629, 1, '管理员', 11, '朋友圈', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(630, 1, '管理员', 12, '聊天记录', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(631, 1, '管理员', 13, '操作记录', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(632, 1, '管理员', 14, '广告发布', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(633, 1, '管理员', 15, '反馈建议', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(634, 1, '管理员', 16, '领导驾驶舱', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(635, 1, '管理员', 18, '生成代码', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(636, 1, '管理员', 20, '通用管理', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(637, 1, '管理员', 21, '敏感词配置', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(638, 1, '管理员', 22, '脏话配置', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(639, 1, '管理员', 23, '常用语配置', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(640, 1, '管理员', 24, '注册问题配置', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(641, 1, '管理员', 25, '客户端管理', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(642, 1, '管理员', 26, '实时聊天', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(643, 1, '管理员', 27, '音乐播放', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(644, 1, '管理员', 28, '添加朋友', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(645, 1, '管理员', 29, '我的朋友', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(646, 1, '管理员', 30, '我的申请', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(647, 1, '管理员', 31, '好友申请', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(648, 1, '管理员', 32, '朋友圈', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(649, 1, '管理员', 33, '反馈建议', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(650, 1, '管理员', 34, '智能助手', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(651, 1, '管理员', 35, '网易新闻', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(652, 1, '管理员', 38, 'H5游戏', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(653, 1, '管理员', 39, '超市管理', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(654, 1, '管理员', 40, '申请成为商家', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(655, 1, '管理员', 42, '商品类型', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(656, 1, '管理员', 43, '我的商品', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(657, 1, '管理员', 44, '我的店铺', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(658, 1, '管理员', 45, '超市购物', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(659, 1, '管理员', 46, '定时任务', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(660, 1, '管理员', 47, '聊天室', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(661, 1, '管理员', 50, '文件管理', '2022-01-14 14:30:24', '2022-01-14 14:30:24'),
	(662, 3, '游客', 1, '系统管理', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(663, 3, '游客', 2, '娱乐管理', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(664, 3, '游客', 3, '游戏娱乐', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(665, 3, '游客', 11, '朋友圈', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(666, 3, '游客', 15, '反馈建议', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(667, 3, '游客', 16, '领导驾驶舱', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(668, 3, '游客', 25, '客户端管理', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(669, 3, '游客', 26, '实时聊天', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(670, 3, '游客', 27, '音乐播放', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(671, 3, '游客', 28, '添加朋友', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(672, 3, '游客', 29, '我的朋友', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(673, 3, '游客', 30, '我的申请', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(674, 3, '游客', 31, '好友申请', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(675, 3, '游客', 32, '朋友圈', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(676, 3, '游客', 33, '反馈建议', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(677, 3, '游客', 34, '智能助手', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(678, 3, '游客', 35, '网易新闻', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(679, 3, '游客', 38, 'H5游戏', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(680, 3, '游客', 39, '超市管理', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(681, 3, '游客', 40, '申请成为商家', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(682, 3, '游客', 42, '商品类型', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(683, 3, '游客', 43, '我的商品', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(684, 3, '游客', 44, '我的店铺', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(685, 3, '游客', 45, '超市购物', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(686, 3, '游客', 47, '聊天室', '2022-01-14 14:30:52', '2022-01-14 14:30:52'),
	(687, 2, '普通用户', 2, '娱乐管理', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(688, 2, '普通用户', 3, '游戏娱乐', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(689, 2, '普通用户', 25, '客户端管理', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(690, 2, '普通用户', 26, '实时聊天', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(691, 2, '普通用户', 27, '音乐播放', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(692, 2, '普通用户', 28, '添加朋友', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(693, 2, '普通用户', 29, '我的朋友', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(694, 2, '普通用户', 30, '我的申请', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(695, 2, '普通用户', 31, '好友申请', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(696, 2, '普通用户', 32, '朋友圈', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(697, 2, '普通用户', 33, '反馈建议', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(698, 2, '普通用户', 34, '智能助手', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(699, 2, '普通用户', 35, '网易新闻', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(700, 2, '普通用户', 38, 'H5游戏', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(701, 2, '普通用户', 39, '超市管理', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(702, 2, '普通用户', 40, '申请成为商家', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(703, 2, '普通用户', 43, '我的商品', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(704, 2, '普通用户', 44, '我的店铺', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(705, 2, '普通用户', 45, '超市购物', '2022-01-16 15:44:53', '2022-01-16 15:44:53'),
	(706, 2, '普通用户', 47, '聊天室', '2022-01-16 15:44:53', '2022-01-16 15:44:53');
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;

-- 导出  表 zhdd.sys_task 结构
CREATE TABLE IF NOT EXISTS `sys_task` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `job_name` varchar(255) DEFAULT NULL COMMENT '任务名',
  `job_group` varchar(255) DEFAULT NULL COMMENT '任务分组',
  `bean_class` varchar(255) DEFAULT NULL COMMENT '任务执行时调用哪个类的方法 包名+类名',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT 'cron表达式',
  `method_name` varchar(255) DEFAULT NULL COMMENT '任务调用的方法名',
  `parameters` varchar(1000) DEFAULT NULL COMMENT '方法参数(JSON串)',
  `description` varchar(255) DEFAULT NULL COMMENT '任务描述',
  `is_concurrent` varchar(255) DEFAULT NULL COMMENT '任务是否有状态',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `job_status` varchar(255) DEFAULT NULL COMMENT '任务状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 正在导出表  zhdd.sys_task 的数据：1 rows
/*!40000 ALTER TABLE `sys_task` DISABLE KEYS */;
INSERT INTO `sys_task` (`id`, `job_name`, `job_group`, `bean_class`, `cron_expression`, `method_name`, `parameters`, `description`, `is_concurrent`, `create_date`, `update_date`, `job_status`) VALUES
	(17, '定时任务1', 'group1', 'com.pjb.springbootjwt.zhddkk.quartz.jobs.TestJob', '*/5 * * * * ?', 'execute', '{"name":"hch","age":25}', '这是测试任务1.', NULL, '2021-07-18 08:22:57', '2021-07-18 08:24:12', '0');
/*!40000 ALTER TABLE `sys_task` ENABLE KEYS */;

-- 导出  表 zhdd.sys_user_role 结构
CREATE TABLE IF NOT EXISTS `sys_user_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL COMMENT '用户id',
  `user_name` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名称',
  `role_id` int(10) unsigned NOT NULL COMMENT '角色id',
  `role_name` varchar(50) NOT NULL DEFAULT '' COMMENT '角色名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COMMENT='用户与角色关系表';

-- 正在导出表  zhdd.sys_user_role 的数据：~16 rows (大约)
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` (`id`, `user_id`, `user_name`, `role_id`, `role_name`, `create_time`, `update_time`) VALUES
	(11, 24, 'bb', 2, '普通用户', '2021-01-10 13:12:59', '2021-01-10 13:12:59'),
	(12, 30, 'cc', 2, '普通用户', '2021-01-10 13:13:04', '2021-01-10 13:13:04'),
	(13, 33, 'cccc', 2, '普通用户', '2021-01-10 13:13:06', '2021-01-10 13:13:06'),
	(17, 1, 'admin', 1, '管理员', '2021-01-10 15:08:25', '2021-01-10 15:08:25'),
	(19, 18, 'hch', 2, '普通用户', '2021-01-10 15:11:51', '2021-01-16 19:43:22'),
	(20, 29, 'gt', 2, '普通用户', '2021-01-11 21:52:26', '2021-01-11 21:52:26'),
	(22, 35, 'aa01', 2, '普通用户', '2021-01-17 11:07:55', '2021-01-17 11:07:55'),
	(23, 36, 'ee', 2, '普通用户', '2021-02-10 23:28:13', '2021-02-10 23:28:13'),
	(24, 37, 'ff', 2, '普通用户', '2021-02-10 23:28:39', '2021-02-10 23:28:39'),
	(25, 38, 'gg', 2, '普通用户', '2021-02-10 23:29:00', '2021-02-10 23:29:00'),
	(26, 39, 'hh', 2, '普通用户', '2021-02-10 23:29:27', '2021-02-10 23:29:27'),
	(27, 40, 'ii', 2, '普通用户', '2021-02-10 23:40:01', '2021-02-10 23:40:01'),
	(28, 23, 'aa', 2, '普通用户', '2021-06-05 09:34:58', '2021-06-05 09:34:58'),
	(29, 31, 'dd', 2, '普通用户', '2021-06-27 18:34:17', '2021-06-27 18:34:17'),
	(30, 41, 'sbs', 2, '普通用户', '2021-07-18 23:12:03', '2021-07-18 23:12:03'),
	(32, 42, 'guest', 3, '游客', '2021-11-19 18:36:14', '2021-11-19 18:36:14'),
	(33, 43, 'guest2', 2, '普通用户', '2021-11-20 12:14:04', '2021-11-20 12:14:04');
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;

-- 导出  表 zhdd.t_permission 结构
CREATE TABLE IF NOT EXISTS `t_permission` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(50) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- 正在导出表  zhdd.t_permission 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `t_permission` DISABLE KEYS */;
INSERT INTO `t_permission` (`id`, `permission_name`, `create_time`) VALUES
	(1, 'add', '2020-03-12 15:41:48'),
	(2, 'delete', '2020-03-12 16:00:10');
/*!40000 ALTER TABLE `t_permission` ENABLE KEYS */;

-- 导出  表 zhdd.t_role 结构
CREATE TABLE IF NOT EXISTS `t_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `rolename` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- 正在导出表  zhdd.t_role 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `t_role` DISABLE KEYS */;
INSERT INTO `t_role` (`id`, `rolename`, `create_time`) VALUES
	(1, 'admin', '2020-03-12 14:54:56'),
	(2, 'user', '2020-03-12 14:55:01');
/*!40000 ALTER TABLE `t_role` ENABLE KEYS */;

-- 导出  表 zhdd.t_role_permission 结构
CREATE TABLE IF NOT EXISTS `t_role_permission` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(10) unsigned NOT NULL,
  `role_name` varchar(200) NOT NULL DEFAULT '',
  `permission_id` int(10) unsigned NOT NULL,
  `permission_name` varchar(200) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- 正在导出表  zhdd.t_role_permission 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `t_role_permission` DISABLE KEYS */;
INSERT INTO `t_role_permission` (`id`, `role_id`, `role_name`, `permission_id`, `permission_name`) VALUES
	(1, 1, 'admin', 1, 'add');
/*!40000 ALTER TABLE `t_role_permission` ENABLE KEYS */;

-- 导出  表 zhdd.t_user 结构
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` int(45) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 正在导出表  zhdd.t_user 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` (`id`, `username`, `password`, `create_time`, `update_time`) VALUES
	(1, '张三', '123456', '2020-03-12 13:46:32', '2020-03-12 13:46:32'),
	(2, '李四', '123456', '2020-03-12 13:46:32', '2020-03-12 13:46:32'),
	(3, '王五', '123456', '2020-03-12 13:46:32', '2020-03-12 13:46:32');
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;

-- 导出  表 zhdd.t_user_role 结构
CREATE TABLE IF NOT EXISTS `t_user_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `username` varchar(50) NOT NULL DEFAULT '',
  `role_id` int(10) unsigned NOT NULL,
  `role_name` varchar(50) NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- 正在导出表  zhdd.t_user_role 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `t_user_role` DISABLE KEYS */;
INSERT INTO `t_user_role` (`id`, `user_id`, `username`, `role_id`, `role_name`, `create_time`) VALUES
	(1, 1, '张三', 1, 'admin', '2020-03-12 14:55:34'),
	(2, 1, '张三', 2, 'user', '2020-03-12 14:55:53');
/*!40000 ALTER TABLE `t_user_role` ENABLE KEYS */;

-- 导出  视图 zhdd.view_ws_file 结构
-- 创建临时表以解决视图依赖性错误
CREATE TABLE `view_ws_file` (
	`id` INT(10) UNSIGNED NOT NULL COMMENT '主键',
	`user` VARCHAR(50) NOT NULL COMMENT '用户名' COLLATE 'utf8_general_ci',
	`folder` VARCHAR(50) NOT NULL COMMENT '文件类型' COLLATE 'utf8_general_ci',
	`filename` VARCHAR(300) NOT NULL COMMENT '文件名' COLLATE 'utf8_general_ci',
	`disk_path` VARCHAR(100) NULL COMMENT '存储磁盘目录' COLLATE 'utf8_general_ci',
	`url` VARCHAR(100) NOT NULL COMMENT 'url' COLLATE 'utf8_general_ci',
	`file_size` BIGINT(20) UNSIGNED NOT NULL COMMENT '文件大小',
	`author` VARCHAR(50) NOT NULL COMMENT '未知' COLLATE 'utf8_general_ci',
	`track_length` VARCHAR(50) NOT NULL COMMENT '文件时长' COLLATE 'utf8_general_ci',
	`access_status` TINYINT(3) UNSIGNED NOT NULL COMMENT '访问性 0:不可访问 1:可访问',
	`create_time` DATETIME NOT NULL COMMENT '创建时间',
	`update_time` DATETIME NOT NULL COMMENT '更新时间',
	`status` TINYINT(1) UNSIGNED NOT NULL COMMENT '0:无效 1:有效'
) ENGINE=MyISAM;

-- 导出  表 zhdd.ws_ads 结构
CREATE TABLE IF NOT EXISTS `ws_ads` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `back_img` varchar(300) NOT NULL COMMENT '背景图片',
  `content` longtext NOT NULL COMMENT '内容',
  `receive_list` varchar(1000) NOT NULL DEFAULT '' COMMENT '接收人列表',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 COMMENT='广告表';


-- 导出  表 zhdd.ws_chatlog 结构
CREATE TABLE IF NOT EXISTS `ws_chatlog` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `time` varchar(50) DEFAULT NULL COMMENT '时间',
  `room_name` varchar(200) DEFAULT NULL COMMENT '房间名称',
  `user` varchar(50) DEFAULT '' COMMENT '发起人',
  `to_user` varchar(50) DEFAULT '' COMMENT '被聊人',
  `msg` varchar(4000) DEFAULT '' COMMENT '内容',
  `remark` varchar(400) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='聊天记录表';

-- 正在导出表  zhdd.ws_chatlog 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `ws_chatlog` DISABLE KEYS */;
/*!40000 ALTER TABLE `ws_chatlog` ENABLE KEYS */;

-- 导出  表 zhdd.ws_chatroom 结构
CREATE TABLE IF NOT EXISTS `ws_chatroom` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `room_id` varchar(50) NOT NULL COMMENT '房间id',
  `name` varchar(50) NOT NULL COMMENT '房间名称',
  `password` varchar(100) DEFAULT NULL COMMENT '房间密码',
  `desc` varchar(200) NOT NULL DEFAULT '' COMMENT '房间描述',
  `category1` varchar(10) DEFAULT '' COMMENT '大分类',
  `category2` varchar(10) DEFAULT '' COMMENT '小分类',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 0:删除 1:正常 2:封锁',
  PRIMARY KEY (`id`),
  UNIQUE KEY `インデックス 2` (`room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='聊天室房间表';

-- 正在导出表  zhdd.ws_chatroom 的数据：~20 rows (大约)
/*!40000 ALTER TABLE `ws_chatroom` DISABLE KEYS */;
INSERT INTO `ws_chatroom` (`id`, `room_id`, `name`, `password`, `desc`, `category1`, `category2`, `create_user_id`, `create_time`, `update_time`, `status`) VALUES
	(1, '71938635', '房间1', '123456', '王者荣耀房间', '03', '0002', 1, '2021-07-27 14:24:14', '2021-11-04 21:09:44', 1),
	(2, '21406026', 'GTA5房间', NULL, '', '03', '9999', 23, '2021-07-27 17:01:19', '2021-11-06 12:05:33', 1),
	(3, '43619361', 'LOL一区房间1', '9874547', '', '03', '0001', 23, '2021-07-27 17:01:37', '2021-11-04 21:10:02', 1),
	(4, '68165954', '永恒之塔2区房间2', '87777', '', '03', '0003', 23, '2021-07-27 17:01:52', '2021-11-04 21:10:09', 1),
	(5, '60004811', 'Aion私服1', '123', '', '', '', 23, '2021-07-27 17:54:05', '2021-07-27 17:54:05', 1),
	(6, '40293469', '王者荣耀电信一区', '123456', '王者', '03', '0002', 41, '2021-08-06 15:41:43', '2021-11-04 21:19:23', 1),
	(7, '50802387', '传奇', '123456', '经典传奇', '03', '9999', 41, '2021-08-06 15:42:20', '2021-11-04 21:10:31', 1),
	(8, '69523803', 'R0000001', NULL, 'N号房', '', '', 1, '2021-10-29 13:59:09', '2021-11-06 12:05:33', 1),
	(9, '61076643', 'R0000002', NULL, '', '', '', 1, '2021-10-29 13:59:14', '2021-11-06 12:05:33', 1),
	(10, '28458377', 'R0000003', NULL, '', '', '', 1, '2021-10-29 13:59:20', '2021-11-06 12:05:33', 1),
	(11, '09986367', 'R0000003', NULL, '', '', '', 1, '2021-10-29 13:59:23', '2021-11-06 12:05:33', 1),
	(12, '60742926', 'R0000004', NULL, '', '', '', 1, '2021-10-29 13:59:29', '2021-11-06 12:05:33', 1),
	(13, '00046169', 'R0000005', NULL, '', '', '', 1, '2021-10-29 13:59:33', '2021-11-06 12:05:33', 1),
	(14, '82681284', 'R0000006', NULL, '', '02', '9999', 1, '2021-10-29 13:59:42', '2021-11-06 12:05:33', 1),
	(15, '75498604', 'R0000007', NULL, '', '', '', 1, '2021-10-29 13:59:48', '2021-11-06 12:05:33', 1),
	(16, '73819292', 'R0000008', NULL, '', '', '', 1, '2021-10-29 13:59:51', '2021-11-06 12:05:33', 1),
	(17, '07260101', 'R0000009', NULL, '', '', '', 1, '2021-10-29 13:59:55', '2021-11-06 12:05:33', 1),
	(18, '66502474', 'R0000010', NULL, '', '', '', 1, '2021-10-29 14:00:00', '2021-11-06 12:05:33', 1),
	(19, '96533992', '韩国烤肉', '111111', '', '01', '01', 1, '2021-11-04 21:18:20', '2021-11-04 21:18:20', 1),
	(20, '80300189', '冬天的雪', '111111', '', '99', '99999', 1, '2021-11-04 21:18:49', '2021-11-04 21:18:49', 1);
/*!40000 ALTER TABLE `ws_chatroom` ENABLE KEYS */;

-- 导出  表 zhdd.ws_chatroom_users 结构
CREATE TABLE IF NOT EXISTS `ws_chatroom_users` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `room_id` varchar(50) NOT NULL COMMENT '房间号',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `user_name` varchar(150) NOT NULL DEFAULT '' COMMENT '用户名',
  `is_owner` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否是群主 0:不是 1:是',
  `is_manager` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否是管理员 0:不是 1:是',
  `ban_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '禁言状态 0:正常 1:已被禁言',
  `black_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '黑名单状态 0:正常 1:已被拉黑',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 0:离线 1:在线',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COMMENT='聊天室人员信息';


-- 导出  表 zhdd.ws_circle 结构
CREATE TABLE IF NOT EXISTS `ws_circle` (
  `id` int(255) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(30) NOT NULL COMMENT '用户名',
  `user_id` int(11) unsigned NOT NULL COMMENT '用户id',
  `content` varchar(1000) NOT NULL COMMENT '内容',
  `like_num` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '点赞数',
  `dislike_num` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '点踩数',
  `pic1` varchar(255) DEFAULT NULL COMMENT '图片1',
  `pic2` varchar(255) DEFAULT NULL COMMENT '图片2',
  `pic3` varchar(255) DEFAULT NULL COMMENT '图片3',
  `pic4` varchar(255) DEFAULT NULL COMMENT '图片4',
  `pic5` varchar(255) DEFAULT NULL COMMENT '图片5',
  `pic6` varchar(255) DEFAULT NULL COMMENT '图片6',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发表时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8 COMMENT='朋友圈表';

-- 正在导出表  zhdd.ws_circle 的数据：~9 rows (大约)
/*!40000 ALTER TABLE `ws_circle` DISABLE KEYS */;
INSERT INTO `ws_circle` (`id`, `user_name`, `user_id`, `content`, `like_num`, `dislike_num`, `pic1`, `pic2`, `pic3`, `pic4`, `pic5`, `pic6`, `create_time`) VALUES
	(42, '无名3', 13, '考虑时间管理会计师的价格快速', 4, 0, 'http://101.132.167.61:8101/circle\\8.jpg', NULL, 'http://101.132.167.61:8101/circle\\8da770a589874189852b1504361231ae_th.gif', NULL, NULL, NULL, '2020-05-11 18:38:35'),
	(49, 'aa', 23, '给公司高管', 2, 0, 'http://101.132.167.61:8101/circle/c8e7a6a0-b90b-424f-a6dd-63bf35a27160.jpg', NULL, NULL, NULL, NULL, NULL, '2020-06-03 19:26:44'),
	(50, 'aa', 23, '给打个广告', 3, 0, 'http://101.132.167.61:8101/circle/b2f1ba4d-ec0f-41a4-b25a-e776338ae48e.jpg', NULL, NULL, NULL, NULL, NULL, '2020-06-03 19:36:14'),
	(53, 'aa', 23, '如何他很讨厌很有体会', 2, 0, NULL, NULL, NULL, NULL, NULL, NULL, '2020-06-03 19:38:21'),
	(54, 'aa', 23, '光和热人如何', 14, 0, NULL, NULL, NULL, NULL, NULL, NULL, '2020-06-03 19:43:49'),
	(55, 'bb', 24, '工行股份分红华国锋和', 24, 0, NULL, 'http://101.132.167.61:8101/circle/5ad4775b-a3f6-44b3-8066-5847215baa23.jpg', NULL, NULL, NULL, NULL, '2020-06-03 20:19:46'),
	(56, 'admin', 1, '2020/10/25', 12, 0, 'http://101.132.167.61:8101/circle/0909a1bd-5ba8-4cf8-b012-525ad86f3685.jpg', 'http://101.132.167.61:8101/circle/db76a2c2-d7ba-463e-9a4c-18a026591d14.jpg', NULL, NULL, NULL, NULL, '2020-10-25 10:30:57'),
	(57, 'admin', 1, '你好', 39, 2, 'http://101.132.167.61:8101/circle/5beba4a2-519c-439e-8680-e7d09ab1fabb.jpg', NULL, NULL, NULL, NULL, NULL, '2020-11-19 21:38:20'),
	(58, 'sbs', 41, '오늘날씨가 너무 더워요', 0, 0, 'http://101.132.167.61:8101/circle/5c1bc2d2-6286-424a-ba07-d9b5ce99e810.jpg', NULL, NULL, 'http://101.132.167.61:8101/circle/b02b1f35-7c4e-4d75-b6d0-192d6b2e3a47.jpg', NULL, NULL, '2021-07-19 11:46:27'),
	(59, 'admin', 1, 'test001', 1, 0, 'http://101.132.167.61:8101/circle/982f80b6-0100-420f-91c2-32fd89ad1e67.jpg', 'http://101.132.167.61:8101/circle/123551dd-7cd4-4520-99c0-9894fc4b5894.jpeg', NULL, NULL, NULL, NULL, '2021-11-01 13:02:39'),
	(60, 'admin', 1, '日语五十音图', 2, 1, 'http://101.132.167.61:8101/circle/35d280dd-5281-4a3f-ac52-159ca88ff82b.jpeg', 'http://101.132.167.61:8101/circle/02cb8b0d-4f01-48bf-b1e2-d9a6d3862dd6.jpg', NULL, NULL, NULL, NULL, '2021-11-05 22:41:58');
/*!40000 ALTER TABLE `ws_circle` ENABLE KEYS */;

-- 导出  表 zhdd.ws_circle_comment 结构
CREATE TABLE IF NOT EXISTS `ws_circle_comment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `circle_id` int(11) unsigned DEFAULT NULL COMMENT '朋友圈id',
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '评论人id',
  `user_name` varchar(255) DEFAULT NULL COMMENT '评论人姓名',
  `comment` varchar(1000) DEFAULT '' COMMENT '评论内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8 COMMENT='朋友圈评论表';

-- 正在导出表  zhdd.ws_circle_comment 的数据：~17 rows (大约)
/*!40000 ALTER TABLE `ws_circle_comment` DISABLE KEYS */;
INSERT INTO `ws_circle_comment` (`id`, `circle_id`, `user_id`, `user_name`, `comment`, `create_time`) VALUES
	(1, 1, 25, 'vb', '为什么不舒服', '2019-01-04 21:20:28'),
	(2, 1, 28, 'good', '你怎么呢', '2019-01-04 21:20:29'),
	(3, 1, 26, 'jkx', '咋啦', '2019-01-04 21:20:30'),
	(38, 40, 1, 'admin', '真的妈妈们木木木吗木吗', '2019-01-21 13:37:12'),
	(40, 41, 18, '黄朝辉', '121345', '2019-01-26 21:57:47'),
	(41, 41, 15, '无名7', '15/‘了，', '2019-01-26 21:57:56'),
	(43, 11, 1, 'admin', '会更加', '2020-05-11 20:11:47'),
	(44, 11, 1, 'admin', 'fdafaffafa ', '2020-05-13 12:43:48'),
	(46, 42, 23, 'aa', '共和国韩国', '2020-06-03 19:18:09'),
	(47, 48, 24, 'bb', '个人围观围观', '2020-06-03 19:18:43'),
	(49, 54, 23, 'aa', '规范规定', '2020-06-03 20:18:50'),
	(50, 54, 24, 'bb', '加快速度加快构建是理工科', '2020-06-03 20:19:34'),
	(51, 55, 1, 'admin', '给我如果我', '2020-06-03 20:20:33'),
	(52, 55, 23, 'aa', '范德萨副队', '2020-06-06 11:54:06'),
	(53, 57, 18, '黄朝辉', '很好', '2020-11-21 11:15:04'),
	(54, 57, 1, 'admin', '合伙人', '2020-12-12 09:33:51'),
	(55, 54, 1, 'admin', '4544', '2021-01-03 18:19:56'),
	(56, 60, 1, 'admin', 'good', '2021-11-11 18:09:45');
/*!40000 ALTER TABLE `ws_circle_comment` ENABLE KEYS */;

-- 导出  表 zhdd.ws_common 结构
CREATE TABLE IF NOT EXISTS `ws_common` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(50) DEFAULT NULL COMMENT '类型',
  `name` varchar(300) DEFAULT NULL COMMENT '内容',
  `orderby` int(10) unsigned DEFAULT NULL COMMENT '排序号',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  KEY `Index 1` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8 COMMENT='通用配置表';

-- 正在导出表  zhdd.ws_common 的数据：~41 rows (大约)
/*!40000 ALTER TABLE `ws_common` DISABLE KEYS */;
INSERT INTO `ws_common` (`id`, `type`, `name`, `orderby`, `remark`) VALUES
	(3, 'mgc', '六四', 7, NULL),
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
	(17, 'zh', '操', 68, NULL),
	(14, 'zcwt', '你最讨厌什么样的人?', 73, NULL),
	(13, 'zcwt', '你怎么看待中国的房价?', 74, NULL),
	(11, 'zh', 'What the fuck?', 77, NULL),
	(10, 'mgc', 'rtyt', 78, NULL),
	(9, 'mgc', 'getrty', 79, NULL),
	(83, 'mgc', 'gcd', 88, NULL);
/*!40000 ALTER TABLE `ws_common` ENABLE KEYS */;

-- 导出  表 zhdd.ws_feedback 结构
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='问题反馈';

-- 正在导出表  zhdd.ws_feedback 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `ws_feedback` DISABLE KEYS */;
INSERT INTO `ws_feedback` (`id`, `user_id`, `user_name`, `type`, `title`, `content`, `pic_url`, `reply_content`, `reply_time`, `create_time`, `update_time`, `status`) VALUES
	(1, 23, 'aa', '1', '网络卡', '首页太卡啦', 'http://101.132.167.61:8101/feedback/89b6168a-ce2c-4764-86b7-fbbc6fc61239.jpg', '的瓦房', '2020-09-26 08:18:59', '2020-05-31 10:22:27', '2020-05-31 10:22:27', 0),
	(2, 15, '无名7', '1', '浏览器偶尔卡顿', '打开多个窗口后，浏览器有点卡', '', '收到', '2020-07-03 09:30:58', '2020-07-03 09:30:45', '2020-07-03 09:30:45', 2);
/*!40000 ALTER TABLE `ws_feedback` ENABLE KEYS */;

-- 导出  表 zhdd.ws_file 结构
CREATE TABLE IF NOT EXISTS `ws_file` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `folder` varchar(50) NOT NULL DEFAULT '' COMMENT '文件类型',
  `filename` varchar(300) NOT NULL DEFAULT '' COMMENT '文件名',
  `disk_path` varchar(100) DEFAULT '' COMMENT '存储磁盘目录',
  `url` varchar(100) NOT NULL DEFAULT '' COMMENT 'url',
  `file_size` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '文件大小',
  `author` varchar(50) NOT NULL DEFAULT '' COMMENT '未知',
  `track_length` varchar(50) NOT NULL DEFAULT '' COMMENT '文件时长',
  `access_status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '访问性 0:不可访问 1:可访问',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '0:无效 1:有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3589 DEFAULT CHARSET=utf8 COMMENT='文件表';


-- 导出  表 zhdd.ws_friends 结构
CREATE TABLE IF NOT EXISTS `ws_friends` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` int(10) unsigned NOT NULL COMMENT '用户id',
  `uname` varchar(50) NOT NULL DEFAULT '' COMMENT '用户姓名',
  `fid` int(11) unsigned NOT NULL COMMENT '好友id',
  `fname` varchar(50) NOT NULL DEFAULT '' COMMENT '好友姓名',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8 COMMENT='好友列表';


-- 导出  表 zhdd.ws_friends_apply 结构
CREATE TABLE IF NOT EXISTS `ws_friends_apply` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `from_id` int(10) unsigned NOT NULL COMMENT '申请人id',
  `from_name` varchar(50) NOT NULL DEFAULT '' COMMENT '申请人姓名',
  `to_id` int(10) unsigned NOT NULL COMMENT '好友id',
  `to_name` varchar(50) NOT NULL DEFAULT '' COMMENT '好友姓名',
  `process_status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '申请状态 1:申请中 2:被拒绝 3:申请成功',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=164 DEFAULT CHARSET=utf8 COMMENT='好友申请表';

-- 导出  表 zhdd.ws_operation_log 结构
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

-- 正在导出表  zhdd.ws_operation_log 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `ws_operation_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `ws_operation_log` ENABLE KEYS */;

-- 导出  表 zhdd.ws_sign 结构
CREATE TABLE IF NOT EXISTS `ws_sign` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(10) unsigned NOT NULL COMMENT '用户id',
  `user_name` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='用户签到表';

-- 正在导出表  zhdd.ws_sign 的数据：~13 rows (大约)
/*!40000 ALTER TABLE `ws_sign` DISABLE KEYS */;
INSERT INTO `ws_sign` (`id`, `user_id`, `user_name`, `create_time`) VALUES
	(1, 23, 'aa', '2021-10-21 22:37:10'),
	(2, 1, 'admin', '2021-10-23 20:23:58'),
	(3, 24, 'bb', '2021-10-31 00:28:07'),
	(4, 1, 'admin', '2021-11-01 13:02:04'),
	(5, 42, 'guest', '2021-11-19 19:10:42'),
	(6, 43, 'guest2', '2021-11-23 18:43:59'),
	(7, 1, 'admin', '2021-12-07 21:43:13'),
	(8, 1, 'admin', '2021-12-09 19:15:49'),
	(9, 1, 'admin', '2021-12-22 15:54:46'),
	(10, 24, 'bb', '2022-01-11 22:31:31'),
	(11, 1, 'admin', '2022-01-14 06:37:04'),
	(12, 1, 'admin', '2022-01-14 19:04:27'),
	(13, 1, 'admin', '2022-01-16 01:50:53');
/*!40000 ALTER TABLE `ws_sign` ENABLE KEYS */;

-- 导出  表 zhdd.ws_users 结构
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
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8 COMMENT='用户账号表';

-- 正在导出表  zhdd.ws_users 的数据：~35 rows (大约)
/*!40000 ALTER TABLE `ws_users` DISABLE KEYS */;
INSERT INTO `ws_users` (`id`, `name`, `password`, `register_time`, `state`, `last_login_time`, `last_logout_time`, `enable`, `speak`, `coin_num`, `question1`, `answer1`, `question2`, `answer2`, `question3`, `answer3`, `create_time`) VALUES
	(1, 'admin', 'NDc5ODhGQTcxODNBQzFCODFCNDE5OUM5QTZERDBDNjA=', '2018-03-22 22:37:07', '1', '2022-01-17 08:14:10', '2022-01-16 21:05:28', '1', '1', 990, '你最喜欢的明星是谁?', '11', '你最喜欢的一首歌是什么?', '22', '你父亲的生日是什么时候', '33', '2018-12-12 10:10:15'),
	(4, 'mb', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-31 21:55:17', '0', '2020-07-03 09:29:04', '2018-12-12 16:07:08', '1', '1', 0, '你最喜欢的水果是什么?', 'apple', '你的初中在哪里？', '湖北', '你的第一次是什么时候？', '16', '2018-12-12 10:10:15'),
	(5, '小七', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-24 11:03:07', '0', '2018-03-31 23:35:22', '2018-03-31 23:38:03', '1', '1', 0, '你最喜欢的明星是谁?', '用户', '你最喜欢的一首歌是?', '计划', '你的初恋女友是谁?', '哦了', '2018-12-12 10:10:15'),
	(6, '小三', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-24 10:45:15', '0', '2018-03-25 22:28:51', '2018-03-25 22:27:00', '1', '1', 0, '你最喜欢的明星是谁?', '喜鹊', '你最喜欢的一首歌是?', '我的心', '你的初恋女友是谁?', '曹君怡', '2018-12-12 10:10:15'),
	(7, '小五', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-24 10:59:24', '0', '2020-06-11 20:45:28', '2020-06-11 20:45:34', '1', '1', 0, '你最喜欢的明星是谁?', '广告', '你最喜欢的一首歌是?', '刚刚', '你的初恋女友是谁?', '33', '2018-12-12 10:10:15'),
	(8, '小六', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-24 11:01:33', '0', '2020-06-11 20:45:53', '2020-06-11 20:45:57', '1', '1', 0, '你最喜欢的明星是谁?', '22', '你最喜欢的一首歌是?', '33', '你的初恋女友是谁?', '55', '2018-12-12 10:10:15'),
	(9, '小四', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 22:19:23', '0', '2020-06-11 20:45:42', '2020-06-11 20:45:46', '1', '1', 0, '你最喜欢的明星是谁?', '广告', '你最喜欢的一首歌是?', '22', '你的初恋女友是谁?', '6.0', '2018-12-12 10:10:15'),
	(10, '小散', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-22 21:49:11', '0', '2018-03-24 09:13:55', '2018-03-24 09:13:54', '1', '1', 0, '你最喜欢的明星是谁?', '张娜拉', '你最喜欢的一首歌是什么?', '黄昏', '你父亲的生日是什么时候', '6.5', '2018-12-12 10:10:15'),
	(11, '无名1', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 21:30:19', '0', '2020-11-28 11:47:48', '2020-11-28 11:47:39', '1', '1', 40, '你最喜欢的明星是谁?', '不知道', '你最喜欢的一首歌是?', '微微', '你的初恋女友是谁?', '阿阿', '2018-12-12 10:10:15'),
	(12, '无名2', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 21:31:16', '0', '2020-07-03 09:27:55', '2018-04-01 07:19:46', '1', '1', 0, '你最喜欢的明星是谁?', '方法', '你最喜欢的一首歌是?', '广告', '你的初恋女友是谁?', '黄昏', '2018-12-12 10:10:15'),
	(13, '无名3', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 21:32:59', '0', '2020-07-03 09:28:06', '2020-05-11 20:19:56', '1', '1', 0, '你最喜欢的明星是谁?', '国际化', '你最喜欢的一首歌是?', '5鱼', '你的初恋女友是谁?', '偶爱', '2018-12-12 10:10:15'),
	(14, '无名5', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 21:34:28', '0', '2020-07-03 09:28:21', '2019-01-10 12:28:17', '1', '1', 0, '你最喜欢的明星是谁?', '哦哦', '你最喜欢的一首歌是?', '看了', '你的初恋女友是谁?', '鱼体育', '2018-12-12 10:10:15'),
	(15, '无名7', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 21:41:30', '0', '2020-07-03 09:28:35', '2019-01-26 22:01:13', '1', '1', 0, '你最喜欢的明星是谁?', '开个', '你最喜欢的一首歌是?', '提供', '你的初恋女友是谁?', '吧v大', '2018-12-12 10:10:15'),
	(16, '无名9', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-25 21:43:22', '0', '2020-07-03 09:28:47', '2019-01-10 12:28:36', '1', '1', 0, '你最喜欢的明星是谁?', '提供', '你最喜欢的一首歌是?', '耳朵', '你的初恋女友是谁?', '而非', '2018-12-12 10:10:15'),
	(17, '陈晓', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-23 14:10:55', '0', '2018-03-23 22:53:36', '2018-03-23 22:17:47', '1', '1', 0, '你的初恋女孩叫什么名字?', '你吗', '你母亲的生日是什么时候?', '0621', '你最喜欢的一首歌是什么?', '喊话', '2018-12-12 10:10:15'),
	(18, 'hch', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-03-22 22:36:37', '0', '2021-06-27 19:03:07', '2021-06-27 19:03:30', '1', '1', 80, '你最喜欢的明星是谁?', '张娜拉', '你最喜欢的一首歌是什么?', '天天', '你父亲的生日是什么时候', '6.0', '2018-12-12 10:10:15'),
	(21, '徐志摩', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-12 10:10:50', '0', '2018-12-12 10:16:02', '2018-12-12 10:16:02', '1', '1', 0, '你最喜欢的水果是什么?', '梨子', '你父亲生日是什么时候?', '元旦节', '你最想去哪旅行?', '非洲', '2018-12-12 10:10:50'),
	(22, 'hhh', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-12 13:55:37', '0', '2020-07-03 09:25:55', '2018-12-12 14:51:08', '1', '1', 0, '你最喜欢的水果是什么?', '梨子', '你父亲生日是什么时候?', 'dd', '你最想去哪旅行?', '한국', '2018-12-12 13:55:37'),
	(23, 'aa', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-12 16:38:58', '1', '2022-01-16 21:05:35', '2022-01-16 15:45:47', '1', '1', 261, '你最喜欢的水果是什么?', '带到', '你父亲生日是什么时候?', '带到', '你最想去哪旅行?', '的啊', '2018-12-12 16:38:58'),
	(24, 'bb', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-12 16:39:16', '0', '2022-01-16 15:43:41', '2022-01-16 15:43:55', '1', '1', 65, '你最喜欢的水果是什么?', '的的', '你父亲生日是什么时候?', '都是大神', '你最想去哪旅行?', '都是的', '2018-12-12 16:39:16'),
	(25, 'vb', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-13 15:34:55', '0', '2020-07-03 09:26:09', '2020-06-01 21:23:16', '1', '1', 0, '你最喜欢的水果是什么?', '的的', '你父亲生日是什么时候?', '33', '你最想去哪旅行?', '55', '2018-12-13 15:34:55'),
	(26, 'jkx', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-13 15:35:13', '0', '2018-12-13 15:41:00', '2018-12-13 15:41:00', '1', '1', 0, '你最想去哪旅行?', '中国', '你最想去哪旅行?', '中国', '你怎么看待中国的房价?', '太高了', '2018-12-13 15:35:13'),
	(28, 'good', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-13 15:35:31', '0', '2020-07-03 09:25:13', '2018-12-13 15:37:53', '1', '1', 0, '你的初中在哪里？', '不告诉你', '你父亲生日是什么时候?', '不告诉你', '你最想去哪旅行?', '不告诉你', '2018-12-13 15:35:31'),
	(29, 'gt', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2018-12-21 11:42:00', '0', '2020-07-03 09:25:33', NULL, '1', '1', 0, '你最喜欢的水果是什么?', '11', '你父亲生日是什么时候?', '11', '你最想去哪旅行?', '11', '2018-12-21 11:42:01'),
	(30, 'cc', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2020-05-13 13:34:23', '0', '2021-10-18 08:58:56', '2021-10-18 08:59:18', '1', '1', 0, '你最喜欢的水果是什么?', '111', '你父亲生日是什么时候?', '111', '你最想去哪旅行?', '111', '2020-05-13 13:34:23'),
	(31, 'dd', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2020-05-13 13:35:41', '0', '2021-07-04 12:53:32', '2021-07-01 22:44:04', '1', '1', 20, '你最喜欢的水果是什么?', '111', '你父亲生日是什么时候?', '111', '你最想去哪旅行?', '111', '2020-05-13 13:35:41'),
	(33, 'cccc', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2020-12-26 11:07:28', '0', '2020-12-26 11:07:28', NULL, '1', '1', 0, '你父亲生日是什么时候?', '111', '你的第一次是什么时候？', '111', '你的初中在哪里？', '111', '2020-12-26 11:07:28'),
	(34, 'hhhh', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2020-12-26 11:30:09', '0', '2021-01-16 10:25:58', '2021-01-16 10:26:14', '1', '1', 0, '你的月薪多少？', '111', '你感觉人生操蛋吗?', '111', '你最想去哪旅行?', '111', '2020-12-26 11:30:09'),
	(35, 'aa01', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2021-01-17 11:07:09', '0', '2021-01-17 11:08:20', NULL, '1', '1', 0, '你最喜欢的水果是什么?', '111', '你父亲生日是什么时候?', '111', '你的第一次是什么时候？', '111', '2021-01-17 11:07:11'),
	(36, 'ee', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2021-02-10 23:28:13', '0', '2021-10-13 08:45:27', '2021-10-13 08:45:39', '1', '1', 10, '你最喜欢的水果是什么?', '111', '你感觉人生操蛋吗?', '111', '你的月薪多少？', '111', '2021-02-10 23:28:13'),
	(37, 'ff', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2021-02-10 23:28:39', '0', '2021-02-10 23:28:39', NULL, '1', '1', 0, '你最喜欢的水果是什么?', '111', '你的初中在哪里？', '111', '你的初中在哪里？', '111', '2021-02-10 23:28:39'),
	(38, 'gg', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2021-02-10 23:29:00', '0', '2021-02-10 23:29:00', NULL, '1', '1', 0, '你最喜欢的水果是什么?', '111', '你感觉人生操蛋吗?', '111', '你的月薪多少？', '111', '2021-02-10 23:29:00'),
	(39, 'hh', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2021-02-10 23:29:27', '0', '2021-02-10 23:29:27', NULL, '1', '1', 0, '你最喜欢的水果是什么?', '111', '你感觉人生操蛋吗?', '111', '你的月薪多少？', '111', '2021-02-10 23:29:27'),
	(40, 'ii', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2021-02-10 23:40:01', '0', '2021-02-10 23:40:01', NULL, '1', '1', 0, '你最喜欢的水果是什么?', '111', '你感觉人生操蛋吗?', '111', '你的初中在哪里？', '111', '2021-02-10 23:40:01'),
	(41, 'sbs', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2021-07-18 23:12:03', '1', '2021-10-13 08:52:04', '2021-10-13 08:51:59', '1', '1', 45, '你最喜欢的水果是什么?', '111', '你感觉人生操蛋吗?', '111', '你的初中在哪里？', '111', '2021-07-18 23:12:03'),
	(42, 'guest', 'MkYyMDg5N0I1N0M2NDhFNzlFNzlFNUVBQzc4Q0JCRDY=', '2021-11-19 18:35:42', '0', '2022-01-16 15:40:12', '2022-01-16 15:41:24', '1', '1', 10, '你最喜欢的水果是什么?', '111', '你的月薪多少？', '111', '你最想去哪旅行?', '111', '2021-11-19 18:35:42'),
	(43, 'guest2', 'NDIzMUQzQ0UwODNEOUNGNTNEMTdDMEQzMkZDRTBFQ0E=', '2021-11-20 12:14:04', '0', '2021-11-23 18:40:31', '2021-11-23 18:48:08', '1', '1', 10, '你最喜欢的水果是什么?', '111', '你的初中在哪里？', '111', '你最讨厌什么样的人?', '111', '2021-11-20 12:14:04');
/*!40000 ALTER TABLE `ws_users` ENABLE KEYS */;

-- 导出  表 zhdd.ws_user_file 结构
CREATE TABLE IF NOT EXISTS `ws_user_file` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
  `user_name` varchar(50) NOT NULL DEFAULT '0' COMMENT '用户名',
  `file_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '文件id',
  `file_name` varchar(200) NOT NULL DEFAULT '0' COMMENT '文件名称',
  `file_url` varchar(300) NOT NULL DEFAULT '0' COMMENT 'url',
  `category` varchar(20) DEFAULT NULL COMMENT '分类名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1484 DEFAULT CHARSET=utf8 COMMENT='用户文件表';

-- 导出  表 zhdd.ws_user_profile 结构
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
  `location` varchar(150) NOT NULL DEFAULT '' COMMENT '位置区域',
  `address` varchar(150) NOT NULL DEFAULT '' COMMENT '详细地址',
  `profession` int(11) unsigned NOT NULL DEFAULT '99' COMMENT '职业  1:IT 2:建筑  3:金融  4:个体商户 5:旅游 99:其他',
  `profession_text` varchar(50) NOT NULL DEFAULT '其他' COMMENT '职业(文本)',
  `hobby` int(11) unsigned NOT NULL DEFAULT '99' COMMENT '爱好 1:篮球 2:足球  3:爬山 4:旅游  5:网游  99:其他',
  `hobby_text` varchar(50) NOT NULL DEFAULT '其他' COMMENT '爱好(文本)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='用户简历表';

-- 正在导出表  zhdd.ws_user_profile 的数据：~28 rows (大约)
/*!40000 ALTER TABLE `ws_user_profile` DISABLE KEYS */;
INSERT INTO `ws_user_profile` (`id`, `user_id`, `user_name`, `real_name`, `img`, `sign`, `age`, `sex`, `sex_text`, `tel`, `location`, `address`, `profession`, `profession_text`, `hobby`, `hobby_text`, `create_time`) VALUES
	(6, 24, 'bb', '艰苦艰苦人', 'http://101.132.167.61:8101/headImg/41a440e4-02be-4624-813f-2d8d42e455ce.jpg', '草泥马的', 11, 1, '男', '2548745', '', '韩国首尔', 6, '其他', 6, '其他', '2019-01-08 16:22:30'),
	(7, 18, 'hch', '黄CC', 'http://101.132.167.61:8101/headImg/341cd438-87f1-47d9-9f4f-9b050903d6c9.jpg', '啊啊  西巴咯马', 23, 1, '男', '14785749565', '湖北省-黄石市-大冶市', '广东佛山', 5, '旅游', 6, '其他', '2019-01-08 17:47:05'),
	(8, 1, 'admin', '系统管理员', 'http://101.132.167.61:8101/headImg/41a8eaba-05de-4622-a0b5-facbee71a66b.jpg', '这人很懒,一点也没留下', 23, 1, '男', '15005179204', '山西省-晋城市-泽州县', '无名岛', 1, '建筑', 1, '旅游', '2019-01-16 11:00:50'),
	(9, 23, 'aa', '我没有名字a啊', 'http://101.132.167.61:8101/headImg/8.jpg', '不喜欢签名kijl', 56, 1, '男', '13888888', '', '日本东京', 4, '个体商户', 4, '旅游', '2019-01-17 13:17:29'),
	(10, 31, 'dd', '蛋蛋', 'http://101.132.167.61:8101/headImg/2.jpg', '这人很懒,一点也没留下', 23, 1, '男', '16605141987', '', '韩国釜山', 4, '个体商户', 4, '旅游', '2020-05-13 13:35:42'),
	(11, 30, 'cc', '哇卡卡打开', 'http://101.132.167.61:8101/headImg/5c9e527a-5140-4f79-bea6-48530e46eba2.jpg', '好累', 45, 1, '男', '18795413636', '甘肃省-天水市-清水县', '韩国龙山', 1, '建筑', 1, '其他', '2020-05-13 14:15:20'),
	(13, 25, 'vb', '', 'http://101.132.167.61:8101/headImg/70d5921d-b876-4dcc-b478-109a2382b989.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 6, '其他', 6, '其他', '2020-06-01 21:10:20'),
	(14, 28, 'good', 'good', 'http://101.132.167.61:8101/headImg/15cc6f27-7564-4d08-aaec-f19a833032b4.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 6, '其他', 6, '其他', '2020-07-03 09:35:53'),
	(15, 29, 'gt', 'gt', 'http://101.132.167.61:8101/headImg/de87e2b1-8be5-42e6-87d8-cca3cd073ab1.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 6, '其他', 6, '其他', '2020-07-03 09:36:06'),
	(16, 22, 'hhh', 'hhhh', 'http://101.132.167.61:8101/headImg/d3ce489f-0b33-48b7-bc7d-86bf26cc157f.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 6, '其他', 6, '其他', '2020-07-03 09:36:20'),
	(17, 2, 'bbbb', 'bbbb', 'http://101.132.167.61:8101/headImg/d25df26a-81a1-47ba-88ca-03172c13190d.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 6, '其他', 6, '其他', '2020-07-03 09:36:31'),
	(18, 11, '无名1', '无名1', 'http://101.132.167.61:8101/headImg/5e877c77-37d3-467c-8a1c-e0d04b7ba256.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 6, '其他', 6, '其他', '2020-07-03 09:36:41'),
	(19, 12, '无名2', '无名2', 'http://101.132.167.61:8101/headImg/17edc79c-269c-45c3-9bc4-9293b5d0b16e.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 6, '其他', 6, '其他', '2020-07-03 09:36:50'),
	(20, 13, '无名3', '无名3', 'http://101.132.167.61:8101/headImg/786c5ae9-e26c-47ec-b38f-95b161228d60.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 6, '其他', 6, '其他', '2020-07-03 09:37:01'),
	(21, 14, '无名5', '无名4', 'http://101.132.167.61:8101/headImg/793d6377-73a3-4777-802f-e6c365d8700f.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 6, '其他', 6, '其他', '2020-07-03 09:37:14'),
	(22, 15, '无名7', '无名7', 'http://101.132.167.61:8101/headImg/95201f37-27f9-40ce-bc3e-5fc9d995f03c.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 6, '其他', 6, '其他', '2020-07-03 09:37:32'),
	(23, 16, '无名9', '无名9', 'http://101.132.167.61:8101/headImg/2d0d5d7b-67d4-422e-814e-9a0380254184.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 6, '其他', 6, '其他', '2020-07-03 09:37:42'),
	(24, 4, 'mb', 'mb', 'http://101.132.167.61:8101/headImg/6f4496cb-1d74-4357-92b4-aa586c1497df.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 6, '其他', 6, '其他', '2020-07-03 09:37:53'),
	(25, 32, 'aaaa', '', 'http://101.132.167.61:8101/headImg/07aa6451-daf4-494d-ac9e-a993cb4f43e3.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 99, '其他', 99, '其他', '2020-12-26 11:05:51'),
	(26, 33, 'cccc', '', 'http://101.132.167.61:8101/headImg/6c3cee40-5892-4307-924a-a3fe0d5635fd.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 99, '其他', 99, '其他', '2020-12-26 11:07:29'),
	(27, 34, 'hhhh', '', '', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 99, '其他', 99, '其他', '2020-12-26 11:30:10'),
	(28, 35, 'aa01', '', 'http://101.132.167.61:8101/headImg/3906febc-e433-4eb3-9250-d83e0057fa54.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 99, '其他', 99, '其他', '2021-01-17 11:07:26'),
	(29, 36, 'ee', '', 'http://101.132.167.61:8101/headImg/06927a30-44b2-48bb-afac-09a7aeb84741.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 99, '其他', 99, '其他', '2021-02-10 23:28:14'),
	(30, 37, 'ff', '', 'http://101.132.167.61:8101/headImg/7fb2b5d2-c687-4387-a3eb-bbfad305e382.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 99, '其他', 99, '其他', '2021-02-10 23:28:40'),
	(31, 38, 'gg', '', 'http://101.132.167.61:8101/headImg/8e48c9f7-2dd4-47b0-8817-1107a04f9a0c.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 99, '其他', 99, '其他', '2021-02-10 23:29:01'),
	(32, 39, 'hh', '', 'http://101.132.167.61:8101/headImg/afcc48c4-2b7e-4d53-9f35-ec1fe95149a3.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 99, '其他', 99, '其他', '2021-02-10 23:29:28'),
	(33, 40, 'ii', '', 'http://101.132.167.61:8101/headImg/2c9a622a-e5bb-4883-aacb-db5df4a6d228.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 99, '其他', 99, '其他', '2021-02-10 23:40:02'),
	(34, 41, 'sbs', 'xibar2', 'http://101.132.167.61:8101/headImg/b88a44e2-4013-4eac-a23b-149af57d279d.jpg', '这人很懒,一点也没留下', 0, 1, '男', '15874859622', '宁夏-银川市-null', '街道办公室001', 1, '其他', 1, '其他', '2021-07-18 23:12:03'),
	(35, 42, 'guest', '', 'http://101.132.167.61:8101/headImg/b75897e6-ec22-4971-bc8e-851276943029.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 99, '其他', 99, '其他', '2021-11-19 18:35:43'),
	(36, 43, 'guest2', '', 'http://101.132.167.61:8101/headImg/dabfb550-81aa-4acc-915a-ca9a8459b4a3.jpg', '这人很懒,一点也没留下', 0, 1, '男', '', '', '', 99, '其他', 99, '其他', '2021-11-20 12:14:04');
/*!40000 ALTER TABLE `ws_user_profile` ENABLE KEYS */;

-- 导出  表 zhdd.ws_user_session 结构
CREATE TABLE IF NOT EXISTS `ws_user_session` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `user_name` varchar(50) DEFAULT '' COMMENT '用户名',
  `session_id` varchar(100) NOT NULL COMMENT 'SESSIONID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='用户会话表';

-- 正在导出表  zhdd.ws_user_session 的数据：~8 rows (大约)
/*!40000 ALTER TABLE `ws_user_session` DISABLE KEYS */;
INSERT INTO `ws_user_session` (`id`, `user_id`, `user_name`, `session_id`, `create_time`, `update_time`) VALUES
	(8, 36, 'ee', 'FAD2AF49A57EEB4734FAE1F33F42973D', '2021-10-12 17:40:44', '2021-10-13 08:45:27'),
	(9, 41, 'sbs', '6B93DB30323531933AC61C89CDD264ED', '2021-10-12 22:34:11', '2021-10-13 08:52:05'),
	(10, 24, 'bb', '9115957F9BDFA24F64D4E6143A191A72', '2021-10-12 22:35:53', '2022-01-16 01:43:42'),
	(11, 23, 'aa', '20C2FDF2D3B220AD0320440991D1B6B9', '2021-10-12 22:36:32', '2022-01-16 07:05:35'),
	(12, 1, 'admin', 'E72226C7310B8AE5D84D677E757AD115', '2021-10-17 02:11:49', '2022-01-16 18:14:10'),
	(13, 30, 'cc', '2F8B2E7A1DD58CC2350122F8CEB686D5', '2021-10-17 21:30:01', '2021-10-18 08:58:56'),
	(14, 42, 'guest', '70BEF73364444E2EF5DA09864E707B74', '2021-11-19 18:37:07', '2022-01-16 01:40:13'),
	(15, 43, 'guest2', '96F329F9902B72092040A6E588447174', '2021-11-20 12:14:16', '2021-11-23 18:40:31');
/*!40000 ALTER TABLE `ws_user_session` ENABLE KEYS */;

-- 导出  视图 zhdd.view_ws_file 结构
-- 移除临时表并创建最终视图结构
DROP TABLE IF EXISTS `view_ws_file`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `view_ws_file` AS select `t`.`id` AS `id`,`t`.`user` AS `user`,`t`.`folder` AS `folder`,`t`.`filename` AS `filename`,`t`.`disk_path` AS `disk_path`,`t`.`url` AS `url`,`t`.`file_size` AS `file_size`,`t`.`author` AS `author`,`t`.`track_length` AS `track_length`,`t`.`access_status` AS `access_status`,`t`.`create_time` AS `create_time`,`t`.`update_time` AS `update_time`,`t`.`status` AS `status` from `ws_file` `t` where ((`t`.`user` is not null) and (length(`t`.`user`) > 0));

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
