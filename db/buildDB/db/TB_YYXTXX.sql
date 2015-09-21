-- Create table
create table TB_YYXTXX
(
  ZJID   VARCHAR2(30) not null,
  YYXTBH VARCHAR2(30) not null,
  YYXTMC VARCHAR2(100) not null,
  ZGBM   VARCHAR2(100),
  FZR    VARCHAR2(20),
  LXFS   VARCHAR2(30),
  XTFWDZ VARCHAR2(200),
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
comment on table TB_YYXTXX
  is '应用系统信息表';
-- Add comments to the columns 
comment on column TB_YYXTXX.ZJID
  is '主键ID';
comment on column TB_YYXTXX.YYXTBH
  is '应用系统编号';
comment on column TB_YYXTXX.YYXTMC
  is '应用系统名称';
comment on column TB_YYXTXX.ZGBM
  is '主管部门';
comment on column TB_YYXTXX.FZR
  is '负责人';
comment on column TB_YYXTXX.LXFS
  is '联系方式';
comment on column TB_YYXTXX.XTFWDZ
  is '系统访问地址';
comment on column TB_YYXTXX.SYBZ
  is '使用标识 0-禁用；1-启用';
comment on column TB_YYXTXX.BZ
  is '备注';
comment on column TB_YYXTXX.CJYH
  is '创建用户';
comment on column TB_YYXTXX.GXYH
  is '更新用户';
comment on column TB_YYXTXX.JLXZSJ
  is '记录新增时间';
comment on column TB_YYXTXX.JLXGSJ
  is '记录更新时间';
comment on column TB_YYXTXX.JLSCSJ
  is '记录删除时间';
comment on column TB_YYXTXX.JLYXZT
  is '记录有效标志 -1-已归档；0-无效（已注销）；1-有效';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_YYXTXX
  add constraint PK_TB_YYXTXX primary key (ZJID);
commit;
exit;