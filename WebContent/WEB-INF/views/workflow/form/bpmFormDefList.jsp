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
<title></title>
<script type="text/javascript">
	var topWin = FrameHelper.getTop();
	function doQuery(){
		var postData = {};
		postData.subject = $("#subject").val();
		$("#bpmFormDefList").grid("setGridParam", {postData:postData});
		$("#bpmFormDefList").trigger("reloadGrid");
	}
	
	function addBpmFormDef(){
		var height = $(topWin.document.body).height();
		var width = $(topWin.document.body).width();
		var diag = topWin.Dialog.open({
			Title : "新增表单",
			URL : '${ctx}/workflow/formDef/toSet.html',
			Width : 500,
			Height : 320,
			RefreshHandler : function(){
				var formDef = diag.getData();
				var tmplDiag = topWin.Dialog.open({
					Title : "选择表单模板",
					URL : '${ctx}/workflow/formDef/selectTmpl.html?tableId='+formDef.tableId,
					Width : 500,
					Height : 320,
					RefreshHandler : function(){
						var tmpl = tmplDiag.getData();
						var mainTmplId = tmpl.mainTmplId;
						var addDiag = new topWin.Dialog();
						addDiag.Title = "编辑表单内容";
						addDiag.URL = "${ctx}/workflow/formDef/toAdd.html?tableId="+formDef.tableId
										+"&tableDesc="+encodeURI(encodeURI(formDef.tableDesc))
										+"&subject="+encodeURI(encodeURI(formDef.subject))
										+"&formKey="+encodeURI(encodeURI(formDef.formKey))
										+"&catalogId="+formDef.catalogId
										+"&formDesc="+encodeURI(encodeURI(formDef.formDesc))
										+"&mainTmplId="+mainTmplId;
						addDiag.Width = width;
						addDiag.Height = height;
						addDiag.RefreshHandler = function(){
							$("#bpmFormDefList").trigger("reloadGrid");
						};
						setTimeout(function(){
							addDiag.show();
						},10);
						
					}
				});
			}});
	}
	
	function editBpmFormDef(id){
		topWin.Dialog.open({
			Title : "修改表单",
			URL : '${ctx}/workflow/formDef/toUpdate.html?id='+id,
			Width : 600,
			Height : 400,
			RefreshHandler : function(){
				$("#bpmFormDefList").trigger("reloadGrid");
			}});
	}
	
	function viewBpmFormDef(id){
		topWin.Dialog.open({
			Title : "查看表单",
			URL : '${ctx}/workflow/formDef/toView.html?id='+id,
			Width : 600,
			Height : 400
			});
	}
	
	function delBpmFormDef(){
		var selectedIds = $("#bpmFormDefList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请选择要删除的记录！");
			return;
		}
		var ids = selectedIds.join(",");
		MessageUtil.confirm("确定删除选择的记录吗？",function(){
			$.postc('${ctx}/workflow/formDef/delete.html', {ids:ids}, function(data){
				MessageUtil.show("删除记录成功！");
				$("#bpmFormDefList").trigger("reloadGrid");
			});
		});
	}
	

	function onGridComplete(){
		using("button", function(){
			$(".easyui-button").button();
		});
	}
	
	function operaFormatter(cellVal,options,rowd){
		var html = "";
		html += "<input type='button' class='easyui-button' style='margin:1px' onclick=\"javascript:viewBpmFormDef('"+rowd.formDefId+"')\" value='查看' />";
		html += "<input type='button' class='easyui-button' style='margin:1px' onclick=\"javascript:editBpmFormDef('"+rowd.formDefId+"');\" value='修改' />";
		return html;
	}
</script>
</head>
<body style="padding:1px">
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:75px;">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="query" href="#" iconCls="icon-search" onclick="doQuery()">查询</a>
			<a id="add" href="#" iconCls="icon-add" onclick="addBpmFormDef()">新增</a>
			<a id="remove" href="#" iconCls="icon-remove" onclick="deleteProcess()">删除</a>
		</div>
		<form id="queryForm">
			<table class="formTable">
				<tr>
					<td class="label" style="width:100px">
						表单标题：
					</td>
					<td>
						<input type="text" class="easyui-textinput" id="subject" name="subject" />
					</td>
				</tr>
				<tr>
				</tr>
			</table>
		</form>
	</ui:Layout>
	<ui:Layout region="center" border="false" style="padding-top:1px">
		<ui:Grid id="bpmFormDefList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
				url="${ctx}/bpm/formDef/list.html?catalogId=${catalogId}" multiselect="true" rownumbers="false"
				rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
				<ui:Column name="formDefId" key="true" hidden="true"></ui:Column>
				<ui:Column header="表单分类" name="catalogId" width="90"></ui:Column>
				<ui:Column header="表单key" name="formKey" width="90"></ui:Column>
				<ui:Column header="表单标题" name="subject" width="90"></ui:Column>
				<ui:Column header="版本号" name="versionNo" width="90"></ui:Column>
				<ui:Column header="操作" formatter="operaFormatter" width="120" fixed="true"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>