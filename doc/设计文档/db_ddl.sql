
/*  设备类型表
*/
create table t_alarm_device_category 
(
   id                   varchar(32)                    not null,
   category_code        varchar(40)                    null,
   category_name        varchar(100)                   null,
   device_brand         varchar(100)                   null,
   model_num            varchar(100)                   null,
   vender               varchar(100)                   null,
   device_place         varchar(100)                   null,
   specifications       varchar(100)                   null,
   weight               varchar(100)                   null,
   device_image         varchar(256)                   null,
   ccc_authentication   varchar(256)                   null,
   examining_report     varchar(256)                   null,
   alarm_threshold      varchar(40)                    null,
   have_ip              char(1)                        null,
   remark               varchar(512)                   null,
   constraint PK_T_ALARM_DEVICE_CATEGORY primary key (id)
);



/*  设备表
*/
create table t_alarm_device 
(
   id                   varchar(32)                    not null,
   category_id          varchar(32)                    null,
   position             varchar(100)                   null,
   device_code          varchar(40)                    null,
   ip_address           varchar(64)                    null,
   longitude            decimal(8,2)                   null,
   latitude             decimal(8,2)                   null,
   moxa_serial_num      varchar(40)                    null,
   rfid_data            varchar(40)                    null,
   alarm_threshold      varchar(40)                    null,
   up_connect_device    varchar(32)                    null,
   mount_point          varchar(128)                   null,
   mount_point_id       varchar(32)                    null,
   mount_personnel      varchar(40)                    null,
   construction_unit    varchar(128)                   null,
   project_manager      varchar(40)                    null,
   construction_date    varchar(19)                    null,
   crate_num            varchar(40)                    null,
   construction_distance varchar(40)                    null,
   constructor          varchar(128)                   null,
   constraint PK_T_ALARM_DEVICE primary key (id)
);


/*  设备采集数据
*/
create table t_alarm_detail 
(
   id                   varchar(32)                    not null,
   device_id            varchar(32)                    null,
   analysis_data        varchar(40)                    null,
   is_alarm             char(1)                        null,
   collect_date         varchar(19)                    null,
   update_user          varchar(32)                    null,
   update_date          varchar(19)                    null,
   constraint PK_T_ALARM_DETAIL primary key (id)
);


/*  菜单信息表
*/
create table t_sys_menu 
(
   id                   varchar(32)                    not null,
   menu_code            varchar(32)                    null,
   menu_name            varchar(64)                    null,
   menu_order           int(4)                         null default '0',
   is_leaf              char(1)                        null,
   is_enable            char(1)                        null default '1',
   menu_type            char(1)                        null,
   menu_info            varchar(1000)                  null,
   parent_id            varchar(32)                    null,
   menu_client_type     char(1)                        null default '0',
   menu_url             varchar(256)                   null,
   menu_image           varchar(256)                   null,
   remark               varchar(256)                   null,
   constraint PK_T_SYS_MENU primary key (id)
);


/*  菜单功能信息表
*/
create table t_sys_func 
(
   id                   varchar(32)                    not null,
   func_code            varchar(40)                    null,
   func_name            varchar(64)                    null,
   remark               varchar(256)                   null,
   constraint PK_T_SYS_FUNC primary key clustered (id)
);


/*  菜单与功能关联信息表
*/
create table t_sys_menu_func 
(
   id                   varchar(32)                    not null,
   menu_id              varchar(32)                    null,
   func_id              varchar(32)                    null,
   constraint PK_T_SYS_MENU_FUNC primary key clustered (id)
);


/*  组织机构信息表
*/
create table t_sys_org 
(
   id                   varchar(32)                    not null,
   tenant_id            varchar(32)                    not null,
   org_code             varchar(50)                    not null,
   org_name             varchar(100)                   not null,
   is_enable            char(1)                        not null,
   created_user         varchar(32)                    not null,
   created_date         varchar(19)                    not null,
   updated_user         varchar(32)                    not null,
   updated_date         varchar(19)                    not null,
   parent_id            varchar(32)                    null,
   is_leaf              char(1)                    not null,
   constraint PK_T_SYS_ORG primary key (id)
);


/*  用户信息表
*/
create table t_sys_user 
(
   id                   varchar(32)                    not null,
   login_name           varchar(64)                    null,
   password             varchar(128)                   null,
   tenant_id            varchar(32)                    null,
   user_code            varchar(40)                    null,
   user_name            varchar(64)                    null,
   sex                  char(1)                        null,
   is_enable            tinyint(1)                     null default '1',
   is_lock              tinyint(1)                     null,
   created_user         varchar(32)                    null,
   created_date         varchar(19)                    null,
   updated_user         varchar(32)                    null,
   updated_date         varchar(32)                    null,
   org_id               varchar(32)                    null,
   email                varchar(100)                   null,
   mobile               char(11)                       null,
   weixin               varchar(100)                   null,
   lock_date            varchar(19)                    null,
   lock_by_user         varchar(32)                    null,
   constraint PK_T_SYS_USER primary key (id)
);



/*  组织机构与用户关联信息表
*/
create table t_sys_org_user 
(
   id                   varchar(32)               not null,
   org_id               varchar(32)                    null,
   user_id              varchar(32)                    null,
   constraint PK_T_SYS_ORG_USER primary key clustered (id)
);


/*  租户信息表
*/
create table t_sys_tenant 
(
   id                   varchar(32)                    not null,
   tenant_name          varchar(128)                   null,
   contacts             varchar(64)                    null,
   email                varchar(64)                    null,
   telephone            varchar(16)                    null,
   created_date         varchar(19)                    null,
   created_user         varchar(32)                    null,
   tenant_admin         varchar(32)                    null,
   enabled              char(1)                        null,
   tenant_visit_url     varchar(256)                   null,
   validity_period      varchar(19)                    null,
   constraint PK_T_SYS_TENANT primary key clustered (id)
);


/*  角色信息表
*/
create table t_sys_role 
(
   id                   varchar(32)                    not null,
   role_code            varchar(40)                    not null,
   role_name            varchar(100)                   not null,
   remark               varchar(512)                   null,
   is_enable            char(1)                        not null,
   created_user         varchar(32)                    not null,
   created_date         varchar(19)                    not null,
   updated_user         varchar(32)                    not null,
   updated_date         varchar(19)                    not null,
   owner_id             varchar(32)                    null,
   role_type            char(1)                        null,
   constraint PK_T_SYS_ROLE primary key (id)
);


/*  用户与角色信息表
*/
create table t_sys_user_role 
(
   id                   varchar(32)                    not null,
   user_id              varchar(32)                    not null,
   role_id              varchar(32)                    not null,
   constraint PK_T_SYS_USER_ROLE primary key (id)
);



/*  角色关联的资源信息表
*/
create table t_sys_role_res 
(
   id                   varchar(32)                    not null,
   role_id              varchar(32)                    not null,
   res_type             char(1)                        not null,
   res_id               varchar(32)                    not null,
   from_obj_ids         varchar(1024)                  not null default '0',
   constraint PK_T_SYS_ROLE_RES primary key (id)
);


/*  用户登录日志信息表
*/
create table t_log_login 
(
   id                   varchar(32)                    not null,
   client_ip            varchar(64)                    null,
   login_date           varchar(19)                    null,
   login_status         char(1)                        null,
   params               varchar(1024)                  null,
   tenant_id            varchar(32)                    null,
   login_user           varchar(40)                    null,
   login_type           varchar(40)                    null,
   lock_trigger         char(1)                        null,
   error_info           varchar(500)                   null,
   is_form_login        tinyint(1)                     null,
   server_ip            varchar(64)                    null,
   constraint PK_sy_loginlog primary key (id)
);


/*  用户操作日志信息表
*/
create table t_log_operation 
(
   id                   varchar(32)                    not null,
   session_id           varchar(32)                    null,
   login_user           varchar(32)                    null,
   user_id              varchar(40)                    null,
   operation_module     varchar(40)                    null,
   operation_time       varchar(19)                    null,
   operation_type       varchar(20)                    null,
   operation_desc       varchar(256)                   null,
   constraint PK_T_LOG_OPERATION primary key clustered (id)
);


/*  编码信息表
*/
CREATE TABLE t_code_info (
	id varchar(32) primary key,
	code_no varchar(40),
	code_name varchar(64),
	remark varchar(512)
);

