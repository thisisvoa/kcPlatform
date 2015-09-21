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
	var operateType = "${operateType}";
	
	function onSelectRow(rowid,iRow,iCol){
			//分配用户
			if(operateType=="assignUser"){
				parent.frames["funcFrame"].document.location.href = "${ctx }/userRole/assignUserToRolePage.html"
					+"?roleId="+rowid;
			}
			//分配功能
			else if(operateType=="assignFunc"){
				parent.frames["funcFrame"].document.location.href = "${ctx }/roleFunc/assignFuncToRolePage.html"
					+"?roleId="+rowid;
			}
			//分配组织
			else if(operateType=="assignOrg"){
				parent.frames["orgFrame"].document.location.href = "${ctx }/orgRole/assignOrgToRolePage.html"
					+"?roleId="+rowid;
			}
			
	}
	function queryRoleList(){
		$("#roleList").grid("setGridParam", {postData:{roleName:$("#roleName").val()}});
		$("#roleList").trigger("reloadGrid");
	}
	function queryEnter(e){
		if(e == 13){
			$("#queryBtn").click();	
		}
	}
</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:60px" border="false" >
		<table class="formTable">
			<tr>
				<td class="label" style="width:100px">
					角色名称：
				</td>
				<td>
					<input type="text" id="roleName" name="roleName" class="easyui-textinput" watermark="支持模糊查询" title="按下回车键可触发查询"  onkeydown="queryEnter(event.keyCode||event.which)"/>
				</td>
			</tr>
			<tr>
				<td style="width:100px;text-align:right" colspan="2">
					<input id="queryBtn" type="button" class="easyui-button" value="查询" onclick="queryRoleList()">
				</td>
			</tr>
		</table>
	</ui:Layout>
	<ui:Layout region="center" style="padding-top:2px;padding-right:1px" border="false">
		<ui:Grid id="roleList" datatype="json" shrinkToFit="true" fit="true" viewrecords="false" pageable="true"
			url="${ctx}/roleCtl/roleList.html?useTarg=1" multiselect="false" onSelectRow="onSelectRow"
			rowNum="20" rowList="[10,20,50]" pagerpos="left" pginput="true">
			<ui:Column name="zjid" key="true" hidden="true"></ui:Column>
			<ui:Column header="角色名称" name="jsmc" width="160"></ui:Column>
			<ui:Column header="角色代码" name="jsdm" width="160"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>