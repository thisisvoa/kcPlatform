/**
 * @(#)com.casic27.platform.base.file.service.UploadFileService
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright: Copyright(c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.base.file.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casic27.platform.sys.context.SpringContextHolder;
import com.casic27.platform.sys.security.context.SecurityContext;
import com.casic27.platform.base.file.addon.FileServiceAddon;
import com.casic27.platform.base.file.config.FileServiceConfiguration;
import com.casic27.platform.base.file.context.FileUploadContext;
import com.casic27.platform.base.file.dao.IUploadFileMapper;
import com.casic27.platform.base.file.entity.UploadFile;
import com.casic27.platform.base.file.exception.FileUploadException;
import com.casic27.platform.base.file.util.FileUploadUtil;
import com.casic27.platform.core.service.BaseService;

@Service("uploadFileService")
public class UploadFileService extends BaseService {
	
	private static Log log = LogFactory.getLog(UploadFileService.class);
	
	@Autowired
	private IUploadFileMapper uploadFileMapper;
	
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<UploadFile> findUploadFile(Map<String,Object> queryMap){
		return uploadFileMapper.findUploadFile(queryMap);
	}
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 */
	public List<UploadFile> findUploadFilePaging(Map<String,Object> queryMap){
		return uploadFileMapper.findUploadFilePaging(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public UploadFile getUploadFileById(String id){
		return uploadFileMapper.getUploadFileById(id);
	}
	
	/**
	 * 根据ID查询,多个Id值用逗号(,)隔开
	 */
	public List<UploadFile> getUploadFileListById(String[] ids){
		return uploadFileMapper.getUploadFileListById(ids);
	}
	/**
	 * 新增
	 */
	public void addUploadFile(UploadFile uploadFile){
		String userId = SecurityContext.getCurrentUser().getZjid();
		uploadFile.setCreator(userId);
		uploadFile.setModifyUser(userId);
		uploadFileMapper.addUploadFile(uploadFile);
	}
	
	/**
	 * 修改
	 */
	public void updateUploadFile(UploadFile uploadFile){
		uploadFileMapper.updateUploadFile(uploadFile);
	}
	
	/**
	 *删除
	 */
	public void deleteUploadFile(String id){
		uploadFileMapper.deleteUploadFile(id);
	}
	
	/**
	 * 下载次数+1
	 * @param id
	 */
	public void upDownloadNum(String id){
		uploadFileMapper.upDownloadNum(id);
	}
	
	
	/**
	 * 上传文件
	 * @param fileUploadContext
	 * @return
	 * @throws FileUploadException
	 */
	public boolean uploadFile(FileUploadContext fileUploadContext)throws FileUploadException {
		FileServiceConfiguration configuration = FileServiceConfiguration.getInstance();
		String addonBeanName = fileUploadContext.getAddonBeanName();
		FileServiceAddon addon = SpringContextHolder.getBean(addonBeanName);;
		if(addon!=null){
			boolean isContinue = addon.onBeforeUpload(fileUploadContext);//触发插件
			if(!isContinue){
				return false;
			};
		}
		InputStream inputStream = fileUploadContext.getFileInputStream();
		UploadFile uploadFile = fileUploadContext.getUploadFile();
		boolean useFtp = configuration.isUseFtp();
		String diskFileName = uploadFile.getDiskFileName();
        String filePath = uploadFile.getFilePath();
        
        boolean result = false;
		try {
			if (useFtp) {
				filePath = filePath.replace(diskFileName, "");  //因为路径中包含了文件名，所以把文件名清空
				result = FtpService.uploadFtp(filePath, diskFileName, inputStream);//直接取得输入流上传到FTP上
			}else{
				String fileDiskPath = configuration.getLocalDiskPath(filePath);  //取得文件保存到硬盘上的路径
				result = FileUploadUtil.saveFile(new File(fileDiskPath), inputStream); //保存文件到硬盘上
			}
		}catch(FileUploadException e){
			throw e;
		}finally{
			try {
				inputStream.close();
			} catch (IOException e) {
				log.error(e);
			}
		}
		if(result){
			try {
				addUploadFile(uploadFile);
				if(addon!=null){
					addon.onAfterUpload(fileUploadContext);//触发插件
				}
			} catch (Exception e) {
				throw new FileUploadException("保存上传文件信息失败！");
			}
		}
		
		return result;
	}
	
	/**
	 * 下载文件
	 * @param uploadFile
	 * @param outputStream
	 * @return
	 * @throws Exception
	 */
	public boolean downloadFile(UploadFile uploadFile, OutputStream outputStream)throws Exception{
		boolean result = false;
		FileServiceConfiguration configuration = FileServiceConfiguration.getInstance();
		String ftpFileName = uploadFile.getDiskFileName();
        String filePath = uploadFile.getFilePath();
		boolean useFtp = configuration.isUseFtp();
		try {
			if (useFtp) {
				String relativePath = filePath.replace(ftpFileName, "");  //因为路径中包含了文件名，所以把文件名清空
			    result = FtpService.retrieveFile(relativePath, ftpFileName, outputStream); //向输出流中写数据
			}else{
				String relativePath = configuration.getLocalDiskPath(filePath);
				String fileType = uploadFile.getFileType();
				if(FileUploadUtil.isZip(fileType)){
					FileUploadUtil.zipFileToOS(relativePath, outputStream);
				}else{
					FileUploadUtil.fileToOS(relativePath, outputStream);
				}
			}
			outputStream.flush();
			upDownloadNum(uploadFile.getFileId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 删除文件
	 * @param fileUploadContext
	 */
	public void deleteFile(FileUploadContext fileUploadContext){
		FileServiceConfiguration configuration = FileServiceConfiguration.getInstance();
		String addonBeanName = fileUploadContext.getAddonBeanName();
		FileServiceAddon addon = SpringContextHolder.getBean(addonBeanName);;
		if(addon!=null){
			boolean isContinue = addon.onBeforeDelete(fileUploadContext);
			if(!isContinue){
				return ;
			};
		}
		UploadFile uploadFile = fileUploadContext.getUploadFile();
		boolean useFtp = configuration.isUseFtp();  //判断是否有FTP服务器
		//如果配置了FTP文件服务器，就直接从文件服务器中删除，否则从应用服务器中删除
        if (useFtp) {
            FtpService.deleteFile(uploadFile.getFilePath());
        } else {
            FileUploadUtil.deleteFile(new File(configuration.getLocalDiskPath( uploadFile.getFilePath())));
        }
        deleteUploadFile(fileUploadContext.getUploadFile().getFileId());
        if(addon!=null){
			addon.onAfterDelete(fileUploadContext);
		}
	}
}