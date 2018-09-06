
/*  
* 报警信息
*/
create table t_alarm_info 
(
   id                   varchar(32)                    not null,
   device_type		char(1)			       null comment '报警设备:D探测器|P处理器',
   nfc_number		varchar(14)                    null,
   device_info          varchar(128)                   null comment '设备标识信息' ,
   longitude            decimal(10,7)                  null comment '经度' ,
   latitude             decimal(10,7)                  null comment '纬度' ,
   pos_desc             varchar(256)                   null comment '位置描述',
   alarm_type		char(1)                        null comment '警报类型:0触动|2掉线' ,
   alarm_date           varchar(19)                    null comment '报警时间' ,
   status               char(1)                        null comment '处理状态' ,
   is_valid             char(1)                        null comment '是否有效' ,
   danger_level         tinyint                        null comment '危险级别' ,
   alarm_desc           varchar(256)                   null comment '警情描述' ,
   confirm_date         varchar(19)                    null comment '确认时间' ,
   confirm_person	varchar(64)                    null comment '确认人' ,
   remark       	varchar(256)                   null comment '备注' ,
   constraint PK_T_ALARM_INFO_ID primary key (id)
);


/*  
*  报警处理信息
*/
create table t_alarm_process 
(
   id                   varchar(32)                    not null,
   alarm_id             varchar(32)                    not null comment '警报ID' ,
   process_date         varchar(19)                    null comment '处理时间' ,
   process_person	varchar(64)                    null comment '处理人' ,
   process_method       varchar(128)                   null comment '处置方法' ,
   process_result       text                           null comment '处置结果' ,
   person_feature       varchar(256)                   null comment '人员特征' ,
   alarm_image          varchar(128)                   null comment '报警图片' ,
   alarm_reason         char(1)                        null comment '报警原因代码' ,
   out_police           varchar(64)                    null comment '出警人',
   constraint PK_T_ALARM_PROCESS_ID primary key (id)
);


/*  
*  机箱信息
*/
create table t_machine_box 
(
   id                   varchar(32)                    not null,
   box_number           varchar(12)                    null comment '机箱编号' ,
   nfc_number           varchar(20)                    null comment 'NFC编号' ,
   longitude            decimal(10,7)                  null comment '经度' ,
   latitude             decimal(10,7)                  null comment '纬度' ,
   pos_desc             varchar(128)                   null comment '位置描述' , 
   create_date          varchar(19)                    null ,
   constraint pk_t_machine_box_id primary key (id)
);

/*  
*  设备所属属地
*/
create table t_device_apanage
(
   id                   varchar(32)                    not null,
   device_type          char(1)                        null comment '设备类型:M机箱|D探测器' ,
   device_id            varchar(32)                    null comment '设备ID' ,
   org_id               varchar(32)                    null comment '所属组织' ,
   constraint pk_t_device_apanage_id primary key (id)
);


/*  
*  处理器信息
*/
create table t_processor 
(
   id                   varchar(32)                    not null,
   machine_box_id       varchar(32)                    null comment '对应机箱id' ,
   nfc_number           varchar(20)                    null comment 'NFC序列号' ,
   moxa_number          varchar(20)                    null comment 'moxa序列号' ,
   ip                   varchar(32)                    null comment 'ip地址' ,
   detector_num         int(4)                         null comment '下属探测器数量' ,
   create_date          varchar(19)                    null ,
   constraint pk_t_processor_id primary key (id)
);

/*  
*  探测器信息
*/
create table t_detector 
(
   id                   varchar(32)                    not null,
   detector_seq	        varchar(10)                    null comment '探测器编号' ,
   processor_id         varchar(32)                    null comment '对应处理器id' ,
   nfc_number           varchar(20)                    null comment 'NFC序列号' ,
   longitude            decimal(10,7)                  null comment '经度' ,
   latitude             decimal(10,7)                  null comment '纬度' ,
   pos_desc             varchar(128)                   null comment '位置描述' ,
   constraint pk_t_detector_id primary key clustered (id)
);

/*  
*  摄像头类别
*/
create table t_camera_class
(
   id                   varchar(32)                    not null,
   vendor	        char(128)                      null comment '供应商' ,
   vendor_code	        varchar(4)                     null comment '供应商编码' ,
   model                varchar(40)                    null comment '摄像机型号' ,
   constraint pk_t_camera_class_id primary key clustered (id)
);


/*  
*  摄像头信息
*/
create table t_camera
(
   id                   varchar(32)                    not null,
   class_id	        varchar(32)                    null comment '类别ID' ,
   pos_desc             varchar(256)                   null comment '位置描述' ,
   longitude            decimal(10,7)                  null comment '经度' ,
   latitude             decimal(10,7)                  null comment '纬度' ,
   ip                   varchar(16)                    null comment '摄像头ip',
   channel_id	        varchar(10)		       null comment '对应录像机通道',
   web_port             int                            null comment '摄像头web端口',
   device_port          int                            null comment '摄像头设备端口',
   recorder_ip		varchar(16)                    null comment '录像机ip',
   recorder_port        int                            null comment '录像机web端口',
   camera_user_name     varchar(20)                    null comment '摄像头登录名',
   camera_password      varchar(20)                    null comment '摄像头登录密码',
   recorder_user_name   varchar(20)                    null comment '录像机登录名',
   recorder_password    varchar(20)                    null comment '录像机登录密码',
   constraint pk_t_camera_id primary key clustered (id)
);

/*  
*  摄像头预置位信息
*/
create table t_camera_preset
(
   id                   varchar(32)                    not null,
   camera_id            varchar(32)                    null comment '摄像机id' ,
   preset_num         int                            null comment '预置位编号' ,
   preset_desc        varchar(256)                   null comment '预置位描述' ,
   constraint pk_t_preset_position_id primary key clustered (id)
);

/*  
*  设备与摄像头关联信息
*/
create table t_camera_deploy
(
   id                   varchar(32)                    not null,
   device_type          char(1)                        null comment 'P:处理器,D:探测器',
   device_id	        varchar(32)                    null comment '目标设备id' ,
   camera_id            varchar(32)                    null comment '摄像机id' ,
   preset_id            varchar(32)                    null comment '预置位id' ,
   constraint pk_t_detector_camera_id primary key clustered (id)
);

/*  
*  系统菜单信息
*/
create table t_menu 
(
   id                   varchar(32)                    not null,
   menu_name            varchar(64)                    null comment '菜单名称' ,
   menu_code            varchar(20)                    null comment '菜单编码' ,
   menu_url             varchar(128)                   null comment '菜单资源地址' ,
   icon_url             varchar(128)                   null comment '菜单图标地址' ,
   parent_id            varchar(32)                    null comment '上级菜单id' ,
   order_num            int(4)                         null comment '菜单序号' ,
   remark               varchar(128)                   null comment '备注' ,
   creator              varchar(32)                    null ,
   create_date          varchar(19)                    null,
   constraint pk_t_menu_id primary key (id)
);

/*  
*  组织信息
*/
create table t_org 
(
   id                   varchar(32)                    not null,
   parent_id		varchar(32)                    not null,
   org_name             varchar(256)                   not null comment '单位名称',
   org_code             varchar(20)                    not null comment '单位编码',
   address              varchar(256)                   null comment '单位地址',
   telephone            varchar(11)                    null comment '单位电话',
   contacts             varchar(32)                    null comment '联系人',
   contact_position     varchar(64)                    null comment '联系人职务',
   email                varchar(64)                    null comment '联系邮件',
   status               char(1)                        null comment '状态D：删除,,R:恢复,N:新建',
   order_num		int(4)			       null comment '排序字段' ,
   creator              varchar(64)                    null ,
   create_date          varchar(19)                    null ,
   constraint pk_t_org_id primary key (id)
);


/*  
*  用户信息
*/
create table t_user 
(
   id                   varchar(32)                    not null,
   user_name            varchar(64)                    not null		comment '用户名',
   full_name            varchar(64)                    not null		comment '姓名',
   org_id               varchar(32)                    null		comment '所属单位',
   contact_number       varchar(11)                    null		comment '联系电话',
   status               char(1)                        null		comment '状态值:D删除,N新建,R恢复',
   password             varchar(32)                    null		comment '用户密码',
   init_password	char(1)			       null		comment '是否初始化密码',
   sex                  char(1)                        null		comment '性别M:男,F:女',
   birthday             varchar(19)                    null		comment '出生日期',
   creator              varchar(64)                    null ,
   create_date          varchar(19)                    null ,
   constraint pk_t_user_id primary key (id)
);


/*  
*  角色信息
*/
create table t_role 
(
   id                   varchar(32)                    not null,
   role_name            varchar(64)                    null comment '角色名称' ,
   role_type		varchar(1)		       null comment '值S:超级管理员,M：管理员,G：普通角色' ,
   home_page            varchar(128)                   null comment '角色的首页' ,
   role_desc            varchar(128)                   null comment '角色描述' ,
   creator              varchar(32)                    null,
   create_date          Varchar(19)                    null,
   constraint pk_t_role_id primary key (id)
);


/*  
*  用户角色关联信息
*/
create table t_user_role 
(
   id                   varchar(32)                    not null,
   user_id              varchar(32)                    null  comment '用户id' ,
   role_id              varchar(32)                    null  comment '角色id' ,
   constraint pk_t_user_role_id primary key (id)
);

/*  
*  角色资源
*/
create table t_role_res 
(
   id                   varchar(32)                    not null,
   role_id              varchar(32)                    null comment '角色id' ,
   res_type             char(1)                        null comment '资源类型' ,
   res_id               varchar(32)                    null comment '对应资源id' ,
   constraint pk_t_role_res_id primary key (id)
);


create table t_session_log 
(
   id                   varchar(32)                    not null,
   session_id           varchar(64)                    null comment '会话id' ,
   user_name            varchar(64)                    null comment '操作的用户' ,
   login_date           varchar(19)                    null comment '登入时间' ,
   client_ip            varchar(32)                    null comment '客户端ip' ,
   remark               varchar(128)                   null comment '备注' ,
   constraint pk_t_session_log_id primary key (id)
);

alter table t_session_log add index index_session_user_name(user_name);

create table t_admin_log 
(
   id                   varchar(32)                    not null,
   user_name            varchar(64)                    null comment '操作的用户' ,
   operation_date       varchar(19)                    null comment '操作时间' ,
   operation_module     varchar(20)                    null comment '操作模块' ,
   operation_desc       varchar(256)                   null comment '操作描述' ,
   constraint pk_t_admin_log_id primary key (id)
);

set global log_bin_trust_function_creators=1;

CREATE FUNCTION `getOrgChildLst`(rootId varchar(32)) 
     RETURNS varchar(1000) 
     BEGIN 
       DECLARE sTemp VARCHAR(1000); 
       DECLARE sTempChd VARCHAR(1000); 
     
       SET sTemp = ''; 
       SET sTempChd =cast(rootId as CHAR); 
     
       WHILE sTempChd is not null DO 
         SET sTemp = concat(sTemp,',',sTempChd); 
         SELECT group_concat(id) INTO sTempChd FROM t_org where FIND_IN_SET(parent_id,sTempChd)>0; 
      END WHILE; 
      RETURN sTemp; 
    END;


create table t_processor_log 
(
   id                   varchar(32)                    not null,
   processor_id         varchar(32)                    null comment '处理器ID' ,
   operation_date       varchar(19)                    null comment '操作时间' ,
   start_status         char(1)                        null comment '启动状态' ,
   error_info           varchar(128)                   null comment '启动错误描述' ,
   constraint pk_t_processor_log_id primary key (id)
);


