-- Create table 菜单信息表
create table TB_CDXX
(
  ZJID     VARCHAR2(30) not null,
  CDMC     VARCHAR2(100) not null,
  CDJB     CHAR(1) not null,
  CDXH     VARCHAR2(10) not null,
  SJCD     VARCHAR2(30),
  CDDZ     VARCHAR2(200) not null,
  SFZHYICD CHAR(1) not null,
  SYBZ     CHAR(1) not null,
  BZ       VARCHAR2(1000),
  CJYH     VARCHAR2(30),
  GXYH     VARCHAR2(30),
  JLXZSJ   VARCHAR2(14),
  JLXGSJ   VARCHAR2(14),
  JLSCSJ   VARCHAR2(14),
  JLYXZT   VARCHAR2(2) not null,
  ZYLB     CHAR(1),
  ZCDW     VARCHAR2(30),
  ZCSJ     VARCHAR2(20)
);
-- Add comments to the table 
comment on table TB_CDXX
  is '菜单信息表';
-- Add comments to the columns 
comment on column TB_CDXX.ZJID
  is '主键ID';
comment on column TB_CDXX.CDMC
  is '菜单名称';
comment on column TB_CDXX.CDJB
  is '菜单级别';
comment on column TB_CDXX.CDXH
  is '菜单序号';
comment on column TB_CDXX.SJCD
  is '上级菜单';
comment on column TB_CDXX.CDDZ
  is '菜单地址';
comment on column TB_CDXX.SFZHYICD
  is '是否最后一级菜单 0-否；1-是';
comment on column TB_CDXX.SYBZ
  is '使用标识 0-禁用；1-启用';
comment on column TB_CDXX.BZ
  is '备注';
comment on column TB_CDXX.CJYH
  is '创建用户';
comment on column TB_CDXX.GXYH
  is '更新用户';
comment on column TB_CDXX.JLXZSJ
  is '记录新增时间';
comment on column TB_CDXX.JLXGSJ
  is '记录更新时间';
comment on column TB_CDXX.JLSCSJ
  is '记录删除时间';
comment on column TB_CDXX.JLYXZT
  is '记录有效标志 -1-已归档；0-无效（已注销）；1-有效';
comment on column TB_CDXX.ZYLB
  is '资源类别-0-菜单资源，-1-报表资源';
comment on column TB_CDXX.ZCDW
  is '注册单位';
comment on column TB_CDXX.ZCSJ
  is '注册时间';
alter table TB_CDXX add constraint PK_TB_CDXX primary key (ZJID);

insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('64', '权限管理', '2', '3', '26', 'javascript:void(0)', '0', '1', null, null, null, '20121210151723', '20121210151759', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('86', '归档管理', '2', '7', '26', 'javascript:void(0)', '0', '1', null, null, null, '20131008141741', '20131010102201', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('87', '归档配置管理', '3', '1', '86', '/gdpz/toMain.html', '1', '1', null, null, null, '20131008141854', '20131022093322', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('88', '系统归档清单', '3', '2', '86', '/gdqd/toMain.html', '1', '1', null, null, null, '20131008141950', '20131009101324', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('63', '任务调度', '3', '6', '56', '/job/toMain.html', '1', '1', null, null, null, '20121206164318', '20121206164339', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('72', '接口调用日志', '3', '4', '46', '/interfaceLog/toMain.html', '1', '1', null, null, null, '20130428132306', '20130428132306', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('77', '文件信息管理', '3', '7', '56', '/uploadFile/toMain.html', '1', '1', null, null, null, '20130527100550', '20130527100600', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('56', '基础信息管理', '2', '1', '26', 'javascript:void(0)', '0', '1', null, null, null, '20120517142926', '20121206164412', null, '1', '0', null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('26', '系统管理', '1', '9', '0', 'javascript:void(0)', '0', '1', null, null, null, '20120511103503', '20121025110241', null, '1', '0', null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('27', '单位信息管理', '3', '1', '38', '/orgCtl/toOrgMainList.html', '1', '1', null, null, null, '20120511103543', '20130805152915', null, '1', '0', null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('28', '用户信息管理', '3', '2', '38', '/userCtl/toUserMainList.html', '1', '1', null, null, null, '20120511103600', '20120511111120', null, '1', '0', null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('29', '角色信息管理', '3', '1', '64', '/roleCtl/toRoleMainList.html', '1', '1', null, null, null, '20120511103617', '20121210151914', null, '1', '0', null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('30', '菜单信息管理', '3', '1', '56', '/menu/menu_main.html', '1', '1', null, null, null, '20120511103707', '20130812102154', null, '1', '0', null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('31', '功能信息管理', '3', '2', '56', '/func/func_main.html', '1', '1', null, null, null, '20120511110426', '20130426140353', null, '1', '0', null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('32', '参数信息管理', '3', '3', '56', '/para/toParaList.html', '1', '1', null, null, null, '20120511110449', '20121115134902', null, '1', '0', null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('38', '组织机构管理', '2', '2', '26', 'javascript:void(0)', '0', '1', null, null, null, '20120511111111', '20121206164417', null, '1', '0', null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('46', '日志管理', '2', '4', '26', 'javascript:void(0)', '0', '1', null, null, null, '20120514155404', '20121210151803', null, '1', '0', null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('47', '系统登录日志', '2', '1', '46', '/logonLog/toMain.html', '1', '1', null, null, null, '20120514155734', '20121114150832', null, '1', '0', null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('60', '权限分配管理', '3', '2', '64', '/toAssignMain.html', '1', '1', null, null, null, '20120518170130', '20121210151910', null, '1', '0', null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('48', '系统操作日志', '2', '3', '46', '/sysLog/toMain.html', '1', '1', null, null, null, '20120515143915', '20120515144043', null, '1', '0', null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('57', '代码信息管理', '3', '4', '56', '/codeCtl/toCodeValidList.html', '1', '1', null, null, null, '20120518134215', '20121206164330', null, '1', '0', null, null);

insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('103', '流程管理', '2', '7', '26', 'javascript:void(0)', '0', '1', null, null, null, '20130515160401', '20131106163921', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('104', '流程定义管理', '3', '1', '103', '/workflow/bpmDef.html', '0', '1', null, null, null, '20130515160520', '20130625171824', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('105', '流程实例管理', '3', '3', '103', '/workflow/bpm/processRun/toMain.html', '0', '1', null, null, null, '20130520140940', '20131211102643', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('106', '流程任务管理', '3', '2', '103', '/workflow/task/toList.html', '0', '1', null, null, null, '20131211102740', '20131211102740', null, '1', null, null, null);

insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('90', '个人中心', '1', '9', '0', 'javascript:void(0)', '0', '1', null, null, null, '20131211141847', '20131224132126', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('91', '我的流程', '2', '1', '90', 'javascript:void(0)', '0', '1', null, null, null, '20131217103014', '20131218172127', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('92', '我接收的流程', '3', '1', '91', 'javascript:void(0)', '0', '1', null, null, null, '20131223160101', '20131226160745', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('93', '我发起的流程', '3', '2', '91', 'javascript:void(0)', '0', '1', null, null, null, '20131219165312', '20131226160755', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('102', '流程委托代理', '3', '3', '91', '/workflow/bpm/agentSetting/toMain.html', '0', '1', null, null, null, '20140217132109', '20140217132542', null, '1', null, null, null);

insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('94', '待办事项', '4', '1', '92', '/workflow/task/toPendingList.html', '1', '1', null, null, null, '20131211141933', '20131223160112', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('95', '经办事项', '4', '2', '92', '/workflow/bpm/processRun/toAlreadyMatterList.html', '1', '1', null, null, null, '20131217103626', '20131223160121', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('96', '办结事项', '4', '3', '92', '/workflow/bpm/processRun/toCompleteMatterList.html', '1', '1', null, null, null, '20131224111644', '20131226160623', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('97', '代理事项', '4', '4', '92', '/workflow/bpm/taskExe/toAccordingMattersList.html', '0', '1', null, null, null, '20140218175743', '20140220112307', null, '1', null, null, null);

insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('98', '新建流程', '4', '1', '93', '/workflow/bpmDef/myListMain.html', '1', '1', null, null, null, '20131227104400', '20131227105830', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('99', '我的请求', '4', '2', '93', '/workflow/bpm/processRun/toMyRequestList.html', '1', '1', null, null, null, '20131223160253', '20131227104409', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('100', '我的办结', '4', '3', '93', '/workflow/bpm/processRun/toMyCompleteList.html', '1', '1', null, null, null, '20131223171849', '20131227104414', null, '1', null, null, null);
insert into TB_CDXX (ZJID, CDMC, CDJB, CDXH, SJCD, CDDZ, SFZHYICD, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, ZYLB, ZCDW, ZCSJ)
values ('101', '我的草稿', '4', '4', '93', '/workflow/bpm/processRun/toMyDraftList.html', '0', '1', null, null, null, '20131223100930', '20131227104420', null, '1', null, null, null);

commit;
exit;