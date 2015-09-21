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
	//校验单位编码的唯一性 
	$parsed(function() {
		$("#editForm").validate("addRule",
			{	name:"checkRoleCode",
				rule:{
		           "url": "${ctx}/roleCtl/checkRoleCode.html?roleId=${roleInfo.zjid}",
		           "alertText": "* 此编号已存在"
				}
			});
	});

	function save() {
		if($("#editForm").validate("validate")){
			var data = $("#editForm").serialize();
			if ("${operModel}" == "ADD") {
				$.postc("${ctx}/roleCtl/addRoleInfo.html", data, function(data){
					MessageUtil.show("添加成功!");
					$("#saveBtn").attr("disabled",true);
					$("#resetBtn").removeAttr("disabled");
					parentDialog.markUpdated();
				});
			} else if ("${operModel}" == "UPDATE"){
				MessageUtil.confirm("确定保存角色信息？",function(){
					$.postc("${ctx}/roleCtl/updateRoleInfo.html", data, function(data){
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
		<input type="hidden" id="operModel" name="operModel" value="${operModel}" /> 
		<input type="hidden" id="roleId" name="roleId" value="${roleInfo.zjid}" />
		<table class="formTable">
			<tr>
				<td colspan="2" align="center">
					<c:if test="${operModel == 'ADD'}">
						<input id="resetBtn" type="button" class="easyui-button" value="再次添加 " onclick="formReset();" disabled/>
					</c:if>
					<input id="saveBtn" type="button" class="easyui-button" value="保存 " onclick="save()" /> 
					<input type="button" class="easyui-button" value="关 闭 " onclick="javascript:parentDialog.close();"/>
				</td>
			</tr>
			<tr>
				<td class="label">角色名称：</td>
				<td>
					<input id="roleName" type="text" name="roleName" style="width:310px;" value="${roleInfo.jsmc}"
						class="easyui-textinput validate[required, maxSize[50]]" />
					<span id="checkUserName" class="star">*</span>
				</td>
			</tr>
			<tr>
				<td  class="label">角色代码：</td>
				<td>
					<input id="roleCode" type="text" name="roleCode" style="width:310px;" value="${roleInfo.jsdm}"
						class="easyui-textinput validate[required,maxSize[15],ajax[checkRoleCode]]" 
						<c:if test="${operModel == 'UPDATE'}"> readonly="readonly"</c:if> />
					<span id="checkRoleCode" class="star">*</span>
				</td>
			</tr>
			<tr>
				<td  class="label">角色级别：</td>
				<td>
					<select id="jsjb" name="jsjb" class="easyui-combobox validate[required]" dropdownHeight="125" selValue="${roleInfo.jsjb}">
							<option value="">---请选择---</option>
							<c:if test="${YHJB eq '1'}">
								<option value="1">省级</option>
							</c:if>
							<c:if test="${YHJB <= '2'}">
						    	<option value="2">市级</option>
						    </c:if>
						    <c:if test="${YHJB <= '3'}">
						    <option value="3">县级</option>
						    </c:if>
						    <c:if test="${YHJB <= '4'}">
						    	<option value="4">派出所</option>
						    </c:if>
					</select>
					<span class="star">*</span>
				</td>
			</tr>
			<tr>
				<td  class="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
				<td><textarea id="remark" class="easyui-textarea validate[maxSize[500]]" name="remark" style="width:310px;height:110px">${roleInfo.bz}</textarea>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
