-- Create table
create table BPM_FORM_TABLE
(
  TABLE_ID        VARCHAR2(32) not null,
  TABLE_NAME      VARCHAR2(64),
  TABLE_DESC      VARCHAR2(128),
  IS_MAIN         CHAR(1),
  MAIN_TABLE_ID   VARCHAR2(32),
  VERSION_NO      NUMBER,
  IS_DEFAULT      CHAR(1),
  IS_PUBLISHED    CHAR(1),
  PUBLISHED_BY    VARCHAR2(32),
  PUBLISH_TIME    TIMESTAMP(6),
  IS_EXTERNAL     CHAR(1),
  DS_ALIAS        VARCHAR2(64),
  DS_NAME         VARCHAR2(64),
  RELATION        VARCHAR2(512),
  KEY_TYPE        VARCHAR2(2),
  KEY_VALUE       VARCHAR2(32),
  PK_FIELD        VARCHAR2(32),
  LIST_TEMPLATE   CLOB,
  DETAIL_TEMPLATE CLOB
);
-- Add comments to the columns 
comment on column BPM_FORM_TABLE.TABLE_NAME
  is '表名';
comment on column BPM_FORM_TABLE.TABLE_DESC
  is '表描述';
comment on column BPM_FORM_TABLE.IS_MAIN
  is '是否主表';
comment on column BPM_FORM_TABLE.MAIN_TABLE_ID
  is '主表ID';
comment on column BPM_FORM_TABLE.VERSION_NO
  is '版本号';
comment on column BPM_FORM_TABLE.IS_EXTERNAL
  is '是否外部数据源';
comment on column BPM_FORM_TABLE.DS_ALIAS
  is '数据源别名';
comment on column BPM_FORM_TABLE.DS_NAME
  is '数据源名称';
comment on column BPM_FORM_TABLE.RELATION
  is '字段关联关系';
comment on column BPM_FORM_TABLE.KEY_TYPE
  is '键类型';
comment on column BPM_FORM_TABLE.KEY_VALUE
  is '键值';
comment on column BPM_FORM_TABLE.PK_FIELD
  is '主键字段';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_FORM_TABLE
  add constraint PK_BPM_FORM_TABLE primary key (TABLE_ID);
exit;