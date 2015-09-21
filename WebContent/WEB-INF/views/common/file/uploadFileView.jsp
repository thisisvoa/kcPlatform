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
</script>
</head>
<body>
<ui:Panel fit="true">
<table class="tableView">
	<tr>
		<td class="label" style="width:100px">
			上传文件名：
		</td>
		<td>
			${uploadFile.fileName}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			存储文件名：
		</td>
		<td>
			${uploadFile.diskFileName}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			文件大小：
		</td>
		<td>
			
			<span id="fileSizeDom"></span>
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			文件类型：
		</td>
		<td>
			${uploadFile.fileType}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			文件路径：
		</td>
		<td>
			${uploadFile.filePath}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			下载次数：
		</td>
		<td>
			${uploadFile.downloadNum}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			上传时间：
		</td>
		<td>
			<fmt:formatDate value='${uploadFile.createTime}' pattern="yyyy-MM-dd hh:mm:ss"/>
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			上传用户：
		</td>
		<td>
			${uploadUser.yhmc}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			修改时间：
		</td>
		<td>
			<fmt:formatDate value='${uploadFile.modifyTime}' pattern="yyyy-MM-dd hh:mm:ss"/>
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			修改用户：
		</td>
		<td>
			${modifyUser.yhmc}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			备注：
		</td>
		<td>
			${uploadFile.remark}
		</td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<input type="button" class="easyui-button" value="关闭" onclick="javascript:parentDialog.close()">
		</td>
	</tr>
</table>
</ui:Panel>
</body>
</html>