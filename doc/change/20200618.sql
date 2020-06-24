#客户市场表中,增加客户等级,客户行业等相关信息
ALTER TABLE customer_market ADD COLUMN `grade` TINYINT DEFAULT NULL COMMENT '客户等级' AFTER owner_id;
ALTER TABLE customer_market ADD COLUMN `profession` VARCHAR ( 32 ) DEFAULT NULL COMMENT '客户行业' AFTER grade;
ALTER TABLE customer_market ADD COLUMN `operating_area` VARCHAR ( 40 ) DEFAULT NULL COMMENT '经营地区' AFTER profession;
ALTER TABLE customer_market ADD COLUMN `operating_lng` VARCHAR ( 20 ) DEFAULT NULL COMMENT '经营地区经度' AFTER operating_area;
ALTER TABLE customer_market ADD COLUMN `operating_lat` VARCHAR ( 20 ) DEFAULT NULL COMMENT '经营地区纬度' AFTER operating_lng;
ALTER TABLE customer_market ADD COLUMN `other_title` VARCHAR ( 40 ) DEFAULT NULL COMMENT '其它头衔' AFTER operating_lat;
ALTER TABLE customer_market ADD COLUMN `main_category` VARCHAR ( 40 ) DEFAULT NULL COMMENT '主营品类' AFTER other_title;
#更新客户市场表的数据
UPDATE customer_market cm,
customer c
SET cm.type='inside_buyer',
cm.grade = c.grade,
cm.profession = c.profession,
cm.operating_area = c.operating_area,
cm.operating_lng = c.operating_lng,
cm.operating_lat = c.operating_lat,
cm.other_title = c.other_title,
cm.main_category = c.main_category
WHERE
	cm.customer_id = c.id;

#删除客户主表上的 客户等级,客户行业等信息
ALTER TABLE customer drop COLUMN `grade`;
ALTER TABLE customer drop COLUMN `profession`;
ALTER TABLE customer drop COLUMN `operating_area`;
ALTER TABLE customer drop COLUMN `operating_lng`;
ALTER TABLE customer drop COLUMN `operating_lat`;
ALTER TABLE customer drop COLUMN `other_title`;
ALTER TABLE customer drop COLUMN `main_category`;

#数据字典配置
INSERT INTO `uap`.`data_dictionary`(`code`, `name`, `level`, `system_code`, `description`, `created`, `modified`) VALUES ('cus_customer_type', '客户身份类型信息', NULL, 'CUSTOMER', '客户身份类型信息', now(), now());
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`) VALUES ('cus_customer_type', 1, '园内买家', 'inside_buyer', NULL, now(), now());
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`) VALUES ('cus_customer_type', 2, '园内卖家', 'inside_seller', NULL, now(), now());
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`) VALUES ('cus_customer_type', 3, '买卖家', 'purchaseSale', NULL, now(), now());
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`) VALUES ('cus_customer_type', 4, '司机', 'driver', NULL, now(), now());