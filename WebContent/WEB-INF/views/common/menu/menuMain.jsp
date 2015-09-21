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
<title>菜单信息管理</title>
<script type="text/javascript">
	var selectNode = null;
	var topWin = FrameHelper.getTop();
	//新增菜单信息
	function addMenu(){
		var treeObj = $("#menuTree").tree("getZTreeObj");
		var selectedNodes = treeObj.getSelectedNodes();
		if(selectedNodes!=null && selectedNodes.length>0){
			var itemId = selectedNodes[0].zjId;
			var sybz = selectedNodes[0].sybz;
			var sfzhyicd = selectedNodes[0].sfzhyicd;
			if("0" == sybz){
				MessageUtil.alert("该菜单已禁用，不能在其下添加子菜单!");
				return;
			}
			if("1" == sfzhyicd){
				MessageUtil.alert("该菜单是最后一级菜单，不能在其下添加子菜单!");
				return;
			}
			var diag = new topWin.Dialog();
			diag.Title = "新增菜单";
			diag.URL = '${ctx}/menu/menuAdd.html?parentId='+itemId;
			diag.Width = 600;
			diag.Height = 400;
			diag.RefreshHandler = function(){
				var selectNodes = treeObj.getSelectedNodes();
				if(selectNodes.length>0){
					selectNode = selectNodes[0];
				}
				treeObj.reAsyncChildNodes(null, "refresh");
			};
			diag.show();
		}else{
			MessageUtil.alert("请选择父菜单!");
		}
	}
	
	
	function delMenu(){
		var treeObj = $("#menuTree").tree("getZTreeObj");
		var selectedNodes = treeObj.getSelectedNodes();
		if(selectedNodes!=null && selectedNodes.length>0){
			var id = selectedNodes[0].zjId;
			MessageUtil.confirm("确定删除该菜单及其下级菜单吗?",function(){
				$.postc("${ctx}/menu/delMenu.html",{id:id}, function(){
					MessageUtil.show("删除成功!");
					treeObj.reAsyncChildNodes(null, "refresh");
				});
			});
		}else{
			MessageUtil.alert("请选择菜单!");
		}
		
	}
	//树节点单击
	function onMenuTreeClick(event, treeId, treeNode){
		var itemId = treeNode.zjId;
		if(itemId!="0"){
			$("#menuDetail").attr("src","${ctx}/menu/menuEdit.html?zjId="+itemId);
		}
	}
	
	function onAsyncSuccess(event, treeId, treeNode, msg){
		var treeObj = $("#menuTree").tree("getZTreeObj");
		treeObj.expandAll(true);
		if(selectNode){
			var node = treeObj.getNodeByParam("zjId", selectNode.zjId);
			treeObj.selectNode(node);
		}
	}
	function queryEnter(e){
		if(e == 13){
			$("#queryBtn").click();	
		}
	}
	
	function exportAuth(){
		topWin.Dialog.open({
			URL : "${ctx}/menu/menuAuthExport.html",
			ShowCloseButton : true,
			Width: 600,
			Height : 500,
			Title : "菜单权限导出"
		});
	}
</script>
</head>
<body style="padding:1px" onkeydown="queryEnter(event.keyCode||event.which)">

	<ui:LayoutContainer fit="true" id="outerContainer">
		<ui:Layout region="west" style="width:250px;padding:1px" split="true">
			<ui:Panel fit="true" title="菜单树" id="treePanel">
			  	<ui:LayoutContainer fit="true" id="innerContainer" >
					<ui:Layout region="north" style="height:32px" border="false">
				 		<div id="toolbar" class="easyui-toolbar">
							<kpm:HasPermission permCode="SYS_MENU_ADD">
								<a id="add" href="#" iconCls="icon-add" onclick="addMenu()">新增</a>
							</kpm:HasPermission>
							<kpm:HasPermission permCode="SYS_MENU_DEL">
								<a id="del" href="#" iconCls="icon-remove" onclick="delMenu()">删除</a>
							</kpm:HasPermission>
							<kpm:HasPermission permCode="SYS_MENU_AUTH_EXPORT">
								<a id="export" href="#" iconCls="icon-xls" onclick="exportAuth()">导出权限</a>
							</kpm:HasPermission>
						</div>
					</ui:Layout>
					<ui:Layout region="center" border="false" style="overflow:auto" id="treeLayout">
						<ul id="menuTree" class="easyui-tree" asyncEnable="true"  url="${ctx}/menu/loadAllMenu.html" 
			  				idKey="zjId" nameKey="cdmc" pIdKey="sjcd" rootPId="0" simpleDataEnable="true"
			  				onClick="onMenuTreeClick" onAsyncSuccess="onAsyncSuccess" ></ul>
					</ui:Layout>
					<input type="hidden" id="test" value="a"/>
				</ui:LayoutContainer>
		  	</ui:Panel>
		</ui:Layout>
		<ui:Layout region="center" style="overflow:hidden">
			<iframe id="menuDetail" style="width:100%;height:100%" name="menuDetail" frameBorder=0 src="" style="padding:0px;margin:0px" >
			</iframe>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>
