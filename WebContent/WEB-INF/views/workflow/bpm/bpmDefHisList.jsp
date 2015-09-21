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
	
	//删除
	function remove(id){
		MessageUtil.confirm("确定删除选中的流程定义吗？",function(){
			$.postc("${ctx}/workflow/bpmDef/del.html",{ids:id,isOnlyVersion:true}, function(){
				$("#bpmDefList").trigger("reloadGrid");
				MessageUtil.show("流程定义删除成功!");
			});	
		});
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
		var height = $(topWin.document.body).height();
		var width = $(topWin.document.body).width();
		topWin.Dialog.open({
			Title : "流程定义明细",
			URL : "${ctx}/workflow/bpmDef/defConfig.html?defId="+defId,
			Width : width,
			Height : height
		});
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
		html += "<a id=\"rem\" plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-remove\" onclick=\"remove('"+rowd.defId+"')\">删除</a>";
		html += "<a id=\"design\" plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-flowDesign\" onclick=\"edit('"+rowd.defId+"')\">查看设计</a>";
		html += "<a id=\"edit\" plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-edit\" onclick=\"config('"+rowd.defId+"')\">明细</a>";
		html += "<a id=\"defaultBtn\" plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-edit\" onclick=\"setMain('"+rowd.defId+"')\">设为主版本</a>";	
		return html;
	}
	
	function setMain(defId){
		$.postc("${ctx}/workflow/bpmDef/setMain.html",{defId:defId}, function(){
			MessageUtil.show("流程已经设置为主版本!");
			parent.parent.needRefresh = true;
		});
	}
	
	function onGridComplete() {
		using("linkbutton", function() {
			$(".easyui-linkbutton").linkbutton();
		});
	}
	
</script>
</head>
<body>
	<ui:LayoutContainer fit="true" id="main">
		<ui:Layout region="center" border="false" style="padding-top:2px">
			<ui:Grid id="bpmDefList" datatype="json" shrinkToFit="false"
				fit="true" viewrecords="true" pageable="true"
				url="${ctx}/workflow/bpmDef/hisList.html?defId=${defId}" multiselect="false"
				rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete" postData="{typeId:'${typeId}'}">
				<ui:Column name="defId" key="true" hidden="true"></ui:Column>
				<ui:Column header="标题" name="subject" width="120"></ui:Column>
				<ui:Column header="流程编号" name="defKey" width="100"></ui:Column>
				<ui:Column header="流程分类" name="catalogName" width="100"></ui:Column>
				<ui:Column header="版本号" name="version" width="60" align="center"></ui:Column>
				<ui:Column header="发布状态" name="status" width="60" edittype="select" formatter="'select'" align="center" editoptions="{value:\"0:<span style='color:red'>未发布</span>;1:已发布\"}"></ui:Column>
				<ui:Column header="创建时间" name="createTime" width="130" formatter="dateFormatter"></ui:Column>
				<ui:Column header="操作" name="" width="280" formatter="operationFormatter"></ui:Column>
			</ui:Grid>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>