-- Create table
create table BPM_AGENT_DEF
(
  ID         VARCHAR2(32) not null,
  SETTING_ID VARCHAR2(32),
  FLOW_KEY   VARCHAR2(64),
  FLOW_NAME  VARCHAR2(64)
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_AGENT_DEF
  add constraint PK_BPM_AGENT_DEF primary key (ID);
exit;