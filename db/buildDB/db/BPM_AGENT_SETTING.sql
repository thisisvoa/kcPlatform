-- Create table
create table BPM_AGENT_SETTING
(
  ID          VARCHAR2(32) not null,
  AUTH_ID     VARCHAR2(32),
  AUTH_NAME   VARCHAR2(128),
  CREATE_TIME TIMESTAMP(6),
  START_DATE  TIMESTAMP(6),
  END_DATE    TIMESTAMP(6),
  ENABLED     NUMBER,
  AUTH_TYPE   NUMBER,
  AGENT_ID    VARCHAR2(32),
  AGENT       VARCHAR2(128),
  FLOW_KEY    VARCHAR2(64),
  FLOW_NAME   VARCHAR2(64)
);
-- Add comments to the columns 
comment on column BPM_AGENT_SETTING.ID
  is 'ID';
comment on column BPM_AGENT_SETTING.AUTH_ID
  is '代理人ID';
comment on column BPM_AGENT_SETTING.AUTH_NAME
  is '代理人名称';
comment on column BPM_AGENT_SETTING.CREATE_TIME
  is '创建时间';
comment on column BPM_AGENT_SETTING.START_DATE
  is '代理开始时间';
comment on column BPM_AGENT_SETTING.END_DATE
  is '代理结束时间';
comment on column BPM_AGENT_SETTING.ENABLED
  is '是否启用';
comment on column BPM_AGENT_SETTING.AUTH_TYPE
  is '代理类型';
comment on column BPM_AGENT_SETTING.AGENT_ID
  is '被代理人ID';
comment on column BPM_AGENT_SETTING.AGENT
  is '被代理人名称';
comment on column BPM_AGENT_SETTING.FLOW_KEY
  is '流程KEY';
comment on column BPM_AGENT_SETTING.FLOW_NAME
  is '流程名称';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_AGENT_SETTING
  add constraint PK_BPM_AGENT_SETTING primary key (ID);
exit;