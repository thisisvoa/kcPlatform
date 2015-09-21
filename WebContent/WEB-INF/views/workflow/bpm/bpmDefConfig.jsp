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
</head>
<body>
<ui:TabNavigator fit="true">
	<ui:Tab id="tab1" iconCls="icon-tab" title="流程明细" url="${ctx}/workflow/bpmDef/detail.html?defId=${bpmDef.defId}"></ui:Tab>
	<ui:Tab id="tab2" iconCls="icon-tab" title="节点设置" url="${ctx}/workflow/bpmDef/graphNav.html?defId=${bpmDef.defId}"></ui:Tab>
	<ui:Tab id="tab3" iconCls="icon-tab" title="表单设置" url="${ctx}/workflow/bpmDef/nodeConfig.html?defId=${bpmDef.defId}"></ui:Tab>
	<ui:Tab id="tab4" iconCls="icon-tab" title="人员设置" url="${ctx}/workflow/bpmDef/nodeParticipant/all.html?defId=${bpmDef.defId}"></ui:Tab>
	<ui:Tab id="tab5" iconCls="icon-tab" title="操作按钮" url="${ctx}/workflow/bpmDef/nodeButton/toMain.html?defId=${bpmDef.defId}"></ui:Tab>
	<%-- <ui:Tab id="tab6" iconCls="icon-tab" title="流程变量" url="${ctx}/workflow/bpmDef/var/toMain.html?defId=${bpmDef.defId}"></ui:Tab> --%>
	<ui:Tab id="tab9" iconCls="icon-tab" title="历史版本" url="${ctx}/workflow/bpmDef/toHisList.html?defId=${bpmDef.defId}"></ui:Tab>
	<ui:Tab id="tab7" iconCls="icon-tab" title="流程实例" url="${ctx}/workflow/bpm/processRun/toListDefId.html?defId=${bpmDef.defId}"></ui:Tab>
	<ui:Tab id="tab8" iconCls="icon-tab" title="其他设置" url="${ctx}/workflow/bpmDef/otherParam.html?defId=${bpmDef.defId}"></ui:Tab>
</ui:TabNavigator>
</body>
</html>