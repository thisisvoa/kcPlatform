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
	var topWin = FrameHelper.getTop();
	//新增单位信息
	function addOrg() {
		var treeObj = $("#orgTree").tree("getZTreeObj");
		var selectedNodes = treeObj.getSelectedNodes();
		if(selectedNodes.length>0){
			var selctedNode = selectedNodes[0];
			var itemId = selectedNodes[0].id;
			topWin.Dialog.open({
				URL : "${ctx}/orgCtl/toAddOrg.html?pOrgId=" + itemId,
				ShowCloseButton : true,
				Width : 500,
				Height : 450,
				Title : "新增单位",
				RefreshHandler:function(){
					treeObj.reAsyncChildNodes(selctedNode, "refresh");
				}
			});
		}else{
			MessageUtil.alert("请选择所属单位!");
		}
		
	}
	
	function refreshSelectNode(){
		var treeObj = $("#orgTree").tree("getZTreeObj");
		var selectedNodes = treeObj.getSelectedNodes();
		var selctedNode = null;
		if(selectedNodes.length>0){
			selctedNode = selectedNodes[0];
		}
		treeObj.reAsyncChildNodes(selctedNode, "refresh");
	}
	function onOrgTreeClick(event, treeId, treeNode){
		var id = treeNode.id;
		if (id == null || id == "") {
			MessageUtil.alter("请选择要操作的单位");
			return;
		}
		parent.frames["orgInfoList"].document.location.href = "${ctx}/orgCtl/toOrgInfoList.html?parentOrg="
			+ id + "&" + (new Date()).getTime();
	}
	
	function dataFilter(event, parentNode, childNodes){
		if(!childNodes) return null;
		for(var i=0;i<childNodes.length;i++){
			childNodes[i].icon = "${ctx}/ui/css/icon/images/user_group.gif";
		}
		return childNodes;
	}
</script>
</head>
<body>
	<ui:Panel fit="true" title="单位树">
		<ui:LayoutContainer fit="true">
			<ui:Layout region="north" style="height:33px;" border="false">
				<div id="toolbar" class="easyui-toolbar" style="width:99.5%">
					<kpm:HasPermission permCode="SYS_ORG_ADD">
						<a id="add" href="#" iconCls="icon-add" onclick="addOrg()">新增</a>
					</kpm:HasPermission>
				</div>
			</ui:Layout>
			<ui:Layout region="center" border="false">
				<ul id="orgTree" class="easyui-tree" asyncEnable="true"  url="${ctx}/orgCtl/asynOrgTree.html" 
	  				rootPId="0" simpleDataEnable="true" autoParam="['id']"
	  				onClick="onOrgTreeClick" dataFilter="dataFilter"></ul>
			</ui:Layout>
		</ui:LayoutContainer>
			
	</ui:Panel>
</body>
</html>
