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
function refresh(){
	if($("#tab1").find("iframe")[0]!=null){
		$("#tab1").find("iframe")[0].contentWindow.refreshGrid();
	};
	if($("#tab2").find("iframe")[0]!=null){
		$("#tab2").find("iframe")[0].contentWindow.refreshGrid();
	};
	if($("#tab3").find("iframe")[0]!=null){
		$("#tab3").find("iframe")[0].contentWindow.refreshGrid();
	};
	
}
</script>
</head>
<body>
	<ui:LayoutContainer fit="true">
		<ui:Layout region="north" style="height:80px" border="false">
			<table class="formTable">
				<tr>
					<td class="label" style="width:100px">
						用户组名称：
					</td>
					<td>
						<input type="text" id="userGroupName" name="userGroupName" class="easyui-textinput" value="${userGroupInfo.yhzmc}" readonly="readonly"/>
					</td>
					<td class="label" style="width:100px">
						类别：
					</td>
					<td>
						<select id="userGroupType" name="userGroupType" class="easyui-combobox" style="width: 200px;" disabled="disabled" selValue="${userGroupInfo.yhzlx}">
								<option value="1">普通</option>
								<option value="0">高级</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="label" style="width:100px">
						级别：
					</td>
					<td>
						<select id="userGroupLevel" name="userGroupLevel" class="easyui-combobox" style="width: 200px;" disabled="disabled" selValue="${userGroupInfo.yhzjb}">
							<option value="1">省级</option>
							<option value="0">市级</option>
						</select>
					</td>
					<td class="label" style="width:100px">
						状态：
					</td>
					<td>
						<input type="hidden" id="userGroupId" name="userGroupId" value="${userGroupInfo.zjid} }" />&nbsp;&nbsp;
							<c:if test="${userGroupInfo.sybz == '1'}"><font color='green'>启用</font> </c:if>
							<c:if test="${userGroupInfo.sybz == '0'}"> <font color='red'>禁用</font> </c:if>
					</td>
				</tr>
			</table>
		</ui:Layout>
		<ui:Layout region="center">
			<div id="tt" class="easyui-tabs" fit="true" border="false">
				<div id="tab1" style="overflow:hidden" title="全部用户" iconCls="icon-tab" url="${ctx}/userGroupCtl/toUserGroupAllot.html?userGroupId=${userGroupInfo.zjid}">
				</div>
				<div id="tab2" style="overflow:hidden" title="已分配用户" iconCls="icon-tab" url="${ctx}/userGroupCtl/toUserGroupAllot.html?userGroupId=${userGroupInfo.zjid}&isAllot=true">
				</div>
				<div id="tab3" style="overflow:hidden" title="未分配用户" iconCls="icon-tab" url="${ctx}/userGroupCtl/toUserGroupAllot.html?userGroupId=${userGroupInfo.zjid}&isAllot=false">
				</div>
			</div>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>