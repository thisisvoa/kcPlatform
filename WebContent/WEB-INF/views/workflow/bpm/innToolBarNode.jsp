<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
<!--
	function beforeClick(type){<c:if test="${not empty buttonList}">
			switch(type){<c:forEach items="${buttonList}" var="btn"><c:if test="${not empty btn.beforeHandler}">
					case ${btn.type}:
					${btn.beforeHandler}
					break;</c:if></c:forEach>
			}
		</c:if>
	}
	
	function afterClick(type){<c:if test="${not empty buttonList}">
		switch(type){<c:forEach items="${buttonList}" var="btn"><c:if test="${not empty btn.afterHandler}">
				case ${btn.type}:
				${btn.afterHandler}
				break;
				</c:if>
			</c:forEach>
		}
		</c:if>
	}
//-->
</script>
<div id="toolbar" class="easyui-toolbar">
<c:choose>
	<c:when test="${isFirstNode}">
		<c:choose>
			<c:when test="${ isManage==0 }">
				<c:choose>
					<c:when test="${empty buttonList}">
						<c:choose>
							<c:when test="${processRun.status eq 5 or processRun.status eq 6}">
								<a id="startBtn" href="#" iconCls="icon-run" onclick="commit()">重新提交</a>
							</c:when>
							<c:otherwise>
								<a id="startBtn" href="#" iconCls="icon-run" onclick="commit()">提交</a>
							</c:otherwise>
						</c:choose>
						<a id="saveFormBtn" href="#" iconCls="icon-save" onclick="saveForm()">保存表单</a>
						<a id="diagramBtn" href="#" iconCls="icon-flowDesign" onclick="showProRunGraph()">流程图</a>
						<a id="hisBtn" href="#" iconCls="icon-search" onclick="showProRunHistory()">审批历史</a>
					</c:when>
					<c:otherwise>
							
						<c:forEach items="${buttonList}" var="btn" varStatus="status">
							<c:if test="${btn.type==1}">
								<a id="startBtn" href="#" iconCls="icon-run" onclick="commit()">${btn.btnName}</a>
							</c:if>
							<c:if test="${btn.type==2}">
								<a id="saveFormBtn" href="#" iconCls="icon-save" onclick="saveForm()">${btn.btnName}</a>
							</c:if>
							<c:if test="${btn.type==7}">
								<a id="diagramBtn" href="#" iconCls="icon-flowDesign" onclick="showProRunGraph()">${btn.btnName}</a>
							</c:if>
							<c:if test="${btn.type==8}">
								<a id="hisBtn" href="#" iconCls="icon-search" onclick="showProRunHistory()">${btn.btnName}</a>
							</c:if>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:when test="${ isManage==1 }">
				<c:choose>
					<c:when test="${processRun.status eq 5 or processRun.status eq 6}">
						<a id="startBtn" href="#" iconCls="icon-run" onclick="commit()">重新提交</a>
					</c:when>
					<c:otherwise>
						<a id="startBtn" href="#" iconCls="icon-run" onclick="commit()">提交</a>
					</c:otherwise>
				</c:choose>
				<a id="saveFormBtn" href="#" iconCls="icon-save" onclick="saveForm()">保存表单</a>
				<a id="endProcessBtn" href="#" iconCls="icon-giveup" onclick="endProcess()">终止</a>
				<a id="diagramBtn" href="#" iconCls="icon-flowDesign" onclick="showProRunGraph()">流程图</a>
				<a id="hisBtn" href="#" iconCls="icon-search" onclick="showProRunHistory()">审批历史</a>
			</c:when>
		</c:choose>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${ isManage==0 }">
				<c:choose>
					<c:when test="${empty buttonList}">
						<c:if test="${isSignTask && isAllowDirectExecute}">
							<div style="display:inline-block;*display:inline;*zoom:1;height: 24px;line-height: 24px;outline:none;">特权：<input type="checkbox" value="1" id="chkDirectComplete"><label for="chkDirectComplete">直接结束</label></div>
						</c:if>
						<a id="agreeBtn" href="#" iconCls="icon-run" onclick="agree()">同意</a>
						<c:if test="${isSignTask==true}">
							<a id="notAgreeBtn" href="#" iconCls="icon-remove" onclick="notAgree()">反对</a>
							<a id="abandonBtn" href="#" iconCls="icon-giveup" onclick="abandon()">弃权</a>
							<c:if test="${isAllowRetoactive==true}">
								<a id="addSignBtn" href="#" iconCls="icon-userAdd" onclick="addSign()">补签</a>	
							</c:if>
						</c:if>
						<a id="saveFormBtn" href="#" iconCls="icon-save" onclick="saveForm()">保存表单</a>
						<c:if test="${isCanBack}">
							<a id="rejectBtn" href="#" iconCls="icon-reject" onclick="reject()">驳回</a>
							<c:if test="${toBackNodeId!=task.taskDefinitionKey}">
							<a id="rejectToStartBtn" href="#" iconCls="icon-rejectToStart" onclick="rejectToStart()">驳回到发起人</a>
							</c:if>
						</c:if>
						<a id="diagramBtn" href="#" iconCls="icon-flowDesign" onclick="showProRunGraph()">流程图</a>
						<a id="hisBtn" href="#" iconCls="icon-search" onclick="showProRunHistory()">审批历史</a>
					</c:when>
					<c:otherwise>
						<c:if test="${isSignTask && isAllowDirectExecute}">
							<div style="display:inline-block;*display:inline;*zoom:1;height: 24px;line-height: 24px;outline:none;">特权：<input type="checkbox" value="1" id="chkDirectComplete"><label for="chkDirectComplete">直接结束</label></div>
						</c:if>
						<c:forEach items="${buttonList}" var="btn" varStatus="status">
							<c:if test="${btn.type==3}">
								<a id="agreeBtn" href="#" iconCls="icon-run" onclick="agree()">${btn.btnName}</a>
							</c:if>
							<c:if test="${btn.type==4}">
								<a id="notAgreeBtn" href="#" iconCls="icon-remove" onclick="notAgree()">${btn.btnName}</a>
							</c:if>
							<c:if test="${isSignTask==true}">
								
								<c:if test="${btn.type==9}">
									<a id="abandonBtn" href="#" iconCls="icon-giveup" onclick="abandon()">${btn.btnName}</a>
								</c:if>
								<c:if test="${isAllowRetoactive==true}">
									<c:if test="${btn.type==10}">
										<a id="addSignBtn" href="#" iconCls="icon-userAdd" onclick="addSign()">${btn.btnName}</a>
									</c:if>	
								</c:if>
							</c:if>
							<c:if test="${btn.type==2}">
								<a id="saveFormBtn" href="#" iconCls="icon-save" onclick="saveForm()">${btn.btnName}</a>
							</c:if>
							<c:if test="${isCanBack}">
								<c:if test="${btn.type==5}">
									<a id="rejectBtn" href="#" iconCls="icon-reject" onclick="reject()">${btn.btnName}</a>
								</c:if>
								<c:if test="${btn.type==6 && toBackNodeId!=task.taskDefinitionKey}">
									<a id="rejectToStartBtn" href="#" iconCls="icon-rejectToStart" onclick="rejectToStart()">${btn.btnName}</a>
								</c:if>
							</c:if>
							<c:if test="${btn.type==7}">
								<a id="diagramBtn" href="#" iconCls="icon-flowDesign" onclick="showProRunGraph()">${btn.btnName}</a>
							</c:if>
							<c:if test="${btn.type==8}">
								<a id="hisBtn" href="#" iconCls="icon-search" onclick="showProRunHistory()">${btn.btnName}</a>
							</c:if>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<c:if test="${isSignTask && isAllowDirectExecute}">
					<div style="display:inline-block;*display:inline;*zoom:1;height: 24px;line-height: 24px;outline:none;">特权：<input type="checkbox" value="1" id="chkDirectComplete"><label for="chkDirectComplete">直接结束</label></div>
				</c:if>
				<a id="agreeBtn" href="#" iconCls="icon-run" onclick="agree()">同意</a>
				<c:if test="${isSignTask==true}">
					<a id="notAgreeBtn" href="#" iconCls="icon-remove" onclick="notAgree()">反对</a>
					<a id="abandonBtn" href="#" iconCls="icon-giveup" onclick="abandon()">弃权</a>
					<c:if test="${isAllowRetoactive==true}">
						<a id="addSignBtn" href="#" iconCls="icon-userAdd" onclick="addSign()">补签</a>	
					</c:if>
				</c:if>
				<a id="saveFormBtn" href="#" iconCls="icon-save" onclick="saveForm()">保存表单</a>
				<c:if test="${isCanBack}">
					<a id="rejectBtn" href="#" iconCls="icon-reject" onclick="reject()">驳回</a>
					<a id="rejectToStartBtn" href="#" iconCls="icon-rejectToStart" onclick="rejectToStart()">驳回到发起人</a>
				</c:if>
				<a id="endProcessBtn" href="#" iconCls="icon-giveup" onclick="endProcess()">终止</a>
				<a id="diagramBtn" href="#" iconCls="icon-flowDesign" onclick="showProRunGraph()">流程图</a>
				<a id="hisBtn" href="#" iconCls="icon-search" onclick="showProRunHistory()">审批历史</a>
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>

<!-- 
<c:choose>
	<c:when test="${ isManage==1 }">
		<span style="height:28px;line-height:28px;"><input type="radio" checked="checked" name="jumpType" onclick="chooseJumpType(1)" value="1" />&nbsp;正常跳转</span>
		<span style="height:28px;line-height:28px;"><input type="radio" name="jumpType" onclick="chooseJumpType(2)" value="2" />&nbsp;选择路径跳转</span>
		<span style="height:28px;line-height:28px;"><input type="radio" name="jumpType" onclick="chooseJumpType(3)" value="3" />&nbsp;自由跳转</span>
	</c:when>
</c:choose>
 -->
</div>