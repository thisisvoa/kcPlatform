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
<script type="text/javascript">
function next(){
	if($("#editForm").validate('validate')){
		var tmpl = {mainTmplId:$("#mainTmplId").combobox("getValue")};
		parentDialog.setData(tmpl);
		parentDialog.markUpdated();
		parentDialog.close();
	}
}
</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:33px;">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="next" href="#" iconCls="icon-run" onclick="next()">下一步</a>
			<a id="remove" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center">
		<form id="editForm" class="easyui-validate">
			<table class="tableView">
				<tr>
					<td class="label">
						主表模板：
					</td>
					<td>
						<select id="mainTmplId" name="mainTmplId" class="easyui-combobox validate[required]" showEmptySelect="true"
							url="${ctx}/workflow/formTemplate/getTemplateList.html?templateType=1" valueField="templateId" textField="templateName">
						</select>
					</td>
				</tr>
			</table>
		</form>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>