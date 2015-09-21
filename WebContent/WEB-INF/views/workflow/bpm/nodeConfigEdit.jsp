<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="ui" uri="/tags/ui"%>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<script type="text/javascript">
function doSave(){
	if($("#editForm").validate("validate")){
		var parms = $("#editForm").serialize();
		$.postc("${ctx}/workflow/bpmDef/nodeConfig/update.html", parms, function(data){
			MessageUtil.show("修改成功！");
			parentDialog.markUpdated();
			parentDialog.close();
		});
	}
}

function formTypeChange(){
	var formType = $("#formType").combobox("getValue");
	if(formType==""){
		$("#urlForm").hide();
		$("#onlineForm").hide();
		$("#bhArea").hide();
		$("#ahArea").hide();
		$("#formUrl").val("");
		$("#formDefName").val("");
		$("#formKey").val("");
		$("#beforeHandler").val("");
		$("#afterHandler").val("");
	}else if(formType=="1"){
		$("#urlForm").hide();
		$("#onlineForm").show();
		$("#bhArea").show();
		$("#ahArea").show();
		$("#formUrl").val("");
	}else if(formType=="2"){
		$("#onlineForm").hide();
		$("#urlForm").show();
		$("#bhArea").show();
		$("#ahArea").show();
		$("#formDefName").val("");
		$("#formKey").val("");
	}
}
</script>
<style type="text/css">
ul.help li {
	list-style: inside circle;
	list-style-type: decimal;
	padding-left: 10px;
	font-weight: normal;
}
</style>
</head>
<body>
<ui:LayoutContainer fit="true" >
	<ui:Layout region="north" style="height:58px" title="流程节点表单设置-${nodeConfig.nodeName}" iconCls="icon-list">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="save" href="#" iconCls="icon-save" onclick="doSave()">保存</a>
			<a id="close" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center">
		<form id="editForm" class="easyui-validate" style="margin-top:5px;">
			<input type="hidden" id="configId" name="configId" value="${nodeConfig.configId}">
			<input type="hidden" id="defId" name="defId" value="${nodeConfig.defId}">
			<input type="hidden" id="actDefId" name="actDefId" value="${nodeConfig.actDefId}">
			<input type="hidden" id="nodeId" name="nodeId" value="${nodeConfig.nodeId}">
			<table class="tableView">
				<tr>
					<td class="label" style="width:100px;">
						表单类型：
					</td>
					<td>
						<select id="formType" name="formType" class="easyui-combobox validate[required]" style="width:200px" dropdownHeight="75"
								onChange="formTypeChange" selValue="${nodeConfig.formType}">
							<option value="">--请选择--</option>
							<!-- <option value="1">在线表单</option> -->
							<option value="2">URL表单</option>
						</select>
					</td>
				</tr>
				<tr id="urlForm" <c:if test="${nodeConfig.formType != '2'}">style="display:none"</c:if>>
					<td class="label" style="width:100px;">
						表单:
					</td>
					<td>
						<input id="formUrl" name="formUrl" type="text" class="easyui-textinput validate[required]" style="width:200px" value="${nodeConfig.formUrl}">
					</td>
				</tr>
				<tr id="onlineForm" <c:if test="${nodeConfig.formType != '1'}">style="display:none"</c:if> >
					<td class="label" style="width:100px;">
						表单:
					</td>
					<td>
						<input id="formDefName" name="formDefName" type="text"  class="easyui-textinput validate[required]" value="${nodeConfig.formDefName}" style="width:200px" readonly="readonly">
						<input type="button" class="easyui-button" value="请选择">
						<input id="formKey" name="formKey"  type="hidden" value="${nodeConfig.formKey }" >
					</td>
				</tr>
				<tr id="bhArea" <c:if test="${nodeConfig.formType == null || nodeConfig.formType ==''}">style="display:none"</c:if>>
					<td class="label" style="width:100px;">前置处理器:</td>
					<td>
						<input id="beforeHandler"  class="easyui-textinput validate[maxSize[128]]" type="text" name="beforeHandler" value="${nodeConfig.beforeHandler}" style="width:200px"/>
						<span>填写格式:service+方法名(参数【ProcessCmd】)</span>
					</td>
				</tr>
				<tr id="ahArea" <c:if test="${nodeConfig.formType == null || nodeConfig.formType == ''}">style="display:none"</c:if>>
					<td class="label" style="width:100px;">后置处理器:</td>
					<td>
						<input id="afterHandler"  class="easyui-textinput validate[maxSize[128]]" type="text" name="afterHandler" value="${nodeConfig.afterHandler}" style="width:200px"/>
						<span>填写格式:service+方法名(参数【ProcessCmd】)</span>
					</td>
				</tr>
			</table>
		</form>
		<br>
		<table class="tableView" style="background-color:#EDF6FC">
			<tr>
				<td>
					<ul class="help" style="">
						<li>表单
							<ul>
								<li>在线表单,为系统自定义表单。</li>
								<li>url表单,是外部表单。地址写法规则为：如果表单页面平台在同一个应用中，路径从根开始写，<span
									class="red">不需要上下文路径</span>，例如 ：/form/addUser.html。
								</li>
							</ul>
						</li>
						<li>处理器
							<ul>
								<li>处理器是需要开发人员定制的,一般情况下<span class="red">URL表单的处理器需要填写</span>。处理器的写法是
									service类名首字母小写 +"." + 方法名称。
									需要注意的是这个service是需要通过spring容器管理的。另外方法的参数必须是ProcessCmd类。
									例如：userService.add,这个表示是UserService类中有一个，add(ProcessCmd
									cmd)的方法。
								</li>
								<li>前置和后置处理器区别在于执行时机不同，前置处理器是在启动流程或完成下一步这个动作之前执行的。
									后置处理器在启动流程或完成下一步之后执行的。</li>
							</ul>
						</li>
					</ul>
				</td>
			</tr>
		</table>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>