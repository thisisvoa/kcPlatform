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
	var tableName = "TB_LOGON_LOG";
	function doQuery(){
		var postData = {};
		postData.terminalId = $("#terminalId").val();
		postData.policeId = $("#policeId").val();
		postData.userName = $("#userName").val();
		postData.orgName = $("#orgName").val();
		postData.startTime = $("#startTime").val();
		postData.endTime = $("#endTime").val();
		var sfgd = $('#sfgd').combobox('getValue');
		var url = "${ctx}/logonLog/logonLogList.html";
		if(sfgd == "1"){
			url = "${ctx}/logonLog/logonLogListByTable.html";
			var gdbmc = $('#gdbmc').val();
			if(!gdbmc){
				MessageUtil.alert("请选择归档表后再进行查询！");
				return false;
			}
			postData.tableName = gdbmc;
		}
		$("#logonLogList").grid("setGridParam", {url:url,postData:postData,loadError:myLoadError});
		$("#logonLogList").trigger("reloadGrid");
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
	
	function viewLogonLog(id){
		topWin.Dialog.open({
			Title : "登录日志查看",
			URL : '${ctx}/logonLog/toViewLogonLog.html?id='+id+'&tableName='+tableName,
			Width : 400,
			Height : 330
		});
	}
	
	function onGridComplete(){
		var sfgd = $('#sfgd').combobox('getValue');
		if(sfgd == "1"){
			tableName = $('#gdbmc').val();
		}else{
			tableName = "TB_LOGON_LOG";
		}
		using("button", function(){
			$(".easyui-button").button();
		});
	}
	
	function operaFormatter(cellVal,options,rowd){
		var html = "";
		html += "<input type='button' class='easyui-button' style='margin:1px' onclick=\"javascript:viewLogonLog('"+rowd.logId+"')\" value='查看' />";
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
			URL:"${ctx}/gdqd/toTableSelect.html?cxmc="+cxmc+"&gdbmc=TB_LOGON_LOG",
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
	<ui:Layout region="north" style="height:100px;" title="查询条件">
		<form id="queryForm">
			<table class="formTable">
				<tr>
					<td class="label" style="width:100px">
						终端标识(IP)：
					</td>
					<td style="width:130px">
						<input type="text" class="easyui-textinput" id="terminalId" name="terminalId" watermark="支持模糊查询" />
					</td>
					<td class="label" style="width:100px">
						警号：
					</td>
					<td style="width:130px">
						<input type="text" class="easyui-textinput" id="policeId" name="policeId" watermark="支持模糊查询" />
					</td>
					<td class="label" style="width:100px">
						用户名：
					</td>
					<td>
						<input type="text" class="easyui-textinput" id="userName" name="userName" watermark="支持模糊查询" />
					</td>
					<td class="label" style="width:100px">
						单位名称：
					</td>
					<td>
						<input type="text" class="easyui-textinput" id="orgName" name="orgName" watermark="支持模糊查询" />
					</td>
				</tr>
				<tr>
					<td class="label" style="width:100px">
						登录时间：
					</td>
					<td colspan="3">
						<table>
							<tr>
								<td><input class="easyui-datePicker" id="startTime" name="startTime" dateFmt="yyyy-MM-dd HH:mm:ss"></td>
								<td>至</td>
								<td><input id="endTime" name="endTime" type="text" class="easyui-datePicker" dateFmt="yyyy-MM-dd HH:mm:ss" /></td>
							</tr>
						</table>
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
						<kpm:HasPermission permCode="SYS_LOGON_LOG_QUERY">
						<a id="queryBtn" href="#" iconCls="icon-search" onclick="doQuery()">查询</a>
						</kpm:HasPermission>
					</div>
				</ui:Layout>
				<ui:Layout region="center" border="false" style="padding-top:1px">
					<ui:Grid id="logonLogList" datatype="json" shrinkToFit="false" fit="true" viewrecords="true" pageable="true"
							url="${ctx}/logonLog/logonLogList.html" multiselect="true" rownumbers="false"
							rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
							<ui:Column name="logId" key="true" hidden="true"></ui:Column>
							<ui:Column header="用户名" name="userName" width="80"></ui:Column>
							<ui:Column header="登录账号" name="loginId" width="70"></ui:Column>
							<ui:Column header="警号" name="policeId" width="70"></ui:Column>
							<ui:Column header="身份证号" name="idCard" width="140"></ui:Column>
							<ui:Column header="单位名称" name="orgName" width="180"></ui:Column>
							<ui:Column header="终端标识(IP)" name="terminalId" width="90"></ui:Column>
							<ui:Column header="登录时间" name="logonTime" width="130" formatter="dateFormatter"></ui:Column>
							<ui:Column header="登出时间" name="logoutTime" width="130" formatter="dateFormatter"></ui:Column>
							<ui:Column header="登录结果" name="logonResult" width="90" edittype="select" formatter="'select'" editoptions="{value:\"0:<span style='color:red'>登录失败</span>;1:登录成功\"}"></ui:Column>
							<ui:Column header="操作" formatter="operaFormatter" width="90" fixed="true"></ui:Column>
					</ui:Grid>
				</ui:Layout>
		</ui:LayoutContainer>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>