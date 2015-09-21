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
<script language="Javascript" type="text/javascript" src="${ctx}/ui/js/edit_area/edit_area_full.js"></script>
<script type="text/javascript">
$parsed(function(){
	editAreaLoader.init({
		id: "html",
		start_highlight: true,
		allow_toggle: false,
		language: "zh",
		syntax: "html",
		toolbar: "search, go_to_line, |, undo, redo, |, select_font, |, syntax_selection, |, change_smooth_selection, highlight, reset_highlight, |, help",
		syntax_selection_allow: "html",
		is_multi_files: false,
		show_line_colors: true
	});
});
</script>
<title></title>
<body>
<ui:Panel fit="true">
<div id="toolbar" class="easyui-toolbar">
	<a id="close" href="#"  iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
</div>
<table class="tableView">
	<tr>
		<td class="label" style="width:100px">
			模板名称：
		</td>
		<td>
			${bpmFormTemplate.templateName}
		</td>
		<td class="label" style="width:100px">
			别名：
		</td>
		<td>
			${bpmFormTemplate.alias}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			模板类型：
		</td>
		<td>
			<c:if test="${bpmFormTemplate.templateType eq '1'}">主表模板</c:if>
		</td>
		<td class="label" style="width:100px">
			模板来源：
		</td>
		<td>
			<c:if test="${bpmFormTemplate.canEdit eq '0'}">系统模板</c:if>
			<c:if test="${bpmFormTemplate.canEdit eq '1'}">自定义模板</c:if>
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			模版代码：
		</td>
		<td colspan="3">
			<textarea id="html" style="width:90%;height:450px;"  class="editArea">${bpmFormTemplate.html}</textarea>
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			描述：
		</td>
		<td colspan="3">
			<textarea style="width:600px;height:50px">${bpmFormTemplate.templateDesc}</textarea>
		</td>
	</tr>
</table>
</ui:Panel>
</body>
</html>