/*
 Navicat Premium Data Transfer

 Source Server         : MySQL5-7
 Source Server Type    : MySQL
 Source Server Version : 50740
 Source Host           : localhost:3306
 Source Schema         : rocket_shop

 Target Server Type    : MySQL
 Target Server Version : 50740
 File Encoding         : 65001

 Date: 13/07/2023 13:33:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for shop_coupon
-- ----------------------------
DROP TABLE IF EXISTS `shop_coupon`;
CREATE TABLE `shop_coupon`  (
  `coupon_id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '优惠券ID',
  `coupon_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券金额',
  `is_used` int(1) NULL DEFAULT NULL COMMENT '是否使用 0未使用 1已使用',
  `used_time` timestamp(0) NULL DEFAULT NULL COMMENT '使用时间',
  PRIMARY KEY (`coupon_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 74 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shop_coupon
-- ----------------------------
INSERT INTO `shop_coupon` VALUES (1, 198.00, 1, '2023-07-12 13:01:05');
INSERT INTO `shop_coupon` VALUES (2, 111.00, 1, '2023-07-12 14:19:52');

-- ----------------------------
-- Table structure for shop_coupon_unique
-- ----------------------------
DROP TABLE IF EXISTS `shop_coupon_unique`;
CREATE TABLE `shop_coupon_unique`  (
  `order_id` bigint(50) UNSIGNED NOT NULL COMMENT '订单ID',
  UNIQUE INDEX `unique_coupon`(`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for shop_goods
-- ----------------------------
DROP TABLE IF EXISTS `shop_goods`;
CREATE TABLE `shop_goods`  (
  `goods_id` bigint(50) NOT NULL AUTO_INCREMENT,
  `goods_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `goods_number` int(11) NULL DEFAULT NULL COMMENT '商品库存',
  `goods_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `goods_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品描述',
  `add_time` timestamp(0) NULL DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`goods_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shop_goods
-- ----------------------------
INSERT INTO `shop_goods` VALUES (13, '华为 HUAWEI Mate 40 RS', 964, 11898.00, '保时捷设计麒麟9000芯片 超感知徕卡电影五摄 8GB+256GB陶瓷黑5G全网通手机', '2021-05-08 10:28:58');
INSERT INTO `shop_goods` VALUES (14, '诺基亚 新款105', 1000, 119.00, '移动2G老人机 学生手机 备用功能机 蓝色(新105单卡) 直板按键 长待机', '2021-05-08 10:31:46');

-- ----------------------------
-- Table structure for shop_goods_unique
-- ----------------------------
DROP TABLE IF EXISTS `shop_goods_unique`;
CREATE TABLE `shop_goods_unique`  (
  `order_id` bigint(50) UNSIGNED NOT NULL COMMENT '订单ID',
  UNIQUE INDEX `unique_goods`(`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shop_goods_unique
-- ----------------------------
INSERT INTO `shop_goods_unique` VALUES (154864);
INSERT INTO `shop_goods_unique` VALUES (154865);
INSERT INTO `shop_goods_unique` VALUES (154866);
INSERT INTO `shop_goods_unique` VALUES (154867);
INSERT INTO `shop_goods_unique` VALUES (154868);
INSERT INTO `shop_goods_unique` VALUES (154869);
INSERT INTO `shop_goods_unique` VALUES (154870);
INSERT INTO `shop_goods_unique` VALUES (154871);
INSERT INTO `shop_goods_unique` VALUES (154872);
INSERT INTO `shop_goods_unique` VALUES (154873);
INSERT INTO `shop_goods_unique` VALUES (154874);
INSERT INTO `shop_goods_unique` VALUES (154875);
INSERT INTO `shop_goods_unique` VALUES (154876);
INSERT INTO `shop_goods_unique` VALUES (154877);
INSERT INTO `shop_goods_unique` VALUES (154878);
INSERT INTO `shop_goods_unique` VALUES (154879);
INSERT INTO `shop_goods_unique` VALUES (154880);
INSERT INTO `shop_goods_unique` VALUES (154881);
INSERT INTO `shop_goods_unique` VALUES (154882);
INSERT INTO `shop_goods_unique` VALUES (154883);
INSERT INTO `shop_goods_unique` VALUES (154884);
INSERT INTO `shop_goods_unique` VALUES (154885);
INSERT INTO `shop_goods_unique` VALUES (154886);

-- ----------------------------
-- Table structure for shop_order
-- ----------------------------
DROP TABLE IF EXISTS `shop_order`;
CREATE TABLE `shop_order`  (
  `order_id` bigint(50) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `user_id` bigint(50) NULL DEFAULT NULL COMMENT '用户ID',
  `order_status` int(1) NULL DEFAULT NULL COMMENT '订单状态 0未确认 1已确认 2已取消 3无效 4退款',
  `pay_status` int(1) NULL DEFAULT NULL COMMENT '支付状态 0未支付 1支付中 2已支付',
  `shipping_status` int(1) NULL DEFAULT NULL COMMENT '发货状态 0未发货 1已发货 2已收货',
  `goods_id` bigint(50) NULL DEFAULT NULL COMMENT '商品ID',
  `goods_number` int(11) NULL DEFAULT NULL COMMENT '商品数量',
  `coupon_id` bigint(50) NULL DEFAULT NULL COMMENT '优惠券ID',
  `coupon_paid` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券',
  `add_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `confirm_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单确认时间',
  `pay_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`order_id`) USING BTREE,
  INDEX `FK_shop_order`(`user_id`) USING BTREE,
  INDEX `FK_shop_order2`(`goods_id`) USING BTREE,
  INDEX `FK_shop_order3`(`coupon_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 154887 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shop_order
-- ----------------------------
INSERT INTO `shop_order` VALUES (154851, 1, 0, 1, 0, 13, 1, 1, NULL, '2023-07-12 12:36:37', NULL, NULL);
INSERT INTO `shop_order` VALUES (154852, 1, 0, 1, 0, 13, 1, 1, NULL, '2023-07-12 12:38:53', NULL, NULL);
INSERT INTO `shop_order` VALUES (154853, 1, 0, 1, 0, 13, 1, 1, NULL, '2023-07-12 12:41:28', NULL, NULL);
INSERT INTO `shop_order` VALUES (154854, 1, 0, 1, 0, 13, 1, 1, NULL, '2023-07-12 13:00:34', NULL, NULL);
INSERT INTO `shop_order` VALUES (154855, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 13:01:33', NULL, NULL);
INSERT INTO `shop_order` VALUES (154856, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 13:08:42', NULL, NULL);
INSERT INTO `shop_order` VALUES (154857, 1, 3, 1, 0, 13, 1, 2, NULL, '2023-07-12 13:30:59', NULL, NULL);
INSERT INTO `shop_order` VALUES (154858, 1, 1, 2, 0, 13, 1, 2, NULL, '2023-07-12 13:31:14', NULL, NULL);
INSERT INTO `shop_order` VALUES (154859, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 13:38:05', NULL, NULL);
INSERT INTO `shop_order` VALUES (154860, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 13:56:01', NULL, NULL);
INSERT INTO `shop_order` VALUES (154861, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 13:59:31', NULL, NULL);
INSERT INTO `shop_order` VALUES (154862, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:00:44', NULL, NULL);
INSERT INTO `shop_order` VALUES (154863, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:02:42', NULL, NULL);
INSERT INTO `shop_order` VALUES (154864, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:03:50', NULL, NULL);
INSERT INTO `shop_order` VALUES (154865, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:12', NULL, NULL);
INSERT INTO `shop_order` VALUES (154866, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:12', NULL, NULL);
INSERT INTO `shop_order` VALUES (154867, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:12', NULL, NULL);
INSERT INTO `shop_order` VALUES (154868, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:12', NULL, NULL);
INSERT INTO `shop_order` VALUES (154869, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:12', NULL, NULL);
INSERT INTO `shop_order` VALUES (154870, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:12', NULL, NULL);
INSERT INTO `shop_order` VALUES (154871, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:13', NULL, NULL);
INSERT INTO `shop_order` VALUES (154872, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:13', NULL, NULL);
INSERT INTO `shop_order` VALUES (154873, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:13', NULL, NULL);
INSERT INTO `shop_order` VALUES (154874, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:13', NULL, NULL);
INSERT INTO `shop_order` VALUES (154875, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:13', NULL, NULL);
INSERT INTO `shop_order` VALUES (154876, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:13', NULL, NULL);
INSERT INTO `shop_order` VALUES (154877, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:13', NULL, NULL);
INSERT INTO `shop_order` VALUES (154878, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:14', NULL, NULL);
INSERT INTO `shop_order` VALUES (154879, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:14', NULL, NULL);
INSERT INTO `shop_order` VALUES (154880, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:14', NULL, NULL);
INSERT INTO `shop_order` VALUES (154881, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:14', NULL, NULL);
INSERT INTO `shop_order` VALUES (154882, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:14', NULL, NULL);
INSERT INTO `shop_order` VALUES (154883, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:14', NULL, NULL);
INSERT INTO `shop_order` VALUES (154884, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:14', NULL, NULL);
INSERT INTO `shop_order` VALUES (154885, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:51', NULL, NULL);
INSERT INTO `shop_order` VALUES (154886, 1, 0, 1, 0, 13, 1, 2, NULL, '2023-07-12 14:19:52', NULL, NULL);

-- ----------------------------
-- Table structure for shop_user
-- ----------------------------
DROP TABLE IF EXISTS `shop_user`;
CREATE TABLE `shop_user`  (
  `user_id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `user_password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户密码',
  `user_mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `user_reg_time` timestamp(0) NULL DEFAULT NULL COMMENT '注册时间',
  `user_money` decimal(10, 0) NULL DEFAULT NULL COMMENT '用户余额',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shop_user
-- ----------------------------
INSERT INTO `shop_user` VALUES (1, 'lijin', '88888', '18888888888', '2021-05-08 10:42:36', 1680000);
INSERT INTO `shop_user` VALUES (2, 'laoyan', '88888', '18888888887', '2021-05-08 10:42:36', 680000);
INSERT INTO `shop_user` VALUES (3, 'bobo', '88888', '18888888886', '2021-05-08 10:42:36', 80000);

SET FOREIGN_KEY_CHECKS = 1;
