##数据字典 增加客户角色身份
INSERT INTO `uap`.`data_dictionary`(`code`, `name`, `level`, `system_code`, `description`) VALUES ('customer_other_character_type', '客户其它角色身份', NULL, 'CUSTOMER', '客户的其它角色身份');
INSERT INTO `uap`.`data_dictionary`(`code`, `name`, `level`, `system_code`, `description`) VALUES ('customer_buyer_character_type', '客户买家角色身份', NULL, 'CUSTOMER', '客户买家角色身份');
INSERT INTO `uap`.`data_dictionary`(`code`, `name`, `level`, `system_code`, `description`) VALUES ('customer_business_user_character_type', '客户经营户角色身份', NULL, 'CUSTOMER', '经营户角色身份类型');

use dili-customer;
##删除客户表中的字段
ALTER TABLE customer drop COLUMN `cellphone`;

/*==============================================================*/
/* Table: character_type          客户角色身份                    */
/*==============================================================*/
drop table if exists character_type;
create table character_type
(
   id                   bigint not null comment 'ID',
   customer_id          bigint comment '客户ID',
   market_id            bigint comment '所属市场ID',
   character_type       varchar(100) comment '客户角色身份',
   sub_type             varchar(200) comment '客户子身份',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后修改时间',
   primary key (id)
);
alter table character_type comment '客户角色身份信息';
/*==============================================================*/
/* Index: inx_ct_customer_id                                    */
/*==============================================================*/
create index inx_ct_customer_id on character_type
(
   customer_id
);
/*==============================================================*/
/* Index: idx_ct_market_id                                      */
/*==============================================================*/
create index idx_ct_market_id on character_type
(
   market_id
);

/*==============================================================*/
/* Table: vehicle_info    客户车型信息                            */
/*==============================================================*/
drop table if exists vehicle_info;
create table vehicle_info
(
   id                   bigint not null auto_increment comment 'ID',
   customer_id          bigint comment '客户ID',
   market_id            bigint comment '所属市场ID',
   registration_number  varchar(20) comment '注册车牌号',
   type_number          bigint comment '车型编号',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后修改时间',
   creator_id           bigint comment '创建人ID',
   modifier_id          bigint comment '最后修改人ID',
   primary key (id)
);
alter table vehicle_info comment '客户市场车辆信息';
/*==============================================================*/
/* Index: inx_vi_customer_id                                    */
/*==============================================================*/
create index inx_vi_customer_id on vehicle_info
(
   customer_id
);
/*==============================================================*/
/* Index: idx_vi_market_id                                      */
/*==============================================================*/
create index idx_vi_market_id on vehicle_info
(
   market_id
);

ALTER TABLE customer ADD COLUMN `is_certification` TINYINT DEFAULT NULL COMMENT '是否实名认证' AFTER certificate_addr;
update customer SET is_certification = 0;

ALTER TABLE customer_market MODIFY COLUMN profession VARCHAR(200) COMMENT '客户行业代码';
ALTER TABLE customer_market ADD COLUMN `profession_name` VARCHAR(512) DEFAULT NULL COMMENT '客户行业名称' AFTER profession;
ALTER TABLE customer_market ADD COLUMN `business_nature` VARCHAR(120) DEFAULT NULL COMMENT '客户经营性质' AFTER profession_name;
ALTER TABLE customer_market ADD COLUMN `approval_status` tinyint DEFAULT NULL COMMENT '客户资料审核状态' AFTER alias;
ALTER TABLE customer_market ADD COLUMN `approval_user_id` bigint DEFAULT NULL COMMENT '客户资料审核人ID' AFTER approval_status;
ALTER TABLE customer_market ADD COLUMN `approval_time` datetime DEFAULT NULL COMMENT '客户资料审核时间' AFTER approval_user_id;
ALTER TABLE customer_market ADD COLUMN `approval_notes` datetime DEFAULT NULL COMMENT '客户资料审核备注' AFTER approval_time;

update customer_market set approval_status = 2;

INSERT INTO character_type(id,customer_id,market_id,sub_type) SELECT cm.id,cm.customer_id,cm.market_id,type FROM customer_market cm;
update `dili-customer`.character_type ct,uap.data_dictionary_value ddv set ct.character_type=ddv.dd_code where ct.sub_type=ddv.`code` and ct.market_id = ddv.firm_id and ddv.dd_code IN ('business_user_character_type','buyer_character_type','other_character_type');

delete from `uap`.`data_dictionary_value` where dd_code = 'customer_business';
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',1, '农、林、牧、渔业', 'A', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',2, '采矿业', 'B', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',3, '制造业', 'C', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',4, '电力、热力、燃气及水生产和供应业', 'D', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',5, '建筑业', 'E', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',6, '批发和零售业', 'F', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',7, '交通运输、仓储和邮政业', 'G', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',8, '住宿和餐饮业', 'H', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',9, '信息传输、软件和信息技术服务业', 'I', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',10, '金融业', 'J', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',11, '房地产业', 'K', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',12, '租赁和商务服务业', 'L', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',13, '科学研究和技术服务业', 'M', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',14, '水利、环境和公共设施管理业', 'N', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',15, '居民服务、修理和其他服务业', 'O', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',16, '教育', 'P', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',17, '卫生和社会工作', 'Q', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',18, '文化、体育和娱乐业', 'R', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',19, '综合', 'S', '',1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`,`order_number`, `name`, `code`, `description`, `state`) VALUES ('customer_business',20, '其它', 'T', '',1);

##经营性质
INSERT INTO `uap`.`data_dictionary`(`code`, `name`, `level`, `system_code`, `description`) VALUES ('business_nature', '经营性质', NULL, 'CUSTOMER', NULL);
##杭州水产
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 11, 'hzsc', 1, '批发', 'wholesale', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 11, 'hzsc', 2, '农贸', 'agricultural_trade', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 11, 'hzsc', 3, '团体', 'team', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 11, 'hzsc', 4, '个人', 'Individual', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 11, 'hzsc', 5, '餐饮', 'catering', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 11, 'hzsc', 6, '配送商', 'distributors', 1);
##杭州蔬菜
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 14, 'hzve', 1, '批发', 'wholesale', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 14, 'hzve', 2, '农贸', 'agricultural_trade', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 14, 'hzve', 3, '团体', 'team', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 14, 'hzve', 4, '个人', 'Individual', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 14, 'hzve', 5, '餐饮', 'catering', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 14, 'hzve', 6, '配送商', 'distributors', 1);
##杭州果品
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 15, 'hzgp', 1, '批发', 'wholesale', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 15, 'hzgp', 2, '农贸', 'agricultural_trade', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 15, 'hzgp', 3, '团体', 'team', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 15, 'hzgp', 4, '个人', 'Individual', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 15, 'hzgp', 5, '餐饮', 'catering', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 15, 'hzgp', 6, '配送商', 'distributors', 1);

/*==============================================================*/
/* Table: user_account         客户账号                                 */
/*==============================================================*/
drop table if exists user_account;
create table user_account
(
    id                   bigint not null auto_increment comment '主键ID',
    customer_id          bigint comment '所属客户',
    certificate_number   varchar(40) comment '客户证件号',
    customer_code        varchar(20) comment '客户编码',
    cellphone            varchar(11) comment '手机号码',
    account_name         varchar(50) comment '账号名称',
    account_code         varchar(40) comment '用户账号',
    password             varchar(100) comment '用户密码',
    cellphone_valid      tinyint comment '手机号是否已验证',
    is_enable            tinyint comment '是否启用',
    wechat_terminal_code varchar(30) comment '微信终端号',
    avatar_url           varchar(255) comment '头像地址',
    notes                varchar(255) comment '备注',
    changed_pwd_time     datetime comment '更新密码的时间',
    new_account_id      bigint comment '账号合并后关联的新ID',
    deleted              tinyint comment '是否已删除',
    operator_id          bigint comment '创建人',
    create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
    modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);
alter table user_account comment '用户账号信息';
/*==============================================================*/
/* Index: idx_user_account_code                                 */
/*==============================================================*/
create unique index idx_user_account_code on user_account
(
   account_code
);
## 更改附件类型为营业执照
update attachment set file_type=1;
