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
	function onMenuTreeClick(event, treeId, treeNode){
		var itemId = treeNode.zjId;
		if(itemId!="0"){
			$("#funcFrame").attr("src", "${ctx}/func/funcList.html?id="+itemId);
		}
	}
	
	
	$parsed(function(){
		$("#ws").layout("panel","center").panel("body").html("<iframe id=\"funcFrame\" name=\"funcFrame\" style=\"width:100%;height:100%;\" "+
			"src=\"${ctx}/func/funcList.html\" frameBorder=0 style=\"padding:0px;margin:0px\" ></iframe>");
	});
</script>
</head>
<body>
	<ui:LayoutContainer id="ws" fit="true">
		<ui:Layout region="west" style="width:250px;padding:1px" split="true">
			<ui:Panel fit="true" title="功能信息管理">
				<ui:LayoutContainer fit="true">
					<ui:Layout region="center" border="false"  style="overflow:auto">
						<ul id="menuTree" class="easyui-tree" asyncEnable="true"  url="${ctx}/menu/loadAllEnableMenu.html" 
							idKey="zjId" nameKey="cdmc" pIdKey="sjcd" rootPId="0" simpleDataEnable="true"
							onClick="onMenuTreeClick"></ul>
					</ui:Layout>
				</ui:LayoutContainer>
			</ui:Panel>
		</ui:Layout>
		<ui:Layout region="center" style="overflow:hidden">
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>