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
<title>用户调动</title>
<!--多功能树结构end-->
<script type="text/javascript">
	var setting = {
			view: {
				fontCss: function(treeId, node){
					if(node.enable==false){
						return {color:"#ccc"};	
					}
				}
			}
	};
	
	function beforeCheck(treeId, treeNode) {
		if(treeNode.enable==false){
			return false;	
		}
		return true;
	}

	function dataFilter(event, parentNode, childNodes){
		if(!childNodes) return null;
		for(var i=0;i<childNodes.length;i++){
			childNodes[i].icon = "${ctx}/ui/css/icon/images/user_group.gif";
		}
		return childNodes;
	}
	
	function confirmTransfer() {
		var treeObj = $("#orgTree").tree("getZTreeObj");
		var selectedNodes = treeObj.getCheckedNodes(true);
		if(selectedNodes.length<=0){
			MessageUtil.alert("请选择要调动的部门！");
		} else {
			var orgId = selectedNodes[0].id;
			MessageUtil.confirm("确定要将选择的用户调动至["+selectedNodes[0].name+"]吗?",function(){
				var userIds = $("#userIds").val();
				var params = {
					ids:userIds,
					orgId:orgId
				};
				var url = "${ctx}/userCtl/transfer.html";
				$.postc(url,params,function(data){
					MessageUtil.show('人员调动成功');
					parentDialog.markUpdated();
				});
			});
		}
	}
</script>
</head>
<body>
	<ui:LayoutContainer fit="true">
		<ui:Layout region="center">
			<ul id="orgTree" class="easyui-tree" asyncEnable="true"  url="${ctx}/orgCtl/asynOrgTree.html" 
	 				rootPId="0" simpleDataEnable="true" autoParam="['id']" chkEnable="true" chkStyle="radio" radioType="all"
	 				dataFilter="dataFilter" setting="setting" beforeCheck="beforeCheck"></ul>
		</ui:Layout>
		<ui:Layout region="north" style="text-align:center;height:30px;background-color:#F8F8F8" border="false">
			<input id="userIds" type="hidden" value="${userIds}"/>
			<input type="button" class="easyui-button" style="margin-top:3px" value="调动" onclick="confirmTransfer()">
			<input type="button" class="easyui-button" style="margin-top:3px" value="关闭" onclick="javascript:parentDialog.close();">
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>
