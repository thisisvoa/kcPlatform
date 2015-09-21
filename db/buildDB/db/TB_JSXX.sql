-- Create table
create table TB_JSXX
(
  ZJID   VARCHAR2(30) not null,
  JSMC   VARCHAR2(100) not null,
  JSLX   VARCHAR2(2) not null,
  JSJB   VARCHAR2(2) not null,
  SYBZ   CHAR(1) not null,
  BZ     VARCHAR2(1000),
  CJYH   VARCHAR2(30),
  GXYH   VARCHAR2(30),
  JLXZSJ VARCHAR2(14),
  JLXGSJ VARCHAR2(14),
  JLSCSJ VARCHAR2(14),
  JLYXZT VARCHAR2(2) not null,
  JSDM   VARCHAR2(30)
);
-- Add comments to the table 
comment on table TB_JSXX
  is '角色信息表';
-- Add comments to the columns 
comment on column TB_JSXX.ZJID
  is '主键ID';
comment on column TB_JSXX.JSMC
  is '角色名称';
comment on column TB_JSXX.JSLX
  is '角色类型 预留字段，默认为01';
comment on column TB_JSXX.JSJB
  is '角色级别 预留字段，10表示省级、20表示市级、30表示县级';
comment on column TB_JSXX.SYBZ
  is '使用标识 0-禁用；1-启用';
comment on column TB_JSXX.BZ
  is '备注';
comment on column TB_JSXX.CJYH
  is '创建用户';
comment on column TB_JSXX.GXYH
  is '更新用户';
comment on column TB_JSXX.JLXZSJ
  is '记录新增时间';
comment on column TB_JSXX.JLXGSJ
  is '记录更新时间';
comment on column TB_JSXX.JLSCSJ
  is '记录删除时间';
comment on column TB_JSXX.JLYXZT
  is '记录有效标志 -1-已归档；0-无效（已注销）；1-有效';
comment on column TB_JSXX.JSDM
  is '角色代码';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_JSXX
  add constraint PK_TB_JSXX primary key (ZJID);
insert into TB_JSXX (ZJID, JSMC, JSLX, JSJB, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, JSDM)
values ('10019', '县分局用户管理角色', '01', '3', '1', null, '1000000000000', '1000000000000', '20130110100601', '20130829165228', null, '1', '0099');
insert into TB_JSXX (ZJID, JSMC, JSLX, JSJB, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, JSDM)
values ('10018', '市州局用户管理角色', '01', '2', '1', null, '1000000000000', '1000000000000', '20130110100549', '20130829165235', null, '1', '0066');
insert into TB_JSXX (ZJID, JSMC, JSLX, JSJB, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT, JSDM)
values ('10020', '省厅用户管理角色', '01', '1', '1', null, '1000000000000', '1000000000000', '20130110100611', '20130829165221', null, '1', '0088');
commit;
exit;