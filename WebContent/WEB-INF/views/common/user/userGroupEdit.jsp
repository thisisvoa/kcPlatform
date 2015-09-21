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
	function save() {
		if($("#editForm").validate("validate")){
			if ("${operModel}" == "ADD") {
				var data = $("#editForm").serialize();
				$.postc("${ctx}/userGroupCtl/addUserGroup.html", data, function(data){
					MessageUtil.show("添加成功!");
					parentDialog.markUpdated();
					$("#saveBtn").attr("disabled",true);
					$("#resetBtn").removeAttr("disabled");
				});
			}else if ("${operModel}" == "UPDATE") {
				var data = $("#editForm").serialize();
				MessageUtil.confirm("确定保存用户组信息？",function(){
					$.postc("${ctx}/userGroupCtl/updateUserGroup.html", data, function(data){
						MessageUtil.show("修改成功!");
						parentDialog.markUpdated();
					});
				});
			}	
		}
	}
	function formReset(){
		$("#editForm")[0].reset();
		$("#resetBtn").attr("disabled",true);
		$("#saveBtn").removeAttr("disabled");
	}
</script>
</head>
 <body>
	<form id="editForm" class="easyui-validate">
		<input type="hidden" id="userGroupId" name="userGroupId" value="${userGroupInfo.zjid}" />
		<table class="formTable">
			<tr>
				<td class="label" style="width:25%">用户组名称：</td>
				<td>
					<input id="userGroupName" type="text" name="userGroupName"
						value="${userGroupInfo.yhzmc}" class="easyui-textinput validate[required]" /> 
					<span id="checkUserName" class="star">*</span></td>
			</tr>
			<tr>
				<td class="label">用户组类别：</td>
				<td>
					<select id="userGroupType" name="userGroupType" class="easyui-combobox validate[required]" selValue="${userGroupInfo.yhzlx}" dropdownHeight="60">
						<option value="1">普通</option>
						<option value="0">高级</option>
					</select>
					<span class="star">*</span>
				</td>
			</tr>
			<tr>
				<td class="label">用户组级别：</td>
				<td>
					<select id="userGroupLevel" name="userGroupLevel" class="easyui-combobox validate[required]" selValue="${userGroupInfo.yhzjb}" dropdownHeight="60">
						<option value="1">省级</option>
						<option value="0">市级</option>
				</select><span class="star">*</span></td>
			</tr>
			<tr>
				<td class="label">使用标识：</td>
				<td>
					<input type="radio" name="useTarg" value="1" class="easyui-radiobox"
						<c:if test="${userGroupInfo.sybz == '1'}"> checked="checked" </c:if>
						<c:if test="${operModel == 'ADD'}"> checked="checked" </c:if> />有效
					<input type="radio" name="useTarg" value="0" class="easyui-radiobox"
						<c:if test="${userGroupInfo.sybz == '0'}"> checked="checked" </c:if> />禁用
				</td>
			</tr>
			<tr>
				<td class="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
				<td>
					<textarea id="remark" class="easyui-textarea" name="remark" style="width:360px;height:60px">${userGroupInfo.bz}</textarea>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<c:if test="${operModel == 'ADD'}">
						<input type="button" id="resetBtn" class="easyui-button" disabled value="再次添加  " onclick="formReset();"/>
					</c:if>
					<input type="button" id="saveBtn" class="easyui-button" value="保存 " onclick="save()"/>
					<input type="button" value="关 闭 " class="easyui-button" onclick="javascript:parentDialog.close();"/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
