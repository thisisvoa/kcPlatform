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
<title></title>
<script type="text/javascript">
	var topWin = FrameHelper.getTop();
	function doQuery(){
		var postData = {};
		postData.gdmc = $("#gdmc").val();
		postData.gdbmc = $("#gdbmc").val();
		$("#gdpzList").grid("setGridParam", {postData:postData});
		$("#gdpzList").trigger("reloadGrid");
	}
	
	function addGdpz(){
		topWin.Dialog.open({
			Title : "新增归档配置",
			URL : '${ctx}/gdpz/toAdd.html',
			Width : 800,
			Height : 300,
			RefreshHandler : function(){
				$("#gdpzList").trigger("reloadGrid");
			}});
	}
	
	function editGdpz(){
		var selectedIds = $("#gdpzList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要编辑的记录！");
			return;
		}
		if(selectedIds.length > 1){
			MessageUtil.alert("请选择一条要编辑的记录！");
			return;
		}
		var id = selectedIds.join(",");
		topWin.Dialog.open({
			Title : "修改归档配置",
			URL : '${ctx}/gdpz/toUpdate.html?id='+id,
			Width : 800,
			Height : 300,
			RefreshHandler : function(){
				$("#gdpzList").trigger("reloadGrid");
			}});
	}
	
	function viewGdpz(){
		var selectedIds = $("#gdpzList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要查看的记录！");
			return;
		}
		if(selectedIds.length > 1){
			MessageUtil.alert("请选择一条要查看的记录！");
			return;
		}
		var id = selectedIds.join(",");
		topWin.Dialog.open({
			Title : "查看归档配置",
			URL : '${ctx}/gdpz/toView.html?id='+id,
			Width : 700,
			Height : 280
			});
	}
	
	//启用用户(批量)
	function enabledGdpzs(){
		var selectedIds = $("#gdpzList").grid("getGridParam", "selarrrow");
		enabledGdpz(selectedIds.join(","));
	}
	
	function enabledGdpz(zjids){
		if (zjids == null || zjids.length <= 0) {
			MessageUtil.alert("请选择要启用的记录！");
			return;
		}
		MessageUtil.confirm("您确定启用选中的记录吗？",function() {				
			var url = "${ctx}/gdpz/enabledGdpz.html";
			var params = {
				ids:zjids
			};
			$.postc(url,params,function(data){
				MessageUtil.show("操作成功");
				$("#gdpzList").trigger("reloadGrid");
			});
		});
	}
	
	//禁用用户(批量)
	function forbidGdpzs(){
		var selectedIds = $("#gdpzList").grid("getGridParam", "selarrrow");
		forbidGdpz(selectedIds.join(","));
	}
	
	//禁用用户
	function forbidGdpz(zjids){
		if (zjids == null || zjids.length <= 0) {
			MessageUtil.alert("请选择要禁用的记录！");
			return;
		}
		MessageUtil.confirm("您确定禁用选中的记录吗？",function() {				
			var url = "${ctx}/gdpz/forbidGdpz.html";
			var params = {
				ids:zjids
			};
			$.postc(url,params,function(data){
				MessageUtil.show("操作成功");
				$("#gdpzList").trigger("reloadGrid");
			});
		});
	}
	
	
	function delGdpz(){
		var selectedIds = $("#gdpzList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要删除的记录！");
			return;
		}
		var ids = selectedIds.join(",");
		MessageUtil.confirm("确定删除选择的记录吗？",function(){
			$.postc('${ctx}/gdpz/deleteGdpz.html', {ids:ids}, function(data){
				MessageUtil.show("删除记录成功！");
				$("#gdpzList").trigger("reloadGrid");
			});
		});
	}
	

	function onGridComplete(){
		using("button", function(){
			$(".easyui-button").button();
		});
	}
	
	function operaFormatter(cellVal,options,rowd){
		var html = "";
		var sybz = rowd.sybz;
		if(sybz == "0"){			
			html += "<input type='button' class='easyui-button' style='margin:1px' onclick=\"javascript:enabledGdpz('"+rowd.zjid+"')\" value='启用' />";
		}else if(sybz == "1"){			
			html += "<input type='button' class='easyui-button' style='margin:1px' onclick=\"javascript:forbidGdpz('"+rowd.zjid+"');\" value='禁用' />";
		}
		return html;
	}
	
	function gdzqFormatter(cellVal,options,rowd){
		var html = cellVal;
		var gdzqDw = rowd.gdzqDw;
		if(gdzqDw == "1"){
			html += "日";
		}else if(gdzqDw == "2"){
			html += "个月";
		}else if(gdzqDw == "3"){
			html += "年";
		}else{
			html = "未知";
		}
		return html;
	}
	
	function ycsjFormatter(cellVal,options,rowd){
		return cellVal + "天";
	}
	
	function sybzFormatter(cellVal,options,rowd){
		if(cellVal=="0"){
			return "<span style='color:red'>禁用</span>";
		}else{
			return "启用";
		}
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
	<ui:Layout region="north" style="height:90px;" title="查询条件">
		<form id="queryForm" onkeydown="queryEnter(event.keyCode||event.which)">
			<table class="formTable">
				<tr>
					<td class="label" style="width:100px">
						归档名称：
					</td>
					<td style="width:150px">
						<input type="text" class="easyui-textinput" id="gdmc" name="gdmc" watermark="支持模糊查询"/>
					</td>
					<td class="label" style="width:100px">
						归档表：
					</td>
					<td>
						<input type="text" class="easyui-textinput" id="gdbmc" name="gdbmc"  watermark="支持模糊查询"/>
					</td>
				</tr>
			</table>
		</form>
		<div id="toolbar" class="easyui-toolbar" style="width:100%;margin-top:2px">
			<kpm:HasPermission permCode="SYS_GDPZ_QUERY">
			<a id="queryBtn" href="#" iconCls="icon-search" onclick="doQuery()">查询</a>
			</kpm:HasPermission>
			<kpm:HasPermission permCode="SYS_GDPZ_ADD">
			<a id="add" href="#" iconCls="icon-add" onclick="addGdpz()">新增</a>
			</kpm:HasPermission>
			<kpm:HasPermission permCode="SYS_GDPZ_UPDATE">
			<a id="eidt" href="#" iconCls="icon-edit" onclick="editGdpz()">修改</a>
			</kpm:HasPermission>
			<kpm:HasPermission permCode="SYS_GDPZ_DEL">
			<a id="forbid" href="#" iconCls="icon-remove" onclick="delGdpz()">删除</a>
			</kpm:HasPermission>
			<kpm:HasPermission permCode="SYS_GDPZ_VIEW">
			<a id="view" href="#" iconCls="icon-view" onclick="viewGdpz()">查看</a>
			</kpm:HasPermission>
		</div>
	</ui:Layout>
	<ui:Layout region="center" border="false" style="padding-top:1px">
		<ui:Grid id="gdpzList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
				url="${ctx}/gdpz/gdpzList.html" multiselect="true" rownumbers="false"
				rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
				<ui:Column name="zjid" key="true" hidden="true"></ui:Column>
				<ui:Column header="归档名称" name="gdmc" width="90"></ui:Column>
				<ui:Column header="归档表" name="gdbmc" width="90"></ui:Column>
				<ui:Column header="归档周期" name="gdzq" width="90" fixed="true" align="center" formatter="gdzqFormatter"></ui:Column>
				<ui:Column hidden="true" name="gdzqDw"></ui:Column>
				<ui:Column header="延迟天数" name="ycsj" width="45" align="center" formatter="ycsjFormatter"></ui:Column>
				<ui:Column header="归档时间" name="zxsj" width="60"></ui:Column>
				<ui:Column header="状态" name="sybz" width="60" fixed="true" align="center" formatter="sybzFormatter"></ui:Column>
				<ui:Column header="操作" formatter="operaFormatter" width="90" fixed="true"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>