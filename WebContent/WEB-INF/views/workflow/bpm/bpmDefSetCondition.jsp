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
<title>设置分支条件</title>
<script type="text/javascript">
var slectTextarea;
$parsed(function() {
	$("a[name='signResult']").click(function() {
		addToTextarea($(this).attr("result"));
	});
});
//textarea切换获取焦点
function toggleFocus(e){
	slectTextarea=e;
};
//将条件表达式追加到脚本输入框内
function addToTextarea(str){
	if(slectTextarea==null){
		MessageUtil.alert('请先选中脚本输入框');
		return;
	}
	
	$(slectTextarea).html($(slectTextarea).html()+str);
};
function doSave(){
	var data = $("#scriptForm").serialize();
	$.postc("${ctx}/workflow/bpmDef/saveCondition.html", data, function(data){
		MessageUtil.show("分支条件设置成功!");
		parentDialog.markUpdated();
	});
}
</script>
</head>
<body>
<ui:LayoutContainer fit="true">
<ui:Layout region="north" style="height:33px;" border="false">
	<div id="toolbar" class="easyui-toolbar">
		<a id="saveBtn" href="#" iconCls="icon-save" onclick="doSave()">保存</a>
		<a id="close" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
	</div>
</ui:Layout>
<ui:Layout region="center" border="false">
	<form id="scriptForm" class="easyui-validate">
		<input type="hidden" name="defId" id="defId" value="${defId}"/>
		<input type="hidden" name="nodeId" id="nodeId" value="${nodeId}"/>
		<input type="hidden" name="deployId" id="deployId" value="${deployId}"/>
		<table class="tableView" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td class="label" style="width:100px">
					条件表达式：
				</td>
				<td>
					<c:forEach items="${incomeNodes}" var="inNode">
						<div style="padding: 4px;">
							<c:choose>
								<c:when test="${inNode.isMultiple==true}">
									<a href="#" name="signResult" style="color:blue;"
										result='signResult_${inNode.nodeId}=="pass"'>[${inNode.nodeName}]投票通过</a>
												&nbsp;
									<a href="#" name="signResult" style="color:blue;"
										result='signResult_${inNode.nodeId}=="refuse"'>[${inNode.nodeName}]投票不通过</a>
								</c:when>
								<c:otherwise>
									<a href="#" name="signResult" style="color:blue;"
										result="approvalStatus_${inNode.nodeId}==1">[${inNode.nodeName}]-通过</a>
												&nbsp;
									<a href="#" name="signResult" style="color:blue;"
										result="approvalStatus_${inNode.nodeId}==2">[${inNode.nodeName}]-反对</a>
								</c:otherwise>
							</c:choose>
							<br>
							1.先选中下方的脚本输入框，然后再插入条件表达式。
							<br>
							2.表达式中不能有分号或return语句。
						</div>
					</c:forEach>
				</td>
			</tr>
			<c:forEach items="${outcomeNodes}" var="outNode">
				<tr>
					<td class="label" style="width:100px">
						<input type="hidden" name="task" value="${outNode.nodeId}" /> ${outNode.nodeName }：
					</td>
					<td>
						<textarea name="condition" style="width:500px;height:100px;" class="easyui-textarea" onfocus="toggleFocus(this)">${outNode.condition}</textarea>
					</td>
				</tr>
			</c:forEach>
		</table>
	</form>
</ui:Layout>
</ui:LayoutContainer>
</body>
</html>