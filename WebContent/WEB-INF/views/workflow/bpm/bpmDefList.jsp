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
	var needRefresh = false;
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
	
	//删除
	function deleteProcess(){
		var selectedIds = $("#bpmDefList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请要删除的流程定义！");
			return;
		}else{
			MessageUtil.confirm("确定删除选中的流程定义吗？",function(){
				$.postc("${ctx}/workflow/bpmDef/del.html",{ids:selectedIds.join(",")}, function(){
					$("#bpmDefList").trigger("reloadGrid");
					MessageUtil.show("流程定义删除成功!");
				});	
			});
		}
	}
	
	//在线设计
	function designNew(){
		window.open("${ctx}/workflow/bpmDef/bpmEditor.html");
	}
	//编辑流程
	function edit(id){
		window.open("${ctx}/workflow/bpmDef/bpmEditor.html?defId="+id);
	}
	
	function release(defId){
		MessageUtil.progress("流程发布中...");
		$.postc("${ctx}/workflow/bpmDef/deploy.html",{defId:defId}, function(){
			$("#bpmDefList").trigger("reloadGrid");
			MessageUtil.show("流程发布成功!");
			MessageUtil.closeProgress();
		},function(){
			MessageUtil.closeProgress();
		});
	}
	
	function config(defId){
		$("#configFrame").attr("src","${ctx}/workflow/bpmDef/defConfig.html?defId="+defId);
		$("#main").hide();
		$("#config").show();
	}
	
	function start(defId, actDefId){
		FlowUtil.startFlow(defId, actDefId);
	}
	
	function dateFormatter(cellVal, options, rowd) {
		if (cellVal != null) {
			var d = new Date(cellVal);
			return DateFormat.format(d, "yyyy-MM-dd hh:mm:ss");
		} else {
			return "";
		}
	}
	
	function operationFormatter(cellVal, options, rowd) {
		var html = "";
		if(rowd.status=='0')
		{
			html += "<a id=\"design\" plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-flowDesign\" onclick=\"edit('"+rowd.defId+"')\">设计</a>";
			html += "<a id=\"release\" plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-start\" onclick=\"release('"+rowd.defId+"')\">发布</a>";
		}else if(rowd.status=='1'){
			html += "<a id=\"design\" plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-flowDesign\" onclick=\"edit('"+rowd.defId+"')\">设计</a>";
			html += "<a id=\"edit\" plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-edit\" onclick=\"config('"+rowd.defId+"')\">设置</a>";
			html += "<a id=\"design\" plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-start\" onclick=\"start('"+rowd.defId+"','"+rowd.actDefId+"')\">启动</a>";
		}
		return html;
	}
	
	function onGridComplete() {
		using("linkbutton", function() {
			$(".easyui-linkbutton").linkbutton();
		});
	}
	
	function updateState(usable){
		var message = "";
		if(usable=="1"){
			message = "启用";
		}else if(usable=="0"){
			message = "禁用";
		}
		var selectedIds = $("#bpmDefList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请要"+message+"的流程定义！");
			return;
		}else{
			MessageUtil.confirm("确定"+message+"该流程定义?",function(){
				$.postc("${ctx}/workflow/bpmDef/updateUsable.html",{ids:selectedIds.join(","), usable:usable}, function(){
					$("#bpmDefList").trigger("reloadGrid");
					MessageUtil.show("流程定义已"+message+"!");
				});
			});	
		}
		
	}
	
	function refreshGrid(){
		$("#bpmDefList").trigger("reloadGrid");
	}
	
	function back(){
		$("#main").show();
		$("#config").hide();
		if(needRefresh){
			refreshGrid();
		}
	}
	
</script>
</head>
<body>
	<ui:LayoutContainer fit="true" id="main">
		<ui:Layout region="north" style="height:105px;">
			<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
				<a id="query" href="#" iconCls="icon-search" onclick="query()">查询</a>
				<a id="remove" href="#" iconCls="icon-remove" onclick="deleteProcess()">删除</a>
				<a id="design" href="#" iconCls="icon-flowDesign" onclick="designNew()">在线设计</a>
				<!-- <a id="import" href="#" iconCls="icon-upload" onclick="deployProcess()">导入</a> -->
				<a id="refresh" href="#" iconCls="icon-refresh" onclick="refreshGrid()">刷新</a>
				<a id="enableBtn" href="#" iconCls="icon-ok" onclick="updateState('1');">启用</a>
				<a id="forbitBtn" href="#" iconCls="icon-no" onclick="updateState('0');">禁用</a>
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
				url="${ctx}/workflow/bpmDef/list.html" multiselect="true"
				rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete" postData="{typeId:'${typeId}'}">
				<ui:Column name="defId" key="true" hidden="true"></ui:Column>
				<ui:Column header="标题" name="subject" width="120"></ui:Column>
				<ui:Column header="流程编号" name="defKey" width="100"></ui:Column>
				<ui:Column header="流程分类" name="catalogName" width="100"></ui:Column>
				<ui:Column header="版本号" name="version" width="60" align="center"></ui:Column>
				<ui:Column header="发布状态" name="status" width="60" edittype="select" formatter="'select'" align="center" editoptions="{value:\"0:<span style='color:red'>未发布</span>;1:已发布\"}"></ui:Column>
				<ui:Column header="创建时间" name="createTime" width="130" formatter="dateFormatter"></ui:Column>
				<ui:Column header="启用状态" name="usable" width="60" edittype="select" formatter="'select'" align="center" editoptions="{value:\"0:<span style='color:red'>禁用</span>;1:启用\"}"></ui:Column>
				<ui:Column header="操作" name="" width="200" formatter="operationFormatter"></ui:Column>
			</ui:Grid>
		</ui:Layout>
	</ui:LayoutContainer>
	<ui:LayoutContainer fit="true" id="config">
		<ui:Layout region="north" style="height:32px" border="false">
			<div id="toolbar" class="easyui-toolbar"
				style="width: 100%;">
				<a id="query" href="#" iconCls="icon-back" onclick="back()">返回</a>
			</div>
		</ui:Layout>
		<ui:Layout region="center" style="overflow:hidden" border="false">
			<iframe id="configFrame" name="configFrame" src="" style="width:100%;height:100%;" frameborder="0"></iframe>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>