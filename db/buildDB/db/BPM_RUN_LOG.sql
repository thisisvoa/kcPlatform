-- Create table
create table BPM_RUN_LOG
(
  ID              VARCHAR2(32) not null,
  USER_ID         VARCHAR2(32),
  USER_NAME       VARCHAR2(64),
  CREATE_TIME     TIMESTAMP(6),
  OPERATOR_TYPE   NUMBER,
  MEMO            VARCHAR2(512),
  RUN_ID          VARCHAR2(32),
  PROCESS_SUBJECT VARCHAR2(256)
);
-- Add comments to the columns 
comment on column BPM_RUN_LOG.USER_ID
  is '用户ID';
comment on column BPM_RUN_LOG.USER_NAME
  is '用户名称';
comment on column BPM_RUN_LOG.CREATE_TIME
  is '创建时间';
comment on column BPM_RUN_LOG.OPERATOR_TYPE
  is '操作类型';
comment on column BPM_RUN_LOG.MEMO
  is '备注';
comment on column BPM_RUN_LOG.RUN_ID
  is '流程实例ID';
comment on column BPM_RUN_LOG.PROCESS_SUBJECT
  is '流程实例标题';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_RUN_LOG
  add constraint PK_BPM_RUN_LOG primary key (ID);
exit;