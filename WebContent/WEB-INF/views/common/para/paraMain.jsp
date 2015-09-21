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
<script type="text/javascript">
function refresh(){
	if($("#tab1").find("iframe")[0]!=null){
		$("#tab1").find("iframe")[0].contentWindow.refreshParamList();
	};
	if($("#tab2").find("iframe")[0]!=null){
		$("#tab2").find("iframe")[0].contentWindow.refreshParamList();
	};
	if($("#tab3").find("iframe")[0]!=null){
		$("#tab3").find("iframe")[0].contentWindow.refreshParamList();
	};
	
}
</script>
</head>
	<div id="tt" class="easyui-tabs" fit="true" border="false">
		<div id="tab1" style="overflow:hidden" title="全部" iconCls="icon-tab" url="${ctx}/para/toParaList.html?id=${id}&res=">
		</div>
		<div id="tab2" style="overflow:hidden" title="启用" iconCls="icon-tab" url="${ctx}/para/toParaList.html?id=${id}&res=1">
		</div>
		<div id="tab3" style="overflow:hidden" title="禁用" iconCls="icon-tab" url="${ctx}/para/toParaList.html?id=${id}&res=0">
		</div>
	</div>
</html>