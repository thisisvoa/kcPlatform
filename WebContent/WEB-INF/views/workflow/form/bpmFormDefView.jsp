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
<body>
<ui:Panel fit="true">
<table class="tableView">
	<tr>
		<td class="label" style="width:100px">
			ID：
		</td>
		<td>
			${bpmFormDef.formDefId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			表单分类：
		</td>
		<td>
			${bpmFormDef.catalogId}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			表单key：
		</td>
		<td>
			${bpmFormDef.formKey}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			表单标题：
		</td>
		<td>
			${bpmFormDef.subject}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			描述：
		</td>
		<td>
			${bpmFormDef.formDesc}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			定义html：
		</td>
		<td>
			${bpmFormDef.html}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			版本号：
		</td>
		<td>
			${bpmFormDef.versionNo}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			是否默认版本：
		</td>
		<td>
			${bpmFormDef.isDefault}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			是否发布：
		</td>
		<td>
			${bpmFormDef.isPublished}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			发布用户：
		</td>
		<td>
			${bpmFormDef.publishedBy}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			发布时间：
		</td>
		<td>
			<fmt:formatDate value='${bpmFormDef.publishTime}' pattern="yyyy-MM-dd"/>
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			创建用户：
		</td>
		<td>
			${bpmFormDef.createUser}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			创建时间：
		</td>
		<td>
			<fmt:formatDate value='${bpmFormDef.createTime}' pattern="yyyy-MM-dd"/>
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			修改用户：
		</td>
		<td>
			${bpmFormDef.modifyUser}
		</td>
	</tr>
	<tr>
		<td class="label" style="width:100px">
			修改时间：
		</td>
		<td>
			<fmt:formatDate value='${bpmFormDef.modifyTime}' pattern="yyyy-MM-dd"/>
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