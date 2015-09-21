
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
<title></title>
<script type="text/javascript">
	$parsed(function(){
		$("#editForm").validate("addRule",
				{	name:"validateVarKey",
					rule:{
			           "url": "${ctx}/workflow/bpmDef/var/validateVarKey.html?varId=${bpmDefVar.varId}",
			           "alertText": "* 此变量Key已存在"
					}
				});
	});
	function doSave(){
		if($("#editForm").validate("validate")){
			var parms = $("#editForm").serialize();
			var type = "${type}";
			if(type=="add"){
				$.postc("${ctx}/workflow/bpmDef/var/addBpmDefVar.html", parms, function(data){
					MessageUtil.show("添加成功！");
					$("#saveBtn").linkbutton("disable");
					$("#reAddBtn").linkbutton("enable");
					parentDialog.markUpdated();
				});
			}else if(type=="update"){
				$.postc("${ctx}/workflow/bpmDef/var/updateBpmDefVar.html", parms, function(data){
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
</script>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:33px" border="false">
		<div id="toolbar" class="easyui-toolbar">
			<a id="saveBtn" href="#" iconCls="icon-save" onclick="doSave()">保存</a>
			<c:if test="${type=='add'}">
				<a id="reAddBtn" href="#" iconCls="icon-redo" onclick="reAdd();">再次添加</a>
			</c:if>
			<a id="close" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center" border="false">
		<form id="editForm" class="easyui-validate" style="margin-top:5px;">
			<input type="hidden" name="varId" id="varId" value="${bpmDefVar.varId}"/>
			<input type="hidden" name="defId" id="defId" value="${bpmDefVar.defId}"/>
			<table class="formTable">
				<tr>
					<td class="label">
						变量名称：
					</td>
					<td>
					<input type="text" class="easyui-textinput validate[required,maxSize[64]]" id="varName" name="varName" value="${bpmDefVar.varName}" />
					<span class="star">*</span>
					</td>
				</tr>
				<tr>
					<td class="label">
						变量Key：
					</td>
					<td>
					<input type="text" class="easyui-textinput validate[required,maxSize[64],ajax[validateVarKey]]" id="varKey" name="varKey" value="${bpmDefVar.varKey}" />
					<span class="star">*</span>
					</td>
				</tr>
				<tr>
					<td class="label">
						变量数据类型：
					</td>
					<td>
						<select class="easyui-combobox " id="varDataType" name="varDataType" 
								selValue="${bpmDefVar.varDataType}">
								<option value="varchar">字符串(varchar)</option>
								<option value="number">数字(number)</option>
								<option value="date">日期(date)</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="label">
						默认值：
					</td>
					<td>
					<input type="text" class="easyui-textinput validate[maxSize[128]]" id="defValue" name="defValue" value="${bpmDefVar.defValue}" />
					</td>
				</tr>
			</table>
		</form>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>