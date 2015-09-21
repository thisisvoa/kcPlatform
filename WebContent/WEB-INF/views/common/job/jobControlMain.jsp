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
	var data = [{id:"0",name:"任务调度控制台",icon:"${ctx}/ui/css/icon/images/network.gif"},
	            {id:"jobScheduler",name:"调度服务器",icon:"${ctx}/ui/css/icon/images/server.gif"
	            	,url:"${ctx}/job/toJobSchedulerMain.html",target:"ws", pId:"0"},
	            {id:"job",name:"任务",icon:"${ctx}/ui/css/icon/images/clock.gif"
	            	,url:"${ctx}/job/toJobMain.html",target:"ws", pId:"0"},
	            {id:"jobRunlog",name:"运行日志",icon:"${ctx}/ui/css/icon/images/history.gif"
	            	,url:"${ctx}/job/toJobRunLog.html",target:"ws", pId:"0"}
	            ];
	
	function onNodeCreated(event, treeId, treeNode){
		if(treeNode.id=="0"){
			var treeObj = $("#navTree").tree("getZTreeObj");
			treeObj.expandAll(true);
		}
	}

</script>
</head>
<body>
	<ui:LayoutContainer fit="true">
		<ui:Layout region="west" style="width:150px" split="true">
			<ul id="navTree"  class="easyui-tree" data="data" simpleDataEnable="true" onNodeCreated="onNodeCreated"></ul>
		</ui:Layout>
		<ui:Layout region="center" style="overflow:hidden">
			<iframe id="ws" name="ws" style="width:100%;height:100%" frameBorder=0 src="${ctx}/job/toJobSchedulerMain.html"></iframe>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>