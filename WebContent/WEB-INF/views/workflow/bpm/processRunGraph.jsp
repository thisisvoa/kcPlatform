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
<script type="text/javascript" src="${ctx}/ui/js/plugins/jquery.qtip.js" ></script>
<link href="${ctx}/ui/css/default/jquery.qtip.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/ui/js/util/easyTemplate.js"></script>
<style type="text/css">
div.header{
	background-image: url(${ctx}/styles/default/images/tool_bg.jpg);
	height:24px;
	line-height:24px;
	font-weight: bold;
	font-size: 14px;
	padding-left: 5px;
	margin: 0;
	width: 394px;
}
div .legend {
	width:14px;
	height:14px;
	border: 1px solid black;
	float: left;
}

.table-task {
	margin: 0 auto;
	width:210px;
	border-collapse: collapse;
}

.target{
	height:20px;
	float: left;
	margin:10px;
}
div.icon{
	border:1px solid #000;
	line-height: 10px;
	width: 15px;
	height:15px;
	float: left;
	overflow: hidden;
}
.target span{
	margin: 0 0 0 5px;
	font-size: 14px;
	font-weight: bold;
	float:left;
	vertical-align: middle;
	white-space: nowrap;
}
</style>
<script type="text/javascript">
var processInstanceId="${processInstanceId}";
var isStatusLoaded=false;
var _height=${shapeMeta.height};
var topWin = FrameHelper.getTop();
$parsed(function(){
	$.each($("div.flowNode"),function(){
		var obj=$(this);
		var nodeId=$(this).attr('id');
		if(obj.attr('type')=='userTask' || obj.attr('type')=='multiUserTask'){
			obj.css('cursor','pointer');
			//只有用户任务和会签任务显示节点。
			checkStatusInfo(nodeId);
		}
	});
});

//初始化qtip
function checkStatusInfo(nodeId){
	var html =  getTableHtml(nodeId);
	if(html){
		var isIE6 = Utils.isIE6();
		$("#"+nodeId).qtip({
			content:{
				text:function(){
					var html =  getTableHtml(nodeId);
					if(html){
						return html;
					}
					else{
						return "<span style='color:red;line-height:24px;'>未执行</span>";
					}
				},
				title:{
					text: "任务执行情况"			
				}
			},
	        position: {
	        	at:'center',
	        	target:'event',
	        	adjust: {
	        		x:-15,
	        		y:-15
	  				},  
	  				/* viewport: isIE6?"":$(window) */
					viewport:  $(window)
	        },
	        show:{   			        	
		     	effect: function(offset) {
					$(this).slideDown(200);
					$("a[candidateUserUrl]").each(showResult);
				}
	        }, 
	        hide: {
	        	event:'mouseleave',
	        	fixed:true,
	        	delay:300
	    	},  
	        style: {
	       	  classes:'ui-tooltip-light ui-tooltip-shadow',
	          width : isIE6? 279 : ""
	        } 			    
		});
	}
	
};

//构建显示的html
function getTableHtml(nodeId){	
	var node = loadStatus(nodeId);
	if (!node)
		return false;
	var taskOpinionList = node.taskOpinionList;
	var taskExecutorList = node.taskExecutorList;
	var lastCheckStatus = node.lastCheckStatus;
	var height = Utils.isIE6()?"240px":"";
	var html = [ '<div style="max-height:240px;_height:'+height+';width:260px;overflow-y:auto;overflow-x:hidden;">' ];
	if (lastCheckStatus != "-2") {  //正在执行的节点
		if (taskOpinionList==null || taskOpinionList.length == 0) {
			return false;
		} else {
			var tableHtml = $("#txtTaskStatus").val();
			var str = easyTemplate(tableHtml, node);
			html.push(str);
			html.push('</div>');
		}
	}else if(lastCheckStatus == "-2"){ //未执行的节点
		if (taskExecutorList.length == 0) {
			return false;
		} else {
			var tableHtml = $("#txtTaskStatusExecutors").val();
			var str = easyTemplate(tableHtml, node);
			html.push(str);
			html.push('</div>');
		}
	}

	return html.join('');
}
var taskNodeStatus={};
//加载流程状态数据。
function loadStatus(nodeId){
	var status = taskNodeStatus[nodeId];
	var dt = (new Date()).getTime();
	if(!status && status!=""){
		var url="${ctx}/workflow/bpm/processRun/getFlowStatus.html";
		var params={
				instanceId:processInstanceId,
				nodeId:nodeId,
				dt:dt
			};
		$.ajax({
			url:url,
			async:false,
			data:params
		}).done(function(result){
			status=result;
		});
		taskNodeStatus[nodeId]=status;
	}
	return status;
};

function showResult(){
	var targetUrl = $(this).attr("candidateUserUrl") ;
	var template=$("#txtReceiveTemplate").val();
	$(this).qtip({  
		content:{
			text:$lang.tip.loading,
			ajax:{
				url:targetUrl,
				type:"GET",
				success:function(data,status){
					var html=easyTemplate(template,data).toString();
					this.set("content.text",html);
				}
			},
			title:{
				text:"执行人列表"			
			}
		},
        position: {
        	at:'top left',
        	target:'event',
			viewport:  $(window)
        },
        show:{
        	event:"focus mouseenter"
        },   			     	
        hide: {
        	event:'unfocus mouseleave',
        	fixed:true
        },  
        style: {
       	  classes:'ui-tooltip-light ui-tooltip-shadow'
        }    
 	});	
}

function showUser(userId){
	topWin.Dialog.open({
			URL : "${ctx}/userCtl/toViewUser.html?id="+userId,
			Width: 500,
		Height : 540,
			Title : "用户详细信息"
		});
}

function showOrg(orgId){
	topWin.Dialog.open({
			URL : "${ctx}/orgCtl/orgUserInfo.html?id="+orgId,
			Width: 800,
		Height : 600,
			Title : "单位用户信息"
		});
}

function showRole(roleId){
	topWin.Dialog.open({
			URL : "${ctx}/roleCtl/roleUserInfo.html?id="+roleId,
			Width: 800,
		Height : 600,
			Title : "角色用户信息"
		});
}
</script>
</head>
<body>
	<ui:Panel border="false" fit="true">
		<div>
			<div class="target">
				<div class="icon" style="background:gray;"></div>
				<span>未执行</span>
			</div>
			<div class="target">
				<div class="icon" style="background:#F89800;"></div>
				<span>提交</span>
			</div>
			<div class="target">
				<div class="icon" style="background:#FFE76E;"></div>
				<span>重新提交</span>
			</div>
			<div class="target">
				<div class="icon" style="background:#00FF00;"></div>
				<span>同意</span>
			</div>
			<div class="target">
				<div class="icon" style="background:orange;"></div>
				<span>弃权</span>
			</div>
			<div class="target">
				<div class="icon" style="background:red;"></div>
				<span>当前节点</span>
			</div>
			<div class="target">
				<div class="icon" style="background:blue;"></div>
				<span>反对</span>
			</div>
			<div class="target">
				<div class="icon" style="background:#8A0902;"></div>
				<span>驳回</span>
			</div>
			<div class="target">
				<div class="icon" style="background:#023B62;"></div>
				<span>撤销</span>
			</div>
			<div class="target">
				<div class="icon" style="background:#338848;"></div>
				<span>会签通过</span>
			</div>
			<div class="target">
				<div class="icon" style="background:#82B7D7;"></div>
				<span>会签不通过</span>
			</div>
			<div class="target">
				<div class="icon" style="background:#EEAF97;"></div>
				<span>人工终止</span>
			</div>
			<div class="target">
				<div class="icon" style="background:#C33A1F;"></div>
				<span>完成</span>
			</div>
		</div>
		<div style="padding-top:20px;clear:both;position: relative;">
			<div style="margin-bottom: 5px; "><b>说明：</b>点击任务节点可以查看节点的执行人员。
				<c:if test="${superInstanceId != null}">
					<a class="link setting" onclick="ViewSuperExecutionFlowWindow({'actInstanceId':'${superInstanceId}'})">查看主流程</a>
				</c:if>
			</div>
			<div id="divTaskContainer" style="margin:0 auto;margin-left:1px; position: relative;background:url('${ctx}/workflow/bpmImage.html?processInstanceId=${processInstanceId}&randId=<%=Math.random()%>') no-repeat;width:${shapeMeta.width}px;height:${shapeMeta.height}px;">
				${shapeMeta.xml}
			</div>
		</div>
	</ui:Panel>
	<textarea id="txtTaskStatus" style="display:none">
			<#list data.taskOpinionList as obj>
				<table class="tableView" style="width: 96%;">
				<tr><td class="label" style="width:60px;">任务名称: </td>
				<td>\${obj.taskName}</td></tr>
				<#if (obj.checkStatus == -1)> <!-- 正在审批 -->
					<tr>
						<td class="label" style="width:60px;">执行人: </td>
						<#if (obj.taskExeStatus==null)>
							<td></td>
						<#else>
							<td><a style="color:blue" href="javascript:showUser('\${obj.taskExeStatus.executorId}');">\${obj.taskExeStatus.executor}</a>\${obj.taskExeStatus.read==true?"(<font color='green'>已读</font>)":"(<font color='red'>未读</font>)"}</td>
						</#if>
					</tr>
					<tr>
						<td class="label" style="width:60px;">候选人: </td>
						<#if (obj.candidateUserStatusList==null)>
							<td></td>
						<#else>
							<td>
								<#list obj.candidateUserStatusList as candidateUserStatus>
									<#if (candidateUserStatus.type=="user")>
									<a style="color:blue" href="javascript:showUser('\${candidateUserStatus.executorId}');">\${candidateUserStatus.candidateUser}</a>
										<span>\${candidateUserStatus.read==true?"(<font color='green'>已读</font>)":"(<font color='red'>未读</font>)"} </span><br/>
									<#else>
										<#if (candidateUserStatus.type=="org")>
											<span><a style="color:blue" href="javascript:showOrg('\${candidateUserStatus.executorId}');">\${candidateUserStatus.candidateUser}</a>(组织)</span><br/>
										<#elseif (candidateUserStatus.type=="role")>
											<span><a style="color:blue" href="javascript:showRole('\${candidateUserStatus.executorId}');">\${candidateUserStatus.candidateUser}</a>(角色)</span><br/>
										</#if>
									</#if>
								</#list>
							</td>
						</#if>
					</tr>
				<#else>
					<tr>
						<td class="label" style="width:60px;">执行人: </td>
						<td><a style="color:blue"  href="javascript:showUser('\${obj.exeUserId}');">\${obj.exeUserName}</a></td>
					</tr>
				</#if>
				<tr><td class="label" style="width:60px;">开始时间: </td>
				<td>\${obj.startTimeStr}</td></tr>
				
				<tr><td class="label" style="width:60px;">结束时间: </td>
				<td>\${obj.endTimeStr}</td></tr>
				
				<tr><td class="label" style="width:60px;">时长: </td>
				<td>\${obj.durTimeStr}</td></tr>
				
				<tr><td class="label" style="width:60px;">状态: </td>
				<td>\${obj.status}</td></tr>
				
				<tr><td class="label" style="width:60px;">意见: </td>
				<td>\${obj.opinion==null?"":obj.opinion}</td></tr>
				</table><br>
			</#list>
		</textarea>
		<textarea id="txtTaskStatusExecutors" style="display:none">
			<div>执行状态 ： <span style='color:red;line-height: 24px;'>未执行</span> </div>
				<table class="tableView" cellpadding="0" cellspacing="0" border="0">
					<tr >
					<td class="label" style="width:60px;">执行人: </td>
						<td>
						<#list data.taskExecutorList as obj>
						<a style="color:blue" href="javascript:showUser('\${obj.executeId}');">\${obj.executor}</a></br>
						</#list>
						</td>
					</tr>
				</table>
		</textarea>
		<textarea id="txtReceiveTemplate"  style="display: none;">
		    <div  style="height:150px;width:150px;overflow:auto">
		  		<table class="tableView" cellpadding="0" cellspacing="0" border="0">
			  		<#list data as obj>
				  		<tr>
				  			<td class="label" style="width:60px;">\${obj_index+1}</td>
				  			<td>\${obj.yhmc}</td>
				  		</tr>
			  		</#list>
		  		</table>
		  	</div>
	   </textarea>
</body>
</html>