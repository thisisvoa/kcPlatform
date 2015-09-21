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
		postData.gdbmc = $("#gdbmc").val();
		postData.gdhbmc = $("#gdhbmc").val();
		postData.gdmc = $("#gdmc").val();
		$("#gdqdList").grid("setGridParam", {postData:postData});
		$("#gdqdList").trigger("reloadGrid");
	}
	
	function viewGdqd(){
		var selectedIds = $("#gdqdList").grid("getGridParam", "selarrrow");
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
			Title : "查看",
			URL : '${ctx}/gdqd/toView.html?id='+id,
			Width : 600,
			Height : 200
			});
	}
	
	function delGdqd(){
		var selectedIds = $("#gdqdList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要删除的记录！");
			return;
		}
		var ids = selectedIds.join(",");
		MessageUtil.confirm("确定删除选择的记录吗？",function(){
			$.postc('${ctx}/gdqd/deleteGdqd', {ids:ids}, function(data){
				MessageUtil.show("删除记录成功！");
				$("#gdqdList").trigger("reloadGrid");
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
		html += "<input type='button' class='easyui-button' style='margin:1px' onclick=\"javascript:viewGdqd('"+rowd.zjid+"')\" value='查看' />";
		html += "<input type='button' class='easyui-button' style='margin:1px' onclick=\"javascript:editGdqd('"+rowd.zjid+"');\" value='修改' />";
		return html;
	}
	
	function queryEnter(e){
		if(e == 13){
			$("#queryBtn").click();	
		}
	}
	
	function dateFormatter(cellVal, options, rowd) {
		if (cellVal) {
			var d = new Date(cellVal);
			return DateFormat.format(d, "yyyy-MM-dd");
		} else {
			return "";
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
					<td>
						<input type="text" class="easyui-textinput" id="gdmc" name="gdmc" watermark="支持模糊查询"/>
					</td>
					<td class="label" style="width:100px">
						归档表：
					</td>
					<td>
						<input type="text" class="easyui-textinput" id="gdbmc" name="gdbmc" watermark="支持模糊查询"/>
					</td>
				</tr>
			</table>
		</form>
		<div id="toolbar" class="easyui-toolbar" style="width:100%;margin-top:2px">
			<kpm:HasPermission permCode="SYS_GDQD_QUERY">
			<a id="queryBtn" href="#" iconCls="icon-search" onclick="doQuery()">查询</a>
			</kpm:HasPermission>
			<kpm:HasPermission permCode="SYS_GDQD_VIEW">
			<a id="view" href="#" iconCls="icon-view" onclick="viewGdqd()">查看</a>
			</kpm:HasPermission>
		</div>
	</ui:Layout>
	<ui:Layout region="center" border="false" style="padding-top:1px">
		<ui:Grid id="gdqdList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
				url="${ctx}/gdqd/gdqdList.html" multiselect="true" rownumbers="false"
				rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
				<ui:Column name="zjid" key="true" hidden="true"></ui:Column>
				<ui:Column hidden="true" name="gdpzZjid"></ui:Column>
				<ui:Column header="归档名称" name="gdmc" width="90"></ui:Column>
 				<ui:Column header="归档表" name="gdbmc" width="90"></ui:Column>
				<ui:Column header="存储表" name="gdhbmc" width="90"></ui:Column>
				<ui:Column header="起始时间" name="gdsjKssj" width="60" align="center" formatter="dateFormatter"></ui:Column>
				<ui:Column header="结束时间" name="gdsjJssj" width="60"  align="center" formatter="dateFormatter"></ui:Column>
				<ui:Column header="归档时间" name="jlcjsj" width="60"  align="center" formatter="dateFormatter"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>