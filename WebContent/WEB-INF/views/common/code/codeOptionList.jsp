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

<title>代码项列表</title>
<!--弹出式提示框-->
<script type="text/javascript">
		var topWin = FrameHelper.getTop();
  		// 查看角色详细信息
  		function view(){
  			var selectedIds = $("#codeList").grid("getGridParam", "selarrrow");
  			if (selectedIds.length == 0){
  				MessageUtil.alert("请先选择一个代码项！");
  				return;
  			}else if(selectedIds.length > 1){
  				MessageUtil.alert("只能选择一个代码项！");
  			}else{
  				topWin.Dialog.open({
  	  				URL : "${ctx}/codeCtl/toViewCode.html?isCodeOption=1&id="+selectedIds[0],
  	  				Modal : false,
  	  				Title : "代码项详细信息",
  					Width:650,
  					Height:325
  	  			});	
  			}
  			
		}
		//新增代码信息
		function add(){
			var codeId = $("#codeId").val();
			topWin.Dialog.open({
				URL : "${ctx}/codeCtl/toAddCode.html?isDialog=1&isCodeOption=1&pCodeId="+codeId,
				ShowCloseButton : true,
				Title : "新增代码项",
				Width:650,
				Height:325,
				RefreshHandler:function(){
					$("#codeList").trigger('reloadGrid');
				}
			});
		}
		//编辑代码项信息
		function edit(){
			var selectedIds = $("#codeList").grid("getGridParam", "selarrrow");
  			if (selectedIds.length == 0){
  				MessageUtil.alert("请先选择一个代码项！");
  				return;
  			}else if(selectedIds.length > 1){
  				MessageUtil.alert("只能选择一个代码项！");
  			}else{
  				topWin.Dialog.open({
  					URL : "${ctx}/codeCtl/toEditCode.html?isDialog=1&isCodeOption=1&id="+selectedIds[0],
  					ShowCloseButton : true,
  					Title : "修改代码项",
  					Width:650,
  					Height:325,
  					RefreshHandler:function(){
  						$("#codeList").trigger('reloadGrid');
  					}
  				});
  			}
			
		}
		
		//禁用代码项(可批量)
		function forbid(){
			var zjIds = mutilSelect();
			if(zjIds != null && zjIds != ''){
				MessageUtil.confirm("您确定禁用选中的代码项吗？",function() {				
					var url = "${ctx}/codeCtl/forbidCode.html?ids=" + zjIds;
					$.postc(url,null,function(data){
						if(data == 1){
							MessageUtil.show("操作成功!");
							$("#codeList").trigger('reloadGrid');
						}else if(data == 0){
							MessageUtil.show("操作失败，请联系系统管理员！");
						}
					});
					
				}, function() {
					return;
				});
			}
			
		}
		//启用代码项(可批量)
		function enabled(){
			var zjIds = mutilSelect();
			if(zjIds != null && zjIds != ''){
				MessageUtil.confirm("您确定启用选中的代码项吗？",function() {				
					var url = "${ctx}/codeCtl/enabledCode.html?ids=" + zjIds;
					$.postc(url,null,function(data){
						if(data == 1){
							MessageUtil.show("操作成功!");
							$("#codeList").trigger('reloadGrid');
						}else if(data == 0){
							MessageUtil.show("操作失败，请联系系统管理员！");
						}
					});
				}, function() {
					return;
				});
			}
		}				
		
	//逻辑删除代码项(可批量)
	function deleted(){
		var selectedIds = $("#codeList").grid("getGridParam", "selarrrow");
		if (selectedIds.length <= 0) {
			MessageUtil.alert("请先最少选择一个代码项！");
			return;
		}
		MessageUtil.confirm("您确定删除选中的代码项吗？",function(){
			var url = "${ctx}/codeCtl/logicDelCode.html";
			var params = {
				ids:selectedIds.join(",")
			};
			$.postc(url,params,function(data){
				MessageUtil.show("操作成功!","系统提示");
				$("#codeList").trigger('reloadGrid');
			});
			
		});
	}
	
	function timeFormatter(cellVal,options,rowd){
		if(cellVal!=null){
			//var date = new Date(cellVal);
			return DateFormat.format(DateFormat.parse(rowd.jlxgsj, 'yyyy-MM-dd hh:mm:ss'),"yyyy-MM-dd hh:mm:ss");
			//return DateFormat.format(date,"yyyy-MM-dd hh:mm:ss.S");
		}
		return "";
	}
	
	
	function onGridComplete(){
		using("button", function(){
			$(".easyui-button").button();	
		});
	}
	
	function back(){
		document.location.href = "${ctx}/codeCtl/toCodeList.html";
	}
	
	//多选
	function mutilSelect(){
		var selectedIds = $("#codeList").grid("getGridParam", "selarrrow");
		var returnValue = "";
		if (selectedIds.length == 0){
			MessageUtil.alert("请先最少选择一个代码项！");
		}else{
			returnValue = selectedIds;
		}
		
		return returnValue;
	}
	
	function sybzFormater(cellVal,options,rowd){
		if(cellVal=="0"){
			return "<span style='color:red'>禁用</span>";
		}else{
			return "启用";
		}
	}
	
	function queryList(){
		$("#codeList").grid("setGridParam", {postData:{type:'query',codeName:$("#codeItemName").val(),codeItemCode:$("#codeItemCode").val(),useFlag:$("#codeItemUseFlag").combobox("getValue"),codeSimpleName:$("#codeSimpleName").val()}});
		$("#codeList").trigger('reloadGrid');
	}
	
	function queryEnter(e){
		if(e == 13){
			$("#queryBtn").click();	
		}
	}
</script>
</head>

<body>
	<ui:LayoutContainer fit="true">
		<ui:Layout region="north" radius="true" style="height:90px" title="代码项管理">
			<table class="formTable">				
				<tr>
					<td class="label" width="15%">代码名称：</td>						
					<td >
						<input type="hidden" id="codeId" name="codeId" value="${codeInfo.zjid }" />
						<input type="text" id="codeName" name="codeName" class="easyui-textinput" disabled value="${codeInfo.dmmc }" style="width:180px;"/>
						
					</td>
					<td class="label" width="15%">代码简称：</td>
					<td>
						<input type="text" id="codeSimpleName"  name="codeSimpleName" class="easyui-textinput" value="${codeInfo.dmjc}" style="width:180px;"
									disabled="disabled"/>
					</td>
					<td class="label" width="15%">代码使用标识：</td>
					<td width="15%">
						<c:if test="${codeInfo.sybz == '1'}"><font color='green'>已启用</font> </c:if>
						<c:if test="${codeInfo.sybz == '0'}"> <font color='red'>已禁用</font> </c:if>
					</td>
				</tr>
				<tr>
						<td class="label" width="15%">代码项编号：</td>
						<td>
							<input type="text" id="codeItemCode" name="codeItemCode" watermark="支持模糊查询" class="easyui-textinput" style="width:180px;" value="" title="按下回车键可触发查询" onkeydown="queryEnter(event.keyCode||event.which)"/>
						</td>
						<td class="label" width="15%">代码项名称：</td>
						<td>
							<input type="text" id="codeItemName" name="codeItemName" watermark="支持模糊查询" class="easyui-textinput" style="width:180px;" value="" title="按下回车键可触发查询"  onkeydown="queryEnter(event.keyCode||event.which)"/>
						</td>
						<td class="label" width="15%">代码项使用标识：</td>
						<td width="15%">
						<select class="easyui-combobox" id="codeItemUseFlag" name="codeItemUseFlag" selValue="" style="width:90px" dropdownHeight="75">
								<option value="">---请选择---</option>
								<option value="1" >启用</option>
								<option value="0" >禁用</option>
							</select>
						</td>
					</tr>
			</table>
		</ui:Layout>
		<ui:Layout region="center" border="false">
			<ui:LayoutContainer fit="true">
				<ui:Layout region="north" style="height:35px;" border="false">
					<div id="toolbar" class="easyui-toolbar" style="width:99.5%;margin-top:2px">
						<a id="back" href="#" iconCls="icon-back" onclick="back()">返回</a>
						<kpm:HasPermission permCode="SYS_CODE_TWO_QUERY">
						<a id="queryBtn" href="#" iconCls="icon-search" onclick="queryList()">查询</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_CODE_TWO_ADD">
							<a id="add" href="#" iconCls="icon-add" onclick="add()">新增</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_CODE_TWO_UPDATE">
							<a id="modify" href="#" iconCls="icon-edit" onclick="edit()">修改</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_CODE_TWO_DELETE">
							<a id="remove" href="#" iconCls="icon-remove" onclick="deleted()">删除</a>
						</kpm:HasPermission>
						<a id="view" href="#" iconCls="icon-view" onclick="view()">查看</a>
						<kpm:HasPermission permCode="SYS_CODE_TWO_ENABLED">
							<a id="qy" href="#" iconCls="icon-ok" onclick="enabled();">启用</a>
						</kpm:HasPermission>
						<kpm:HasPermission permCode="SYS_CODE_TWO_FORBID">
							<a id="jy" href="#" iconCls="icon-no" onclick="forbid();">禁用</a>
						</kpm:HasPermission>
					</div>
				</ui:Layout>
				<ui:Layout region="center" border="false">
			<ui:LayoutContainer fit="true">
				<ui:Layout region="center" style="padding-top:2px" border="false">
					<ui:Grid id="codeList" datatype="json" shrinkToFit="true" fit="true" viewrecords="true" pageable="true"
						url="${ctx}/codeCtl/codeOptionList.html?codeSimpleName=${codeInfo.dmjc}"
						rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete" multiselect="true">
						<ui:Column name="zjid" key="true" hidden="true"></ui:Column>
						<ui:Column header="代码项编号" name="dmx_bh" width="100" fixed="true"></ui:Column>
						<ui:Column header="代码项名称" name="dmx_mc" width="340" fixed="true"></ui:Column>
						<ui:Column header="更新时间" name="jlxgsj" width="180" fixed="true" align="center"></ui:Column>
						<ui:Column header="使用标识" name="sybz" width="60" fixed="true" edittype="select" align="center" formatter="sybzFormater"  editoptions="{value:\"0:禁用;1:启用\"}"></ui:Column>
					</ui:Grid>
				</ui:Layout>
			</ui:LayoutContainer>
		</ui:Layout>
			</ui:LayoutContainer>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>
