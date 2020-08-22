/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2020/1/13 11:36:05                           */
/*==============================================================*/

/*==============================================================*/
/* Table: customer                                              */
/*==============================================================*/
drop index idx_certificate_number on customer;
drop table if exists customer;
create table customer
(
   id                   bigint not null auto_increment,
   code                 varchar(20) comment '客户编号',
   certificate_number   varchar(40) comment '证件号',
   certificate_type     varchar(20) comment '证件类型',
   certificate_range    varchar(40) comment '证件日期##企业时为营业执照日期,如:2011-09-01 至 长期',
   certificate_long_term tinyint comment '证件是否长期有效',
   certificate_addr     varchar(100) comment '证件地址',
   name                 varchar(40) comment '客户名称',
   birthdate            date comment '出生日期',
   gender               tinyint(1) comment '性别:男,女',
   photo                varchar(200) comment '照片',
   grade                tinyint comment '客户等级',
   cellphone            varchar(40) comment '手机号',
   contacts_phone       varchar(20) comment '联系电话',
   contacts_name        varchar(20) comment '联系人',
   organization_type    varchar(20) comment '组织类型,个人/企业',
   source_system        varchar(20) comment '来源系统##外部系统来源标识',
   source_channel       varchar(20) comment '来源渠道',
   registered_capital   bigint comment '注册资金##企业客户属性',
   employee_number      varchar(20) comment '企业员工数',
   corporation_certificate_type varchar(20) comment '法人证件类型',
   corporation_certificate_number varchar(40) comment '法人证件号',
   corporation_name     varchar(40) comment '法人真实姓名',
   is_cellphone_valid   tinyint(1) comment '手机号是否验证',
   current_city_path    varchar(40) comment '现住址城市ID路径',
   current_city_name    varchar(40) comment '现住址城市合成名称',
   current_address      varchar(255) comment '现住址详细地址',
   creator_id           bigint comment '创建人',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   is_delete            tinyint(1) default 1 comment '是否可用',
   state                int default 1 comment '客户状态 0注销，1生效，2禁用，',
   primary key (id)
);
alter table customer comment '客户基础数据';
/*==============================================================*/
/* Index: idx_certificate_number                                */
/*==============================================================*/
create index idx_certificate_number on customer
(
   certificate_number
);

/*==============================================================*/
/* Table: contacts                                     */
/*==============================================================*/
drop index idx_c_market_id on contacts;
drop index idx_c_customer_id on contacts;
drop table if exists contacts;
create table contacts
(
   id                   bigint not null comment 'ID',
   customer_id          bigint comment '所属客户',
   market_id            bigint comment '归属市场',
   name                 varchar(40) comment '姓名',
   gender               tinyint(1) comment '性别男，女',
   phone                varchar(20) comment '电话',
   nation               int comment '民族',
   address              varchar(250) comment '地址',
   position             varchar(100) comment '职务/关系',
   birthdate            date comment '出生日期',
   notes                varchar(250) comment '备注',
   is_default           tinyint comment '是否默认联系人',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   creator_id           bigint comment '创建人',
   modifier_id          bigint comment '修改人',
   primary key (id)
);
alter table contacts comment '联系人(可多条)';
/*==============================================================*/
/* Index: idx_c_customer_id                                     */
/*==============================================================*/
create index idx_c_customer_id on contacts
(
   customer_id
);
/*==============================================================*/
/* Index: idx_c_market_id                                       */
/*==============================================================*/
create index idx_c_market_id on contacts
(
   market_id
);

/*==============================================================*/
/* Table: customer_market                                       */
/*==============================================================*/
drop index idx_market_id on customer_market;
drop index idx_customer_id on customer_market;
drop table if exists customer_market;
create table customer_market
(
   id                   bigint not null auto_increment comment 'ID',
   market_id            bigint comment '归属组织',
   department_id        bigint comment '归属部门##内部创建归属到创建员工的部门',
   customer_id          bigint comment '客户id',
   owner_id             bigint comment '所有者',
   grade                tinyint comment '客户级别',
   profession           varchar(32) comment '客户行业',
   operating_area       varchar(40) comment '经营地区',
   operating_lng        varchar(20) comment '经营地区精确',
   operating_lat        varchar(20) comment '经营地区纬度',
   other_title          varchar(40) comment '其它头衔',
   category_id          varchar(200) comment '经营品类ID,多个逗号分隔',
   category_name        varchar(512) comment '经营品类名称,多个以逗号隔开',
   sales_market         varchar(40) comment '销地市场',
   alias                varchar(40) comment '客户别名',
   type                 varchar(20) comment '客户类型##采购、销售、代买等##{provider:"dataDictionaryValueProvider",queryParams:{dd_id:4}}',
   notes                varchar(250) comment '备注信息',
   creator_id           bigint comment '客户所在当前市场时的创建人',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   modifier_id          bigint comment '修改人id',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (id)
);
alter table customer_market comment '客户归属市场关系表';
/*==============================================================*/
/* Index: idx_customer_id                                       */
/*==============================================================*/
create index idx_customer_id on customer_market
(
   customer_id
);
/*==============================================================*/
/* Index: idx_market_id                                         */
/*==============================================================*/
create index idx_market_id on customer_market
(
   market_id
);

/*==============================================================*/
/* Table: address                                               */
/*==============================================================*/
drop index idx_a_customer_id on address;
drop index idx_a_market_id on address;
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
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (id)
);
alter table attachment comment '客户上传的附件信息';

/*==============================================================*/
/* Table: tallying_area                                         */
/*==============================================================*/
drop index idx_ta_customer_id on tallying_area;
drop table if exists tallying_area;
create table tallying_area
(
   id                   bigint not null comment '主键ID',
   customer_id          bigint comment '客户ID',
   market_id            bigint comment '客户理货区市场',
   department_id        bigint comment '理货区所属部门',
   assets_id            bigint comment '理货区ID',
   assets_name          varchar(32) comment '理货区(摊位)名称',
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
