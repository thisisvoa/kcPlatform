package com.casic27.platform.base.file.config;

import com.casic27.platform.base.file.entity.FileServer;
import com.casic27.platform.sys.context.ApplicationContext;

/**
 * 文件服务配置信息
 * @author Administrator
 *
 */
public class FileServiceConfiguration {
	private static FileServiceConfiguration configuration = new FileServiceConfiguration();

	public FileServiceConfiguration(){
		
	}
	
	public static FileServiceConfiguration getInstance(){
		return configuration;
	}
	
	/**
     * 上传文件的最大尺寸，单位为M
     */
	private int uploadMaxSize;
	
	 /**
     * 临时文件最大生成时间，以分种为单位
     */
	private int tempFileMaxElapse;
	
	/**
	 * 应用服务器存放上传文件的目录
	 */
	private String uploadTempDir;
	
	/**
	 * 使用ftp服务器
	 */
	private boolean useFtp = false;
	
	/**
	 * ftp服务器配置
	 */
	private FileServer fileServer;
	
	public String getLocalDiskPath(String relativePath){
		StringBuffer sb = new StringBuffer();
		sb.append(ApplicationContext.getInstance().getWebRoot())
		  .append(uploadTempDir)
		  .append("/")
		  .append(relativePath);
		
		return sb.toString();
	}
	public int getUploadMaxSize() {
		return uploadMaxSize;
	}

	public void setUploadMaxSize(int uploadMaxSize) {
		this.uploadMaxSize = uploadMaxSize;
	}

	public int getTempFileMaxElapse() {
		return tempFileMaxElapse;
	}

	public void setTempFileMaxElapse(int tempFileMaxElapse) {
		this.tempFileMaxElapse = tempFileMaxElapse;
	}

	public String getUploadTempDir() {
		return uploadTempDir;
	}

	public void setUploadTempDir(String uploadTempDir) {
		this.uploadTempDir = uploadTempDir;
	}

	public boolean isUseFtp() {
		return useFtp;
	}

	public void setUseFtp(boolean useFtp) {
		this.useFtp = useFtp;
	}

	public FileServer getFileServer() {
		return fileServer;
	}

	public void setFileServer(FileServer fileServer) {
		this.fileServer = fileServer;
	}
}
