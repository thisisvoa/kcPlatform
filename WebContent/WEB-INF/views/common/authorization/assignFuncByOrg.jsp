<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="/tags/ui" %>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<frameset cols="250px,*" style="width:100%;height:100%;padding:0px;margin:0px">
	<frame src="${ctx }/orgFunc/orgTree.html?operateType=assignFunc" name="tree" bordercolor="blue" scrolling="no">
	<frame src="" name="funcFrame" id="funcFrame" style="padding:0px;margin:0px" scrolling="no">
</frameset>
</html>