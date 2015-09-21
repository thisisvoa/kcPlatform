-- Create table
create table TB_YHZXX
(
  ZJID   VARCHAR2(30) not null,
  YHZMC  VARCHAR2(100) not null,
  YHZLX  CHAR(1) not null,
  YHZJB  CHAR(1) not null,
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
comment on table TB_YHZXX
  is '用户组信息表';
-- Add comments to the columns 
comment on column TB_YHZXX.ZJID
  is '主键ID';
comment on column TB_YHZXX.YHZMC
  is '用户组名称';
comment on column TB_YHZXX.YHZLX
  is '用户组类型 预留字段';
comment on column TB_YHZXX.YHZJB
  is '用户组级别 预留字段';
comment on column TB_YHZXX.SYBZ
  is '使用标识 0-禁用；1-启用';
comment on column TB_YHZXX.BZ
  is '备注';
comment on column TB_YHZXX.CJYH
  is '创建用户';
comment on column TB_YHZXX.GXYH
  is '更新用户';
comment on column TB_YHZXX.JLXZSJ
  is '记录新增时间';
comment on column TB_YHZXX.JLXGSJ
  is '记录更新时间';
comment on column TB_YHZXX.JLSCSJ
  is '记录删除时间';
comment on column TB_YHZXX.JLYXZT
  is '记录有效标志 -1-已归档；0-无效（已注销）；1-有效';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_YHZXX
  add constraint PK_TB_YHZXX primary key (ZJID);
commit;
exit;