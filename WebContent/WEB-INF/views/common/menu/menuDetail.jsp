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
<script language="javascript">
	function btnSubmit() {
		if($("#form").validate("validate")){
			if($("#type").val() == "add"){
				$("#saveBtn").attr("disabled",true);
				$("#reAddBtn").removeAttr("disabled");
			}
			
			var data = $("#form").serialize();
			var menuTree = $("#sjcd").combotree("tree");
			var treeObj = menuTree.tree("getZTreeObj");
			var selItem = treeObj.getSelectedNodes()[0];
			if(selItem.zjId=="0"){
				data += ("&cdjb=1");
			}else{
				data += ("&cdjb="+(parseInt(selItem.cdjb)+1));	
			}
			
			if("${type}"=="edit"){
				var oldSybz = '${menu.sybz}';
				var sybz = $("input[name='sybz']:checked").val();
				if(sybz==oldSybz){
					MessageUtil.confirm("确定对菜单信息进行修改吗?",function(){
						$.postc("${ctx}/menu/menuSave.html",data, function(){
							MessageUtil.show("操作成功!");
							window.parent.location.reload();
						});
					});	
				}else{
					if(sybz=="1"){
						MessageUtil.confirm("您启用了该菜单，系统将启用其上级菜单，确定进行修改?",function(){
							$.postc("${ctx}/menu/menuSave.html",data, function(){
								MessageUtil.show("操作成功!");
								window.parent.location.reload();
							});
						});
					}else{
						MessageUtil.confirm("您禁用了该菜单，系统将禁用其子菜单，确定进行修改?",function(){
							$.postc("${ctx}/menu/menuSave.html",data, function(){
								MessageUtil.show("操作成功!");
								window.parent.location.reload();
							});
						});
					}
				}
				
			}else{
				$.postc("${ctx}/menu/menuSave.html",data, function(){
					parentDialog.markUpdated();	
					MessageUtil.show("操作成功!");
				});
			}
			
		}
		
	}
	
	/**
	 * 再次添加
	 */
	function reAdd(){
		$("#reAddBtn").attr("disabled",true);
		$("#saveBtn").removeAttr("disabled");
		$("#form")[0].reset();
		queryMaxCdxh();
	}
	
	function btnClose() {
		parentDialog.close();
	}
	/**
	 * 加载完后删除选中节点的子节点及自己
	 */
	function onAsyncSuccess(event, treeId, treeNode, msg){
		if($("#zjId").val()!=null && $("#zjId").val()!=""){
			var menuTree = $("#sjcd").combotree("tree");
			var treeObj = menuTree.tree("getZTreeObj");
			var node = treeObj.getNodeByParam("zjId", $("#zjId").val());
			treeObj.removeChildNodes(node);
			treeObj.removeNode(node);
		}
		
	}
	
	function queryMaxCdxh(){
		if($("#type").val()=="add"){
			var sjcd = $("#sjcd").combotree("getValue");
			if(sjcd!="" ){
				$.postc("${ctx}/menu/queryMaxCdxh.html?parentId="+sjcd,null, function(data){
					$("#cdxh").val(data);
					$("#cdxh").trigger("blur");
				});
			}
			
		}
	}
</script>
</head>
<body>
	<ui:Panel fit="true" border="false">
		<form id="form" class="easyui-validate">
			<input type="hidden" id="zjId" name="zjId" value="${menu.zjId}" />
			<input type="hidden" id="type" name="type" value="${type}" />
			<table class="formTable">
				<c:if test="${type eq 'edit'}">
				<tr>
					<td height="2">&nbsp;</td>
				</tr>
				</c:if>
				<tr>
					<td class="label">菜单名称：</td>
					<td><input id="cdmc" type="text" name="cdmc" value="${menu.cdmc}" 
						class="easyui-textinput validate[required,maxSize[50]]" id="gndm" style="width: 360px;"/> <span
						class="star" id="checkCdmc" >*</span></td>
				</tr>
				<tr>
					<td class="label">菜单地址：</td>
					<td style="padding:2px 0px 2px 0px"><textarea name="cddz" 
						class="easyui-textarea validate[required,maxSize[100]]" style="width:360px;height:60px">${menu.cddz}</textarea> 
						<span class="star">*</span>
					</td>
				</tr>
				<tr>
					<td class="label">上级菜单：</td>
					<td>
						<select name="sjcd" id="sjcd" class="easyui-combotree validate[required]" url="${ctx}/menu/loadAllMenu.html"
							idKey="zjId" nameKey="cdmc" pIdKey="sjcd" panelWidth="350" simpleDataEnable="true"
							selValue="${menu.sjcd}" onAsyncSuccess="onAsyncSuccess" onChange="queryMaxCdxh">
						</select> 
						<span class="star">*</span>
					</td>
				</tr>
				<tr>
					<td class="label">最后一级菜单：</td>
					<td>
						<span>
							<input type="radio" name="sfzhyicd" value="1" class="easyui-radiobox validate[required]"
								${(menu.sfzhyicd== '1')?'checked':''}/>是
							<input type="radio" name="sfzhyicd" value="0" class="easyui-radiobox validate[required]"
								${(menu.sfzhyicd== '0')?'checked':''}/>否
						</span>
					</td>
				</tr>
				<tr>
					<td class="label">菜单序号：</td>
					<td><input type="text" name="cdxh" value="${menu.cdxh}" id="cdxh"
						class="easyui-textinput validate[required,custom[integer],maxSize[10]]"
						onkeyup="this.value=this.value.replace(/\D/g,'')"
						onafterpaste="this.value=this.value.replace(/\D/g,'')" /> <span
						class="star">*</span>
					</td>
				</tr>
				<c:if test="${type=='edit'}">
				<tr>
					<td class="label">使用标识：</td>
					<td>
						<span>
							<input id="sybz1" type="radio" name="sybz" value="1" class="easyui-radiobox validate[required]"
								${(menu.sybz== '1')?'checked':''}/>启用
							<input id="sybz2" type="radio" name="sybz" value="0" class="easyui-radiobox validate[required]"
								${(menu.sybz== '0')?'checked':''}/>禁用
						</span>
					</td>
				</tr>
				</c:if>
				<tr>
					<td class="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
					<td><textarea id="bz" class="easyui-textarea validate[maxSize[500]" name="bz"  style="width:360px;height:120px">${menu.bz}</textarea>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align:center">
						
						<c:if test="${type != 'edit'}">
							<input id="reAddBtn" type="button"  class="easyui-button" disabled value="再次添加" onclick="reAdd()"/>
						</c:if>
						
						<kpm:HasPermission permCode="SYS_MENU_UPDATE">
							<input id="saveBtn" type="button" class="easyui-button" value="保存" onclick="btnSubmit()"/>
						</kpm:HasPermission>
						
						<c:if test="${type != 'edit'}">
							<input type="button"  class="easyui-button" value="关 闭" onclick="btnClose()"/>
						</c:if>
					</td>
				</tr>
			</table>
		</form>
	</ui:Panel>
</body>
</html>
