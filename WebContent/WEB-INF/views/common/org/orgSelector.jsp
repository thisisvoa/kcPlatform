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
	//当前选中的节点
	var org =  {orgId:"${org.zjid}", orgName:"${org.dwmc}"};
	var filterId = "${filterId}";
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
	function onTreeCheck(event, treeId, treeNode) {
		var checked = treeNode.checked;
		if(checked){
			org = {orgId:treeNode.id, orgName:treeNode.name};
			$("input[name='orgSelect']").removeAttr("checked"); 
			$("#ra_"+treeNode.id).attr("checked", "checked"); 
			$("#dwmc").html(treeNode.name);
		}else{
			$("input[name='orgSelect']").removeAttr("checked"); 
			org ={};
			$("#dwmc").html("");
		}
	}
	
	function onAsyncSuccess(event, treeId, treeNode, msg) {
		if(org.orgId!=null){
			var treeObj = $("#orgTree").tree("getZTreeObj");
			var node = treeObj.getNodeByParam("id", org.orgId);
			if(node!=null){
				treeObj.checkNode(node,true);
			}
		}
	}
	
	function dataFilter(event, parentNode, childNodes){
		if(!childNodes) return null;
		var nodes = [];
		for(var i=0;i<childNodes.length;i++){
			if(childNodes[i].id!=filterId){
				childNodes[i].icon = "${ctx}/ui/css/icon/images/user_group.gif";
				nodes.push(childNodes[i]);
			}
		}
		return nodes;
	}
	
	function gridCheck(orgId){
		var d = $("#orgList").grid("getRowData", orgId);
		org = {orgId:d.ZJID, orgName:d.DWMC};
		$("#dwmc").html(d.DWMC);
		var treeObj = $("#orgTree").tree("getZTreeObj");
		var node = treeObj.getNodeByParam("id", orgId);
		if(node!=null){
			treeObj.checkNode(node,true);
		}else{
			treeObj.checkAllNodes(false);
		}
	}
	function radioFormat(cellVal,options,rowd){
		if(org.orgId==rowd.ZJID){
			return "<input id=\"ra_"+rowd.ZJID+"\" type=\"radio\" name=\"orgSelect\" value=\""+rowd.ZJID+"\" checked=\"checked\" onclick=\"gridCheck('"+rowd.ZJID+"')\">";
		}else{
			return "<input id=\"ra_"+rowd.ZJID+"\" type=\"radio\" name=\"orgSelect\" value=\""+rowd.ZJID+"\" onclick=\"gridCheck('"+rowd.ZJID+"')\">";
		}
	}
	
	function queryOrgList() {
		$("#orgList").grid("setGridParam", {postData:{orgNum:$("#orgNum").val(), orgName:$("#orgName").val()}});
		$("#orgList").trigger("reloadGrid");
	}
	
	function queryEnter(e){
		if(e == 13){
			$("#queryBtn").click();
		}
	}
	
	function save(){
		parentDialog.setData(org);
		parentDialog.markUpdated();
		parentDialog.close();
	}
	
	/**
	 *第一次加载时候展开第一层节点
	 */
	function onNodeCreated(event, treeId, treeNode){
		if(treeNode.pId=="0"){
			var treeObj = $("#orgTree").tree("getZTreeObj");
			if(treeNode.children==null){
				treeObj.reAsyncChildNodes(treeNode, "refresh");	
			}else{
			}
		}
	}
</script>
</head>
<body style="padding:1px;background-color:#F8F8F8">
	<ui:LayoutContainer fit="true">
		<ui:Layout region="north" style="height:30px;padding-top:5px;padding-left:2px" border="false">
			<div class="icon-qbzc" style="width:16px;height:16px;float:left"></div>已选单位:<span id="dwmc">${org.dwmc}</span>
		</ui:Layout>
		<ui:Layout region="center">
			<ui:TabNavigator fit="true" border="false">
				<ui:Tab id="tab1" title="树形选择">
						<ul id="orgTree" class="easyui-tree" asyncEnable="true"  url="${ctx}/orgCtl/asynOrgTree.html" 
			  				rootPId="0" simpleDataEnable="true" autoParam="['id']" 
			  				chkEnable="true" chkboxType="{'Y': '', 'N': ''}" chkStyle="radio" radioType="all"
			  				dataFilter="dataFilter" onCheck="onTreeCheck" setting = "setting" beforeCheck="beforeCheck"
			  				onAsyncSuccess="onAsyncSuccess" onNodeCreated="onNodeCreated"></ul>
				</ui:Tab>
				<ui:Tab id="tab2" title="列表选择">
					<ui:LayoutContainer fit="true">
						<ui:Layout region="north" style="height:40px">
							<table class="formTable">
								<tr>
									<td class="label" style="width:100px">
										单位编码：
									</td>
									<td>
										<input type="text" id="orgNum" class="easyui-textinput" watermark="支持模糊查询" title="按下回车键可触发查询" onkeydown="queryEnter(event.keyCode||event.which)"/>
									</td>
									<td class="label" style="width:100px">
										单位名称：
									</td>
									<td>
										<input type="text" id="orgName" class="easyui-textinput" watermark="支持模糊查询" style="width: 200px;" title="按下回车键可触发查询" onkeydown="queryEnter(event.keyCode||event.which)"/>
									</td>
									<td>
										<input id="queryBtn" type="button" class="easyui-button" value="查询" onclick="queryOrgList()">
									</td>
								</tr>
							</table>
						</ui:Layout>
						<ui:Layout region="center" style="padding-top:2px" border="false">
							<ui:Grid id="orgList" datatype="json" shrinkToFit="true" fit="true" 
								viewrecords="true" pageable="true" url="${ctx}/orgCtl/orgList.html" 
								rowNum="20" rowList="[10,20,50]">
								<ui:Column name="ZJID" key="true" hidden="true"></ui:Column>
								<ui:Column header="" width="30" fixed="true" align="center" formatter="radioFormat"></ui:Column>
								<ui:Column header="单位编码" name="DWBH" width="90" fixed="true"></ui:Column>
								<ui:Column header="单位名称" name="DWMC"  width="250" fixed="true"></ui:Column>
								<ui:Column header="上级单位" name="SJDWMC" width="250" fixed="true"></ui:Column>
							</ui:Grid>
						</ui:Layout>
					</ui:LayoutContainer>
				</ui:Tab>
			</ui:TabNavigator>
				
		</ui:Layout>
		<ui:Layout region="south" border="false" style="height:30px;text-align:center">
			<input type="button" class="easyui-button" style="margin-top:3px" value="确定" onclick="save()" />
			<input type="button" class="easyui-button" style="margin-top:3px" value="关闭" onclick="javascript:parentDialog.close()" />
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>