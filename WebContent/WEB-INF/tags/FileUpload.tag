<!-- 
	文件上传标签
 -->
<%@tag import="com.kcp.platform.util.StringUtils"%>
<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="id" type="java.lang.String" required="true"  description="控件ID"%>
<%@ attribute name="name" type="java.lang.String" required="true"  description="名称"%>
<%@ attribute name="value" type="java.lang.String" required="false"  description="值"%>
<%@ attribute name="multiple" type="java.lang.Boolean" required="false" description="是否支持多个文件上传"%>
<%@ attribute name="flashUrl" type="java.lang.String" required="false" description="控件所在位置"%>
<%@ attribute name="uploadUrl" type="java.lang.String" required="false" description="处理上传文件的地址"%>
<%@ attribute name="deleteUrl" type="java.lang.String" required="false" description="删除上传文件的地址"%>
<%@ attribute name="initUrl" type="java.lang.String" required="false" description="初始化已上传数据URL"%>
<%@ attribute name="postParams" type="java.lang.String" required="false" description="传递的参数JSON，类型的参数"%>
<%@ attribute name="fileSizeLimit" type="java.lang.Integer" required="false" description="文件大小限制，单位为KB，0表示无限制"%>
<%@ attribute name="fileUploadLimit" type="java.lang.Integer" required="false" description="允许上传的最多文件数量,0表示无限制"%>
<%@ attribute name="fileTypes" type="java.lang.String" required="false" description="可上传的文件类型，“如*.jpg;*.gif”，用分号隔开"%>
<%@ attribute name="fileTypesDescription" type="java.lang.String" required="false" description="可上传的文件类型描述文字"%>
<%@ attribute name="uploadStart" type="java.lang.String" required="false" description="单文件上传开始时候触发"%>
<%@ attribute name="uploadComplete" type="java.lang.String" required="false" description="单文件上传完成后触发"%>
<%@ attribute name="allUploadComplete" type="java.lang.String" required="false" description="所有文件上传完成后触发"%>
<%@ attribute name="uploadSuccess" type="java.lang.String" required="false" description="单文件上传成功后触发"%>
<%@ attribute name="allUploadSuccess" type="java.lang.String" required="false" description="所有文件上传成功后触发"%>
<%@ attribute name="addonBeanName" type="java.lang.String" required="false" description="插件Bean名称"%>
<%
	jspContext.setAttribute("id", id);
	jspContext.setAttribute("name", name);
	jspContext.setAttribute("value", value);
	jspContext.setAttribute("multiple", multiple);
	String ctx = request.getContextPath();
	jspContext.setAttribute("ctx", ctx);
	jspContext.setAttribute("postParams", postParams);
	jspContext.setAttribute("fileSizeLimit", fileSizeLimit);
	jspContext.setAttribute("fileUploadLimit", fileUploadLimit);
	jspContext.setAttribute("fileTypes", fileTypes);
	jspContext.setAttribute("fileTypesDescription", fileTypesDescription);
	jspContext.setAttribute("uploadStart", uploadStart);
	jspContext.setAttribute("uploadComplete", uploadComplete);
	jspContext.setAttribute("allUploadComplete", allUploadComplete);
	jspContext.setAttribute("uploadSuccess", uploadSuccess);
	jspContext.setAttribute("allUploadSuccess", allUploadSuccess);
	jspContext.setAttribute("addonBeanName", addonBeanName);
	if(StringUtils.isEmpty(flashUrl)){
		jspContext.setAttribute("flashUrl", ctx+"/ui/js/swfupload/swfupload_fp10/swfupload.swf;jsessionid="+session.getId());
	}else{
		jspContext.setAttribute("flashUrl",flashUrl);
	}
	if(StringUtils.isEmpty(uploadUrl)){
		jspContext.setAttribute("uploadUrl", ctx+"/uploadFile/uploadFile.html;jsessionid="+session.getId());
	}else{
		jspContext.setAttribute("uploadUrl",uploadUrl);
	}
	if(StringUtils.isEmpty(deleteUrl)){
		jspContext.setAttribute("deleteUrl", ctx+"/uploadFile/deleteFile.html");
	}else{
		jspContext.setAttribute("deleteUrl",deleteUrl);
	}
	if(StringUtils.isEmpty(initUrl)){
		jspContext.setAttribute("initUrl", ctx+"/uploadFile/getUploadFileListById.html");
	}else{
		jspContext.setAttribute("initUrl",initUrl);
	}
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="${id}" name="${name}" class="easyui-fileupload" 
		flash_url="${flashUrl}" 
		upload_url="${uploadUrl}" 
		delete_url="${deleteUrl}" 
		init_url="${initUrl}" 
		value="${value}"
		<c:if test="${multiple}">multiple="${multiple}"</c:if>
		<c:if test="${postParams!=null && postParams!=''}">post_params="${postParams}"</c:if>
		<c:if test="${fileSizeLimit>0}">file_size_limit="${fileSizeLimit}"</c:if>
		<c:if test="${fileUploadLimit>0}">file_upload_limit="${fileUploadLimit}"</c:if>
		<c:if test="${fileTypes!=null && fileTypes!=''}">file_types="${fileTypes}"</c:if>
		<c:if test="${fileTypesDescription!=null && fileTypesDescription!=''}">file_types_description="${fileTypesDescription}"</c:if>
		<c:if test="${uploadStart!=null && uploadStart!=''}">uploadStart="${uploadStart}"</c:if>
		<c:if test="${uploadComplete!=null && uploadComplete!=''}">uploadComplete="${uploadComplete}"</c:if>
		<c:if test="${uploadSuccess!=null && uploadSuccess!=''}">uploadSuccess="${uploadSuccess}"</c:if>
		<c:if test="${allUploadSuccess!=null && allUploadSuccess!=''}">allUploadSuccess="${allUploadSuccess}"</c:if>
		<c:if test="${addonBeanName!=null && addonBeanName!=''}">addonBeanName="${addonBeanName}"</c:if>
>
</div>
