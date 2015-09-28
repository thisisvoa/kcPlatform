package com.kcp.platform.bpm.action;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kcp.platform.bpm.entity.ProcessRun;
import com.kcp.platform.bpm.service.BpmService;
import com.kcp.platform.bpm.service.FlowStatusService;
import com.kcp.platform.bpm.service.ProcessRunService;
import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.util.RequestUtil;
import com.kcp.platform.util.StringUtils;

@Controller
@RequestMapping("workflow")
public class BpmController extends BaseMultiActionController {

	@Autowired
	private BpmService bpmService;

	@Autowired
	private ProcessRunService processRunService;
	
	@Autowired
	private FlowStatusService flowStatusService;

	@RequestMapping("bpmImage")
	public @ResponseBody void bpmImage(HttpServletRequest request, HttpServletResponse response)throws Exception {
		String deployId = RequestUtil.getString(request, "deployId");
		String taskId = RequestUtil.getString(request, "taskId");
		String processInstanceId = RequestUtil.getString(request, "processInstanceId");
		String definitionId = RequestUtil.getString(request, "definitionId");
		String runId = RequestUtil.getString(request,"runId");

		InputStream is = null;

		if (StringUtils.isNotEmpty(deployId)) {
			ProcessDefinitionEntity ent = this.bpmService.getProcessDefinitionByDeployId(deployId);
			BpmnModel model = bpmService.getBpmnModelByDefId(ent.getId());
			is = ProcessDiagramGenerator.generatePngDiagram(model);
		} else if (StringUtils.isNotEmpty(definitionId)) {
			ProcessDefinitionEntity ent = this.bpmService.getProcessDefinitionByDefId(definitionId);
			BpmnModel model = bpmService.getBpmnModelByDefId(ent.getId());
			is = ProcessDiagramGenerator.generatePngDiagram(model);
		} else if (StringUtils.isNotEmpty(taskId)) {
			ProcessDefinitionEntity ent = this.bpmService.getProcessDefinitionByTaskId(taskId);
			BpmnModel model = bpmService.getBpmnModelByDefId(ent.getId());
			TaskEntity taskEntity = this.bpmService.getTask(taskId);
			Map<String,String> highLightList = this.flowStatusService.getStatusByInstanceId(taskEntity.getProcessInstanceId());
			is = ProcessDiagramGenerator.generateDiagram(model, highLightList, "png");
		} else if (StringUtils.isNotEmpty(processInstanceId)) {
			ProcessDefinitionEntity ent = this.bpmService.getProcessDefinitionByProcessInanceId(processInstanceId);
			BpmnModel model = bpmService.getBpmnModelByDefId(ent.getId());
			Map<String,String> highLightMap = flowStatusService.getStatusByInstanceId(processInstanceId);
			is = ProcessDiagramGenerator.generateDiagram(model, highLightMap,"png");
		} else if (StringUtils.isNotEmpty(runId)) {
			ProcessRun processRun = processRunService.getProcessRunById(runId);
			processInstanceId = processRun.getActInstId();
			ProcessDefinitionEntity ent = this.bpmService.getProcessDefinitionByProcessInanceId(processInstanceId);
			BpmnModel model = bpmService.getBpmnModelByDefId(ent.getId());
			Map<String,String> highLightMap = flowStatusService.getStatusByInstanceId(processInstanceId);
			is = ProcessDiagramGenerator.generateDiagram(model, highLightMap, "png");
		}
		if (is != null) {
			response.setContentType("image/png");
			OutputStream out = response.getOutputStream();
			try {
				byte[] bs = new byte[1024];
				int n = 0;
				while ((n = is.read(bs)) != -1) {
					out.write(bs, 0, n);
				}
				out.flush();
			} catch (Exception localException) {
			} finally {
				is.close();
				out.close();
			}
		}
	}
}
