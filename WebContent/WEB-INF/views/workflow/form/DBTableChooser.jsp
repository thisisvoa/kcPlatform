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
<script type="text/javascript">
var table = null;
function radioFormat(cellVal,options,rowd){
	return "<input id=\"ra_"+rowd.TABLE_NAME+"\" type=\"radio\" name=\"tableSelectRadio\" value=\""+rowd.TABLE_NAME+"\" onclick=\"gridRadioCheck('"+rowd.TABLE_NAME+"')\">";
}
function gridRadioCheck(selectTableName){
	var rowd = $("#tableList").grid("getRowData", selectTableName);
	table={tableName:selectTableName,tableDesc:rowd.COMMENTS};
}
function doSave(){
	if(table==null){
		MessageUtil.alert("请选择数据表!");
		return;
	}else{
		parentDialog.markUpdated();
		parentDialog.setData(table);
		parentDialog.close();
	}
}
</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:33px">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
				<a id="saveBtn" href="#" iconCls="icon-save" onclick="doSave()">保存</a>
				<a id="close" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center">
		<ui:Grid id="tableList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="false"
				url="${ctx}/workflow/formTable/getAllTables.html" multiselect="false" rownumbers="false"
				rowNum="9999">
				<ui:Column header="" width="30" fixed="true" align="center" formatter="radioFormat"></ui:Column>
				<ui:Column header="表名" name="TABLE_NAME" width="90" key="true"></ui:Column>
				<ui:Column header="描述" name="COMMENTS" width="120"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>