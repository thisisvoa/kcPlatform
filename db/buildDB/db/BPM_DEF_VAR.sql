-- Create table
create table BPM_DEF_VAR
(
  VAR_ID        VARCHAR2(32) not null,
  DEF_ID        VARCHAR2(32),
  VAR_NAME      VARCHAR2(128),
  VAR_KEY       VARCHAR2(128),
  VAR_DATA_TYPE VARCHAR2(64),
  DEF_VALUE     VARCHAR2(256),
  NODE_NAME     VARCHAR2(256),
  NODE_ID       VARCHAR2(256),
  ACT_DEPLOY_ID VARCHAR2(64),
  VAR_SCOPE     VARCHAR2(64)
);
-- Add comments to the table 
comment on table BPM_DEF_VAR
  is '流程变量';
-- Add comments to the columns 
comment on column BPM_DEF_VAR.VAR_ID
  is '变量ID';
comment on column BPM_DEF_VAR.DEF_ID
  is '流程定义ID';
comment on column BPM_DEF_VAR.VAR_NAME
  is '变量名称';
comment on column BPM_DEF_VAR.VAR_KEY
  is '变量Key';
comment on column BPM_DEF_VAR.VAR_DATA_TYPE
  is '变量数据类型:string date number';
comment on column BPM_DEF_VAR.DEF_VALUE
  is '默认值';
comment on column BPM_DEF_VAR.NODE_NAME
  is '节点名称';
comment on column BPM_DEF_VAR.NODE_ID
  is '节点ID';
comment on column BPM_DEF_VAR.VAR_SCOPE
  is '作用域:全局内=global  任务内=task';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_DEF_VAR
  add constraint PK_BPM_DEF_VAR primary key (VAR_ID);
exit;