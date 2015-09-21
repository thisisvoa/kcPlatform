-- Create table
create table BPM_TASK_FORK
(
  TASK_FORK_ID   VARCHAR2(32),
  ACT_INST_ID    VARCHAR2(64),
  FORK_TASK_NAME VARCHAR2(256),
  FORK_TASK_KEY  VARCHAR2(256),
  FORK_SN        NUMBER,
  FORK_COUNT     NUMBER,
  FININSH_COUNT  NUMBER,
  FORK_TIME      TIMESTAMP(6),
  JOIN_TASK_NAME VARCHAR2(256),
  JOIN_TASK_KEY  VARCHAR2(256),
  FORK_TOKENS    VARCHAR2(512),
  FORK_TOKEN_PRE VARCHAR2(64)
);
-- Add comments to the table 
comment on table BPM_TASK_FORK
  is '任务分发';
-- Add comments to the columns 
comment on column BPM_TASK_FORK.TASK_FORK_ID
  is 'ID';
comment on column BPM_TASK_FORK.ACT_INST_ID
  is '流程实例ID';
comment on column BPM_TASK_FORK.FORK_TASK_NAME
  is '分发任务名称';
comment on column BPM_TASK_FORK.FORK_TASK_KEY
  is '分发任务Key';
comment on column BPM_TASK_FORK.FORK_SN
  is '分发序号';
comment on column BPM_TASK_FORK.FORK_COUNT
  is '分发个数';
comment on column BPM_TASK_FORK.FININSH_COUNT
  is '完成个数';
comment on column BPM_TASK_FORK.FORK_TIME
  is '分发时间';
comment on column BPM_TASK_FORK.JOIN_TASK_NAME
  is '汇总任务Key';
comment on column BPM_TASK_FORK.JOIN_TASK_KEY
  is '汇总任务名称';
comment on column BPM_TASK_FORK.FORK_TOKENS
  is '分发令牌号 格式如：T_1_1,T_1_2,T_1_3, 或 T_1,T_2,T_3,';
comment on column BPM_TASK_FORK.FORK_TOKEN_PRE
  is '分发令牌前缀 格式为T_ 或格式T_1 或T_1_2等';
exit;