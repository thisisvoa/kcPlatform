-- Create table
create table TB_JOB_COMMAND
(
  COMMAND_ID     CHAR(32) not null,
  JOB_ID         CHAR(36),
  JOB_GROUP_NAME VARCHAR2(64),
  JOB_NAME       VARCHAR2(64),
  CTRL_COMMAND   CHAR(1),
  ISSUE_TIME     TIMESTAMP(6)
);
-- Add comments to the table 
comment on table TB_JOB_COMMAND
  is '对任务的控制命令';
-- Add comments to the columns 
comment on column TB_JOB_COMMAND.JOB_ID
  is '任务ID';
comment on column TB_JOB_COMMAND.JOB_GROUP_NAME
  is '任务分组名';
comment on column TB_JOB_COMMAND.JOB_NAME
  is '任务名';
comment on column TB_JOB_COMMAND.CTRL_COMMAND
  is '1：卸载 2：装载';
comment on column TB_JOB_COMMAND.ISSUE_TIME
  is '命令发布时间（数据库时间，精确到秒）';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_JOB_COMMAND
  add constraint PK_T_JOB_COMMAND primary key (COMMAND_ID);
-- Create/Recreate indexes 
create index IDX_JOB_COMMAND_FULL_JOB_NAME on TB_JOB_COMMAND (JOB_GROUP_NAME, JOB_NAME);
create index IDX_JOB_COMMAND_ISSUETIME on TB_JOB_COMMAND (ISSUE_TIME);
create index IDX_JOB_COMMAND_JOBID on TB_JOB_COMMAND (JOB_ID);
commit;
exit;