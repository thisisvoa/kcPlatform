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
<script type="text/javascript">
	function onCodeTreeClick(event, treeId, treeNode){
		if(treeNode.id!="0"){
			parent.frames["rightList"].document.location.href = "${ctx}/codeCtl/toCodeList.html?codeId="
				+ treeNode.id;
		}else{
			parent.frames["rightList"].document.location.href = "${ctx}/codeCtl/toCodeList.html";	
		}
		
	}
	// 查看代码详细信息
	function view() {
		if(checkSelectedTreeId()){
			var treeObj = $("#codeTree").tree("getZTreeObj");
			var selectedNodes = treeObj.getSelectedNodes();
			var selctedNode = selectedNodes[0];
			var id = selctedNode.id;
			if(id=="0") return;
			var topWin = FrameHelper.getTop();
			topWin.Dialog.open({
				URL : "${ctx}/codeCtl/toViewCode.html?id=" + id,
				Width:700,
				Height : 500,
				Modal : false,
				Title : "代码详细信息"
			});	
		};
		
	}

	//编辑代码信息
	function edit(id) {
		if(checkSelectedTreeId()){
			var treeObj = $("#codeTree").tree("getZTreeObj");
			var selectedNodes = treeObj.getSelectedNodes();
			var selctedNode = selectedNodes[0];
			var id = selctedNode.id;
			if(id=="0") return;
			var topWin = FrameHelper.getTop();
			var diag = new topWin.Dialog();
			diag.URL = "${ctx}/codeCtl/toEditCode.html?isDialog=1&id=" + id;
			diag.Width = 600;
			diag.Height = 300;
			diag.Title = "修改代码";
			diag.RefreshHandler = function(){
				var sfdmx = selctedNode.sfdmx;
				if(sfdmx=="1"){
					treeObj.reAsyncChildNodes(selctedNode.getParentNode(), "refresh");
					parent.frames["rightList"].document.location.href = "${ctx}/codeCtl/toCodeList.html?codeId="
						+ selctedNode.id;
				}else if(sfdmx=="0"){
					treeObj.reAsyncChildNodes(null, "refresh");
				}
			};
			diag.show();
		}
		
	}
	function checkSelectedTreeId(){
		var treeObj = $("#codeTree").tree("getZTreeObj");
		var selectedNodes = treeObj.getSelectedNodes();
		if(selectedNodes.length == 0){
			MessageUtil.alert("请选择要操作的代码！");
			return false;
		}
		return true;
	}
	</script>
</head>
<body>
	<ui:Panel fit="true" title="代码信息管理">
		<ui:LayoutContainer fit="true">
  			<ui:Layout region="north" style="height:30px;">
				<table width="100%">
					<tr>
						<td align="right">
							<input type="button" class="easyui-button" onclick="view()" value="查看" />
							<input type="button" class="easyui-button" onclick="edit()" value="修改" />
						</td>
					</tr>
				</table>
  			</ui:Layout>
  			<ui:Layout region="center" border="false">
  				<ul id="codeTree" class="easyui-tree" asyncEnable="true"  url="${ctx}/codeCtl/asyncCodeTree.html" 
  					simpleDataEnable="true" autoParam="['id']"
  				onClick="onCodeTreeClick"></ul>
  			</ui:Layout>
  		</ui:LayoutContainer>
	</ui:Panel>
</body>
</html>