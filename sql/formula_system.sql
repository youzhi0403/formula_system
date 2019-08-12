/*
 Navicat Premium Data Transfer

 Source Server         : youzhi
 Source Server Type    : MySQL
 Source Server Version : 50644
 Source Host           : localhost:3306
 Source Schema         : formula_system

 Target Server Type    : MySQL
 Target Server Version : 50644
 File Encoding         : 65001

 Date: 13/08/2019 02:39:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for catogery
-- ----------------------------
DROP TABLE IF EXISTS `catogery`;
CREATE TABLE `catogery`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `seriesId` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `catogery_ibfk_1`(`seriesId`) USING BTREE,
  CONSTRAINT `catogery_ibfk_1` FOREIGN KEY (`seriesId`) REFERENCES `series` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 68 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of catogery
-- ----------------------------
INSERT INTO `catogery` VALUES (64, 'sfd', 13);
INSERT INTO `catogery` VALUES (65, 'sdf', 13);
INSERT INTO `catogery` VALUES (66, 'fd', 12);
INSERT INTO `catogery` VALUES (67, 'sdf', 12);

-- ----------------------------
-- Table structure for cms_aw_order
-- ----------------------------
DROP TABLE IF EXISTS `cms_aw_order`;
CREATE TABLE `cms_aw_order`  (
  `price` double(10, 2) NULL DEFAULT NULL,
  `pay_time` timestamp(0) NULL DEFAULT NULL,
  `product` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `source` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of cms_aw_order
-- ----------------------------
INSERT INTO `cms_aw_order` VALUES (10.90, '2019-08-06 18:00:08', 'aaa', 'bbb');
INSERT INTO `cms_aw_order` VALUES (45.90, '2019-08-07 18:00:30', 'ccc', 'bbb');
INSERT INTO `cms_aw_order` VALUES (56.89, '2019-08-08 18:00:42', 'eee', 'bbb');

-- ----------------------------
-- Table structure for cms_aw_order_copy
-- ----------------------------
DROP TABLE IF EXISTS `cms_aw_order_copy`;
CREATE TABLE `cms_aw_order_copy`  (
  `price` double(10, 2) NULL DEFAULT NULL,
  `pay_time` datetime(0) NULL DEFAULT NULL,
  `product` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `source` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of cms_aw_order_copy
-- ----------------------------
INSERT INTO `cms_aw_order_copy` VALUES (10.90, '2019-08-06 18:00:08', 'aaa', 'bbb');
INSERT INTO `cms_aw_order_copy` VALUES (45.90, '2019-08-07 18:00:30', 'ccc', 'bbb');
INSERT INTO `cms_aw_order_copy` VALUES (56.89, '2019-08-08 18:00:42', 'eee', 'bbb');
INSERT INTO `cms_aw_order_copy` VALUES (56.89, '2019-08-08 18:00:42', 'eee', 'bbb');

-- ----------------------------
-- Table structure for formula
-- ----------------------------
DROP TABLE IF EXISTS `formula`;
CREATE TABLE `formula`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `formula_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `product_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `formula_number` varchar(0) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `batch_number` varchar(0) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `plan_amount` decimal(10, 0) NULL DEFAULT NULL,
  `plan_unit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `actual_output` decimal(10, 0) NULL DEFAULT NULL,
  `water_ph` decimal(10, 0) NULL DEFAULT NULL,
  `ele_conductivity` decimal(10, 0) NULL DEFAULT NULL,
  `equipment_state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `product_date` varchar(0) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `technology_proc` varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `exception_record` varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `engineer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `material_weighter` varchar(0) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `material_checker` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `material_distributor` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `supervisor` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `physicochemical_target` varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `attention_item` varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `emulStartTime` varchar(0) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `emulEndTime` varchar(0) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `price` decimal(10, 2) NULL DEFAULT NULL,
  `procList` varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `attentionList` varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of formula
-- ----------------------------
INSERT INTO `formula` VALUES (5, '产品乳化作业表2', '面膜', '', '面膜123', '', 100, NULL, 10, 46, 23, '良好', '', '1.准确称取A相的水加入真空乳化锅中，开启均质机，B12、D04、G09、B07用DO1和D37进行预分散后加入真空乳化锅中，均质至完全分散均匀；加入其他所有原料，升温至90℃以上，完全溶解，保温10分钟；<> <>投料时间：＿＿＿＿＿＿          投料温度：    ℃   <>最终温度：    ℃               保温时间：   min<>均质时间＿＿＿分钟             均质速度＿＿＿转/秒<>2.将B相物料依次加入A相，搅拌至完全溶解，抽真空至-0.04mPa，启动均质机，快速均质5分钟后，保温10分钟；<>均质时间＿＿＿分钟            均质速度＿＿＿转/秒<>锅内真空度＿＿＿Mpa          搅拌速度＿＿＿转 /分钟<>保温时间＿＿＿分钟<>3.开启冷却水，以1℃/分钟的速度降温，同时搅拌速度降为25转 /分钟；<>降温时间   ：                锅内真空度      Mpa     <>搅拌速度      转 /分钟<>4. 70℃，加入C相物料，均质3分钟后，继续温度降低，搅拌速度20转 /分钟，抽真空至-0.04mPa，保温搅拌15分钟以上；<>投料时间   ：      投料温度：    ℃  均质时间＿＿＿分钟   <>均质速度＿＿＿转/秒  保温时间＿＿＿分钟  真空度＿＿＿Mpa  <>5.45℃，依次加入D相各个原料，搅拌均匀，外观及pH指标合格后出料。<>出料时间   ：                        出料温度    ℃     <>料体是否均匀细腻无杂质         出料时是否经过过滤＿＿＿       <>6.清洗乳化锅及器具，并消毒，关闭设备电源。<>', ' ', '小红', '', '小橙', '小兰', '小花', '3#12转：', '1.检查生产使用的工具，容器，乳化锅是否清洁。<>2.乳化锅电器是否正常，各阀门是否关闭好<>3.半成品料桶清洗后用酒精纱布擦干。<>', '', '', NULL, '1.准确称取A相的水加入真空乳化锅中，开启均质机，B12、D04、G09、B07用DO1和D37进行预分散后加入真空乳化锅中，均质至完全分散均匀；加入其他所有原料，升温至90℃以上，完全溶解，保温10分钟；<> <>投料时间：＿＿＿＿＿＿          投料温度：    ℃   <>最终温度：    ℃               保温时间：   min<>均质时间＿＿＿分钟             均质速度＿＿＿转/秒<>2.将B相物料依次加入A相，搅拌至完全溶解，抽真空至-0.04mPa，启动均质机，快速均质5分钟后，保温10分钟；<>均质时间＿＿＿分钟            均质速度＿＿＿转/秒<>锅内真空度＿＿＿Mpa          搅拌速度＿＿＿转 /分钟<>保温时间＿＿＿分钟<>3.开启冷却水，以1℃/分钟的速度降温，同时搅拌速度降为25转 /分钟；<>降温时间   ：                锅内真空度      Mpa     <>搅拌速度      转 /分钟<>4. 70℃，加入C相物料，均质3分钟后，继续温度降低，搅拌速度20转 /分钟，抽真空至-0.04mPa，保温搅拌15分钟以上；<>投料时间   ：      投料温度：    ℃  均质时间＿＿＿分钟   <>均质速度＿＿＿转/秒  保温时间＿＿＿分钟  真空度＿＿＿Mpa  <>5.45℃，依次加入D相各个原料，搅拌均匀，外观及pH指标合格后出料。<>出料时间   ：                        出料温度    ℃     <>料体是否均匀细腻无杂质         出料时是否经过过滤＿＿＿       <>6.清洗乳化锅及器具，并消毒，关闭设备电源。<>', '1.检查生产使用的工具，容器，乳化锅是否清洁。<>2.乳化锅电器是否正常，各阀门是否关闭好<>3.半成品料桶清洗后用酒精纱布擦干。<>');
INSERT INTO `formula` VALUES (6, '产品乳化作业表2', '面膜123', '', '面膜', '', 100, NULL, 10, 46, 23, '良好', '', '1.准确称取A相的水加入真空乳化锅中，开启均质机，B12、D04、G09、B07用DO1和D37进行预分散后加入真空乳化锅中，均质至完全分散均匀；加入其他所有原料，升温至90℃以上，完全溶解，保温10分钟；<> <>投料时间：＿＿＿＿＿＿          投料温度：    ℃   <>最终温度：    ℃               保温时间：   min<>均质时间＿＿＿分钟             均质速度＿＿＿转/秒<>2.将B相物料依次加入A相，搅拌至完全溶解，抽真空至-0.04mPa，启动均质机，快速均质5分钟后，保温10分钟；<>均质时间＿＿＿分钟            均质速度＿＿＿转/秒<>锅内真空度＿＿＿Mpa          搅拌速度＿＿＿转 /分钟<>保温时间＿＿＿分钟<>3.开启冷却水，以1℃/分钟的速度降温，同时搅拌速度降为25转 /分钟；<>降温时间   ：                锅内真空度      Mpa     <>搅拌速度      转 /分钟<>4. 70℃，加入C相物料，均质3分钟后，继续温度降低，搅拌速度20转 /分钟，抽真空至-0.04mPa，保温搅拌15分钟以上；<>投料时间   ：      投料温度：    ℃  均质时间＿＿＿分钟   <>均质速度＿＿＿转/秒  保温时间＿＿＿分钟  真空度＿＿＿Mpa  <>5.45℃，依次加入D相各个原料，搅拌均匀，外观及pH指标合格后出料。<>出料时间   ：                        出料温度    ℃     <>料体是否均匀细腻无杂质         出料时是否经过过滤＿＿＿       <>6.清洗乳化锅及器具，并消毒，关闭设备电源。<>', ' ', '小红', '', '小橙', '小兰', '小花', '3#12转：', '1.检查生产使用的工具，容器，乳化锅是否清洁。<>2.乳化锅电器是否正常，各阀门是否关闭好<>3.半成品料桶清洗后用酒精纱布擦干。<>', '', '', NULL, '1.准确称取A相的水加入真空乳化锅中，开启均质机，B12、D04、G09、B07用DO1和D37进行预分散后加入真空乳化锅中，均质至完全分散均匀；加入其他所有原料，升温至90℃以上，完全溶解，保温10分钟；<> <>投料时间：＿＿＿＿＿＿          投料温度：    ℃   <>最终温度：    ℃               保温时间：   min<>均质时间＿＿＿分钟             均质速度＿＿＿转/秒<>2.将B相物料依次加入A相，搅拌至完全溶解，抽真空至-0.04mPa，启动均质机，快速均质5分钟后，保温10分钟；<>均质时间＿＿＿分钟            均质速度＿＿＿转/秒<>锅内真空度＿＿＿Mpa          搅拌速度＿＿＿转 /分钟<>保温时间＿＿＿分钟<>3.开启冷却水，以1℃/分钟的速度降温，同时搅拌速度降为25转 /分钟；<>降温时间   ：                锅内真空度      Mpa     <>搅拌速度      转 /分钟<>4. 70℃，加入C相物料，均质3分钟后，继续温度降低，搅拌速度20转 /分钟，抽真空至-0.04mPa，保温搅拌15分钟以上；<>投料时间   ：      投料温度：    ℃  均质时间＿＿＿分钟   <>均质速度＿＿＿转/秒  保温时间＿＿＿分钟  真空度＿＿＿Mpa  <>5.45℃，依次加入D相各个原料，搅拌均匀，外观及pH指标合格后出料。<>出料时间   ：                        出料温度    ℃     <>料体是否均匀细腻无杂质         出料时是否经过过滤＿＿＿       <>6.清洗乳化锅及器具，并消毒，关闭设备电源。<>', '1.检查生产使用的工具，容器，乳化锅是否清洁。<>2.乳化锅电器是否正常，各阀门是否关闭好<>3.半成品料桶清洗后用酒精纱布擦干。<>');
INSERT INTO `formula` VALUES (7, '产品乳化作业表2', 'HH21', '', '面膜', '', 100, NULL, 10, 46, 23, '良好', '', '1.准确称取A相的水加入真空乳化锅中，开启均质机，B12、D04、G09、B07用DO1和D37进行预分散后加入真空乳化锅中，均质至完全分散均匀；加入其他所有原料，升温至90℃以上，完全溶解，保温10分钟；<> <>投料时间：＿＿＿＿＿＿          投料温度：    ℃   <>最终温度：    ℃               保温时间：   min<>均质时间＿＿＿分钟             均质速度＿＿＿转/秒<>2.将B相物料依次加入A相，搅拌至完全溶解，抽真空至-0.04mPa，启动均质机，快速均质5分钟后，保温10分钟；<>均质时间＿＿＿分钟            均质速度＿＿＿转/秒<>锅内真空度＿＿＿Mpa          搅拌速度＿＿＿转 /分钟<>保温时间＿＿＿分钟<>3.开启冷却水，以1℃/分钟的速度降温，同时搅拌速度降为25转 /分钟；<>降温时间   ：                锅内真空度      Mpa     <>搅拌速度      转 /分钟<>4. 70℃，加入C相物料，均质3分钟后，继续温度降低，搅拌速度20转 /分钟，抽真空至-0.04mPa，保温搅拌15分钟以上；<>投料时间   ：      投料温度：    ℃  均质时间＿＿＿分钟   <>均质速度＿＿＿转/秒  保温时间＿＿＿分钟  真空度＿＿＿Mpa  <>5.45℃，依次加入D相各个原料，搅拌均匀，外观及pH指标合格后出料。<>出料时间   ：                        出料温度    ℃     <>料体是否均匀细腻无杂质         出料时是否经过过滤＿＿＿       <>6.清洗乳化锅及器具，并消毒，关闭设备电源。<>', ' ', '小红', '', '小橙', '小兰', '小花', '3#12转：', '1.检查生产使用的工具，容器，乳化锅是否清洁。<>2.乳化锅电器是否正常，各阀门是否关闭好<>3.半成品料桶清洗后用酒精纱布擦干。<>', '', '', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for formula_material
-- ----------------------------
DROP TABLE IF EXISTS `formula_material`;
CREATE TABLE `formula_material`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `formulaId` int(11) NULL DEFAULT NULL,
  `materialId` int(11) NULL DEFAULT NULL,
  `group` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cn_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `plan_amount` decimal(10, 0) NULL DEFAULT NULL,
  `actual_amout` decimal(10, 0) NULL DEFAULT NULL,
  `m_batch_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `checked_weight` decimal(10, 0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `formulaId`(`formulaId`) USING BTREE,
  INDEX `materialId`(`materialId`) USING BTREE,
  CONSTRAINT `formula_material_ibfk_1` FOREIGN KEY (`formulaId`) REFERENCES `formula` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `formula_material_ibfk_2` FOREIGN KEY (`materialId`) REFERENCES `material` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 91 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of formula_material
-- ----------------------------
INSERT INTO `formula_material` VALUES (55, 7, 4, 'A', '水', NULL, 89, 10, '1', 10);
INSERT INTO `formula_material` VALUES (56, 7, 5, 'A', 'EDTA-2Na', NULL, 0, 10, '1', 10);
INSERT INTO `formula_material` VALUES (57, 7, 6, 'A', 'Solubilisant LRI', NULL, 3, 10, '1', 5);
INSERT INTO `formula_material` VALUES (58, 7, 7, 'A', 'Glycerox 767', NULL, 3, 10, '1', 5);
INSERT INTO `formula_material` VALUES (59, 7, 8, 'A', '丙二醇', NULL, 4, 10, '1', 5);
INSERT INTO `formula_material` VALUES (60, 7, 9, 'A', 'mp', NULL, 0, 10, '1', 5);
INSERT INTO `formula_material` VALUES (61, 7, 10, 'B', '德敏舒', NULL, 0, 10, '1', 5);
INSERT INTO `formula_material` VALUES (62, 7, 11, 'B', 'L.PLUS', NULL, 0, 10, '1', 5);
INSERT INTO `formula_material` VALUES (63, 7, 12, 'B', 'Euxyl PE 9010', NULL, 0, 10, '1', 5);
INSERT INTO `formula_material` VALUES (73, 6, 18, 'A', '水', NULL, 89, 10, '1', 10);
INSERT INTO `formula_material` VALUES (74, 6, 19, 'A', 'EDTA-2Na', NULL, 0, 10, '1', 10);
INSERT INTO `formula_material` VALUES (75, 6, 20, 'A', 'Solubilisant LRI', NULL, 3, 10, '1', 5);
INSERT INTO `formula_material` VALUES (76, 6, 21, 'A', 'Glycerox 767', NULL, 3, 10, '1', 5);
INSERT INTO `formula_material` VALUES (77, 6, 17, 'A', '丙二醇', NULL, 4, 10, '1', 5);
INSERT INTO `formula_material` VALUES (78, 6, 22, 'A', 'mp', NULL, 0, 10, '1', 5);
INSERT INTO `formula_material` VALUES (79, 6, 23, 'B', '德敏舒', NULL, 0, 10, '1', 5);
INSERT INTO `formula_material` VALUES (80, 6, 24, 'B', 'L.PLUS', NULL, 0, 10, '1', 5);
INSERT INTO `formula_material` VALUES (81, 6, 25, 'B', 'Euxyl PE 9010', NULL, 0, 10, '1', 5);
INSERT INTO `formula_material` VALUES (82, 5, 18, 'A', '水', NULL, 89, 10, '1', 10);
INSERT INTO `formula_material` VALUES (83, 5, 19, 'A', 'EDTA-2Na', NULL, 0, 10, '1', 10);
INSERT INTO `formula_material` VALUES (84, 5, 20, 'A', 'Solubilisant LRI', NULL, 3, 10, '1', 5);
INSERT INTO `formula_material` VALUES (85, 5, 21, 'A', 'Glycerox 767', NULL, 3, 10, '1', 5);
INSERT INTO `formula_material` VALUES (86, 5, 17, 'A', '丙二醇', NULL, 4, 10, '1', 5);
INSERT INTO `formula_material` VALUES (87, 5, 22, 'A', 'mp', NULL, 0, 10, '1', 5);
INSERT INTO `formula_material` VALUES (88, 5, 23, 'B', '德敏舒', NULL, 0, 10, '1', 5);
INSERT INTO `formula_material` VALUES (89, 5, 24, 'B', 'L.PLUS', NULL, 0, 10, '1', 5);
INSERT INTO `formula_material` VALUES (90, 5, 25, 'B', 'Euxyl PE 9010', NULL, 0, 10, '1', 5);

-- ----------------------------
-- Table structure for material
-- ----------------------------
DROP TABLE IF EXISTS `material`;
CREATE TABLE `material`  (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `price` decimal(10, 2) NULL DEFAULT NULL,
  `unit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `packingWay` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `origin` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mApparentState` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `supplierId` int(11) NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `inci_cn` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `inci_en` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `application` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `chineseName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `supplierId`(`supplierId`) USING BTREE,
  CONSTRAINT `material_ibfk_1` FOREIGN KEY (`supplierId`) REFERENCES `supplier` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of material
-- ----------------------------
INSERT INTO `material` VALUES (2, 'sf1', 12.00, 'kg', 'sdf1', 'fds1', 'sf1', 8, 'sf1', 'sf1', 'sdf1', 'sdf1', 'sf1');
INSERT INTO `material` VALUES (3, '商品1', 19.00, 'kg', '包装方式1', '广东省1', '外观状态1', 2, 'zhongwen1', 'inci_cn_1', 'inci_en', '用途1', '中文名1');
INSERT INTO `material` VALUES (4, 'D28', 19.00, 'kg', '包装方式2', '广东省2', '外观状态2', 3, 'D28', 'inci_cn_2', 'inci_en', '用途2', '中文名2');
INSERT INTO `material` VALUES (5, '商品3', 19.00, 'kg', '包装方式3', '广东省3', '外观状态3', 4, 'A08', 'inci_cn_3', 'inci_en', '用途3', '中文名3');
INSERT INTO `material` VALUES (6, '商品4', 19.00, 'kg', '包装方式4', '广东省4', '外观状态4', 5, 'E31', 'inci_cn_4', 'inci_en', '用途4', '中文名4');
INSERT INTO `material` VALUES (7, '商品5', 19.00, 'kg', '包装方式5', '广东省5', '外观状态5', 6, 'E30', 'inci_cn_5', 'inci_en', '用途5', '中文名5');
INSERT INTO `material` VALUES (8, '商品6', 19.00, 'kg', '包装方式6', '广东省6', '外观状态6', 7, 'D02', 'inci_cn_6', 'inci_en', '用途6', '中文名6');
INSERT INTO `material` VALUES (9, '商品7', 19.00, 'kg', '包装方式7', '广东省7', '外观状态7', 8, 'F01', 'inci_cn_7', 'inci_en', '用途7', '中文名7');
INSERT INTO `material` VALUES (10, '商品8', 19.00, 'kg', '包装方式8', '广东省8', '外观状态8', 9, 'G04', 'inci_cn_8', 'inci_en', '用途8', '中文名8');
INSERT INTO `material` VALUES (11, '商品9', 19.00, 'kg', '包装方式9', '广东省9', '外观状态9', 10, 'K05', 'inci_cn_9', 'inci_en', '用途9', '中文名9');
INSERT INTO `material` VALUES (12, '商品10', 19.00, 'kg', '包装方式10', '广东省10', '外观状态10', 11, 'K20', 'inci_cn_10', 'inci_en', '用途10', '中文名10');
INSERT INTO `material` VALUES (13, '商品11', 19.00, 'kg', '包装方式11', '广东省11', '外观状态11', 12, 'zhongwen11', 'inci_cn_11', 'inci_en', '用途11', '中文名11');
INSERT INTO `material` VALUES (14, '商品12', 19.00, 'kg', '包装方式12', '广东省12', '外观状态12', 13, 'zhongwen12', 'inci_cn_12', 'inci_en', '用途12', '中文名12');
INSERT INTO `material` VALUES (15, '商品13', 19.00, 'kg', '包装方式13', '广东省13', '外观状态13', 14, 'zhongwen13', 'inci_cn_13', 'inci_en', '用途13', '中文名13');
INSERT INTO `material` VALUES (16, '商品14', 19.00, 'kg', '包装方式14', '广东省14', '外观状态14', 15, 'zhongwen14', 'inci_cn_14', 'inci_en', '用途14', '中文名14');
INSERT INTO `material` VALUES (17, '丙二醇', 19.00, 'kg', '包装方式15', '广东省15', '外观状态15', 16, 'bec', 'inci_cn_15', 'inci_en', '用途15', '中文名15');
INSERT INTO `material` VALUES (18, '水', 12.00, 'kg', '12', '12', '12', 2, 'sf', '12', '12', '12', '大点事');
INSERT INTO `material` VALUES (19, 'EDTA-2Na', 1.00, 'kg', '1', '1', '1', 2, '1', '1', '1', '1', '1');
INSERT INTO `material` VALUES (20, 'Solubilisant LRI', 1.00, 'kg', '1', '1', '1', 2, '1', '1', '1', '1', '1');
INSERT INTO `material` VALUES (21, 'Glycerox 767', 19.00, 'kg', '包装方式1', '广东省1', '外观状态1', 2, '45', 'inci_cn_1', 'inci_en', '用途1', '中文名1');
INSERT INTO `material` VALUES (22, 'mp', 19.00, 'kg', '包装方式2', '广东省2', '外观状态2', 3, '46', 'inci_cn_2', 'inci_en', '用途2', '中文名2');
INSERT INTO `material` VALUES (23, '德敏舒', 19.00, 'kg', '包装方式3', '广东省3', '外观状态3', 4, '47', 'inci_cn_3', 'inci_en', '用途3', '中文名3');
INSERT INTO `material` VALUES (24, 'L.PLUS', 19.00, 'kg', '包装方式4', '广东省4', '外观状态4', 5, '48', 'inci_cn_4', 'inci_en', '用途4', '中文名4');
INSERT INTO `material` VALUES (25, 'Euxyl PE 9010', 19.00, 'kg', '包装方式5', '广东省5', '外观状态5', 6, '49', 'inci_cn_5', 'inci_en', '用途5', '中文名5');

-- ----------------------------
-- Table structure for privilege
-- ----------------------------
DROP TABLE IF EXISTS `privilege`;
CREATE TABLE `privilege`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zindex` int(255) NULL DEFAULT NULL,
  `parentid` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of privilege
-- ----------------------------
INSERT INTO `privilege` VALUES (1, 'product', '产品权限', '/product', 0, 0);
INSERT INTO `privilege` VALUES (2, 'product_add', '产品添加权限', '/product/add', 1, 1);
INSERT INTO `privilege` VALUES (3, 'product_delete', '产品删除权限', '/product/delete', 1, 1);
INSERT INTO `privilege` VALUES (4, 'product_update', '产品更新权限', '/product/update', 1, 1);
INSERT INTO `privilege` VALUES (5, 'product_showlist', '产品展示权限权限', '/product/showlist', 1, 1);
INSERT INTO `privilege` VALUES (6, 'formula', '配方权限', '/formula', 0, 0);
INSERT INTO `privilege` VALUES (7, 'formula_delete', '配方删除权限', '/formula/delete', 6, 6);
INSERT INTO `privilege` VALUES (8, 'formula_update', '配方更新权限', '/formula/update', 6, 6);
INSERT INTO `privilege` VALUES (9, 'formula_import', '配方导入权限', '/formula/import', 6, 6);
INSERT INTO `privilege` VALUES (10, 'formula_export', '配方导出权限', '/formula/export', 6, 6);
INSERT INTO `privilege` VALUES (11, 'formula_showlist', '配方展示权限', '/formula/showlist', 6, 6);
INSERT INTO `privilege` VALUES (12, 'material', '原料权限', '/material', 0, 0);
INSERT INTO `privilege` VALUES (13, 'material_add', '原料添加权限', '/material/add', 12, 12);
INSERT INTO `privilege` VALUES (14, 'material_delete', '原料删除权限', '/material/delete', 12, 12);
INSERT INTO `privilege` VALUES (15, 'material_update', '原料更新权限', '/material/update', 12, 12);
INSERT INTO `privilege` VALUES (16, 'material_showlist', '原料展示权限', '/material/showlist', 12, 12);
INSERT INTO `privilege` VALUES (17, 'supplier', '供应商权限', '/supplier', 0, 0);
INSERT INTO `privilege` VALUES (18, 'supplier_add', '供应商添加权限', '/supplier/add', 17, 17);
INSERT INTO `privilege` VALUES (19, 'supplier_delete', '供应商删除权限', '/supplier', 17, 17);
INSERT INTO `privilege` VALUES (20, 'supplier_update', '供应商更新权限', '/supplier/update', 17, 17);
INSERT INTO `privilege` VALUES (21, 'supplier_showlist', '供应商展示权限', '/supplier/showlist', 17, 17);
INSERT INTO `privilege` VALUES (22, 'system', '系统权限', '/system', 0, 0);
INSERT INTO `privilege` VALUES (23, 'user', '用户权限', '/user', 0, 0);
INSERT INTO `privilege` VALUES (24, 'user_add', '用户添加权限', '/user/add', 23, 23);
INSERT INTO `privilege` VALUES (25, 'user_delete', '用户删除权限', '/user/delete', 23, 23);
INSERT INTO `privilege` VALUES (26, 'user_update', '用户更新权限', '/user/update', 23, 23);
INSERT INTO `privilege` VALUES (27, 'user_showlist', '用户展示权限', '/user/showlist', 23, 23);
INSERT INTO `privilege` VALUES (28, 'role', '角色权限', '/role', 0, 0);
INSERT INTO `privilege` VALUES (29, 'role_add', '角色添加权限', '/role/add', 28, 28);
INSERT INTO `privilege` VALUES (30, 'role_delete', '角色删除权限', '/role/delete', 28, 28);
INSERT INTO `privilege` VALUES (31, 'role_update', '角色更新权限', '/role/update', 28, 28);
INSERT INTO `privilege` VALUES (32, 'role_showlist', '角色展示权限', '/role/showlist', 28, 28);
INSERT INTO `privilege` VALUES (33, 'role_privilege_setting', '角色权限设置权限', '/role/ privilege/setting', 28, 28);
INSERT INTO `privilege` VALUES (34, 'product_SC', '产品系列目录权限', '/product_sc', 0, 0);
INSERT INTO `privilege` VALUES (35, 'product_SC_add', '产品系列目录添加权限', '/product_sc/add', 34, 34);
INSERT INTO `privilege` VALUES (37, 'product_SC_delete', '产品系列目录删除权限', '/product_sc/delete', 34, 34);
INSERT INTO `privilege` VALUES (38, 'product_SC_update', '产品系列目录更新权限', '/product_sc/update', 34, 34);
INSERT INTO `privilege` VALUES (40, 'product_SC_showlist', '展品系列目录展示权限', '/product_sc/showlist', 34, 34);

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `formula` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `formula_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `price` double(10, 2) NULL DEFAULT NULL,
  `major_composition` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sample_quantity` double(255, 0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 116 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (4, '产品31', '123', NULL, '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (5, '产品41', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (6, '产品15', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (7, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (8, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (9, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (10, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (11, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (12, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (13, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (14, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (15, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (16, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (17, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (18, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (19, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (20, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (21, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (22, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (24, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (25, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (26, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (27, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (28, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (29, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (30, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (31, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (32, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (33, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (34, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (35, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (36, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (37, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (38, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (39, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (40, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (41, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (42, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (43, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (44, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (45, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (46, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (47, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (48, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (49, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (50, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (51, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (52, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (53, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (54, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (55, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (56, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (57, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (58, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (59, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (60, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (61, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (62, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (63, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (64, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (65, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (66, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (67, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (68, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (69, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (70, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (71, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (72, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (73, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (74, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (75, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (76, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (77, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (78, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (79, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (80, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (81, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (82, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (83, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (84, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (85, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (86, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (87, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (88, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (89, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (90, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (91, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (92, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (93, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (94, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (95, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (96, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (97, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (98, '产品1', '123', '456', '配方描述', 3.70, '主要原材料', 11);
INSERT INTO `product` VALUES (99, '润肤露', '123432', '1', '配方描述', 0.00, '12.43', 10);
INSERT INTO `product` VALUES (100, '润肤露', '123432', '1', '配方描述', 10.09, 'O2,CO2', 12);
INSERT INTO `product` VALUES (101, 'name1', '123', '123', '配方描述', 1.22, 'O2', 1);
INSERT INTO `product` VALUES (102, 'name1', '123', '123', '配方描述', 1.22, 'O2', 1);
INSERT INTO `product` VALUES (103, 'name1', '123', '123', '配方描述', 1.22, 'O2', 1);
INSERT INTO `product` VALUES (106, '123', '123', '123', '123', 10.90, '123', 11);
INSERT INTO `product` VALUES (107, '123', '123', '123', '123', 10.90, '123', 11);
INSERT INTO `product` VALUES (108, '123', '123', '123', '123', 10.90, '123', 11);
INSERT INTO `product` VALUES (111, 'dfs', 'sf', NULL, 'fds', 12.00, 'sd', 12);
INSERT INTO `product` VALUES (112, '导入产品1', '12', NULL, '123', 12.00, '3213', 12);
INSERT INTO `product` VALUES (113, '导入产品2', '13', NULL, '124', 12.00, '3214', 12);
INSERT INTO `product` VALUES (114, '导入产品3', '14', NULL, '125', 12.00, '3215', 12);
INSERT INTO `product` VALUES (115, '导入产品4', '15', NULL, '126', 12.00, '3216', 12);

-- ----------------------------
-- Table structure for product_series_catogery
-- ----------------------------
DROP TABLE IF EXISTS `product_series_catogery`;
CREATE TABLE `product_series_catogery`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productId` int(11) NULL DEFAULT NULL,
  `seriesId` int(11) NULL DEFAULT NULL,
  `catogeryId` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `productId`(`productId`) USING BTREE,
  INDEX `seriesId`(`seriesId`) USING BTREE,
  INDEX `catogeryId`(`catogeryId`) USING BTREE,
  CONSTRAINT `product_series_catogery_ibfk_1` FOREIGN KEY (`productId`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `product_series_catogery_ibfk_2` FOREIGN KEY (`seriesId`) REFERENCES `series` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `product_series_catogery_ibfk_3` FOREIGN KEY (`catogeryId`) REFERENCES `catogery` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of product_series_catogery
-- ----------------------------
INSERT INTO `product_series_catogery` VALUES (56, 4, 3, NULL);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '超级管理员', '最高权限');
INSERT INTO `role` VALUES (2, '角色1', '说明1');

-- ----------------------------
-- Table structure for role_privilege
-- ----------------------------
DROP TABLE IF EXISTS `role_privilege`;
CREATE TABLE `role_privilege`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleId` int(255) NULL DEFAULT NULL,
  `privilegeId` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `roleId`(`roleId`) USING BTREE,
  INDEX `privilegeId`(`privilegeId`) USING BTREE,
  CONSTRAINT `role_privilege_ibfk_1` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `role_privilege_ibfk_2` FOREIGN KEY (`privilegeId`) REFERENCES `privilege` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of role_privilege
-- ----------------------------
INSERT INTO `role_privilege` VALUES (1, 1, 1);
INSERT INTO `role_privilege` VALUES (2, 1, 2);
INSERT INTO `role_privilege` VALUES (3, 1, 3);
INSERT INTO `role_privilege` VALUES (4, 1, 4);
INSERT INTO `role_privilege` VALUES (5, 1, 5);
INSERT INTO `role_privilege` VALUES (6, 1, 6);
INSERT INTO `role_privilege` VALUES (7, 1, 7);
INSERT INTO `role_privilege` VALUES (8, 1, 8);
INSERT INTO `role_privilege` VALUES (9, 1, 9);
INSERT INTO `role_privilege` VALUES (10, 1, 10);
INSERT INTO `role_privilege` VALUES (11, 1, 11);
INSERT INTO `role_privilege` VALUES (12, 1, 12);
INSERT INTO `role_privilege` VALUES (13, 1, 13);
INSERT INTO `role_privilege` VALUES (14, 1, 14);
INSERT INTO `role_privilege` VALUES (15, 1, 15);
INSERT INTO `role_privilege` VALUES (16, 1, 16);
INSERT INTO `role_privilege` VALUES (17, 1, 17);
INSERT INTO `role_privilege` VALUES (18, 1, 18);
INSERT INTO `role_privilege` VALUES (19, 1, 19);
INSERT INTO `role_privilege` VALUES (20, 1, 20);
INSERT INTO `role_privilege` VALUES (21, 1, 21);
INSERT INTO `role_privilege` VALUES (22, 1, 22);
INSERT INTO `role_privilege` VALUES (23, 1, 23);
INSERT INTO `role_privilege` VALUES (24, 1, 24);
INSERT INTO `role_privilege` VALUES (25, 1, 25);
INSERT INTO `role_privilege` VALUES (26, 1, 26);
INSERT INTO `role_privilege` VALUES (27, 1, 27);
INSERT INTO `role_privilege` VALUES (28, 1, 28);
INSERT INTO `role_privilege` VALUES (29, 1, 29);
INSERT INTO `role_privilege` VALUES (30, 1, 30);
INSERT INTO `role_privilege` VALUES (31, 1, 31);
INSERT INTO `role_privilege` VALUES (32, 1, 32);
INSERT INTO `role_privilege` VALUES (33, 1, 33);
INSERT INTO `role_privilege` VALUES (34, 1, 34);
INSERT INTO `role_privilege` VALUES (35, 1, 35);
INSERT INTO `role_privilege` VALUES (37, 1, 37);
INSERT INTO `role_privilege` VALUES (38, 1, 38);
INSERT INTO `role_privilege` VALUES (40, 1, 40);
INSERT INTO `role_privilege` VALUES (44, 2, 1);
INSERT INTO `role_privilege` VALUES (45, 2, 4);
INSERT INTO `role_privilege` VALUES (46, 2, 5);
INSERT INTO `role_privilege` VALUES (47, 2, 6);
INSERT INTO `role_privilege` VALUES (48, 2, 7);
INSERT INTO `role_privilege` VALUES (49, 2, 8);
INSERT INTO `role_privilege` VALUES (50, 2, 9);
INSERT INTO `role_privilege` VALUES (51, 2, 10);
INSERT INTO `role_privilege` VALUES (52, 2, 11);

-- ----------------------------
-- Table structure for series
-- ----------------------------
DROP TABLE IF EXISTS `series`;
CREATE TABLE `series`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of series
-- ----------------------------
INSERT INTO `series` VALUES (3, '系列2');
INSERT INTO `series` VALUES (4, '系列3');
INSERT INTO `series` VALUES (5, '系列4');
INSERT INTO `series` VALUES (7, '系列6');
INSERT INTO `series` VALUES (8, '系列7');
INSERT INTO `series` VALUES (9, '系列8');
INSERT INTO `series` VALUES (11, '系列10');
INSERT INTO `series` VALUES (12, '系列11');
INSERT INTO `series` VALUES (13, '系列12');

-- ----------------------------
-- Table structure for supplier
-- ----------------------------
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `contact` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `telephone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `companyName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of supplier
-- ----------------------------
INSERT INTO `supplier` VALUES (2, '地址1', '联系人1', '12323454235', '公司1');
INSERT INTO `supplier` VALUES (3, '地址2', '联系人2', '12323454235', '公司2');
INSERT INTO `supplier` VALUES (4, '地址3', '联系人3', '12323454235', '公司3');
INSERT INTO `supplier` VALUES (5, '地址4', '联系人4', '12323454235', '公司4');
INSERT INTO `supplier` VALUES (6, '地址5', '联系人5', '12323454235', '公司5');
INSERT INTO `supplier` VALUES (7, '地址6', '联系人6', '12323454235', '公司6');
INSERT INTO `supplier` VALUES (8, '地址7', '联系人7', '12323454235', '公司7');
INSERT INTO `supplier` VALUES (9, '地址8', '联系人8', '12323454235', '公司8');
INSERT INTO `supplier` VALUES (10, '地址9', '联系人9', '12323454235', '公司9');
INSERT INTO `supplier` VALUES (11, '地址10', '联系人10', '12323454235', '公司10');
INSERT INTO `supplier` VALUES (12, '地址11', '联系人11', '12323454235', '公司11');
INSERT INTO `supplier` VALUES (13, '地址12', '联系人12', '12323454235', '公司12');
INSERT INTO `supplier` VALUES (14, '地址13', '联系人13', '12323454235', '公司13');
INSERT INTO `supplier` VALUES (15, '地址14', '联系人14', '12323454235', '公司14');
INSERT INTO `supplier` VALUES (16, '地址15', '联系人15', '12323454235', '公司15');
INSERT INTO `supplier` VALUES (17, '地址16', '联系人16', '12323454235', '公司16');
INSERT INTO `supplier` VALUES (18, '地址17', '联系人17', '12323454235', '公司17');
INSERT INTO `supplier` VALUES (19, '地址18', '联系人18', '12323454235', '公司18');
INSERT INTO `supplier` VALUES (20, '地址19', '联系人19', '12323454235', '公司19');
INSERT INTO `supplier` VALUES (21, '地址20', '联系人20', '12323454235', '公司20');
INSERT INTO `supplier` VALUES (22, '地址21', '联系人21', '12323454235', '公司21');
INSERT INTO `supplier` VALUES (23, '地址22', '联系人22', '12323454235', '公司22');
INSERT INTO `supplier` VALUES (24, '地址23', '联系人23', '12323454235', '公司23');
INSERT INTO `supplier` VALUES (25, '地址24', '联系人24', '12323454235', '公司24');
INSERT INTO `supplier` VALUES (26, '地址25', '联系人25', '12323454235', '公司25');
INSERT INTO `supplier` VALUES (27, '地址26', '联系人26', '12323454235', '公司26');
INSERT INTO `supplier` VALUES (28, '地址27', '联系人27', '12323454235', '公司27');
INSERT INTO `supplier` VALUES (29, '地址28', '联系人28', '12323454235', '公司28');
INSERT INTO `supplier` VALUES (30, '地址29', '联系人29', '12323454235', '公司29');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'admin', '21232f297a57a5a743894a0e4a801fc3');
INSERT INTO `user` VALUES (4, 'admin2', 'admin2', 'c84258e9c39059a89ab77d846ddab909');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int(11) NOT NULL,
  `roleId` int(11) NULL DEFAULT NULL,
  `userId` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_role_ibfk_1`(`roleId`) USING BTREE,
  INDEX `userId`(`userId`) USING BTREE,
  CONSTRAINT `user_role_ibfk_1` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_role_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (0, 2, 4);
INSERT INTO `user_role` VALUES (1, 1, 1);

-- ----------------------------
-- Procedure structure for copy_order_data
-- ----------------------------
DROP PROCEDURE IF EXISTS `copy_order_data`;
delimiter ;;
CREATE PROCEDURE `copy_order_data`(IN p_source VARCHAR (100))
BEGIN
  -- 需要定义接收游标数据的变量 
  DECLARE done BOOLEAN DEFAULT 0 ;
  -- 自定义变量
  DECLARE var_price DOUBLE DEFAULT NULL ;
  DECLARE var_pay_time TIMESTAMP DEFAULT NULL ;
  DECLARE var_product VARCHAR (100) DEFAULT NULL ;
  DECLARE var_source VARCHAR (100) DEFAULT NULL ;
  -- 声明游标
  DECLARE cur CURSOR FOR 
  -- 作用于哪个语句
  SELECT 
    price,
    pay_time,
    product,
    source 
  FROM
    cms_aw_order 
  WHERE source = p_source ;
  -- 设置结束标志
  -- 这条语句定义了一个 CONTINUE HANDLER，它是在条件出现时被执行的代码。这里，它指出当 SQLSTATE '02000'出现时，SET done=1 。SQLSTATE '02000'是一个未找到条件，当REPEAT由于没有更多的行供循环而不能继续时，出现这个条件
  DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1 ;
  -- 打开游标
  OPEN cur ;
  -- 使用repeat循环语法
  REPEAT
    -- 批读取数据到指定变量上
    FETCH cur INTO var_price,
    var_pay_time,
    var_product,
    var_source ;
    -- 进行逻辑操作
    INSERT INTO cms_aw_order_copy (price, pay_time, product, source) 
    VALUES
      (
        var_price,
        var_pay_time,
        var_product,
        var_source
      ) ;
    -- 循环结束条件
    UNTIL done 
  END REPEAT ;
  -- 关闭游标
  CLOSE cur ;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for deleteFormulaDesc
-- ----------------------------
DROP PROCEDURE IF EXISTS `deleteFormulaDesc`;
delimiter ;;
CREATE PROCEDURE `deleteFormulaDesc`(IN param_id INT)
BEGIN
delete from formula where formula.id = param_id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for deleteMaterial
-- ----------------------------
DROP PROCEDURE IF EXISTS `deleteMaterial`;
delimiter ;;
CREATE PROCEDURE `deleteMaterial`(IN param_id INT)
BEGIN
delete from material where material.id = param_id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for deletePCBySid
-- ----------------------------
DROP PROCEDURE IF EXISTS `deletePCBySid`;
delimiter ;;
CREATE PROCEDURE `deletePCBySid`(IN param_id INT)
BEGIN
delete from catogery where catogery.seriesId= param_id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for deleteprivilege_rolse
-- ----------------------------
DROP PROCEDURE IF EXISTS `deleteprivilege_rolse`;
delimiter ;;
CREATE PROCEDURE `deleteprivilege_rolse`(IN param_id INT)
BEGIN
delete from role_privilege where role_privilege.roleId = param_id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for deleteProduct
-- ----------------------------
DROP PROCEDURE IF EXISTS `deleteProduct`;
delimiter ;;
CREATE PROCEDURE `deleteProduct`(IN param_product_id INT)
BEGIN
delete from product where product.id = param_product_id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for deleteRoles_privilegerolse
-- ----------------------------
DROP PROCEDURE IF EXISTS `deleteRoles_privilegerolse`;
delimiter ;;
CREATE PROCEDURE `deleteRoles_privilegerolse`(IN param_id INT)
BEGIN
delete from role where role.id = param_id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for deleteSC
-- ----------------------------
DROP PROCEDURE IF EXISTS `deleteSC`;
delimiter ;;
CREATE PROCEDURE `deleteSC`(IN param_id INT)
BEGIN
delete from series where series.id = param_id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for deleteSupplier
-- ----------------------------
DROP PROCEDURE IF EXISTS `deleteSupplier`;
delimiter ;;
CREATE PROCEDURE `deleteSupplier`(IN param_id INT)
BEGIN
delete from supplier where supplier.id = param_id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for deleteUser
-- ----------------------------
DROP PROCEDURE IF EXISTS `deleteUser`;
delimiter ;;
CREATE PROCEDURE `deleteUser`(IN param_id INT)
BEGIN
delete from user where user.id = param_id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for deleteuser_roles
-- ----------------------------
DROP PROCEDURE IF EXISTS `deleteuser_roles`;
delimiter ;;
CREATE PROCEDURE `deleteuser_roles`(IN param_user_id INT)
BEGIN
delete from user_role where user_role.userId = param_user_id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for delete_PSC
-- ----------------------------
DROP PROCEDURE IF EXISTS `delete_PSC`;
delimiter ;;
CREATE PROCEDURE `delete_PSC`(IN param_product_id INT)
BEGIN
delete from product_series_catogery where product_series_catogery.productId = param_product_id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for formulaTotalCount
-- ----------------------------
DROP PROCEDURE IF EXISTS `formulaTotalCount`;
delimiter ;;
CREATE PROCEDURE `formulaTotalCount`()
BEGIN
select count(*) from formula;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for insertCategoryBySeriesID
-- ----------------------------
DROP PROCEDURE IF EXISTS `insertCategoryBySeriesID`;
delimiter ;;
CREATE PROCEDURE `insertCategoryBySeriesID`(IN param_series_id INT,IN param_category_name varchar(32))
BEGIN
insert into catogery(catogery.name,catogery.seriesId) values(param_category_name,param_series_id);
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for insertFormulaDesc
-- ----------------------------
DROP PROCEDURE IF EXISTS `insertFormulaDesc`;
delimiter ;;
CREATE PROCEDURE `insertFormulaDesc`(OUT param_id INT,IN param_formula_name varchar(32),IN param_product_code varchar(32),IN param_formula_number varchar(32),IN param_product_name varchar(32),IN param_batch_number varchar(32),IN param_plan_amount decimal,IN param_actual_output decimal,IN param_water_ph decimal,IN param_ele_conductivity decimal,IN param_equipment_state varchar(32),IN param_product_date varchar(32),IN param_physicochemical_target varchar(32),IN param_engineer varchar(32),IN param_material_weigher varchar(32),IN param_material_checker varchar(32),IN param_material_distributor varchar(32),IN supervisor varchar(32),IN param_technology_proc varchar(1000),IN param_exception_record varchar(320),IN param_attention_item varchar(320),IN param_emul_start_time varchar(32),IN param_emul_end_time varchar(32))
BEGIN
-- 定义接收游标数据的变量
	DECLARE done BOOLEAN DEFAULT 0;
	-- 声明游标
	DECLARE cur CURSOR FOR
	-- 作用于哪个语句
	select LAST_INSERT_ID();
	-- 设置结束标志
	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;
	insert into formula(formula.formula_name,formula.product_code,formula.formula_number,formula.product_name,formula.batch_number,formula.plan_amount,formula.actual_output,formula.water_ph,formula.ele_conductivity,formula.equipment_state,formula.product_date,formula.physicochemical_target,formula.engineer,formula.material_weighter,formula.material_checker,formula.material_distributor,formula.supervisor,formula.technology_proc,formula.exception_record,formula.attention_item,formula.emulStartTime,formula.emulEndTime) values(param_formula_name,param_product_code,param_formula_number,param_product_name,param_batch_number,param_plan_amount,param_actual_output, param_water_ph,param_ele_conductivity,param_equipment_state,param_product_date,param_physicochemical_target,param_engineer,param_material_weigher,param_material_checker,param_material_distributor,supervisor,param_technology_proc,param_exception_record,param_attention_item,param_emul_start_time,param_emul_end_time);
	-- 打开游标
	OPEN cur;
	-- 使用REPEAT循环语法
	REPEAT
		-- 批读取数据到指定变量上
		FETCH cur into param_id;
		-- 循环结束条件
		UNTIL done
	END REPEAT;
	-- 关闭游标
	CLOSE cur;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for insertF_Material
-- ----------------------------
DROP PROCEDURE IF EXISTS `insertF_Material`;
delimiter ;;
CREATE PROCEDURE `insertF_Material`(IN param_group varchar(32),IN param_name varchar(32),IN param_plan_amount decimal,IN param_actual_amount decimal,IN param_material_batch_num varchar(32),IN param_checked_weight decimal,IN param_material_id INT,IN param_formula_id INT)
BEGIN
insert into formula_material(formula_material.`group`,formula_material.`name`,formula_material.plan_amount,formula_material.actual_amout,formula_material.m_batch_num,formula_material.checked_weight,formula_material.materialId,formula_material.formulaId) values(param_group,param_name,param_plan_amount,param_actual_amount,param_material_batch_num,param_checked_weight,param_material_id,param_formula_id);
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for insertOrReplaceMaterial
-- ----------------------------
DROP PROCEDURE IF EXISTS `insertOrReplaceMaterial`;
delimiter ;;
CREATE PROCEDURE `insertOrReplaceMaterial`(IN param_id INT,IN param_chinese_name varchar(32),IN param_name varchar(32),IN param_origin varchar(32),IN param_m_apparent_state varchar(32),IN param_sid INT,IN param_code varchar(32),IN param_inci_en varchar(32),IN param_inci_cn varchar(32),IN param_application varchar(32),IN param_packing_way VARCHAR(32),IN param_price DECIMAL,IN param_unit varchar(32))
BEGIN
if(param_id is null) then
	insert into material(chineseName,material.name,origin,mApparentState,supplierId,material.code,inci_en,inci_cn,application,packingWay,price,unit) values(param_chinese_name,param_name,param_origin,param_m_apparent_state,param_sid,param_code,param_inci_en,param_inci_cn,param_application,param_packing_way,param_price,param_unit);
else
	update material set chineseName=param_chinese_name,material.name = param_name,origin=param_origin,mApparentState= param_m_apparent_state,supplierId=param_sid,material.code=param_code,inci_en=param_inci_en,inci_cn=param_inci_cn,application=param_application,packingWay=param_packing_way,price=param_price,unit=param_unit where material.id = param_id;
end if;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for insertOrReplaceProduct
-- ----------------------------
DROP PROCEDURE IF EXISTS `insertOrReplaceProduct`;
delimiter ;;
CREATE PROCEDURE `insertOrReplaceProduct`(INOUT param_id INT,IN param_name varchar(32),IN param_number varchar(32),IN param_fid varchar(32),IN param_formula_desc varchar(32),IN param_major_composition varchar(32),IN param_sample_quantity DOUBLE,IN param_price DOUBLE)
BEGIN
-- 定义接收游标数据的变量
	DECLARE done BOOLEAN DEFAULT 0;
	-- 声明游标
	DECLARE cur CURSOR FOR
	-- 作用于哪个语句
	select LAST_INSERT_ID();
	-- 设置结束标志
	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;
if(param_id is null) then
	insert into product(name,number,formula,formula_desc,price,major_composition,sample_quantity) values(param_name,param_number,param_fid,param_formula_desc,param_price,param_major_composition,param_sample_quantity);
	-- 打开游标
	OPEN cur;
	-- 使用REPEAT循环语法
	REPEAT
		-- 批读取数据到指定变量上
		FETCH cur into param_id;
		-- 循环结束条件
		UNTIL done
	END REPEAT;
	-- 关闭游标
	CLOSE cur;
else
	update product set name=param_name,number = param_number,formula=param_fid,formula_desc= param_formula_desc,price=param_price,major_composition=param_major_composition,sample_quantity=param_sample_quantity where product.id = param_id;
end if;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for insertOrReplaceRoles
-- ----------------------------
DROP PROCEDURE IF EXISTS `insertOrReplaceRoles`;
delimiter ;;
CREATE PROCEDURE `insertOrReplaceRoles`(IN param_id INT,IN param_name varchar(32),IN param_desc varchar(32))
BEGIN
if(param_id is null) then
	insert into role(role.name,role.desc) values(param_name,param_desc);
else
	update role set role.name=param_name,role.desc=param_desc where role.id = param_id;
end if;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for insertOrReplaceSupplier
-- ----------------------------
DROP PROCEDURE IF EXISTS `insertOrReplaceSupplier`;
delimiter ;;
CREATE PROCEDURE `insertOrReplaceSupplier`(IN param_id INT,IN param_address varchar(32),IN param_telephone varchar(32),IN param_contact varchar(32),IN param_company_name varchar(32))
BEGIN
if(param_id is null) then
	insert into supplier(address,contact,telephone,companyName) values(param_address,param_contact,param_telephone,param_company_name);
else
	update supplier set address=param_address,contact=param_contact,telephone=param_telephone,companyName=param_company_name where supplier.id = param_id;
end if;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for insertOrReplaceUserTable
-- ----------------------------
DROP PROCEDURE IF EXISTS `insertOrReplaceUserTable`;
delimiter ;;
CREATE PROCEDURE `insertOrReplaceUserTable`(OUT param_id INT,IN param_account varchar(32),IN param_password varchar(32),IN param_name varchar(32))
BEGIN
-- 定义接收游标数据的变量
	DECLARE done BOOLEAN DEFAULT 0;
	-- 声明游标
	DECLARE cur CURSOR FOR
	-- 作用于哪个语句
	select LAST_INSERT_ID();
	-- 设置结束标志
	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;
if(param_id is null) then
	insert into user(user.account,user.`password`,user.`name`) values(param_account,param_password,param_name);
	-- 打开游标
	OPEN cur;
	-- 使用REPEAT循环语法
	REPEAT
		-- 批读取数据到指定变量上
		FETCH cur into param_id;
		-- 循环结束条件
		UNTIL done
	END REPEAT;
	-- 关闭游标
	CLOSE cur;
else
	update user set user.account=param_account,user.`password` = param_password,user.`name`=param_name where user.id = param_id;
end if;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for insertPC
-- ----------------------------
DROP PROCEDURE IF EXISTS `insertPC`;
delimiter ;;
CREATE PROCEDURE `insertPC`(IN param_series_id INT,IN param_name varchar(32))
BEGIN
insert into catogery(catogery.`name`,catogery.seriesId) values(param_name,param_series_id);
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for insertprivilege_rolse
-- ----------------------------
DROP PROCEDURE IF EXISTS `insertprivilege_rolse`;
delimiter ;;
CREATE PROCEDURE `insertprivilege_rolse`(IN param_privilege_id INT,IN param_role_id INT)
BEGIN
insert into role_privilege(role_privilege.roleId,role_privilege.privilegeId) values(param_role_id,param_privilege_id);
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for insertP_Category
-- ----------------------------
DROP PROCEDURE IF EXISTS `insertP_Category`;
delimiter ;;
CREATE PROCEDURE `insertP_Category`(OUT param_id INT,IN param_name varchar(32),IN param_series_id INT)
BEGIN
-- 定义接收游标数据的变量
	DECLARE done BOOLEAN DEFAULT 0;
	-- 声明游标
	DECLARE cur CURSOR FOR
	-- 作用于哪个语句
	select LAST_INSERT_ID();
	-- 设置结束标志
	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;
	-- 插入数据
	insert into catogery(catogery.`name`,catogery.seriesId) values(param_name,param_series_id);
	-- 打开游标
	OPEN cur;
	-- 使用REPEAT循环语法
	REPEAT
		-- 批读取数据到指定变量上
		FETCH cur into param_id;
		-- 循环结束条件
		UNTIL done
	END REPEAT;
	-- 关闭游标
	CLOSE cur;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for insertP_Series
-- ----------------------------
DROP PROCEDURE IF EXISTS `insertP_Series`;
delimiter ;;
CREATE PROCEDURE `insertP_Series`(OUT param_id INT,IN param_name varchar(32))
BEGIN
-- 定义接收游标数据的变量
	DECLARE done BOOLEAN DEFAULT 0;
	-- 声明游标
	DECLARE cur CURSOR FOR
	-- 作用于哪个语句
	select LAST_INSERT_ID();
	-- 设置结束标志
	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;
	-- 插入数据
	insert into series(series.`name`) values(param_name);
	-- 打开游标
	OPEN cur;
	-- 使用REPEAT循环语法
	REPEAT
		-- 批读取数据到指定变量上
		FETCH cur into param_id;
		-- 循环结束条件
		UNTIL done
	END REPEAT;
	-- 关闭游标
	CLOSE cur;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for insertuser_roles
-- ----------------------------
DROP PROCEDURE IF EXISTS `insertuser_roles`;
delimiter ;;
CREATE PROCEDURE `insertuser_roles`(IN param_user_id INT,IN param_role_id INT)
BEGIN
insert into user_role(user_role.roleId,user_role.userId) values(param_role_id,param_user_id);
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for insert_PSC
-- ----------------------------
DROP PROCEDURE IF EXISTS `insert_PSC`;
delimiter ;;
CREATE PROCEDURE `insert_PSC`(IN param_product_id INT,IN param_series_id INT,IN param_catogery_id INT)
BEGIN
insert into product_series_catogery(productId,seriesId,product_series_catogery.catogeryId) values(param_product_id,param_series_id,param_catogery_id);
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for materialTotalCount
-- ----------------------------
DROP PROCEDURE IF EXISTS `materialTotalCount`;
delimiter ;;
CREATE PROCEDURE `materialTotalCount`()
BEGIN
select count(*) from material,supplier where material.supplierId = supplier.id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for proc_FormulaDesc_update
-- ----------------------------
DROP PROCEDURE IF EXISTS `proc_FormulaDesc_update`;
delimiter ;;
CREATE PROCEDURE `proc_FormulaDesc_update`(IN param_formula_name varchar(32),IN param_product_code varchar(32),IN param_formula_number varchar(32),IN param_product_name varchar(32),IN param_batch_number varchar(32),IN param_plan_amount decimal,IN param_actual_output decimal,IN param_watch_ph decimal,IN param_ele_conductivity decimal,IN param_equipment_state varchar(32),IN param_product_date varchar(32),In param_physicochemical_target varchar(1000),IN param_engineer varchar(32),IN param_material_weigher varchar(32),IN param_material_checker varchar(32),IN param_material_distributor varchar(32),In param_supervisor varchar(32),IN param_procList varchar(1000),IN param_exception_record varchar(1000),IN param_attention_list varchar(1000),IN param_emul_start_time varchar(32),IN param_emul_end_time varchar(32),IN param_id INT)
BEGIN
update formula set formula.formula_name = param_formula_name,formula.product_code=param_product_code,formula.formula_number = param_formula_number,formula.product_name = param_product_name,formula.batch_number = param_batch_number,formula.plan_amount = param_plan_amount,formula.actual_output = param_actual_output,formula.water_ph = param_watch_ph,formula.ele_conductivity = param_ele_conductivity,formula.equipment_state = param_equipment_state,formula.product_date = param_product_date,formula.physicochemical_target = param_physicochemical_target,formula.engineer = param_engineer,formula.material_weighter = param_material_weigher,formula.material_checker = param_material_checker,formula.material_distributor = param_material_distributor,formula.supervisor = param_supervisor,formula.procList = param_procList,formula.exception_record = param_exception_record,formula.attentionList = param_attention_list,formula.emulStartTime = param_emul_start_time,formula.emulEndTime = param_emul_end_time WHERE formula.id = param_id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for proc_F_Material_delete
-- ----------------------------
DROP PROCEDURE IF EXISTS `proc_F_Material_delete`;
delimiter ;;
CREATE PROCEDURE `proc_F_Material_delete`(IN param_id INT)
BEGIN
delete from formula_material where formula_material.formulaId = param_id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for proc_F_Material_insert
-- ----------------------------
DROP PROCEDURE IF EXISTS `proc_F_Material_insert`;
delimiter ;;
CREATE PROCEDURE `proc_F_Material_insert`(IN param_group varchar(32),IN param_name varchar(32),IN param_plan_amount decimal,IN param_actual_amount decimal,IN param_m_batch_num decimal,IN param_checked_weight decimal,IN param_material_id INT,IN param_formula_id INT)
BEGIN
insert into formula_material(formula_material.group,formula_material.`name`,formula_material.plan_amount,formula_material.actual_amout,formula_material.m_batch_num,formula_material.checked_weight,formula_material.materialId,formula_material.formulaId) values(param_group,param_name,param_plan_amount,param_actual_amount,param_m_batch_num,param_checked_weight,param_material_id,param_formula_id);
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for proc_Material_commodityName_select
-- ----------------------------
DROP PROCEDURE IF EXISTS `proc_Material_commodityName_select`;
delimiter ;;
CREATE PROCEDURE `proc_Material_commodityName_select`(IN param_name varchar(32))
BEGIN
select * from material where material.name = param_name;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for productTotalCount
-- ----------------------------
DROP PROCEDURE IF EXISTS `productTotalCount`;
delimiter ;;
CREATE PROCEDURE `productTotalCount`()
BEGIN
select count(*) from product;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for pro_FormulaDescPagination
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_FormulaDescPagination`;
delimiter ;;
CREATE PROCEDURE `pro_FormulaDescPagination`(IN param_formula_name varchar(32),IN param_product_code varchar(32),IN param_offset INT,IN param_rows INT)
BEGIN
select * from formula where formula.product_name like concat('%',param_formula_name,'%') and formula.product_code like concat('%',param_product_code,'%') limit param_offset,param_rows;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for pro_FormulaNumber
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_FormulaNumber`;
delimiter ;;
CREATE PROCEDURE `pro_FormulaNumber`(IN param_formula_number varchar(32))
BEGIN
select formula.formula_number from formula where formula.formula_number = param_formula_number;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for pro_M_Name
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_M_Name`;
delimiter ;;
CREATE PROCEDURE `pro_M_Name`(IN param_name varchar(32))
BEGIN
select material.id,material.`code` from material where material.`name` = param_name;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for pro_M_SPagination
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_M_SPagination`;
delimiter ;;
CREATE PROCEDURE `pro_M_SPagination`(IN param_name varchar(32),IN param_code varchar(32),IN param_inci_cn varchar(32),IN param_offset INT,IN param_rows INT)
BEGIN
select material.id,material.`name` as name,material.origin,material.mApparentState,material.supplierId,supplier.companyName as companyName,material.price,material.unit,material.code,material.inci_cn,material.inci_en,material.application,material.packingWay,material.chineseName from material,supplier where material.`code` like concat('%',param_code,'%') and material.inci_cn like concat('%',param_inci_cn,'%') and material.`name` like concat('%',param_name,'%') and material.supplierId = supplier.id limit param_offset,param_rows;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for pro_ProductPagination
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_ProductPagination`;
delimiter ;;
CREATE PROCEDURE `pro_ProductPagination`(IN param_offset INT,IN param_rows INT,IN param_sid INT,IN param_cid INT,IN param_name VARCHAR(32),IN param_number VARCHAR(32))
BEGIN
	if (param_sid is null and param_cid is null and param_number is null) then
		select * from product where name like concat('%',param_name,'%') limit param_offset,param_rows;
	elseif(param_sid is not null and param_cid is null and param_number is null) then
		select product.id,product.name,product.number,product.formula,product.formula_desc,product.price,product.major_composition,product.sample_quantity from product,product_series_catogery where param_sid = product_series_catogery.seriesId and product.id = product_series_catogery.productId and name like concat('%',param_name,'%') limit param_offset,param_rows;
	elseif(param_sid is not null and param_cid is not null and param_number is null) then
		select product.id,product.name,product.number,product.formula,product.formula_desc,product.price,product.major_composition,product.sample_quantity from product,product_series_catogery where param_sid = product_series_catogery.seriesId and param_cid = product_series_catogery.catogeryId and product.id = product_series_catogery.productId and name like concat('%',param_name,'%') limit param_offset,param_rows;
  elseif(param_sid is not null and param_cid is not null and param_number is not null) then
		select product.id,product.name,product.number,product.formula,product.formula_desc,product.price,product.major_composition,product.sample_quantity from product,product_series_catogery where param_sid = product_series_catogery.seriesId and param_cid = product_series_catogery.catogeryId and product.id = product_series_catogery.productId and name like concat('%',param_name,'%') and param_number = product.number limit param_offset,param_rows;
	elseif(param_sid is null and param_cid is null and param_number is not null) then
		select * from product where name like concat('%',param_name,'%') and param_number = product.number limit param_offset,param_rows;
	elseif(param_sid is not null and param_cid is null and param_number is not null) then
		select product.id,product.name,product.number,product.formula,product.formula_desc,product.price,product.major_composition,product.sample_quantity from product,product_series_catogery where param_sid = product_series_catogery.seriesId and product.id = product_series_catogery.productId and name like concat('%',param_name,'%') and param_number = product.number limit param_offset,param_rows;
	end if;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for pro_Roles
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_Roles`;
delimiter ;;
CREATE PROCEDURE `pro_Roles`(IN param_name varchar(32))
BEGIN
select * from role where role.`name` like CONCAT('%',param_name,'%');
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for pro_SeriesPagination
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_SeriesPagination`;
delimiter ;;
CREATE PROCEDURE `pro_SeriesPagination`(IN param_offset INT,IN param_rows INT)
BEGIN
select * from series limit param_offset,param_rows;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for pro_Supplier
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_Supplier`;
delimiter ;;
CREATE PROCEDURE `pro_Supplier`(IN param_keyword varchar(32))
BEGIN
select supplier.id,supplier.companyName from supplier where supplier.companyName like CONCAT('%',param_keyword,'%');
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for pro_SupplierPagination
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_SupplierPagination`;
delimiter ;;
CREATE PROCEDURE `pro_SupplierPagination`(IN param_address varchar(32),IN param_telephone varchar(32),IN param_contact varchar(32),IN param_company_name varchar(32),IN param_offset INT,IN param_rows INT)
BEGIN
if(param_telephone is null) then
select * from supplier where supplier.address like concat('%',param_address,'%') and supplier.contact like concat('%',param_contact,'%') and supplier.companyName like concat('%',param_company_name,'%') limit param_offset,param_rows;
else
select * from supplier where supplier.address like concat('%',param_address,'%') and supplier.telephone = param_telephone and supplier.contact like concat('%',param_contact,'%') and supplier.companyName like concat('%',param_company_name,'%') limit param_offset,param_rows;
end if;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for pro_UserTable
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_UserTable`;
delimiter ;;
CREATE PROCEDURE `pro_UserTable`(IN param_account varchar(32),IN param_name varchar(32))
BEGIN
select id,account,password,name from user where user.name like concat('%',param_name,'%') and user.account like concat('%',param_account,'%');
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for selectAllMaterial
-- ----------------------------
DROP PROCEDURE IF EXISTS `selectAllMaterial`;
delimiter ;;
CREATE PROCEDURE `selectAllMaterial`()
BEGIN
select material.id,material.`name`,material.origin,material.mApparentState,material.supplierId,material.price,material.unit,material.`code`,material.inci_cn,material.inci_en,material.application,material.packingWay from material;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for selectAllPrivilege
-- ----------------------------
DROP PROCEDURE IF EXISTS `selectAllPrivilege`;
delimiter ;;
CREATE PROCEDURE `selectAllPrivilege`()
BEGIN
select * from privilege;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for selectAllP_Series
-- ----------------------------
DROP PROCEDURE IF EXISTS `selectAllP_Series`;
delimiter ;;
CREATE PROCEDURE `selectAllP_Series`()
BEGIN
select * from series;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for selectAllSupplier
-- ----------------------------
DROP PROCEDURE IF EXISTS `selectAllSupplier`;
delimiter ;;
CREATE PROCEDURE `selectAllSupplier`()
BEGIN
select * from supplier;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for selectByPid_PS
-- ----------------------------
DROP PROCEDURE IF EXISTS `selectByPid_PS`;
delimiter ;;
CREATE PROCEDURE `selectByPid_PS`(IN param_productId INT)
BEGIN
select series.id,series.`name` from series,product_series_catogery,product where product.id = product_series_catogery.productId and product_series_catogery.seriesId = series.id and product.id = param_productId;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for selectByPSid_PC
-- ----------------------------
DROP PROCEDURE IF EXISTS `selectByPSid_PC`;
delimiter ;;
CREATE PROCEDURE `selectByPSid_PC`(IN param_seriesId INT)
BEGIN
select * from catogery where seriesId = param_seriesId;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for selectFM_Material
-- ----------------------------
DROP PROCEDURE IF EXISTS `selectFM_Material`;
delimiter ;;
CREATE PROCEDURE `selectFM_Material`(IN param_material_id INT)
BEGIN
select material.unit,material.price,formula_material.plan_amount,material.`code`,formula_material.`group`,formula_material.`name` from material,formula_material where material.id = param_material_id and material.id = formula_material.materialId;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for selectFormulaDescById
-- ----------------------------
DROP PROCEDURE IF EXISTS `selectFormulaDescById`;
delimiter ;;
CREATE PROCEDURE `selectFormulaDescById`(IN param_id INT)
BEGIN
select formula.id,formula.formula_name,formula.product_name,formula.formula_number,formula.product_name,formula.batch_number,formula.plan_amount,formula.actual_output,formula.water_ph,formula.ele_conductivity,formula.equipment_state,formula.product_date,formula.physicochemical_target,formula.engineer,formula.material_weighter,formula.material_checker,formula.material_distributor,formula.supervisor,formula.technology_proc,formula.exception_record,formula.attention_item,formula.emulStartTime,formula.emulEndTime from formula where formula.id = param_id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for selectMaterial
-- ----------------------------
DROP PROCEDURE IF EXISTS `selectMaterial`;
delimiter ;;
CREATE PROCEDURE `selectMaterial`(IN param_id INT)
BEGIN
select * from material where material.id = param_id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for selectMaterialByName
-- ----------------------------
DROP PROCEDURE IF EXISTS `selectMaterialByName`;
delimiter ;;
CREATE PROCEDURE `selectMaterialByName`(IN param_name varchar(32))
BEGIN
select * from material where material.name = param_name;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for selectPrivilegeByRid
-- ----------------------------
DROP PROCEDURE IF EXISTS `selectPrivilegeByRid`;
delimiter ;;
CREATE PROCEDURE `selectPrivilegeByRid`(IN param_rid VARCHAR(30))
BEGIN
select privilege.id,privilege.`desc`,privilege.`name`,privilege.url,privilege.parentid from role,role_privilege,privilege where role.id = param_rid and role.id = role_privilege.roleId and role_privilege.privilegeId = privilege.id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for selectPrivilegeCodeByUID
-- ----------------------------
DROP PROCEDURE IF EXISTS `selectPrivilegeCodeByUID`;
delimiter ;;
CREATE PROCEDURE `selectPrivilegeCodeByUID`(IN param_uid VARCHAR(30))
BEGIN
select privilege.name as code from role inner join role_privilege on role.id = role_privilege.roleId 
join privilege on role_privilege.privilegeId = privilege.id where role.id in 
(select role.id from user inner join user_role on user.id = user_role.userId
inner join role on role.id = user_role.roleId where user.id = param_uid);
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for selectP_Series
-- ----------------------------
DROP PROCEDURE IF EXISTS `selectP_Series`;
delimiter ;;
CREATE PROCEDURE `selectP_Series`(IN param_id INT)
BEGIN
select series.id,series.name from series where series.id = param_id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for selectRolesByUID
-- ----------------------------
DROP PROCEDURE IF EXISTS `selectRolesByUID`;
delimiter ;;
CREATE PROCEDURE `selectRolesByUID`(IN param_uid VARCHAR(30))
BEGIN
select role.id as id,role.`desc` as role_desc,role.`name` as name
from user inner join user_role on user.id = user_role.userId
inner join role on role.id = user_role.roleId where user.id = param_uid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for selectSingleFM_Material
-- ----------------------------
DROP PROCEDURE IF EXISTS `selectSingleFM_Material`;
delimiter ;;
CREATE PROCEDURE `selectSingleFM_Material`(IN param_id INT)
BEGIN
select formula_material.`group`,formula_material.name,formula_material.cn_name,formula_material.plan_amount,formula_material.actual_amout,formula_material.actual_amout,formula_material.m_batch_num,formula_material.checked_weight,material.price,material.unit,material.inci_cn,material.inci_en,material.code from formula_material,material where formula_material.formulaId = param_id and formula_material.materialId = material.id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for SelectUserTable
-- ----------------------------
DROP PROCEDURE IF EXISTS `SelectUserTable`;
delimiter ;;
CREATE PROCEDURE `SelectUserTable`(IN param_account VARCHAR(30))
BEGIN
select user.id,user.account,user.`password`,user.`name` from user where user.account = param_account;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for select_P_Presentation
-- ----------------------------
DROP PROCEDURE IF EXISTS `select_P_Presentation`;
delimiter ;;
CREATE PROCEDURE `select_P_Presentation`(IN param_productId INT,IN param_seriesId INT)
BEGIN
select catogery.id,catogery.`name` from product,catogery,series,product_series_catogery where product.id = product_series_catogery.productId and product_series_catogery.seriesId = series.id and product_series_catogery.catogeryId = catogery.id and product.id = param_productId and series.id = param_seriesId;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for select_RU
-- ----------------------------
DROP PROCEDURE IF EXISTS `select_RU`;
delimiter ;;
CREATE PROCEDURE `select_RU`(IN param_id INT)
BEGIN
select role.id,role.name,role.desc from role,user,user_role where role.id = user_role.roleId and user.id = user_role.userId and user.id = param_id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for seriesTotalCount
-- ----------------------------
DROP PROCEDURE IF EXISTS `seriesTotalCount`;
delimiter ;;
CREATE PROCEDURE `seriesTotalCount`()
BEGIN
select count(*) from series;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for supplierTotalCount
-- ----------------------------
DROP PROCEDURE IF EXISTS `supplierTotalCount`;
delimiter ;;
CREATE PROCEDURE `supplierTotalCount`()
BEGIN
select count(*) from supplier;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for updateP_Series
-- ----------------------------
DROP PROCEDURE IF EXISTS `updateP_Series`;
delimiter ;;
CREATE PROCEDURE `updateP_Series`(IN param_id INT,IN param_name varchar(32))
BEGIN
update series set series.name = param_name where series.id = param_id;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
