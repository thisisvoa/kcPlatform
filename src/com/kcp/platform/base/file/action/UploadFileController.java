package com.kcp.platform.base.file.action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.common.user.service.UserService;
import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.core.page.Page;
import com.kcp.platform.core.page.PageContextHolder;
import com.kcp.platform.sys.security.context.SecurityContext;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.DateUtils;
import com.kcp.platform.util.JsonParser;
import com.kcp.platform.util.StringUtils;
import com.kcp.platform.base.file.context.FileUploadContext;
import com.kcp.platform.base.file.entity.UploadFile;
import com.kcp.platform.base.file.entity.UploadSuccessResponse;
import com.kcp.platform.base.file.exception.FileUploadException;
import com.kcp.platform.base.file.service.UploadFileService;
import com.kcp.platform.base.file.util.FileUploadUtil;

@Controller
@RequestMapping("uploadFile")
public class UploadFileController extends BaseMultiActionController{
	@Autowired
	private UploadFileService uploadFileService;
	
	@Autowired
	private UserService userService;
	/**
	 * 跳转至主编辑页面
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(HttpServletRequest request){
		return new ModelAndView("/common/file/uploadFileMain");
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("uploadFileList")
	public @ResponseBody GridData uploadFileList(HttpServletRequest request){
	  	String fileName = StringUtils.getNullBlankStr(request.getParameter("fileName"));
	  	String userName = StringUtils.getNullBlankStr(request.getParameter("userName"));
	  	String startTimeStr = StringUtils.getNullBlankStr(request.getParameter("startTime"));
		Date startTime = DateUtils.parseStrint2Date(startTimeStr,"yyyy-MM-dd hh:mm:ss");
		String endTimeStr = StringUtils.getNullBlankStr(request.getParameter("endTime"));
		Date endTime = DateUtils.parseStrint2Date(endTimeStr,"yyyy-MM-dd hh:mm:ss");
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("fileName", fileName);
	 	queryMap.put("userName", userName);
	 	queryMap.put("startTime", startTime);
	 	queryMap.put("endTime", endTime);
		List<UploadFile> result = uploadFileService.findUploadFilePaging(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	
	/**
	 * 跳转至修改页面
	 */
	@RequestMapping("toUpdate")
	public ModelAndView toUpdate(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		UploadFile uploadFile = uploadFileService.getUploadFileById(id);
		if(uploadFile==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("/common/file/uploadFileEdit")
					.addObject("uploadFile",uploadFile)
					.addObject("type","update");
	}
	
	/**
	 * 修改操作
	 */
	@RequestMapping("updateUploadFile")
	public @ResponseBody void updateUploadFile(HttpServletRequest request){
		String fileId = StringUtils.getNullBlankStr(request.getParameter("fileId"));
		UploadFile uploadFile = uploadFileService.getUploadFileById(fileId);
		String remark = StringUtils.getNullBlankStr(request.getParameter("remark"));
		uploadFile.setRemark(remark);
		uploadFile.setModifyTime(new Date());
		uploadFile.setModifyUser(SecurityContext.getCurrentUser().getZjid());
		uploadFileService.updateUploadFile(uploadFile);
	}
	
	/**
	 * 删除操作
	 */
	@RequestMapping("deleteUploadFile")
	public @ResponseBody void deleteUploadFile(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		String addonBeanName = StringUtils.getNullBlankStr(request.getParameter("addonBeanName"));
		if(StringUtils.isNotEmpty(ids)){
			String[] idArr = ids.split(",");
			for(String fileId:idArr){
				FileUploadContext fileUploadContext = new FileUploadContext();
				fileUploadContext.setAddonBeanName(addonBeanName);
				UploadFile uploadFile = uploadFileService.getUploadFileById(fileId);
				if(uploadFile==null) return;
				fileUploadContext.setUploadFile(uploadFile);
				uploadFileService.deleteFile(fileUploadContext);
			}
		}
	}
	@RequestMapping("getUploadFileListById")
	public @ResponseBody List<UploadFile> getUploadFileListById(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		List<UploadFile> uploadFileList = null;
		if(StringUtils.isNotEmpty(ids)){
			uploadFileList = uploadFileService.getUploadFileListById(ids.split(","));
		}
		return uploadFileList;
	}
	/**
	 * 跳转至查看页面
	 */
	@RequestMapping("toView")
	public ModelAndView toView(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		UploadFile uploadFile = uploadFileService.getUploadFileById(id);
		if(uploadFile==null){
			throw new BusinessException("选择的记录不存在！");
		}
		User uploadUser = userService.getUserById(uploadFile.getCreator());
		User modifyUser = userService.getUserById(uploadFile.getModifyUser());
		return new ModelAndView("/common/file/uploadFileView")
					.addObject("uploadFile", uploadFile)
					.addObject("uploadUser", uploadUser)
					.addObject("modifyUser", modifyUser);
	}
	
	@RequestMapping("uploadFile")
	public @ResponseBody String uploadFile(HttpServletRequest request,HttpServletResponse response)throws Exception{
		FileUploadContext fileUploadContext = assembleFileUploadContext(request);
		boolean success = uploadFileService.uploadFile(fileUploadContext);
		if(success){
			return JsonParser.convertObjectToJson(new UploadSuccessResponse(fileUploadContext.getUploadFile().getFileId()));
		}else{
			throw new FileUploadException("文件上传失败！");
		}
	}
	
	@RequestMapping("downloadFile")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String fileId = StringUtils.getNullBlankStr(request.getParameter("fileId"));
		UploadFile uploadFile = uploadFileService.getUploadFileById(fileId);
		if(uploadFile==null){
			throw new BusinessException("文件不存在");
		}
		
		StringBuffer headerSB = new StringBuffer();
        headerSB.append("attachment; filename=");
        headerSB.append(uploadFile.getFileName());
        // 设定响应的内容类为“application/octet-stream”，大小写无关
        response.setContentType(FileUploadUtil.getResponseContentType(request, uploadFile.getFileType()));
        // 设置响应头和下载保存的文件名
        // 设置HTTP的响应头名字为：Content-Disposition，设定值为：attachment;filename得到文件名
        response.setHeader("Content-Disposition", new String(headerSB.toString().getBytes("GBK"), "ISO-8859-1"));
        OutputStream out = response.getOutputStream();
        uploadFileService.downloadFile(uploadFile, response.getOutputStream());
        out.close();
	}
	
	@RequestMapping("deleteFile")
	public void deleteFile(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String fileId = StringUtils.getNullBlankStr(request.getParameter("fileId"));
		String addonBeanName = StringUtils.getNullBlankStr(request.getParameter("addonBeanName"));
		FileUploadContext fileUploadContext = new FileUploadContext();
		fileUploadContext.setAddonBeanName(addonBeanName);
		UploadFile uploadFile = uploadFileService.getUploadFileById(fileId);
		if(uploadFile==null) return;
		fileUploadContext.setUploadFile(uploadFile);
		uploadFileService.deleteFile(fileUploadContext);
	}
	
	/**
	 * 组装FileUploadContext
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private FileUploadContext assembleFileUploadContext(HttpServletRequest request)throws IOException{
		String addonBeanName = StringUtils.getNullBlankStr(request.getParameter("addonBeanName"));
		FileUploadContext fileUploadContext = new FileUploadContext();
		fileUploadContext.setAddonBeanName(addonBeanName);
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("Filedata");
		UploadFile uploadFile = FileUploadContext.transfer2FileUpload(multipartFile);
		fileUploadContext.setUploadFile(uploadFile);
		fileUploadContext.setFileInputStream(multipartFile.getInputStream());
		return fileUploadContext;
	}
}