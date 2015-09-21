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
	$parsed(function(){
		$("#editForm").validate("addRule",
			{	name:"validateHostUrl",
				rule:{
		           "url": "${ctx}/job/validateHostUrl.html?jobSchedulerId=${jobScheduler.jobSchedulerId}",
		           "alertText": "* 任务调度服务器已存在"
				}
			});
		$("#editForm").validate("addRule",
			{	name:"validatePriority",
				rule:{
		           "url": "${ctx}/job/validatePriority.html?jobSchedulerId=${jobScheduler.jobSchedulerId}",
		           "alertText": "* 该优先级已存在"
				}
			});
	});
	function save(){
		if($("#editForm").validate("validate")){
			var data = $("#editForm").serialize();
			if("${type}"=="add"){
				$.postc("${ctx}/job/addJobScheduler.html",data, function(d){
					$("#saveBtn").attr("disabled",true);
					$("#resetBtn").removeAttr("disabled");
					parentDialog.markUpdated();
					MessageUtil.show("操作成功!", "系统提示");
					$("#saveBtn").attr("disabled",true);
					$("#resetBtn").removeAttr("disabled");
				});
			}else{
				MessageUtil.confirm("确定保存该任务调度服务器信息吗？",function(){
					$.postc("${ctx}/job/updateJobScheduler.html",data, function(){
						parentDialog.markUpdated();
						MessageUtil.show("操作成功!", "系统提示");
					});	
				});
			}
		}
	};
	
	function formReset(){
		$("#editForm")[0].reset();
		$("#resetBtn").attr("disabled",true);
		$("#saveBtn").removeAttr("disabled");
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
					<c:if test="${type == 'add'}">
						<input type="button" id="resetBtn" class="easyui-button" disabled value="再次添加  " onclick="formReset();"/>
					</c:if>
					<input type="button" id="saveBtn" class="easyui-button" value="保存 " onclick="save()"/>
					<input type="button" value="关 闭 " class="easyui-button" onclick="javascript:parentDialog.close();"/></td>
			</tr>
			<tr>
				<td class="label" style="width:100px">
					调度服务器地址：
				</td>
				<td  style="width:210px">
					<input type="text" class="easyui-textinput validate[required,custom[url],ajax[validateHostUrl],maxSize[50]]" style="width:200px" id="hostUrl" name="hostUrl" value="${jobScheduler.hostUrl}" />
					<span class="star">*</span>
				</td>
				<td style="color:#cccccc">
					格式为http://ip:port/context
				</td>
			</tr>
			<tr>
				<td class="label" style="width:80px">
					优先级：
				</td>
				<td>
					<input type="text" class="easyui-numberspinner validate[required,custom[integer],ajax[validatePriority]]" min="1" max="10" id="priority" name="priority" value="${jobScheduler.priority}" />
					<span class="star">*</span>
				</td>
			</tr>
			
			<tr>
				<td class="label">
					备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：
				</td>
				<td colspan="2">
					<textarea id="remark" name="remark" style="height:100px;width:200px" class="easyui-textarea validate[maxSize[100]]">${jobScheduler.remark}</textarea>
				</td>
			</tr>
		</table>
	</form>
</ui:Panel>
</body>
</html>