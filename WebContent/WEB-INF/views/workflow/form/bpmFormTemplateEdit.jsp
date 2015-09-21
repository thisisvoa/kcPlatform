
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
<title></title>
<script type="text/javascript">
	$parsed(function(){
		$("#editForm").validate("addRule",
			{	name:"validateAlias",
				rule:{
		           "url": "${ctx}/workflow/formTemplate/validateAlias.html?templateId=${bpmFormTemplate.templateId}",
		           "alertText": "* 此编号已存在"
				}
			});
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
	function doSave(){
		if($("#editForm").validate("validate")){
			var parms = {
				templateId:$("#templateId").val(),
				canEdit:$("#canEdit").val(),
				templateName:$("#templateName").val(),
				alias:$("#alias").val(),
				templateType:$("#templateType").combobox("getValue"),
				html:Utils.escapeHtml(editAreaLoader.getValue("html")),
				templateDesc:$("#templateDesc").val(),
				macroTemplateAlias:$("#macroTemplateAlias").combobox("getValue")
			};
			var type = "${type}";
			if(type=="add"){
				$.postc("${ctx}/workflow/formTemplate/add.html", parms, function(data){
					$("#save").linkbutton("disable");
					$("#readd").linkbutton("enable");
					parentDialog.markUpdated();
					parentDialog.close();
				});
			}else if(type=="update"){
				$.postc("${ctx}/workflow/formTemplate/update.html", parms, function(data){
					parentDialog.markUpdated();
					parentDialog.close();
				});
			}
		}
	}
	function reAdd(){
		$("#editForm")[0].reset();
		$("#save").linkbutton("enable");
		$("#readd").linkbutton("disable");
	}
	
	function onChange(){
		var templateType = $("#templateType").combobox("getValue");
		if(templateType=='3'){
			$("#macroTemplateAlias").combobox("setValue","");
			$("#macroTemplateAliasSpan").hide();
		}else{
			$("#macroTemplateAliasSpan").show();
		}
	}
</script>
<body>
<ui:Panel fit="true">
	<div id="toolbar" class="easyui-toolbar">
		<a id="save" href="#"  iconCls="icon-save" onclick="doSave()">保存</a>
		<c:if test="${type eq 'add'}">
		<a id="readd" href="#"  iconCls="icon-add" onclick="reAdd()" disabled="true">再次添加</a>
		</c:if>
		<a id="close" href="#"  iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
	</div>
	<form id="editForm" class="easyui-validate" style="margin-top:5px;">
		<input type="hidden" name="templateId" id="templateId" value="${bpmFormTemplate.templateId}"/>
		<input type="hidden" id="canEdit" name="canEdit" value="${bpmFormTemplate.canEdit}" />
		<table class="formTable">
			<tr>
				<td class="label">
					模板名称：
				</td>
				<td style="width:150px">
				<input type="text" class="easyui-textinput validate[required,maxSize[32]]" id="templateName" name="templateName" value="${bpmFormTemplate.templateName}" /><span class="star">*</span>
				</td>
				<td class="label">
					别名：
				</td>
				<td>
				<input type="text" class="easyui-textinput validate[required,ajax[validateAlias],maxSize[32]]" id="alias" name="alias" value="${bpmFormTemplate.alias}" /><span class="star">*</span>
				</td>
			</tr>
			<tr>
				<td class="label">
					模板类型：
				</td>
				<td>
					<select id="templateType" name="templateType" class="easyui-combobox validate[required]" dropdownHeight="75" selValue="${bpmFormTemplate.templateType}" onChange="onChange">
						<option value="">--请选择--</option>
						<option value="1">主表模板</option>
						<option value="3">宏模板</option>
					</select><span class="star">*</span>
				</td>
				<td class="label">
					宏模板：
				</td>
				<td>
					<span id="macroTemplateAliasSpan" <c:if test="${bpmFormTemplate.templateType eq '3'}">style='display:none'</c:if>>
					<select id="macroTemplateAlias" name="macroTemplateAlias" class="easyui-combobox validate[required]" showEmptySelect="true" selValue="${bpmFormTemplate.macroTemplateAlias}"
							url="${ctx}/workflow/formTemplate/getTemplateList.html?templateType=3" valueField="alias" textField="templateName">
					</select>
					</span>
				</td>
			</tr>
			<tr>
				<td class="label">
					模版html：
				</td>
				<td colspan="3">
				<textarea id="html" style="width:90%;height:450px;"  class="editArea">${bpmFormTemplate.html}</textarea>
				</td>
			</tr>
			<tr>
				<td class="label">
					描述：
				</td>
				<td colspan="3">
				<textarea class="easyui-textarea validate[maxSize[128]]" style="width:600px;height:50px;" id="templateDesc" name="templateDesc" >${bpmFormTemplate.templateDesc}</textarea>
				</td>
			</tr>
		</table>
	</form>
</ui:Panel>
</body>
</html>