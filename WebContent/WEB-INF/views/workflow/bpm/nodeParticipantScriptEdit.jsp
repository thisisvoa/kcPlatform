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
<script language="Javascript" type="text/javascript" src="${ctx}/ui/js/edit_area/edit_area_full.js"></script>
<script type="text/javascript">
$parsed(function(){
	var script = parentDialog.getData().script;
	$("#script").val(script);
	editAreaLoader.init({
		id: "script",
		start_highlight: true,
		allow_toggle: false,
		language: "zh",
		syntax: "java",
		toolbar: "search, go_to_line, |, undo, redo, |, select_font, |, syntax_selection, |, change_smooth_selection, highlight, reset_highlight, |, help",
		syntax_selection_allow: "java",
		is_multi_files: false,
		show_line_colors: true
	});
});
function doSave(){
	var script = editAreaLoader.getValue("script");
	parentDialog.setData({script:script});
	parentDialog.markUpdated();
	parentDialog.close();
}
</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:33px" border="false">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="saveBtn" href="#" iconCls="icon-save" onclick="doSave()">保存</a>
			<a id="close" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center" border="false" style="padding:1px">
		<table class="tableView" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td class="label" style="width:100%;text-align:left;">
					提示：脚本必须返回Set<String>类型的集合数据，数据项为用户ID
				</td>
			</tr>
			<tr>
				<td>
					<textarea id="script" style="width:100%;height:250px;"  class="editArea"></textarea>
				</td>
			</tr>
		</table>	
	</ui:Layout>
</ui:LayoutContainer>

</body>
</html>