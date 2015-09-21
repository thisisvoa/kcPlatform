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
function doReturn(){
	parent.back();
}
function fieldTypeFormatter(cellVal,options,rowd){
		var fieldType = rowd.fieldType;
		switch(fieldType){
			case "varchar":
				return "varchar("+rowd.charLen+")";
			case "number":
				return "number("+rowd.intLen+","+rowd.decimalLen+")";
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
			return "√";
		}else{
			return "";
		}
	}
	function isQueryFormatter(cellVal,options,rowd){
		var isQuery = rowd.isQuery;
		if(isQuery=="1"){
			return "√";
		}else{
			return "";
		}
	}
	
	
	function isListFormatter(cellVal,options,rowd){
		var isList = rowd.isList;
		if(isList=="1"){
			return "√";
		}else{
			return "";
		}
	}
	
	function isFlowvarFormatter(cellVal,options,rowd){
		var isFlowvar = rowd.isFlowvar;
		if(isFlowvar=="1"){
			return "√";
		}else{
			return "";
		}
	}
	
	function isDeletedFormatter(cellVal,options,rowd){
		return "";
	}
	
</script>
<body>
<ui:LayoutContainer fit="true">
<ui:Layout region="north" style="height:155px">
	<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="return" href="#" iconCls="icon-back" onclick="doReturn()">返回</a>
	</div>
	<table class="tableView">
		<tr>
			<td class="label" style="width:100px">
				表名：
			</td>
			<td>
				${bpmFormTable.tableName}
			</td>
		</tr>
		<tr>
			<td class="label" style="width:100px">
				表描述：
			</td>
			<td>
				${bpmFormTable.tableDesc}
			</td>
		</tr>
		<tr>
			<td class="label" style="width:100px">
				主键字段：
			</td>
			<td>
				${bpmFormTable.pkField}
			</td>
		</tr>
		<tr>
			<td class="label" style="width:100px">
				是否主表：
			</td>
			<td>
				<c:if  test="${bpmFormTable.isMain eq '1'}">是</c:if>
				<c:if  test="${bpmFormTable.isMain eq '0'}">否</c:if>
			</td>
		</tr>
	</table>
</ui:Layout>
<ui:Layout region="center" border="false">
	<ui:Grid id="bpmFormFieldList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="false"
				url="${ctx}/workflow/formField/list.html?tableId=${bpmFormTable.tableId}" multiselect="true" rownumbers="false"
				rowNum="9999" >
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
			<ui:Column header="查询条件" width="60" formatter="isQueryFormatter" align="center"></ui:Column>
			<ui:Column header="显示到列表" width="90" formatter="isListFormatter" align="center"></ui:Column>
			<ui:Column header="是否流程变量" width="90" formatter="isFlowvarFormatter" align="center"></ui:Column>
			<ui:Column header="是否删除" width="80" formatter="isDeletedFormatter"></ui:Column>
	</ui:Grid>
</ui:Layout>
</ui:LayoutContainer>
</body>
</html>