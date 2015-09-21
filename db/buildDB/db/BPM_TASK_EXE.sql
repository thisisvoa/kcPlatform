-- Create table
create table BPM_TASK_EXE
(
  ID            VARCHAR2(32) not null,
  TASK_ID       VARCHAR2(64),
  ASSIGNEE_ID   VARCHAR2(32),
  ASSIGNEE_NAME VARCHAR2(64),
  OWNER_ID      VARCHAR2(32),
  OWNER_NAME    VARCHAR2(64),
  SUBJECT       VARCHAR2(256),
  STATUS        NUMBER,
  MEMO          VARCHAR2(4000),
  CREATE_TIME   TIMESTAMP(6),
  ACT_INST_ID   VARCHAR2(64),
  TASK_NAME     VARCHAR2(256),
  TASK_DEF_KEY  VARCHAR2(64),
  EXE_TIME      TIMESTAMP(6),
  EXE_USER_ID   VARCHAR2(32),
  EXE_USER_NAME VARCHAR2(64),
  ASSIGN_TYPE   NUMBER,
  RUN_ID        VARCHAR2(32),
  TYPE_ID       VARCHAR2(32)
);
-- Add comments to the table 
comment on table BPM_TASK_EXE
  is '转办(代理)';
-- Add comments to the columns 
comment on column BPM_TASK_EXE.ID
  is 'ID';
comment on column BPM_TASK_EXE.TASK_ID
  is '任务ID';
comment on column BPM_TASK_EXE.ASSIGNEE_ID
  is '转办(代理)人ID';
comment on column BPM_TASK_EXE.ASSIGNEE_NAME
  is '转办(代理)人名称';
comment on column BPM_TASK_EXE.OWNER_ID
  is '任务拥有者ID';
comment on column BPM_TASK_EXE.OWNER_NAME
  is '任务拥有者名称';
comment on column BPM_TASK_EXE.SUBJECT
  is '标题';
comment on column BPM_TASK_EXE.STATUS
  is '状态(0:初始状态;1:完成任务;2:取消;3:其他)';
comment on column BPM_TASK_EXE.MEMO
  is '备注';
comment on column BPM_TASK_EXE.CREATE_TIME
  is '创建时间';
comment on column BPM_TASK_EXE.ACT_INST_ID
  is 'ACT流程实例ID';
comment on column BPM_TASK_EXE.TASK_NAME
  is '任务名称';
comment on column BPM_TASK_EXE.TASK_DEF_KEY
  is '任务KEY';
comment on column BPM_TASK_EXE.EXE_TIME
  is '执行时间';
comment on column BPM_TASK_EXE.EXE_USER_ID
  is '执行用户ID';
comment on column BPM_TASK_EXE.EXE_USER_NAME
  is '执行用户名称';
comment on column BPM_TASK_EXE.ASSIGN_TYPE
  is '待办类型(1:代理 2:转办)';
comment on column BPM_TASK_EXE.RUN_ID
  is '流程实例ID';
comment on column BPM_TASK_EXE.TYPE_ID
  is '流程分类ID';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_TASK_EXE
  add constraint PK_BPM_TASK_EXE primary key (ID);
exit;