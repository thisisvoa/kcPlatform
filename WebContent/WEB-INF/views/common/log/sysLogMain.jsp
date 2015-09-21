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
	$parsed(function(){
		sfgdChange();
	});
	var tableName = "TB_SYS_LOG";
	function doQuery(){
		var postData = {};
		postData.policeId = $("#policeId").val();
		postData.userName = $("#userName").val();
		postData.loginId = $("#loginId").val();
		postData.terminalId = $("#terminalId").val();
		postData.orgName = $("#orgName").val();
		postData.moduleName = $("#moduleName").val();
		postData.startTime = $("#startTime").val();
		postData.endTime = $("#endTime").val();
		postData.logType = $("#logType").combobox("getValue");
		var sfgd = $('#sfgd').combobox('getValue');
		var url = "${ctx}/sysLog/sysLogList.html";
		if(sfgd == "1"){
			url = "${ctx}/sysLog/sysLogListByTable.html";
			var gdbmc = $('#gdbmc').val();
			if(!gdbmc){
				MessageUtil.alert("请选择归档表后再进行查询！");
				return false;
			}
			postData.tableName = gdbmc;
		}
		$("#sysLogList").grid("setGridParam", {url:url,postData:postData,loadError:myLoadError});
		$("#sysLogList").trigger("reloadGrid");
	}
	
	function sfgdChange(){
		var sfgd = $('#sfgd').combobox('getValue');
		if(sfgd == "0"){			
			$('#gdbmc').attr("disabled",true);
			$('#gdbmcBtn').attr("disabled",true);
			$('.gdbmTd').hide();
		}else{
			$('#gdbmc').attr("disabled",false);
			$('#gdbmcBtn').attr("disabled",false);
			$('.gdbmTd').show();
		}	
	}
	
	function myLoadError(){
		var tableName = $('#gdbmc').val();
		MessageUtil.alert("查询归档表:"+tableName+"失败,请确认该归档表是否存在！");
	}
	
	function viewSysLog(id){
		topWin.Dialog.open({
			Title : "操作日志查看",
			URL : '${ctx}/sysLog/toViewSysLog.html?id='+id+'&tableName='+tableName,
			Width : 600,
			Height : 420
			});
	}
	
	function onGridComplete(){
		var sfgd = $('#sfgd').combobox('getValue');
		if(sfgd == "1"){
			tableName = $('#gdbmc').val();
		}else{
			tableName = "TB_SYS_LOG";
		}
		using("button", function(){
			$(".easyui-button").button();
		});
	}
	function dateFormatter(cellVal,options,rowd){
		if(cellVal!=null){
			var d = new Date(cellVal);
			return DateFormat.format(d,"yyyy-MM-dd hh:mm:ss");
		}else{
			return "";
		}
	}
	function operaFormatter(cellVal,options,rowd){
		var html = "";
		html += "<input type='button' class='easyui-button' style='margin:1px' onclick=\"javascript:viewSysLog('"+rowd.logId+"')\" value='查看' />";
		return html;
	}
	
	function tableSelect(obj) {
		var cxmc = $("#gdbmc").val();
		var selector = topWin.Dialog.open({
			Title:"归档表选择器",
			Width:600,
			Height:400,
			URL:"${ctx}/gdqd/toTableSelect.html?cxmc="+cxmc+"&gdbmc=TB_SYS_LOG",
			RefreshHandler:function(){
				var selectedRowd = selector.getData().selectedRowd;
				$("#gdbmc").val(selectedRowd.gdhbmc);
			}
		});	
	}
	
</script>
</head>
<body style="padding:1px">
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:98px;" title="查询条件">
		<form id="queryForm">
			<table class="formTable">
				<tr>
					<td class="label" style="width:100px">
						终端标识(IP)：
					</td>
					<td style="width:130px">
						<input type="text" class="easyui-textinput" id="terminalId" name="terminalId" watermark="仅支持精确查询" />
					</td>
					<td class="label" style="width:100px">
						警号：
					</td>
					<td  style="width:130px">
						<input type="text" class="easyui-textinput" id="policeId" name="policeId" watermark="仅支持精确查询" />
					</td>
					<td class="label" style="width:100px">
						操作时间：
					</td>
					<td>
						<table>
							<tr>
								<td><input class="easyui-datePicker" id="startTime" name="startTime" dateFmt="yyyy-MM-dd HH:mm:ss"></td>
								<td>至</td>
								<td><input id="endTime" name="endTime" type="text" class="easyui-datePicker" dateFmt="yyyy-MM-dd HH:mm:ss" /></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="label" style="width:100px">
						操作类型：
					</td>
					<td>
						<select class="easyui-combobox" id="logType" name="logType" dropdownHeight="145">
								<option value="">---请选择---</option>
								<option value="1" >查询</option>
								<option value="2" >新增</option>
								<option value="3" >删除</option>
								<option value="4" >修改</option>
								<option value="5" >授权</option>
								<option value="6" >导出/下载</option>
							</select>
					</td>
					<td class="label" style="width:100px">
						是否已归档：
					</td>
					<td >
						<select id="sfgd" name="sfgd" class="easyui-combobox" style="width: 60px;" selValue="0" dropdownHeight="50" onchange="sfgdChange">
							<option value="0">否</option>
							<option value="1">是</option>
						</select>
					</td>
					<td align="right" class="gdbmTd">
						归档表名：
					</td>
					<td class="gdbmTd">
						<table style="width:100%;height:100%;">
							<tr>
								<td style="width:130px;">
									<input type="text" id="gdbmc" name="gdbmc" class="easyui-textinput" readonly="readonly" onclick="tableSelect()"/>	
								</td>
								<td  align="left">
									<input type="button" class="easyui-button" value="选 择"  onclick="tableSelect()"/>
																
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</ui:Layout>
	<ui:Layout region="center" border="false" style="padding-top:1px">
		<ui:LayoutContainer fit="true">
				<ui:Layout region="north" style="height:32px;" border="false">
					<div id="toolbar" class="easyui-toolbar" style="width:100%;">
						<kpm:HasPermission permCode="SYS_OPERATE_LOG_QUERY">
						<a id="queryBtn" href="#" iconCls="icon-search" onclick="doQuery()">查询</a>
						</kpm:HasPermission>
					</div>
				</ui:Layout>
				<ui:Layout region="center" border="false" style="padding-top:1px">
					<ui:Grid id="sysLogList" datatype="json" shrinkToFit="false" fit="true" viewrecords="true" pageable="true"
							url="${ctx}/sysLog/sysLogList.html" multiselect="true" rownumbers="false"
							rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
							<ui:Column name="logId" key="true" hidden="true"></ui:Column>
							<ui:Column header="功能模块" name="moduleName" width="90"></ui:Column>
							<ui:Column header="操作类型" name="logType" width="60"  edittype="select" formatter="'select'" editoptions="{value:\"1:查询;2:新增;3:删除;4:修改;5:授权;6:导出/下载\"}"></ui:Column>
							<ui:Column header="操作描述" name="operateDesc" width="250"></ui:Column>
							<ui:Column header="终端标识(IP)" name="terminalId" width="90"></ui:Column>
							<ui:Column header="操作用户" name="userName" width="70"></ui:Column>
							<ui:Column header="警号" name="policeId" width="60"></ui:Column>
							<ui:Column header="操作时间" name="operateTime" width="120" formatter="dateFormatter"></ui:Column>
							<ui:Column header="操作内容" name="operateContent" width="200"></ui:Column>
							<ui:Column header="操作" formatter="operaFormatter" width="90" fixed="true"></ui:Column>
					</ui:Grid>
			</ui:Layout>
		</ui:LayoutContainer>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>