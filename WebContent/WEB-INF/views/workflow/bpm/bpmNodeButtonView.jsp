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
			：
		</td>
		<td>
			${bpmNodeButton.btnId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			：
		</td>
		<td>
			${bpmNodeButton.configId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			：
		</td>
		<td>
			${bpmNodeButton.defId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			：
		</td>
		<td>
			${bpmNodeButton.actDefId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			：
		</td>
		<td>
			${bpmNodeButton.nodeId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			按钮名称：
		</td>
		<td>
			${bpmNodeButton.btnName}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			按钮编号：
		</td>
		<td>
			${bpmNodeButton.btnNo}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			操作类型：
		</td>
		<td>
			${bpmNodeButton.operateType}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			调用的js方法，当按钮类型为自定义的时候设置按钮调用的JS方法：
		</td>
		<td>
			${bpmNodeButton.operateFunc}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			图标：
		</td>
		<td>
			${bpmNodeButton.icon}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			按钮序号：
		</td>
		<td>
			${bpmNodeButton.btnOrder}
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