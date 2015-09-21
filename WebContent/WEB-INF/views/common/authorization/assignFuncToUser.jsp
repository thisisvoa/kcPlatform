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
	var funcTree;
	
	function save(){
		var userId = $("#userId").val();
		if(userId==""){
			MessageUtil.alert("请选择授权的用户！");
			return;
		}
		
		var funcTree = $("#funcTree").tree("getZTreeObj");
		var checkItems = funcTree.getCheckedNodes(true);
		
		var selFunc = [];
		for(var i=0;i<checkItems.length;i++){
			if(checkItems[i].type == "FUNC"){
				selFunc.push(checkItems[i].nodeId);
			}
		}
		var funcStr = selFunc.join(",");
		MessageUtil.confirm("确定将选择的功能分配给[${user.yhmc}]吗？",function(){
			$.postc("${ctx}/userFunc/assignFuncToUser.html",{userId: userId, funcs:funcStr},
				function (data){
					MessageUtil.show('成功为用户授权！');
				}
			);	
		});
	}
</script>
</head>
<body>
	<ui:LayoutContainer fit="true">
		<ui:Layout region="north" style="height:30px" border="false">
			<div style="padding-top:8px">
				
				<div>
					<c:forEach var="item" items="${parentOrgList}" varStatus="status"  step="1">  
	        				${item.DWMC}&gt;&gt;
					</c:forEach> 
					${user.yhmc}&nbsp;
				</div>
			</div>
		</ui:Layout>
		<ui:Layout region="center">
			<ul id="funcTree" class="easyui-tree" asyncEnable="true"  url="${ctx}/userFunc/funcTreeJson4User.html?userId=${user.zjid}" chkEnable="true"
  				rootPId="0" simpleDataEnable="true" chkboxType="{'Y':'s', 'N':'s'}"></ul>
		</ui:Layout>
		<ui:Layout region="south" style="height:30px;text-align:center" border="false">
			<input id="userId" type="hidden" value="${user.zjid}"/>
			<input type="button" class="easyui-button" value="保存" style="margin-top:3px" onclick="save()">
			<input type="button" class="easyui-button" style="margin-top:3px" value="关闭" onclick="javascript:parentDialog.close()" />
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>