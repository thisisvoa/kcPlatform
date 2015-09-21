-- Create table
create table TB_YHXX_R_JSXX
(
  ZJID      VARCHAR2(30) not null,
  YHXX_ZJID VARCHAR2(30),
  JSXX_ZJID VARCHAR2(30)
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_YHXX_R_JSXX
  add constraint PK_TB_YHXX_R_JSXX primary key (ZJID);
commit;
exit;