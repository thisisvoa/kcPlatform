<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
<!--
	function beforeClick(type){<c:if test="${not empty buttonList}">
			switch(type){<c:forEach items="${buttonList}" var="btn"><c:if test="${not empty btn.beforeHandler}">
				case ${btn.type}:
					${btn.beforeHandler}
				break;</c:if></c:forEach>
			}</c:if>
	}
	
	function afterClick(type){<c:if test="${not empty buttonList}">
		switch(type){<c:forEach items="${buttonList}" var="btn"><c:if test="${not empty btn.afterHandler}">
			case ${btn.type}:
				${btn.afterHandler}
				break;</c:if></c:forEach>
		}</c:if>
	}
//-->
</script>
<div id="toolbar" class="easyui-toolbar">
	<a id="startBtn" href="#" iconCls="icon-run" onclick="">反馈</a>
	<a id="diagramBtn" href="#" iconCls="icon-flowDesign" onclick="showDiagram()">流程图</a>
	<a id="hisBtn" href="#" iconCls="icon-search" onclick="">审批历史</a>
	<a id="communicationBtn" href="#" iconCls="icon-message" onclick="">沟通</a>
</div>