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
	function doQuery(){
		var postData = {};
		postData.fileName = $("#fileName").val();
		$("#uploadFileList").grid("setGridParam", {postData:postData});
		$("#uploadFileList").trigger("reloadGrid");
	}
	
	function editUploadFile(id){
		var selectedIds = $("#uploadFileList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择一条记录！");
			return;
		}else if(selectedIds.length > 1){
			MessageUtil.alert("只能选择一条记录！");
			return;
		}else{
			topWin.Dialog.open({
				Title : "文件信息修改",
				URL : '${ctx}/uploadFile/toUpdate.html?id='+selectedIds[0],
				Width : 600,
				Height : 340,
				RefreshHandler : function(){
					$("#uploadFileList").trigger("reloadGrid");
				}});
		}
	}
	
	function viewUploadFile(id){
		topWin.Dialog.open({
			Title : "文件详细信息",
			URL : '${ctx}/uploadFile/toView.html?id='+id,
			Width : 600,
			Height : 380
			});
	}
	
	function delUploadFile(){
		var selectedIds = $("#uploadFileList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要删除的记录！");
			return;
		}
		var ids = selectedIds.join(",");
		MessageUtil.confirm("确定删除选择的记录吗？",function(){
			$.postc('${ctx}/uploadFile/deleteUploadFile.html', {ids:ids}, function(data){
				MessageUtil.show("删除记录成功！");
				$("#uploadFileList").trigger("reloadGrid");
			});
		});
	}
	

	function onGridComplete(){
		using("button", function(){
			$(".easyui-button").button();
		});
	}
	
	function operaFormatter(cellVal,options,rowd){
		var html = "";
		html += "<input type='button' class='easyui-button' style='margin:1px' onclick=\"javascript:viewUploadFile('"+rowd.fileId+"')\" value='查看' />";
		return html;
	}
	
	
	/**
	  *四舍五入保留小数位数
	  *numberRound 被处理的数
	  *roundDigit  保留几位小数位
	  */
	function roundFun(numberRound, roundDigit){
		if (numberRound >= 0) {
			var tempNumber = parseInt((numberRound * Math.pow(10, roundDigit) + 0.5))
					/ Math.pow(10, roundDigit);
			return tempNumber;
		} else {
			numberRound1 = -numberRound;
			var tempNumber = parseInt((numberRound1 * Math.pow(10, roundDigit) + 0.5))
					/ Math.pow(10, roundDigit);
			return -tempNumber;
		}
	}
	/**
	  *附件大小格式处理
	  */
	function fileSizeFormatter(value,options,rowd){
		if (null == value || value == '') {
			return "0 Bytes";
		}
		var unitArr = new Array("Bytes", "KB", "MB", "GB", "TB", "PB", "EB",
				"ZB", "YB");
		var index = 0;

		var srcsize = parseFloat(value);
		var size = roundFun(srcsize
				/ Math.pow(1024, (index = Math.floor(Math.log(srcsize)
						/ Math.log(1024)))), 2);
		return size + unitArr[index];
	}
	
	function dateFormatter(cellVal, options, rowd) {
		if (cellVal != null) {
			var d = new Date(cellVal);
			return DateFormat.format(d, "yyyy-MM-dd hh:mm:ss");
		} else {
			return "";
		}
	}
</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:95px;" title="查询条件" border="false">
		<form id="queryForm">
			<table class="formTable">
				<tr>
					<td class="label" style="width:100px">
						文件名：
					</td>
					<td>
						<input type="text" class="easyui-textinput" id="fileName" name="fileName" />
					</td>
					<td class="label" style="width:100px">
						上传用户：
					</td>
					<td>
						<input type="text" class="easyui-textinput" id="userName" name="userName" />
					</td>
					<td class="label" style="width:100px">
						上传时间：
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
			</table>
		</form>
		<div id="toolbar" class="easyui-toolbar" style="width:100%;margin-top:2px">
			<kpm:HasPermission permCode="SYS_FILE_QUERY">
				<a id="queryBtn" href="#" iconCls="icon-search" onclick="doQuery()">查询</a>
			</kpm:HasPermission>
			<kpm:HasPermission permCode="SYS_FILE_UPDATE">
			<a id="editBtn" href="#" iconCls="icon-edit" onclick="editUploadFile()">修改</a>
			</kpm:HasPermission>
			
			<kpm:HasPermission permCode="SYS_FILE_DEL">
			<a id="remove" href="#" iconCls="icon-remove" onclick="delUploadFile()">删除</a>
			</kpm:HasPermission>
		</div>
	</ui:Layout>
	<ui:Layout region="center" border="false" style="padding-top:1px">
		<ui:Grid id="uploadFileList" datatype="json" shrinkToFit="false" fit="true" viewrecords="true" pageable="true"
				url="${ctx}/uploadFile/uploadFileList.html" multiselect="true" rownumbers="false"
				rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
				<ui:Column header="文件ID" name="fileId" key="true" width="180"></ui:Column>
				<ui:Column header="文件名" name="fileName" width="200"></ui:Column>
				<ui:Column header="文件大小" name="fileSize" width="90" formatter="fileSizeFormatter" align="center"></ui:Column>
				<ui:Column header="文件类型" name="fileType" width="60" align="center"></ui:Column>
				<ui:Column header="上传用户" name="userName" width="120"></ui:Column>
				<ui:Column header="上传日期" name="createTime" width="130" formatter="dateFormatter"></ui:Column>
				<ui:Column header="下载次数" name="downloadNum" width="60"></ui:Column>
				<ui:Column header="操作" formatter="operaFormatter" width="80" fixed="true" align="center"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>