<!-- 
	分页标签
 -->
<%@tag import="com.kcp.platform.sys.security.web.tag.HasPermissionTagHelper"%>
<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="permCode" type="java.lang.String" required="true"  description="权限标识"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="<%=com.kcp.platform.sys.security.web.tag.HasPermissionTagHelper.hasPermission(permCode) %>">
	<jsp:doBody></jsp:doBody>
</c:if>