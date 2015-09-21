<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="/tags/ui"%>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/flex/history/history.css" />
<script type="text/javascript" src="${ctx}/flex/swfobject.js"></script>
<script type="text/javascript">
	function addCallBack(){
		if(window.opener!=null){
			window.opener.refreshGrid();
			window.close();
		}
	}
	
	function updateCallBack(){
		if(window.opener!=null){
			window.opener.refreshGrid();
			window.close();
		}
	}
</script>
<title>
	在线流程设计<c:if test="${bpmDefinition!=null}">-${bpmDefinition.subject}</c:if>
</title>
</head>
<body>
	<kpm:FlexSwfLoader filePath="/flex/bpm/bpmeditor.swf" id="bpmeditor" params="defId=${bpmDefinition.defId}" width="100%" height="100%"></kpm:FlexSwfLoader>
</body>
