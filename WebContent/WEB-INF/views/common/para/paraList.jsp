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
	function queryParam(){
		$("#paramList").grid("setGridParam", {postData:{csmc:$("#csmc").val(), csdm:$("#csdm").val(),useFlag:$("#useFlag").combobox("getValue")}});
		$("#paramList").trigger("reloadGrid");	
		
	}
	
	function addParam(){
		topWin.Dialog.open({URL:"${ctx}/para/toParaAdd.html", 
							Width:600, 
							Height:350,
							Title:"新增参数信息",
				  			RefreshHandler : function(){
				  				$("#paramList").trigger("reloadGrid");	
				  			}});
	}
	
	function deleteParam() {
		var zjIds = mutilSelect();
		if(zjIds != null && zjIds != ''){
			MessageUtil.confirm("您确定删除选中的参数吗？",function(){
				var url = "${ctx}/para/deleteParams.html?ids=" + zjIds;
				$.postc(url,null,function(data){
					if(data == 1){
						MessageUtil.show("操作成功!");
						$("#paramList").trigger("reloadGrid");	
					}else if(data == 2){
						MessageUtil.show("操作失败，请联系系统管理员!");
					}
				});
			});
		}
		
	}
	
	function refreshParamList(){
		$("#paramList").trigger('reloadGrid');
	}
	
	//禁用角色(可批量)
	function forbidParam(){
		if (zjId == null) {
			MessageUtil.alert("请选择要禁用的参数！");
			return;
		}
		MessageUtil.confirm("您确定禁用选中的参数吗？",function() {
			var url = "${ctx}/para/updateParaSybz.html";
			var params = {
					zjId:zjId,
					sybz:"0"
			};
			$.postc(url,params,function(data){
				MessageUtil.show("操作成功!");
				$("#paramList").trigger("reloadGrid");	
			});
			
		});
	}
	
	function updateSYBZ(sybz) {
		var msg = "";
		if(sybz=='1'){
			msg = "确定要启用该参数吗?";
		}else{
			msg = "确定要禁用该参数吗?";
		}
		var zjIds = mutilSelect();
		if(zjIds != null && zjIds != ''){
			topWin.Dialog.confirm(msg,function(){
				$.postc(encodeURI("${ctx}/para/updateParaSybz.html?zjIds=" + zjIds + "&sybz=" + sybz),null,function(data){
					if(data == 1){
						MessageUtil.show("操作成功!");
						$("#paramList").trigger('reloadGrid');
					}else if(data == 0){
						MessageUtil.show("操作失败，请联系系统管理员！");
					}
				});
			});
		}
	}
	
	
	
	function viewParam() {
		var zjId = singleSelect();
		if(zjId != null && zjId != ''){
			topWin.Dialog.open({URL:"${ctx}/para/paraView.html?zjId=" + zjId, 
				Width:600, 
				Height:350,
				Title:"查看参数信息",
				Modal:false});
		}
	}
	
	function eidtParam() {
		var zjId = singleSelect();
		if(zjId != null && zjId != ''){
			topWin.Dialog.open({URL:"${ctx}/para/toParaEdit.html?zjId="+zjId, 
				Width:600, 
				Height:350,
				Title:"修改参数信息",
	  			RefreshHandler : function(){
	  				$("#paramList").trigger("reloadGrid");	
	  			}});
		}
		
	}
	
	
	function sybzFormater(cellVal,options,rowd){
		if(cellVal=="0"){
			return "<span style='color:red'>禁用</span>";
		}else{
			return "启用";
		}
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
	
	//单选
	function singleSelect(){
		var selectedIds = $("#paramList").grid("getGridParam", "selarrrow");
		var returnValue = "";
		if (selectedIds.length == 0){
			MessageUtil.alert("请先选择一个参数项！");
		}else if(selectedIds.length > 1){
			MessageUtil.alert("只能选择一个参数项！");
		}else{
			returnValue = selectedIds;
		}
		
		return returnValue;
	}
	
	//多选
	function mutilSelect(){
		var selectedIds = $("#paramList").grid("getGridParam", "selarrrow");
		var returnValue = "";
		if (selectedIds.length == 0){
			MessageUtil.alert("请先最少选择一个参数项！");
		}else{
			returnValue = selectedIds;
		}
		
		return returnValue;
	}
</script>

</head>
<body style="padding:1px"  onkeydown="queryEnter(event.keyCode||event.which)">
	<ui:LayoutContainer fit="true" >
		<ui:Layout region="north" style="height:60px" title="查询条件" radius="true">
			<form method="post" id="queryForm">
				<table class="formTable">
					<tr>
						<td class="label" width="100px;">参数名称：</td>
						<td width="210px;">
							<input type="text" class="easyui-textinput" id="csmc" name="csmc" value="${csmc}" 
								 maxlength="30" style="width: 200px;" title="按下回车键可触发查询" watermark="支持模糊查询" onkeydown="queryEnter(event.keyCode||event.which)"/>
						</td>
						<td class="label" width="100px;">参数代码：</td>
						<td width="210px;">
							<input type="text" class="easyui-textinput" id="csdm" name="csdm" value="${csdm}"
								maxlength="30" style="width: 200px;" title="按下回车键可触发查询" watermark="支持模糊查询" onkeydown="queryEnter(event.keyCode||event.which)" />
						</td>
						<td class="label" width="100px;">使用标识：</td>
						<td >
						<select class="easyui-combobox" id="useFlag" name="useFlag" selValue="" style="width:90px" dropdownHeight="75">
								<option value="">---请选择---</option>
								<option value="1" >启用</option>
								<option value="0" >禁用</option>
							</select>
						</td>
					</tr>
				</table>
				
			</form>
		</ui:Layout>
		<ui:Layout region="center" border="false">
			<ui:LayoutContainer fit="true">
				<ui:Layout region="north" style="height:35px;" border="false">
					<div id="toolbar" class="easyui-toolbar" style="width:100%;margin-top:2px">
						<kpm:HasPermission permCode="SYS_PARAM_QUERY">
							<a id="queryBtn" href="#" iconCls="icon-search" onclick="queryParam()">查询</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_PARAM_ADD">
							<a id="remove" href="#" iconCls="icon-add" onclick="addParam()">新增</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_PARAM_UPDATE">
							<a id="edit" href="#" iconCls="icon-edit" onclick="eidtParam();">修改</a>
						</kpm:HasPermission>	
						<kpm:HasPermission permCode="SYS_PARAM_DELETE">
							<a id="remove" href="#" iconCls="icon-remove" onclick="deleteParam()">删除</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_PARAM_VIEW">
							<a id="view" href="#" iconCls="icon-view" onclick="viewParam();">查看</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_PARAM_ENABLED">	
							<a id="qy" href="#" iconCls="icon-ok" onclick="updateSYBZ(1);">启用</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_PARAM_FORBID">
							<a id="jy" href="#" iconCls="icon-no" onclick="updateSYBZ(0);">禁用</a>
						</kpm:HasPermission>
					</div>
				</ui:Layout>
				<ui:Layout region="center" border="false">
				<ui:LayoutContainer fit="true">
					<ui:Layout region="center" style="padding-top:2px" border="false">
						<ui:Grid id="paramList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
							url="${ctx}/para/paraList.html" multiselect="true"
							rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
							<ui:Column name="zjId" key="true" hidden="true"></ui:Column>
							<ui:Column header="参数名称" name="csmc" width="350" fixed="true"></ui:Column>
							<ui:Column header="参数代码" name="csdm" width="150" fixed="true"></ui:Column>
							<ui:Column header="参 数 值" name="csz" width="180" fixed="true"></ui:Column>
							<ui:Column header="使用标识" name="sybz" width="60" fixed="true" formatter="sybzFormater" align="center"></ui:Column>
						</ui:Grid>
				</ui:Layout>
			</ui:LayoutContainer>
		</ui:Layout>
			</ui:LayoutContainer>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>
