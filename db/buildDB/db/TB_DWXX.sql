-- Create table
create table TB_DWXX
(
  ZJID      VARCHAR2(36) not null,
  DWBH      VARCHAR2(30) not null,
  DWMC      VARCHAR2(200) not null,
  DWJC      VARCHAR2(200),
  DWDH      VARCHAR2(20),
  DWYX      VARCHAR2(20),
  DWFZR     VARCHAR2(20),
  SJDW_ZJID VARCHAR2(30),
  DWLX      CHAR(1) not null,
  DWJB      CHAR(1) not null,
  BZ        VARCHAR2(1000),
  CJYH      VARCHAR2(30),
  GXYH      VARCHAR2(30),
  JLXZSJ    VARCHAR2(14),
  JLXGSJ    VARCHAR2(14),
  JLSCSJ    VARCHAR2(14),
  JLYXZT    VARCHAR2(2) not null
);
-- Add comments to the table 
comment on table TB_DWXX
  is '单位信息表';
-- Add comments to the columns 
comment on column TB_DWXX.ZJID
  is '主键ID';
comment on column TB_DWXX.DWBH
  is '单位编号';
comment on column TB_DWXX.DWMC
  is '单位名称';
comment on column TB_DWXX.DWJC
  is '单位简称';
comment on column TB_DWXX.DWDH
  is '单位电话';
comment on column TB_DWXX.DWYX
  is '单位邮箱';
comment on column TB_DWXX.DWFZR
  is '单位负责人';
comment on column TB_DWXX.SJDW_ZJID
  is '上级单位主键ID';
comment on column TB_DWXX.DWLX
  is '单位类型 预留字段';
comment on column TB_DWXX.DWJB
  is '单位级别 预留字段';
comment on column TB_DWXX.BZ
  is '备注';
comment on column TB_DWXX.CJYH
  is '创建用户';
comment on column TB_DWXX.GXYH
  is '更新用户';
comment on column TB_DWXX.JLXZSJ
  is '记录新增时间';
comment on column TB_DWXX.JLXGSJ
  is '记录更新时间';
comment on column TB_DWXX.JLSCSJ
  is '记录删除时间';
comment on column TB_DWXX.JLYXZT
  is '记录有效标志 -1-已归档；0-无效（已注销）；1-有效';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_DWXX
  add constraint PK_TB_DWXX primary key (ZJID);
insert into TB_DWXX (ZJID, DWBH, DWMC, DWJC, DWDH, DWYX, DWFZR, SJDW_ZJID, DWLX, DWJB, BZ, CJYH, GXYH, JLXZSJ, JLXGSJ, JLSCSJ, JLYXZT)
values ('520000000000', '520000000000', '公安厅', '公安厅', '1', null, '1', '0', '1', '1', null, null, null, '20121020163411', '20121020163411', null, '1');
commit;
exit;