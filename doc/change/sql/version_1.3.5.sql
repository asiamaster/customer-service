##调整字段类型,用于存储数据

ALTER TABLE `dili-customer`.customer_market ADD COLUMN `department_ids` VARCHAR(200) DEFAULT NULL COMMENT '客户所属部门,多个以逗号隔开' AFTER department_id;
ALTER TABLE `dili-customer`.customer_market ADD COLUMN `owner_ids` VARCHAR(200) DEFAULT NULL COMMENT '客户所属人,多个以逗号隔开' AFTER owner_id;

update `dili-customer`.customer_market set department_ids = department_id,owner_ids=owner_id;

ALTER TABLE `dili-customer`.customer_market drop COLUMN `department_id`;
ALTER TABLE `dili-customer`.customer_market drop COLUMN `owner_id`;

INSERT INTO `uap`.`data_dictionary` (`code`, `name`, `level`, `system_code`, `description`, `created`, `modified`) VALUES ('customer_data_auth', '客户数据权限隔离', NULL, 'CUSTOMER', '客户数据权限隔离相关配置', now(), now());
INSERT INTO `uap`.`data_dictionary_value` (`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`, `state`) VALUES ('customer_data_auth', 9, 'sy', 2, '按归属人权限隔离', 'customer_owner_auth', NULL, now(), now(), 1);
INSERT INTO `uap`.`data_dictionary_value` (`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`, `state`) VALUES ('customer_data_auth', 9, 'sy', 1, '按部门权限隔离', 'customer_department_auth', NULL, now(), now(), 1);
