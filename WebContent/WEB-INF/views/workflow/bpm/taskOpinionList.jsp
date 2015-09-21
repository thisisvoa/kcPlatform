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
<title></title>
<script type="text/javascript">
	var topWin = FrameHelper.getTop();
	function viewBpmTaskOpinion(id){
		topWin.Dialog.open({
			Title : "查看",
			URL : '${ctx}/bpmTaskOpinion/toView.html?id='+id,
			Width : 600,
			Height : 400
			});
	}
	
	function dateFormatter(cellVal, options, rowd) {
		if (cellVal != null) {
			var d = new Date(cellVal);
			return DateFormat.format(d, "yyyy-MM-dd hh:mm:ss");
		} else {
			return "";
		}
	}
	//处理时长
	function durTimeFormatter(cellVal, options, rowd){
		var time = "";
	     if (cellVal == null) {
	       return "";
	     }
	     var days = parseInt(cellVal / 1000 / 60 / 60 / 24);
	     if (days > 0) {
	       time = time + days + "天";
	     }
	     var hourMillseconds = cellVal - days * 1000 * 60 * 60 * 24;
	     var hours = parseInt(hourMillseconds / 1000 / 60 / 60);
	     if (hours > 0) {
	       time = time + hours + "小时";
	     }
	     var minuteMillseconds = cellVal - days * 1000 * 60 * 60 * 24 - hours * 1000 * 60 * 60;
	     var minutes = parseInt(minuteMillseconds / 1000 / 60);
	     if (minutes > 0) {
	       time = time + minutes + "分钟";
	     }
	     /* var millseconds = cellVal - days * 1000 * 60 * 60 * 24 - hours * 1000 * 60 * 60 - minutes*1000*60;
	     var seconds = parseInt(millseconds/1000);
	     if (seconds > 0) {
	    	 time = time + seconds + "秒"; 
	     } */
	     return time;
	}
	
	//任务状态
	function taskStatusFormater(cellVal, options, rowd) {
		switch (cellVal) {
			case -2:
				return "<font color='red'>尚未审批</font>";
			case -1:
				return "<font color='red'>正在审批</font>";
			case 0:
				return "<font color='red'>弃权</font>";
			case 1:
				return "<font color='green'>同意</font>";
			case 2:
				return "<font color='orange'>反对</font>";
			case 3:
				return "<font color='red'>驳回</font>";
			case 4:
				return "<font color='red'>撤销</font>";
			case 5:
				return "<font color='green'>会签通过</font>";
			case 6:
				return "<font color='red'>会签不通过</font>";
			case 7:
				return "<font color='red'>知会意见</font>";
			case 8:
				return "<font color='red'>更改执行路径</font>";
			case 9:
				return "<font color='red'>终止</font>";
			case 10:
				return "<font color='red'>沟通意见</font>";
			case 14:
				return "<font color='red'>终止</font>";
			case 15:
				return "<font color='red'>沟通 </font>";
			case 16:
				return "<font color='orange'>办结转发</font>";
			case 17:
				return "<font color='orange'>撤销</font>";
			case 18:
				return "<font color='orange'>删除</font>";
			case 19:
				return "<font color='orange'>抄送</font>";
			case 20:
				return "<font color='green'>沟通反馈</font>";
			case 21:
				return "<font color='red'>转办</font>";
			case 22:
				return "<font color='red'>取消转办</font>";
			case 23:
				return "<font color='red'>更改执行人</font>";
			case 24:
				return "<font color='red'>驳回到发起人</font>";
			case 25:
				return "<font color='red'>撤销(撤销到发起人)</font>";
			case 26:
				return "<font color='brown'>代理</font>";
			case 27:
				return "<font color='green'>取消代理</font>";
			case 28:
				return "<font color='green'>保存表单</font>";
			case 29:
				return "<font color='green'>驳回取消</font>";
			case 30:
				return "<font class='brown'>撤销取消</font>";
			case 31:
				return "<font class='brown'>通过取消</font>";
			case 32:
				return "<font class='brown'>反对取消</font>";
			case 33:
				return "<font class='green'>提交</font>";
			case 34:
				return "<font class='green'>重新提交</font>";
			case 35:
				return "<font class='brown'>干预</font>";
			case 36:
				return "<font class='brown'>重启任务</font>";
			case 38:
				return "<font color='green'>流转</font>";
			case 40:
				return "<font color='red'>代提交</font>";
			case 11:
			case 12:
			case 13:
			case 37:
			case 39:
		}
		return "";
	}
</script>
</head>
<body>
<ui:Panel fit="true" border="false" style="margin-top:5px;" title="流程实例-【${processRun.subject}】审批历史明细。">
	<ui:Grid id="bpmTaskOpinionList" datatype="json" shrinkToFit="false" fit="true" viewrecords="true" pageable="false" 
			url="${ctx}/workflow/bpm/taskOpinion/list.html?actInstId=${processRun.actInstId}" 
			multiselect="true" rownumbers="false" rowNum="9999">
			<ui:Column name="opinionId" key="true" hidden="true"></ui:Column>
			<ui:Column header="任务名称" name="taskName" width="150"></ui:Column>
			<ui:Column header="开始时间" name="startTime" width="130" formatter="dateFormatter"></ui:Column>
			<ui:Column header="结束时间" name="endTime" width="130" formatter="dateFormatter"></ui:Column>
			<ui:Column header="处理时长" name="durTime" width="90" formatter="durTimeFormatter"></ui:Column>
			<ui:Column header="执行人" name="exeUserName" width="90"></ui:Column>
			<ui:Column header="处理意见" name="opinion" width="250"></ui:Column>
			<ui:Column header="审批状态" name="checkStatus" width="90" formatter="taskStatusFormater"></ui:Column>
	</ui:Grid>
</ui:Panel>
</body>
</html>