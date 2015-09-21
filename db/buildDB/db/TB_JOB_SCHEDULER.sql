-- Create table
create table TB_JOB_SCHEDULER
(
  JOB_SCHEDULER_ID CHAR(32) not null,
  HOST_URL         VARCHAR2(50),
  PRIORITY         INTEGER,
  MOUNT_STATUS     CHAR(1),
  ACTIVE_STATUS    CHAR(1),
  CREATE_TIME      TIMESTAMP(6),
  UPDATE_TIME      TIMESTAMP(6),
  LAST_ACTIVE_TIME TIMESTAMP(6),
  CURR_SCHEDULER   CHAR(1),
  REMARK           VARCHAR2(200),
  VERSION          INTEGER
);
-- Add comments to the table 
comment on table TB_JOB_SCHEDULER
  is '最好可配置10台备用的全局任务调度管理器，优先级为1~10，优先级不能相同';
-- Add comments to the columns 
comment on column TB_JOB_SCHEDULER.HOST_URL
  is '任务调度管理器地址,格式为ip:port';
comment on column TB_JOB_SCHEDULER.PRIORITY
  is '优先级，可选值为1~10';
comment on column TB_JOB_SCHEDULER.MOUNT_STATUS
  is '是否装配 0：未装配 1：已装配';
comment on column TB_JOB_SCHEDULER.ACTIVE_STATUS
  is '活动状态  0：停止，1：活动 ';
comment on column TB_JOB_SCHEDULER.CREATE_TIME
  is '创建时间';
comment on column TB_JOB_SCHEDULER.UPDATE_TIME
  is '数据最后更新时间';
comment on column TB_JOB_SCHEDULER.LAST_ACTIVE_TIME
  is '最后活动时间';
comment on column TB_JOB_SCHEDULER.CURR_SCHEDULER
  is '当前调度节点 0：不是 1：是';
comment on column TB_JOB_SCHEDULER.REMARK
  is '备注';
comment on column TB_JOB_SCHEDULER.VERSION
  is '版本号';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_JOB_SCHEDULER
  add constraint PK_T_JOB_SCHEDULER primary key (JOB_SCHEDULER_ID);
-- Create/Recreate indexes 
create index IDX_JOB_SCHEDULER_UPTIME on TB_JOB_SCHEDULER (UPDATE_TIME);
insert into TB_JOB_SCHEDULER (JOB_SCHEDULER_ID, HOST_URL, PRIORITY, MOUNT_STATUS, ACTIVE_STATUS, CREATE_TIME, UPDATE_TIME, LAST_ACTIVE_TIME, CURR_SCHEDULER, REMARK, VERSION)
values ('993B3EF5FB6541FA9F03E124260E4479', 'http://127.0.0.1:8888/gdxt', 1, '1', '1', to_timestamp('30-11-2012 09:51:09.000000', 'dd-mm-yyyy hh24:mi:ss.ff'), to_timestamp('28-10-2013 09:11:09.000000', 'dd-mm-yyyy hh24:mi:ss.ff'), to_timestamp('28-10-2013 13:35:40.000000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1', null, 0);
commit;
exit;