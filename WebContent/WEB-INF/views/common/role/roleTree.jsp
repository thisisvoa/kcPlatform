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
	
	//省级
	var JSJB_SHENGJI = "SHENGJI";
	//市级
	var JSJB_SHIJI = "SHIJI";
	//县级
	var JSJB_XIANJI = "XIANJI";
	
	
	function onRoleTreeClick(event, treeId, treeNode){
		var id = treeNode.id;
		if (JSJB_SHENGJI != id && JSJB_SHIJI != id
				&& JSJB_XIANJI != id) {
			parent.frames["roleList"].document.location.href = "${ctx}/roleCtl/toEditRole.html?id="
					+ id;
		} else {
			parent.frames["roleList"].document.location.href = "${ctx}/roleCtl/toRoleList.html?roleLevelId="
					+ id;
		}
	}
	
	function dataFilter(event, parentNode, childNodes){
		if(!childNodes) return null;
		for(var i=0;i<childNodes.length;i++){
			var id = childNodes[i].id;
			if(JSJB_SHENGJI==id || JSJB_SHIJI==id || JSJB_XIANJI==id){
				childNodes[i].icon = "${ctx}/ui/css/icon/images/user_group.gif";
			}else{
				childNodes[i].icon = "${ctx}/ui/css/icon/images/user.gif";
			}
		}
		return childNodes;
	}
</script>
</head>
<body>
	<ui:Panel fit="true" title="所有角色">
		<ul id="roleTree" class="easyui-tree" asyncEnable="true"  url="${ctx}/roleCtl/roleTreeJson.html" 
  				rootPId="0" simpleDataEnable="true"
  				onClick="onRoleTreeClick" dataFilter="dataFilter"></ul>
	</ui:Panel>
</body>
</html>