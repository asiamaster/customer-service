##调整字段类型,用于存储数据
ALTER TABLE `dili-customer`.customer_market MODIFY COLUMN department_id VARCHAR(200) COMMENT '客户所属部门,多个以逗号隔开';
ALTER TABLE `dili-customer`.customer_market MODIFY COLUMN owner_id VARCHAR(200) COMMENT '客户所属人,多个以逗号隔开';