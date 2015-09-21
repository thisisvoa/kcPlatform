-- Create table
create table BPM_FORM_RUN
(
  ID              VARCHAR2(32) not null,
  FORM_DEF_ID     VARCHAR2(32),
  FORM_DEF_KEY    VARCHAR2(64),
  ACT_INSTANCE_ID VARCHAR2(64),
  ACT_DEF_ID      VARCHAR2(64),
  ACT_NODE_ID     VARCHAR2(64),
  RUN_ID          VARCHAR2(32),
  SET_TYPE        CHAR(1),
  FORM_TYPE       CHAR(1),
  FORM_URL        VARCHAR2(256)
);
-- Add comments to the table 
comment on table BPM_FORM_RUN
  is '运行表单';
-- Add comments to the columns 
comment on column BPM_FORM_RUN.ID
  is '主键';
comment on column BPM_FORM_RUN.FORM_DEF_ID
  is '表单定义ID';
comment on column BPM_FORM_RUN.FORM_DEF_KEY
  is '表单定义key';
comment on column BPM_FORM_RUN.ACT_INSTANCE_ID
  is 'ACT流程实例ID';
comment on column BPM_FORM_RUN.ACT_DEF_ID
  is 'ACT流程定义ID';
comment on column BPM_FORM_RUN.ACT_NODE_ID
  is '流程节点id';
comment on column BPM_FORM_RUN.RUN_ID
  is '流程运行ID';
comment on column BPM_FORM_RUN.SET_TYPE
  is '表单类型0,任务节点 1,开始表单 2,全局表单';
comment on column BPM_FORM_RUN.FORM_TYPE
  is '表单类型';
comment on column BPM_FORM_RUN.FORM_URL
  is '表单URL';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_FORM_RUN
  add constraint PK_BPM_FORM_RUN primary key (ID);
exit;