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
			}else{
				$("#assignedUserList").grid("delRowData", treeNode.userId);
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

	function removeUser(userId){
		$("#assignedUserList").grid("delRowData", userId);
		var treeObj = $("#orgUserTree").tree("getZTreeObj");
		var node = treeObj.getNodeByParam("userId", userId);
		if(node!=null){
			treeObj.checkNode(node,false);	
		}
	}
		
	function operatorFormatter(cellVal,options,rowd){
		var html = "<input type='button' class='easyui-button' value='删除' style='margin:2px' onclick=\"removeUser('"+rowd.ZJID+"')\") />";
		return html;
	}
		
	function onGridComplete(){
		using("button", function(){
			$(".easyui-button").button();	
		});
	}	
	function save(){
		var users = [];
		var assignedUsers = $("#assignedUserList").grid("getRowData");
		for(var i=0;i<assignedUsers.length;i++){
			users.push(assignedUsers[i].ZJID);
		}
		var userStr = users.join(",");
		MessageUtil.confirm("确定将选择的用户分配给功能[${func.gnmc}]吗？",function(){
			$.postc("${ctx}/userFunc/assignUserToFunc.html",{funcId: $("#funcId").val(), users:userStr},
					function (data){
						MessageUtil.show('成功为用户分配功能！');
					}
			);
		});
	}

</script>
</head>
<body>
	<ui:LayoutContainer fit="true">
		<ui:Layout region="north" border="false" style="height:30px">
			<div style="padding-top:8px">
				<c:forEach var="item" items="${parentMenuList}" varStatus="status"  step="1">  
		       		${item.cdmc}&gt;&gt;
				</c:forEach>
				${func.gnmc}&nbsp;
			</div>
		</ui:Layout>
		<ui:Layout region="center" style="background-color:#F8F8F8">
			<table style="width:100%;height:99%">
				<tr>
					<td style="width:50%;height:100%" align="left">
						<fieldset style="width:98%;height:95%;padding:1px"> 
							<legend>用户树</legend>
							<div style="width:99%;height:95%;overflow:auto;border:1px solid #8db2e3;background-color:#FFF">
								<ul id="orgUserTree" class="easyui-tree" asyncEnable="true"  url="${ctx}/userFunc/orgUserTreeJson.html" 
					  				rootPId="0" simpleDataEnable="true" autoParam="['id']" chkEnable="true" chkboxType="{'Y': '', 'N': ''}"
					  				dataFilter="dataFilter" onCheck="onTreeCheck" onAsyncSuccess="onAsyncSuccess" beforeCheck="beforeTreeCheck"></ul>
				  			</div>
						</fieldset>
					</td>
					<td  style="width:50%;height:100%" align="right">
						<fieldset style="width:98%;height:95%;"> 
							<legend>已分配用户</legend>
							<div style="width:100%;height:95%;">
								<ui:Grid id="assignedUserList" datatype="json" shrinkToFit="true" fit="true" rowNum="999"
									url="${ctx}/userFunc/getAssignedUserList.html?funcId=${func.zjId}" gridComplete="onGridComplete">
									<ui:Column name="ZJID" key="true" hidden="true"></ui:Column>
									<ui:Column header="姓名" name="YHMC" width="40"></ui:Column>
									<ui:Column header="所属单位" name="DWMC"  width="90"></ui:Column>
									<ui:Column header="操作" width="40" formatter="operatorFormatter" align="center"></ui:Column>
								</ui:Grid>
							</div> 
						</fieldset>	
					
					</td>
				</tr>
			</table>
		</ui:Layout>
		<ui:Layout region="south" border="false" style="height:30px;text-align:center">
			<input id="funcId" type="hidden" value="${func.zjId}"/>
			<input type="button" class="easyui-button" style="margin-top:3px" value="保存" onclick="save()" />  
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>