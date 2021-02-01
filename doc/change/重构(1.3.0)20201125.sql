##数据字典 增加客户角色身份
INSERT INTO `uap`.`data_dictionary`(`code`, `name`, `level`, `system_code`, `description`) VALUES ('other_character_type', '客户其它角色身份', NULL, 'CUSTOMER', '客户的其它角色身份');
INSERT INTO `uap`.`data_dictionary`(`code`, `name`, `level`, `system_code`, `description`) VALUES ('buyer_character_type', '客户买家角色身份', NULL, 'CUSTOMER', '客户买家角色身份');
INSERT INTO `uap`.`data_dictionary`(`code`, `name`, `level`, `system_code`, `description`) VALUES ('business_user_character_type', '客户经营户角色身份', NULL, 'CUSTOMER', '经营户角色身份类型');

INSERT INTO `uap`.`data_dictionary_value` (`dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`, `firm_id`, `firm_code`, `state`) VALUES ('other_character_type', 4, '司机', 'driver', NULL, '2020-09-23 13:52:47', '2020-09-23 13:52:47', 8, 'sg', 1);
INSERT INTO `uap`.`data_dictionary_value` (`dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`, `firm_id`, `firm_code`, `state`) VALUES ('buyer_character_type', 1, '买家', 'inside_buyer', NULL, '2020-09-22 14:05:57', '2020-09-22 14:05:57', 11, 'hzsc', 1);
INSERT INTO `uap`.`data_dictionary_value` (`dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`, `firm_id`, `firm_code`, `state`) VALUES ('business_user_character_type', 2, '卖家', 'inside_seller', NULL, '2020-09-22 14:05:36', '2020-09-22 14:05:36', 11, 'hzsc', 1);
INSERT INTO `uap`.`data_dictionary_value` (`dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`, `firm_id`, `firm_code`, `state`) VALUES ('business_user_character_type', 2, '卖家', 'inside_seller', NULL, '2020-09-22 15:40:57', '2020-09-22 15:40:57', 17, 'gx', 1);
INSERT INTO `uap`.`data_dictionary_value` (`dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`, `firm_id`, `firm_code`, `state`) VALUES ('business_user_character_type', 7, '卖家', 'inside_seller', NULL, '2020-11-05 19:10:08', '2020-11-05 19:10:08', 10, 'dlk', 1);
INSERT INTO `uap`.`data_dictionary_value` (`dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`, `firm_id`, `firm_code`, `state`) VALUES ('buyer_character_type', 5, '省内客户', 'in_province', NULL, '2020-09-24 13:05:38', '2020-09-24 13:05:38', 8, 'sg', 1);
INSERT INTO `uap`.`data_dictionary_value` (`dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`, `firm_id`, `firm_code`, `state`) VALUES ('buyer_character_type', 6, '本地客户', 'native', NULL, '2020-09-24 13:06:06', '2020-09-24 13:06:06', 8, 'sg', 1);
INSERT INTO `uap`.`data_dictionary_value` (`dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`, `firm_id`, `firm_code`, `state`) VALUES ('buyer_character_type', 2, '理货区客户', 'operation_area', NULL, '2020-09-23 13:52:02', '2020-09-23 13:52:02', 8, 'sg', 1);
INSERT INTO `uap`.`data_dictionary_value` (`dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`, `firm_id`, `firm_code`, `state`) VALUES ('buyer_character_type', 3, '园外买方', 'outside_buyer', NULL, '2020-09-23 13:54:25', '2020-09-23 13:54:25', 8, 'sg', 1);
INSERT INTO `uap`.`data_dictionary_value` (`dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`, `firm_id`, `firm_code`, `state`) VALUES ('business_user_character_type', 1, '卖方客户', 'seller', NULL, '2020-09-23 13:51:47', '2020-09-23 13:51:47', 8, 'sg', 1);

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
ALTER TABLE customer_market ADD COLUMN `approval_notes` VARCHAR(255) DEFAULT NULL COMMENT '客户资料审核备注' AFTER approval_time;
ALTER TABLE customer_market ADD COLUMN `business_region_tag` tinyint DEFAULT NULL COMMENT '客户区域标签' AFTER business_nature;

ALTER TABLE customer_market drop COLUMN `operating_area`;
ALTER TABLE customer_market drop COLUMN `operating_lng`;
ALTER TABLE customer_market drop COLUMN `operating_lat`;
ALTER TABLE customer_market drop COLUMN `other_title`;

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
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 1, '批发', 'wholesale', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 2, '农贸', 'agricultural_trade', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 3, '团体', 'team', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 4, '个人', 'individual', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 5, '餐饮', 'catering', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `order_number`, `name`, `code`,`state`) VALUES ('business_nature', 6, '配送商', 'distributors', 1);
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `order_number`, `name`, `code`,`state`) VALUES ('source_channel', 9, '地利工作台', 'workbench', 1);


/*==============================================================*/
/* Table: user_account         客户账号                           */
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
## 迁移原始账号信息到新系统中
INSERT INTO `dili-customer`.`user_account` (`customer_id`, `certificate_number`, `customer_code`, `cellphone`, `account_name`, `account_code`, `password`, `cellphone_valid`, `is_enable`, `notes`, `changed_pwd_time`, `deleted`, `new_account_id`, `operator_id`, `create_time`, `modify_time`)
SELECT customer_id,certificate_number,customer_code,account_code,account_name,account_code,`password`,1,is_enable,notes,modify_time,0,null,operator_id,create_time, modify_time FROM dili_user.user_account;

## 更改附件类型为营业执照
update attachment set file_type=1;

INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('客户编号', 'customerCode', NULL, NULL, 8, '1', '2020-12-21 16:28:15', '2020-12-21 16:28:15', 1, 1);
INSERT INTO `uap`.`biz_number` (`type`, `value`, `memo`, `version`, `modified`, `created`) VALUES ('customerCode', 1, '客户编号', '176928', '2020-12-21 16:29:53', '2020-12-21 16:28:15');

/*==============================================================*/
/* Table: account_terminal    用户账号绑定的终端类型                                  */
/*==============================================================*/
drop table if exists account_terminal;
create table account_terminal
(
    id                   bigint not null auto_increment comment '主键ID',
    account_id           bigint comment '所属账号',
    app_id               varchar(64) comment '第三方ID',
    terminal_type        tinyint comment '终端类型(微信?支付宝?)',
    terminal_code        varchar(30) comment '终端号(openId)',
    avatar_url           varchar(255) comment '头像地址',
    nick_name            varchar(100) comment '终端账号对应昵称',
    create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
    modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);
alter table account_terminal comment '用户账号绑定的终端信息';

/*==============================================================*/
/* Table: applet_info                                           */
/*==============================================================*/
drop table if exists applet_info;
create table applet_info
(
    id                   bigint not null auto_increment comment '主键ID',
    applet_name          varchar(32) comment '小程序名称',
    system_code          varchar(20) comment '所属系统',
    applet_code          varchar(50) comment '内部编码',
    app_id               varchar(32) comment '小程序应用ID',
    secret               varchar(64) comment '小程序访问密钥',
    applet_type          tinyint comment '小程序类型(微信?支付宝?)',
    create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
    modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);
alter table applet_info comment '小程序资料信息';
drop index uni_app_id_app_type on applet_info;

/*==============================================================*/
/* Index: uni_app_id_app_type                                   */
/*==============================================================*/
create unique index uni_app_id_app_type on applet_info
(
   app_id,
   applet_type
);

INSERT INTO `dili-customer`.`applet_info` (`applet_name`, `system_code`, `applet_code`, `app_id`, `secret`, `applet_type`, `create_time`, `modify_time`) VALUES ('溯源司机端', 'TRACE', 'DRIVER', 'wxdd7397bf57ef8ade', '412a53c4782395dc5273c728e659e240', 1, now(), now());
INSERT INTO `dili-customer`.`applet_info` (`applet_name`, `system_code`, `applet_code`, `app_id`, `secret`, `applet_type`, `create_time`, `modify_time`) VALUES ('溯源买卖家', 'TRACE', 'BuyerAndSeller', 'wxe08c9b2b40546ebd', 'e0ff4f7ee31a5bb1d88ea213ab30bf5e', 1, now(), now());