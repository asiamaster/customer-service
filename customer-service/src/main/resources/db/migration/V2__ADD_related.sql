CREATE TABLE `related` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户',
  `parent` bigint(20) DEFAULT NULL COMMENT '父客户',
  `market_id` bigint(20) DEFAULT NULL COMMENT '市场id',
  `modify_time` datetime DEFAULT NULL COMMENT '更新时间',
  `related_time_start` datetime DEFAULT NULL COMMENT '关联时间开始',
  `related_time_end` datetime DEFAULT NULL COMMENT '关联时间结束',
  `category` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '关联商品',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT=' 关联客户';