-- Create table
create table TB_GDPZ
(
  ZJID      VARCHAR2(32) not null,
  GDMC      VARCHAR2(64) not null,
  GDBMC     VARCHAR2(64) not null,
  SJCLM     VARCHAR2(32) not null,
  SJCL_SJLX CHAR(1) not null,
  SJCL_GS   VARCHAR2(32),
  GDZQ      NUMBER not null,
  GDZQ_DW   CHAR(1) not null,
  YCSJ      NUMBER not null,
  ZXSJ      VARCHAR2(16) not null,
  SYBZ      CHAR(1) not null,
  BZ        VARCHAR2(512),
  ZHZXSJ    TIMESTAMP(6),
  JLCJSJ    TIMESTAMP(6),
  JLCJYH    VARCHAR2(32),
  JLGXSJ    TIMESTAMP(6),
  JLGXYH    VARCHAR2(32)
);
-- Add comments to the table 
comment on table TB_GDPZ
  is '归档配置表';
-- Add comments to the columns 
comment on column TB_GDPZ.ZJID
  is '配置ID';
comment on column TB_GDPZ.GDMC
  is '归档名称';
comment on column TB_GDPZ.GDBMC
  is '归档表名称';
comment on column TB_GDPZ.SJCLM
  is '时间戳列名';
comment on column TB_GDPZ.SJCL_SJLX
  is '时间戳列数据类型(1:日期 2:字符串)';
comment on column TB_GDPZ.SJCL_GS
  is '时间戳列格式';
comment on column TB_GDPZ.GDZQ
  is '归档周期';
comment on column TB_GDPZ.GDZQ_DW
  is '归档周期单位(1:日;2:月;3:年)';
comment on column TB_GDPZ.YCSJ
  is '实际归档延迟时间(单位为：天)';
comment on column TB_GDPZ.ZXSJ
  is '归档执行时间';
comment on column TB_GDPZ.SYBZ
  is '是否启用(0:禁用;1:启用)';
comment on column TB_GDPZ.BZ
  is '备注';
comment on column TB_GDPZ.ZHZXSJ
  is '最后执行时间';
comment on column TB_GDPZ.JLCJSJ
  is '创建时间';
comment on column TB_GDPZ.JLCJYH
  is '创建用户';
comment on column TB_GDPZ.JLGXSJ
  is '更新时间';
comment on column TB_GDPZ.JLGXYH
  is '更新用户';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_GDPZ
  add constraint PK_TB_GDPZ primary key (ZJID);
insert into TB_GDPZ (ZJID, GDMC, GDBMC, SJCLM, SJCL_SJLX, SJCL_GS, GDZQ, GDZQ_DW, YCSJ, ZXSJ, SYBZ, BZ, ZHZXSJ, JLCJSJ, JLCJYH, JLGXSJ, JLGXYH)
values ('9DBD28EA09A74C67A9C285A53F2FB280', '接口访问日志归档', 'TB_INTERFACE_LOG', 'CALL_TIME', '1', null, 25, '1', 2, '13:36', '1', null, null, to_timestamp('21-10-2013 13:37:38.234000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1000000000000', to_timestamp('21-10-2013 13:37:46.796000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1000000000000');
insert into TB_GDPZ (ZJID, GDMC, GDBMC, SJCLM, SJCL_SJLX, SJCL_GS, GDZQ, GDZQ_DW, YCSJ, ZXSJ, SYBZ, BZ, ZHZXSJ, JLCJSJ, JLCJYH, JLGXSJ, JLGXYH)
values ('3F26102A3B7C41B5AD1F9DD2F529BDB0', '系统操作日志归档', 'TB_SYS_LOG', 'OPERATE_TIME', '1', null, 1, '2', 2, '01:00', '1', null, null, to_timestamp('21-10-2013 13:35:29.562000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1000000000000', to_timestamp('21-10-2013 13:35:29.562000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1000000000000');
insert into TB_GDPZ (ZJID, GDMC, GDBMC, SJCLM, SJCL_SJLX, SJCL_GS, GDZQ, GDZQ_DW, YCSJ, ZXSJ, SYBZ, BZ, ZHZXSJ, JLCJSJ, JLCJYH, JLGXSJ, JLGXYH)
values ('474FE88161E84F79A92616B3B29EC93E', '系统登录日志归档', 'TB_LOGON_LOG', 'LOGON_TIME', '1', null, 1, '3', 10, '01:00', '1', null, null, to_timestamp('21-10-2013 13:36:36.796000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1000000000000', to_timestamp('21-10-2013 13:37:55.171000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1000000000000');
commit;
exit;