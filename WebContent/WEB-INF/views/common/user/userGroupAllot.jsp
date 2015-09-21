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
<title>用户组用户分配</title>
<!--弹出式提示框-->
<script type="text/javascript">
	//初始化时选中Id;
	var allotedIds = [];
	function saveRelation(){
		var initIds = allotedIds.join(",");
		var selectedIds = $("#userList").grid("getGridParam", "selarrrow");
		var checkIds = selectedIds.join(",");
		
		var userGroupId = "${userGroupId}";
		var url = "${ctx}/userGroupCtl/saveRelaction.html";
		var params = {
			userIds:checkIds,
			selectedIds:initIds,
			userGroupId:userGroupId
		};
		MessageUtil.confirm("确定将这些用户添加到用户组内?", function(){
			$.postc(url,params,function(data){
				MessageUtil.show("操作成功");
				parent.window.refresh();
			});		
		});
			
	}
	
	function refreshGrid(){
		$("#userList").trigger("reloadGrid");
	}
	function onLoadComplete(xhr){
		var data = $("#userList").grid("getRowData");
		
		for(var i=0;i<data.length;i++){
			if(data[i].ISALLOTE=="1"){
				allotedIds.push(data[i].YHID);
				$("#userList").grid("setSelection", data[i].YHID);
			}
		}
	}

</script>
</head>

<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="padding:1px 2px 0px 1px;height:32px" border="false">
		<div id="toolbar" class="easyui-toolbar" style="width:99.5%">
			<a id="addUserGroupBtn" href="#" iconCls="icon-save" onclick="saveRelation()">保存</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center" border="false" style="padding:1px">
		<ui:Grid id="userList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
			url="${ctx}/userGroupCtl/getAssignUserList.html?userGroupId=${userGroupId}&isAllot=${isAllot}" multiselect="true"
			rowNum="20" rowList="[10,20,50]" multiboxonly="false" loadComplete="onLoadComplete">
			<ui:Column name="YHID" key="true" hidden="true"></ui:Column>
			<ui:Column name="ISALLOTE" hidden="true"></ui:Column>
			<ui:Column header="警员编号" name="JYBH" width="90"></ui:Column>
			<ui:Column header="所在单位" name="DWMC" width="120"></ui:Column>
			<ui:Column header="用户姓名" name="YHMC" width="90"></ui:Column>
			<ui:Column header="性别" name="XB" width="60" edittype="select" formatter="'select'" editoptions="{value:\"0:女;1:男\"}"></ui:Column>
			<ui:Column header="用户类别" name="YHLX" width="60" edittype="select" formatter="'select'" editoptions="{value:\"0:高级;1:普通\"}"></ui:Column>
			<ui:Column header="用户级别" name="YHJB" width="60" edittype="select" formatter="'select'" editoptions="{value:\"0:省级;1:市级\"}"></ui:Column>
		</ui:Grid>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>
