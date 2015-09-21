<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="/tags/ui"%>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"><%@ include
	file="/WEB-INF/includes/common.jsp"%>

<script type="text/javascript">
		var funcTree;
		function save(){
			var roleId = $("#roleId").val();
			if(roleId==""){
				MessageUtil.alert("请选择授权的角色！");
				return;
			}
			var funcTree = $("#funcTree").tree("getZTreeObj");
			var checkItems = funcTree.getCheckedNodes(true);
			
			var selFunc = [];
			for(var i=0;i<checkItems.length;i++){
				if(checkItems[i].type == "FUNC"){
					selFunc.push(checkItems[i].id);
				}
			}
			var funcStr = selFunc.join(",");
			MessageUtil.confirm("确定将选择的功能分配给[${role.jsmc}]吗？",function(){
				$.postc("${ctx}/roleFunc/assignFuncToRole.html",{roleId: roleId, funcs:funcStr},
						function (data){
							MessageUtil.show('成功为角色【${role.jsmc}】授权！');
						}
				);
			});
			
		}
		
		function onAsyncSuccess(event, treeId, treeNode, msg){
			var treeObj = $("#funcTree").tree("getZTreeObj");
			treeObj.expandAll(true);
		}
	</script>
</head>
<body style="padding:1px;">
	<ui:LayoutContainer fit="true">
		<ui:Layout region="north" style="height:30px;text-align:center;background-color:#F8F8F8" border="false">
			<input id="roleId" type="hidden" value='${role.zjid}' />
			<input type="button" class="easyui-button" value="保存" style="margin-top:3px" onclick="save()">
		</ui:Layout>
		
		<ui:Layout region="center">
			<div style="width: 100%;height:100%;overflow:auto;">
			<ul id="funcTree" class="easyui-tree" asyncEnable="true"  url="${ctx}/roleFunc/funcTreeJson4Role.html?roleId=${role.zjid}" chkEnable="true"
  				rootPId="0" simpleDataEnable="true" onAsyncSuccess="onAsyncSuccess"
  				></ul>
		</div>
		</ui:Layout>
		<ui:Layout region="south" style="height:30px;padding-top:5px;background-color:#F8F8F8" border="false">
			<div class="icon-qbzc" style="width:16px;height:16px;float:left"></div>
			<div>
				已选角色：<b>${role.jsmc}</b>
			</div>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>