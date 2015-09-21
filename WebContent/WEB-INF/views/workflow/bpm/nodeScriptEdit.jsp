
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
<script language="Javascript" type="text/javascript" src="${ctx}/ui/js/edit_area/edit_area_full.js"></script>
<title></title>
<script type="text/javascript">
	$parsed(function(){
		var t = "${type}";
		if(t!='multiUserTask'&&t!='userTask'){
			editAreaLoader.init({
				id: "script",
				start_highlight: true,
				allow_toggle: false,
				language: "zh",
				syntax: "java",
				toolbar: "search, go_to_line, |, undo, redo, |, select_font, |, syntax_selection, |, change_smooth_selection, highlight, reset_highlight, |, help",
				syntax_selection_allow: "java",
				is_multi_files: false,
				show_line_colors: true
			});
		}
	});
	function doSave(){
		if($("#scriptForm").validate("validate")){
			var data = $("#scriptForm").serialize();
			var t = "${type}";
			if(t=='multiUserTask'|| t=='userTask'){
				var preScript = editAreaLoader.getValue("scriptPre");
				if(preScript.length>4000){
					MessageUtil.alert("前置脚本超过4000个字符，不合法！");
					return;
				}
				data+="&script=";
				data+=preScript;
				if(afterInit){
					var afterScript = editAreaLoader.getValue("scriptAfter");
					if(afterScript.length>4000){
						MessageUtil.alert("后置脚本超过4000个字符，不合法！");
						return;
					}
					data+="&script=";
					data+=afterScript;
				}else{
					var afterScript = $("#scriptAfter").val();
					data+="&script=";
					data+=afterScript;
				}
			}else{
				var script = editAreaLoader.getValue("script");
				if(script.length>4000){
					MessageUtil.alert("脚本超过4000个字符，不合法！");
					return;
				}
				data+="&script=";
				data+=script;
			}
			$.postc("${ctx}/workflow/bpmDef/nodeScript/save.html", data, function(data){
				MessageUtil.show("添加成功!");
				parentDialog.markUpdated();
			});
		}
	}
	
	var preInit = false;
	var afterInit = false;
	function onSelect(id) {
		if(id=="preTab"){
			if(preInit) return;
			editAreaLoader.init({
				id: "scriptPre",
				start_highlight: true,
				allow_toggle: false,
				language: "zh",
				syntax: "java",
				toolbar: "search, go_to_line, |, undo, redo, |, select_font, |, syntax_selection, |, change_smooth_selection, highlight, reset_highlight, |, help",
				syntax_selection_allow: "java",
				is_multi_files: false,
				show_line_colors: true
			});
			preInit = true;
		}
		if(id=="afterTab"){
			if(afterInit) return;
			editAreaLoader.init({
				id: "scriptAfter",
				start_highlight: true,
				allow_toggle: false,
				language: "zh",
				syntax: "java",
				toolbar: "search, go_to_line, |, undo, redo, |, select_font, |, syntax_selection, |, change_smooth_selection, highlight, reset_highlight, |, help",
				syntax_selection_allow: "java",
				is_multi_files: false,
				show_line_colors: true
			});
			afterInit = true;
		}
		
	}
</script>
<body>
<c:set var="preModel" value="${map.type1}"></c:set>
<c:set var="afterModel" value="${map.type2}"></c:set>
<c:set var="scrptModel" value="${map.type3}"></c:set>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:33px" border="false">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="saveBtn" href="#" iconCls="icon-save" onclick="doSave()">保存</a>
			<a id="close" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center">
		<form id="scriptForm" class="easyui-validate" style="width:100%;height:100%">
			<input type="hidden" name="defId" id="defId" value="${defId}"/>
			<input type="hidden" name="actDefId" id="actDefId" value="${actDefId}"/>
			<input type="hidden" name="nodeId" id="nodeId" value="${nodeId}"/>
			<c:if test="${type eq 'startEvent'}">
				<table class="tableView" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="label" style="width:100px">
							说明：
						</td>
						<td>
							该脚本在流程启动时执行，用户可以使用execution做操作。 例如设置流程变量:execution.setVariable("total", 100);
						</td>
					</tr>
					<tr>
						<td class="label" style="width:100px">脚本：</td>
						<td>
							<textarea id="script" style="width:600px;height:250px;"  class="editArea">${afterModel.script}</textarea>
						</td>
					</tr>
					<tr>
						<td class="label" style="width:100px">脚本描述:</td>
						<td>
							<textarea id="remark" name="remark" style="width:600px;height:50px;" class="easyui-textarea validate[maxSize[64]]" promptPosition="bottom">${afterModel.remark}</textarea>
							<input type="hidden"  name="scriptType" value="1"/>
						</td>
					</tr>
				</table>
			</c:if>
			<c:if test="${type eq 'endEvent'}">
				<table class="tableView" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="label" style="width:100px">
							说明：
						</td>
						<td>
							该脚本在流程结束时执行，用户可以使用execution做操作。 例如设置流程变量:execution.setVariable("total", 100);
						</td>
					</tr>
					<tr>
						<td class="label" style="width:100px">脚本：</td>
						<td>
							<textarea id="script" style="width:600px;height:250px;"  class="editArea">${afterModel.script}</textarea>
						</td>
					</tr>
					<tr>
						<td class="label" style="width:100px">脚本描述:</td>
						<td>
							<textarea id="remark" name="remark" style="width:600px;height:50px;" class="easyui-textarea validate[maxSize[64]]" promptPosition="bottom">${afterModel.remark}</textarea>
							<input type="hidden"  name="scriptType" value="2"/>
						</td>
					</tr>
				</table>
			</c:if>
			<c:if test="${type eq 'script' }">
				<table class="tableView" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="label" style="width:100px">脚本：</td>
						<td>
							<textarea id="script" style="width:600px;height:250px;"  class="editArea">${scrptModel.script}</textarea>
						</td>
					</tr>
					<tr>
						<td class="label" style="width:100px">脚本描述:</td>
						<td>
							<textarea id="remark" name="remark" style="width:600px;height:50px;" class="easyui-textarea validate[maxSize[64]]" promptPosition="bottom">${scrptModel.remark}</textarea>
							<input type="hidden"  name="scriptType" value="3"/>
						</td>
					</tr>
				</table>
			</c:if>
			<c:if test="${type=='multiUserTask'||type=='userTask' }">
				<div id="tabs" class="easyui-tabs" fit="true" onSelect="onSelect">
					<div id="preTab" title="前置脚本" iconCls="icon-tab">
						<table class="tableView" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td class="label" style="width:100px">
									说明：
								</td>
								<td>
									该事件在启动该任务时执行，用户可以使用task做操作。 例如设置流程变量:task.setVariable("total", 100);
								</td>
							</tr>
							<tr>
								<td class="label" style="width:100px">脚本：</td>
								<td>
									<textarea id="scriptPre" style="width:600px;height:230px;"  class="editArea">${preModel.script}</textarea>
								</td>
							</tr>
							<tr>
								<td class="label" style="width:100px">脚本描述:</td>
								<td>
									<textarea id="remark" name="remark" style="width:600px;height:50px;" class="easyui-textarea validate[maxSize[64]]" promptPosition="bottom">${preModel.remark}</textarea>
									<input type="hidden"  name="scriptType" value="1"/>
								</td>
							</tr>
						</table>
					</div>
					<div id="afterTab" title="后置脚本" iconCls="icon-tab">
						<table class="tableView" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td class="label" style="width:100px">
									说明：
								</td>
								<td>
									该事件在任务完成时执行，用户可以使用task做操作。 例如设置流程变量:task.setVariable("total", 100);
								</td>
							</tr>
							<tr>
								<td class="label" style="width:100px">脚本：</td>
								<td>
									<textarea id="scriptAfter" style="width:600px;height:230px;"  class="editArea">${afterModel.script}</textarea>
								</td>
							</tr>
							<tr>
								<td class="label" style="width:100px">脚本描述:</td>
								<td>
									<textarea id="remark" name="remark" style="width:600px;height:50px;" class="easyui-textarea validate[maxSize[64]]" promptPosition="bottom">${afterModel.remark}</textarea>
									<input type="hidden"  name="scriptType" value="2"/>
								</td>
							</tr>
						</table>
					</div>
				</div>
				</c:if>
			</form>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>