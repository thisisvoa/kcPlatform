<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<table class="tableView">
	<tr>
		<td class="label">意&nbsp;&nbsp;见：</td>
		<td>
			<textarea class="easyui-textarea validate[maxSize[300]]"  style="width:700px;height:60px" id="voteContent" name="voteContent" >${taskOpinion.opinion}</textarea>
		</td>
	</tr>
</table>