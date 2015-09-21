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
	var topWin = FrameHelper.getTop();
	function doQuery(){
		var jobName = $("#jobName").val();
		var scheduler = $("#scheduler").val();
		var beginTime = $("#beginTime").val();
		var endTime = $("#endTime").val();
		var resultType = $("#resultType").combobox("getValue");
		$("#jobRunLogList").grid("setGridParam", 
							{postData:{jobName:jobName, scheduler:scheduler, 
								beginTime:beginTime, endTime:endTime,resultType:resultType}});
		$("#jobRunLogList").trigger("reloadGrid");
	}
	
	function delJobRunLog(){
		var selectedIds = $("#jobRunLogList").grid("getGridParam", "selarrrow");
		if (selectedIds.length == 0){
			MessageUtil.alert("请选择待删除的任务！");
			return;
		}else{
			MessageUtil.confirm("确定删除选择的任务？",function(){
				$.postc("${ctx}/job/deleteJobRunLog.html?deleteType=DeleteByPrimaryKey",{ids:selectedIds.join(",")},function(data){
					MessageUtil.show("操作成功!");
					$("#jobRunLogList").trigger("reloadGrid");
				});
			});
		}
	}
	function dateFormatter(cellVal,options,rowd){
		if(cellVal==null){
			return "";
		}else{
			var d = new Date(cellVal);
			return DateFormat.format(d, "yyyy-MM-dd hh:mm:ss");
		}
	}	
	
	function showRunLogFormatter(cellVal,options,rowd){
		var html ="";
		html += "<a style=\"color:blue\" onclick=\"javascript:showRunLog('"+rowd.logId+"');\">查看 </a>";
		return html;
	}
	
	function showRunLog(id){
		topWin.Dialog.open({
			Title:"查看运行日志",
			Width:600,
			Height:300,
			URL:"${ctx}/job/showJobRunLog.html?id="+id
		});
	}
</script>
</head>
<body style="padding:1px">
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" title="查询条件" style="height:100px">
		<table class="formTable">
			<tr>
				<td class="label" style="width:100px">
					周知标识：
				</td>
				<td>
					<input type="text" class="easyui-textinput" name="jobName" id="jobName">
				</td>
				<td class="label" style="width:100px">
					运行地址：
				</td>
				<td>
					<input type="text" class="easyui-textinput" name="scheduler" id="scheduler">
				</td>
			</tr>
			<tr>
				<td class="label" style="width:100px">
					运行时间：
				</td>
				<td>
					<table>
						<tr>
							<td>
								<input type="text" class="easyui-datePicker" style="width:100px" id="beginTime" name="beginTime" dateFmt="yyyy-MM-dd HH:mm:ss"/>
							</td>
							<td style="width:15px">
								至
							</td>
							<td>
								<input type="text" class="easyui-datePicker" style="width:100px" id="endTime" name="endTime" dateFmt="yyyy-MM-dd HH:mm:ss"/>
							</td>
						</tr>
					</table>
				</td>
				<td class="label" style="width:100px">
					执行结果：
				</td>
				<td>
					<select class="easyui-combobox" id="resultType" name="resultType" dropdownHeight="75">
						<option value="">---请选择---</option>
						<option value="1" >成功</option>
						<option value="0" >失败</option>
					</select>
				</td>
			</tr>
		</table>
	</ui:Layout>
	<ui:Layout region="center" border="false" style="padding-top:1px">
		<ui:LayoutContainer fit="true">
			<ui:Layout region="north" style="height:33px" border="false">
				<div id="toolbar" class="easyui-toolbar" style="width:100%;">
					<a id="queryBtn" href="#" iconCls="icon-search" onclick="doQuery()">查询</a>
					<kpm:HasPermission permCode="SYS_JOBRUNLOG_DEL">
						<a id="delBtn" href="#" iconCls="icon-remove" onclick="delJobRunLog()">删除</a>
					</kpm:HasPermission>
				</div>
			</ui:Layout>
			<ui:Layout region="center" style="padding-top:1px" border="false">
				<ui:Grid id="jobRunLogList" datatype="json" shrinkToFit="true" fit="true" pageable="true"
					url="${ctx}/job/jobRunLogList.html" multiselect="true" viewrecords="true"
					rowNum="20" rowList="[10,20,50]">
					<ui:Column name="logId" key="true" hidden="true"></ui:Column>
					<ui:Column header="周知标识" name="jobName"  width="90"></ui:Column>
					<ui:Column header="调度服务器" name="scheduler"  width="90"></ui:Column>
					<ui:Column header="开始时间" name="beginTime"  width="90" formatter="dateFormatter"></ui:Column>
					<ui:Column header="结束时间" name="endTime"  width="90" formatter="dateFormatter"></ui:Column>
					<ui:Column header="执行结果" width="50" name="resultType" edittype="select" formatter="'select'" editoptions="{value:\"0:失败;1:成功\"}"></ui:Column>
					<ui:Column header="" formatter="showRunLogFormatter"></ui:Column>
				</ui:Grid>
			</ui:Layout>
		</ui:LayoutContainer>
	</ui:Layout>	
</ui:LayoutContainer>
</body>
</html>