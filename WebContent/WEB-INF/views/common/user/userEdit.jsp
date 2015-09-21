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
		$("#userEditForm").validate("addRule",
			{	name:"validateUniqueUserNum",
				rule:{
		           "url": "${ctx}/userCtl/checkUserNum.html?userId=${userInfo.ZJID}",
		           "alertText": "* 此编号已存在"
				}
			});
		$("#userEditForm").validate("addRule",
				{	name:"validateUniqueLoginName",
					rule:{
			           "url": "${ctx}/userCtl/checkLoginName.html?userId=${userInfo.ZJID}",
			           "alertText": "* 此账号已存在"
					}
				});
		$("#userEditForm").validate("addRule",
				{	name:"checkSfzh",
					rule:{
			           "url": "${ctx}/userCtl/checkSfzh.html?userId=${userInfo.ZJID}",
			           "alertText": "* 此身份证号码已存在"
					}
				});
		
	});
	
	function save() {
		if($("#userEditForm").validate("validate")){
			if("${operModel}"=="ADD"){
				var data = $("#userEditForm").serialize();
				$.postc("${ctx}/userCtl/addUserInfo.html", data, function(data){
					MessageUtil.show("添加成功!");
					parentDialog.markUpdated();
					$("#saveBtn").attr("disabled",true);
					$("#resetBtn").removeAttr("disabled");
				});
			}else if("${operModel}"=="UPDATE"){
				var data = $("#userEditForm").serialize();
				MessageUtil.confirm("确定保存用户信息？",function(){
					$.postc("${ctx}/userCtl/updateUserInfo.html", data, function(data){
						MessageUtil.show("修改成功!");
						parentDialog.markUpdated();
					});	
				});
				
			}
		};
	}
	function formReset(){
		$("#userEditForm")[0].reset();
		$("#resetBtn").attr("disabled",true);
		$("#saveBtn").removeAttr("disabled");
	}
	
	function selectOrg(){
		var orgId = $("#org").val();
		var diag = new topWin.Dialog();
		diag.URL = "${ctx}/orgCtl/orgSelector.html?orgId=" + orgId;
		diag.Width = 700;
		diag.Height = 400;
		diag.Title = "单位选择器";
		diag.RefreshHandler = function(){
			var org = diag.getData();
			if(org!=null){
				$("#org").val(org.orgId);	
				$("#org_name").val(org.orgName);
				$("#org_name").trigger('blur');	
			}
		};
		diag.show();
	}
	
	//校验身份证
	function validIdCard(field, rules, i, options){
		var result = Validator.checkIdCard(field.val())
		if(result!=true){
			return result;
		};
	}
	//身份证小写转大写
	function upperLatter(obj){
		var upperValue = obj.value;
		if(upperValue != null && upperValue != ''){
			obj.value = upperValue.toUpperCase();
		}
	}
</script>
</head>
<body>
	<ui:Panel fit="true"> 
		<form id="userEditForm" class="easyui-validate" method="post">
			<input type="hidden" id="userId" name="userId" value="${userInfo.ZJID}" />
			<table class="formTable">
				<tr>
					<td colspan="2" align="center">
						<c:if test="${operModel == 'ADD'}">
							<input type="button" id="resetBtn" class="easyui-button" disabled value="再次添加  " onclick="formReset();"/>
						</c:if>
						<input type="button" id="saveBtn" class="easyui-button" value="保存 " onclick="save()"/>
						<input type="button" value="关 闭 " class="easyui-button" onclick="javascript:parentDialog.close();"/></td>
				</tr>
				<tr>
					<td class="label">用户名称：</td>
					<td><input id="userName" type="text" name="userName" style="width: 200px;" value="${userInfo.YHMC}"
							class="easyui-textinput validate[required,maxSize[15]]" /> 
						<span class="star">*</span></td>
				</tr>
				
				<tr>
					<td  class="label">登录账号：</td>
					<td><input id="loginName" type="text" name="loginName" style="width: 200px;" value="${userInfo.YHDLZH}"
						class="easyui-textinput validate[required,maxSize[15],ajax[validateUniqueLoginName]]"/> 
						<span class="star">*</span></td>
				</tr>
				<c:if test="${operModel == 'ADD'}">
				<tr>
					<td  class="label">登录密码：</td>
					<td><input id="psw" type="password" name="psw" style="width: 200px;"
						class="easyui-textinput validate[required,maxSize[16]]" value="123456"  /> 
						<span class="star">*</span>
						<span style="color:#F8F8F8">默认密码(123456)</span>
					</td>
				</tr>
				<tr>
					<td  class="label">确认密码：</td>
					<td><input id="psw2" type="password" name="psw2" 
						style="width: 200px;"
						class="easyui-textinput validate[required,maxSize[16],equals[psw]]" value="123456"  /> <span class="star">*</span></td>
				</tr>
				</c:if>
				<tr>
					<td  class="label">警员编号：</td>
					<td><input type="text" id="userNum" name="userNum" style="width: 200px;" 
						class="easyui-textinput validate[required,maxSize[20],ajax[validateUniqueUserNum]]"
						value="${userInfo.JYBH}"/> 
						<span id="checkUserNum" class="star">*</span>
					</td>
				</tr>
				<tr>
					<td  class="label">所属单位：</td>
					<td>
						<input type="text" id="org_name" onclick="selectOrg();" readonly="readonly" value="${userInfo.DWMC}" class="easyui-textinput validate[required]" style="width: 200px;"/>
						<span id="checkUserNum" class="star">*</span>
						<!-- <input type="button" id="org_btn" class="easyui-button" title="请选择" value="选择" onclick="selectOrg()"/>
						 --><input type="hidden" id="org" name="org" value="${userInfo.SSDW_ZJID}"/>
						
					</td>
				</tr>
				<tr>
					<td  class="label">性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别：</td>
					<td>
						<select id="sex" name="sex" class="easyui-combobox" style="width: 200px;" dropdownHeight="75" selValue="${userInfo.XB}">
							<option value="">---请选择---</option>
							<option value="1">男</option>
							<option value="0">女</option>
						</select>
					</td>
				</tr>
				<tr>
					<td  class="label">身份证号：</td>
					<td>
						<input type="text" id="sfzh" name="sfzh" onblur="upperLatter(this);"
							style="width: 200px;" class="easyui-textinput validate[required,funcCall[validIdCard],ajax[checkSfzh]]" 
							value="${userInfo.SFZH}" />
						<span id="checkUserNum" class="star">*</span>
					</td>
				</tr>
				<tr>
					<td  class="label">联系电话：</td>
					<td>
						<input type="text" id="telephoneNo" name="telephoneNo"
							style="width: 200px;" class="easyui-textinput validate[maxSize[20]]" 
							value="${userInfo.LXDH}" />
					</td>
				</tr>
				<tr>
					<td  class="label">移动电话：</td>
					<td>
						<input type="text" id="mobileNo" name="mobileNo"
							style="width: 200px;" class="easyui-textinput validate[maxSize[20]]" 
							value="${userInfo.YDDH}" />
						<span id="checkMobileNo" class="star"></span>
					</td>
				</tr>
				<tr>
					<td  class="label">单位邮箱：</td>
					<td>
						<input type="text" name="email" name="email"
							style="width: 200px;" class="easyui-textinput validate[maxSize[20],custom[email]]" 
							value="${userInfo.DZYX}" />
						<span id="checkEmail" class="star"></span>
					</td>
				</tr>
				<tr>
					<td  class="label"  class="label">用户级别：</td>
					<td>
						<select id="userLevel" name="userLevel" class="easyui-combobox validate[required]" dropdownHeight="125" style="width: 200px;" selValue="${userInfo.YHJB}">
								<option value="">---请选择---</option>
								<c:if test="${YHJB eq '1'}">
									<option value="1">省级</option>
								</c:if>
								<c:if test="${YHJB <= '2'}">
							    	<option value="2">市级</option>
							    </c:if>
							    <c:if test="${YHJB <= '3'}">
							    <option value="3">县级</option>
							    </c:if>
							    <c:if test="${YHJB <= '4'}">
							    	<option value="4">派出所</option>
							    </c:if>
						</select>
						<span class="star">*</span>
					</td>
				</tr>
				<tr>
					<td class="label">用户类别：</td>
					<td>
						<select id="userType" name="userType"
							class="easyui-combobox validate[required]" style="width: 200px;" dropdownHeight="75" selValue="${userInfo.YHLX}">
								<option value="">---请选择---</option>
								<option value="1">普通</option>
								<option value="0">高级</option>
						</select>
						<span class="star">*</span>
					</td>
				</tr>
				<tr>
					<td class="label">使用标识：</td>
					<td>
						<input type="radio" name="useTarg" value="1" class="easyui-radiobox"
							<c:if test="${userInfo.SYBZ == '1'}"> checked="checked" </c:if>
							<c:if test="${operModel == 'ADD'}"> checked="checked" </c:if> />有效
						<input type="radio" name="useTarg" value="0" class="easyui-radiobox"
						<c:if test="${userInfo.SYBZ == '0'}"> checked="checked" </c:if> />禁用
					</td>
				</tr>
				<tr>
					<td class="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
					<td><textarea class="easyui-textarea validate[maxSize[500]]" id="remark" name="remark" style="width:360px;height:100px" promptPosition="bottom">${userInfo.BZ}</textarea>
					</td>
				</tr>
			</table>
		</form>
	</ui:Panel>
</body>
</html>
