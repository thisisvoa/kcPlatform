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
	function remove(ids){
		MessageUtil.confirm("确定删除选择的流程日志吗？",function(){
			$.postc('${ctx}/workflow/bpm/runLog/del.html', {ids:ids}, function(data){
				MessageUtil.show("删除流程日志成功！");
				$("#bpmRunLogList").trigger("reloadGrid");
			});
		});
	}
	

	function onGridComplete(){
		using("linkbutton", function(){
			$(".easyui-linkbutton").linkbutton();
		});
	}
	
	function operaFormatter(cellVal,options,rowd){
		var html = "";
		html += "<a plain=\"true\" class=\"easyui-linkbutton\" href=\"#\" iconCls=\"icon-remove\" onclick=\"remove('"+rowd.id+"')\">删除</a>";
		return html;
	}
	
	function dateFormatter(cellVal, options, rowd) {
		if (cellVal != null) {
			var d = new Date(cellVal);
			return DateFormat.format(d, "yyyy-MM-dd hh:mm:ss");
		} else {
			return "";
		}
	}
	//任务状态
	function operatorTypeFormater(cellVal, options, rowd) {
		switch (cellVal) {
			case 0:
				return "<span class='green'>启动流程</span>";
			case 1:
				return "<span class='green'>交办</span>";
			case 2:
				return "<span class='red'>追回</span>";
			case 3:
				return "<span class='red'>删除流程实例</span>";
			case 4:
				return "<span class='green'>同意(投票)</span>";
			case 5:
				return "<span class='red'>反对(投票)</span>";
			case 6:
				return "<span class='green'>弃权(投票)</span>";
			case 7:
				return "<span class='green'>补签</span>";
			case 8:
				return "<span class='red'>驳回</span>";
			case 9:
				return "<span class='red'>驳回到发起人</span>";
			case 10:
				return "<span class='red'>删除任务</span>";
			case 11:
				return "<span class='green'>代理任务</span>";
			case 13:
				return "<span class='green'>锁定任务</span>";
			case 14:
				return "<span class='green'>任务解锁</span>";
			case 15:
				return "<span class='green'>添加意见</span>";
			case 16:
				return "<span class='green'>任务指派</span>";
			case 17:
				return "<span class='green'>设定所有人</span>";
			case 18:
				return "<span class='green'>结束任务</span>";
			case 21:
				return "<span class='red'>终止流程</span>";
			case 22:
				return "<span class='green'>保存草稿</span>";
			case 23:
				return "<span class='green'>删除流程草稿</span>";
			case 24:
				return "<span class='green'>会签直接通过</span>";
			case 25:
				return "<span class='red'>会签直接不通过</span>";
			default:
				return "<span class='red'>其他</span>";
		}
		return "";
	}
</script>
</head>
<body>
<ui:Panel fit="true" border="false" style="margin-top:5px" title="流程实例-【${processRun.subject}】操作日志明细">
		<ui:Grid id="bpmRunLogList" datatype="json" shrinkToFit="false" fit="true" viewrecords="true" pageable="true"
				url="${ctx}/workflow/bpm/runLog/list.html?runId=${processRun.runId}" multiselect="true" rownumbers="false"
				rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
				<ui:Column name="id" key="true" hidden="true"></ui:Column>
				<ui:Column header="流程标题" name="processSubject" width="220"></ui:Column>
				<ui:Column header="用户名称" name="userName" width="120"></ui:Column>
				<ui:Column header="创建时间" name="createTime" width="130" formatter="dateFormatter"></ui:Column>
				<ui:Column header="操作类型" name="operatorType" width="90" formatter="operatorTypeFormater"></ui:Column>
				<ui:Column header="备注" name="memo" width="280"></ui:Column>
				<ui:Column header="操作" formatter="operaFormatter" width="70" fixed="true"></ui:Column>
		</ui:Grid>
</ui:Panel>
</body>
</html>