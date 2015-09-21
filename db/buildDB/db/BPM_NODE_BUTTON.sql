-- Create table
create table BPM_NODE_BUTTON
(
  BTN_ID         VARCHAR2(64) not null,
  DEF_ID         VARCHAR2(64),
  ACT_DEF_ID     VARCHAR2(64),
  NODE_ID        VARCHAR2(64),
  NODE_TYPE      CHAR(1),
  BTN_NAME       VARCHAR2(64),
  TYPE           VARCHAR2(2),
  BEFORE_HANDLER VARCHAR2(1024),
  AFTER_HANDLER  VARCHAR2(1024),
  SN             NUMBER
);
-- Add comments to the columns 
comment on column BPM_NODE_BUTTON.BTN_ID
  is 'ID';
comment on column BPM_NODE_BUTTON.DEF_ID
  is '流程定义ID';
comment on column BPM_NODE_BUTTON.ACT_DEF_ID
  is 'ACT流程定义ID';
comment on column BPM_NODE_BUTTON.NODE_ID
  is '节点ID';
comment on column BPM_NODE_BUTTON.NODE_TYPE
  is '节点类型:(0:开始节点 1:普通节点 2:会签节点)';
comment on column BPM_NODE_BUTTON.BTN_NAME
  is '按钮名称';
comment on column BPM_NODE_BUTTON.TYPE
  is '操作类型';
comment on column BPM_NODE_BUTTON.BEFORE_HANDLER
  is '前置脚本';
comment on column BPM_NODE_BUTTON.AFTER_HANDLER
  is '后置脚本';
comment on column BPM_NODE_BUTTON.SN
  is '按钮序号';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_NODE_BUTTON
  add constraint PK_BPM_NODE_BUTTON primary key (BTN_ID);
exit;