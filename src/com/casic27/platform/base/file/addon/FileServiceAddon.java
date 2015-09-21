package com.casic27.platform.base.file.addon;

import com.casic27.platform.base.file.context.FileUploadContext;


public interface FileServiceAddon {
	/**
	 * 文件是上传前
	 * @param fileUploadContext
	 */
	boolean onBeforeUpload(FileUploadContext fileUploadContext);
	
	/**
	 * 文件上传后
	 * @param fileUploadContext
	 */
	void onAfterUpload(FileUploadContext fileUploadContext);
	
	/**
	 * 文件删除前
	 * @param fileUploadContext
	 */
	boolean onBeforeDelete(FileUploadContext fileUploadContext);
	
	/**
	 * 文件删除后
	 * @param fileUploadContext
	 */
	void onAfterDelete(FileUploadContext fileUploadContext);
}
