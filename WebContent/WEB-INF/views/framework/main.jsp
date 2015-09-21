<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="/tags/ui" %>
<%@ page import="com.casic27.platform.web.listener.OnLineListener" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/WEB-INF/includes/common.jsp"%>
<title>基础开发平台V1.0</title>
<script type=text/javascript src="${ctx}/ui/frame.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/ui/js/ddaccordion/skins/blue/style.css" id="compStyle"/>
<link rel="stylesheet" type="text/css" href="${ctx}/ui/css/style.css"/>
<script type="text/javascript" src="${ctx}/ui/js/ddaccordion/js/ddaccordion_split.js"></script>


<script language="javascript">
   var TOP_INDEX = 0;
 
   	function loginout(){
 	  	 MessageUtil.confirm("您确定要退出系统吗?",
 		function() {
 		  	window.location.href = "${ctx}/logout.html";
 				}, function() {
 				return;
 		});
 	}
	var index = 0;
	function addTab(id, name, url){
		if($('#rightNav').tabs("exists",id)){
			$('#rightNav').tabs("select",id);
			$('#rightNav').tabs('update',{
				tab:$('#rightNav').tabs('getSelected'),
				options:{
					content:"<iframe frameborder='0' src='"+url+"' style=\"width:100%;height:100%;\"/>"
				}
			});
		}else{
			$('#rightNav').tabs('add',{
				id:id,
				title:name,
				iconCls:'icon-tab',
				content:"<iframe frameborder='0' src='"+url+"' style=\"width:100%;height:100%;\"/>",
				closable:true
			});
		}
	}
	
	function toTab(id, title, src) {
		addTab(id, title, src);
	}
	
	function main(){
		$('#rightNav').tabs('add',{
			id:'menu2',
			title:"比对信息处理",
			content:"<iframe frameborder='0' src='${ctx}//jwry/jwryczMain.html?_PAGE_ABLE_=true&sfqs=N' style=\"width:100%;height:100%;\"/>",
			closable:true,
			iconCls:false
		});
		index++;
	}
	
	function getCurrentTime(){
		var weekDayLabels = new Array("星期日", "星期一", "星期二",
				"星期三", "星期四", "星期五", "星期六");
		var now = new Date();
		var year = now.getFullYear();
		var month = now.getMonth() + 1;
		var day = now.getDate();
		var currentime = year + "年" + month + "月" + day + "日 "
				+ weekDayLabels[now.getDay()];
		return currentime;
	}
	
	$(function(){
		$('#currentTime').text(getCurrentTime());
	});
	
</script>
</head>
<body class="easyui-layout">
	<div region="north" title="" split="false" border="false">
		<div id="top" style="height: 83px; padding: 0px;">
			<div id="top_logo"></div>
				<div class="bs_navright" style="color:white">
					<br/>
					<br/>
					<br/>
					<br/>
				 <!--    <span class="icon_home hand"><a target="frameRight" onclick="main();"><font color="white">首页</font></a></span>  --> 
				    <!-- 
				    <span class="icon_edit"><a onclick='top.Dialog.open({URL:"${ctx}/user/password.html",Width:380,Height:180,Title:"当前位置>>密码修改"});'>密码修改</a></span>
				     -->
					<span class="icon_no" onclick="loginout();" style="cursor:pointer">退出系统</span>
					<div class="clear"></div>
				</div>
		</div>
		<div class="bs_nav" style="height: 20px; padding: 1px; border:1px solid #a6b0ba;">
			<div class="bs_navleft">
				   	<span style="float: left;">今天是<span id="currentTime"></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
					<div class="clear"></div>
			</div>
			<div class="bs_navright">
					&nbsp;${ssdwmc}&nbsp;&nbsp;(${loginUser.yhmc})&nbsp;&nbsp;&nbsp;
					在线用户: &nbsp;${onlineCount},&nbsp;&nbsp;&nbsp;历史在线:&nbsp;${loginTotalCount}&nbsp;&nbsp;&nbsp;
				<div class="clear"></div>
			</div>
		</div>
	</div>
	<div region="west" split="true" title="导航菜单"
		style="width: 180px; padding: 1px;">
		<div>
			${menuHTML}
		</div>
	</div>
	<div region="center" style="overflow: hidden;">
		<div id="rightNav" class="easyui-tabs" fit="true" border="false"></div>
	</div>
</body>
</html>
