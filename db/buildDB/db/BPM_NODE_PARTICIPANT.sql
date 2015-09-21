-- Create table
create table BPM_NODE_PARTICIPANT
(
  PARTICIPANT_ID   VARCHAR2(64) not null,
  CONFIG_ID        VARCHAR2(64),
  DEF_ID           VARCHAR2(64),
  ACT_DEF_ID       VARCHAR2(64),
  NODE_ID          VARCHAR2(64),
  PARTICIPANT_TYPE VARCHAR2(2),
  PARTICIPAN       VARCHAR2(4000),
  PARTICIPAN_DESC  VARCHAR2(4000),
  COMPUTE_TYPE     CHAR(1),
  SN               NUMBER,
  EXTRACT_USER     NUMBER
);
-- Add comments to the table 
comment on table BPM_NODE_PARTICIPANT
  is '任务参与者';
-- Add comments to the columns 
comment on column BPM_NODE_PARTICIPANT.PARTICIPANT_TYPE
  is '参与者类型';
comment on column BPM_NODE_PARTICIPANT.PARTICIPAN
  is '参与者';
comment on column BPM_NODE_PARTICIPANT.PARTICIPAN_DESC
  is '参与者描述';
comment on column BPM_NODE_PARTICIPANT.COMPUTE_TYPE
  is '运算类型 0:或 1:与 2:排除';
comment on column BPM_NODE_PARTICIPANT.SN
  is '排序号';
comment on column BPM_NODE_PARTICIPANT.EXTRACT_USER
  is '抽取用户(0:抽取 1:不抽取)';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_NODE_PARTICIPANT
  add constraint PK_BPM_NODE_PARTICIPANT primary key (PARTICIPANT_ID);
exit;