
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
	var topWin = FrameHelper.getTop();
	var isUpdate = false;
	$parsed(function(){
		if("${type}"=="update"){
			$("#bpmFormFieldList").grid("setGridParam", {url:"${ctx}/workflow/formField/list.html?tableId=${bpmFormTable.tableId}"});
			$("#bpmFormFieldList").trigger("reloadGrid");	
		}
	});
	function doSave(){
		if($("#editForm").validate("validate")){
			var type = "${type}";
			var table = {tableId:$("#tableId").val(), 
					 tableName:$("#tableName").val(),
					 tableDesc:$("#tableDesc").val(),
					 pkField:$("#pkField").val(),
					 isMain:"1"};
			if(table.isMain=='0'){
				table.mainTableId = $("#mainTableId").combobox("getValue");
			}
			var fieldList = $("#bpmFormFieldList").grid("getRowData"); 
			if(fieldList.length<=0){
				MessageUtil.alert("列不能为空！");
				return;
			}
			for(var i=0;i<fieldList.length;i++){
				delete fieldList[i]['tmp'];
				delete fieldList[i]['undefined'];
			}
			if(type=='add'){
				$.postc("${ctx}/workflow/formTable/add.html",{table:JSON.stringify(table),fields:JSON.stringify(fieldList)},function(){
					parent.saveSuccess();
				});
			}else{
				$.postc("${ctx}/workflow/formTable/update.html",{table:JSON.stringify(table),fields:JSON.stringify(fieldList)},function(){
					parent.saveSuccess();
				});
			}
		}
	}
	
	function doReturn(){
		if(isUpdate){
			MessageUtil.confirm("业务表已修改，确定放弃修改?",function(){
				parent.back();		
			});
		}else{
			parent.back();
		}
		
	}
	
	function addField(){
		var diag = topWin.Dialog.open({
			Title : "新增列",
			URL : "${ctx}/workflow/formField/toAdd.html",
			Width : 800,
			Height : 400,
			RefreshHandler : function(){
				var field = diag.getData();
				field.fieldId = (new Date()).getTime();
				$("#bpmFormFieldList").grid("addRowData", field.fieldId, field);
				isUpdate = true;
			}});
	}
	
	function importFromDb(){
		var diag = topWin.Dialog.open({
			Title : "从数据库导入",
			URL : "${ctx}/workflow/formTable/tableChooser.html",
			Width : 500,
			Height : 400,
			RefreshHandler : function(){
				var table = diag.getData();
				$("#tableName").val(table.tableName);
				$("#tableDesc").val(table.tableDesc);
				setTimeout(function(){
					MessageUtil.progress("生成初始化数据");
					$.postc("${ctx}/workflow/formTable/getTableColumns.html",{tableName:table.tableName},function(data){
						var fieldList = eval("("+data+")");
						$("#bpmFormFieldList").grid("clearGridData");
						for(var i=0;i<fieldList.length;i++){
							var field = fieldList[i];
							field.fieldId = (new Date()).getTime();
							$("#bpmFormFieldList").grid("addRowData", field.fieldId, field);
						}
						isUpdate = true;
						MessageUtil.closeProgress();
					});	
					
				},10);
				
			}});
	}
	
	function editField(id){
		var diag = new topWin.Dialog();
		diag.URL = "${ctx}/workflow/formField/toUpdate.html";
		diag.Width = 800;
		diag.Height = 400;
		diag.Title = "修改列";
		var rowd = $("#bpmFormFieldList").grid("getRowData", id);
		diag.setData(rowd);
		diag.RefreshHandler = function(){
			var field = diag.getData();
			var rowInd = $("#bpmFormFieldList").grid("getInd", field.fieldId);
			var dataIds = $("#bpmFormFieldList").grid("getDataIDs");
			if(rowInd == 1){
				$("#bpmFormFieldList").grid("delRowData", field.fieldId);
				$("#bpmFormFieldList").grid("addRowData", field.fieldId, field, "first");	
			}else if(rowInd==dataIds.length){
				$("#bpmFormFieldList").grid("delRowData", field.fieldId);
				$("#bpmFormFieldList").grid("addRowData", field.fieldId, field, "last");	
			}else{
				var preRowId =  dataIds[rowInd-2];
				$("#bpmFormFieldList").grid("delRowData", field.fieldId);
				$("#bpmFormFieldList").grid("addRowData", field.fieldId, field, "after", preRowId);
			}
			$("#bpmFormFieldList").grid("setSelection",field.fieldId);
			isUpdate = true;
		};
		diag.show();
	}
	
	function showMainTable(){
		if($("#main").attr("checked")){
			$("#MainTableInput").hide();
		}else{
			$("#MainTableInput").show();	
		}
	}
	
	function deleteField(){
		var selectedIds = $("#bpmFormFieldList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要删除的列！");
			return;
		}
		MessageUtil.confirm("确定删除选择的列吗？",function(){
			var ids = selectedIds.concat(); 
			for(var i=0;i<ids.length;i++){
				$("#bpmFormFieldList").grid("delRowData", ids[i]);
			}
		});
		isUpdate = true;
	}
	
	function up(){
		var selectedIds = $("#bpmFormFieldList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要的移动的记录！");
			return;
		}
		if(selectedIds.length>1){
			MessageUtil.alert("只能选择一条记录进行移动 ！");
			return;
		}
		var rowInd = $("#bpmFormFieldList").grid("getInd", selectedIds[0]);
		if(rowInd==1){
			return;
		}else{
			var dataIds = $("#bpmFormFieldList").grid("getDataIDs");
			var rowd = $("#bpmFormFieldList").grid("getRowData", selectedIds[0]);
			var rowid = rowd.fieldId;
			var preRowId =  dataIds[rowInd-2];
			$("#bpmFormFieldList").grid("delRowData", rowid);
			$("#bpmFormFieldList").grid("addRowData", rowid, rowd, "before", preRowId);
			$("#bpmFormFieldList").grid("setSelection",rowid);
			isUpdate = true;
		}
		
	}
	
	function down(){
		var selectedIds = $("#bpmFormFieldList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要的移动的记录！");
			return;
		}
		if(selectedIds.length>1){
			MessageUtil.alert("只能选择一条记录进行移动 ！");
			return;
		}
		var rowInd = $("#bpmFormFieldList").grid("getInd", selectedIds[0]);
		var dataIds = $("#bpmFormFieldList").grid("getDataIDs");
		if(rowInd==dataIds.length){
			return;
		}else{
			var rowd = $("#bpmFormFieldList").grid("getRowData", selectedIds[0]);
			var rowid = rowd.fieldId;
			var afterRowId =  dataIds[rowInd];
			$("#bpmFormFieldList").grid("delRowData", rowid);
			$("#bpmFormFieldList").grid("addRowData", rowid, rowd, "after", afterRowId);
			$("#bpmFormFieldList").grid("setSelection",rowid);
			isUpdate = true;
		}
	}
	
	function setPk(){
		var selectedIds = $("#bpmFormFieldList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要设置为主键的列！");
			return;
		}
		if(selectedIds.length>1){
			MessageUtil.alert("只能选择一个列设置为主键 ！");
			return;
		}
		setAsKey(selectedIds[0]);
	}
	function  setAsKey(id){
		var rowd = $("#bpmFormFieldList").grid("getRowData", id);
		$("#pkField").val(rowd.fieldName);
		isUpdate = true;
	}
	function fieldTypeFormatter(cellVal,options,rowd){
		var fieldType = rowd.fieldType;
		switch(fieldType){
			case "varchar":
				return "varchar("+rowd.charLen+")";
			case "number":
				return "number("+rowd.intLen+","+rowd.decimalLen+")";
			case "integer":
				return "integer";
			case "long":
				return "long";
			case "date":
				return "date";
			case "clob":
				return "clob";
		}
		return "";
	}
	
	function isRequiredFormatter(cellVal,options,rowd){
		var isRequired = rowd.isRequired;
		if(isRequired=="1"){
			return "<input type='checkbox' id='isRequired_"+rowd.fieldId+"' checked='checked' onclick=\"changePropertyValue('isRequired','"+rowd.fieldId+"')\">";
		}else{
			return "<input type='checkbox' id='isRequired_"+rowd.fieldId+"' onclick=\"changePropertyValue('isRequired','"+rowd.fieldId+"')\">";
		}
	}
	
	function isHiddenFormatter(cellVal,options,rowd){
		var isHidden = rowd.isHidden;
		if(isHidden=="1"){
			return "<input type='checkbox' id='isHidden_"+rowd.fieldId+"' checked='checked' onclick=\"changePropertyValue('isHidden','"+rowd.fieldId+"')\">";
		}else{
			return "<input type='checkbox' id='isHidden_"+rowd.fieldId+"' onclick=\"changePropertyValue('isHidden','"+rowd.fieldId+"')\">";
		}
	}
	
	function isQueryFormatter(cellVal,options,rowd){
		var isQuery = rowd.isQuery;
		if(isQuery=="1"){
			return "<input type='checkbox' id='isQuery_"+rowd.fieldId+"' checked='checked' onclick=\"changePropertyValue('isQuery','"+rowd.fieldId+"')\">";
		}else{
			return "<input type='checkbox' id='isQuery_"+rowd.fieldId+"' onclick=\"changePropertyValue('isQuery','"+rowd.fieldId+"')\">";
		}
	}
	
	
	function isListFormatter(cellVal,options,rowd){
		var isList = rowd.isList;
		if(isList=="1"){
			return "<input type='checkbox' id='isList_"+rowd.fieldId+"' checked='checked' onclick=\"changePropertyValue('isList','"+rowd.fieldId+"')\">";
		}else{
			return "<input type='checkbox' id='isList_"+rowd.fieldId+"' onclick=\"changePropertyValue('isList','"+rowd.fieldId+"')\">";
		}
	}
	
	function isFlowvarFormatter(cellVal,options,rowd){
		var isFlowvar = rowd.isFlowvar;
		if(isFlowvar=="1"){
			return "<input type='checkbox' id='isFlowvar_"+rowd.fieldId+"' checked='checked' onclick=\"changePropertyValue('isFlowvar','"+rowd.fieldId+"')\">";
		}else{
			return "<input type='checkbox' id='isFlowvar_"+rowd.fieldId+"' onclick=\"changePropertyValue('isFlowvar','"+rowd.fieldId+"')\">";
		}
	}
	
	function isDeletedFormatter(cellVal,options,rowd){
		return "";
	}
	function operaFormatter(cellVal,options,rowd){
		var html = "";
		html += "<a href='#' class='easyui-linkbutton' style='margin:1px' plain='true' iconCls='icon-edit' onclick=\"javascript:editField('"+rowd.fieldId+"')\" >编辑</a>";
		html += "<a href='#' class='easyui-linkbutton' style='margin:1px' plain='true' iconCls='icon-key' onclick=\"javascript:setAsKey('"+rowd.fieldId+"')\" >设为主键</a>";
		return html;
	}
	
	function changePropertyValue(property, rowid){
		var ctlId = "#"+property+"_"+rowid ;
		var rowd = $("#bpmFormFieldList").grid("getRowData", rowid);
		if($(ctlId).attr("checked")){
			rowd[property] = "1";
		}else{
			rowd[property] = "0";
		}
		$("#bpmFormFieldList").grid("setCell", rowid, property, rowd[property]);
		isUpdate = true;
	}
	
	function onGridComplete(){
		using("button", function(){
			$(".easyui-linkbutton").linkbutton();
		});
	}
</script>
<body>
<div class="easyui-layout" fit="true">
	<div region="north" style="height:100px" <c:if test="${type eq 'add'}">title="新增业务表"</c:if> <c:if test="${type eq 'update'}">title="修改业务表"</c:if>>
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="save" href="#" iconCls="icon-save" onclick="doSave()">保存</a>
			<a id="return" href="#" iconCls="icon-back" onclick="doReturn()">返回</a>
			<a id="add" href="#" iconCls="icon-add" onclick="addField()">添加列</a>
			<a id="up" href="#" iconCls="icon-moveup" onclick="up()">上移</a>
			<a id="down" href="#" iconCls="icon-movedown" onclick="down()">下移</a>
			<a id="remove" href="#" iconCls="icon-remove" onclick="deleteField()">删除</a>
			<a id="key" href="#" iconCls="icon-key" onclick="setPk()">设置主键</a>
			<c:if test="${type eq 'add'}">
				<a id="import" href="#" iconCls="icon-upload" onclick="importFromDb()">从数据库导入</a>
			</c:if>
		</div>
		<form id="editForm" class="easyui-validate" style="margin-top:5px;">
			<input type="hidden" name="tableId" id="tableId" value="${bpmFormTable.tableId}"/>
			<table class="formTable">
				<tr>
					<td class="label">
						表名：
					</td>
					<td style="width:150px">
						<input type="text" class="easyui-textinput validate[required]" id="tableName" name="tableName" value="${bpmFormTable.tableName}" />
					</td>
					<td class="label">
						表描述：
					</td>
					<td style="width:150px">
						<input type="text" class="easyui-textinput validate[required]" id="tableDesc" name="tableDesc" value="${bpmFormTable.tableDesc}" />
					</td>
					<td class="label">
						主键：
					</td>
					<td style="width:150px">
						<input type="text" class="easyui-textinput validate[required]" id="pkField" name="pkField" value="${bpmFormTable.pkField}" readonly="readonly"/>
					</td>
					<td class="label" style="display:none">
						是否主表：
					</td>
					<td style="width:80px">
						<input type="hidden" id="main" name="isMain" value="1" />
						<!--  
						<input type="radio" name="isMain" id="main" class="validate[required]" value="1" <c:if test="${bpmFormTable.isMain eq '1' || type eq 'add' }">checked="checked"</c:if> onclick="showMainTable()">主表
						<input type="radio" name="isMain" id="notMain" class="validate[required]" value="0"  <c:if test="${bpmFormTable.isMain eq '0'}">checked="checked"</c:if> onclick="showMainTable()">从表
						-->
					</td>
					<td class="label" id="MainTableLabel" <c:if test="${bpmFormTable.isMain eq '1' || type eq 'add' }">style="display:none"</c:if>>
						主表：
					</td>
					<td id="MainTableInput" style="display:none">
						<select id="mainTableId" name="mainTableId" class="easyui-combobox validate[required]">
						</select>
					</td>
					<td>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div region="center" border="false">
		<ui:Grid id="bpmFormFieldList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="false"
				multiselect="true" rownumbers="false"
				rowNum="9999" gridComplete="onGridComplete">
				<ui:Column name="fieldId" key="true" hidden="true"></ui:Column>
				<ui:Column name="tableId" hidden="true"></ui:Column>
				<ui:Column name="fieldType" hidden="true"></ui:Column>
				<ui:Column name="isRequired" hidden="true"></ui:Column>
				<ui:Column name="isList" hidden="true"></ui:Column>
				<ui:Column name="isQuery" hidden="true"></ui:Column>
				<ui:Column name="charLen" hidden="true"></ui:Column>
				<ui:Column name="intLen" hidden="true"></ui:Column>
				<ui:Column name="decimalLen" hidden="true"></ui:Column>
				<ui:Column name="dictType" hidden="true"></ui:Column>
				<ui:Column name="isDeleted" hidden="true"></ui:Column>
				<ui:Column name="validRule" hidden="true"></ui:Column>
				<ui:Column name="originalName" hidden="true"></ui:Column>
				<ui:Column name="sn" hidden="true"></ui:Column>
				<ui:Column name="valueFrom" hidden="true"></ui:Column>
				<ui:Column name="script" hidden="true"></ui:Column>
				<ui:Column name="controlType" hidden="true"></ui:Column>
				<ui:Column name="isHidden" hidden="true"></ui:Column>
				<ui:Column name="isFlowvar" hidden="true"></ui:Column>
				<ui:Column name="identity" hidden="true"></ui:Column>
				<ui:Column name="options" hidden="true"></ui:Column>
				<ui:Column name="ctlProperty" hidden="true"></ui:Column>
				<ui:Column name="optionFrom" hidden="true"></ui:Column>
				<ui:Column header="列名" name="fieldName" width="90"></ui:Column>
				<ui:Column header="列描述" name="fieldDesc" width="120"></ui:Column>
				<ui:Column header="类型" width="60" formatter="fieldTypeFormatter"></ui:Column>
				<ui:Column header="必填" width="60" formatter="isRequiredFormatter" align="center"></ui:Column>
				<ui:Column header="是否隐藏项" width="60" formatter="isHiddenFormatter" align="center"></ui:Column>
				<ui:Column header="查询条件" width="60" formatter="isQueryFormatter" align="center"></ui:Column>
				<ui:Column header="显示到列表" width="60" formatter="isListFormatter" align="center"></ui:Column>
				<ui:Column header="是否流程变量" width="60" formatter="isFlowvarFormatter" align="center"></ui:Column>
				<ui:Column header="管理" name="tmp" formatter="operaFormatter" width="150" align="center" fixed="true"></ui:Column>
		</ui:Grid>
	</div>
</div>
</body>
</html>