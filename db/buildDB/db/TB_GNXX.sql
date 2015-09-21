-- Create table
create table TB_GNXX
(
  ZJID      VARCHAR2(30) not null,
  GNMC      VARCHAR2(100) not null,
  GNDM      VARCHAR2(100) not null,
  CDXX_ZJID VARCHAR2(30) not null,
  GNXH      VARCHAR2(10) not null,
  SYBZ      CHAR(1) not null,
  BZ        VARCHAR2(1000),
  CJYH      VARCHAR2(30),
  GXYH      VARCHAR2(30),
  JLXZSJ    VARCHAR2(14),
  JLXGSJ    VARCHAR2(14),
  JLSCSJ    VARCHAR2(14),
  JLYXZT    VARCHAR2(2) not null,
  GNURL     VARCHAR2(512)
);
-- Add comments to the table 
comment on table TB_GNXX
  is '功能信息表';
-- Add comments to the columns 
comment on column TB_GNXX.ZJID
  is '主键ID';
comment on column TB_GNXX.GNMC
  is '功能名称';
comment on column TB_GNXX.GNDM
  is '功能代码';
comment on column TB_GNXX.CDXX_ZJID
  is '菜单主键ID';
comment on column TB_GNXX.GNXH
  is '功能序号';
comment on column TB_GNXX.SYBZ
  is '使用标识 0-禁用；1-启用';
comment on column TB_GNXX.BZ
  is '备注';
comment on column TB_GNXX.CJYH
  is '创建用户';
comment on column TB_GNXX.GXYH
  is '更新用户';
comment on column TB_GNXX.JLXZSJ
  is '记录新增时间';
comment on column TB_GNXX.JLXGSJ
  is '记录更新时间';
comment on column TB_GNXX.JLSCSJ
  is '记录删除时间';
comment on column TB_GNXX.JLYXZT
  is '记录有效标志 -1-已归档；0-无效（已注销）；1-有效';
comment on column TB_GNXX.GNURL
  is '功能URL';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_GNXX
  add constraint PK_TB_GNXX primary key (ZJID);

insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('142', '菜单权限导出', 'SYS_MENU_AUTH_EXPORT', '30', '3', '1', null, null, null, '20131022173856', '20131022173856', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('143', '查看功能', 'SYS_FUNC_VIEW', '31', '8', '1', null, null, null, '20131022174240', '20131024093143', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('144', '查看参数', 'SYS_PARAM_VIEW', '32', '6', '1', null, null, null, '20131022174445', '20131023091118', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('136', '新增归档配置', 'SYS_GDPZ_ADD', '87', '1', '1', null, null, null, '20131008141854', '20131022180209', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('137', '查看归档清单', 'SYS_GDQD_VIEW', '88', '1', '1', null, null, null, '20131008141950', '20131022180443', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('99', '新增任务调度器', 'SYS_JOBSCHEDULER_ADD', '63', '1', '1', null, null, null, '20121206175630', '20121206180237', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('89', '修改一级代码', 'SYS_CODE_ONE_UPDATE', '57', '2', '1', null, null, null, '20121122104150', '20121122104150', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('91', '启用一级代码', 'SYS_CODE_ONE_ENABLED', '57', '4', '1', null, null, null, '20121122104300', '20121122104300', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('93', '新增二级代码', 'SYS_CODE_TWO_ADD', '57', '6', '1', null, null, null, '20121122104732', '20121122104732', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('95', '删除二级代码', 'SYS_CODE_TWO_DELETE', '57', '8', '1', null, null, null, '20121122104754', '20121122104754', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('97', '禁用二级代码', 'SYS_CODE_TWO_FORBID', '57', '10', '1', null, null, null, '20121122104826', '20121122104826', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('88', '新增一级代码', 'SYS_CODE_ONE_ADD', '57', '1', '1', null, null, null, '20121122104103', '20121122104103', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('90', '删除一级代码', 'SYS_CODE_ONE_DELETE', '57', '3', '1', null, null, null, '20121122104221', '20121122104221', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('92', '禁用一级代码', 'SYS_CODE_ONE_FORBID', '57', '5', '1', null, null, null, '20121122104332', '20121122104332', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('94', '修改二级代码', 'SYS_CODE_TWO_UPDATE', '57', '7', '1', null, null, null, '20121122104743', '20121122104743', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('96', '启用二级代码', 'SYS_CODE_TWO_ENABLED', '57', '9', '1', null, null, null, '20121122104809', '20121122104809', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('98', '角色分配', 'SYS_ORG_ASSIGN_ROLE', '27', '4', '1', null, null, null, '20121122105109', '20121122105109', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('101', '删除任务调度器', 'SYS_JOBSCHEDULER_DEL', '63', '3', '1', null, null, null, '20121206175729', '20121206180253', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('102', '切换任务调度器', 'SYS_JOBSCEHDULER_SWITCH', '63', '4', '1', null, null, null, '20121206175816', '20121206180304', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('104', '修改任务', 'SYS_JOB_UPDATE', '63', '6', '1', null, null, null, '20121206175916', '20121206175916', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('106', '启动任务', 'SYS_JOB_START', '63', '8', '1', null, null, null, '20121206180013', '20121206180013', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('107', '停止任务', 'SYS_JOB_STOP', '63', '9', '1', null, null, null, '20121206180059', '20121206180059', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('109', '删除任务运行日志', 'SYS_JOBRUNLOG_DEL', '63', '11', '1', null, null, null, '20121206180329', '20121206180329', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('6', '修改用户', 'SYS_USER_UPDATE', '28', '3', '1', null, null, null, '20120511135352', '20130105170114', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('8', '新增角色', 'SYS_ROLE_ADD', '29', '1', '1', null, null, null, '20120511135555', '20121123103834', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('9', '修改角色', 'SYS_ROLE_UPDATE', '29', '3', '1', null, null, null, '20120511135638', '20130806134623', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('4', '新增用户', 'SYS_USER_ADD', '28', '1', '1', null, null, null, '20120511135322', '20130806135817', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('10', '新增菜单', 'SYS_MENU_ADD', '30', '1', '1', null, null, null, '20120511135909', '20131024144751', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('30', '禁用功能', 'SYS_FUNC_FORBID', '31', '3', '1', null, null, null, '20120517152722', '20131024093143', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('31', '删除功能', 'SYS_FUNC_DELETE', '31', '4', '1', null, null, null, '20120517152741', '20131024093143', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('1', '新增单位', 'SYS_ORG_ADD', '27', '1', '1', null, null, null, '20120511132813', '20130812102310', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('24', '删除角色', 'SYS_ROLE_DELETE', '29', '3', '1', null, null, null, '20120517135525', '20121123103834', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('11', '新增功能', 'SYS_FUC_ADD', '31', '1', '1', null, null, null, '20120511140104', '20131024093143', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('25', '禁用角色', 'SYS_ROLE_FORBID', '29', '4', '1', null, null, null, '20120517140420', '20121123103834', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('28', '角色分配功能', 'SYS_ROLE_ASSIGN_FUNC', '29', '7', '1', null, null, null, '20120517143745', '20121123103834', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('34', '删除单位信息', 'SYS_ORG_DELETE', '27', '3', '1', null, null, null, '20120517162431', '20120726093435', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('41', '用户调动', 'SYS_USER_TRANSFER', '28', '7', '1', null, null, null, '20120517171251', null, null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('42', '用户禁用', 'SYS_USER_FORBID', '28', '8', '1', null, null, null, '20120517171359', null, null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('43', '用户启用', 'SYS_USER_ENABLED', '28', '9', '1', null, null, null, '20120517171432', '20120725180314', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('44', '修改参数', 'SYS_PARAM_UPDATE', '32', '2', '1', null, null, null, '20120517173500', '20131023091118', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('46', '禁用参数', 'SYS_PARAM_FORBID', '32', '4', '1', null, null, null, '20120517173545', '20131023091118', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('47', '启用参数', 'SYS_PARAM_ENABLED', '32', '5', '1', null, null, null, '20120517173710', '20131023091118', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('14', '为角色分配功能', 'SYS_ROLE_FUNC_ASSIGN', '60', '1', '1', null, null, null, '20120511140659', '20121123103834', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('22', '为用户分配角色', 'SYS_USER_ROLE_ASSIGN', '60', '1', '1', null, null, null, '20120511141357', '20121123103834', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('13', '新增参数', 'SYS_PARAM_ADD', '32', '1', '1', null, null, null, '20120511140158', '20131023091118', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('15', '为功能分配角色', 'SYS_FUNC_ROLE_ASSIGN', '60', '2', '1', null, null, null, '20120511140752', '20121123103834', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('23', '为角色分配用户', 'SYS_ROLE_USER_ASSIGN', '60', '2', '1', null, null, null, '20120511141441', '20121123103834', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('26', '启用角色', 'SYS_ROLE_ENABLED', '29', '5', '1', null, null, null, '20120517140518', '20121123103834', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('27', '为角色分配用户', 'SYS_ROLE_ASSIGN_USER', '29', '6', '1', null, null, null, '20120517143117', '20121123103834', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('37', '删除用户', 'SYS_USER_DELETE', '28', '3', '1', null, null, null, '20120517170729', null, null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('38', '密码重置', 'SYS_USER_RESET_PWD', '28', '4', '1', null, null, null, '20120517170800', null, null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('39', '用户分配功能', 'SYS_USER_ASSIGN_FUNC', '28', '5', '1', null, null, null, '20120517170935', null, null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('40', '用户分配角色', 'SYS_USER_ASSIGN_ROLE', '28', '6', '1', null, null, null, '20120517171013', null, null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('45', '删除参数', 'SYS_PARAM_DELETE', '32', '3', '1', null, null, null, '20120517173524', '20131023091118', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('29', '修改功能', 'SYS_FUNC_UPDATE', '31', '2', '1', null, null, null, '20120517152655', '20131024093143', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('2', '修改单位', 'SYS_ORG_UPDETE', '27', '2', '1', null, null, null, '20120511132959', '20130806140228', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('32', '启用功能', 'SYS_FUNC_ENABLED', '31', '5', '1', null, null, null, '20120517153455', '20131024093143', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('33', '修改菜单', 'SYS_MENU_UPDATE', '30', '2', '1', null, null, null, '20120517155139', '20131024144751', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('100', '修改任务调度器', 'SYS_JOBSCHEDULER_UPDATE', '63', '2', '1', null, null, null, '20121206175700', '20121206180245', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('103', '新增任务', 'SYS_JOB_ADD', '63', '5', '1', null, null, null, '20121206175854', '20121206175854', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('105', '删除任务', 'SYS_JOB_DEL', '63', '7', '1', null, null, null, '20121206175939', '20121206175939', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('108', '发布任务', 'SYS_JOB_RELEASE', '63', '10', '1', null, null, null, '20121206180154', '20121206180154', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('145', '文件修改', 'SYS_FILE_UPDATE', '77', '1', '1', null, null, null, '20131022175157', '20131022175157', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('146', '文件信息删除', 'SYS_FILE_DEL', '77', '2', '1', null, null, null, '20131022175217', '20131022175217', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('147', '为单位分配角色', 'SYS_ORG_ROLE_ASSIGN', '60', '5', '1', null, null, null, '20131022175659', '20131022175659', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('148', '登录日志查询', 'SYS_LOGON_LOG_QUERY', '47', '1', '1', null, null, null, '20131022175809', '20131022175858', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('149', '操作日志查询', 'SYS_OPERATE_LOG_QUERY', '48', '1', '1', null, null, null, '20131022175849', '20131022175849', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('150', '接口日志查询', 'SYS_INTERFACE_LOG_QUERY', '72', '1', '1', null, null, null, '20131022175919', '20131022175919', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('151', '修改归档配置', 'SYS_GDPZ_UPDATE', '87', '2', '1', null, null, null, '20131022180222', '20131022180222', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('152', '删除归档配置', 'SYS_GDPZ_DEL', '87', '3', '1', null, null, null, '20131022180237', '20131022180237', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('153', '查看归档配置', 'SYS_GDPZ_VIEW', '87', '4', '1', null, null, null, '20131022180325', '20131022180325', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('154', '查询归档配置', 'SYS_GDPZ_QUERY', '87', '5', '1', null, null, null, '20131022180355', '20131022180355', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('155', '查询归档清单', 'SYS_GDQD_QUERY', '88', '2', '1', null, null, null, '20131022180430', '20131022180430', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('156', '查询功能', 'SYS_FUNC_QUERY', '31', '9', '1', null, null, null, '20131023091032', '20131024093143', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('157', '查询参数', 'SYS_PARAM_QUERY', '32', '7', '1', null, null, null, '20131023091137', '20131023091137', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('158', '文件查询', 'SYS_FILE_QUERY', '77', '3', '1', null, null, null, '20131023091322', '20131023091322', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('159', '查询单位', 'SYS_ORG_QUERY', '27', '5', '1', null, null, null, '20131023091431', '20131023091431', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('160', '查询用户', 'SYS_USER_QUERY', '28', '10', '1', null, null, null, '20131023091525', '20131023091525', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('161', '查询角色', 'SYS_ROLE_QUERY', '29', '8', '1', null, null, null, '20131023091626', '20131023091626', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('162', '查询一级代码', 'SYS_CODE_ONE_QUERY', '57', '11', '1', null, null, null, '20131023091810', '20131023091810', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('163', '查询二级代码', 'SYS_CODE_TWO_QUERY', '57', '12', '1', null, null, null, '20131023091835', '20131023091835', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('164', '查询任务', 'SYS_JOB_QUERY', '63', '12', '1', null, null, null, '20131023092051', '20131023092051', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('165', '删除菜单', 'SYS_MENU_DEL', '30', '4', '1', null, null, null, '20131030165426', '20131030165426', null, '1', null);

insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('180', '流程定义查询', 'BPM_DEF_QUERY', '104', '1', '1', null, null, null, '20140103145433', '20140103145433', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('181', '流程实例查询', 'BPM_PRORUN_QUERY', '105', '1', '1', null, null, null, '20140103145531', '20140103145531', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('182', '流程任务查询', 'BPM_TASK_QUERY', '106', '1', '1', null, null, null, '20131211102740', '20140103145506', null, '1', null);

insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('183', '待办事项', 'BPM_PENDING', '94', '1', '1', null, null, null, '20131211141933', '20140303105334', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('184', '经办事项', 'BPM_ALREADY_MATTER', '95', '1', '1', null, null, null, '20131217103626', '20140303105344', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('185', '办结事项', 'BPM_COMPLETE_MATTER', '96', '1', '1', null, null, null, '20131224111644', '20140303105356', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('186', '代理事项', 'BPM_ACCORDING_MATTER', '97', '1', '1', null, null, null, '20140218175743', '20140303105404', null, '1', null);

insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('187', '新建流程', 'BPM_FLOW_NEW', '98', '1', '1', null, null, null, '20131227104400', '20140303105320', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('188', '我的请求', 'BPM_MY_REQUEST', '99', '1', '1', null, null, null, '20131223160253', '20140303105310', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('189', '我的办结', 'BPM_MY_COMPLETE', '100', '1', '1', null, null, null, '20131223171850', '20140303105545', null, '1', null);
insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('190', '我的草稿', 'BPM_MY_DRAFT', '101', '1', '1', null, null, null, '20131223100930', '20140303105516', null, '1', null);

insert into TB_GNXX (ZJID, GNMC, GNDM, CDXX_ZJID, GNXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, GNURL)
values ('191', '委托代理', 'BPM_AGENT', '102', '1', '1', null, null, null, '20140217132109', '20140303105438', null, '1', null);

commit;
exit;