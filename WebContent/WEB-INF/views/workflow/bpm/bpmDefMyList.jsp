<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="/tags/ui"%>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<script type="text/javascript" src="${ctx}/ui/platform/workflow/bpm/FlowUtil.js"></script>
<script type="text/javascript">
	var topWin = FrameHelper.getTop();
	//查询
	function query() {
		var postData = {deploymentId:""};
		postData.subject = $("#subject").val();
		postData.defKey = $("#defKey").val();
		postData.startTime = $("#startTime").val();
		postData.endTime = $("#endTime").val();
		$("#bpmDefList").grid("setGridParam", {
			postData : postData
		});
		$("#bpmDefList").trigger("reloadGrid");
	}
	
	function start(defId, actDefId){
		FlowUtil.startFlow(defId, actDefId);
	}
	
	function operationFormatter(cellVal, options, rowd) {
		var html = "";
		html += "<a id=\"design\" plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-start\" onclick=\"start('"+rowd.defId+"','"+rowd.actDefId+"')\">启动</a>";
		return html;
	}
	
	function onGridComplete() {
		using("linkbutton", function() {
			$(".easyui-linkbutton").linkbutton();
		});
	}
	
	function refreshGrid(){
		$("#bpmDefList").trigger("reloadGrid");
	}
	
	
</script>
</head>
<body>
	<ui:LayoutContainer fit="true" id="main">
		<ui:Layout region="north" style="height:105px;">
			<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
				<a id="query" href="#" iconCls="icon-search" onclick="query()">查询</a>
			</div>
			<form method="post" id="queryForm">
				<table class="formTable">
					<tr>
						<td class="label" style="width: 100px">流程名称：</td>
						<td style="width: 130px">
							<input type="text" class="easyui-textinput" id="subject" name="subject" watermark="支持模糊查询" />
						</td>
						<td class="label" style="width: 100px">流程编号：</td>
						<td >
							<input type="text" class="easyui-textinput" id="defKey" name="defKey" watermark="支持模糊查询" />
						</td>
					</tr>
					<tr>
						<td class="label" style="width: 100px">创建时间：</td>
						<td colspan="3">
							<table>
								<tr>
									<td><input class="easyui-datePicker" id="startTime"
										name="startTime" dateFmt="yyyy-MM-dd HH:mm:ss"></td>
									<td>至</td>
									<td><input id="endTime" name="endTime" type="text"
										class="easyui-datePicker" dateFmt="yyyy-MM-dd HH:mm:ss" /></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</form>
		</ui:Layout>
		<ui:Layout region="center" border="false" style="padding-top:2px">
			<ui:Grid id="bpmDefList" datatype="json" shrinkToFit="false"
				fit="true" viewrecords="true" pageable="true"
				url="${ctx}/workflow/bpmDef/myList.html" multiselect="true"
				rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete" postData="{typeId:'${typeId}'}">
				<ui:Column name="defId" key="true" hidden="true"></ui:Column>
				<ui:Column header="流程名称" name="subject" width="180"></ui:Column>
				<ui:Column header="流程编号" name="defKey" width="100"></ui:Column>
				<ui:Column header="流程分类" name="catalogName" width="80"></ui:Column>
				<ui:Column header="版本号" name="version" width="60" align="center"></ui:Column>
				<ui:Column header="管理" name="" width="60" formatter="operationFormatter"></ui:Column>
			</ui:Grid>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>