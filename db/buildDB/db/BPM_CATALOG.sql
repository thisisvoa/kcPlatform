-- Create table
create table BPM_CATALOG
(
  ID           VARCHAR2(64) not null,
  CATALOG_KEY  VARCHAR2(32),
  CATALOG_NAME VARCHAR2(64),
  PARENT_ID    VARCHAR2(64),
  ORDER_NO     INTEGER,
  CREATE_TIME  TIMESTAMP(6),
  CREATE_USER  VARCHAR2(30),
  MODIFY_TIME  TIMESTAMP(6),
  MODIFY_USER  VARCHAR2(30),
  LAYER_CODE   VARCHAR2(64),
  CATALOG_TYPE VARCHAR2(2)
);
-- Add comments to the table 
comment on table BPM_CATALOG
  is '流程目录/分类';
-- Add comments to the columns 
comment on column BPM_CATALOG.ID
  is '流程分类ID';
comment on column BPM_CATALOG.CATALOG_KEY
  is '节点key(需唯一)';
comment on column BPM_CATALOG.CATALOG_NAME
  is '节点名称';
comment on column BPM_CATALOG.PARENT_ID
  is '父节点ID';
comment on column BPM_CATALOG.ORDER_NO
  is '排序号';
comment on column BPM_CATALOG.CREATE_TIME
  is '创建时间';
comment on column BPM_CATALOG.CREATE_USER
  is '创建用户';
comment on column BPM_CATALOG.MODIFY_TIME
  is '修改时间';
comment on column BPM_CATALOG.MODIFY_USER
  is '修改用户';
comment on column BPM_CATALOG.LAYER_CODE
  is '层级代码，内部维护方便查询';
comment on column BPM_CATALOG.CATALOG_TYPE
  is '节点类型';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_CATALOG
  add constraint PK_BPM_CATALOG primary key (ID);
exit;