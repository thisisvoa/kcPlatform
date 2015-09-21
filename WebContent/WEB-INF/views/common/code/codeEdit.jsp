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

	function formReset(){
		var operModel = $("#operModel").val();
		var isCodeOption = $("#isCodeOption").val();
		if (operModel == "ADD") {
			location.href = "${ctx}/codeCtl/toAddCode.html?isCodeOption="+isCodeOption;
		} else{
			var id = $("#codeId").val();
			location.href = "${ctx}/codeCtl/toEditCode.html?isCodeOption="+isCodeOption+"&id="+id;
		} 
	}
	
	function save(){
		if($("#editForm").validate("validate")){
			var data = $("#editForm").serialize();
			if("${operModel}"=="ADD"){
				$.postc("${ctx}/codeCtl/submitCodeInfo.html",data, function(){
					if("${isDialog}"=="1"){
						parentDialog.markUpdated();
					}
					$("#saveBtn").attr("disabled",true);
					$("#resetBtn").removeAttr("disabled");
					MessageUtil.show("操作成功!", "系统提示", 30000);
				});
			}else{
				MessageUtil.confirm("确定保存该编码信息吗？",function(){
					$.postc("${ctx}/codeCtl/submitCodeInfo.html",data, function(){
						if("${isDialog}"=="1"){
							parentDialog.markUpdated();
						}
						MessageUtil.show("操作成功!", "系统提示", 30000);
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
	//校验一级代码简称的唯一性
	$parsed(function() {
		$("#editForm").validate("addRule",
			{	name:"checkDmjcUnique",
				rule:{
		           "url": "${ctx}/codeCtl/checkDmjcUnique.html?zjid=${codeInfo.zjid}",
		           "alertText": "* 该代码简称已存在！"
				}
			});
		//校验二级代码项编号的唯一性
		var codeSimpleName = $("#codeSimpleName").val();
		$("#editForm").validate("addRule",
			{	name:"checkDmxBhUnique",
				rule:{
		           "url": "${ctx}/codeCtl/checkDmxBhUnique.html?zjid=${codeInfo.zjid}&codeSimpleName=" + codeSimpleName,
		           "alertText": "* 该代码项编号已存在！"
				}
			});
	});
</script>
</head>
<body >
<c:if test="${codeInfo.sfdmx == '0'}">
	<form id="editForm" class="easyui-validate" method="post">
		<input type="hidden" id="operModel" name="operModel" value="${operModel}" />
		<input type="hidden" id="codeId" name="codeId" value="${codeInfo.zjid}" />
		<input id="isCodeOption" type="hidden" name="isCodeOption" value="${codeInfo.sfdmx}"/>
		<table class="formTable">
			<tr>
				<td colspan="42" align="center">
					<input type="button" class="easyui-button" value="保存 " onclick="save()"/>
					<c:if test="${isDialog=='1'}">
						<input type="button" class="easyui-button" value="关 闭 " onclick="javascript:parentDialog.close();"/>
					</c:if>
				</td>
			</tr>
			<tr>
				<td class="label">代码名称：</td>
				<td>
					<input id="codeName" type="text" name="codeName" style="width:380px" value="${codeInfo.dmmc}"
						class="easyui-textinput validate[required,maxSize[50]]" /> 
					<span id="checkCodeName" class="star">*</span>
				</td>
			</tr>
			<tr>
				<td class="label">代码简称：</td>
				<td>
					<input id="codeSimpleName" type="text" name="codeSimpleName" style="width:380px" value="${codeInfo.dmjc}"
							class="easyui-textinput validate[required,maxSize[15],ajax[checkDmjcUnique]]"/> 
					<span id="checkSimpleName" class="star">*</span>
				</td>
			</tr>
			<tr>
				<td class="label">使用标识：</td>
				<td>
					<input type="radio" name="useTarg" value="1" class="easyui-radiobox"
					<c:if test="${codeInfo.sybz == '1'}"> checked </c:if>
					<c:if test="${operModel == 'ADD'}"> checked </c:if> />有效
					<input type="radio" name="useTarg" value="0" class="easyui-radiobox"
					<c:if test="${codeInfo.sybz == '0'}"> checked </c:if> />禁用
				</td>
			</tr>
			<tr>
				<td class="label">排&nbsp;序&nbsp;号：</td>
				<td>
					<input id="orderNo" type="text" name="orderNo"
						style="width: 200px;" value="${codeInfo.pxh}"
						class="easyui-textinput validate[required,maxSize[10],custom[integer]]"  
						onkeyup="this.value=this.value.replace(/\D/g,'')"/> 
					<span class="star">*</span>
				</td>
			</tr>
			<tr>
				<td class="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
				<td>
					<textarea class="easyui-textarea  validate[maxSize[500]" id="remark" name="remark"  style="width:380px;height: 140px;">${codeInfo.bz}</textarea>
				</td>
			</tr>
		</table>
	</form>
</c:if>
<c:if  test="${codeInfo.sfdmx == '1'}">
	<form id="editForm" class="easyui-validate" action="${ctx}/codeCtl/submitCodeInfo.html" method="post">
		<input type="hidden" id="operModel" name="operModel" value="${operModel}" />
		<input type="hidden" id="codeId" name="codeId" value="${codeInfo.zjid}" />
		<input type="hidden" id="msg" name="msg" value="${msg}" />
		<input id="isCodeOption" type="hidden" name="isCodeOption" value="${codeInfo.sfdmx}"/>
		<input id="codeSimpleName" type="hidden" name="codeSimpleName" value="${codeInfo.dmjc}"/>
		<table class="formTable">
			<tr>
				<td colspan="2" align="center">
					<c:if test="${operModel == 'ADD'}">
						<input id="resetBtn" type="button" class="easyui-button" value="再次添加 " onclick="formReset();" disabled/>
					</c:if>
					<input id="saveBtn" type="button" class="easyui-button" value="保存 " onclick="save()" /> 
					<c:if test="${isDialog=='1'}">
						<input type="button" class="easyui-button" value="关 闭 " onclick="javascript:parentDialog.close();"/>
					</c:if>
				</td>
			</tr>
			<tr>
				<td class="label">代码名称：</td>
				<td>
				<input id="codeName" type="text" name="codeName" readonly="readonly"
					style="width:380px" value="${codeInfo.dmmc}" 
					class="easyui-textinput validate[required,maxSize[50]]" /> 
					<span id="checkCodeName" class="star">*</span>
				</td>			
			</tr>
			<tr>
				<td class="label">代码项编号：</td>
				<td>
				<input id="codeOptionNum" type="text" name="codeOptionNum"
					style="width:380px" value="${codeInfo.dmx_bh}"
					class="easyui-textinput validate[required,maxSize[15],ajax[checkDmxBhUnique]]" /> 
					<span id="checkCodeOptionNum" class="star">*</span>
				</td>			
			</tr>
			<tr>
				<td class="label">代码项名称：</td>
				<td>
				<input id="codeOptionName" type="text" name="codeOptionName"
					style="width:380px" value="${codeInfo.dmx_mc}"
					class="easyui-textinput validate[required,length[0,50]]" /> 
					<span id="checkCodeOptionName" class="star">*</span>
				</td>			
			</tr>
			<tr>
				<td class="label">使用标识：</td>
				<td>
					<input type="radio" name="useTarg" value="1" class="easyui-radiobox"
					<c:if test="${codeInfo.sybz == '1'}"> checked </c:if>
					<c:if test="${operModel == 'ADD'}"> checked</c:if> />有效
					<input type="radio" name="useTarg" value="0" class="easyui-radiobox"
					<c:if test="${codeInfo.sybz == '0'}"> checked</c:if> />禁用
				</td>
			</tr>
			<tr>
				<td class="label">排序号：</td>
				<td>
				<input id="orderNo" type="text" name="orderNo"
					style="width: 200px;" value="${codeInfo.pxh}" onkeyup="this.value=this.value.replace(/\D/g,'')"
					class="easyui-textinput validate[required,maxSize[10],custom[integer]]" /> 
					<span id="checkOrderNo" class="star">*</span>
				</td>			
			</tr>
			<tr>
				<td class="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
				<td>
					<textarea id="remark" class="easyui-textarea validate[maxSize[500]" name="remark"  style="width:380px;height: 140px;">${codeInfo.bz}</textarea>
				</td>
			</tr>
		</table>
	</form>
</c:if>
</body>
</html>
