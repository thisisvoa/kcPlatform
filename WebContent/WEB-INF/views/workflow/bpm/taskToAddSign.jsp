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
<title>选择补签人员</title>
<script type="text/javascript">
var topWin = FrameHelper.getTop();
function doSave(){
	if($("#editForm").validate("validate")){
		MessageUtil.confirm("确定添加选择的补签人员？",function(){
			var params = $("#editForm").serialize();
	   		 $.postc('${ctx}/workflow/task/addSign.html',params,
	       		 function(){
	   			 	parentDialog.markUpdated();
					MessageUtil.show("你成功进行了补签!");
					parentDialog.close();
	       		 }
	   		 );
		});
	}
}

function selectSignUsers(){
	var diag = new topWin.Dialog({
		URL : "${ctx}/userCtl/cmp/userSelector.html",
		Width: 900,
		Height: 450,
		Title : "用户选择器",
		RefreshHandler : function(){
			var selectedUserList = diag.getData().selectedUserList;
			if(selectedUserList!=null){
				var ids = "";
				var descs = "";
				var spliter='';
				for(var i=0;i<selectedUserList.length;i++){
					ids += spliter+selectedUserList[i].ZJID;
					descs += spliter+selectedUserList[i].YHMC;
					spliter=',';
				}
				$("#signUserIds").val(ids);
				$("#signUserNames").val(descs);
			}else{
				$("#signUserIds").val("");
				$("#signUserNames").val("");
			}
			$("#signUserNames").trigger('blur');	
		}
	});
	diag.setData({selectedUser:$("#signUserIds").val()});
	diag.show();
}
</script>
</head>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" style="height:33px">
		<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
			<a id="saveBtn" href="#" iconCls="icon-userAdd" onclick="doSave()">补签</a>
			<a id="closeBtn" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center">
		<form id="editForm" class="easyui-validate" onsubmit="javascript:return false;">
		<table class="tableView">
			<tr>
				<td class="label">
					选择补签人员：
				</td>
				<td valign="bottom">
					<input type="hidden" id="signUserIds" name="signUserIds"/>
					<input type="hidden" id="taskId" name="taskId" value="${taskId}"/>
					<textarea readonly="readonly" id="signUserNames" class="easyui-textarea validate[required]" style="width:300px;height:100px" onclick="selectSignUsers()" promptPosition="bottom"></textarea> 
					<input type="button" class="easyui-button" style="width:45px;" onclick="selectSignUsers()" value="选择.."/>
				</td>
			</tr>
		</table>
		</form>		
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>