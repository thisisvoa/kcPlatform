
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
				{	name:"validateType",
					rule:{
			           "url": "${ctx}/workflow/bpmDef/nodeButton/validateType.html?btnId=${bpmNodeButton.btnId}&nodeId=${bpmNodeButton.nodeId}&defId=${bpmNodeButton.defId}&nodeType=${bpmNodeButton.nodeType}",
			           "alertText": "* 已存在相同的操作类型"
					}
				});
		editAreaLoader.init({
			id: "beforeHandler",
			start_highlight: true,
			allow_toggle: false,
			language: "zh",
			syntax: "js",
			toolbar: "search, go_to_line, |, undo, redo, |, select_font, |, syntax_selection, |, change_smooth_selection, highlight, reset_highlight, |, help",
			syntax_selection_allow: "js",
			is_multi_files: false,
			show_line_colors: true
		});
		editAreaLoader.init({
			id: "afterHandler",
			start_highlight: true,
			allow_toggle: false,
			language: "zh",
			syntax: "js",
			toolbar: "search, go_to_line, |, undo, redo, |, select_font, |, syntax_selection, |, change_smooth_selection, highlight, reset_highlight, |, help",
			syntax_selection_allow: "js",
			is_multi_files: false,
			show_line_colors: true
		});
		
	});
	function doSave(){
		if($("#editForm").validate("validate")){
			var parms = $("#editForm").serialize();
			var beforeHandler = editAreaLoader.getValue("beforeHandler");
			var afterHandler = editAreaLoader.getValue("afterHandler");
			parms += ("&beforeHandler="+beforeHandler);
			parms += ("&afterHandler="+afterHandler);
			var type = "${type}";
			if(type=="add"){
				$.postc("${ctx}/workflow/bpmDef/nodeButton/add.html", parms, function(data){
					MessageUtil.show("添加成功！");
					$("#saveBtn").linkbutton("disable");
					$("#reAddBtn").linkbutton("enable");
					parentDialog.markUpdated();
				});
			}else if(type=="update"){
				$.postc("${ctx}/workflow/bpmDef/nodeButton/update.html", parms, function(data){
					MessageUtil.show("修改成功！");
					parentDialog.markUpdated();
				});
			}
		}
	}
	function reAdd(){
		$("#editForm")[0].reset();
		$("#saveBtn").linkbutton("enable");
		$("#reAddBtn").linkbutton("disable");
	}
	
	function typeChange(){
		var type = $("#type").combobox("getValue");
		var opt = $("#type").combobox("getOptionByValue",type);
		$("#btnName").val(opt.text);
		$("#btnName").trigger("blur");
		if(opt.script=="0"){
			$("#beforeHandlerTr").hide();
			$("#afterHandlerTr").hide();
		}else{
			$("#beforeHandlerTr").show();
			$("#afterHandlerTr").show();
		}
	}
</script>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:33px;" border="false">
		<div id="toolbar" class="easyui-toolbar">
			<a id="saveBtn" href="#" iconCls="icon-save" onclick="doSave()">保存</a>
			<c:if test="${type=='add'}">
				<a id="reAddBtn" href="#" iconCls="icon-redo" onclick="reAdd();">再次添加</a>
			</c:if>
			<a id="close" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center" border="false">
		<form id="editForm" class="easyui-validate">
			<input type="hidden" name="btnId" id="btnId" value="${bpmNodeButton.btnId}"/>
			<input type="hidden" name="defId" id="defId" value="${bpmNodeButton.defId}"/>
			<input type="hidden" name="actDefId" id="actDefId" value="${bpmNodeButton.actDefId}"/>
			<input type="hidden" name="nodeId" id="nodeId" value="${bpmNodeButton.nodeId}"/>
			<input type="hidden" name="nodeType1" id="nodeType1" value="${bpmNodeButton.nodeType}"/>
			<table class="formTable">
				<tr>
					<td class="label">
						操作类型：
					</td>
					<td>
						<select id="type" name="type" class="easyui-combobox validate[required,ajax[validateType]]"
									url="${ctx}/workflow/bpmDef/nodeButton/btnTypesList.html" showEmptySelect="true"
									valueField="operatortype" textField="text" selValue="${bpmNodeButton.type}" onChange="typeChange">
						</select>
						<span class="star">*</span>
					</td>
				</tr>
				<tr>
					<td class="label">
						按钮名称：
					</td>
					<td>
						<input type="text" class="easyui-textinput validate[required]" id="btnName" name="btnName" value="${bpmNodeButton.btnName}" />
						<span class="star">*</span>
					</td>
				</tr>
				<tr id="beforeHandlerTr">
					<td class="label">
						前置脚本：
					</td>
					<td>
						<textarea id="beforeHandler" style="width:600px;height:180px;"  class="editArea">${bpmNodeButton.beforeHandler}</textarea>
					</td>
				</tr>
				<tr id="afterHandlerTr">
					<td class="label">
						后置脚本：
					</td>
					<td>
						<textarea id="afterHandler" style="width:600px;height:180px;"  class="editArea">${bpmNodeButton.afterHandler}</textarea>
					</td>
				</tr>
			</table>
		</form>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>