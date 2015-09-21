
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
		var type = "${type}";
		if(type=="add"){
			$("#reAddBtn").linkbutton("disable");
		}
		
		$("#editForm").validate("addRule",
		{	name:"validateKey",
			rule:{
	           "url": "${ctx}/workflow/bpmCatalog/validateKey.html?id=${bpmCatalog.id}",
	           "alertText": "* key已存在"
			}
		});
	});
	function doSave(){
		if($("#editForm").validate("validate")){
			var parms = $("#editForm").serialize();
			var type = "${type}";
			if(type=="add"){
				$.postc("${ctx}/workflow/bpmCatalog/add.html", parms, function(data){
					MessageUtil.show("添加成功！");
					$("#saveBtn").linkbutton("disable");
					$("#reAddBtn").linkbutton("enable");
					parentDialog.markUpdated();
				});
			}else if(type=="update"){
				$.postc("${ctx}/workflow/bpmCatalog/update.html", parms, function(data){
					MessageUtil.show("修改成功！");
					parentDialog.markUpdated();
				});
			}
		}
	}
	function reAdd(){
		$("#editForm")[0].reset();
		$("#saveBtn").linkbutton("enable");
		$("#reAddBtn").linkbutton("disable");
	}
</script>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:32px" border="false">
		<div id="toolbar" class="easyui-toolbar" style="width:100%;">
			<a id="saveBtn" href="#" iconCls="icon-save" onclick="doSave()">保存</a>
			<c:if test="${type=='add'}">
				<a id="reAddBtn" href="#" iconCls="icon-redo" onclick="reAdd();">再次添加</a>
			</c:if>
			<a id="close" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center" border="false" style="padding:1px">
		<form id="editForm" class="easyui-validate" style="margin-top:5px;">
			<input type="hidden" name="id" id="id" value="${bpmCatalog.id}"/>
			<input type="hidden" name="catalogType" id="catalogType" value="${bpmCatalog.catalogType}"/>
			<table class="tableView">
				<tr>
					<td class="label" style="width:100px">
						父节点：
					</td>
					<td>
						<input type="hidden" id="parentId" name="parentId" value="${parent.id}" />
						${parent.catalogName}
					</td>
				</tr>
				<tr>
					<td class="label" style="width:100px">
						节点名称：
					</td>
					<td>
					<input type="text" class="easyui-textinput validate[required,maxSize[32]]" id="catalogName" name="catalogName" value="${bpmCatalog.catalogName}" />
					</td>
				</tr>
				<tr>
					<td class="label" style="width:100px">
						节点key：
					</td>
					<td>
					<input type="text" class="easyui-textinput validate[required,maxSize[32],ajax[validateKey]]" id="catalogKey" name="catalogKey" value="${bpmCatalog.catalogKey}" />
					</td>
				</tr>
				<tr>
					<td class="label" style="width:100px">
						排序号：
					</td>
					<td>
					<input type="text" class="easyui-textinput  validate[required,custom[integer]]" id="orderNo" name="orderNo" value="${bpmCatalog.orderNo}" />
					</td>
				</tr>
			</table>
		</form>
	</ui:Layout>
</ui:LayoutContainer>
	
</body>
</html>