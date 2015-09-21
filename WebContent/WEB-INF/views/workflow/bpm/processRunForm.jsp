<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="/tags/ui"%>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<title>流程实例业务表单明细</title>
<script type="text/javascript">
var isExtForm=${isExtForm};
var isFormEmpty=${isFormEmpty};
$(function(){
	//表单不为空的情况。
	if(isFormEmpty==false){
		if(isExtForm){
			var formUrl = $('#divExternalForm').attr("formUrl");
			$.ajax({
				url : formUrl,
				cache : false,
				success : function(html) {
					$("#divExternalForm").html(html);
					$.parser.parse($("#divExternalForm"));
				}
			});
		}else{
		}
	}
});
</script>
</head>
<body>
<ui:Panel fit="true" title="流程实例-【<i>${processRun.subject}</i>】业务表单。" border="false" style="padding:1px">
	<c:choose>
		<c:when test="${!isFormEmpty}">
			<c:choose>
				<c:when test="${isExtForm}">
					<div id="divExternalForm" formUrl="${form}"></div>
				</c:when>
				<c:otherwise>
					${form}
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			未设置流程实例业务表单
		</c:otherwise>
	</c:choose>
</ui:Panel>
</body>
</html>