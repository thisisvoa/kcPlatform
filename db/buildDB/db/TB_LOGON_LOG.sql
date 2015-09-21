-- Create table
create table TB_LOGON_LOG
(
  LOG_ID       VARCHAR2(32) not null,
  ORG_NO       VARCHAR2(30) not null,
  ORG_NAME     VARCHAR2(200) not null,
  ID_CARD      VARCHAR2(18),
  POLICE_ID    VARCHAR2(20),
  USER_NAME    VARCHAR2(30) not null,
  LOGIN_ID     VARCHAR2(30) not null,
  LOGON_TIME   TIMESTAMP(6),
  LOGOUT_TIME  TIMESTAMP(6),
  TERMINAL_ID  VARCHAR2(64) not null,
  LOGON_RESULT VARCHAR2(1) not null,
  SESSION_ID   VARCHAR2(128),
  ORG_ID       VARCHAR2(30) not null,
  USER_ID      VARCHAR2(30) not null
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_LOGON_LOG
  add constraint PK_TB_LOGON_LOG primary key (LOG_ID);
commit;
exit;