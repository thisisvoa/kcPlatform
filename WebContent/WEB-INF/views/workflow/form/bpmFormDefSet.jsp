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
var topWin = FrameHelper.getTop();
$parsed(function(){
	$("#editForm").validate("addRule",
		{	name:"validateFormKey",
			rule:{
	           "url": "${ctx}/workflow/formDef/validateFormKey.html",
	           "alertText": "* 表单编号已存在"
			}
		});
});

function next(){
	if($("#editForm").validate("validate")){
		var formDef = {
			subject:$("#subject").val(),	
			formKey:$("#formKey").val(),
			catalogId:$("#catalogId").combotree("getValue"),
			tableId:$("#tableId").val(),
			tableDesc:$("#tableDesc").val(),
			formDesc:$("#formDesc").val()
		};
		parentDialog.setData(formDef);
		parentDialog.markUpdated();
		parentDialog.close();
	}
}

function selectBpmFormTable(){
	var diag = topWin.Dialog.open({
		Title : "选择业务表",
		URL : '${ctx}/workflow/formTable/chooser.html',
		Width : 600,
		Height : 400,
		RefreshHandler : function(){
			var table = diag.getData();
			$("#tableId").val(table.tableId);
			$("#tableDesc").val(table.tableDesc);
			$("#tableDesc").trigger("blur");
		}});
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
						表单标题：
					</td>
					<td>
						<input type="text" class="easyui-textinput validate[required,maxSize[64]]" id="subject" name="subject">
					</td>
				</tr>
				<tr>
					<td class="label">
						表单Key：
					</td>
					<td>
						<input type="text" class="easyui-textinput validate[required,maxSize[32],ajax[validateFormKey]]" id="formKey" name="formKey">
					</td>
				</tr>
				<tr>
					<td class="label">
						表单分类：
					</td>
					<td>
						<select name="catalogId" id="catalogId" class="easyui-combotree validate[required]" url="${ctx}/workflow/bpmCatalog/list.html?catalogType=2"
							idKey="id" nameKey="catalogName" pIdKey="parentId" panelWidth="200" simpleDataEnable="true">
						</select> 
					</td>
				</tr>
				<tr>
					<td class="label">
						业务表：
					</td>
					<td>
						<input type="text" class="easyui-textinput validate[required]" id="tableDesc" name="tableDesc" promptPosition="bottom" readonly="readonly">
						<input type="hidden" id="tableId" name="tableId">
						<input type="button" class="easyui-button" value="请选择" onclick="selectBpmFormTable()"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						表单描述：
					</td>
					<td>
						<textarea class="easyui-textarea" id="formDesc" name="formDesc" style="width:300px;height:100px;"></textarea>
					</td>
				</tr>
			</table>
		</form>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>