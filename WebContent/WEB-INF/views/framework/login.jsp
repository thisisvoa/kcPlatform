<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="/tags/ui" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String basePath2 = "https://"+request.getServerName()+":8643"+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>基础开发平台V1.0</title>
<link rel="stylesheet" type="text/css" href="${ctx}/ui/css/default/login_style.css">
<script type="text/javascript" src="${ctx}/ui/jquery-1.7.1.min.js"></script>
<script>
	$(function(){ 
		var isCas = '${isCas}';
	});
	function pkiLogin(){
		location.href="<%=basePath2%>pkilogin.html?service=<%=basePath%>toMain.html";
	}
</script>
</head>
<body class="body">
	<div class="bg">
		<form method="post" action="${ctx}/login.html" name="login" id="form1">
		<input type="hidden" name="isCas" value="${isCas}" />
		<div style="padding:152px;"></div>
		<div style="height:100px; width:350px; float:left;"></div>
		<div class="login_form">
			<div class="login_user"><input name="username" id="username" type="text" style="margin-top:5px; line-height:25px;" /></div> 
			<div class="login_user"><input name="password" id="password" type="password" style="line-height:25px;"/></div>
			<div><span style="color:red;font-size:12px">${login_fail_msg}</span></div>
		</div>
		<input type="submit" name="sig" value="登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录" class="bt"  onfocus="this.blur();"/>
		<input type="button"  value="数字证书登录" class="bt" onclick="pkiLogin()"/>
		
	   </form>
	</div>
</body>

</html>
