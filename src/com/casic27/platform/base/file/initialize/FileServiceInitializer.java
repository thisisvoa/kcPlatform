package com.casic27.platform.base.file.initialize;


import org.springframework.stereotype.Component;

import com.casic27.platform.base.file.config.FileServiceConfiguration;
import com.casic27.platform.base.file.entity.FileServer;
import com.casic27.platform.sys.config.PropertyConfigurer;
import com.casic27.platform.sys.initialize.AbstractInitializer;

@Component
public class FileServiceInitializer extends AbstractInitializer {
	@Override
	public void init() {
			FileServiceConfiguration configuration = FileServiceConfiguration.getInstance();
			configuration.setUploadMaxSize(PropertyConfigurer.getIntValue("FILE_UPLOAD_MAX_SIZE"));
			configuration.setTempFileMaxElapse(PropertyConfigurer.getIntValue("TEMP_FILE_MAX_ELAPSE"));
			configuration.setUploadTempDir(PropertyConfigurer.getValue("UPLOAD_TEMP_DIR"));
			configuration.setUseFtp(Boolean.parseBoolean(PropertyConfigurer.getValue("USE_FTP")));
			FileServer fileServer = new FileServer();
			String ftpUrl = PropertyConfigurer.getValue("FTP_URL");
			String ftpUserName = PropertyConfigurer.getValue("FTP_USERNAME");
			String ftpPassword = PropertyConfigurer.getValue("FTP_PASSWORD");
			String ftpRootDir = PropertyConfigurer.getValue("FTP_ROOT_DIR");
			String httpUrl = PropertyConfigurer.getValue("HTTP_URL");
			String httpRootDir = PropertyConfigurer.getValue("HTTP_ROOT_DIR");
			fileServer.setFtpUrl(ftpUrl);
			fileServer.setFtpUserName(ftpUserName);
			fileServer.setFtpPassword(ftpPassword);
			fileServer.setFtpRootDir(ftpRootDir);
			fileServer.setHttpUrl(httpUrl);
			fileServer.setHttpRootDir(httpRootDir);
			configuration.setFileServer(fileServer);
	}
	
	@Override
	public String getDesc() {
		return "文件服务";
	}
}
