package com.kcp.platform.base.file.entity;

import java.util.Date;

import com.kcp.platform.core.entity.BaseEntity;

public class UploadFile extends BaseEntity{

	/**
	 * 文件ID
	 */
	private String fileId;

	/**
	 * 文件名
	 */
	private String fileName;

	/**
	 * 源文件文件名
	 */
	private String diskFileName;

	/**
	 * 文件大小
	 */
	private Long fileSize;

	/**
	 * 文件类型
	 */
	private String fileType;

	/**
	 * 文件路径
	 */
	private String filePath;

	/**
	 * 下载次数
	 */
	private Integer downloadNum;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 上传时间
	 */
	private Date createTime;

	/**
	 * 上传用户
	 */
	private String creator;

	/**
	 * 上传用户名
	 */
	private String userName;
	
	/**
	 * 修改时间
	 */
	private Date modifyTime;

	/**
	 * 修改用户
	 */
	private String modifyUser;


	public String getFileId(){
		return this.fileId;
	}
	
	public void setFileId(String fileId){
		this.fileId = fileId;
	}

	public String getFileName(){
		return this.fileName;
	}
	
	public void setFileName(String fileName){
		this.fileName = fileName;
	}

	public String getDiskFileName(){
		return this.diskFileName;
	}
	
	public void setDiskFileName(String diskFileName){
		this.diskFileName = diskFileName;
	}

	public Long getFileSize(){
		return this.fileSize;
	}
	
	public void setFileSize(Long fileSize){
		this.fileSize = fileSize;
	}

	public String getFileType(){
		return this.fileType;
	}
	
	public void setFileType(String fileType){
		this.fileType = fileType;
	}

	public String getFilePath(){
		return this.filePath;
	}
	
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}

	public Integer getDownloadNum(){
		return this.downloadNum;
	}
	
	public void setDownloadNum(Integer downloadNum){
		this.downloadNum = downloadNum;
	}

	public String getRemark(){
		return this.remark;
	}
	
	public void setRemark(String remark){
		this.remark = remark;
	}

	public Date getCreateTime(){
		return this.createTime;
	}
	
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public String getCreator(){
		return this.creator;
	}
	
	public void setCreator(String creator){
		this.creator = creator;
	}

	public Date getModifyTime(){
		return this.modifyTime;
	}
	
	public void setModifyTime(Date modifyTime){
		this.modifyTime = modifyTime;
	}

	public String getModifyUser(){
		return this.modifyUser;
	}
	
	public void setModifyUser(String modifyUser){
		this.modifyUser = modifyUser;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
