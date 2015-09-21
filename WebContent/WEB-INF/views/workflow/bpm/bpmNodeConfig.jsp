<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="ui" uri="/tags/ui"%>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<script type="text/javascript" src="${ctx}/ui/js/util/json2.js"></script>
<title>流程表单设置</title>
<script type="text/javascript">
function doSave() {
	if($("#configForm").validate("validate")){
		var globalNode = {
				formType:$("#formType_global").combobox("getValue"),
				formUrl:$("#formUrl_global").val(),
				formDefName:$("#formDefName_global").val(),
				formKey:$("#formKey_global").val(),
				beforeHandler:$("#beforeHandler_global").val(),
				afterHandler:$("#afterHandler_global").val()
		};
		
		var nodeConfigList = [];
		$(".nodeConfig").each(function(i,v){
			var id= v.id;
			var nodeConfig = {
					configId:$("#configId_"+id).val(),
					formType:$("#formType_"+id).combobox("getValue"),
					formUrl:$("#formUrl_"+id).val(),
					formDefName:$("#formDefName_"+id).val(),
					formKey:$("#formKey_"+id).val(),
					beforeHandler:$("#beforeHandler_"+id).val(),
					afterHandler:$("#afterHandler_"+id).val()
			};
			nodeConfigList.push(nodeConfig);
		});
		var postData = {globalNode:globalNode, nodeConfigList:nodeConfigList};
		var dataStr = JSON.stringify(postData);
		$.postc("${ctx}/workflow/bpmDef/nodeConfig/save.html", {defId:"${defId}",nodeConfigs:dataStr}, function(data){
			MessageUtil.show("修改成功!");
		});	
	}
}

function validate(){
	
}

function formTypeChange(el){
	var id = el.id;
	var nodeId = id.split("_")[1];
	var formType = $(el).combobox("getValue");
	if(formType==""){
		$("#formUrlTr_"+nodeId).hide();
		$("#formDefNameTr_"+nodeId).hide();
		$("#beforeHandlerTr_"+nodeId).hide();
		$("#afterHandlerTr_"+nodeId).hide();
		$("#formUrl_"+nodeId).val("");
		$("#formDefName_"+nodeId).val("");
		$("#formKey_"+nodeId).val("");
		$("#beforeHandler_"+nodeId).val("");
		$("#afterHandler_"+nodeId).val("");
	}else if(formType=="1"){
		$("#formUrlTr_"+nodeId).hide();
		$("#formDefNameTr_"+nodeId).show();
		$("#beforeHandlerTr_"+nodeId).show();
		$("#afterHandlerTr_"+nodeId).show();
		$("#formUrl_"+nodeId).val("");
	}else if(formType=="2"){
		$("#formUrlTr_"+nodeId).show();
		$("#formDefNameTr_"+nodeId).hide();
		$("#beforeHandlerTr_"+nodeId).show();
		$("#afterHandlerTr_"+nodeId).show();
		$("#formDefName_"+nodeId).val("");
		$("#formKey_"+nodeId).val("");
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
	<ui:LayoutContainer fit="true">
		<ui:Layout region="north" style="height:33px">
			<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
				<a id="save" href="#" iconCls="icon-save" onclick="doSave()">保存</a>
			</div>
		</ui:Layout>
		<ui:Layout region="center" border="false" style="padding:5px">
			<form id="configForm"  class="easyui-validate">
			<table class="tableView" style="margin-top:5px">
				<tr>
					<td class="label" style="width: 100px">
						全局表单
						<input type="hidden" id="configId_global" name="configId" value="${gblNodeConfig.configId}">
					</td>
					<td style="width: 150px">
						<select id="formType_global" name="formType" class="easyui-combobox" dropdownHeight="75" onChange="formTypeChange" selValue="${gblNodeConfig.formType}">
							<option value="">--请选择--</option>
							<!-- <option value="1">在线表单</option> -->
							<option value="2">URL表单</option>
						</select>
					</td>
					<td>
						<table class="tableView">
							<tr id="formUrlTr_global" <c:if test="${gblNodeConfig==null || gblNodeConfig.formType=='1'}">style='display:none'</c:if>>
								<td class="label" style="width: 100px;">表单:</td>
								<td>
									<input id="formUrl_global" name="formUrl" type="text" class="easyui-textinput validate[required]" style="width: 200px" 
										value="${gblNodeConfig.formUrl}">
								</td>
							</tr >
							<tr id="formDefNameTr_global" <c:if test="${gblNodeConfig==null || gblNodeConfig.formType=='2'}">style='display:none'</c:if>>
								<td class="label" style="width: 100px;">表单:</td>
								<td>
									<input id="formDefName_global" name="formDefName" type="text" class="easyui-textinput validate[required]"
										style="width: 200px" readonly="readonly" value="${gblNodeConfig.formDefName}">
									<input type="button" class="easyui-button" value="请选择"> 
									<input id="formKey" name="formKey" type="hidden" value="${gblNodeConfig.formKey}">
								</td>
							</tr>
							<tr id="beforeHandlerTr_global" <c:if test="${gblNodeConfig==null}">style='display:none'</c:if>>
								<td class="label" style="width: 100px;">前置处理器:</td>
								<td>
									<input id="beforeHandler_global" class="easyui-textinput validate[maxSize[128]]" 
										type="text" name="beforeHandler" style="width: 200px" value="${gblNodeConfig.beforeHandler}"/>
								</td>
							</tr>
							<tr id="afterHandlerTr_global" <c:if test="${gblNodeConfig==null}">style='display:none'</c:if>>
								<td class="label" style="width: 100px;">后置处理器:</td>
								<td>
									<input id="afterHandler_global" class="easyui-textinput validate[maxSize[128]]" 
										type="text" name="afterHandler" style="width: 200px" value="${gblNodeConfig.afterHandler}"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<table class="tableView" style="margin-top:5px">
				<tr>
					<td style="width:150px;height:20px;font-weight:bold" class="ui-state-default">
						节点名称
					</td>
					<td style="width:150px;height:20px;font-weight:bold;" class="ui-state-default">
						表单类型
					</td>
					<td style="height:20px;font-weight:bold" class="ui-state-default">
						表单
					</td>
				</tr>
				<c:forEach var="nodeConfig" items="${nodeConfigList}">
					<tr class="nodeConfig" id="${nodeConfig.nodeId}">
						<td>
							${nodeConfig.nodeName}
							<input type="hidden" id="configId_${nodeConfig.nodeId}" name="configId" value="${nodeConfig.configId}">
						</td>
						<td>
							<select id="formType_${nodeConfig.nodeId}" name="formType" class="easyui-combobox" dropdownHeight="75" selValue="${nodeConfig.formType}" onChange="formTypeChange">
								<option value="">--请选择--</option>
								<!-- <option value="1">在线表单</option> -->
								<option value="2">URL表单</option>
							</select>
						</td>
						<td>
							<table class="tableView">
								<tr id="formUrlTr_${nodeConfig.nodeId}" <c:if test="${nodeConfig.formType!='2'}">style='display:none'</c:if>>
									<td class="label" style="width: 100px;">表单:</td>
									<td>
										<input id="formUrl_${nodeConfig.nodeId}" name="formUrl" type="text" class="easyui-textinput validate[required]" style="width: 200px" value="${nodeConfig.formUrl}">
									</td>
								</tr>
								<tr id="formDefNameTr_${nodeConfig.nodeId}" <c:if test="${nodeConfig.formType!='1'}">style='display:none'</c:if>>
									<td class="label" style="width: 100px;">表单:</td>
									<td>
										<input id="formDefName_${nodeConfig.nodeId}" name="formDefName" type="text" class="easyui-textinput validate[required]"
											style="width: 200px" readonly="readonly" value="${nodeConfig.formDefName}">
										<input type="button" class="easyui-button" value="请选择"> 
										<input id="formKey" name="formKey" type="hidden" value="${nodeConfig.formKey}">
									</td>
								</tr>
								<tr id="beforeHandlerTr_${nodeConfig.nodeId}" <c:if test="${nodeConfig.formType!='1' && nodeConfig.formType!='2'}">style='display:none'</c:if>>
									<td class="label" style="width: 100px;">前置处理器:</td>
									<td>
										<input id="beforeHandler_${nodeConfig.nodeId}" class="easyui-textinput validate[maxSize[128]]" 
											type="text" name="beforeHandler" style="width: 200px" value="${nodeConfig.beforeHandler}"/>
									</td>
								</tr>
								<tr id="afterHandlerTr_${nodeConfig.nodeId}" <c:if test="${nodeConfig.formType!='1' && nodeConfig.formType!='2'}">style='display:none'</c:if>>
									<td class="label" style="width: 100px;">后置处理器:</td>
									<td>
										<input id="afterHandler_${nodeConfig.nodeId}" class="easyui-textinput validate[maxSize[128]]" 
											type="text" name="afterHandler" style="width: 200px" value="${nodeConfig.afterHandler}"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</c:forEach>
			</table>
			<table class="tableView" style="margin-top:5px;">
				<tr>
					<td style="background-color: #EDF6FC">
						<span style="font-weight:bold; font-size:14px">说明：</span>
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
										cmd)的方法。</li>
									<li>前置和后置处理器区别在于执行时机不同，前置处理器是在启动流程或完成下一步这个动作之前执行的。
										后置处理器在启动流程或完成下一步之后执行的。</li>
								</ul></li>
						</ul></td>
				</tr>
			</table>
			</form>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>