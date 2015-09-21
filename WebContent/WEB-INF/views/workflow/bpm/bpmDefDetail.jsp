<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="ui" uri="/tags/ui"%>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
</head>
<body>
	<ui:Panel fit="true" style="padding:5px">
		<table class="tableView">
			<tr>
				<td class="label" style="width:150px">
					流程分类：
				</td>
				<td>
					${bpmCatalog.catalogName}
				</td>
			</tr>
			<tr>
				<td class="label" style="width:150px">
					流程标题：
				</td>
				<td >
					${bpmDef.subject}
				</td>
			</tr>
			<tr>
				<td class="label" style="width:150px">
					流程定义Key：
				</td>
				<td >
					${bpmDef.defKey}
				</td>
			</tr>
			<tr>
				<td class="label" style="width:150px">
					流程实例标题规则：
				</td>
				<td >
					${bpmDef.instNameRule}
				</td>
			</tr>
			<tr>
				<td class="label" style="width:150px">
					流程描述：
				</td>
				<td >
					${bpmDef.description}
				</td>
			</tr>
			<tr>
				<td class="label" style="width:150px">
					创建时间：
				</td>
				<td >
					<fmt:formatDate value='${bpmDef.createTime}' pattern="yyyy-MM-dd hh:mm:ss"/>
				</td>
			</tr>
			<tr>
				<td class="label" style="width:150px">
					创建人：
				</td>
				<td>
					${creator.yhmc}
				</td>
			</tr>
			<tr>
				<td class="label" style="width:150px">
					修改时间：
				</td>
				<td >
					<fmt:formatDate value='${bpmDef.modifyTime}' pattern="yyyy-MM-dd hh:mm:ss"/>
				</td>
			</tr>
			<tr>
				<td class="label" style="width:150px">
					修改人：
				</td>
				<td>
					${modifyUser.yhmc}
				</td>
			</tr>
			<tr>
				<td class="label" style="width:150px">
					流程状态：
				</td>
				<td>
					<c:if test="${bpmDef.usable=='0'}">
						<span style="color:red">
							禁用
						</span>
					</c:if>
					<c:if test="${bpmDef.usable=='1'}">
						<span style="color:blue">
							启用
						</span>
					</c:if>
				</td>
			</tr>
			<tr>
				<td class="label" style="width:150px">
					版本号：
				</td>
				<td>
					${bpmDef.version}
				</td>
			</tr>
		</table>
	</ui:Panel>
</body>
</html>