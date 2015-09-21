<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="/tags/ui" %>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<script type="text/javascript">
var topWin = FrameHelper.getTop();
function viewUserFormatter(cellVal,options,rowd){
	var html = "";
	html += "<a style=\"color: blue\" onclick=\"javascript:viewUser('"+rowd.ZJID+"');\">" + cellVal + "</a>";
	return html;
}
function viewUser(userId){
	topWin.Dialog.open({
			URL : "${ctx}/userCtl/toViewUser.html?id="+userId,
			Width: 500,
			Height : 540,
			Title : "用户详细信息"
		});
}
</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="padding:2px;height:145px;" border="false">
		<table style="width:100%">
			<tr>
				<td align="center">
				<input type="button" class="easyui-button" value="关 闭 " onclick="javascript:parentDialog.close();"/>
				</td>
			</tr>
		</table>
		<fieldset class="easyui-fieldset" allowCollapsed="false" style="padding:2px">
			<legend>角色详细信息</legend>
			<table class="tableView">
				<tr>
					<td class="label">角色名称：</td>
					<td>${roleInfo.jsmc}</td>
					<td class="label">角色代码：</td>
					<td>${roleInfo.jsdm}</td>
				</tr>
				<tr>
					<td class="label">备&nbsp;&nbsp;注：</td>
					<td colspan="3">
						<textarea name="bz" class="easyui-textarea" readonly="readonly" style="width:500px;height:60px" >${roleInfo.bz}</textarea>
					</td>
				</tr>
			</table>
		</fieldset>
	</ui:Layout>
	<ui:Layout region="center" border="false" style="padding:2px;margin-top:2px;">
		<fieldset class="easyui-fieldset" style='height:430px' allowCollapsed="false" >
			<legend>角色用户列表</legend>
			<ui:Grid id="userList" datatype="json" shrinkToFit="true" autowidth="true" height="360" viewrecords="true" pageable="true"
					url="${ctx}/userCtl/findUserByRole.html" postData="{roleId:'${roleInfo.zjid}'}" multiselect="false"
					rowNum="20" rowList="[10,20,50]">
					<ui:Column name="ZJID" key="true" hidden="true"></ui:Column>
					<ui:Column header="警员编号" name="JYBH" width="100" fixed="true" formatter="viewUserFormatter"></ui:Column>
					<ui:Column header="所在单位" name="DWMC"  width="180" fixed="true"></ui:Column>
					<ui:Column header="用户姓名" name="YHMC" width="90" fixed="true"></ui:Column>
					<ui:Column header="用户账号" name="YHDLZH" width="70" fixed="true"></ui:Column>
					<ui:Column header="性别" width="45" name="XB" edittype="select" formatter="'select'" align="center" editoptions="{value:\"0:女;1:男\"}" fixed="true"></ui:Column>
					<ui:Column header="用户级别" name="YHJB" fixed="true" width="60" edittype="select" formatter="'select'" editoptions="{value:\"1:省级;2:市级;3:县级;4:派出所\"}"></ui:Column>
			</ui:Grid>
		</fieldset>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>