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
	var topWin = FrameHelper.getTop();
	$parsed(function(){
		$("#orgEditForm").validate("addRule",
			{	name:"validateUniqueOrgNo",
				rule:{
		           "url": "${ctx}/orgCtl/validateUniqueOrgNo.html?orgId=${orgInfo.zjid}",
		           "alertText": "* 此编号已存在"
				}
			});
	});
	function save() {
		if($("#orgEditForm").validate("validate")){
			var data = $("#orgEditForm").serialize();
			if("${operModel}"=="ADD"){
				$.postc("${ctx}/orgCtl/addOrgInfo.html", data, function(){
					$("#saveBtn").attr("disabled", true);
					$("#resetBtn").removeAttr("disabled");
					parentDialog.markUpdated();
					MessageUtil.show("新增单位信息成功!");
				});
			}else if("${operModel}"=="UPDATE"){
				MessageUtil.confirm("确定保存单位信息？",function(){
					$.postc("${ctx}/orgCtl/editOrgInfo.html", data, function(){
						MessageUtil.show("修改单位信息成功!");
						parentDialog.markUpdated();
					});
					
				});
				
			}
			
		}
	}	
	function formReset(){
		$("#resetBtn").attr("disabled", true);
		$("#saveBtn").removeAttr("disabled");
		$("#orgEditForm")[0].reset();
	}
	
	function selectOrg(){
		var orgId = $("#parentOrg").val();
		var filterId = $("#orgId").val();//需过滤掉的组织
		var diag = new topWin.Dialog();
		diag.URL = "${ctx}/orgCtl/orgSelector.html?orgId=" + orgId+"&filterId="+filterId;
		diag.Width = 600;
		diag.Height = 450;
		diag.Title = "单位选择器";
		diag.RefreshHandler = function(){
			var org = diag.getData();
			if(org!=null){
				$("#parentOrg").val(org.orgId);	
				$("#parentOrg_name").val(org.orgName);
			}
		};
		diag.show();
	}
</script>
</head>

<body>
	<form id="orgEditForm" class="easyui-validate" method="post">
		<input type="hidden" id="operModel" name="operModel" value="${operModel}" />
		<input type="hidden" id="orgId" name="orgId" value="${orgInfo.zjid}" />
		<table class="formTable">
			<tr>
				<td colspan="2" align="center">
					<c:if test="${operModel=='ADD'}">
						<input id="resetBtn" type="button" onclick="formReset();" class="easyui-button" style="width:60px;" disabled value="再次添加" />
					</c:if> 
					<input id="saveBtn" type="button" value="保存" class="easyui-button" style=" width:60px;" onclick="save()"/> 
					<input type="button" value="关 闭 " onclick="javascript:parentDialog.close();" class="easyui-button" style=" width:60px;"/>
				</td>
			</tr>
			<tr>
				<td class="label">单位编号：</td>
				<td>
					<input type="text" id="orgNum" name="orgNum"
						value="${orgInfo.dwbh}" style="width:360px;"
						class="easyui-textinput validate[required,maxSize[15],ajax[validateUniqueOrgNo]]"/> 
					<span id="checkOrgNum" class="star">*</span></td>
			</tr>
			<tr>
				<td class="label">单位名称：</td>
				<td>
					<input id="orgName" type="text" name="orgName" value="${orgInfo.dwmc}" style="width:360px;"
						class="easyui-textinput validate[required,maxSize[50]]" /> 
					<span class="star">*</span>
				</td>
			</tr>
			<tr>
				<td class="label">单位简称：</td>
				<td>
					<input type="text" id="simpleName" name="simpleName" value="${orgInfo.dwjc}" class="easyui-textinput validate[maxSize[50]]" style="width:360px;"/>
				</td>
			</tr>
			<tr>
				<td class="label">上级单位：</td>
				<td>
					<input type="text" id="parentOrg_name" readonly="readonly" value="${parentOrg.dwmc}"  
						class="easyui-textinput" style="width: 360px;" />&nbsp;
					<input type="button" class="easyui-button" id="parentOrg_btn" title="请选择" value="选择"  onclick="selectOrg()"/>
					<input type="hidden" id="parentOrg" name="parentOrg" value="${parentOrg==null?'0':parentOrg.zjid}"/>
				</td>
			</tr>

			<tr>
				<td class="label">单位负责人：</td>
				<td><input type="text" id="orgManager" name="orgManager"
					value="${orgInfo.dwfzr}" style="width: 200px;" class="easyui-textinput validate[maxSize[10]]"/> 
				</td>
			</tr>
			<tr>
				<td class="label">单位电话：</td>
				<td><input type="text" id="telephoneNo" name="telephoneNo" class="easyui-textinput validate[maxSize[20]]"
					value="${orgInfo.dwdh}" style="width: 200px;" /></td>
			</tr>
			<tr>
				<td class="label">单位邮箱：</td>
				<td><input type="text" id="email" name="email" class="easyui-textinput validate[maxSize[20],custom[email]]"
					value="${orgInfo.dwyx}" style="width: 200px;" /><span id="checkEmail" class="star"></span></td>
			</tr>
			<tr>
				<td class="label">单位类别：</td>
				<td>
					<select id="orgType" name="orgType" class="easyui-combobox validate[required]" 
						style="width: 200px;" selValue="${orgInfo.dwlx}" dropdownHeight="75">
						<option value="">---请选择---</option>
						<option value="1">普通</option>
						<option value="0">高级</option>
					</select><span class="star">*</span>
				</td>
			</tr>
			<tr>
				<td class="label">单位级别：</td>
				<td>
					<select id="orgLevel" name="orgLevel"
						class="easyui-combobox validate[required]" style="width: 200px;" dropdownHeight="75" selValue="${orgInfo.dwjb}" dropdownHeight="60">
							<option value="">---请选择---</option>
							<option value="1">省级</option>
							<option value="0">市级</option>
					</select><span class="star">*</span>
				</td>
			</tr>
			<tr>
				<td class="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
				<td><textarea id="remark" name="remark" style="width:360px;height:120px" class="easyui-textarea validate[maxSize[500]]">${orgInfo.bz }</textarea>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
