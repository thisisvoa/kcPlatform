<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% 
response.setHeader("Pragma","No-cache"); 
response.setHeader("Cache-Control","no-cache"); 
response.setDateHeader("Expires", 0); 
%> 
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<link rel="stylesheet" type="text/css" href="${ctx}/ui/css/frame.css">
<link rel="stylesheet" type="text/css" href="${ctx}/ui/css/default/import.css">
<link rel="stylesheet" type="text/css" href="${ctx}/ui/css/icon/icon.css">

<script type="text/javascript" src="${ctx}/ui/dynamic.jsp"></script>
<script type="text/javascript" src="${ctx}/ui/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="${ctx}/ui/js/util/Utils.js"></script>
<script type="text/javascript" src="${ctx}/ui/easyloader.js"></script>
<script type="text/javascript" src="${ctx}/ui/pageHelper.js"></script>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
<META HTTP-EQUIV="Expires" CONTENT="0">
<style>
	/**
	 * 由于页面是才进行渲染，故一开始让页面不显示，待渲染完成后再显示(jquery.parser.js的onComplete方法中实现)
	 */
	body{
		visibility:hidden
	}
</style>
