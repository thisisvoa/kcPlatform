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
<title>取消代理</title>
<script type="text/javascript">
function doSave(){
	if($("#editForm").validate("validate")){
		MessageUtil.confirm("确认取消代理吗？？",function(){
			var opinion = $("#opinion").val();
			parentDialog.setData(opinion);
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
			<a id="saveBtn" href="#" iconCls="icon-save" onclick="doSave()">取消</a>
			<a id="closeBtn" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center">
		<form id="editForm" class="easyui-validate" onsubmit="javascript:return false;">
		<table class="tableView">
			<tr>
				<td class="label">
					原&nbsp;&nbsp;因：
				</td>
				<td>
					<textarea id="opinion" name="opinion" class="easyui-textarea validate[required]" style="width:300px;height:130px;" promptPosition="bottom"></textarea>
					<span class="star">*</span>
				</td>
			</tr>
		</table>
		</form>		
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>