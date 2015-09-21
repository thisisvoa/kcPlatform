-- Create table
create table TB_DMXX
(
  ZJID   VARCHAR2(36) not null,
  DMMC   VARCHAR2(100) not null,
  DMJC   VARCHAR2(30) not null,
  SFDMX  CHAR(1) not null,
  DMX_BH VARCHAR2(30),
  DMX_MC VARCHAR2(100),
  PXH    VARCHAR2(10) not null,
  SYBZ   CHAR(1) not null,
  BZ     VARCHAR2(1000),
  CJYH   VARCHAR2(30),
  GXYH   VARCHAR2(30),
  JLXZSJ VARCHAR2(14),
  JLXGSJ VARCHAR2(14),
  JLSCSJ VARCHAR2(14),
  JLYXZT VARCHAR2(2)
);
-- Add comments to the table 
comment on table TB_DMXX
  is '代码信息表';
-- Add comments to the columns 
comment on column TB_DMXX.ZJID
  is '主键ID';
comment on column TB_DMXX.DMMC
  is '代码名称';
comment on column TB_DMXX.DMJC
  is '代码简称';
comment on column TB_DMXX.SFDMX
  is '是否代码项';
comment on column TB_DMXX.DMX_BH
  is '代码项编码';
comment on column TB_DMXX.DMX_MC
  is '代码项名称';
comment on column TB_DMXX.PXH
  is '排序号';
comment on column TB_DMXX.SYBZ
  is '使用标识 0-禁用；1-启用';
comment on column TB_DMXX.BZ
  is '备注';
comment on column TB_DMXX.CJYH
  is '创建用户';
comment on column TB_DMXX.GXYH
  is '更新用户';
comment on column TB_DMXX.JLXZSJ
  is '记录新增时间';
comment on column TB_DMXX.JLXGSJ
  is '记录更新时间';
comment on column TB_DMXX.JLSCSJ
  is '记录删除时间';
comment on column TB_DMXX.JLYXZT
  is '记录有效标志 -1-已归档；0-无效（已注销）；1-有效';
alter table TB_DMXX add constraint PK_TB_DMXX primary key (ZJID);
insert into TB_DMXX (ZJID, DMMC, DMJC, SFDMX, DMX_BH, DMX_MC, PXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT)
values ('10204141', '流程参与者类型', 'BPM_PARTICIPANT', '0', null, null, '1', '1', null, '1000000000000', '1000000000000', '20130708135923', '20130812104504', null, '1');
insert into TB_DMXX (ZJID, DMMC, DMJC, SFDMX, DMX_BH, DMX_MC, PXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT)
values ('10204142', '流程参与者类型', 'BPM_PARTICIPANT', '1', '1', '发起人', '1', '1', null, '1000000000000', '1000000000000', '20130708140415', '20130812104602', null, '1');
insert into TB_DMXX (ZJID, DMMC, DMJC, SFDMX, DMX_BH, DMX_MC, PXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT)
values ('10204143', '流程参与者类型', 'BPM_PARTICIPANT', '1', '2', '用户', '2', '1', null, '1000000000000', null, '20130708140434', '20130708140434', null, '1');
insert into TB_DMXX (ZJID, DMMC, DMJC, SFDMX, DMX_BH, DMX_MC, PXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT)
values ('10204144', '流程参与者类型', 'BPM_PARTICIPANT', '1', '3', '角色', '3', '1', null, '1000000000000', null, '20130708140500', '20130708140500', null, '1');
insert into TB_DMXX (ZJID, DMMC, DMJC, SFDMX, DMX_BH, DMX_MC, PXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT)
values ('10204145', '流程参与者类型', 'BPM_PARTICIPANT', '1', '4', '单位', '4', '1', null, '1000000000000', null, '20130708140517', '20130708140517', null, '1');
insert into TB_DMXX (ZJID, DMMC, DMJC, SFDMX, DMX_BH, DMX_MC, PXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT)
values ('10204155', '流程参与者类型', 'BPM_PARTICIPANT', '1', '8', '发起人同部门', '8', '1', null, '1000000000000', null, '20140115170931', '20140115170931', null, '1');
insert into TB_DMXX (ZJID, DMMC, DMJC, SFDMX, DMX_BH, DMX_MC, PXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT)
values ('10204154', '流程参与者类型', 'BPM_PARTICIPANT', '1', '9', '发起人同部门角色', '9', '1', null, '1000000000000', null, '20140115170951', '20140115170951', null, '1');
insert into TB_DMXX (ZJID, DMMC, DMJC, SFDMX, DMX_BH, DMX_MC, PXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT)
values ('10204153', '流程参与者类型', 'BPM_PARTICIPANT', '1', '7', '已执行节点相同执行人同部门角色', '7', '1', null, '1000000000000', '1000000000000', '20140115164202', '20140115170904', null, '1');
insert into TB_DMXX (ZJID, DMMC, DMJC, SFDMX, DMX_BH, DMX_MC, PXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT)
values ('10204146', '流程参与者类型', 'BPM_PARTICIPANT', '1', '5', '已执行节点相同执行人', '5', '1', null, '1000000000000', '1000000000000', '20130708150556', '20131120093005', null, '1');
insert into TB_DMXX (ZJID, DMMC, DMJC, SFDMX, DMX_BH, DMX_MC, PXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT)
values ('10204152', '流程参与者类型', 'BPM_PARTICIPANT', '1', '6', '已执行节点相同执行人同部门', '6', '1', null, '1000000000000', '1000000000000', '20140115164149', '20140115170843', null, '1');
insert into TB_DMXX (ZJID, DMMC, DMJC, SFDMX, DMX_BH, DMX_MC, PXH, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT)
values ('10204148', '流程参与者类型', 'BPM_PARTICIPANT', '1', '20', '脚本', '20', '1', null, '1000000000000', '1000000000000', '20130708151839', '20140115170835', null, '1');
commit;
exit;