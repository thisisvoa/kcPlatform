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
<c:choose>
	<c:when test="${empty buttonList}">
		<a id="startBtn" href="#" iconCls="icon-run" onclick="startWorkFlow()">提交</a>
		<c:choose>
			<c:when test="${isDraft}">
				<a id="saveDraftBtn" href="#" iconCls="icon-save" class="isDraft" onclick="saveAsDraft()">保存草稿</a>
			</c:when>
			<c:otherwise>
				<a id="saveDraftBtn" href="#" iconCls="icon-save" onclick="saveAsDraft()">保存表单</a>
			</c:otherwise>
		</c:choose>
		<a id="diagramBtn" href="#" iconCls="icon-flowDesign" onclick="showDiagram()">流程图</a>
	</c:when>
	<c:otherwise>
		<c:forEach items="${buttonList}" var="btn" varStatus="status">
			<c:choose>
				<c:when test="${btn.type==1 }">
					<a id="startBtn" href="#" iconCls="icon-run" onclick="startWorkFlow()">${btn.btnName}</a>
				</c:when>
				<c:when test="${btn.type==2}">
					<c:choose>
						<c:when test="${isDraft}">
							<a id="saveDraftBtn" href="#" iconCls="icon-save" class="isDraft" onclick="saveAsDraft()">保存草稿</a>
						</c:when>
						<c:otherwise>
							<a id="saveDraftBtn" href="#" iconCls="icon-save" onclick="saveAsDraft()">保存表单</a>
						</c:otherwise>
					</c:choose>
				</c:when>
				
				<c:when test="${btn.type==7}">
					<a id="diagramBtn" href="#" iconCls="icon-flowDesign" onclick="showDiagram()">${btn.btnName}</a>
				</c:when>
			</c:choose>
		</c:forEach>
	</c:otherwise>
</c:choose>
</div>