-- Create table
create table TB_DWXX_R_GNXX
(
  ZJID      VARCHAR2(30) not null,
  DWXX_ZJID VARCHAR2(30) not null,
  GNXX_ZJID VARCHAR2(30) not null
);
-- Add comments to the table 
comment on table TB_DWXX_R_GNXX
  is '单位功能关系表';
-- Add comments to the columns 
comment on column TB_DWXX_R_GNXX.ZJID
  is '主键ID';
comment on column TB_DWXX_R_GNXX.DWXX_ZJID
  is '单位主键ID';
comment on column TB_DWXX_R_GNXX.GNXX_ZJID
  is '功能主键ID';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_DWXX_R_GNXX
  add constraint PK_TB_DWXX_R_GNXX primary key (ZJID);
commit;
exit;