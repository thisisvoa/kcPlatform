-- Create table
create table BPM_TKSIGN_DATA
(
  DATA_ID        VARCHAR2(64) not null,
  ACT_DEF_ID     VARCHAR2(64),
  ACT_INST_ID    VARCHAR2(64),
  NODE_NAME      VARCHAR2(128),
  NODE_ID        VARCHAR2(128),
  TASK_ID        VARCHAR2(64),
  VOTE_USER_ID   VARCHAR2(32),
  VOTE_USER_NAME VARCHAR2(128),
  VOTE_TIME      TIMESTAMP(6),
  IS_AGREE       INTEGER,
  CONTENT        VARCHAR2(512),
  SIGN_NUMS      NUMBER,
  IS_COMPLETED   NUMBER,
  BATCH          NUMBER
);
-- Add comments to the table 
comment on table BPM_TKSIGN_DATA
  is '会签任务执行结果';
-- Add comments to the columns 
comment on column BPM_TKSIGN_DATA.DATA_ID
  is 'ID';
comment on column BPM_TKSIGN_DATA.ACT_DEF_ID
  is 'Activiti流程定义ID';
comment on column BPM_TKSIGN_DATA.ACT_INST_ID
  is 'Activiti流程实例ID';
comment on column BPM_TKSIGN_DATA.NODE_NAME
  is '流程节点名称';
comment on column BPM_TKSIGN_DATA.NODE_ID
  is '流程节点ID';
comment on column BPM_TKSIGN_DATA.TASK_ID
  is '会签任务Id';
comment on column BPM_TKSIGN_DATA.VOTE_USER_ID
  is '投票人ID';
comment on column BPM_TKSIGN_DATA.VOTE_USER_NAME
  is '投票人名';
comment on column BPM_TKSIGN_DATA.VOTE_TIME
  is '投票时间';
comment on column BPM_TKSIGN_DATA.IS_AGREE
  is '是否同意： 0=弃权票 1=同意2=拒绝，跟task_sign中的decideType是一样';
comment on column BPM_TKSIGN_DATA.CONTENT
  is '投票意见内容';
comment on column BPM_TKSIGN_DATA.SIGN_NUMS
  is '投票次数';
comment on column BPM_TKSIGN_DATA.IS_COMPLETED
  is '是否完成1=完成 0=未完成
       ';
comment on column BPM_TKSIGN_DATA.BATCH
  is '是否批量1=是 0=否';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_TKSIGN_DATA
  add constraint PK_BPM_TKSIGN_DATA primary key (DATA_ID);
exit;