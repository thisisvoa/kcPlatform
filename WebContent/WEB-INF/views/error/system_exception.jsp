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
	<title>错误提示</title>
</head>
<style type=text/css>
body {
	font-size: 9pt; color: #842b00; line-height: 16pt; font-family: "tahoma", "宋体"; text-decoration: none
}
table {
	font-size: 9pt; color: #842b00; line-height: 16pt; font-family: "tahoma", "宋体"; text-decoration: none
}
td {
	font-size: 9pt; color: #842b00; line-height: 16pt; font-family: "tahoma", "宋体"; text-decoration: none
}
body {
	scrollbar-highlight-color: buttonface; scrollbar-shadow-color: buttonface; scrollbar-3dlight-color: buttonhighlight; scrollbar-track-color: #eeeeee; background-color: #ffffff
}
a {
	font-size: 9pt; color: #842b00; line-height: 16pt; font-family: "tahoma", "宋体"; text-decoration: none
}
a:hover {
	font-size: 9pt; color: #0188d2; line-height: 16pt; font-family: "tahoma", "宋体"; text-decoration: underline
}
h1 {
	font-size: 9pt; font-family: "tahoma", "宋体"
}
</style>
</head>
<body topmargin=20>
	<table cellspacing=0 width="100%" align=center border=0 cepadding="0">
		<tbody>
			<tr colspan="2">
				<td valign=top align=middle style="width:100px;height:100px">
					<img height="100px" src="${ctx}/ui/css/icon/images/404.jpg"
					width="100px" border=0>
				<td>
				<td>
					<!--------system return begin------------>
					<h1>出错了!</h1> ${responseError.errorMessage}
					<hr noshade size=0>
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>