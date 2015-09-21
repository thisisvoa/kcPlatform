-- Create table
create table BPM_NODE_PRIVILEGE
(
  PRIVILEGE_ID   VARCHAR2(32) not null,
  ACT_DEF_ID     VARCHAR2(64),
  NODE_ID        VARCHAR2(64),
  PRIVILEGE_MODE NUMBER,
  USER_TYPE      NUMBER,
  CMP_NAMES      CLOB,
  CMP_IDS        CLOB
);
-- Add comments to the table 
comment on table BPM_NODE_PRIVILEGE
  is '会签特权人员';
-- Add comments to the columns 
comment on column BPM_NODE_PRIVILEGE.ACT_DEF_ID
  is '流程定义ID';
comment on column BPM_NODE_PRIVILEGE.NODE_ID
  is '节点ID';
comment on column BPM_NODE_PRIVILEGE.PRIVILEGE_MODE
  is '特权类型(0=所有特权,1=允许直接处理,2=允许一票制,3=允许补签)';
comment on column BPM_NODE_PRIVILEGE.USER_TYPE
  is '用户类型';
comment on column BPM_NODE_PRIVILEGE.CMP_NAMES
  is '参与者名称';
comment on column BPM_NODE_PRIVILEGE.CMP_IDS
  is '参与者ID';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_NODE_PRIVILEGE
  add constraint PK_BPM_NODE_PRIVILEGE primary key (PRIVILEGE_ID);
exit;