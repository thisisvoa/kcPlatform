-- Create table
create table TB_SYS_LOG
(
  LOG_ID          VARCHAR2(32) not null,
  ORG_NO          VARCHAR2(30),
  ORG_NAME        VARCHAR2(200),
  ID_CARD         VARCHAR2(18),
  POLICE_ID       VARCHAR2(20),
  USER_NAME       VARCHAR2(30),
  LOGIN_ID        VARCHAR2(30),
  OPERATE_TIME    TIMESTAMP(6) not null,
  TERMINAL_ID     VARCHAR2(64),
  OPERATE_CONTENT VARCHAR2(4000),
  OPERATE_RESULT  VARCHAR2(200),
  OPERATE_DESC    VARCHAR2(4000),
  MODULE_NAME     VARCHAR2(64),
  SYS_NAME        VARCHAR2(64),
  LOG_TYPE        VARCHAR2(2) not null,
  ORG_ID          VARCHAR2(30),
  USER_ID         VARCHAR2(30)
);
-- Create/Recreate indexes 
create bitmap index IDX_TB_SYS_LOG_LOG_TYPE on TB_SYS_LOG (LOG_TYPE);
create index IDX_TB_SYS_LOG_OPERATE_TIME on TB_SYS_LOG (OPERATE_TIME DESC);
create bitmap index IDX_TB_SYS_LOG_POLICE_ID on TB_SYS_LOG (POLICE_ID);
create bitmap index IDX_TB_SYS_LOG_TERMINAL_ID on TB_SYS_LOG (TERMINAL_ID);
commit;
exit;