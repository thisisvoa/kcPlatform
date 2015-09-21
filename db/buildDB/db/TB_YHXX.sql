-- Create table
create table TB_YHXX
(
  ZJID      VARCHAR2(30) not null,
  YHMC      VARCHAR2(30) not null,
  YHDLZH    VARCHAR2(30) not null,
  YHDLMM    VARCHAR2(32) not null,
  JYBH      VARCHAR2(20) not null,
  SSDW_ZJID VARCHAR2(30) not null,
  SFZH      VARCHAR2(18) not null,
  XB        CHAR(1),
  LXDH      VARCHAR2(20),
  YDDH      VARCHAR2(20),
  DZYX      VARCHAR2(20),
  YHLX      CHAR(1) not null,
  YHJB      CHAR(1) not null,
  SYBZ      CHAR(1) not null,
  BZ        VARCHAR2(1000),
  CJYH      VARCHAR2(30),
  GXYH      VARCHAR2(30),
  JLXZSJ    VARCHAR2(14),
  JLXGSJ    VARCHAR2(14),
  JLSCSJ    VARCHAR2(14),
  JLYXZT    VARCHAR2(2) not null
);
-- Add comments to the table 
comment on table TB_YHXX
  is '用户信息表';
-- Add comments to the columns 
comment on column TB_YHXX.ZJID
  is '主键ID';
comment on column TB_YHXX.YHMC
  is '用户名称';
comment on column TB_YHXX.YHDLZH
  is '用户登录账号';
comment on column TB_YHXX.YHDLMM
  is '用户登录密码';
comment on column TB_YHXX.JYBH
  is '警员编号';
comment on column TB_YHXX.SSDW_ZJID
  is '所属单位主键ID';
comment on column TB_YHXX.SFZH
  is '身份证号';
comment on column TB_YHXX.XB
  is '性别';
comment on column TB_YHXX.LXDH
  is '联系电话';
comment on column TB_YHXX.YDDH
  is '移动电话';
comment on column TB_YHXX.DZYX
  is '电子邮箱';
comment on column TB_YHXX.YHLX
  is '用户类型 预留字段';
comment on column TB_YHXX.YHJB
  is '用户级别 预留字段';
comment on column TB_YHXX.SYBZ
  is '使用标识 0-禁用；1-启用';
comment on column TB_YHXX.BZ
  is '备注';
comment on column TB_YHXX.CJYH
  is '创建用户';
comment on column TB_YHXX.GXYH
  is '更新用户';
comment on column TB_YHXX.JLXZSJ
  is '记录新增时间';
comment on column TB_YHXX.JLXGSJ
  is '记录更新时间';
comment on column TB_YHXX.JLSCSJ
  is '记录删除时间';
comment on column TB_YHXX.JLYXZT
  is '记录有效标志 -1-已归档；0-无效（已注销）；1-有效';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_YHXX
  add constraint PK_TB_YHXX primary key (ZJID);
insert into TB_YHXX (ZJID, YHMC, YHDLZH, YHDLMM, JYBH, SSDW_ZJID, SFZH, XB, LXDH, YDDH, DZYX, YHLX, YHJB, SYBZ, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT)
values ('1000000000000', '系统管理员', 'admin', 'E10ADC3949BA59ABBE56E057F20F883E', '000', '520000000000', '522636197905083213', '1', null, null, null, '1', '1', '1', null, null, '1000000000000', '20121020163021', '20131028101405', null, '1');
commit;
exit;