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
<script language="javascript">
//校验单位编码的唯一性 
	$parsed(function() {
		$("#editForm").validate("addRule",
			{	name:"checkCsdm",
				rule:{
		           "url": "${ctx}/para/checkCsdm.html?zjId=${para.zjId}",
		           "alertText": "* 此编号已存在"
				}
			});
	});
	
	function save() {
		if($("#editForm").validate("validate")){
			var data = $("#editForm").serialize();
			if ("${type}" == "ADD") {
				$.postc("${ctx}/para/addPara.html", data, function(data){
					MessageUtil.show("操作成功!");
					$("#saveBtn").attr("disabled",true);
					$("#resetBtn").removeAttr("disabled");
					parentDialog.markUpdated();
				});
			} else if ("${type}" == "UPDATE"){
				MessageUtil.confirm("确定修改该参数信息吗？",function(){
					$.postc("${ctx}/para/updatePara.html", data, function(data){
						MessageUtil.show("操作成功!");
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
	<input type="hidden" name="zjId" value="${para.zjId}" />
	<table class="formTable">
		<tr>
			<td colspan="2" align="center">
				<c:if test="${type == 'ADD'}">
						<input id="resetBtn" type="button" class="easyui-button" value="再次添加 " onclick="formReset();" disabled/>
					</c:if>
					<input id="saveBtn" type="button" class="easyui-button" value="保存 " onclick="save()" /> 
					<input type="button" class="easyui-button" value="关 闭 " onclick="javascript:parentDialog.close();"/>
			</td>
		</tr>
		<tr>
			<td class="label">参数名称：</td>
			<td><input type="text" name="csmc" value="${para.csmc}"
				class="easyui-textinput validate[required,maxSize[50]]" style="width: 360px;"/> 
				<span class="star">*</span>
			</td>
		</tr>
		<tr>
			<td class="label">参数代码：</td>
			<td><input type="text" name="csdm" value="${para.csdm}"
				class="easyui-textinput validate[required,maxSize[15],ajax[checkCsdm]]" id="csdm" style="width: 360px;"/>
				<span class="star">*</span>
			</td>
		</tr>
		<tr>
			<td class="label">参 数 值：</td>
			<td><input type="text" name="csz" value="${para.csz}"
				class="easyui-textinput validate[required,maxSize[50]]" style="width: 360px;"/> <span class="star">*</span>
			</td>
		</tr>
		<tr>
			<td class="label">使用标识：</td>
			<td><input type="radio" name="sybz" value="1" class="easyui-radiobox"
				${(para.sybz== '1')?'checked':''}/>启用
				<input type="radio" name="sybz" value="0" class="easyui-radiobox"
				${(para.sybz== '0')?'checked':''}/>禁用
			</td>
		</tr>
		<tr>
			<td class="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td><textarea name="bz" class="easyui-textarea validate[maxSize[500]]" style="width:360px;height:150px">${para.bz}</textarea>
			</td>
		</tr>
	</table>
</form>
</body>
</html>
