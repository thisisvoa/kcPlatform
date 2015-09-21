
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
	$parsed(function(){
	});
</script>
<body>
<ui:Panel fit="true">
<table class="tableView" style="position:relative">
	<tr>
		<td colspan="4" align="center">
			<input type="button" class="easyui-button" value="关闭" onclick="javascript:parentDialog.close()">
		</td>
	</tr>
	<tr>
		<td class="label" >
			归档名称：
		</td>
		<td>
			${gdpz.gdmc}
		</td>
		<td class="label" >
			归档表：
		</td>
		<td>
			${gdpz.gdbmc}
		</td>
	</tr>
	<tr>
		<td class="label" >
			时间戳字段名：
		</td>
		<td>
			${gdpz.sjclm}
		</td>
		<td class="label" >
			时间戳字段类型： 
		</td>
		<td>
			<c:if test="${gdpz.sjclSjlx eq '1'}">
				日期
			</c:if>
			<c:if test="${gdpz.sjclSjlx eq '2'}">
				字符串&nbsp;&nbsp;&nbsp;&nbsp;
				日期格式：${gdpz.sjclGs}
			</c:if>
		</td>
	</tr>
	<tr>
		<td class="label" >
			归档周期：
		</td>
		<td>
			${gdpz.gdzq}&nbsp;&nbsp;&nbsp;
			<c:if test="${gdpz.gdzqDw eq '1'}">日</c:if>
			<c:if test="${gdpz.gdzqDw eq '2'}">月</c:if>
			<c:if test="${gdpz.gdzqDw eq '3'}">年</c:if>
		</td>
		<td class="label" >
			归档延迟天数：
		</td>
		<td>
			${gdpz.ycsj}
		</td>
	</tr>
	<tr>
		<td class="label" >
			归档执行时间：
		</td>
		<td colspan="3">
			${gdpz.zxsj}
		</td>		
	</tr>
	<tr>
		<td class="label" >
			备注：
		</td>
		<td colspan="3">
			<textarea class="easyui-textarea" id="bz" name="bz" style="width:600px;height:100px">${gdpz.bz}</textarea>
		</td>
	</tr>
</table>
</ui:Panel>
</body>
</html>