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
<script>
	/**
	 * 树形节点是单位，不允许选择
	 */
	function beforeTreeCheck(treeId, treeNode)  {
		var type = treeNode.type;
		if(type=="user"){
			return true;	
		}else{
			return false;
		}
	}
	
	function onTreeCheck(event, treeId, treeNode) {
		var type = treeNode.type;
		if(type=="user"){
			var checked = treeNode.checked;
			if(checked){
				var rowd = {ZJID:treeNode.userId, YHMC:treeNode.name, DWMC:treeNode.getParentNode().name};
				$("#assignedUserList").grid("addRowData", treeNode.userId,rowd);
				if($("#userList").grid("getRowData", treeNode.userId)!=null){
					$("#userList").grid("setSelection", treeNode.userId,false);	
				}
			}else{
				$("#assignedUserList").grid("delRowData", treeNode.userId);
				if($("#userList").grid("getRowData", treeNode.userId)!=null){
					$("#userList").grid("setSelection", treeNode.userId,false);	
				}
			}
		}
	}
	
	function onAsyncSuccess(event, treeId, treeNode, msg) {
		var assignedUsers = $("#assignedUserList").grid("getRowData");
		var treeObj = $("#orgUserTree").tree("getZTreeObj");
		for(var i=0;i<assignedUsers.length;i++){
			var userId = assignedUsers[i].ZJID;
			var node = treeObj.getNodeByParam("userId", userId);
			if(node!=null){
				treeObj.checkNode(node,true);	
			}
		}
	}
	
	/**
	 *第一次加载时候展开第一层节点
	 */
	function onNodeCreated(event, treeId, treeNode){
		if(treeNode.pId=="0"){
			var treeObj = $("#orgUserTree").tree("getZTreeObj");
			if(treeNode.children==null){
				treeObj.reAsyncChildNodes(treeNode, "refresh");	
			}else{
			}
		}
	}
	
	function save(){
		var assignedUsers = $("#assignedUserList").grid("getDataIDs");
		var userStr = assignedUsers.join(",");
		MessageUtil.confirm("确定将选择的用户分配给角色[${role.jsmc}]吗？",function(){
			$.postc("${ctx}/userRole/assignUserToRole.html",{roleId: $("#roleId").val(), users:userStr},
				function (data){
					MessageUtil.show('成功为角色分配用户！');
				}
			);
		});
	}
	/**
	 * 加上图标
	 */
	function dataFilter(event, parentNode, childNodes){
		if(!childNodes) return null;
		for(var i=0;i<childNodes.length;i++){
			var id = childNodes[i].id;
			var type = childNodes[i].type;
			if(type=="org"){
				childNodes[i].icon = "${ctx}/ui/css/icon/images/user_group.gif";
			}else{
				childNodes[i].icon = "${ctx}/ui/css/icon/images/user.gif";
			}
		}
		return childNodes;
	}
	
	function operatorFormatter(cellVal,options,rowd){
		var html = "<a href=\"javascript:void(0)\" onclick=\"removeUser('"+rowd.ZJID+"')\" ><img src='${ctx}/ui/css/icon/images/delete.gif' style='border:none'></img></a>";
		return html;
	}
	
	function removeUser(userId){
		MessageUtil.confirm("确定将此用户从当前角色移除吗？",function(){
			$("#assignedUserList").grid("delRowData", userId);
			var treeObj = $("#orgUserTree").tree("getZTreeObj");
			var node = treeObj.getNodeByParam("userId", userId);
			if(node!=null){
				treeObj.checkNode(node,false);
			}
			if($("#userList").grid("getRowData", userId)!=null){
				$("#userList").grid("setSelection", userId,false);	
			}
		});	
	}
	
	
	/**
	 * 查询
	 */
	function queryUserList() {
		$("#userList").grid("setGridParam", {postData:{userName:$("#userName").val(), userNum:$("#userNum").val()}});
		$("#userList").trigger("reloadGrid");
	}
	
	function queryEnter(e){
		if(e == 13){
			$("#queryBtn").click();	
		}
	}
	
	function onAssignedLoadComplete(xhr){
		var treeObj = $("#orgUserTree").tree("getZTreeObj");
		treeObj.setting.async.url = "${ctx}/userFunc/orgUserTreeJson.html";
		treeObj.reAsyncChildNodes(null, "refresh");

		$("#userList").grid("setGridParam", {url:"${ctx}/userCtl/userList.html?useTarg=1"});
		$("#userList").trigger("reloadGrid");
	}
	
	function userListGridComplete(){
		var dataIDs = $("#userList").grid("getDataIDs");
		var assignedIds = $("#assignedUserList").grid("getDataIDs");
		for(var i=0;i<dataIDs.length;i++){
			var isExist = false;
			for(var j=0;j<assignedIds.length;j++){
				if(dataIDs[i]==assignedIds[j]){
					isExist = true;
					break;
				}
			}
			if(isExist){
				$("#userList").grid("setSelection",dataIDs[i],false);
			}
		}
	}
	
	function onSelectRow(rowid,status){
		if(status){
			var rowd = $("#userList").grid("getRowData", rowid);
			$("#assignedUserList").grid("addRowData", rowid, rowd);
		}else{
			if($("#assignedUserList").grid("getRowData", rowid)!=null){
				$("#assignedUserList").grid("delRowData", rowid);
			}
		}
	}
	
	function onSelectAll(rowids,status){
		if(status){
			for(var i=0;i<rowids.length;i++){
				var rowid = rowids[i];
				if($("#assignedUserList").grid("getInd", rowid)==false){
					var rowd = $("#userList").grid("getRowData", rowid);
					$("#assignedUserList").grid("addRowData", rowid, rowd);	
				}
			}
		}else{
			for(var i=0;i<rowids.length;i++){
				var rowid = rowids[i];
				if($("#assignedUserList").grid("getRowData", rowid)!=null){
					$("#assignedUserList").grid("delRowData", rowid);
				}
			}
		}
	}
</script>
</head>
<body>
	<ui:LayoutContainer fit="true">
		<ui:Layout region="north" border="false" style="height:30px;text-align:center;background-color:#F8F8F8">
			<input id="roleId" type="hidden" value="${role.zjid}"/>
			<input type="button" class="easyui-button" style="margin-top:3px" value="保存" onclick="save()" />
		</ui:Layout>
		<ui:Layout region="center">
			<ui:TabNavigator fit="true" border="false">
				<ui:Tab id="tab2" title="列表选择">
					<ui:LayoutContainer fit="true">
						<ui:Layout region="north" style="height:65px">
							<table class="formTable">
								<tr>
									<td class="label" style="width:100px">
										警员编号：
									</td>
									<td colspan="2">
										<input type="text" class="easyui-textinput" id="userNum" name="userNum" watermark="支持模糊查询" title="按下回车键可触发查询" onkeydown="queryEnter(event.keyCode||event.which)"/>
									</td>
								</tr>
								<tr>
									<td class="label" style="width:100px">
										用户姓名：
									</td>
									<td style="width:135px;">
										<input type="text" class="easyui-textinput" id="userName" name="userName" watermark="支持模糊查询" title="按下回车键可触发查询" onkeydown="queryEnter(event.keyCode||event.which)"/>
									</td>
									<td align="left">
										<input id="queryBtn" type="button" class="easyui-button" value="查询" onclick="queryUserList()">
									</td>
								</tr>
							</table>
						</ui:Layout>
						<ui:Layout region="center" style="padding-top:2px" border="false">
							<ui:Grid id="userList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
									multiselect="true" multiboxonly="false" gridComplete="userListGridComplete"
									rowNum="20" rowList="[10,20,50]" onSelectRow="onSelectRow" onSelectAll="onSelectAll">
									<ui:Column name="ZJID" key="true" hidden="true"></ui:Column>
									<ui:Column header="警员编号" name="JYBH" width="100" fixed="true"></ui:Column>
									<ui:Column header="用户姓名" name="YHMC" width="90" fixed="true"></ui:Column>
									<ui:Column header="所在单位" name="DWMC"  width="180" fixed="true"></ui:Column>
							</ui:Grid>
						</ui:Layout>
					</ui:LayoutContainer>
				</ui:Tab>
				<ui:Tab id="tab1" title="树形选择">
					<ul id="orgUserTree" class="easyui-tree" asyncEnable="true"  url="" 
					  				rootPId="0" simpleDataEnable="true" autoParam="['id']" chkEnable="true" chkboxType="{'Y': '', 'N': ''}"
					  				dataFilter="dataFilter" onCheck="onTreeCheck"  onNodeCreated="onNodeCreated"
					  				onAsyncSuccess="onAsyncSuccess" beforeCheck="beforeTreeCheck"></ul>
				</ui:Tab>
			</ui:TabNavigator>
		</ui:Layout>
		<ui:Layout region="east" style="width:200px;padding-top:2px;padding-left:1px" split="true" title="已分配用户">
			<ui:Grid id="assignedUserList" datatype="json" shrinkToFit="true" fit="true" rowNum="999" 
				url="${ctx}/userRole/getAssignedUserList.html?roleId=${role.zjid}" loadComplete="onAssignedLoadComplete">
				<ui:Column name="ZJID" key="true" hidden="true"></ui:Column>
				<ui:Column header="姓名" name="YHMC" width="120"></ui:Column>
				<ui:Column header="操作" width="40" formatter="operatorFormatter" align="center" fixed="true"></ui:Column>
			</ui:Grid>
		</ui:Layout>
		<ui:Layout region="south" style="height:30px;padding-top:5px;background-color:#F8F8F8" border="false">
				<div class="icon-qbzc" style="width:16px;height:16px;float:left"></div>已选角色：<b>${role.jsmc}</b>
		</ui:Layout>
	</ui:LayoutContainer>
	
</body>
</html>