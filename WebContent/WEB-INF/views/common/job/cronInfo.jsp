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
	body{
		font-size:9pt
	}
	
	table{
		border-top:#c0c0c0 1px solid;
		border-left:#c0c0c0 1px solid;
	}
	table td{
		border-bottom:#c0c0c0 1px solid;
		border-right:#c0c0c0 1px solid;
	}
	table th{
		border-bottom:#c0c0c0 1px solid;
		border-right:#c0c0c0 1px solid;
	}
	p{
		padding:5px;
	}
</style>
</head>
<body>
	<ui:Panel fit="true">
		<div style="text-align: center">
			<h1>Cron 表达式说明</h1>
		</div>
		<div>
			<div>
				<p>Cron 表达式，可以匹配任意时间，它的规则如下：</p>
				<p>Cron 表达式包括以下 7 个字段（1 个可选）</p>
				<p>秒&nbsp; 分 小时 月内日期 月 周内日期 年（可选）</p>
				<p>表达式的每个数值域都是一个有最大值和最小值的集合，如：秒域和分钟域的集合是0-59，日期域是1-31，月份域是1-12。注意：秒、分、小时
					字段是从小到大排序的，这是西方人的习惯，所以在使用的时候要小心，不要颠倒过来。</p>
				<p>允许值及对应表见表1。</p>
				<p align="center">表1. Cron 表达式允许值及对应表</p>
				<div align="center">
					<table width="400" align="center" border="0" cellpadding="2"
						cellspacing="0">
						<tbody>
							<tr>
								<th valign="top" width="133">字段</th>
								<th valign="top" width="150">允许值</th>
								<th valign="top" width="116">允许的特殊字符</th>
							</tr>
							<tr>
								<td valign="top" width="133">秒</td>
								<td valign="top" width="150">0-59</td>
								<td valign="top" width="116">, - * /</td>
							</tr>
							<tr>
								<td valign="top" width="133">分</td>
								<td valign="top" width="150">0-59</td>
								<td valign="top" width="116">, - * /</td>
							</tr>
							<tr>
								<td valign="top" width="133">小时</td>
								<td valign="top" width="150">0-23</td>
								<td valign="top" width="116">, - * /</td>
							</tr>
							<tr>
								<td valign="top" width="133">月内日期</td>
								<td valign="top" width="150">1-31</td>
								<td valign="top" width="116">, - * ? / L W C</td>
							</tr>
							<tr>
								<td valign="top" width="133">月</td>
								<td valign="top" width="150">1-12 或者 JAN-DEC</td>
								<td valign="top" width="116">, - * /</td>
							</tr>
							<tr>
								<td valign="top" width="133">周内日期</td>
								<td valign="top" width="150">1-7 或者 SUN-SAT</td>
								<td valign="top" width="116">, - * ? / L C #</td>
							</tr>
							<tr>
								<td valign="top" width="133">年（可选）</td>
								<td valign="top" width="150">留空, 1970-2099</td>
								<td valign="top" width="116">, - * /</td>
							</tr>
						</tbody>
					</table>
				</div>
				<p>特殊字符意义对应表见表2。</p>
				<div align="center">
					<br />表2. Cron 表达式特殊字符意义对应表
				</div>
				<div align="center">
					<table width="500" align="center" border="0" cellpadding="2"
						cellspacing="0">
						<tbody>
							<tr>
								<th valign="top" width="45">
									<p align="left">特殊字符</p>
								</th>
								<th valign="top" width="599">
									<p align="left">意义</p>
								</th>
							</tr>
							<tr>
								<td valign="top" width="45">
									<p align="left">*</p>
								</td>
								<td valign="top" width="599">
									<p align="left">匹配所有的值。如：*在分钟的字段域里表示 每分钟</p>
								</td>
							</tr>
							<tr>
								<td valign="top" width="45">
									<p align="left">?</p>
								</td>
								<td valign="top" width="599">
									<p align="left">只在日期域和星期域中使用。它被用来指定&#8220;非明确的值&#8221;</p>
								</td>
							</tr>
							<tr>
								<td valign="top" width="45">
									<p align="left">-</p>
								</td>
								<td valign="top" width="599">
									<p align="left">指定一个范围。如：&#8220;10-12&#8221;在小时域意味着&#8220;10点、11点、12点&#8221;</p>
								</td>
							</tr>
							<tr>
								<td valign="top" width="45">
									<p align="left">,</p>
								</td>
								<td valign="top" width="599">
									<p align="left">指定几个可选值。如：&#8220;MON,WED,FRI&#8221;在星期域里表示&#8220;星期一、星期三、星期五&#8221;
									</p>
								</td>
							</tr>
							<tr>
								<td valign="top" width="45">
									<p align="left">/</p>
								</td>
								<td valign="top" width="599">
									<p align="left">指定增量。如：&#8220;0/15&#8221;在秒域意思是没分钟的0，15，30和45秒。&#8220;5/15&#8221;在分钟域表示没小时的
										5，20，35和50。符号&#8220;*&#8221;在&#8220;/&#8221;前面（如：*/10）等价于0在&#8220;/&#8221;前面（如：0/10）</p>
								</td>
							</tr>
							<tr>
								<td valign="top" width="45">
									<p align="left">L</p>
								</td>
								<td valign="top" width="599">
									<p align="left">表示day-of-month和day-of-week域，但在两个字段中的意思不同，例如day-of-month域
										中表示一个月的最后一天。如果在day-of-week域表示&#8216;7&#8217;或者&#8216;SAT&#8217;，如果在day-of-week域中前面加上数字，它表示一个月的最后
										几天，例如&#8216;6L&#8217;就表示一个月的最后一个星期五</p>
								</td>
							</tr>
							<tr>
								<td valign="top" width="45">
									<p align="left">W</p>
								</td>
								<td valign="top" width="599">
									<p align="left">只允许日期域出现。这个字符用于指定日期的最近工作日。例如：如果你在日期域中写
										&#8220;15W&#8221;，表示：这个月15号最近的工作日。所以，如果15号是周六，则任务会在14号触发。如果15好是周日，则任务会在周一也就是16号触发。如果
										是在日期域填写&#8220;1W&#8221;即使1号是周六，那么任务也只会在下周一，也就是3号触发，&#8220;W&#8221;字符指定的最近工作日是不能够跨月份的。字符&#8220;W&#8221;只能配合一个
										单独的数值使用，不能够是一个数字段，如：1-15W是错误的</p>
								</td>
							</tr>
							<tr>
								<td valign="top" width="45">
									<p align="left">LW</p>
								</td>
								<td valign="top" width="599">
									<p align="left">L和W可以在日期域中联合使用，LW表示这个月最后一周的工作日</p>
								</td>
							</tr>
							<tr>
								<td valign="top" width="45">
									<p align="left">#</p>
								</td>
								<td valign="top" width="599">
									<p align="left">只允许在星期域中出现。这个字符用于指定本月的某某天。例如：&#8220;6#3&#8221;表示本月第三周的星期五（6表示星期五，3表示
										第三周）。&#8220;2#1&#8221;表示本月第一周的星期一。&#8220;4#5&#8221;表示第五周的星期三</p>
								</td>
							</tr>
							<tr>
								<td valign="top" width="45">
									<p align="left">C</p>
								</td>
								<td valign="top" width="599">
									<p align="left">允许在日期域和星期域出现。这个字符依靠一个指定的&#8220;日历&#8221;。也就是说这个表达式的值依赖于相关的&#8220;日历&#8221;的计算结果，
										如果没有&#8220;日历&#8221;关联，则等价于所有包含的&#8220;日历&#8221;。如：日期域是&#8220;5C&#8221;表示关联&#8220;日历&#8221;中第一天，或者这个月开始的第一天的后5天。星期域是&#8220;1C&#8221;
										表示关联&#8220;日历&#8221;中第一天，或者星期的第一天的后1天，也就是周日的后一天（周一）</p>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<p>示例：</p>
				<p>
					"0 0 0 1 1 ?" &nbsp;每年元旦1月1日 0 点触发
				</p>
				<p>
					"0 15 10 * * ? *" &nbsp;每天上午10:15触发
				</p>
				<p>
					"0 15 10 * * ? 2005"&nbsp;2005年的每天上午10:15触发
				</p>
				<p>
					"0 0-5 14 * * ?"&nbsp;每天下午2点到下午2:05期间的每1分钟触发
				</p>
				<p>
					"0 10,44 14 ? 3 WED"&nbsp;每年三月的星期三的下午2:10和2:44触发
				</p>
				<p>
					"0 15 10 ? * MON-FRI"&nbsp; 周一至周五的上午10:15触发
				</p>
				<p>
					"0 15 10 ? * 6#3"&nbsp;每月的第三个星期五上午10:15触发
				</p>
			</div>
		</div>
	</ui:Panel>
</body>
</html>