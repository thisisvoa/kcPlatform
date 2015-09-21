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
<style type="text/css">
.navicon {
	padding: 15px 0 15px 0;
	cursor: pointer;
	cursor: hand;
	width: 240px;
	height:70px;
	border:solid 1px #fff;
}

.navicon img {
	width: 64px;
	height: 64px;
}

.navicon_left {
	float: left;
	width: 64px;
}

.navicon_right {
	float: left;
	padding: 0 0 0 8px;
	line-height: 180%;
	width: 150px;
}
.navicon_right_title{
	font-weight:bold;
	color:#0460b7; /*标题文字颜色*/
}
.navicon_hover{
	background-color:#e7f4fe!important; /*鼠标移入背景色*/
	border:solid 1px #438dce!important; /*鼠标移入边框色*/
}
</style>
</head>
<script type="text/javascript">
	$parsed(function(){
		$(".navicon").hover(function(){
			$(this).addClass("navicon_hover");
		},function(){
			$(this).removeClass("navicon_hover");
		});
		$("#workplace").hide();
	});
	
	function toShowPage(url,title){
		$("#workplace").show();
		$("#workFrame").attr("src",url);
		$("#nav").hide();
		$("#title").html(title);
	}
	function returnNav(){
		$("#nav").show();
		$("#workplace").hide();
	}
</script>
<body>
	<ui:LayoutContainer fit="true" id="workplace">
		<ui:Layout region="north" style="height:30px;padding-top:5px;padding-right:5px;background-color:#F8F8F8" border="false">
			<div style="float:left">
				<div class="icon-qbzc" style="width:16px;height:16px;float:left"></div>
				权限信息管理>><span id="title"></span>
			</div>
			<a href="#" iconCls="icon-back" style="float:right" class="easyui-linkbutton" onclick="returnNav()">返回</a>
		</ui:Layout>
		<ui:Layout region="center" style="overflow:hidden" border="false">
			<iframe id="workFrame" style="width:100%;height:100%" frameBorder=0 src=""></iframe>
		</ui:Layout>
	</ui:LayoutContainer>
	<div id="nav" style="width: 100%;">
		<table style="text-align: left;vertical-align: top;width:100%">
			<tbody>
				<tr height=140>
					<kpm:HasPermission permCode="SYS_ORG_ROLE_ASSIGN">
					<td width=240>
						<div class="navicon" onclick="toShowPage('${ctx}/orgRole/assignRoleByOrg.html','按单位分配角色')">
							<div class="navicon_left">
								<img style="width: 64px; height: 64px"
									src="${ctx}/ui/css/icon/images/Users.png">
							</div>
							<div class="navicon_right">
								<div class=navicon_right_title>按单位分配角色</div>
								<div class=navicon_right_con>
									将单位作为分配对象，把角色分配给单位
								</div>
							</div>
							<div class="clear"></div>
						</div>
					</td>
					</kpm:HasPermission>
					<kpm:HasPermission permCode="SYS_USER_ROLE_ASSIGN">
					<td width=240>
						<div class="navicon" onclick="toShowPage('${ctx}/userRole/assignRoleByUser.html','按用户分配角色')">
							<div class=navicon_left>
								<img style="width: 64px; height: 64px"
									src="${ctx}/ui/css/icon/images/User_male.png">
							</div>
							<div class=navicon_right>
								<div class=navicon_right_title>按用户分配角色</div>
								<div class=navicon_right_con>
									将用户作为分配对象，把角色分配给用户
								</div>
							</div>
							<div class=clear></div>
						</div>
					</td>
					</kpm:HasPermission>
					<kpm:HasPermission permCode="SYS_ROLE_USER_ASSIGN">
					<td width=240>
						<div class="navicon" onclick="toShowPage('${ctx}/userRole/assignUserByRole.html','按角色分配用户')">
							<div class=navicon_left>
								<img style="width: 64px; height: 64px"
									src="${ctx}/ui/css/icon/images/Pencil.png">
							</div>
							<div class=navicon_right>
								<div class=navicon_right_title>按角色分配用户</div>
								<div class=navicon_right_con>
									将角色作为分配对象，把用户分配给用户
								</div>
							</div>
							<div class=clear></div>
						</div>
					</td>
					</kpm:HasPermission>
				</tr>
				<tr height=140>
					<kpm:HasPermission permCode="SYS_ROLE_FUNC_ASSIGN">
					<td>
						<div class="navicon" onclick="toShowPage('${ctx}/roleFunc/assignFuncByRole.html','按角色分配功能')">
							<div class=navicon_left>
								<img style="width: 64px; height: 64px"
									src="${ctx}/ui/css/icon/images/Pin.png">
							</div>
							<div class=navicon_right>
								<div class=navicon_right_title>按角色分配功能</div>
								<div class=navicon_right_con>
									将角色作为分配对象，把功能分配给用户
								</div>
							</div>
							<div class=clear></div>
						</div>
					</td>
					</kpm:HasPermission>
					<kpm:HasPermission permCode="SYS_FUNC_ROLE_ASSIGN">
					<td>
						<div class="navicon" onclick="toShowPage('${ctx}/roleFunc/assignRoleByFunc.html','按功能分配角色')">
							<div class=navicon_left>
								<img style="width: 64px; height: 64px"
									src="${ctx}/ui/css/icon/images/Gears.png">
							</div>
							<div class=navicon_right>
								<div class=navicon_right_title>按功能分配角色</div>
								<div class=navicon_right_con>
									将功能作为分配对象，把角色分配给用户
								</div>
							</div>
							<div class=clear></div>
						</div>
					</td>
					</kpm:HasPermission>
					<td>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>