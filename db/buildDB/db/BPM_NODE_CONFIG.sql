-- Create table
create table BPM_NODE_CONFIG
(
  CONFIG_ID      VARCHAR2(64) not null,
  DEF_ID         VARCHAR2(64),
  NODE_NAME      VARCHAR2(128),
  NODE_ID        VARCHAR2(64),
  FORM_TYPE      CHAR(1),
  FORM_URL       VARCHAR2(256),
  FORM_DEF_NAME  VARCHAR2(64),
  FORM_KEY       VARCHAR2(32),
  NODE_TYPE      VARCHAR2(2),
  JOIN_TASK_KEY  VARCHAR2(128),
  JOIN_TASK_NAME VARCHAR2(128),
  BEFORE_HANDLER VARCHAR2(128),
  AFTER_HANDLER  VARCHAR2(128),
  ALLOW_BACK     CHAR(1),
  SET_TYPE       CHAR(1),
  ACT_DEF_ID     VARCHAR2(64),
  SN             NUMBER
);
-- Add comments to the table 
comment on table BPM_NODE_CONFIG
  is '流程节点设置';
-- Add comments to the columns 
comment on column BPM_NODE_CONFIG.DEF_ID
  is '流程定义ID';
comment on column BPM_NODE_CONFIG.NODE_NAME
  is '节点名称';
comment on column BPM_NODE_CONFIG.NODE_ID
  is '节点ID';
comment on column BPM_NODE_CONFIG.FORM_TYPE
  is '表单类型';
comment on column BPM_NODE_CONFIG.FORM_URL
  is '表单URL';
comment on column BPM_NODE_CONFIG.FORM_DEF_NAME
  is '表单名称';
comment on column BPM_NODE_CONFIG.FORM_KEY
  is '表单KEY';
comment on column BPM_NODE_CONFIG.BEFORE_HANDLER
  is '前置处理器';
comment on column BPM_NODE_CONFIG.AFTER_HANDLER
  is '后置处理器';
comment on column BPM_NODE_CONFIG.SET_TYPE
  is '设置类型 0.任务节点 1.开始表单 2.默认表单';
comment on column BPM_NODE_CONFIG.ACT_DEF_ID
  is 'Activiti流程定义ID';
comment on column BPM_NODE_CONFIG.SN
  is '排序号';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_NODE_CONFIG
  add constraint PK_BPM_NODE_CONFIG primary key (CONFIG_ID);
exit;