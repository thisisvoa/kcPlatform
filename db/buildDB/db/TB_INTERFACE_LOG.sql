-- Create table
create table TB_INTERFACE_LOG
(
  LOG_ID         VARCHAR2(32) not null,
  CALL_TIME      TIMESTAMP(6) not null,
  CALLER_NAME    VARCHAR2(64),
  TERMINAL_ID    VARCHAR2(64),
  RESULT_COUNT   NUMBER,
  INTERFACE_DESC VARCHAR2(200),
  ORG_NO         VARCHAR2(30),
  ORG_NAME       VARCHAR2(200),
  USER_NAME      VARCHAR2(30),
  LOGIN_ID       VARCHAR2(30),
  CALL_SUCCESS   VARCHAR2(1),
  PARAM_VARIABLE VARCHAR2(200),
  ORG_ID         VARCHAR2(30),
  USER_ID        VARCHAR2(30)
);
-- Add comments to the columns 
comment on column TB_INTERFACE_LOG.LOG_ID
  is '日志ID，记录唯一标示';
comment on column TB_INTERFACE_LOG.CALL_TIME
  is '调用时间';
comment on column TB_INTERFACE_LOG.CALLER_NAME
  is '服务请求方名称';
comment on column TB_INTERFACE_LOG.TERMINAL_ID
  is '终端标识(IP或移动设备序列号)';
comment on column TB_INTERFACE_LOG.RESULT_COUNT
  is '调用参数记录';
comment on column TB_INTERFACE_LOG.INTERFACE_DESC
  is '调用接口描述';
comment on column TB_INTERFACE_LOG.ORG_NO
  is '调用用户所在单位编号';
comment on column TB_INTERFACE_LOG.ORG_NAME
  is '调用用户所在单位名称';
comment on column TB_INTERFACE_LOG.USER_NAME
  is '调用用户名';
comment on column TB_INTERFACE_LOG.LOGIN_ID
  is '调用用户账号';
comment on column TB_INTERFACE_LOG.CALL_SUCCESS
  is '调用结果（0:失败;1:成功）';
comment on column TB_INTERFACE_LOG.PARAM_VARIABLE
  is '返回数据条目数';
comment on column TB_INTERFACE_LOG.ORG_ID
  is '调用用户所在单位ID';
comment on column TB_INTERFACE_LOG.USER_ID
  is '用户ID';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_INTERFACE_LOG
  add constraint PK_TB_INTERFACE_LOG primary key (LOG_ID);
commit;
exit;