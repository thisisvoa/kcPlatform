-- Create table
create table BPM_TASK_OPINION
(
  OPINION_ID      VARCHAR2(32) not null,
  ACT_DEF_ID      VARCHAR2(64),
  TASK_NAME       VARCHAR2(256),
  TASK_KEY        VARCHAR2(64),
  TASK_ID         VARCHAR2(64),
  TASK_TOKEN      VARCHAR2(64),
  ACT_INST_ID     VARCHAR2(64),
  START_TIME      TIMESTAMP(6),
  END_TIME        TIMESTAMP(6),
  DUR_TIME        NUMBER,
  EXE_USER_ID     VARCHAR2(32),
  EXE_USER_NAME   VARCHAR2(128),
  OPINION         VARCHAR2(512),
  CHECK_STATUS    INTEGER,
  FORM_DEF_ID     VARCHAR2(32),
  FIELD_NAME      VARCHAR2(64),
  SUPER_EXECUTION VARCHAR2(64)
);
-- Add comments to the table 
comment on table BPM_TASK_OPINION
  is '节点审批意见';
-- Add comments to the columns 
comment on column BPM_TASK_OPINION.OPINION_ID
  is 'ID';
comment on column BPM_TASK_OPINION.ACT_DEF_ID
  is 'ACT流程定义ID';
comment on column BPM_TASK_OPINION.TASK_NAME
  is '任务名称';
comment on column BPM_TASK_OPINION.TASK_KEY
  is '任务KEY';
comment on column BPM_TASK_OPINION.TASK_ID
  is '任务ID';
comment on column BPM_TASK_OPINION.TASK_TOKEN
  is '任务TOKEN';
comment on column BPM_TASK_OPINION.ACT_INST_ID
  is '流程实例ID';
comment on column BPM_TASK_OPINION.START_TIME
  is '执行开始时间';
comment on column BPM_TASK_OPINION.END_TIME
  is '结束时间';
comment on column BPM_TASK_OPINION.DUR_TIME
  is '持续时间';
comment on column BPM_TASK_OPINION.EXE_USER_ID
  is '执行人ID';
comment on column BPM_TASK_OPINION.EXE_USER_NAME
  is '执行人名';
comment on column BPM_TASK_OPINION.OPINION
  is '审批意见';
comment on column BPM_TASK_OPINION.CHECK_STATUS
  is '审批状态1=同意 2=反对3=驳回 0=弃权4=追回';
comment on column BPM_TASK_OPINION.FORM_DEF_ID
  is '表单定义ID';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_TASK_OPINION
  add constraint PK_BPM_TASK_OPINION primary key (OPINION_ID);
exit;