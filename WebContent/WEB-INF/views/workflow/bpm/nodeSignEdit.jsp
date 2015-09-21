
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
	var topWin = FrameHelper.getTop();
	$parsed(function(){
	});
	function doSave(){
		if(validate()){
			var parms = $("#editForm").serialize();
			$.postc("${ctx}/workflow/bpmDef/nodeSign/save.html", parms, function(data){
				MessageUtil.show("保存成功！");
				parentDialog.markUpdated();
			});
		}
	}
	function validate(){
		var decideType = $("#decideType").combobox("getValue");
		if(decideType==""){
			MessageUtil.alert("请选择决策方式!");
			return false;
		}
		var voteAmount = $("#voteAmount").val();
		if(voteAmount.trim()==""){
			MessageUtil.alert("票数不能为空!");
			return false;
		}
		var re = /^[1-9]+[0-9]*]*$/;
		if(!re.test(voteAmount)){
			MessageUtil.alert("票数必须为正整数!");
			return false;
		}
		return true;
	}
	function userTypeChange(obj){
		var cmpIds = $(obj).parent().next("td").children("input[name='cmpIds']");
		var cmpNames = $(obj).parent().next("td").children("textarea[name='cmpNames']");
		var chooseBtn = $(obj).parent().next("td").children("input[name='choose']");
		var clearBtn = $(obj).parent().next("td").children("input[name='clear']");
		var userType = $(obj).combobox("getValue");
		cleanCmp(obj);
		switch(userType){
			case "":
				cmpNames.hide();
				chooseBtn.button("hide");
				clearBtn.button("hide");
				break;
			case "1":
				cmpNames.hide();
				chooseBtn.button("hide");
				clearBtn.button("hide");
				break;
			case "2":
				cmpNames.attr("readonly", true);
				cmpNames.show();
				chooseBtn.button("show");
				clearBtn.button("show");
				break;
			case "3":
				cmpNames.attr("readonly", true);
				cmpNames.show();
				chooseBtn.button("show");
				clearBtn.button("show");
				break;
			case "4":
				cmpNames.attr("readonly", true);
				cmpNames.show();
				chooseBtn.button("show");
				clearBtn.button("show");
				break;
			case "5":
				cmpNames.attr("readonly", true);
				cmpNames.show();
				chooseBtn.button("show");
				clearBtn.button("show");
				break;
			case "6":
				cmpNames.removeAttr("readonly");
				cmpNames.show();
				chooseBtn.button("hide");
				clearBtn.button("hide");
				break;
		}
	}
	
	/**清空**/
	function cleanCmp(obj){
		var cmpIds=$("input[name='cmpIds']",$(obj).parent().parent());
		var cmpNames=$("textarea[name='cmpNames']",$(obj).parent().parent());
		cmpIds.val('');
		cmpNames.val('');
	}
	/**选择**/
	function selectCmp(obj){
		var userType = $(obj).parent().prev('td').children("select[id='userType']").combobox("getValue");
		var cmpIds = $(obj).siblings("input[name='cmpIds']");
		var cmpNames = $(obj).siblings("textarea[name='cmpNames']");
		selectParticiPant(userType, cmpIds, cmpNames);
	}
	
	/**选择人员**/
	function selectParticiPant(userType, cmpIds, cmpNames){
		if(userType=='2'){
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
						cmpIds.val(ids);
						cmpNames.val(descs);
					}else{
						cmpIds.val("");
						cmpNames.val("");
					}
				}
			});
			diag.setData({selectedUser:cmpIds.val()});
			diag.show();
		}else if(userType=='3'){
			var diag = new topWin.Dialog({
				URL : "${ctx}/roleCtl/cmp/roleSelector.html",
				Width: 700,
				Height: 450,
				Title : "角色选择器",
				RefreshHandler : function(){
					var selectedRoleList = diag.getData().selectedRoleList;
					if(selectedRoleList!=null){
						var ids = "";
						var descs = "";
						var spliter='';
						for(var i=0;i<selectedRoleList.length;i++){
							ids += spliter+selectedRoleList[i].zjid;
							descs += spliter+selectedRoleList[i].jsmc;
							spliter=',';
						}
						cmpIds.val(ids);
						cmpNames.val(descs);
					}else{
						cmpIds.val("");
						cmpNames.val("");
					}
				}
			});
			diag.setData({selectedRole:cmpIds.val()});
			diag.show();
		}else if(userType=='4'){
			var diag = new topWin.Dialog({
				URL : "${ctx}/orgCtl/cmp/orgSelector.html",
				Width: 900,
				Height: 450,
				Title : "单位选择器",
				RefreshHandler : function(){
					var selectedOrgList = diag.getData().selectedOrgList;
					if(selectedOrgList!=null){
						var ids = "";
						var descs = "";
						var spliter='';
						for(var i=0;i<selectedOrgList.length;i++){
							ids += spliter+selectedOrgList[i].ZJID;
							descs += spliter+selectedOrgList[i].DWMC;
							spliter=',';
						}
						cmpIds.val(ids);
						cmpNames.val(descs);
					}else{
						cmpIds.val("");
						cmpNames.val("");
					}
				}
			});
			diag.setData({selectedOrg:cmpIds.val()});
			diag.show();
		}else if(userType=='5'){
			var diag = new topWin.Dialog({
				URL : "${ctx}/workflow/bpmDef/taskNodeSelector.html?actDefId=${nodeSign.actDefId}&nodeId=${nodeSign.nodeId}",
				Width: 700,
				Height: 450,
				Title : "流程任务节点选择器",
				RefreshHandler : function(){
					var selectedNode = diag.getData().selectedNode;
					if(selectedNode!=null){
						cmpIds.val(selectedNode.id);
						cmpNames.val(selectedNode.name);
					}else{
						cmpIds.val("");
						cmpNames.val("");
					}
				}
			});
			diag.show();
		}
	}
</script>
<body>
<ui:Panel fit="true">
	<div id="toolbar" class="easyui-toolbar" style="width: 100%;">
		<a id="saveBtn" href="#" iconCls="icon-save" onclick="doSave()">保存</a>
		<a id="close" href="#" iconCls="icon-remove" onclick="javascript:parentDialog.close()">关闭</a>
	</div>
	<form id="editForm" class="easyui-validate" style="margin-top:2px;">
		<input type="hidden" name="signId" value="${nodeSign.signId}" />
		<input type="hidden" id="nodeId" name="nodeId" value="${nodeSign.nodeId}"/>
		<input type="hidden" id="actDefId" name="actDefId" value="${nodeSign.actDefId}" />
		<ui:TabNavigator border="false">
			<ui:Tab id="tab1" title="投票规则设置">
				<table class="tableView">
					<tr>
						<td class="label" style="width:100px">
							决策方式：
						</td>
						<td>
							<select id="decideType" name="decideType" class="easyui-combobox validate[required]" selValue="${nodeSign.decideType}" dropdownHeight="75"> 
								<option value="">--请选择--</option>
								<option value="1">通过</option>
								<option value="2">拒绝</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="label" style="width:100px">
							后续处理模式: 
						</td>
						<td>
							<c:choose>
								<c:when test="${nodeSign.signId==null}">
									<input type="radio" value="1" class="easyui-radiobox validate[required]" name="flowMode" checked="checked" />直接处理
									<input type="radio" value="2" class="easyui-radiobox validate[required]" name="flowMode"  />等待所有人投票
								</c:when>
								<c:otherwise>
									<input type="radio" value="1" name="flowMode" class="easyui-radiobox validate[required]"  <c:if test="${nodeSign.flowMode==1}">checked="checked"</c:if>  />直接处理
									<input type="radio" value="2" name="flowMode" class="easyui-radiobox validate[required]"  <c:if test="${nodeSign.flowMode==2}">checked="checked"</c:if> />等待所有人投票
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td class="label" style="width:100px">
							投票类型：
						</td>
						<td>
							<c:choose>
								<c:when test="${nodeSign.signId==null}">
									<input type="radio" value="2" class="easyui-radiobox validate[required]" name="voteType" checked="checked"/>绝对票数
									<input type="radio" value="1" class="easyui-radiobox validate[required]" name="voteType"  />百分比
								</c:when>
								<c:otherwise>
									<input type="radio" value="2" class="easyui-radiobox validate[required]" name="voteType" <c:if test="${nodeSign.voteType==2}">checked="checked"</c:if> />绝对票数
									<input type="radio" value="1" class="easyui-radiobox validate[required]" name="voteType" <c:if test="${nodeSign.voteType==1}">checked="checked"</c:if>  />百分比
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td class="label" style="width:100px">
							票数：
						</td>
						<td>
							<input type="text" class="easyui-textinput validate[required,custom[integer]]" id="voteAmount" name="voteAmount" value="${nodeSign.voteAmount}" />
							<span style="color:#ccc;margin-left:2px;"> 若投票类型为绝对票数输入整数票数。若投票类型为百分比输入50则表示50%。</span>
						</td>
					</tr>
				</table>
			</ui:Tab>
			<ui:Tab id="tab2" title="特权功能设置" style="width:100%;height:100%">
				<table class="tableView" style="margin-top:2px">
					<tr>
						<td style="width:100px;height:20px;" class="ui-state-default" align="center">
							特权类型
						</td>
						<td style="width:150px;height:20px;" class="ui-state-default">
							用户类型
						</td>
						<td style="height:20px;" class="ui-state-default">
							用户来自
						</td>
					</tr>
					<c:forEach items="${modeList}" var="text" varStatus="i">
						<c:set var="bpmNodePrivilege" value="${bpmNodePrivilegeList[i.index]}"/>
						<tr style="height:65px"">
							<td>
								${text}
							</td>
							<td>
								<select id="userType" name="userType" class="easyui-combobox" dropdownHeight="120" 
									onChange="userTypeChange" selValue="${bpmNodePrivilege.userType}">
										<option value="">--请选择--</option>
										<option value="2">用户</option>
										<option value="3">角色</option>
										<option value="4">单位</option>
										<option value="6">脚本</option>
								</select>
							</td>
							<td>
								<input type="hidden" name="cmpIds" value='${bpmNodePrivilege.cmpIds}'/>
								<c:choose>
									<c:when test="${bpmNodePrivilege==null}">
										<textarea name="cmpNames" class="easyui-textarea" style="height:60px;width:400px;display:none" readonly="readonly">${bpmNodePrivilege.cmpNames}</textarea>
										<input name="choose" type="button" value="选择..." class="easyui-button" hidden="true" onclick="selectCmp(this);" style="width:60px">
										<input name="clear" type="button" value="清空" class="easyui-button" hidden="true" onclick="cleanCmp(this);" style="width:60px">
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${bpmNodePrivilege.userType==1}">
												<textarea name="cmpNames" class="easyui-textarea" style="height:60px;width:400px;" readonly="readonly">${bpmNodePrivilege.cmpNames}</textarea>
												<a class="easyui-linkbutton" onclick="selectCmp(this);">选择...</a>
												<input name="choose" type="button" value="选择..." class="easyui-button" hidden="true" onclick="selectCmp(this);" style="width:60px">
											</c:when>
											<c:when test="${bpmNodePrivilege.userType==6}">
												<textarea name="cmpNames" class="easyui-textarea" style="height:60px;width:400px;" >${bpmNodePrivilege.cmpNames}</textarea>
												<input name="choose" type="button" value="选择..." class="easyui-button" onclick="selectCmp(this);" hidden="true" style="width:60px">
											</c:when>
											<c:otherwise>
												<textarea name="cmpNames" class="easyui-textarea" style="height:60px;width:400px;" readonly="readonly">${bpmNodePrivilege.cmpNames}</textarea>
												<input name="choose" type="button" value="选择..." class="easyui-button" onclick="selectCmp(this);" style="width:60px">
											</c:otherwise>
										</c:choose>
										<input name="clear" type="button" value="清空" class="easyui-button" onclick="cleanCmp(this);" style="width:60px">
									</c:otherwise>
								</c:choose>
								
								
							</td>
						</tr>
					</c:forEach>
				</table>
			</ui:Tab>
		</ui:TabNavigator>
	</form>
</ui:Panel>
</body>
</html>