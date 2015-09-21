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
		MessageUtil.confirm("是否确认撤销？",function(){
			var data = $("#editForm").serialize();
			$.postc("${ctx}/workflow/bpm/processRun/recover.html",data, function(){
				parentDialog.markUpdated();
				parentDialog.close();
			});
		});
	}
}

</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:33px">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="saveBtn" href="#" iconCls="icon-redo" onclick="doSave()">撤销</a>
			<a id="closeBtn" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center">
		<form id="editForm" class="easyui-validate" onsubmit="javascript:return false;">
		<table class="tableView">
			<tr>
				<td class="label">
					撤销原因：
				</td>
				<td>
					<input type="hidden" name="runId" value="${runId }"/>
					<input type="hidden" name="backToStart" value="${backToStart}"/>
					<textarea id="opinion" name="opinion" class="easyui-textarea validate[required,maxSize[256]]" style="width:300px;height:130px;" promptPosition="bottom"></textarea>
					<span class="star">*</span>
				</td>
			</tr>
		</table>
		</form>		
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>