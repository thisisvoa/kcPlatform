
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
<script type="text/javascript" charset="utf-8" src="${ctx}/ui/js/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${ctx}/ui/js/ueditor/editor_api.js"></script>
<title></title>
<script type="text/javascript">
var editor;

$parsed(function(){
	var h = $("#htmlDom").height();
	$("#txtHtml").height(h-105);
	editor = new UE.ui.Editor();
	editor.render("txtHtml");
});
function doSave(){
	if($("#editForm").validate("validate")){
		var parms = $("#editForm").serialize();
		var type = "${type}";
		if(type=="add"){
			$.postc("${ctx}/workflow/formDef/add.html", parms, function(data){
				MessageUtil.show("添加成功！");
				$("#reAddBtn").removeAttr("disabled");
				$("#saveBtn").attr("disabled",true);
				parentDialog.markUpdated();
			});
		}else if(type=="update"){
			$.postc("${ctx}/workflow/formDef/update.html", parms, function(data){
				MessageUtil.show("修改成功！");
				parentDialog.markUpdated();
			});
		}
	}
}
function reAdd(){
	$("#editForm")[0].reset();
	$("#saveBtn").removeAttr("disabled");
	$("#reAddBtn").attr("disabled",true);
}

function dataFilter(event, parentNode, childNodes){
	if(!childNodes) return null;
	for(var i=0;i<childNodes.length;i++){
		childNodes[i].icon = "${ctx}/ui/css/icon/images/"+childNodes[i].fieldType+".png";	
	}
	return childNodes;
}
</script>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:100px">
		<div id="toolbar" class="easyui-toolbar" style="width:100%;">
			<a id="save" href="#" iconCls="icon-save" onclick="doSave()">保存</a>
			<a id="close" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
		</div>
		<form id="editForm" class="easyui-validate">
			<input type="hidden" name="formDefId" id="formDefId" value="${bpmFormDef.formDefId}"/>
			<table class="formTable">
				<tr>
					<td class="label" style="width:100px">
						表单分类：
					</td>
					<td style="width:150px">
						<select name="catalogId" id="catalogId" class="easyui-combotree validate[required]" url="${ctx}/workflow/bpmCatalog/list.html?catalogType=2"
							idKey="id" nameKey="catalogName" pIdKey="parentId" panelWidth="200" simpleDataEnable="true" selValue="${bpmFormDef.catalogId}">
						</select>
					</td>
					<td class="label" style="width:100px">
						表单标题：
					</td>
					<td style="width:150px">
						<input type="text" class="easyui-textinput " id="subject" name="subject" value="${bpmFormDef.subject}" />
					</td>
					
					<td class="label" style="width:100px">
						表单Key：
					</td>
					<td style="width:150px">
						<input type="text" class="easyui-textinput " id="subject" name="subject" value="${bpmFormDef.subject}" />
					</td>
				</tr>
				<tr>
					<td class="label" style="width:100px">
						业务表：
					</td>
					<td style="width:150px">
						<input type="text" class="easyui-textinput" id="tableDesc" name="tableDesc" value="${bpmFormDef.tableDesc}" readonly="readonly">
						<input type="hidden" id="tableId" name="tableId" value="${bpmFormDef.tableId}">
					</td>
					<td class="label" style="width:100px">
						描述：
					</td>
					<td colspan="3">
						<input type="text" class="easyui-textinput " id="formDesc" name="formDesc" style="width:250px" value="${bpmFormDef.formDesc}" />
					</td>
				</tr>
			</table>
		</form>
	</ui:Layout>
	<ui:Layout region="west" style="width:200px" title="表字段" split="true">
		<ui:LayoutContainer fit="true">
			<ui:Layout region="north" style="height:33px" border="false">
				<div id="toolbar" class="easyui-toolbar">
					<a id="refresh" href="#" iconCls="icon-refresh" onclick="refreshFields()">刷新</a>
				</div>
			</ui:Layout>
			<ui:Layout region="center">
				<ul id="fieldTree" class="easyui-tree" asyncEnable="true"  
						url="${ctx}/workflow/formField/getFieldByTable.html?tableId=${bpmFormDef.tableId}" 
			  			idKey="fieldId" nameKey="fieldDesc" pIdKey="tableId" rootPId="0" 
			  			simpleDataEnable="true" dataFilter="dataFilter"></ul>
			</ui:Layout>
		</ui:LayoutContainer>
	</ui:Layout>
	<ui:Layout region="center" id="htmlDom" style="padding:1px">
		<textarea id="txtHtml" name="html" style="width:100%;">${bpmFormDef.html}</textarea>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>