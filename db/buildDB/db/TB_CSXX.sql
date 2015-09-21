-- Create table
create table TB_CSXX
(
  ZJID   VARCHAR2(30) not null,
  CSMC   VARCHAR2(100) not null,
  CSDM   VARCHAR2(30) not null,
  CSZ    VARCHAR2(100) not null,
  SYBZ   CHAR(1) not null,
  BZ     VARCHAR2(1000),
  CJYH   VARCHAR2(30),
  GXYH   VARCHAR2(30),
  JLXZSJ VARCHAR2(14),
  JLXGSJ VARCHAR2(14),
  JLSCSJ VARCHAR2(14),
  JLYXZT VARCHAR2(2) not null
);
-- Add comments to the table 
comment on table TB_CSXX
  is '参数信息表';
-- Add comments to the columns 
comment on column TB_CSXX.ZJID
  is '主键ID';
comment on column TB_CSXX.CSMC
  is '参数名称';
comment on column TB_CSXX.CSDM
  is '参数代码';
comment on column TB_CSXX.CSZ
  is '参数值';
comment on column TB_CSXX.SYBZ
  is '使用标识 0-禁用；1-启用';
comment on column TB_CSXX.BZ
  is '备注';
comment on column TB_CSXX.CJYH
  is '创建用户';
comment on column TB_CSXX.GXYH
  is '更新用户';
comment on column TB_CSXX.JLXZSJ
  is '记录新增时间';
comment on column TB_CSXX.JLXGSJ
  is '记录更新时间';
comment on column TB_CSXX.JLSCSJ
  is '记录删除时间';
comment on column TB_CSXX.JLYXZT
  is '记录有效标志 -1-已归档；0-无效（已注销）；1-有效';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_CSXX add constraint PK_TB_CSXX primary key (ZJID);

insert into TB_CSXX (ZJID, CSMC, CSDM, CSZ, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT)
values ('10025', '历史在线人数', 'HIS_ONLINE', '0', '1', null, null, null, '20130502102808', '20130812102428', null, '1');
insert into TB_CSXX (ZJID, CSMC, CSDM, CSZ, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT)
values ('10022', '省级组织机构管理员角色编号', 'SHJZZJGGLYJSBH', '0088', '1', null, null, null, '20130110100647', '20130111162954', null, '1');
insert into TB_CSXX (ZJID, CSMC, CSDM, CSZ, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT)
values ('10023', '市级组织机构管理员角色编号', 'SJZZJGGLYJSBH', '0066', '1', null, null, null, '20130111162809', '20130111163015', null, '1');
insert into TB_CSXX (ZJID, CSMC, CSDM, CSZ, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT)
values ('10024', '县级组织机构管理员角色编号', 'XJZZJGGLYJSBH', '0099', '1', null, null, null, '20130111162913', '20130111163036', null, '1');
commit;
exit;