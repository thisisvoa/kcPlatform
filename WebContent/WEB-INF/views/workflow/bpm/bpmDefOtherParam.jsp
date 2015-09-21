<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="/tags/ui"%>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<script type="text/javascript">
	function onVarChange(){
		var val = $("#bpmVar").combobox("getValue");
		if(val!=""){
			var opt = $("#bpmVar").combobox("getOptionByValue",val);
			var text = opt.text;
			var inStr="{"+text+":"+val+"}";
			$("#instNameRule").htmleditor("insertHtml",inStr);
		}
	}
	function doSave(){
		var defId = "${bpmDefinition.defId}";
		var instNameRule = $("#instNameRule").htmleditor("getText");
		var toFirstNode = $("#toFirstNode").attr("checked")==undefined?'0':'1';
		var directStart = $("#directStart").attr("checked")==undefined?'0':'1';
		var sameExecutorJump = $("#sameExecutorJump").attr("checked")==undefined?'0':'1';
		var data = {defId:defId,instNameRule:instNameRule,toFirstNode:toFirstNode,directStart:directStart,sameExecutorJump:sameExecutorJump};
		$.postc("${ctx}/workflow/bpmDef/saveParam.html", data, function(data){
			MessageUtil.show("参数保存成功!");
		});	
	}
</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:33px" border="false">
		<div id="toolbar" class="easyui-toolbar" style="width:100%;">
			<a id="saveBtn" href="#" iconCls="icon-save" onclick="doSave()">保存</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center" border="false">
		<form id="editForm" class="easyui-validate" style="padding:1px;">
			<input type="hidden" id="defId" name="defId" value="${bpmDefinition.defId }">
			<table class="tableView">
				<tr>
					<td class="label" style="width:200px">
						流程标题规则定义：
					</td>
					<td>
						<table style="border:none">
							<tr>
								<td style="border:none">
									流程变量:
								</td>
								<td style="border:none">
									<select id="bpmVar" class="easyui-combobox" onChange="onVarChange">
										<option value="">--请选择--</option>
										<option value="title">流程标题</option>
										<option value="startUser">发起人</option>
										<option value="startDate">发起日期</option>
										<option value="startTime">发起时间</option>
										<option value="businessKey">业务ID</option>
									</select>
								</td>
							</tr>
						</table>
						<textarea id="instNameRule" name="instNameRule" class="easyui-htmleditor validate[required]"  style="height:60px;width:600px;">${bpmDefinition.instNameRule }</textarea>
					</td>
				</tr>
				<tr>
					<td class="label" style="width:200px">
						跳过第一个任务：
					</td>
					<td>
						<input id="toFirstNode" class="easyui-checkbox" type="checkbox" name="toFirstNode" value="1"  <c:if test="${bpmDefinition.toFirstNode==1 }">checked="checked"</c:if> />
						<span style="color:#ccc;margin-left:2px;">流程启动后直接完成第一个节点的任务。</span>
					</td>
				</tr>
				<tr>
					<td class="label" style="width:200px">
						直接启动流程：
					</td>
					<td>
						<input id="directStart" class="easyui-checkbox" type="checkbox" name="directStart" value="1"  <c:if test="${bpmDefinition.directStart==1 }">checked="checked"</c:if> />
						<span style="color:#ccc;margin-left:2px;">不使用表单直接启动流程，启动流程时不传入主键。</span>
					</td>
				</tr>
				<tr>
					<td class="label" style="width:200px">
						相邻任务节点人员相同时自动跳过：
					</td>
					<td>
						<input id="sameExecutorJump" class="easyui-checkbox" type="checkbox" name="sameExecutorJump" value="1"  <c:if test="${bpmDefinition.sameExecutorJump==1 }">checked="checked"</c:if> />
						<span style="color:#ccc;margin-left:2px;">如果相邻的任务节点执行人相同时,直接跳过。</span>
					</td>
				</tr>
			</table>
		</form>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>