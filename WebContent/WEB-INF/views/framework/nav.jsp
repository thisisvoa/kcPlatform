<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String basePath2 = "https://"+request.getServerName()+":8643"+path+"/";
%>
<script type="text/javascript">
var requestURL = "${requestURL}";
if(requestURL == null || requestURL == '')
{
	requestURL = "<%=basePath%>toMain.html";	
}
window.location.href=requestURL;
</script>