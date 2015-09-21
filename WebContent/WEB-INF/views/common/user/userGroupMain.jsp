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
	// 查看用户组详细信息
	function viewUserGroup(userGroupId) {
		topWin.Dialog.open({
			URL : "${ctx}/userGroupCtl/toViewUserGroup.html?id=" + userGroupId,
			Height : 300,
			Modal : false,
			Title : "用户组详细信息"
		});
	}
	//新增用户组信息
	function addUserGroup() {
		topWin.Dialog.open({
			URL : "${ctx}/userGroupCtl/toAddUserGroup.html",
			ShowCloseButton : true,
			Height : 300,
			Title : "新增用户组",
			RefreshHandler : function(){
				$("#userGroupList").trigger("reloadGrid");
			}
		});
	}
	//编辑用户组信息
	function eidtUserGroup(userGroupId) {
		topWin.Dialog.open({
			URL : "${ctx}/userGroupCtl/toEditUserGroup.html?id=" + userGroupId,
			ShowCloseButton : true,
			Height : 300,
			Title : "修改用户组",
			RefreshHandler : function(){
				$("#userGroupList").trigger("reloadGrid");
			}
		});
	}
	
	//逻辑删除用户组(可批量)
	function deleteUserGroup(userGroupIds) {
		var selectedIds = $("#userGroupList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0) {
			MessageUtil.alert("请选择要删除的用户组！");
			return;
		}
		MessageUtil.confirm("您确定删除选中的用户组吗？",function(){
				var userGroupIds = selectedIds.join(",");
				var url = "${ctx}/userGroupCtl/logicDelUserGroup.html";
				var params = {
					ids:userGroupIds
				};
				$.postc(url,params,function(data){
					MessageUtil.show("操作成功！");
					$("#userGroupList").trigger("reloadGrid");
				});
				
			});	
	}
	function updateUserGroup(){
		var selectedIds = $("#userList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择要修改的用户组！");
			return;
		}else if(selectedIds.length > 1){
			MessageUtil.alert("只能选择一条记录进行修改！");
		}else{
			eidtUserGroup(selectedIds[0]);
		}
	}
	
	function queryUserGroupList() {
		$("#userGroupList").grid("setGridParam", {postData:{userGroupName:$("#userGroupName").val()}});
		$("#userGroupList").trigger("reloadGrid");
	}
	
	function ondblClickRow(rowid,iRow,iCol,e){
		$("#userGroupListFrame").attr("src","${ctx}/userGroupCtl/toUserGroupUserList.html?userGroupId="+ rowid);
	}
	
</script>
</head>
<body>
	<ui:LayoutContainer id="ws" fit="true">
		<ui:Layout region="west" style="width:250px;padding:1px" split="true">
			<ui:Panel fit="true" title="用户组">
				<ui:LayoutContainer fit="true">
					<ui:Layout region="north" style="height:40px" border="false">
						<form method="post" id="searchForm">
							<table style="formTable">
								<tr>
									<td>
										用户组名：<input type="text" id="userGroupName" class="easyui-textinput" style="width:100px" name="userGroupName" title="按下回车键可触发查询" />
									</td>
									<td>
										<input type="button" class="easyui-button" value="查询" onclick="queryUserGroupList()">
									</td>
								</tr>
							</table>
						</form>
					</ui:Layout>
					<ui:Layout region="center" border="false">
						<ui:LayoutContainer fit="true">
							<ui:Layout region="north" style="padding:1px 2px 0px 1px;height:32px" border="false">
								<div id="toolbar" class="easyui-toolbar" style="width:99.5%">
									<a id="addUserGroupBtn" href="#" iconCls="icon-add" onclick="addUserGroup()">新增</a>
									<a id="eidtUserGroupBtn" href="#" iconCls="icon-edit" onclick="eidtUserGroup()">修改</a>
									<a id="remove" href="#" iconCls="icon-remove" onclick="deleteUserGroup()">删除</a>
								</div>
							</ui:Layout>
							<ui:Layout region="center" border="false" style="padding:1px">
								<ui:Grid id="userGroupList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
									url="${ctx}/userGroupCtl/userGroupList.html" multiselect="true" rownumbers="false" ondblClickRow="ondblClickRow"
									rowNum="20" rowList="[10,20,50]">
									<ui:Column name="zjid" key="true" hidden="true"></ui:Column>
									<ui:Column header="用户组名" name="yhzmc" width="90"></ui:Column>
								</ui:Grid>
							</ui:Layout>
						</ui:LayoutContainer>
					</ui:Layout>
				</ui:LayoutContainer>
			</ui:Panel>
		</ui:Layout>
		<ui:Layout region="center" style="overflow:hidden">
			<iframe id="userGroupListFrame" style="width:100%;height:100%"  src="" name="userGroupListFrame" frameBorder=0 style="padding:0px;margin:0px" >
			</iframe>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>
