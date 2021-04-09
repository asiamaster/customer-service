
/*==============================================================*/
/* Table: employee          员工基本信息                          */
/*==============================================================*/
drop table if exists `dili-customer`.employee;
create table `dili-customer`.employee
(
    id                   bigint not null auto_increment comment 'ID',
    name                 varchar(40) comment '员工姓名',
    cellphone            varchar(11) comment '手机号',
    password             varchar(100) comment '登录密码',
    changed_pwd_time     datetime comment '密码修改时间',
    create_time          datetime comment '创建时间',
    modify_time          datetime comment '修改时间',
    primary key (id)
);
alter table `dili-customer`.employee comment '员工基础信息表';

/*==============================================================*/
/* Index: uni_employee_cellphone                                */
/*==============================================================*/
create unique index uni_employee_cellphone on `dili-customer`.employee
(
   cellphone
);

/*==============================================================*/
/* Table: customer_employee           客户员工信息                */
/*==============================================================*/
drop table if exists `dili-customer`.customer_employee;
create table `dili-customer`.customer_employee
(
    id                   bigint not null auto_increment comment 'ID',
    customer_id          bigint comment '客户ID',
    employee_id          bigint comment '员工ID',
    deleted              tinyint comment '是否删除(即：是否解除关系)',
    create_time          datetime comment '创建时间',
    modify_time          datetime comment '修改时间',
    primary key (id)
);
alter table `dili-customer`.customer_employee comment '客户员工关联关系';

/*==============================================================*/
/* Table: employee_card         员工持卡信息                      */
/*==============================================================*/
drop table if exists `dili-customer`.employee_card;
create table `dili-customer`.employee_card
(
    id                   bigint not null auto_increment comment 'ID',
    customer_employee_id bigint comment '客户员工ID',
    card_account_id      bigint comment '卡账户ID',
    card_no              varchar(20) comment '园区卡号',
    market_id            bigint comment '所属市场',
    deleted              tinyint comment '是否删除',
    create_time          datetime comment '创建时间',
    modify_time          datetime comment '修改时间',
    primary key (id)
);
alter table `dili-customer`.employee_card comment '员工持卡信息';
/*==============================================================*/
/* Index: idx_customer_employee_id                              */
/*==============================================================*/
create index idx_customer_employee_id on `dili-customer`.employee_card
(
   customer_employee_id
);
/*==============================================================*/
/* Index: idx_card_no                                           */
/*==============================================================*/
create index idx_card_no on `dili-customer`.employee_card
(
   card_no
);