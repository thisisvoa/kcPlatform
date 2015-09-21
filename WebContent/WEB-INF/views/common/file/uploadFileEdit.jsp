
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
<title></title>
<script type="text/javascript">
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
	function formateFileSize(value){
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
		$("#fileSizeDom").html(size + unitArr[index]);
	}
	$parsed(function(){
		formateFileSize('${uploadFile.fileSize}');
	});
	function doSave(){
		if($("#editForm").validate("validate")){
			var parms = $("#editForm").serialize();
			$.postc("${ctx}/uploadFile/updateUploadFile.html", parms, function(data){
				MessageUtil.show("修改成功！");
				parentDialog.markUpdated();
			});
		}
	}
	function reAdd(){
		$("#editForm")[0].reset();
		$("#saveBtn").removeAttr("disabled");
		$("#reAddBtn").attr("disabled",true);
	}
</script>
<body>
<ui:Panel fit="true">
	<form id="editForm" class="easyui-validate" style="margin-top:5px;">
		<input type="hidden" name="fileId" id="fileId" value="${uploadFile.fileId}"/>
		<table class="tableView">
			<tr>
				<td class="label">
					文件名：
				</td>
				<td>
				${uploadFile.fileName}
				</td>
			</tr>
			<tr>
				<td class="label">
					存储文件名：
				</td>
				<td>
				${uploadFile.diskFileName}
				</td>
			</tr>
			<tr>
				<td class="label">
					文件大小：
				</td>
				<td>
				<span id="fileSizeDom"></span>
				</td>
			</tr>
			<tr>
				<td class="label">
					文件类型：
				</td>
				<td>
					${uploadFile.fileType}
				</td>
			</tr>
			<tr>
				<td class="label">
					文件路径：
				</td>
				<td>
				${uploadFile.filePath}
				</td>
			</tr>
			<tr>
				<td class="label">
					下载次数：
				</td>
				<td>
				${uploadFile.downloadNum}
				</td>
			</tr>
			<tr>
				<td class="label">
					备注：
				</td>
				<td>
				<textarea class="easyui-textarea validate[maxSize[128]]" promptPosition="top" id="remark" name="remark" style="width:400px">${uploadFile.remark}</textarea>
				</td>
			</tr>
			<tr>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<input id="saveBtn" type="button" class="easyui-button" value="保存" onclick="doSave()">
					<input type="button" class="easyui-button" value="关闭" onclick="javascript:parentDialog.close()">
				</td>
			</tr>
		</table>
	</form>
</ui:Panel>
</body>
</html>