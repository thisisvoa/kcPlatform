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
	//校验功能编码的唯一性 
	$parsed(function() {
		$("#form").validate("addRule",
			{	name:"checkGndm",
				rule:{
		           "url": "${ctx}/func/checkGndm.html?zjId=${func.zjId}",
		           "alertText": "* 此编号已存在"
				}
			});
	});

	
	function save() {
		if($("#form").validate("validate")){
			var data = 	$("#form").serialize();
			if("${type}"=="edit"){
				MessageUtil.confirm("确定对该功能信息进行修改吗？",function(){
					$.postc("${ctx}/func/funcSave.html",data, function(data){
						parentDialog.markUpdated();	
						MessageUtil.show("操作成功!");
					});
				});
			}else{
				$.postc("${ctx}/func/funcSave.html",data, function(data){
					parentDialog.markUpdated();	
					$("#saveBtn").attr("disabled",true);
					$("#resetBtn").removeAttr("disabled");
					MessageUtil.show("操作成功!");
				});	
			}
			
		}
	}
	
	function formReset(){
		$("#form")[0].reset();
		$("#resetBtn").attr("disabled",true);
		$("#saveBtn").removeAttr("disabled");
	}
</script>
</head>
<body>
	<form id="form" class="easyui-validate" action="${ctx}/func/funcSave.html" method="post">
		<input type="hidden" name="zjId" value="${func.zjId}" />
		<input type="hidden" name="type" value="${type}" />
		<table class="formTable">
			<tr>
				<td colspan="2" align="center">
					<c:if test="${type == 'add'}">
						<input id="resetBtn" type="button" class="easyui-button" value="再次添加 " onclick="formReset();" disabled/>
					</c:if>
					<input id="saveBtn" type="button" class="easyui-button" value="保存 " onclick="save()" /> 
					<input type="button" class="easyui-button" value="关 闭" onclick="javascript:parentDialog.close()"/>
				</td>
			</tr>
			<tr>
				<td class="label" style="width">功能名称：</td>
				<td><input type="text" name="gnmc" value="${func.gnmc}"
					class="easyui-textinput validate[required,maxSize[50]]" style="width: 380px;"/> <span
						class="star">*</span>
				</td>
			</tr>
			<tr>
				<td class="label">功能代码：</td>
				<td><input type="text" name="gndm" value="${func.gndm}"
					class="easyui-textinput validate[required,maxSize[50],ajax[checkGndm]]" id="gndm" style="width: 380px;"/> <span
						class="star" id="checkGndm">*</span>
				</td>
			</tr>
			<tr>
				<td class="label">所属菜单：</td>
				<td>
					<select name="cdxxZjId" id="sscd" class="easyui-combotree validate[required]" url="${ctx}/menu/loadAllEnableMenu.html"
						idKey="zjId" nameKey="cdmc" pIdKey="sjcd" panelWidth="350" simpleDataEnable="true"
						selValue="${func.cdxxZjId}">
					</select>  
					<span class="star">*</span>
				</td>
			</tr>
			<tr>
				<td class="label">功能序号：</td>
				<td><input type="text" name="gnxh" value="${func.gnxh}"
					class="easyui-textinput validate[required,maxSize[10],custom[integer]]"
					onkeyup="this.value=this.value.replace(/\D/g,'')"
					/> <span
					class="star">*</span>
				</td>
			</tr>
			<tr>
				<td class="label">使用标识：</td>
				<td><input type="radio" name="sybz" value="1" class="easyui-radiobox"
					${(func.sybz== '1')?'checked':''}/>启用
					<input type="radio" name="sybz" value="0" class="easyui-radiobox"
					${(func.sybz== '0')?'checked':''}/>禁用
				</td>
			</tr>
			<tr>
				<td class="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
				<td><textarea name="bz" class="easyui-textarea validate[maxSize[500]]" style="width:380px;height:190px">${func.bz}</textarea>
				 <span class="star">*</span></td>
			</tr>
		</table>
	</form>
</body>
</html>
