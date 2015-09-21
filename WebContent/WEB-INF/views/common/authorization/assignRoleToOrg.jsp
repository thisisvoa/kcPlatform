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
	function save(){
		var roles = $("#assignedRoleList").grid("getDataIDs");
		var checkRoles = roles.join(",");
		MessageUtil.confirm("确定将选择的角色分配给单位吗？",function(){
			$.postc("${ctx}/orgRole/assignRoleToOrg.html",{orgId: "${orgId}", roles:checkRoles},
					function (data){
						MessageUtil.show('成功为单位分配角色！');
					}
			);
		});
	}
	
	function queryRoleList(){
		$("#roleList").grid("setGridParam", {postData:{roleName:$("#roleName").val()}});
		$("#roleList").trigger("reloadGrid");
	}
	
	function onSelectRow(rowid,status){
		if(status){
			var rowd = $("#roleList").grid("getRowData",rowid);
			var assignRow = {ZJID:rowd.zjid,JSMC:rowd.jsmc};
			$("#assignedRoleList").grid("addRowData",rowd.zjid,assignRow);
		}else{
			$("#assignedRoleList").grid("delRowData",rowid);
		}
	}
	
	function operationFormatter(cellVal,opts,rowd){
		var html = "<a href=\"javascript:void(0)\" onclick=\"unCheck('"+rowd.ZJID+"')\" ><img src='${ctx}/ui/css/icon/images/delete.gif' style='border:none'></img></a>";
		return html;
	}
	
	/**
	 * 从以分配列表中删除
	 */
	function unCheck(zjid){
		$("#assignedRoleList").grid("delRowData",zjid);
		if($("#roleList").grid("getRowData", zjid)!=null){
			$("#roleList").grid("setSelection", zjid,false);	
		}
	}
	/**
	 * 加载完进行选中
	 */
	function roleListGridComplete(){
		var dataIDs = $("#roleList").grid("getDataIDs");
		var assignedIds = $("#assignedRoleList").grid("getDataIDs");
		for(var i=0;i<dataIDs.length;i++){
			var isExist = false;
			for(var j=0;j<assignedIds.length;j++){
				if(dataIDs[i]==assignedIds[j]){
					isExist = true;
					break;
				}
			}
			if(isExist){
				$("#roleList").grid("setSelection",dataIDs[i],false);
			}
		}
	}
	
	/**
	 * 加载完已分配数据后加载所有角色列表
	 */
	function onAssignedLoadComplete(xhr){
		$("#roleList").grid("setGridParam", {url:"${ctx}/roleCtl/roleList.html?useTarg=1"});
		$("#roleList").trigger("reloadGrid");
	}
	
	function queryEnter(e){
		if(e == 13){
			$("#queryBtn").click();	
		}
	}
	
	function onSelectAll(rowids,status){
		if(status){
			for(var i=0;i<rowids.length;i++){
				var rowid = rowids[i];
				if($("#assignedRoleList").grid("getInd", rowid)==false){
					var rowd = $("#roleList").grid("getRowData", rowid);
					$("#assignedRoleList").grid("addRowData", rowid, {ZJID:rowd.zjid,JSMC:rowd.jsmc});	
				}
			}
		}else{
			for(var i=0;i<rowids.length;i++){
				var rowid = rowids[i];
				if($("#assignedRoleList").grid("getRowData", rowid)!=null){
					$("#assignedRoleList").grid("delRowData", rowid);
				}
			}
		}
	}
</script>
</head>
<body>
	<ui:LayoutContainer fit="true">
		<ui:Layout region="north" style="height:30px;text-align:center;background-color:#F8F8F8" border="false">
			<input id="orgId" type="hidden" value="${orgId}"/>
			<input type="button" class="easyui-button" style="margin-top:3px" value="保存" onclick="save()" /> 
		</ui:Layout>
		<ui:Layout region="center" title="待分配角色">
			<ui:LayoutContainer fit="true">
				<ui:Layout region="north" style="height:40px" border="false" >
					<table class="formTable">
						<tr>
							<td class="label" style="width:100px">
								角色名称：
							</td>
							<td>
								<input type="text" id="roleName" name="roleName" class="easyui-textinput" watermark="支持模糊查询" title="按下回车键可触发查询"  onkeydown="queryEnter(event.keyCode||event.which)"/>
							</td>
							<td style="width:100px">
								<input id="queryBtn" type="button" class="easyui-button" value="查询" onclick="queryRoleList()">
							</td>
						</tr>
					</table>
				</ui:Layout>
				<ui:Layout region="center" style="padding-top:2px;padding-right:1px" border="false">
					<ui:Grid id="roleList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
						url="" multiselect="true" multiboxonly="false"
						rowNum="20" rowList="[10,20,50]" onSelectRow="onSelectRow" gridComplete="roleListGridComplete" onSelectAll="onSelectAll">
						<ui:Column name="zjid" key="true" hidden="true"></ui:Column>
						<ui:Column header="角色名称" name="jsmc" width="160"></ui:Column>
						<ui:Column header="角色代码" name="jsdm" width="160"></ui:Column>
					</ui:Grid>
				</ui:Layout>
			</ui:LayoutContainer>
		</ui:Layout>
		<ui:Layout region="east" style="width:200px;padding-top:2px;padding-left:1px" split="true" title="已分配角色">
			<ui:Grid id="assignedRoleList" datatype="json" shrinkToFit="true" fit="true" pageable="false"
						url="${ctx}/orgRole/getAssignedRoleList.html?orgId=${orgId}" rownumbers="false"
						rowNum="999" loadComplete="onAssignedLoadComplete">
				<ui:Column name="ZJID" key="true" hidden="true"></ui:Column>
				<ui:Column header="角色名称" name="JSMC" width="160"></ui:Column>
				<ui:Column header="" width="30" fixed="true" formatter="operationFormatter"></ui:Column>
			</ui:Grid>
		</ui:Layout>
		<ui:Layout region="south" style="height:30px;padding-top:5px;background-color:#F8F8F8" border="false">
				<div class="icon-qbzc" style="width:16px;height:16px;float:left"></div>
				<div>
					已选单位：
					<c:forEach var="item" items="${parentOrgList}" varStatus="status"  step="1">  
		       				<c:if test="${!status.last}">
		       					${item.DWMC}&gt;&gt;
		       				</c:if>
		       				<c:if test="${status.last}">
		       					<b>${item.DWMC}</b>
		       				</c:if>
					</c:forEach> 
				</div>
		</ui:Layout>
	</ui:LayoutContainer>
</html>