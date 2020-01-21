/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2020/1/13 11:36:05                           */
/*==============================================================*/


drop table if exists customer;

drop table if exists address;

drop table if exists contacts;

drop table if exists customer_firm;

drop table if exists vehicle;

/*==============================================================*/
/* Table: customer                                              */
/*==============================================================*/
create table customer
(
   id                   bigint not null auto_increment,
   code                 varchar(20) comment '客户编号',
   certificate_number   varchar(40) comment '证件号',
   certificate_type     varchar(20) comment '证件类型',
   certificate_range    varchar(40) comment '证件日期##企业时为营业执照日期,如:2011-09-01 至 长期',
   certificate_addr     varchar(100) comment '证件地址',
   name                 varchar(40) comment '客户名称',
   birthdate            date comment '出生日期',
   gender               tinyint(1) comment '性别:男,女',
   photo                varchar(6000) comment '照片',
   cellphone            varchar(40) comment '手机号',
   organization_type    varchar(20) comment '组织类型,个人/企业',
   source_system        varchar(20) comment '来源系统##外部系统来源标识',
   profession           varchar(20) comment '客户行业##水果批发/蔬菜批发/超市',
   operating_area       varchar(40) comment '经营地区##经营地区城市id',
   operating_lng        varchar(20) comment '经营地区经度',
   operating_lat        varchar(20) comment '经营地区纬度',
   other_title          varchar(40) comment '其它头衔',
   main_category        varchar(40) comment '主营品类',
   registered_capital   bigint comment '注册资金##企业客户属性',
   employee_number      varchar(20) comment '企业员工数',
   corporation_certificate_type varchar(20) comment '法人证件类型',
   corporation_certificate_number varchar(40) comment '法人证件号',
   corporation_name     varchar(40) comment '法人真实姓名',
   is_cellphone_valid   tinyint(1) comment '手机号是否验证',
   creator_id           bigint comment '创建人',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   is_delete            tinyint(1) default 1 comment '是否可用',
   state                int default 1 comment '客户状态 0注销，1生效，2禁用，',
   primary key (id)
);

alter table customer comment '客户基础数据
企业客户没有性别和民族和certificate_time，但有certificate_rang';

/*==============================================================*/
/* Table: customer_address                                      */
/*==============================================================*/
create table address
(
   id                   bigint not null auto_increment comment 'ID',
   name                 varchar(40) comment '名称',
   address              varchar(250) comment '地址',
   city_id              varchar(20) comment '所在城市',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   creator_id           bigint comment '创建人',
   modifier_id          bigint comment '修改人',
   lat                  varchar(20) comment '纬度',
   lng                  varchar(20) comment '经度',
   is_default           tinyint(1) comment '是否默认地址',
   primary key (id)
);

/*==============================================================*/
/* Table: contacts                                     */
/*==============================================================*/
create table contacts
(
   id                   bigint not null auto_increment comment 'ID',
   customer_id          bigint comment '所属客户',
   firm_id              bigint comment '归属市场',
   name                 varchar(20) comment '姓名',
   gender               tinyint(1) comment '性别男，女',
   phone                varchar(20) comment '电话',
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
/* Table: customer_firm                                    */
/*==============================================================*/
create table customer_firm
(
   id                   bigint not null auto_increment comment 'ID',
   firm_id              varchar(20) comment '归属组织',
   department_id        bigint comment '归属部门##内部创建归属到创建员工的部门',
   customer_id          bigint comment '客户id',
   owner_id             bigint comment '所有者',
   alias                varchar(40) comment '客户别名',
   type                 varchar(20) comment '客户类型##采购、销售、代买等##{provider:"dataDictionaryValueProvider",queryParams:{dd_id:4}}',
   notes                varchar(250) comment '备注信息',
   modifier_id          bigint comment '修改人id',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (id)
);

alter table customer_firm comment '客户归属市场关系表';

/*==============================================================*/
/* Table: customer_vehicle                                      */
/*==============================================================*/
create table vehicle
(
   id                   bigint not null auto_increment comment 'ID',
   name                 varchar(40) comment '名称',
   registration_number  varchar(20) comment '车牌号',
   type                 varchar(20) comment '车型',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   creator_id           bigint comment '创建人',
   modifier_id          bigint comment '修改人',
   primary key (id)
);