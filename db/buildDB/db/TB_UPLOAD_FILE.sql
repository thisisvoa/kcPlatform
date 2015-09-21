-- Create table
create table TB_UPLOAD_FILE
(
  FILE_ID        VARCHAR2(32) not null,
  FILE_NAME      VARCHAR2(64),
  DISK_FILE_NAME VARCHAR2(512),
  FILE_SIZE      NUMBER,
  FILE_TYPE      VARCHAR2(16),
  FILE_PATH      VARCHAR2(512),
  DOWNLOAD_NUM   NUMBER,
  REMARK         VARCHAR2(256),
  CREATE_TIME    TIMESTAMP(6),
  CREATOR        VARCHAR2(30),
  MODIFY_TIME    TIMESTAMP(6),
  MODIFY_USER    VARCHAR2(30)
);
-- Add comments to the columns 
comment on column TB_UPLOAD_FILE.FILE_ID
  is '文件ID';
comment on column TB_UPLOAD_FILE.FILE_NAME
  is '文件名';
comment on column TB_UPLOAD_FILE.DISK_FILE_NAME
  is '源文件文件名';
comment on column TB_UPLOAD_FILE.FILE_SIZE
  is '文件大小';
comment on column TB_UPLOAD_FILE.FILE_TYPE
  is '文件类型';
comment on column TB_UPLOAD_FILE.FILE_PATH
  is '文件路径';
comment on column TB_UPLOAD_FILE.DOWNLOAD_NUM
  is '下载次数';
comment on column TB_UPLOAD_FILE.REMARK
  is '备注';
comment on column TB_UPLOAD_FILE.CREATE_TIME
  is '上传时间';
comment on column TB_UPLOAD_FILE.CREATOR
  is '上传用户';
comment on column TB_UPLOAD_FILE.MODIFY_TIME
  is '修改时间';
comment on column TB_UPLOAD_FILE.MODIFY_USER
  is '修改用户';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TB_UPLOAD_FILE
  add constraint PK_TB_UPLOAD_FILE primary key (FILE_ID);
commit;
exit;