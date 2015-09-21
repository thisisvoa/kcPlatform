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
	var tableName = "TB_INTERFACE_LOG";
	function doQuery(){
		var postData = {};
		postData.callTime = $("#callTime").val();
		postData.callerName = $("#callerName").val();
		postData.terminalId = $("#terminalId").val();
		postData.startTime = $("#startTime").val();
		postData.endTime = $("#endTime").val();
		var sfgd = $('#sfgd').combobox('getValue');
		var url = "${ctx}/interfaceLog/interfaceLogList.html";
		if(sfgd == "1"){
			url = "${ctx}/interfaceLog/interfaceLogListByTable.html";
			var gdbmc = $('#gdbmc').val();
			if(!gdbmc){
				MessageUtil.alert("请选择归档表后再进行查询！");
				return false;
			}
			postData.tableName = gdbmc;
		}
		$("#interfaceLogList").grid("setGridParam", {url:url,postData:postData,loadError:myLoadError});
		$("#interfaceLogList").trigger("reloadGrid");
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
	
	function viewInterfaceLog(id){
		topWin.Dialog.open({
			Title : "接口日志查看",
			URL : '${ctx}/interfaceLog/toViewInterfaceLog.html?id='+id+'&tableName='+tableName,
			Width : 600,
			Height : 360
			});
	}
	
	function onGridComplete(){
		var sfgd = $('#sfgd').combobox('getValue');
		if(sfgd == "1"){
			tableName = $('#gdbmc').val();
		}else{
			tableName = "TB_INTERFACE_LOG";
		}
		using("button", function(){
			$(".easyui-button").button();
		});
	}
	
	function operaFormatter(cellVal,options,rowd){
		var html = "";
		html += "<input type='button' class='easyui-button' style='margin:1px' onclick=\"javascript:viewInterfaceLog('"+rowd.logId+"')\" value='查看' />";
		return html;
	}
	function dateFormatter(cellVal,options,rowd){
		if(cellVal!=null){
			var d = new Date(cellVal);
			return DateFormat.format(d,"yyyy-MM-dd hh:mm:ss");
		}else{
			return "";
		}
	}
	
	function tableSelect(obj) {
		var cxmc = $("#gdbmc").val();
		var selector = topWin.Dialog.open({
			Title:"归档表选择器",
			Width:600,
			Height:400,
			URL:"${ctx}/gdqd/toTableSelect.html?cxmc="+cxmc+"&gdbmc=TB_INTERFACE_LOG",
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
	<ui:Layout region="north" style="height:95px;" title="查询条件">
		<form id="queryForm">
			<table class="formTable">
				<tr>
					<td class="label" style="width:100px">
						请求方名称：
					</td>
					<td style="width:150px">
						<input type="text" class="easyui-textinput" id="callerName" name="callerName" watermark="支持模糊查询" />
					</td>
					<td class="label" style="width:100px">
						终端标识(IP)：
					</td>
					<td style="width:150px">
						<input type="text" class="easyui-textinput" id="terminalId" name="terminalId" watermark="支持模糊查询" />
					</td>
					<td class="label" style="width:100px">
						调用时间：
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
					<td class="gdbmTd" colspan="3">
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
						<kpm:HasPermission permCode="SYS_INTERFACE_LOG_QUERY">
						<a id="queryBtn" href="#" iconCls="icon-search" onclick="doQuery()">查询</a>
						</kpm:HasPermission>
					</div>
				</ui:Layout>
				<ui:Layout region="center" border="false" style="padding-top:1px">
					<ui:Grid id="interfaceLogList" datatype="json" shrinkToFit="false" fit="true" viewrecords="true" pageable="true"
							url="${ctx}/interfaceLog/interfaceLogList.html" multiselect="true" rownumbers="false"
							rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
							<ui:Column name="logId" key="true" hidden="true"></ui:Column>
							<ui:Column header="请求方名称" name="callerName" width="120"></ui:Column>
							<ui:Column header="接口描述" name="interfaceDesc" width="200"></ui:Column>
							<ui:Column header="调用时间" name="callTime" width="150" formatter="dateFormatter"></ui:Column>
							<ui:Column header="终端标识(IP)" name="terminalId" width="90"></ui:Column>
							<ui:Column header="返回条目数" name="resultCount" width="90"></ui:Column>
							<ui:Column header="操作" formatter="operaFormatter" width="90" fixed="true"></ui:Column>
					</ui:Grid>
				</ui:Layout>
		</ui:LayoutContainer>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>