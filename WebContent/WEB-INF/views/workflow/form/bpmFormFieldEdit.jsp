
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
<script type="text/javascript" src="${ctx}/ui/platform/workflow/form/EditTable.js"></script>
<script type="text/javascript" src="${ctx}/ui/js/util/json2.js"></script>
<title></title>
<script type="text/javascript">
	$parsed(function(){
		var type = "${type}";
		if(type=='update'){
			var field = {};
			$.extend(field,parentDialog.getData());
			initFieldValue(field);
		}
	});
	function doSave(){
		if($("#editForm").validate("validate")){
			var field = getFieldValue();
			parentDialog.setData(field);
			parentDialog.markUpdated();
			parentDialog.close();
		}
	}
</script>
<body>
<ui:Panel fit="true">
	<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
		<c:if test="${type eq 'add' }">
			<a id="add" href="#" iconCls="icon-add" onclick="doSave()">添加</a>
		</c:if>
		<c:if test="${type eq 'update' }">
			<a id="update" href="#" iconCls="icon-save" onclick="doSave()">保存</a>
		</c:if>
		<a id="update" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
	</div>
	<form id="editForm" class="easyui-validate" style="margin-top:5px;">
		<input type="hidden" name="fieldId" id="fieldId" />
		<table class="tableView">
			<tr>
				<td class="label">
					<span class="star">*</span>字段描述：
				</td>
				<td style="width:230px;">
					<input type="text" class="easyui-textinput validate[required,maxSize[64]]" id="fieldDesc" name="fieldDesc" />
				</td>
				<td class="label">
					<span class="star">*</span>字段名称：
				</td>
				<td>
					<input type="text" class="easyui-textinput validate[required,maxSize[64]]" id="fieldName" name="fieldName" />
				</td>
			</tr>
			<tr>
				<td class="label">
					<span class="star">*</span>字段类型：
				</td>
				<td>
					<select id="fieldType" name="fieldType" class="easyui-combobox validate[required]" dropdownHeight="105" onChange="onFieldTypeChange">
						<option value="varchar">文字</option>
						<option value="number">数字</option>
						<option value="integer">整数</option>
						<option value="long">长整数</option>
						<option value="date">日期</option>
						<option value="clob">大文本</option>
					</select>
				</td>
				<td class="label">
					<span class="star">*</span>长度：
				</td>
				<td>
					<div id="spanCharLen">
						<input type="text" class="easyui-textinput  validate[required,custom[integer]]" id="charLen" name="charLen" value="50" />
					</div>
					<span id="spanIntLen" style="display: none">
						<label><span class="star">*</span>整数位:</label>
						<input type="text" class="easyui-textinput validate[required,custom[integer]]" id="intLen" name="intLen" style="width:40px" />
					</span>
					<span id="spanDecimalLen" style="display: none">
						<label><span class="star">*</span>小数位:</label>
						<input type="text" class="easyui-textinput validate[required,custom[integer]]" id="decimalLen" name="decimalLen" style="width:40px"/>
					</span>
				</td>
			</tr>
			<tr id="trDateFormat" style="display:none">
				<td class="label">
					<span class="star">*</span>日期格式：
				</td>
				<td>
					<select id="dateFormat" class="easyui-combobox" dropdownHeight="75">
						<option value="yyyy-MM-dd">yyyy-MM-dd</option>
						<option value="yyyy-MM-dd HH:mm:ss">yyyy-MM-dd HH:mm:ss</option>
						<option value="yyyy-MM-dd HH:mm:00">yyyy-MM-dd HH:mm</option>
					</select>
				</td>
				<td class="label">
					取当前时间：
				</td>
				<td>
					<input type="checkbox" class="easyui-checkbox " id="isCurrentDate" name="isCurrentDate"/>
				</td>
			</tr>
			<tr>
				<td class="label">
					选项：
				</td>
				<td colspan="3">
					<input type='checkbox' class="easyui-checkbox" id="isHidden" name="isHidden">&nbsp;隐藏项&nbsp;&nbsp;
					<input type='checkbox' class="easyui-checkbox" id="isRequired" name="isRequired">&nbsp;必填&nbsp;&nbsp;
					<input type="checkbox" class="easyui-checkbox " id="isQuery" name="isQuery"/>&nbsp;查询条件&nbsp;&nbsp;
					<input type="checkbox" class="easyui-checkbox " id="isList" name="isList" />&nbsp;列表显示&nbsp;&nbsp;
					<input type="checkbox" class="easyui-checkbox " id="isFlowvar" name="isFlowvar"/>&nbsp;流程变量
				</td>
			</tr>
			<tr>
				<td class="label">
					<span class="star">*</span>控件类型:
				</td>
				<td>
					<input type="hidden" id="valueFrom" name="valueFrom" value="1">
					<select id="controlType" name="controlType" class="easyui-combobox validate[required]" onChange="controlTypeChange">
						<!--<option value="1">隐藏域</option>-->
						<option value="2">单行文本框</option>
						<option value="3">多行文本框</option>
						<option value="4">下拉框</option>
						<!--<option value="5">下拉树</option>-->
						<option value="6">复选框</option>
						<option value="7">单选框</option>
						<option value="8">日期控件</option>
						<option value="9">人员选择器(单选)</option>
						<option value="10">人员选择器(多选)</option>
						<option value="11">单位选择器</option>
						<option value="12">角色选择器</option>
					</select>
				</td>
				<td class="label">
					验证规则:
				</td>
				<td>
					<span id="spanValidRule" style="display:none">
						<select id="validRule" name="validRule" class="easyui-combobox" showEmptySelect="true"
							 url="${ctx}/codeCtl/getCodeItemList.html?dmjc=BPM_FORM_VALI" valueField="dmx_bh" textField="dmx_mc">
						</select>
					</span>
				</td>
			</tr>
			<tr id="trOptionFrom" style="display:none">
				<td class="label">
					<span class="star">*</span>选项来源：
				</td>
				<td colspan="3">
					<select id="optionFrom" name="optionFrom" class="easyui-combobox validate[required]" dropdownHeight="75" onChange="optionFromChange">
						<option value="1">固定项</option>
						<option value="2">字典表</option>
					</select>
				</td>
			</tr>
			<tr id="trDictType" style="display:none">
				<td class="label">
					<span class="star">*</span>字典表类型：
				</td>
				<td colspan="3">
					<select id="dictType" name="dictType" class="easyui-combobox validate[required]" showEmptySelect="true"
						 url="${ctx}/codeCtl/getCodeList.html" valueField="dmjc" textField="dmmc">
					</select>
				</td>
			</tr>
			<tr id="trOptions" style="display:none">
				<td class="label">
					<span class="star">*</span>下拉选项：
				</td>
				<td colspan="3">
					每个项目用分号隔开(如：0:女;1:男)：<br>
					<textarea id="options" name="options" style="width:600px;height:80px;" class="easyui-textarea validate[required]"></textarea>
				</td>
			</tr>
		</table>
	</form>
</ui:Panel>
</body>
</html>