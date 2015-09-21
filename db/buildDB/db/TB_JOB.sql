-- Create table
create table TB_JOB
(
  JOB_ID           CHAR(32) not null,
  JOB_GROUP_NAME   VARCHAR2(64),
  JOB_NAME         VARCHAR2(64),
  TITLE            VARCHAR2(200),
  START_TIME       VARCHAR2(60),
  END_TIME         VARCHAR2(60),
  TRIGGER_PARAM    VARCHAR2(4000),
  CREATE_TIME      TIMESTAMP(6),
  UPDATE_TIME      TIMESTAMP(6),
  LAST_ACTIVE_TIME TIMESTAMP(6),
  REMARK           VARCHAR2(200),
  ACTIVE_STATUS    CHAR(1),
  RELEASED         CHAR(1)
);
-- Add comments to the columns 
comment on column TB_JOB.JOB_GROUP_NAME
  is 'TAG，默认为DEFAULT';
comment on column TB_JOB.JOB_NAME
  is '周知标识：终身唯一的周知标识。';
comment on column TB_JOB.TITLE
  is '任务描述';
comment on column TB_JOB.START_TIME
  is '为0时表示马上开始，开始时间 格式为yyyyMMddHHmmss';
comment on column TB_JOB.END_TIME
  is '为0时表示没有结束时间，开始时间 格式为yyyyMMddHHmmss';
comment on column TB_JOB.TRIGGER_PARAM
  is '调度任务配置参数，其格式定义如下：
调度任务配置参数，其格式定义如下：
[{
    type:simple|cron,//触发器类型，simple对应SimpleTrigger  cron对应CronTrigger
    repeatInterval:10,//间隔秒数
     cronExpr:0 0 12 * * ? ,//Cron格式的表达式
},
...
]';
comment on column TB_JOB.CREATE_TIME
  is '创建时间';
comment on column TB_JOB.UPDATE_TIME
  is '更改时间戳';
comment on column TB_JOB.LAST_ACTIVE_TIME
  is '任务的最后活动时间';
comment on column TB_JOB.REMARK
  is '备注';
comment on column TB_JOB.ACTIVE_STATUS
  is '活动状态  0：停止，1：活动 ';
comment on column TB_JOB.RELEASED
  is '发布状态  0：未发布 1：已发布';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_JOB
  add constraint PK_T_JOB primary key (JOB_ID);
-- Create/Recreate indexes 
create index IDX_JOB_UPDATE_TIME on TB_JOB (UPDATE_TIME);
insert into TB_JOB (JOB_ID, JOB_GROUP_NAME, JOB_NAME, TITLE, START_TIME, END_TIME, TRIGGER_PARAM, CREATE_TIME, UPDATE_TIME, LAST_ACTIVE_TIME, REMARK, ACTIVE_STATUS, RELEASED)
values ('BD7DA81CB1B54EDBB319D101629CF7BC', 'DEFAULT', 'archiveJob', '归档任务调度', null, null, '[{"type":"cron","repeatInterval":"","cronExpr":"0 0 0 * * ? *"}]', to_timestamp('12-10-2013 13:22:49.000000', 'dd-mm-yyyy hh24:mi:ss.ff'), to_timestamp('12-10-2013 13:22:57.000000', 'dd-mm-yyyy hh24:mi:ss.ff'), to_timestamp('16-10-2013 15:51:04.000000', 'dd-mm-yyyy hh24:mi:ss.ff'), '归档任务调度', '1', '1');
commit;
exit;