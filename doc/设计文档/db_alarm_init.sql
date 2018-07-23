
/* 初始 用户 */
INSERT INTO t_user VALUES ('u8952c8666964e07a9a285b10d706a61', 'admin@sierotech.com', '系统管理员', 'o3f25612335a4d188020d617801ea7bf', '00000000', 'N', 'dd4b21e9ef71e1291183a46b913ae6f2', 'Y', 'F', null, null, null);

/* 初始 菜单 */
INSERT INTO  t_menu(id,menu_name,menu_code,menu_url,icon_url,parent_id,order_num) VALUES ('ma900252694f4bd99fa3e16ce9dfacd0','电子地图','m.electronic.map','','','ROOT',2);

INSERT INTO  t_menu(id,menu_name,menu_code,menu_url,icon_url,parent_id,order_num) VALUES ('ma900252694f4bd99fa3e16ce9dfacb5','历史报警','m.history.alarm','','','ROOT',3);

INSERT INTO  t_menu(id,menu_name,menu_code,menu_url,icon_url,parent_id,order_num) VALUES ('ma900252694f4bd99fa3e16ce9dfafb8','视频回放','m.video.back','','','ROOT',4);

INSERT INTO  t_menu(id,menu_name,menu_code,menu_url,icon_url,parent_id,order_num) VALUES ('ma900252694f4bd99fa3e16ce9dfacb7','处警记录','m.processor.log','','','ROOT',5);

INSERT INTO  t_menu(id,menu_name,menu_code,menu_url,icon_url,parent_id,order_num) VALUES ('ma900252694f4bd99fa3e16ce9df989e','警情统计','m.alarm.statistics','','','ROOT',6);

INSERT INTO  t_menu(id,menu_name,menu_code,menu_url,icon_url,parent_id,order_num) VALUES ('ma900252694f4bd99fa3e16ce9df3c78','权限管理','m.auth.mgr','','','ROOT',7);

INSERT INTO  t_menu(id,menu_name,menu_code,menu_url,icon_url,parent_id,order_num) VALUES ('ma900252694f4bd99fa3e16ce9df3c62','设备管理','m.device.mgr','','','ROOT',8);

/* 初始 角色 */
INSERT INTO t_role(id,role_name,role_type,home_page,role_desc) VALUES('r0dd980638bc43efb2e01d362db3bbs2','接警岗','S','','值班员,查看系统报警的用户');

INSERT INTO t_role(id,role_name,role_type,home_page,role_desc) VALUES('r0dd980638bc43efb2e01d362db3cdf9','处警岗','S','','系统中处理报警的用户');

INSERT INTO t_role(id,role_name,role_type,home_page,role_desc) VALUES('r0dd980638bc43efb2e01d362db3cdeb','监督岗','S','','查看报警处理情况、报警统计的用户');

INSERT INTO t_role(id,role_name,role_type,home_page,role_desc) VALUES('r0dd980638bc43efb2e01d362db3cda6','管理岗','S','','管理员');

INSERT INTO t_role(id,role_name,role_type,home_page,role_desc) VALUES('r0dd980638bc43efb2e01d362db3cd88','超级管理员','R','','超级管理员');

/* 初始用户与角色关系 */
INSERT INTO t_user_role(id, user_id, role_id) VALUES('r0drk80638bc43efb2e01d362db34728','u8952c8666964e07a9a285b10d706a61','r0dd980638bc43efb2e01d362db3cd88');

/* 初始 超级管理员角色权限 */
INSERT INTO  t_role_res(id,role_id,res_type,res_id) VALUES ('rr5abe33b3ae4f459cf664859d9d1aa3','r0dd980638bc43efb2e01d362db3cd88','M','ma900252694f4bd99fa3e16ce9dfacd0');
INSERT INTO  t_role_res(id,role_id,res_type,res_id) VALUES ('rr5abe33b3ae4f459cf664859d9d1aa4','r0dd980638bc43efb2e01d362db3cd88','M','ma900252694f4bd99fa3e16ce9dfacb5');
INSERT INTO  t_role_res(id,role_id,res_type,res_id) VALUES ('rr5abe33b3ae4f459cf664859d9d1aa5','r0dd980638bc43efb2e01d362db3cd88','M','ma900252694f4bd99fa3e16ce9dfafb8');
INSERT INTO  t_role_res(id,role_id,res_type,res_id) VALUES ('rr5abe33b3ae4f459cf664859d9d1aa6','r0dd980638bc43efb2e01d362db3cd88','M','ma900252694f4bd99fa3e16ce9dfacb7');
INSERT INTO  t_role_res(id,role_id,res_type,res_id) VALUES ('rr5abe33b3ae4f459cf664859d9d1aa7','r0dd980638bc43efb2e01d362db3cd88','M','ma900252694f4bd99fa3e16ce9df989e');
INSERT INTO  t_role_res(id,role_id,res_type,res_id) VALUES ('rr5abe33b3ae4f459cf664859d9d1aa8','r0dd980638bc43efb2e01d362db3cd88','M','ma900252694f4bd99fa3e16ce9df3c78');
INSERT INTO  t_role_res(id,role_id,res_type,res_id) VALUES ('rr5abe33b3ae4f459cf664859d9d1aq7','r0dd980638bc43efb2e01d362db3cd88','M','ma900252694f4bd99fa3e16ce9df3c62');


