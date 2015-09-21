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
		topWin.Dialog.open({
			URL : "${ctx}/orgCtl/toAddOrg.html?pOrgId="+$("#parentOrg").val(),
			ShowCloseButton : true,
			Width : 600,
			Height : 450,
			Title : "新增单位",
			RefreshHandler:function(){
				$("#orgList").trigger("reloadGrid");
				parent.window.refreshNode($("#parentOrg").val());
			}
		});
	}
	
	// 查看单位详细信息
	function viewOrg(orgId) {
		if(orgId==null){
			var selectedIds = $("#orgList").grid("getGridParam", "selarrrow");
			if (selectedIds.length == 0){
				MessageUtil.alert("请选择待查看的单位！");
				return;
			}else if(selectedIds.length > 1){
				MessageUtil.alert("只能选择一条记录进行查看！");
				return;
			}else{
				orgId = selectedIds[0];
			}
		}
		topWin.Dialog.open({
			URL : "${ctx}/orgCtl/toViewOrg.html?id=" + orgId,
			Width : 600,
			Height : 450,
			Modal : false,
			Title : "单位详细信息"
		});
	}
	
	//编辑单位信息
	function eidtOrg() {
		var selectedIds = $("#orgList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择待修改的单位！");
			return;
		}else if(selectedIds.length > 1){
			MessageUtil.alert("只能选择一条记录进行修改！");
		}else{
			topWin.Dialog.open({
				URL : "${ctx}/orgCtl/toEditOrg.html?id=" + selectedIds[0],
				ShowCloseButton : true,
				Width : 600,
				Height : 450,
				Title : "修改单位",
				RefreshHandler : function(){
					$("#orgList").trigger("reloadGrid");
					parent.window.refreshParentNode(selectedIds[0]);
				}
			});
		}
		
		
	}

	//逻辑删除单位(可批量)
	function deleteOrg(orgIds) {
		var selectedIds = $("#orgList").grid("getGridParam", "selarrrow");
		if(selectedIds.length==0){
			MessageUtil.alert("请选择需删除的单位!");
		}else{
			var orgIds = selectedIds.join(",");
			topWin.Dialog.confirm("您确定删除选中的单位吗？",function(){
				var url = "${ctx}/orgCtl/logicDelOrg.html";
				var params = {
					ids:orgIds
				};
				$.postc(url,params,function(data){
						MessageUtil.show("操作成功！");
						$("#orgList").trigger("reloadGrid");
						parent.window.refreshNode($("#parentOrg").val());
				});
				
			});
		}
	}
	
	function queryOrgList() {
		$("#parentOrg").val("");
		parent.window.cancelSelectedNode();
		$("#orgList").grid("setGridParam", {postData:{parentOrg:"",orgNum:$("#orgNum").val(), orgName:$("#orgName").val()}});
		$("#orgList").trigger("reloadGrid");
	}
	
	function allocateFunctions() {
		var selectedIds = $("#orgList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择待分配的单位！");
			return;
		}else if(selectedIds.length > 1){
			MessageUtil.alert("只能选择一条记录进行功能分配！");
		}else{
			topWin.Dialog.open({
				URL : "${ctx }/orgFunc/assignFuncToOrgPage.html"+"?orgId="+selectedIds[0],
				ShowCloseButton : true,
				Width : 700,
				Height : 450,
				Title : "单位功能分配"
			});
		}
	}
	
	function allocateRoles() {
		var selectedIds = $("#orgList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择待分配的单位！");
			return;
		}else if(selectedIds.length > 1){
			MessageUtil.alert("只能选择一条记录进行角色分配！");
		}else{
			topWin.Dialog.open({
				URL : "${ctx }/orgRole/assignRoleToOrgPage.html?orgId="+selectedIds[0],
				ShowCloseButton : true,
				Width : 700,
				Height : 450,
				Title : "单位角色分配"
			});
		}
	}
	
	
	function viewOrgFormatter(cellVal,options,rowd){
		var html = "";
		html += "<a style=\"color: blue\" onclick=\"javascript:viewOrg('"+rowd.ZJID+"')\">" + cellVal + "</a>";
		return html;
	}
	
	function onGridComplete(){
		using("button", function(){
			$(".easyui-button").button();
		});
	}
	function queryEnter(e){
		if(e == 13){
			$("#queryBtn").click();
		}
	}
</script>

</head>
<body style="padding:1px">
	<ui:LayoutContainer fit="true">
		<ui:Layout region="north" style="height:66px" title="查询条件" radius="true">
			<form method="post" id="searchForm">
			<input type="hidden" value="${org.sjdw_zjid}" id="parentOrg"/>
				<table class="formTable">
					<tr>
						<td class="label" style="width:100px">
							单位编码：
						</td>
						<td>
							<input type="text" id="orgNum" name="orgNum" class="easyui-textinput" watermark="支持模糊查询" title="按下回车键可触发查询" onkeydown="queryEnter(event.keyCode||event.which)"/>
						</td>
						<td class="label" style="width:100px">
							单位名称：
						</td>
						<td>
							<input type="text" id="orgName" name="orgName" class="easyui-textinput" watermark="支持模糊查询" style="width: 200px;" title="按下回车键可触发查询" onkeydown="queryEnter(event.keyCode||event.which)"/>
						</td>
					</tr>
				</table>
			</form>
		</ui:Layout>
		<ui:Layout region="center" border="false">
			<ui:LayoutContainer fit="true">
				<ui:Layout region="north" style="height:35px;" border="false">
					<div id="toolbar" class="easyui-toolbar" style="width:100%;margin-top:2px">
						<kpm:HasPermission permCode="SYS_ORG_QUERY">
						<a id="queryBtn" href="#" iconCls="icon-search" onclick="queryOrgList()">查询</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_ORG_ADD">
							<a id="add" href="#" iconCls="icon-add" onclick="addOrg()">新增</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_ORG_UPDETE">
							<a id="update" href="#" iconCls="icon-edit" onclick="eidtOrg()">修改</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_ORG_DELETE">
							<a id="remove" href="#" iconCls="icon-remove" onclick="deleteOrg()">删除</a>
						</kpm:HasPermission>
						<a id="view" href="#" iconCls="icon-view" onclick="viewOrg()">查看</a>
						<kpm:HasPermission permCode="SYS_ORG_ASSIGN_ROLE">
							<a id="allocateRoles" href="#" iconCls="icon-edit" onclick="allocateRoles()">角色分配</a>
						</kpm:HasPermission>
					</div>
				</ui:Layout>
				<ui:Layout region="center" style="padding-top:2px" border="false">
					<ui:Grid id="orgList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
						url="${ctx}/orgCtl/orgList.html" postData="{parentOrg:'${org.sjdw_zjid}'}" multiselect="true"
						rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
						<ui:Column name="ZJID" key="true" hidden="true"></ui:Column>
						<ui:Column header="单位编码" name="DWBH" width="90" fixed="true"></ui:Column>
						<ui:Column header="单位名称" name="DWMC"  width="250" fixed="true" formatter="viewOrgFormatter"></ui:Column>
						<ui:Column header="上级单位" name="SJDWMC" width="250" fixed="true"></ui:Column>
						<ui:Column header="单位负责人" name="DWFZR" width="65" fixed="true"></ui:Column>
					</ui:Grid>
				</ui:Layout>
			</ui:LayoutContainer>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>
