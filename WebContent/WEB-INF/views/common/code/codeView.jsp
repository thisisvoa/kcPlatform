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
<title>代码详细信息</title>
<script type="text/javascript">
function timeFormatter(cellVal,options,rowd){
	if(cellVal!=null){
		//var date = new Date(cellVal);
		return DateFormat.format(DateFormat.parse(cellVal, 'yyyyMMddhh24miss'),"yyyy-MM-dd hh:mm:ss");
		//return DateFormat.format(date,"yyyy-MM-dd hh:mm:ss.S");
	}
	return "";
}
</script>
</head>
<body>
	<c:if test="${codeInfo.sfdmx == '0'}">
		<ui:LayoutContainer fit="true">
			<ui:Layout region="north" style="height:200px;text-align:center;" border="false">
				<input type="button" class="easyui-button" style="margin-top:2px" value="关 闭 " onclick="javascript:parentDialog.close();"/>
				<table class="tableView" style="margin-top:10px;">
					<tr>
						<td  class="label">代码名称：</td>
						<td style="width:250px;"  align="left">${codeInfo.dmmc}</td>
						<td class="label">代码简称：</td>
						<td style="width:250px;"  align="left">${codeInfo.dmjc}</td>
					</tr>
					<tr>
						<td class="label">使用标识：</td>
						<td  align="left">
							<c:if test="${codeInfo.sybz == '1'}">启用</c:if>
							
							<c:if test="${codeInfo.sybz == '0'}">禁用</c:if> 
						</td>
						<td class="label">排序号：</td>
						<td align="left">${codeInfo.pxh}</td>
					</tr>
					<tr>
						<td class="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
						<td colspan="3" align="left"><textarea class="easyui-textarea" style="width:550px;height:80px;">${codeInfo.bz}</textarea></td>
					</tr>
				</table>
			</ui:Layout>
			<ui:Layout region="center" border="false">
				<ui:Grid id="logList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
					url="${ctx}/codeCtl/codeOptionList.html?codeSimpleName=${codeInfo.dmjc}"
					rowNum="20" rowList="[10,20,50]">
					<ui:Column name="zjid" key="true" hidden="true"></ui:Column>
					<ui:Column header="编号" name="dmx_bh"></ui:Column>
					<ui:Column header="代码项名称" name="dmx_mc"></ui:Column>
					<ui:Column header="更新时间" name="jlxgsj" datefmt="yyyy-MM-dd hh:mm:ss"></ui:Column>
				</ui:Grid>
			</ui:Layout>
		</ui:LayoutContainer>
	</c:if>
	<c:if test="${codeInfo.sfdmx == '1'}">
			<table class="tableView">	
					<tr>
						<td width="35%"  class="label">代码名称：</td>
						<td width="65%">${codeInfo.dmmc}</td>			
					</tr>
					<tr>
						<td  class="label">代码项编号：</td>
						<td>${codeInfo.dmx_bh}</td>			
					</tr>
					<tr>
						<td  class="label">代码项名称：</td>
						<td>${codeInfo.dmx_mc}</td>			
					</tr>
					<tr>
						<td  class="label">使用标识：</td>
						<td>
							<c:if test="${codeInfo.sybz == '1'}">启用</c:if>
							<c:if test="${codeInfo.sybz == '0'}">禁用</c:if>
						</td>
					</tr>
					<tr>
						<td  class="label">排序号：</td>
						<td>${codeInfo.pxh}</td>			
					</tr>
					<tr>
						<td  class="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
						<td>${codeInfo.bz}</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<input type="button" class="easyui-button" value="关 闭 " onclick="javascript:parentDialog.close();"/>
						</td>
					</tr>
				</table>			
		</c:if>	
</body>
</html>
