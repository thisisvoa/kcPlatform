
<%@ tag import="com.kcp.platform.util.StringUtils" %>
<%@ tag pageEncoding="UTF-8" %>

<%@ attribute name="id" required="true" rtexprvalue="true" description="组件id" %>
<%@ attribute name="params" required="false" rtexprvalue="true" description="要传递给FlexSwf的参数" %>
<%@ attribute name="height" required="false" rtexprvalue="true" description="高度，像素或百分比"%>
<%@ attribute name="width" required="false" rtexprvalue="true" description="宽度，像素或百分比" %>
<%@ attribute name="align" required="false" rtexprvalue="true" description="对齐方式" %>
<%@ attribute name="filePath" required="true" rtexprvalue="true" description="待加载的FlexSwf文件路径" %>
<%
    String serverURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    request.setAttribute("serverURL", serverURL);
%>
    <script type="text/javascript">
	    var swfVersionStr = "11.1.0";
	    var xiSwfUrlStr = "${serverURL}/flex/playerProductInstall.swf";
	    var flashvars = {};
	    var params = {};
	    params.quality = "high";
	    params.bgcolor = "#ffffff";
	    params.allowscriptaccess = "sameDomain";
	    params.allowfullscreen = "true";
	    var attributes = {};
	    attributes.id = "${id}";
	    attributes.name = "${id}";
	    attributes.align = "middle";
	    swfobject.embedSWF(
	        "${serverURL}${filePath}", "flashContent", 
	        "${width}", "${width}", 
	        swfVersionStr, xiSwfUrlStr, 
	        flashvars, "serverURL=${serverURL}&${params}", attributes);
    </script>
	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
				id="${id}" width="${width}" height="${width}"
				codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
				<param name="movie" value="${serverURL}${filePath}" />
				<param name="quality" value="high" />
				<param name="bgcolor" value="#869ca7" />
				<param name="allowScriptAccess" value="sameDomain" />
				<param name="flashVars" value="serverURL=${serverURL}&${params}" />
				<param name="wmode" value="transparent" />
				<embed src="${serverURL}${filePath}" quality="high" bgcolor="#ffffff"
					width="100%" height="100%" name="Monitor" align="middle"
					play="true"
					loop="false"
					quality="high"
					allowScriptAccess="sameDomain"
					type="application/x-shockwave-flash"
					flashVars="serverURL=${serverURL}&${params}"
					pluginspage="http://www.adobe.com/go/getflashplayer">
				</embed>
	</object>