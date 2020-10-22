update customer set is_cellphone_valid = 0 where is_cellphone_valid is null;
ALTER TABLE related_log
MODIFY COLUMN `customer_name` varchar(40)  DEFAULT NULL COMMENT '客户名称' AFTER `id`;
ALTER TABLE related_log
MODIFY COLUMN `cardNo` varchar(200)  DEFAULT NULL COMMENT '园区卡号' AFTER `phone`;