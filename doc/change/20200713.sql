/*==============================================================*/
/* Table: tallying_area                                         */
/*==============================================================*/
drop table if exists tallying_area;
create table tallying_area
(
   id                   bigint not null auto_increment comment '主键ID',
   customer_id          bigint comment '客户ID',
   market_id            bigint comment '客户理货区市场',
   department_id        bigint comment '理货区所属部门',
   assets_id            bigint comment '理货区(资产)ID',
   assets_name          varchar(32) comment '理货区(资产)名称',
   is_lease             tinyint comment '是否存在租赁关系',
   is_usable            tinyint comment '是否合法可用的数据(根据有租赁且有效或没有租赁算出)',
   start_time           datetime comment '租赁开始时间',
   end_time             datetime comment '租赁结束时间',
   state                int comment '租赁状态',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (id)
);
alter table tallying_area comment '客户理货区关联关系';
/*==============================================================*/
/* Index: idx_ta_customer_id                                    */
/*==============================================================*/
create index idx_ta_customer_id on tallying_area
(
   customer_id
);

/*==============================================================*/
/* Table: address                                               */
/*==============================================================*/
drop table if exists address;
create table address
(
   id                   bigint not null auto_increment comment 'ID',
   customer_id          bigint comment '客户ID',
   market_id            bigint comment '所属市场ID',
   city_path            varchar(30) comment '所在城市ID路径',
   city_name            varchar(50) comment '所在城市合并名称',
   address              varchar(250) comment '地址',
   is_current           tinyint comment '是否现住址',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   creator_id           bigint comment '创建人',
   modifier_id          bigint comment '修改人',
   primary key (id)
);
alter table address comment '客户联系地址信息';
/*==============================================================*/
/* Index: idx_a_market_id                                       */
/*==============================================================*/
create index idx_a_market_id on address
(
   market_id
);
/*==============================================================*/
/* Index: idx_a_customer_id                                     */
/*==============================================================*/
create index idx_a_customer_id on address
(
   customer_id
);

/*==============================================================*/
/* Table: attachment                                            */
/*==============================================================*/
drop table if exists attachment;
create table attachment
(
   id                   bigint not null auto_increment comment 'ID',
   customer_id          bigint comment '客户ID',
   market_id            bigint comment '所属市场ID',
   file_type            integer comment '附件类型',
   address              varchar(255) comment '附件地址',
   file_name            varchar(255) comment '源文件名称',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   modify_time           datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (id)
);
alter table attachment comment '客户上传的附件信息';

ALTER TABLE customer ADD COLUMN `current_city_path` VARCHAR ( 40 ) DEFAULT NULL COMMENT '现住址城市ID路径' AFTER is_cellphone_valid;
ALTER TABLE customer ADD COLUMN `current_city_name` VARCHAR ( 40 ) DEFAULT NULL COMMENT '现住址城市名称' AFTER current_city_path;
ALTER TABLE customer ADD COLUMN `current_address` VARCHAR (255) DEFAULT NULL COMMENT '现住址详细地址' AFTER current_city_name;
ALTER TABLE customer_market ADD COLUMN `sales_market` VARCHAR ( 40 ) DEFAULT NULL COMMENT '销地市场' AFTER other_title;
ALTER TABLE customer_market drop COLUMN `main_category`;
ALTER TABLE customer drop COLUMN `cellphone`;

/*==============================================================*/
/* Table: business_category                                     */
/*==============================================================*/
drop table if exists business_category;
create table business_category
(
   id                   bigint not null comment 'ID',
   customer_id          bigint comment '客户ID',
   market_id            bigint comment '所属市场ID',
   category_id          bigint comment '经营品类ID路径',
   category_name        varchar(40) comment '经营品类名称全路径',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后修改时间',
   primary key (id)
);
alter table business_category comment '客户经营品类信息';
/*==============================================================*/
/* Index: inx_bc_customer_id                                    */
/*==============================================================*/
create index inx_bc_customer_id on business_category
(
   customer_id
);
/*==============================================================*/
/* Index: idx_bc_market_id                                      */
/*==============================================================*/
create index idx_bc_market_id on business_category
(
   market_id
);

CREATE TABLE `related` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户',
  `parent` bigint(20) DEFAULT NULL COMMENT '父客户',
  `market_id` bigint(20) DEFAULT NULL COMMENT '市场id',
  `modify_time` datetime DEFAULT NULL COMMENT '更新时间',
  `related_time_start` datetime DEFAULT NULL COMMENT '关联时间开始',
  `related_time_end` datetime DEFAULT NULL COMMENT '关联时间结束',
  `category` varchar(255) DEFAULT NULL COMMENT '关联商品',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  COMMENT=' 关联客户';

CREATE TABLE `related_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_name` varchar(20) DEFAULT NULL COMMENT '客户名称',
  `phone` varchar(20)  DEFAULT NULL COMMENT '联系电话',
  `cardNo` varchar(20)  DEFAULT NULL COMMENT '园区卡',
  `modify_time` datetime DEFAULT NULL COMMENT '时间',
  `creator` varchar(20) DEFAULT NULL COMMENT '操作员',
  `notes` varchar(100)  DEFAULT NULL COMMENT '操作详情',
  `parent` bigint(255) DEFAULT NULL COMMENT '上级客户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 COMMENT=' 关联客户';
