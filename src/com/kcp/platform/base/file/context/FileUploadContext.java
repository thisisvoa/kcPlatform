package com.kcp.platform.base.file.context;


import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.kcp.platform.base.file.entity.UploadFile;
import com.kcp.platform.base.file.util.FileUploadUtil;
import com.kcp.platform.util.CodeGenerator;

public class FileUploadContext {
	/**
	 * 上传的文件列表
	 */
	private UploadFile uploadFile;
	
	/**
	 * 文件上传插件名称
	 */
	private String addonBeanName;
	
	/**
	 * 文件读入流
	 */
	private InputStream fileInputStream;

	public UploadFile getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(UploadFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getAddonBeanName() {
		return addonBeanName;
	}

	public void setAddonBeanName(String addonBeanName) {
		this.addonBeanName = addonBeanName;
	}
	
	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}
	
	public static UploadFile transfer2FileUpload(MultipartFile multipartFile) {
        String realFileName = multipartFile.getOriginalFilename();
        String fileId = CodeGenerator.getUUID32();
        String fileType = FileUploadUtil.getFileType(realFileName);
        long fileSize = multipartFile.getSize();
        String filePath = FileUploadUtil.getSaveRelativePath(fileId + "." + fileType);
        UploadFile result = new UploadFile();
        result.setFileId(fileId);
        result.setFilePath(filePath);
        result.setFileName(realFileName);
        result.setFileSize(fileSize);
        result.setFileType(fileType);
        result.setDiskFileName(fileId + "." + fileType);
        result.setDownloadNum(0);
        return result;
    }
}
