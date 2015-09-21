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
	
	function onTreeCheck(event, treeId, treeNode) {
		var checked = treeNode.checked;
		if(checked){
			var rowd = {ZJID:treeNode.id, DWMC:treeNode.name};
			$("#assignedOrgList").grid("addRowData", treeNode.id,rowd);
		}else{
			$("#assignedOrgList").grid("delRowData", treeNode.id);
		}
	}
	
	function onAsyncSuccess(event, treeId, treeNode, msg) {
		var assignedOrgs = $("#assignedOrgList").grid("getRowData");
		var treeObj = $("#orgTree").tree("getZTreeObj");
		for(var i=0;i<assignedOrgs.length;i++){
			var orgId = assignedOrgs[i].ZJID;
			var node = treeObj.getNodeByParam("id", orgId);
			if(node!=null){
				treeObj.checkNode(node,true);	
			}
		}
	}
	
	function dataFilter(event, parentNode, childNodes){
		if(!childNodes) return null;
		for(var i=0;i<childNodes.length;i++){
			childNodes[i].icon = "${ctx}/ui/css/icon/images/user_group.gif";
		}
		return childNodes;
	}
	
	function removeOrg(orgId){
		$("#assignedOrgList").grid("delRowData", orgId);
		var treeObj = $("#orgTree").tree("getZTreeObj");
		var node = treeObj.getNodeByParam("id", orgId);
		if(node!=null){
			treeObj.checkNode(node,false);	
		}
	}
		
	function operatorFormatter(cellVal,options,rowd){
		var html = "<input type='button' class='easyui-button' value='删除' style='margin:2px' onclick=\"removeOrg('"+rowd.ZJID+"')\") />";
		return html;
	}
		
	function onGridComplete(){
		using("button", function(){
			$(".easyui-button").button();	
		});
	}
	
	function save(){
		var orgs = [];
		var assignedUsers = $("#assignedOrgList").grid("getRowData");
		for(var i=0;i<assignedUsers.length;i++){
			orgs.push(assignedUsers[i].ZJID);
		}
		var orgStr = orgs.join(",");
		MessageUtil.confirm("确定将选择的单位分配给功能[${func.gnmc}]吗？",function(){
			$.postc("${ctx}/orgFunc/assignOrgToFunc.html",{funcId: $("#funcId").val(), orgs:orgStr},
				function (data){
					MessageUtil.show('成功为功能分配单位!');
				}
			);
		});
	}
	//加载完表格后再去加载树形的数据，避免由于异步而引发的数据节点没有被选中
	function loadComplete(xhr){
		var treeObj = $("#orgTree").tree("getZTreeObj");
		treeObj.setting.async.url = "${ctx}/orgCtl/asynOrgTree.html";
		treeObj.reAsyncChildNodes(null, "refresh");
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
							<legend>单位树</legend>
							<div style="width:99%;height:95%;overflow:auto;border:1px solid #8db2e3;background-color:#FFF">
								<ul id="orgTree" class="easyui-tree" asyncEnable="true"
						  				rootPId="0" simpleDataEnable="true" autoParam="['id']" chkEnable="true" chkboxType="{'Y': '', 'N': ''}"
						  				dataFilter="dataFilter" onCheck="onTreeCheck" onAsyncSuccess="onAsyncSuccess"></ul>
				  			</div>
						</fieldset>
					</td>
					<td  style="width:50%;height:100%" align="right">
						<fieldset style="width:98%;height:95%;"> 
							<legend>已分配单位</legend>
							<div style="width:100%;height:95%;">
								<ui:Grid id="assignedOrgList" datatype="json" shrinkToFit="true" fit="true" rowNum="999"
									url="${ctx}/orgFunc/getAssignedOrgList.html?funcId=${func.zjId}" gridComplete="onGridComplete" loadComplete="loadComplete">
									<ui:Column name="ZJID" key="true" hidden="true"></ui:Column>
									<ui:Column header="单位名称" name="DWMC"  width="90"></ui:Column>
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