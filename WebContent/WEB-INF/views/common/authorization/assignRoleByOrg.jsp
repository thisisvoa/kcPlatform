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
<body>
<ui:LayoutContainer	fit="true">
	<ui:Layout region="west" split="true" style="width:220px;overflow:hidden">
		<iframe style="width:100%;height:100%" frameBorder=0 src="${ctx }/orgFunc/orgTree.html?operateType=assignRole"></iframe>
	</ui:Layout>
	<ui:Layout region="center" style="overflow:hidden">
		<iframe src="" name="roleFrame" id="roleFrame" frameBorder=0 scrolling="no" style="width:100%;height:100%;overflow:hidden"></iframe>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>