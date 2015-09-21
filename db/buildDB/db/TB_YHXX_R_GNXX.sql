-- Create table
create table TB_YHXX_R_GNXX
(
  ZJID      VARCHAR2(30) not null,
  YHXX_ZJID VARCHAR2(30) not null,
  GNXX_ZJID VARCHAR2(30) not null
);
-- Add comments to the table 
comment on table TB_YHXX_R_GNXX
  is '用户功能关系表';
-- Add comments to the columns 
comment on column TB_YHXX_R_GNXX.ZJID
  is '主键ID';
comment on column TB_YHXX_R_GNXX.YHXX_ZJID
  is '用户主键ID';
comment on column TB_YHXX_R_GNXX.GNXX_ZJID
  is '功能主键ID';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_YHXX_R_GNXX
  add constraint PK_TB_YHXX_R_GNXX primary key (ZJID);
commit;
exit;