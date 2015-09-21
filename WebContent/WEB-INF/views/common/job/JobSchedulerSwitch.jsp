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
	
	function switchJobScheduler(){
		var toHostUrl = $("#toHostUrl").combobox("getValue");
		if(toHostUrl==""){
			MessageUtil.alert("请选择切换的调度服务器!");
			return;
		}else{
			$.postc("${ctx}/job/switchJobScheduler.html",{toHostUrl:toHostUrl}, function(d){
				parentDialog.markUpdated();
				MessageUtil.show("操作成功!", "系统提示");
			});
		}
	}
	
</script>
</head>
<body>
<ui:Panel fit="true">
	<form id="editForm" class="easyui-validate">
		<input type="hidden" name="jobSchedulerId" id="jobSchedulerId" value="${jobScheduler.jobSchedulerId}"/>
		<table class="formTable">
			<tr>
				<td colspan="3" align="center">
					<input type="button" id="switchBtn" class="easyui-button" value="切换" onclick="switchJobScheduler()"/>
					<input type="button" class="easyui-button" value="关 闭 " onclick="javascript:parentDialog.close();"/>
				</td>
			</tr>
			<tr>
				<td class="label" style="width:100px">
					当前调度服务器：
				</td>
				<td>
					${curJobScheduler.hostUrl==null?"----":curJobScheduler.hostUrl}
				</td>
			</tr>
			<tr>
				<td class="label" style="width:100px">
					切换到：
				</td>
				<td>
					<select id="toHostUrl" class="easyui-combobox validate[required]" name="toHostUrl" editable="true" url="${ctx}/job/jobSchedulerComboList.html" textField="hostUrl" valueField="jobSchedulerId"  selValue="4,5" showEmptySelect="true">
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="padding-left:40px;padding-top:10px;color:#4040a0">
					提示：<br>
					1.可切换的调度服务器列表为处于活动状态的调度服务器<br>
					2.优先级高的调度服务器排在前面
				</td>
			</tr>
		</table>
	</form>
</ui:Panel>
</body>
</html>