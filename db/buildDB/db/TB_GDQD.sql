-- Create table
create table TB_GDQD
(
  ZJID      VARCHAR2(32) not null,
  GDPZ_ZJID VARCHAR2(32) not null,
  GDBMC     VARCHAR2(64) not null,
  GDHBMC    VARCHAR2(64) not null,
  GDSJ_KSSJ DATE,
  GDSJ_JSSJ DATE not null,
  GDJLS     NUMBER not null,
  JLCJSJ    TIMESTAMP(6)
);
-- Add comments to the table 
comment on table TB_GDQD
  is '归档清单表';
-- Add comments to the columns 
comment on column TB_GDQD.ZJID
  is '清单ID';
comment on column TB_GDQD.GDPZ_ZJID
  is '归档配置ID';
comment on column TB_GDQD.GDBMC
  is '归档表名称';
comment on column TB_GDQD.GDHBMC
  is '归档后的表名称';
comment on column TB_GDQD.GDSJ_KSSJ
  is '归档数据开始时间';
comment on column TB_GDQD.GDSJ_JSSJ
  is '归档数据结束时间';
comment on column TB_GDQD.GDJLS
  is '归档记录数';
comment on column TB_GDQD.JLCJSJ
  is '清单创建时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_GDQD
  add constraint PK_TB_GDQD primary key (ZJID);
commit;
exit;