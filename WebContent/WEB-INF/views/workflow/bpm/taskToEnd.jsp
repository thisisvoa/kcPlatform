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
<title>终止流程</title>
<script type="text/javascript">
function doSave(){
	if($("#editForm").validate("validate")){
		MessageUtil.confirm("确定终止该任务吗？",function(){
			parentDialog.setData($("#memo").val());
			parentDialog.markUpdated();
			parentDialog.close();
		});
	}
}

</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:33px">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="saveBtn" href="#" iconCls="icon-save" onclick="doSave()">确定</a>
			<a id="closeBtn" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center">
		<form id="editForm" class="easyui-validate" onsubmit="javascript:return false;">
		<table class="tableView">
			<tr>
				<td class="label">
					终止原因：
				</td>
				<td>
					<textarea id="memo" name="memo" class="easyui-textarea validate[required]" style="width:350px;height:100px;" promptPosition="bottom"></textarea>
				</td>
			</tr>
		</table>
		</form>		
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>