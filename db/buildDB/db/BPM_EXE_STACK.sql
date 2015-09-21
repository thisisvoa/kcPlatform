-- Create table
create table BPM_EXE_STACK
(
  STACK_ID      VARCHAR2(32) not null,
  ACT_DEF_ID    VARCHAR2(64),
  NODE_ID       VARCHAR2(128),
  NODE_NAME     VARCHAR2(256),
  START_TIME    TIMESTAMP(6),
  END_TIME      TIMESTAMP(6),
  ASSIGNEES     VARCHAR2(1024),
  IS_MULTI_TASK INTEGER,
  PARENT_ID     VARCHAR2(32),
  ACT_INST_ID   VARCHAR2(64),
  TASK_IDS      VARCHAR2(4000),
  NODE_PATH     VARCHAR2(4000),
  DEPTH         NUMBER,
  TASK_TOKEN    VARCHAR2(64)
);
-- Add comments to the table 
comment on table BPM_EXE_STACK
  is '任务堆栈';
-- Add comments to the columns 
comment on column BPM_EXE_STACK.STACK_ID
  is 'ID';
comment on column BPM_EXE_STACK.NODE_NAME
  is '节点名称';
comment on column BPM_EXE_STACK.START_TIME
  is '开始时间';
comment on column BPM_EXE_STACK.END_TIME
  is '结束时间';
comment on column BPM_EXE_STACK.ASSIGNEES
  is '执行人IDS，如1,2,3';
comment on column BPM_EXE_STACK.IS_MULTI_TASK
  is '是否多实例1=是 0=否';
comment on column BPM_EXE_STACK.PARENT_ID
  is '父ID';
comment on column BPM_EXE_STACK.ACT_INST_ID
  is 'ACT流程实例ID';
comment on column BPM_EXE_STACK.NODE_PATH
  is '节点路径格式如：0.1.2. 其中2则为本行记录的主键值';
comment on column BPM_EXE_STACK.DEPTH
  is '节点层';
comment on column BPM_EXE_STACK.TASK_TOKEN
  is '针对分发任务时，携带的令牌，方便查找其父任务堆栈';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_EXE_STACK
  add constraint PK_BPM_EXE_STACK primary key (STACK_ID);
exit;