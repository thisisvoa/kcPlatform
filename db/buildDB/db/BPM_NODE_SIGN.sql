-- Create table
create table BPM_NODE_SIGN
(
  SIGN_ID     VARCHAR2(32),
  ACT_DEF_ID  VARCHAR2(64),
  NODE_ID     VARCHAR2(64),
  VOTE_AMOUNT NUMBER,
  DECIDE_TYPE NUMBER,
  VOTE_TYPE   NUMBER,
  FLOW_MODE   NUMBER
);
-- Add comments to the table 
comment on table BPM_NODE_SIGN
  is '会签设置';
-- Add comments to the columns 
comment on column BPM_NODE_SIGN.SIGN_ID
  is 'ID';
comment on column BPM_NODE_SIGN.ACT_DEF_ID
  is 'act流程定义ID';
comment on column BPM_NODE_SIGN.NODE_ID
  is '节点ID';
comment on column BPM_NODE_SIGN.VOTE_AMOUNT
  is '票数';
comment on column BPM_NODE_SIGN.DECIDE_TYPE
  is '决策方式1：通过 2：拒绝';
comment on column BPM_NODE_SIGN.VOTE_TYPE
  is '投票类型：1=百分比 2=绝对票数';
comment on column BPM_NODE_SIGN.FLOW_MODE
  is '后续处理模式 1=直接处理 2=等待所有人投票';
exit;