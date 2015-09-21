<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="ui" uri="/tags/ui"%>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<script type="text/javascript">
var selectedNode = null; 
function radioFormat(cellVal,options,rowd){
	return "<input id=\"ra_"+rowd.id+"\" type=\"radio\" name=\"nodeSelectRadio\" value=\""+rowd.id+"\" onclick=\"gridRadioCheck('"+rowd.id+"')\">";
}
function gridRadioCheck(id){
	var rowd = $("#nodeList").grid("getRowData", id);
	selectedNode = rowd;
}

function doSave(){
	if(selectedNode==null){
		MessageUtil.alert("请选择任务节点");
		return;
	}else{
		parentDialog.setData({selectedNode:selectedNode});
		parentDialog.markUpdated();
		parentDialog.close();
	}
}
</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:33px">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="saveBtn" href="#" iconCls="icon-save" onclick="doSave()">选择</a>
			<a id="close" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center">
		<ui:Grid id="nodeList" datatype="json" shrinkToFit="false" fit="true" viewrecords="true" pageable="false"
			url="${ctx}/workflow/bpmDef/taskNodeList.html?actDefId=${actDefId}&nodeId=${nodeId}" multiselect="false" multiboxonly="true" rowNum="999">
			<ui:Column name="id" key="true" hidden="true"></ui:Column>
			<ui:Column header="" width="50" fixed="true" align="center" formatter="radioFormat"></ui:Column>
			<ui:Column header="节点名称" name="name" width="300"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>