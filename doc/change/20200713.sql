drop table if exists tallying_area;
create table tallying_area
(
   id                   bigint not null auto_increment comment '主键ID',
   customer_id          bigint comment '客户ID',
   market_id            bigint comment '客户理货区市场',
   assets_id            bigint comment '理货区(资产)ID',
   assets_name          varchar(32) comment '理货区(资产)名称',
   is_lease             tinyint comment '是否存在租赁关系',
   start_time           datetime comment '租赁开始时间',
   end_time             datetime comment '租赁结束时间',
   state                int comment '租赁状态',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (id)
);
alter table tallying_area comment '客户理货区关联关系';

drop table if exists address;
/*==============================================================*/
/* Table: address                                               */
/*==============================================================*/
create table address
(
   id                   bigint not null auto_increment comment 'ID',
   customer_id          bigint comment '客户ID',
   market_id            bigint comment '所属市场ID',
   city_id              bigint comment '所在城市ID',
   city_name            varchar(50) comment '所在城市合并名称',
   address              varchar(250) comment '地址',
   is_current           tinyint comment '是否现住址',
   is_default           tinyint comment '是否默认地址',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   creator_id           bigint comment '创建人',
   modifier_id          bigint comment '修改人',
   primary key (id)
);
alter table address comment '客户联系地址信息';

ALTER TABLE customer ADD COLUMN `current_city_id` BIGINT DEFAULT NULL COMMENT '现住址城市ID' AFTER is_cellphone_valid;
ALTER TABLE customer ADD COLUMN `current_city_name` VARCHAR ( 40 ) DEFAULT NULL COMMENT '现住址城市名称' AFTER current_city_id;
ALTER TABLE customer ADD COLUMN `current_address` BIGINT DEFAULT NULL COMMENT '现住址详细地址' AFTER current_city_name;
ALTER TABLE customer_market ADD COLUMN `category` VARCHAR ( 512 ) DEFAULT NULL COMMENT '经营品类' AFTER other_title;
ALTER TABLE customer_market ADD COLUMN `sales_market` VARCHAR ( 40 ) DEFAULT NULL COMMENT '销地市场' AFTER category;
update customer_market set category = main_category;
ALTER TABLE customer_market drop COLUMN `main_category`;