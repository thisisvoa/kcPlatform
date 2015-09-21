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
<script type="text/javascript" src="${ctx}/ui/js/util/json2.js"></script>
<script type="text/javascript">
	var topWin = FrameHelper.getTop();
	function save(){
		if($("#editForm").validate("validate")){
			var triggerList = $("#triggerList").grid("getDataIDs");
			if(triggerList.length==0){
				MessageUtil.alert("调度规则不能为空！");
				return;
			}
			var triggerParam = [];
			for(var i=0;i<triggerList.length;i++){
				var id = triggerList[i];
				var type = $("#"+id+"_triggerType").val();
				var repeatInterval = $("#"+id+"_repeatInterval").val();
				var cronExpr = $("#"+id+"_cronExpr").val();
				if(type==""){
					MessageUtil.alert("调度类型不能为空！");
					return ;
				}
				if(type=="simple" && repeatInterval==""){
					MessageUtil.alert("间隔时间不能为空！");
					return ;
				}
				
				if(type=="cron" && cronExpr==""){
					MessageUtil.alert("Corn表达式不能为空！");
					return ;
				}
				var d = {type:type, repeatInterval:repeatInterval, cronExpr:cronExpr};
				triggerParam.push(d);
			}
			var triggerParamStr = JSON.stringify(triggerParam);
			var data = $("#editForm").serialize();
			data += ("&triggerParam="+triggerParamStr);
			if("${type}"=="add"){
				$.postc("${ctx}/job/addJob.html",data, function(d){
					$("#saveBtn").attr("disabled",true);
					$("#resetBtn").removeAttr("disabled");
					parentDialog.markUpdated();
					MessageUtil.show("操作成功!", "系统提示");
					$("#saveBtn").attr("disabled",true);
					$("#resetBtn").removeAttr("disabled");
				});
			}else{
				MessageUtil.confirm("确定保存该任务信息吗？",function(){
					$.postc("${ctx}/job/updateJob.html",data, function(){
						parentDialog.markUpdated();
						MessageUtil.show("操作成功!", "系统提示");
					});	
				});
			}
		}
	};
	
	function formReset(){
		$("#editForm")[0].reset();
		$("#resetBtn").attr("disabled",true);
		$("#saveBtn").removeAttr("disabled");
	}
	
	function triggerTypeFormatter(cellVal,options,rowd){
		var html = "<select id=\""+rowd.id+"_triggerType\" style='width:100%' onchange=triggerTypeChange('"+rowd.id+"')>";
			html += "<option value=\"\">--请选择--</option>";
			if(cellVal=="simple"){
				html += "<option value=\"simple\" selected>Simple</option>";
			}else{
				html += "<option value=\"simple\">Simple</option>";
			}
			if(cellVal=="cron"){
				html += "<option value=\"cron\" selected>Cron</option>";
			}else{
				html += "<option value=\"cron\">Cron</option>";
			}
			html += "</select>";
		return html;
	}
	
	function repeatIntervalForamter(cellVal,options,rowd){
		var html = "";
		if(cellVal!=null && cellVal!=""){
			html += "<input type=\"text\" value=\""+cellVal+"\" id=\""+rowd.id+"_repeatInterval\" style='width:98%'>";	
		}else{
			html += "<input type=\"text\" disabled id=\""+rowd.id+"_repeatInterval\" style='width:98%'>";
		}
		
		return html;
	}
	
	function cronExprForamter(cellVal,options,rowd){
		var html = "";
		if(cellVal!=null && cellVal!=""){
			html += "<input type=\"text\" value=\""+cellVal+"\" id=\""+rowd.id+"_cronExpr\" style='width:98%'>";
		}else{
			html += "<input type=\"text\" disabled id=\""+rowd.id+"_cronExpr\" style='width:98%'>";
		}
		
		return html;
	}
	
	function operationForamter(cellVal,options,rowd){
		var html ="";
		html += "<a style=\"color:blue\" onclick=\"javascript:delTrigger('"+rowd.id+"');\">删除 </a>";
		return html;
	}
	
	function delTrigger(id){
		$("#triggerList").grid("delRowData",id);
	}
	
	function addNewTrigger(){
		var id = (new Date()).getTime();
		$("#triggerList").grid("addRowData",id,{id:id,type:"",repeatInterval:"",cronExpr:""});
	}
	
	function triggerTypeChange(id){
		var type = $("#"+id+"_triggerType").val();
		if(type==""){
			$("#"+id+"_repeatInterval").val("");
			$("#"+id+"_cronExpr").val("");
			$("#"+id+"_repeatInterval").attr("disabled",true);
			$("#"+id+"_cronExpr").attr("disabled",true);
		}else if(type=="simple"){
			$("#"+id+"_cronExpr").val("");
			$("#"+id+"_cronExpr").attr("disabled",true);
			$("#"+id+"_repeatInterval").removeAttr("disabled",true);
		}else if(type=="cron"){
			$("#"+id+"_repeatInterval").val("");
			$("#"+id+"_repeatInterval").attr("disabled",true);
			$("#"+id+"_cronExpr").removeAttr("disabled",true);
		}
	}
	
	$parsed(function(){
		var triggerParamStr = '${job.triggerParam}';
		if(triggerParamStr!=""){
			var triggerParam = eval("("+triggerParamStr+")");
			for(var i=0;i<triggerParam.length;i++){
				var id = (new Date()).getTime();
				var trigger = triggerParam[i];
				$("#triggerList").grid("addRowData",id,{id:id,type:trigger.type,repeatInterval:trigger.repeatInterval,cronExpr:trigger.cronExpr});
			}
		}
	});
	
	function showCronInfo(){
		topWin.Dialog.open({
			Title:"Cron详细说明",
			Width:600,
			Height:500,
			URL:"${ctx}/job/showCronInfoForm.html"
		});
	}
	
</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:30px;text-align:center;padding-top:5px" border="false">
		<c:if test="${type == 'add'}">
			<input type="button" id="resetBtn" class="easyui-button" disabled value="再次添加  " onclick="formReset();"/>
		</c:if>
		<input type="button" id="saveBtn" class="easyui-button" value="保存 " onclick="save()"/>
		<input type="button" value="关 闭 " class="easyui-button" onclick="javascript:parentDialog.close();"/>
	</ui:Layout>
	<ui:Layout region="center" style="padding:1px;overflow:hidden" border="false">
		<form id="editForm" class="easyui-validate" style="margin-top: 5px;position: relative;">
			<input type="hidden" name="jobId" id="jobId" value="${job.jobId}" />
			<fieldset style="width:99%;position: relative;">
				<legend>基本信息</legend>
				<table class="formTable">
					<tr>
						<td class="label" style="width:80px">任务名称：</td>
						<td colspan="3">
							<input type="text" class="easyui-textinput validate[required,maxSize[64]]" id="title"
								name="title" value="${job.title}" />
								<span class="star">*</span>
						</td>
					</tr>
					<tr>
						<td class="label" style="width:80px">周知标识：</td>
						<td>
							<input type="text" class="easyui-textinput validate[required,maxSize[64]]" id="jobName"
							name="jobName" value="${job.jobName}" />
							<span class="star">*</span>
						</td>
						
						<td class="label" style="width:80px">分组名：</td>
						<td><input type="text" class="easyui-textinput validate[maxSize[64]]" id="jobGroupName"
							name="jobGroupName" value="${job.jobGroupName}" />
						</td>
					</tr>
					<tr>
						<td class="label" style="width:80px">备注：</td>
						<td colspan="3">
							<textarea class="easyui-textarea validate[maxSize[100]]" id="remark" name="remark" promptPosition="bottom" style="width:450px;height:60px">${job.remark}</textarea>
						</td>
					</tr>
				</table>
			</fieldset>
			<fieldset style="width:99%;">
				<legend>调度规则</legend>
				<table class="formTable">
					<tr>
						<td class="label" style="width:80px">开始时间：</td>
						<td>
							<input type="text" class="easyui-datePicker" style="width:100px" id="startTime" name="startTime" value="${job.startTime}" dateFmt="yyyyMMddHHmmss"/>
						</td>
						<td class="label" style="width:80px">结束时间：</td>
						<td>
							<input type="text" class="easyui-datePicker" style="width:100px" id="endTime" name="endTime" value="${job.endTime}" dateFmt="yyyyMMddHHmmss"/>
						</td>
						<td style="width:100px">
							<a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="addNewTrigger()">新增规则</a>
						</td>
					</tr>
					<tr>
						<td colspan="5">
							<ui:Grid id="triggerList" datatype="json" shrinkToFit="true" 
								fit="true" pageable="false" width="400" height="120" url=""
								multiselect="false" >
								<ui:Column name="id" hidden="true" key="true"></ui:Column>
								<ui:Column header="类型" name="type" formatter="triggerTypeFormatter"  width="90"></ui:Column>
								<ui:Column header="间隔时间" name="repeatInterval" formatter="repeatIntervalForamter" width="120"></ui:Column>
								<ui:Column header="Corn表达式" name="cronExpr" formatter="cronExprForamter" width="120"></ui:Column>
								<ui:Column header="" formatter="operationForamter" width="40" align="center"></ui:Column>
							</ui:Grid>
						</td>
					</tr>
				</table>
			</fieldset>
		</form>
	</ui:Layout>
	<ui:Layout region="south" style="height:90px;padding:5px;" border="false">
		<b>提示：</b><br>
		1、每条任务必须在程序中拥有对应的Bean，即实现com.casic27.platform.base.job.Job接口，且Bean名称为"任务组名.周知标识"，TAG可为空，而周知标识不能为空；<br>
		2、开始时间为空表示即时开始，结束时间为空表示永不结束，格式为yyyyMMddhhmmss；<br>
		3、Corn表达式的配置<a href="#" style="color:#4040a0" onclick="showCronInfo()">参见详细说明</a>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>