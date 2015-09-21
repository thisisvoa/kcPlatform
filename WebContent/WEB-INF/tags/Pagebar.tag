<!-- 
	分页标签
 -->
<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="searchForm" type="java.lang.String" required="true"  description="查询Form的ID"%>
<%@ attribute name="pageSizeSelectList" type="java.lang.Number[]" required="false"  description="每页几条记录下拉框"%>
<%@ attribute name="isShowPageSizeList" type="java.lang.Boolean" required="false" description="是否显示每页几条记录下拉框"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	jspContext.setAttribute("searchForm", searchForm);
	jspContext.setAttribute("page", request.getAttribute("_PAGEINFO_"));
	
	Integer[] defaultPageSizes = new Integer[]{10,50,100};
	if(jspContext.getAttribute("pageSizeSelectList") == null) {
		jspContext.setAttribute("pageSizeSelectList",defaultPageSizes); 
	}
	
	if(jspContext.getAttribute("isShowPageSizeList") == null) {
		jspContext.setAttribute("isShowPageSizeList",true); 
	}
%>
<c:if test="${page != null}">
	<script type="text/javascript">
		var PAGE_NO_KEY = "_PAGE_NO_";
		var PAGE_SIZE_KEY = "_PAGE_SIZE_";
		
		function PageTool(formId,pageNumber,pageSize){
			this.formId = formId;
			this.pageNumber = pageNumber;
			this.pageSize = pageSize;
		}
		PageTool.prototype = {
			doJump : function(pageNumber,pageSize) {
				this.pageNumber = pageNumber;
				this.pageSize = pageSize;
				var pair = function(k,v) {return ' <input type="hidden" name="'+k+'" value="'+v+'" />'};
				$(pair(PAGE_NO_KEY,this.pageNumber)).appendTo('#'+this.formId);
				$(pair(PAGE_SIZE_KEY,this.pageSize)).appendTo('#'+this.formId);
				$('#'+this.formId).submit();
			},
			
			togglePage : function(pageNumber) {
				this.doJump(pageNumber,this.pageSize);
			},
			
			togglePageSize : function(pageSize) {
				this.doJump(this.pageNumber,pageSize);
			}
		}
		
		var _pageTool = null;
		$(function(){
			_pageTool = new PageTool("${searchForm}",${page.page}, ${page.pageSize});
		});
	</script>
	
	<div class="float_left padding_top4">
		<!-- 上一页按钮-->
		<c:choose>
			<c:when test="${page.hasPrePage}">
				<span><a href="javascript:_pageTool.togglePage(${page.prePage});">上一页</a></span>
			</c:when>
			<c:otherwise>
				<span class="paging_disabled"><a href="javascript:;">上一页</a></span>
				
			</c:otherwise>
		</c:choose>
		
		<!-- 小于等于7页，全部显示 -->
		<c:if test="${page.totalPages<=7}">
			<c:forEach begin="1" end="${page.totalPages}" var="n">
				<c:if test="${page.page==n}">
					<span class="paging_current"><a href="javascript:;">${n}</a></span>
				</c:if>
				<c:if test="${page.page!=n}">
					<span><a href="javascript:_pageTool.togglePage(${n});">${n}</a></span>
				</c:if>
			</c:forEach>
		</c:if>
		<!-- 大于7页，连续5页? + 首页和末页 -->
		<c:if test="${page.totalPages>7}">
			<!-- 首页 -->
			<c:if test="${page.page==1}">
				<span class="paging_current"><a href="javascript:;">1</a></span>
			</c:if>
			<c:if test="${page.page!=1}">
				<span><a href="javascript:_pageTool.togglePage(1);">1</a></span>
			</c:if>
			<!-- 连续5页，三种情况 -->
			<!-- 靠近首页 -->
			<c:if test="${page.page<4}">
				<c:forEach begin="2" end="5" var="n">
					<c:if test="${page.page==n}">
						<span class="paging_current"><a href="javascript:;">${n}</a></span>
					</c:if>
					<c:if test="${page.page!=n}">
						<span><a href="javascript:_pageTool.togglePage(${n});">${n}</a></span>
					</c:if>
				</c:forEach>
			</c:if>
			
			<c:if test="${page.page>4}">
				<span>...</span>
			</c:if>
			<!-- 处于中间 -->
			<c:if test="${(page.page>=4) && (page.page<=(page.totalPages-3))}">
				<c:forEach begin="${page.page-2}" end="${page.page+2}" var="n">
					<c:if test="${page.page==n}">
						<span class="paging_current"><a href="javascript:;">${n}</a></span>
					</c:if>
					<c:if test="${page.page!=n}">
						<span><a href="javascript:_pageTool.togglePage(${n});">${n}</a></span>
					</c:if>
				</c:forEach>
			</c:if>
			<c:if test="${page.page<(page.totalPages-3)}">
				<span>...</span>
			</c:if>
			<!-- 靠近最后 -->
			<c:if test="${page.page>(page.totalPages-3)}">
				<c:forEach begin="${page.totalPages-4}" end="${page.totalPages-1}" var="n">
					<c:if test="${page.page==n}">
						<span class="paging_current"><a href="javascript:;">${n}</a></span>
					</c:if>
					<c:if test="${page.page!=n}">
						<span><a href="javascript:_pageTool.togglePage(${n});">${n}</a></span>
					</c:if>
				</c:forEach>
			</c:if>
			<!-- 最后一页 -->
			<c:if test="${page.page==page.totalPages}">
				<span class="paging_current"><a href="javascript:;">${page.totalPages}</a></span>
			</c:if>
			<c:if test="${page.page!=page.totalPages}">
				<span><a href="javascript:_pageTool.togglePage(${page.totalPages});">${page.totalPages}</a></span>
			</c:if>
		</c:if>
		
		<!-- 下一页按钮-->
		<c:choose>
			<c:when test="${page.hasNextPage}">
				<span><a href="javascript:_pageTool.togglePage(${page.nextPage});">下一页</a></span>
			</c:when>
			<c:otherwise>
				<span class="paging_disabled"><a href="javascript:;">下一页</a></span>
			</c:otherwise>
		</c:choose>
		
	</div>
	<div class="float_left"></div>
	
	<div class="float_left padding_top4">共 ${page.totalItems} 条记录</div>
	
	<c:if test="${isShowPageSizeList}">
		<select autoWidth="true" id="pageSizeList" onChange="_pageTool.togglePageSize($('#pageSizeList').val())" style="width:50px">
			<c:forEach var="item" items="${pageSizeSelectList}">
				<option value="${item}" ${page.pageSize == item ? 'selected' : '' }>${item}</option>
			</c:forEach> 
		</select>
	</c:if>
	
	<div class="clear"></div>
</c:if>