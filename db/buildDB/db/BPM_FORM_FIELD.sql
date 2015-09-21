-- Create table
create table BPM_FORM_FIELD
(
  FIELD_ID      VARCHAR2(32) not null,
  TABLE_ID      VARCHAR2(32),
  FIELD_NAME    VARCHAR2(128),
  FIELD_TYPE    VARCHAR2(64),
  IS_REQUIRED   CHAR(1),
  IS_LIST       CHAR(1),
  IS_QUERY      CHAR(1),
  FIELD_DESC    VARCHAR2(128),
  CHAR_LEN      NUMBER,
  INT_LEN       NUMBER,
  DECIMAL_LEN   NUMBER,
  DICT_TYPE     VARCHAR2(128),
  IS_DELETED    CHAR(1),
  VALID_RULE    VARCHAR2(128),
  ORIGINAL_NAME VARCHAR2(128),
  SN            NUMBER,
  VALUE_FROM    VARCHAR2(2),
  SCRIPT        VARCHAR2(1024),
  CONTROL_TYPE  VARCHAR2(2),
  IS_HIDDEN     CHAR(1),
  IS_FLOWVAR    CHAR(1),
  IDENTITY      VARCHAR2(32),
  OPTIONS       VARCHAR2(256),
  CTL_PROPERTY  VARCHAR2(512),
  OPTION_FROM   CHAR(1)
);
-- Add comments to the table 
comment on table BPM_FORM_FIELD
  is '表单字段;';
-- Add comments to the columns 
comment on column BPM_FORM_FIELD.TABLE_ID
  is '所属表';
comment on column BPM_FORM_FIELD.FIELD_NAME
  is '字段名称';
comment on column BPM_FORM_FIELD.FIELD_TYPE
  is '字段类型';
comment on column BPM_FORM_FIELD.IS_REQUIRED
  is '是否必填';
comment on column BPM_FORM_FIELD.IS_LIST
  is '是否列表显示';
comment on column BPM_FORM_FIELD.IS_QUERY
  is '是否查询显示';
comment on column BPM_FORM_FIELD.FIELD_DESC
  is '字段描述';
comment on column BPM_FORM_FIELD.CHAR_LEN
  is '字符长度';
comment on column BPM_FORM_FIELD.INT_LEN
  is '整数长度';
comment on column BPM_FORM_FIELD.DECIMAL_LEN
  is '小数长度';
comment on column BPM_FORM_FIELD.DICT_TYPE
  is '字典表类型';
comment on column BPM_FORM_FIELD.IS_DELETED
  is '是否删除';
comment on column BPM_FORM_FIELD.VALID_RULE
  is '验证规则';
comment on column BPM_FORM_FIELD.ORIGINAL_NAME
  is '字段原名';
comment on column BPM_FORM_FIELD.SN
  is '排列顺序';
comment on column BPM_FORM_FIELD.VALUE_FROM
  is '值来源0 - 表单 1 - 脚本';
comment on column BPM_FORM_FIELD.SCRIPT
  is '默认脚本';
comment on column BPM_FORM_FIELD.CONTROL_TYPE
  is '0,无特殊控件
1,数据字典
2,用户选择器
3.组织选择器';
comment on column BPM_FORM_FIELD.IS_HIDDEN
  is '是否隐藏';
comment on column BPM_FORM_FIELD.IS_FLOWVAR
  is '是否流程变量';
comment on column BPM_FORM_FIELD.OPTIONS
  is '选项';
comment on column BPM_FORM_FIELD.CTL_PROPERTY
  is '控件属性';
comment on column BPM_FORM_FIELD.OPTION_FROM
  is '选项数据源来源(1:固定项 2:字典表 3:URL)';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPM_FORM_FIELD
  add constraint PK_BPM_FORM_FIELD primary key (FIELD_ID);
exit;