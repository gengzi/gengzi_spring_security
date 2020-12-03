/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50548
Source Host           : localhost:3306
Source Database       : sys_manager

Target Server Type    : MYSQL
Target Server Version : 50548
File Encoding         : 65001

Date: 2020-12-03 21:13:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for other_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `other_sys_user`;
CREATE TABLE `other_sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `scope` varchar(64) DEFAULT NULL COMMENT '第三方系统',
  `uuid` varchar(64) DEFAULT NULL COMMENT '第三方系统唯一账户',
  `user_id` bigint(20) DEFAULT NULL COMMENT '关联系统用户表的id',
  `username` varchar(64) DEFAULT NULL COMMENT '登陆用户名',
  `create_time` datetime DEFAULT NULL COMMENT '绑定时间（创建时间）',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`) USING BTREE COMMENT '用户id索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of other_sys_user
-- ----------------------------

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL,
  `permission_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`),
  KEY `idex_role_id_permission_Id` (`role_id`,`permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES ('1', '1', '1');
INSERT INTO `role_permission` VALUES ('2', '1', '2');
INSERT INTO `role_permission` VALUES ('4', '1', '3');
INSERT INTO `role_permission` VALUES ('3', '2', '3');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pid` bigint(20) DEFAULT NULL COMMENT '父id',
  `name` varchar(50) NOT NULL COMMENT '菜单名称',
  `type` tinyint(1) DEFAULT NULL COMMENT '类型   0：菜单   1：按钮',
  `sort` int(11) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL COMMENT '菜单URL',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限标识，如：sys:menu:save',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `remark` varchar(500) DEFAULT NULL COMMENT '说明',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `updater` varchar(100) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `px_id` (`id`) USING BTREE,
  KEY `idx_pid` (`pid`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('1', '0', '系统管理', '0', '1', '/manager', null, null, '0', null, '2020-11-05 09:58:45', 'admin', '2020-11-05 09:58:58', null);
INSERT INTO `sys_permission` VALUES ('2', '1', '查询', '1', '0', null, 'sys:manager:qry', null, '0', null, '2020-11-05 09:59:54', 'admin', '2020-11-05 09:59:57', null);
INSERT INTO `sys_permission` VALUES ('3', '1', '查询test信息', '1', '0', null, 'sys:test:qry', null, '0', null, '2020-11-05 09:59:54', 'admin', '2020-11-05 09:59:57', null);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) NOT NULL COMMENT '角色名称',
  `role_code` varchar(20) NOT NULL COMMENT '角色code',
  `remark` varchar(500) DEFAULT NULL COMMENT '说明',
  `sort` int(10) DEFAULT NULL COMMENT '排序',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识  0：未删除    1：删除',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `updater` varchar(100) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `pk_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '管理员', 'admin', '管理员', '1', '0', 'admin', '2020-11-05 09:52:50', null, '2020-11-05 09:52:50');
INSERT INTO `sys_role` VALUES ('2', '测试用户', 'test', '测试', '2', '0', 'admin', '2020-11-05 10:30:35', null, null);

-- ----------------------------
-- Table structure for sys_users
-- ----------------------------
DROP TABLE IF EXISTS `sys_users`;
CREATE TABLE `sys_users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(30) DEFAULT NULL COMMENT '登陆用户名',
  `realname` varchar(100) DEFAULT NULL COMMENT '真正用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `gender` int(4) DEFAULT NULL COMMENT '性别',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL,
  `create_user` varchar(100) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL,
  `is_enable` int(11) DEFAULT NULL COMMENT '是否可用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`) USING BTREE,
  KEY `pk_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_users
-- ----------------------------
INSERT INTO `sys_users` VALUES ('1', 'admin', '超级管理员', '$2a$10$R8pC1Ne5WQs7N.KXLFTM0uBBnhQ54ED7eOh1S8hAPehrM2cfLbSg2', '1', '1909099090', '3322@qq.com', 'admin', '2020-11-11 15:44:13', '1');
INSERT INTO `sys_users` VALUES ('2', 'test', '测试用户', '$2a$10$HiZ7cadA9QgmvDY77RRx4OFLXAgSQXAIlFZe8DJBL8UqyXVyKUBzK', '1', '1909099090', '2121@qq.com', 'admin', '2020-11-05 10:30:02', '1');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ur_uidandroid_index` (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1', '1');
INSERT INTO `user_role` VALUES ('2', '2', '2');
