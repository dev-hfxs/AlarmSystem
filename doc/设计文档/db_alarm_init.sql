
/* ��ʼ �û� */
INSERT INTO t_user VALUES ('u8952c8666964e07a9a285b10d706a61', 'admin@sierotech.com', 'ϵͳ����Ա', 'o3f25612335a4d188020d617801ea7bf', '00000000', 'N', 'dd4b21e9ef71e1291183a46b913ae6f2', 'Y', 'F', null, null, null);

/* ��ʼ �˵� */
INSERT INTO  t_menu(id,menu_name,menu_code,menu_url,icon_url,parent_id,order_num) VALUES ('ma900252694f4bd99fa3e16ce9dfacd0','���ӵ�ͼ','m.electronic.map','','','ROOT',2);

INSERT INTO  t_menu(id,menu_name,menu_code,menu_url,icon_url,parent_id,order_num) VALUES ('ma900252694f4bd99fa3e16ce9dfacb5','��ʷ����','m.history.alarm','','','ROOT',3);

INSERT INTO  t_menu(id,menu_name,menu_code,menu_url,icon_url,parent_id,order_num) VALUES ('ma900252694f4bd99fa3e16ce9dfafb8','��Ƶ�ط�','m.video.back','','','ROOT',4);

INSERT INTO  t_menu(id,menu_name,menu_code,menu_url,icon_url,parent_id,order_num) VALUES ('ma900252694f4bd99fa3e16ce9dfacb7','������¼','m.processor.log','','','ROOT',5);

INSERT INTO  t_menu(id,menu_name,menu_code,menu_url,icon_url,parent_id,order_num) VALUES ('ma900252694f4bd99fa3e16ce9df989e','����ͳ��','m.alarm.statistics','','','ROOT',6);

INSERT INTO  t_menu(id,menu_name,menu_code,menu_url,icon_url,parent_id,order_num) VALUES ('ma900252694f4bd99fa3e16ce9df3c78','Ȩ�޹���','m.auth.mgr','','','ROOT',7);

INSERT INTO  t_menu(id,menu_name,menu_code,menu_url,icon_url,parent_id,order_num) VALUES ('ma900252694f4bd99fa3e16ce9df3c62','�豸����','m.device.mgr','','','ROOT',8);

/* ��ʼ ��ɫ */
INSERT INTO t_role(id,role_name,role_type,home_page,role_desc) VALUES('r0dd980638bc43efb2e01d362db3bbs2','�Ӿ���','S','','ֵ��Ա,�鿴ϵͳ�������û�');

INSERT INTO t_role(id,role_name,role_type,home_page,role_desc) VALUES('r0dd980638bc43efb2e01d362db3cdf9','������','S','','ϵͳ�д��������û�');

INSERT INTO t_role(id,role_name,role_type,home_page,role_desc) VALUES('r0dd980638bc43efb2e01d362db3cdeb','�ල��','S','','�鿴�����������������ͳ�Ƶ��û�');

INSERT INTO t_role(id,role_name,role_type,home_page,role_desc) VALUES('r0dd980638bc43efb2e01d362db3cda6','�����','S','','����Ա');

INSERT INTO t_role(id,role_name,role_type,home_page,role_desc) VALUES('r0dd980638bc43efb2e01d362db3cd88','��������Ա','R','','��������Ա');

/* ��ʼ�û����ɫ��ϵ */
INSERT INTO t_user_role(id, user_id, role_id) VALUES('r0drk80638bc43efb2e01d362db34728','u8952c8666964e07a9a285b10d706a61','r0dd980638bc43efb2e01d362db3cd88');

/* ��ʼ ��������Ա��ɫȨ�� */
INSERT INTO  t_role_res(id,role_id,res_type,res_id) VALUES ('rr5abe33b3ae4f459cf664859d9d1aa3','r0dd980638bc43efb2e01d362db3cd88','M','ma900252694f4bd99fa3e16ce9dfacd0');
INSERT INTO  t_role_res(id,role_id,res_type,res_id) VALUES ('rr5abe33b3ae4f459cf664859d9d1aa4','r0dd980638bc43efb2e01d362db3cd88','M','ma900252694f4bd99fa3e16ce9dfacb5');
INSERT INTO  t_role_res(id,role_id,res_type,res_id) VALUES ('rr5abe33b3ae4f459cf664859d9d1aa5','r0dd980638bc43efb2e01d362db3cd88','M','ma900252694f4bd99fa3e16ce9dfafb8');
INSERT INTO  t_role_res(id,role_id,res_type,res_id) VALUES ('rr5abe33b3ae4f459cf664859d9d1aa6','r0dd980638bc43efb2e01d362db3cd88','M','ma900252694f4bd99fa3e16ce9dfacb7');
INSERT INTO  t_role_res(id,role_id,res_type,res_id) VALUES ('rr5abe33b3ae4f459cf664859d9d1aa7','r0dd980638bc43efb2e01d362db3cd88','M','ma900252694f4bd99fa3e16ce9df989e');
INSERT INTO  t_role_res(id,role_id,res_type,res_id) VALUES ('rr5abe33b3ae4f459cf664859d9d1aa8','r0dd980638bc43efb2e01d362db3cd88','M','ma900252694f4bd99fa3e16ce9df3c78');
INSERT INTO  t_role_res(id,role_id,res_type,res_id) VALUES ('rr5abe33b3ae4f459cf664859d9d1aq7','r0dd980638bc43efb2e01d362db3cd88','M','ma900252694f4bd99fa3e16ce9df3c62');


