-- Create table
create table BPM_NODE_SCRIPT
(
  SCRIPT_ID   VARCHAR2(64) not null,
  DEF_ID      VARCHAR2(64),
  ACT_DEF_ID  VARCHAR2(64),
  NODE_ID     VARCHAR2(64),
  SCRIPT_TYPE CHAR(1),
  SCRIPT      VARCHAR2(4000),
  REMARK      VARCHAR2(128)
);
-- Add comments to the columns 
comment on column BPM_NODE_SCRIPT.DEF_ID
  is '流程定义ID';
comment on column BPM_NODE_SCRIPT.ACT_DEF_ID
  is 'ACTIVITI流程定义ID';
comment on column BPM_NODE_SCRIPT.NODE_ID
  is '节点ID';
comment on column BPM_NODE_SCRIPT.SCRIPT_TYPE
  is '脚本类型(1.事件前置脚本 2.事件后置脚本 3.脚本节点)';
comment on column BPM_NODE_SCRIPT.SCRIPT
  is '脚本';
comment on column BPM_NODE_SCRIPT.REMARK
  is '备注';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_NODE_SCRIPT
  add constraint PK_BPM_NODE_SCRIPT primary key (SCRIPT_ID);
exit;