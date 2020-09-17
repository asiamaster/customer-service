CREATE TABLE `related_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_name` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '客户名称',
  `phone` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '联系电话',
  `cardNo` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '园区卡',
  `modify_time` datetime DEFAULT NULL COMMENT '时间',
  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '操作员',
  `notes` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '操作详情',
  `parent` bigint(255) DEFAULT NULL COMMENT '上级客户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT=' 关联客户';