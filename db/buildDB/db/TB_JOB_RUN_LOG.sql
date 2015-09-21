-- Create table
create table TB_JOB_RUN_LOG
(
  LOG_ID      CHAR(32) not null,
  SCHEDULER   VARCHAR2(60),
  JOB_NAME    VARCHAR2(60),
  BEGIN_TIME  TIMESTAMP(6),
  END_TIME    TIMESTAMP(6),
  RESULT_TYPE CHAR(1),
  RESULT_INFO VARCHAR2(2000),
  CREATE_TIME TIMESTAMP(6)
);
-- Add comments to the table 
comment on table TB_JOB_RUN_LOG
  is '任务运行日志';
-- Add comments to the columns 
comment on column TB_JOB_RUN_LOG.LOG_ID
  is '主键ID';
comment on column TB_JOB_RUN_LOG.SCHEDULER
  is '任务调度管理器，格式为IP:PORT';
comment on column TB_JOB_RUN_LOG.JOB_NAME
  is '任务名称';
comment on column TB_JOB_RUN_LOG.BEGIN_TIME
  is '开始时间';
comment on column TB_JOB_RUN_LOG.END_TIME
  is '结束时间';
comment on column TB_JOB_RUN_LOG.RESULT_TYPE
  is '执行结果 1：成功执行 2：发生异常';
comment on column TB_JOB_RUN_LOG.RESULT_INFO
  is '操作结果信息';
comment on column TB_JOB_RUN_LOG.CREATE_TIME
  is '创建时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_JOB_RUN_LOG
  add constraint PK_T_JOB_RUN_LOG primary key (LOG_ID);
commit;
exit;