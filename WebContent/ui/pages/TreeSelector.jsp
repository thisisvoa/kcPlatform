<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="/tags/ui" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<%
	String showSelected = request.getParameter("showSelected");
	pageContext.setAttribute("showSelected",showSelected);
%>
<script type="text/javascript">
	var selectedList;
	var options;
	function init(opts,selList){
		options = opts;
		selectedList = selList;
		initGrid();
		initTree();
		function initTree(){
			var setting = {};
			setting.data = {
				simpleData : {
					enable : options.simpleDataEnable,
					idKey : options.idKey,
					pIdKey : options.pIdKey,
					rootPId : options.rootPId
				},
				key : {
					name : options.nameKey
				}
			};
			setting.check = {
					enable : true,
					chkboxType : {"Y": "", "N": ""},
					chkStyle : options.multiple?"checkbox":"radio",
					radioType : "all"
			};
			setting.async = {
					enable : true,
					autoParam : options.autoParam,
					dataFilter : dataFilter,
					url : options.url
			};
			setting.callback = {
				onCheck:onTreeCheck,
				onAsyncSuccess:onAsyncSuccess
			};
			using("tree",function(){
				$("#selectorTree").data("tree", {options:{"setting":setting}}).tree();	
			});
		}
		
		function initGrid(){
			if(opts.showSelected){
				var cols = opts.cols;
				var colModel = [];
				var colNames = [];
				colModel.push({name:options.idKey,key:true,hidden:true});
				colNames.push('');
				for(var i=0;i<cols.length;i++){
					var arr = cols[i].split(":");
					var name = arr[0];
					var header = arr[1];
					colModel.push({name:name,align:'center',sortable:false,width:100});
					colNames.push(header);
				}
				colModel.push({fixed:true,formatter:operationFormatter,align:'center',width:70,sortable:false});
				colNames.push('');
				using("grid",function(){
					$('#selectedGrid').data("grid",{options:{colModel:colModel,colNames:colNames,fit:true}}).grid();
					if(selectedList!=null){
						for(var i=0;i<selectedList.length;i++){
							$("#selectedGrid").grid("addRowData", selectedList[i][options.idKey],selectedList[i]);
						}
					}
				});
			}
		}
	}
	
	function onAsyncSuccess(event, treeId, treeNode, msg) {
		var selectedList = $("#selectedGrid").grid("getRowData");
		var treeObj = $("#selectorTree").tree("getZTreeObj");
		for(var i=0;i<selectedList.length;i++){
			var id = selectedList[i][options.idKey];
			var node = treeObj.getNodeByParam(options.idKey, id);
			if(node!=null){
				treeObj.checkNode(node,true);
			}
		}
	}
	
	function dataFilter(event, parentNode, childNodes){
		if(options.icon!=""){
			for(var i=0;i<childNodes.length;i++){
				childNodes[i].icon = options.icon;
			}
		}
		if(options.dataFilter){
			return options.dataFilter(event, parentNode, childNodes);options.dataFilter	
		}else{
			return childNodes;
		}
		
	}
	
	function onTreeCheck(event, treeId, treeNode) {
		var checked = treeNode.checked;
		if(checked){
			if(!options.multiple){
				$("#selectedGrid").grid("clearGridData");
			}
			if(options.showSelected){
				$("#selectedGrid").grid("addRowData", treeNode[options.idKey],treeNode);
			}
		}else{
			if(options.showSelected){
				$("#selectedGrid").grid("delRowData", treeNode[options.idKey]);	
			}
		}
	}
	
	function unCheck(id){
		var treeObj = $("#selectorTree").tree("getZTreeObj");
		var node = treeObj.getNodeByParam(options.idKey, id);
		if(node!=null){
			treeObj.checkNode(node,false);	
		}
		$("#selectedGrid").grid("delRowData", id);
		return false;
	}
	
	function save(){
		var selectedList = $("#selectedGrid").grid("getRowData");
		parentDialog.setData(selectedList);
		parentDialog.markUpdated();
		parentDialog.close();
	}
	
	function operationFormatter(cellVal,opts,rowd){
		var html = "<a href=\"javascript:void(0)\" onclick=\"unCheck('"+rowd[options.idKey]+"')\" ><img src='"+easyloader.base+"/css/icon/images/delete.gif' style='border:none'></img></a>";
		return html;
	}
	
</script>
</head>
<body style="padding:1px">
<ui:LayoutContainer fit="true">
	<ui:Layout region="center"  border="false">
		<fieldset style="width:98%;height:95%;padding:1px;overflow:auto;border:1px solid #a6c9e2"> 
			<legend>请选择</legend>
			<div style="width:99%;height:90%;overflow:auto;border:1px solid #a6c9e2">
				<ul id="selectorTree"></ul>
  			</div>
		</fieldset>
	</ui:Layout>
	
	<c:if test="${showSelected=='true'}">
	<ui:Layout region="east" style="width:300px; padding:1px;" border="false">
		<fieldset style="width:98%;height:95%;padding:1px;border:1px solid #a6c9e2"> 
			<legend>已选列表</legend>
			<div style="width:99%;height:90%;">
				<table id="selectedGrid">
				</table>
			</div>
		</fieldset>
	</ui:Layout>
	</c:if>
	<ui:Layout region="south" border="false" style="height:30px;text-align:center">
		<input type="button" id="saveBtn" class="easyui-button" style="margin-top:3px" value="确定" onclick="save()" />
		<input type="button" class="easyui-button" style="margin-top:3px" value="关闭" onclick="javascript:parentDialog.close()" />
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>