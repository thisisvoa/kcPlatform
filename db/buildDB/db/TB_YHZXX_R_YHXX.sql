-- Create table
create table TB_YHZXX_R_YHXX
(
  ZJID       VARCHAR2(30) not null,
  YHZXX_ZJID VARCHAR2(30) not null,
  YHXX_ZJID  VARCHAR2(30) not null
);
-- Add comments to the table 
comment on table TB_YHZXX_R_YHXX
  is '用户组用户关系表';
-- Add comments to the columns 
comment on column TB_YHZXX_R_YHXX.ZJID
  is '主键ID';
comment on column TB_YHZXX_R_YHXX.YHZXX_ZJID
  is '用户组主键ID';
comment on column TB_YHZXX_R_YHXX.YHXX_ZJID
  is '用户主键ID';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_YHZXX_R_YHXX
  add constraint PK_TB_YHZXX_R_YHXX primary key (ZJID);
commit;
exit;