
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="ui" uri="/tags/ui" %>
<%@ taglib prefix="kpm" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<title></title>
<script type="text/javascript">
	$parsed(function(){
		sjlxChange();
		$("#editForm").validate("addRule",
			{	name:"validateGdbmc",
				rule:{
		           "url": "${ctx}/gdpz/validateGdbmc.html?zjid=${gdpz.zjid}",
		           "alertText": "* 此归档表已存在"
				}
			});
	});
	
	function doSave(){
		if($("#editForm").validate("validate")){
			var parms = $("#editForm").serialize();
			var type = "${type}";
			if(type=="add"){
				$.postc("${ctx}/gdpz/addGdpz.html", parms, function(data){
					MessageUtil.show("添加成功！");
					$("#reAddBtn").linkbutton("enable");
					$("#saveBtn").linkbutton("disable");
					parentDialog.markUpdated();
				});
			}else if(type=="update"){
				$.postc("${ctx}/gdpz/updateGdpz.html", parms, function(data){
					MessageUtil.show("修改成功！");
					parentDialog.markUpdated();
				});
			}
		}
	}
	function reAdd(){
		$("#editForm")[0].reset();
		$("#reAddBtn").linkbutton("disable");
		$("#saveBtn").linkbutton("enable");
	}
	
	function sjlxChange(){
		var sjclSjlx = $('#sjclSjlx').combobox('getValue');
		if(sjclSjlx == "1"){			
			$('#sjclGs').combobox('disable',true);
			$('.rqgs').hide();
		}else{
			$('#sjclGs').combobox('disable',false);
			$('.rqgs').show();
		}
	}
</script>
<body>
<ui:LayoutContainer fit="true">
	<ui:Layout region="north" border="false" style="height:33px;">
		<div id="toolbar" class="easyui-toolbar">
			<c:if test="${type == 'add'}">
				<a href="#" iconCls="icon-add" onclick="reAdd()" id="reAddBtn" disabled>继续添加</a>
			</c:if>
			<a href="#" iconCls="icon-save" onclick="doSave()" id="saveBtn">保存</a>
			<a href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close();">关闭</a>
		</div>
	</ui:Layout>
	<ui:Layout region="center" border="false">
		<form id="editForm" class="easyui-validate" style="margin-top:5px;">
			<input type="hidden" name="zjid" id="zjid" value="${gdpz.zjid}"/>
			<input type="hidden" name="type" id="type" value="${type}"/>
			<c:if test="${type == 'add'}">
				<input type="hidden" name="sybz" id="sybz" value="1"/>	
			</c:if>
			<c:if test="${type == 'update'}">
				<input type="hidden" name="sybz" id="sybz" value="${gdpz.sybz}"/>
			</c:if>
			<table class="formTable">
				<tr>
					<td class="label" style="width:100px">
						归档名称：
					</td>
					<td style="width:220px">
					<input type="text" class="easyui-textinput validate[required,maxSize[32]]" style="width: 150px;" id="gdmc" name="gdmc" value="${gdpz.gdmc}" />
					<span class="star">*</span>
					</td>
					<td class="label" style="width:110px">
						归档表：
					</td>
					<td>
					<input type="text" class="easyui-textinput validate[required,maxSize[32],ajax[validateGdbmc]]"  style="width: 150px;" <c:if test="${type == 'update'}"> readonly="readonly"</c:if> id="gdbmc" name="gdbmc" value="${gdpz.gdbmc}" />
					<span class="star">*</span>
					</td>
				</tr>
				<tr>
					<td class="label" style="width:100px">
						时间戳字段名：
					</td>
					<td style="width:220px">
						<input type="text" class="easyui-textinput validate[required,maxSize[16]]" style="width:150px;" id="sjclm" name="sjclm" value="${gdpz.sjclm}" />
						<span class="star">*</span>
					</td>
					<td class="label" style="width:110px">
						时间戳字段类型：
					</td>
					<td>
						<table style="width: 100%;height:100%;">
							<tr>
								<td style="width:65px;">
									<select id="sjclSjlx" name="sjclSjlx" class="easyui-combobox" style="width: 65px;" selValue="${gdpz.sjclSjlx}" dropdownHeight="50" onchange="sjlxChange">
										<option value="1">日期</option>
										<option value="2">字符串</option>
									</select>
								</td>
								<td align="right" style="wdith:35px;" class="rqgs">
									日期格式：
								</td>
								<td class="rqgs">
									<select id="sjclGs" name="sjclGs" class="easyui-combobox " style="width: 140px;" selValue="${gdpz.sjclGs}" dropdownHeight="100">
										<option value="yyyyMMdd">yyyyMMdd</option>
										<option value="yyyy-MM-dd">yyyy-MM-dd</option>
										<option value="yyyyMMddhhmmss">yyyyMMddHHmmss</option>
										<option value="yyyy-MM-dd hh:mm:ss">yyyy-MM-dd HH:mm:ss</option>
									</select>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="label" style="width:100px">
						归档周期：
					</td>
					<td style="width:220px">
						<table style="width: 100%;height:100%;">
							<tr>
								<td style="width:50px;">
									<input type="text" class="easyui-textinput validate[required,maxSize[5],custom[integer]]" style="width: 60px;" id="gdzq" name="gdzq" value="${gdpz.gdzq}" />
								</td>
								<td align="left">
									<select id="gdzqDw" name="gdzqDw" class="easyui-combobox validate[required]" style="width: 50px;" selValue="${gdpz.gdzqDw}" dropdownHeight="75">
											<option value="1">日</option>
											<option value="2">月</option>
											<option value="3">年</option>
									</select>						
									<span class="star">*</span>
								</td>
							</tr>
						</table>
					</td>
					<td class="label" style="width:110px">
						归档延迟天数：
					</td>
					<td>
						<input type="text" class="easyui-textinput validate[required,maxSize[5],custom[integer]]"  style="width: 60px;"  id="ycsj" name="ycsj" value="${gdpz.ycsj}" />
						<span class="star">*</span>
					</td>
				</tr>
				<tr>
					<td class="label" style="width:100px">
						归档时间：
					</td>
					<td >
						<table style="width: 100%;height:100%;">
							<tr>
								<td style="width:120px;">
									<input id="zxsj" name="zxsj" class="easyui-datePicker" dateFmt="HH:mm" value="${gdpz.zxsj}"/>			
								</td>
								<td align="left">
									<span class="star">*</span>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="label" style="width:100px">
						备注：
					</td>
					<td colspan="3">
						<textarea class="easyui-textarea validate[maxSize[256]]" id="bz" name="bz" style="width:550px;height:100px">${gdpz.bz}</textarea>
					</td>
				</tr>

			</table>
		</form>
	</ui:Layout>
</ui:LayoutContainer>
</body>
</html>