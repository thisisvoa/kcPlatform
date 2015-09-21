-- Create table
create table BPM_FORM_DEF
(
  FORM_DEF_ID  VARCHAR2(32) not null,
  CATALOG_ID   VARCHAR2(32),
  FORM_KEY     VARCHAR2(64),
  SUBJECT      VARCHAR2(128),
  FORM_DESC    VARCHAR2(256),
  TABLE_ID     VARCHAR2(32),
  TEMPLATE     CLOB,
  HTML         CLOB,
  VERSION_NO   NUMBER,
  IS_DEFAULT   CHAR(1),
  IS_PUBLISHED CHAR(1),
  PUBLISHED_BY VARCHAR2(32),
  PUBLISH_TIME TIMESTAMP(6),
  CREATE_USER  VARCHAR2(32),
  CREATE_TIME  TIMESTAMP(6),
  MODIFY_USER  VARCHAR2(32),
  MODIFY_TIME  TIMESTAMP(6)
);
-- Add comments to the table 
comment on table BPM_FORM_DEF
  is '表单定义';
-- Add comments to the columns 
comment on column BPM_FORM_DEF.FORM_DEF_ID
  is 'ID';
comment on column BPM_FORM_DEF.CATALOG_ID
  is '表单分类';
comment on column BPM_FORM_DEF.FORM_KEY
  is '表单key';
comment on column BPM_FORM_DEF.SUBJECT
  is '表单标题';
comment on column BPM_FORM_DEF.FORM_DESC
  is '描述';
comment on column BPM_FORM_DEF.TABLE_ID
  is '主表名';
comment on column BPM_FORM_DEF.TEMPLATE
  is 'freeMaker模版';
comment on column BPM_FORM_DEF.HTML
  is '定义html';
comment on column BPM_FORM_DEF.VERSION_NO
  is '版本号';
comment on column BPM_FORM_DEF.IS_DEFAULT
  is '是否默认使用版本';
comment on column BPM_FORM_DEF.IS_PUBLISHED
  is '是否发布';
comment on column BPM_FORM_DEF.PUBLISHED_BY
  is '发布用户';
comment on column BPM_FORM_DEF.PUBLISH_TIME
  is '发布时间';
comment on column BPM_FORM_DEF.CREATE_USER
  is '创建用户';
comment on column BPM_FORM_DEF.CREATE_TIME
  is '创建时间';
comment on column BPM_FORM_DEF.MODIFY_USER
  is '修改用户';
comment on column BPM_FORM_DEF.MODIFY_TIME
  is '修改时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_FORM_DEF
  add constraint PK_BPM_FORM_DEF primary key (FORM_DEF_ID);
exit;