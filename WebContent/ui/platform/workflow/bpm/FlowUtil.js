if (typeof FlowUtil == "undefined") {
	FlowUtil = {};
}
/**
 * 启动流程
 * @param defId 流程定义ID
 * @param actDefId Act流程定义ID
 */
FlowUtil.startFlow = function(defId, actDefId) {
	$.postc(__ctx + "/workflow/bpmDef/getCanDirectStart.html", {defId : defId}, function(data) {
		if (data=='true') {
			MessageUtil.confirm("需要启动流程吗?",function(){
				$.postc(__ctx + "/workflow/task/startFlow.html",{actDefId:actDefId},function(){
					MessageUtil.show("启动流程成功!");
				});
			});
		} else {
			var topWin = FrameHelper.getTop();
			var height = $(topWin.document.body).height();
			var width = $(topWin.document.body).width();
			topWin.Dialog.open({
				Title : "启动流程",
				URL : __ctx + '/workflow/task/startFlowForm.html?defId='+defId,
				Width : width,
				Height : height
			});
		}
	});
};

/**
 * 显示审批历史
 * @param runId
 */
FlowUtil.showProRunHistory = function(runId){
	var topWin = FrameHelper.getTop();
	topWin.Dialog.open({
		Title : "流程历史",
		URL : __ctx + '/workflow/bpm/taskOpinion/toList.html?runId='+runId,
		Width : 1000,
		Height : 600
	});
};

/**
 * 显示流程图
 * @param runId
 */
FlowUtil.showProRunGraph = function(runId){
	var topWin = FrameHelper.getTop();
	topWin.Dialog.open({
		Title : "流程示意图",
		URL : __ctx + '/workflow/bpm/processRun/graph.html?runId='+runId,
		Width : 1000,
		Height : 600
	});
};

/**
 * 显示流程图
 * @param runId
 */
FlowUtil.showBpmDefGraph = function(definitionId){
	var topWin = FrameHelper.getTop();
	topWin.Dialog.open({
		Title : "流程示意图",
		URL : __ctx + '/workflow/bpmImage.html?definitionId='+definitionId,
		Width : 1000,
		Height : 600
	});
};

/**
 * 撤销操作
 * @param b
 */
FlowUtil.recover = function(b) {
};