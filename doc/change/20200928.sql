ALTER TABLE business_category ADD COLUMN `category_id` BIGINT DEFAULT NULL COMMENT '品类ID' AFTER category_path;
ALTER TABLE business_category ADD COLUMN `category_name` VARCHAR ( 40 ) DEFAULT NULL COMMENT '品类名称' AFTER category_name_path;
ALTER TABLE business_category drop COLUMN `category_path`;
ALTER TABLE business_category drop COLUMN `category_name_path`;