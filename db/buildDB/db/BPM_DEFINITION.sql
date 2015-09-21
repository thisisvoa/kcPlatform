-- Create table
create table BPM_DEFINITION
(
  DEF_ID             VARCHAR2(64) not null,
  TYPE_ID            VARCHAR2(64),
  SUBJECT            VARCHAR2(64),
  DEF_KEY            VARCHAR2(64),
  INST_NAME_RULE     VARCHAR2(128),
  DESCRIPTION        VARCHAR2(512),
  STATUS             CHAR(1),
  DEF_XML            CLOB,
  ACT_DEF_ID         VARCHAR2(64),
  ACT_DEF_KEY        VARCHAR2(255),
  ACT_DEPLOY_ID      VARCHAR2(64),
  CREATE_TIME        TIMESTAMP(6),
  CREATOR            VARCHAR2(30),
  MODIFY_TIME        TIMESTAMP(6),
  MODIFY_USER        VARCHAR2(30),
  TO_FIRST_NODE      CHAR(1),
  PARENT_DEF_ID      VARCHAR2(64),
  IS_MAIN            CHAR(1),
  VERSION            NUMBER,
  USABLE             CHAR(1),
  DIRECT_START       CHAR(1),
  SAME_EXECUTOR_JUMP CHAR(1)
);
-- Add comments to the columns 
comment on column BPM_DEFINITION.DEF_ID
  is 'ID';
comment on column BPM_DEFINITION.TYPE_ID
  is '流程分类';
comment on column BPM_DEFINITION.SUBJECT
  is '流程标题';
comment on column BPM_DEFINITION.DEF_KEY
  is '流程';
comment on column BPM_DEFINITION.INST_NAME_RULE
  is '流程实例标题';
comment on column BPM_DEFINITION.DESCRIPTION
  is '流程描述';
comment on column BPM_DEFINITION.STATUS
  is '状态( 0：草稿 1：已发布  2：挂起)';
comment on column BPM_DEFINITION.ACT_DEF_ID
  is '发布后的activitiID';
comment on column BPM_DEFINITION.ACT_DEPLOY_ID
  is 'activiti部署包ID';
comment on column BPM_DEFINITION.CREATE_TIME
  is '创建时间';
comment on column BPM_DEFINITION.CREATOR
  is '创建人';
comment on column BPM_DEFINITION.MODIFY_TIME
  is '修改时间';
comment on column BPM_DEFINITION.MODIFY_USER
  is '修改用户';
comment on column BPM_DEFINITION.TO_FIRST_NODE
  is '跳转到第一个节点(0,不跳转 1,跳转)';
comment on column BPM_DEFINITION.PARENT_DEF_ID
  is '隶属主定义ID(当基于旧版本流程上进行新版本发布时，则生成一份新的记录，并且该记录的该字段则隶属于原来记录的主键id)';
comment on column BPM_DEFINITION.IS_MAIN
  is '是否为主版本(1=主版本 0=非主版本)';
comment on column BPM_DEFINITION.VERSION
  is '版本号';
comment on column BPM_DEFINITION.USABLE
  is '是否启用';
comment on column BPM_DEFINITION.DIRECT_START
  is '直接启动流程';
comment on column BPM_DEFINITION.SAME_EXECUTOR_JUMP
  is '相邻任务节点人员相同时自动跳过';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_DEFINITION
  add constraint PK_BPM_DEFINITION primary key (DEF_ID);
exit;