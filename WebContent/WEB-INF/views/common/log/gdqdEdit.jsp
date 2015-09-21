
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
<title></title>
<script type="text/javascript">
	$parsed(function(){
	});
	function doSave(){
		if($("#editForm").validate("validate")){
			var parms = $("#editForm").serialize();
			var type = "${type}";
			if(type=="add"){
				$.postc("${ctx}/gdqd/addGdqd.html", parms, function(data){
					MessageUtil.show("添加成功！");
					$("#reAddBtn").removeAttr("disabled");
					$("#saveBtn").attr("disabled",true);
					parentDialog.markUpdated();
				});
			}else if(type=="update"){
				$.postc("${ctx}/gdqd/updateGdqd.html", parms, function(data){
					MessageUtil.show("修改成功！");
					parentDialog.markUpdated();
				});
			}
		}
	}
	function reAdd(){
		$("#editForm")[0].reset();
		$("#saveBtn").removeAttr("disabled");
		$("#reAddBtn").attr("disabled",true);
	}
</script>
<body>
<ui:Panel fit="true">
	<form id="editForm" class="easyui-validate" style="margin-top:5px;">
		<input type="hidden" name="zjid" id="zjid" value="${gdqd.zjid}"/>
		<table class="formTable">
			<tr>
				<td class="label">
					归档配置ID：
				</td>
				<td>
				<input type="text" class="easyui-textinput validate[required]" id="gdpzZjid" name="gdpzZjid" value="${gdqd.gdpzZjid}" />
				<span class="star">*</span>
				</td>
				<td class="label">
					归档表名称：
				</td>
				<td>
				<input type="text" class="easyui-textinput validate[required]" id="gdbmc" name="gdbmc" value="${gdqd.gdbmc}" />
				<span class="star">*</span>
				</td>
			</tr>
			<tr>
				<td class="label">
					归档后的表名称：
				</td>
				<td>
				<input type="text" class="easyui-textinput validate[required]" id="gdhbmc" name="gdhbmc" value="${gdqd.gdhbmc}" />
				<span class="star">*</span>
				</td>
				<td class="label">
					归档数据开始时间：
				</td>
				<td>
				<input class="easyui-datePicker validate[required]" id="gdsjKssj" name="gdsjKssj" dateFmt="yyyy-MM-dd" value='<fmt:formatDate value='${gdqd.gdsjKssj}' pattern='yyyy-MM-dd'/>'>
				<span class="star">*</span>
				</td>
			</tr>
			<tr>
				<td class="label">
					归档数据结束时间：
				</td>
				<td>
				<input class="easyui-datePicker validate[required]" id="gdsjJssj" name="gdsjJssj" dateFmt="yyyy-MM-dd" value='<fmt:formatDate value='${gdqd.gdsjJssj}' pattern='yyyy-MM-dd'/>'>
				<span class="star">*</span>
				</td>
				<td class="label">
					归档记录数：
				</td>
				<td>
				<input type="text" class="easyui-textinput validate[required]" id="gdjls" name="gdjls" value="${gdqd.gdjls}" />
				<span class="star">*</span>
				</td>
			</tr>
			<tr>
				<td class="label">
					清单创建时间：
				</td>
				<td>
				<input class="easyui-datePicker validate[required]" id="jlcjsj" name="jlcjsj" dateFmt="yyyy-MM-dd" value='<fmt:formatDate value='${gdqd.jlcjsj}' pattern='yyyy-MM-dd'/>'>
				<span class="star">*</span>
				</td>
			</tr>
			<tr>
				<td colspan="4" align="center">
					<c:if test="${type=='add'}">
						<input id="reAddBtn" type="button" class="easyui-button" value="继续添加" onclick="reAdd();" disabled>
					</c:if>
					<input id="saveBtn" type="button" class="easyui-button" value="保存" onclick="doSave()">
					<input type="button" class="easyui-button" value="关闭" onclick="javascript:parentDialog.close()">
				</td>
			</tr>
		</table>
	</form>
</ui:Panel>
</body>
</html>