<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="ui" uri="/tags/ui" %>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<title></title>
<body>
<ui:Panel fit="true">
<table class="tableView">
	<tr>
		<td class="label" style="width:100px">
			变量ID：
		</td>
		<td>
			${bpmDefVar.varId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			流程定义ID：
		</td>
		<td>
			${bpmDefVar.defId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			变量名称：
		</td>
		<td>
			${bpmDefVar.varName}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			变量Key：
		</td>
		<td>
			${bpmDefVar.varKey}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			变量数据类型：
		</td>
		<td>
			${bpmDefVar.varDataType}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			默认值：
		</td>
		<td>
			${bpmDefVar.defValue}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			节点名称：
		</td>
		<td>
			${bpmDefVar.nodeName}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			节点ID：
		</td>
		<td>
			${bpmDefVar.nodeId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			：
		</td>
		<td>
			${bpmDefVar.actDeployId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			作用域：
		</td>
		<td>
			${bpmDefVar.varScope}
		</td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<input type="button" class="easyui-button" value="关闭" onclick="javascript:parentDialog.close()">
		</td>
	</tr>
</table>
</ui:Panel>
</body>
</html>