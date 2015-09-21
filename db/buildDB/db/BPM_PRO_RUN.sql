-- Create table
create table BPM_PRO_RUN
(
  RUN_ID               VARCHAR2(32) not null,
  DEF_ID               VARCHAR2(32),
  PROCESS_NAME         VARCHAR2(256),
  SUBJECT              VARCHAR2(256),
  CREATOR_ID           VARCHAR2(32),
  CREATOR              VARCHAR2(128),
  CREATE_TIME          TIMESTAMP(6),
  BUS_DESCP            VARCHAR2(3000),
  STATUS               INTEGER,
  ACT_INST_ID          VARCHAR2(64),
  ACT_DEF_ID           VARCHAR2(64),
  BUSINESS_KEY         VARCHAR2(256),
  BUSINESS_URL         VARCHAR2(256),
  END_TIME             TIMESTAMP(6),
  DURATION             NUMBER,
  PK_NAME              VARCHAR2(64),
  TABLE_NAME           VARCHAR2(64),
  PARENT_ID            VARCHAR2(32),
  START_ORG_ID         VARCHAR2(32),
  START_ORG_NAME       VARCHAR2(64),
  FORM_DEF_ID          VARCHAR2(32),
  TYPE_ID              VARCHAR2(32),
  FLOW_KEY             VARCHAR2(64),
  FORM_TYPE            CHAR(1),
  FORM_KEY_URL         VARCHAR2(64),
  LAST_SUBMIT_DURATION NUMBER,
  IS_FORMAL            CHAR(1)
);
-- Add comments to the table 
comment on table BPM_PRO_RUN
  is '流程实例';
-- Add comments to the columns 
comment on column BPM_PRO_RUN.RUN_ID
  is 'ID';
comment on column BPM_PRO_RUN.DEF_ID
  is '流程定义ID';
comment on column BPM_PRO_RUN.PROCESS_NAME
  is '流程名';
comment on column BPM_PRO_RUN.SUBJECT
  is '标题';
comment on column BPM_PRO_RUN.CREATOR_ID
  is '创建人ID';
comment on column BPM_PRO_RUN.CREATOR
  is '创建人';
comment on column BPM_PRO_RUN.CREATE_TIME
  is '创建时间';
comment on column BPM_PRO_RUN.BUS_DESCP
  is '业务表单简述';
comment on column BPM_PRO_RUN.STATUS
  is '状态:0=草稿 1=启动 2=正在运行 3=正常结束';
comment on column BPM_PRO_RUN.ACT_INST_ID
  is 'ACT流程实例ID';
comment on column BPM_PRO_RUN.ACT_DEF_ID
  is 'ACT流程定义ID';
comment on column BPM_PRO_RUN.END_TIME
  is '结束时间';
comment on column BPM_PRO_RUN.DURATION
  is '持续时间';
comment on column BPM_PRO_RUN.PK_NAME
  is '主键名';
comment on column BPM_PRO_RUN.TABLE_NAME
  is '表名';
comment on column BPM_PRO_RUN.PARENT_ID
  is '父流程实例ID';
comment on column BPM_PRO_RUN.START_ORG_ID
  is '启动单位ID';
comment on column BPM_PRO_RUN.START_ORG_NAME
  is '启动单位名称';
comment on column BPM_PRO_RUN.TYPE_ID
  is '流程分组';
comment on column BPM_PRO_RUN.LAST_SUBMIT_DURATION
  is '最后提交时长';
comment on column BPM_PRO_RUN.IS_FORMAL
  is '是否正式';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_PRO_RUN
  add constraint PK_BPM_PRO_RUN primary key (RUN_ID);
exit;