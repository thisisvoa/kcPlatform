-- Create table
create table BPM_TASK_READ
(
  ID          VARCHAR2(32),
  ACT_INST_ID VARCHAR2(64),
  TASK_ID     VARCHAR2(64),
  USER_ID     VARCHAR2(32),
  USER_NAME   VARCHAR2(64),
  CREATE_TIME TIMESTAMP(6)
);
-- Add comments to the table 
comment on table BPM_TASK_READ
  is '已读任务列表';
-- Add comments to the columns 
comment on column BPM_TASK_READ.ACT_INST_ID
  is '流程实例ID';
comment on column BPM_TASK_READ.TASK_ID
  is '任务ID';
comment on column BPM_TASK_READ.USER_ID
  is '用户ID';
comment on column BPM_TASK_READ.USER_NAME
  is '用户名称';
comment on column BPM_TASK_READ.CREATE_TIME
  is '创建时间';
exit;