<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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
	var topWin = FrameHelper.getTop();
	function btnConfirmClick(){
		var val=$('input:radio[name="cxRadio"]:checked').val(); 
		if(val){
			var rowd = $("#cxList").grid('getRowData',val);
			parentDialog.setData({selectedRowd:rowd});
			parentDialog.markUpdated();
			parentDialog.close();
		}else{
			MessageUtil.alert("请选择一张归档表！");
		}
	} 
	function trim(str){ //删除左右两端的空格 
		return str.replace(/(^\s*)|(\s*$)/g, ""); 
	}
	
	function btnQueryClick(){
		var cxmc = trim(document.getElementById("qcxmc").value);
		if (/[%~#^$@%&!*,.，。'"]/gi.test(cxmc)
				 ) {
				      topWin.Dialog.alert("查询条件不能包含特殊字符！");
					return false;
				}
			var postData = {};
			postData.gdhbmc = $("#qcxmc").val();
			$("#cxList").grid("setGridParam", {postData:postData,page:1});
			$("#cxList").trigger("reloadGrid");
		}
	

	function xtDetailFormat(cellVal, options, rowd){
		var cxmc = "${cxmc}";
		var checkStr = "";
		if(cxmc && cxmc == rowd.gdhbmc)
			checkStr = " checked='checked' ";
		var html = "<input type='radio' name='cxRadio' class='easyui-radiobox' style='margin:1px' value='"+rowd.gdhbmc+"' "+checkStr+"/>";
		return html;
	}
	
	function onGridComplete(){
		using("button", function(){
			$(".easyui-button").button();
		});
	}
	
	function timeFormatter(cellVal,options,rowd){
		if(cellVal==null){
			return "";
		}else{
			var d = new Date(cellVal);
			return DateFormat.format(d, "yyyy-MM-dd hh:mm:ss");
		}
	}
/* 	//回车查询事件
	function queryEnter(e){
		if(e == 13){
			$("#btnQuery").click();	
		}
	} */
</script>
</head>
<body style="padding:1px" >
	<ui:LayoutContainer fit="true">
		<ui:Layout region="north" style="height:70px"  radius="true"> 
			<div id="toolbar" class="easyui-toolbar" style="width:100%;margin-top:2px">
				<a id="queryBtn" href="#" iconCls="icon-search" onclick="btnQueryClick()">查询</a>
				<a id="add" href="#" iconCls="icon-add" onclick="btnConfirmClick()">选择</a>
				<a id="close" href="#" iconCls="icon-close" onclick="javascript:parentDialog.close();">取消</a>
			</div>
			<form id = "searchForm" class="easyui-validate" >
				<table class="formTable">
					<tr>
						<td class="label" style="width:100px">
							归档表名称：
						</td>
						<td>
							<input name="qcxmc" id="qcxmc" class="easyui-textinput validate[maxSize[25]]" >
						</td>
					</tr>
				</table>		
			</form>
		</ui:Layout>
		<ui:Layout region="center" border="false">
			<ui:Grid id="cxList" datatype="json" shrinkToFit="false" fit="true" viewrecords="true" pageable="true"
				url="${ctx}/gdqd/gdqdTableList.html?gdbmc=${gdbmc}" multiselect="false"
				rowNum="20" rowList="[10,20,50]" gridComplete="onGridComplete">
				<ui:Column name="gdhbmc" key="true" hidden="true"></ui:Column>
				<ui:Column  header="" name="zjid" width="20" formatter="xtDetailFormat"></ui:Column>
				<ui:Column header="归档表名称" name="gdhbmc" width="250"></ui:Column>
				<ui:Column header="归档数据开始时间" name="gdsjKssj" width="130" formatter="timeFormatter"></ui:Column>
				<ui:Column header="归档数据结束时间" name="gdsjJssj" width="130" formatter="timeFormatter"></ui:Column>
			</ui:Grid>
		</ui:Layout>
	</ui:LayoutContainer>
</body>
</html>
