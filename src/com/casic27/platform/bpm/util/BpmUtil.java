package com.casic27.platform.bpm.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.apache.commons.lang.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import com.casic27.platform.bpm.entity.ProcessCmd;
import com.casic27.platform.bpm.entity.TaskExecutor;
import com.casic27.platform.bpm.graph.DivShape;
import com.casic27.platform.bpm.graph.ShapeMeta;
import com.casic27.platform.util.Dom4jUtil;
import com.casic27.platform.util.RequestUtil;
import com.casic27.platform.util.StringUtils;

@SuppressWarnings("unchecked")
public class BpmUtil {
	/**
	 * 解析request 组装成ProcessCmd
	 * @param request
	 * @return
	 */
	public static ProcessCmd getProcessCmd(HttpServletRequest request) {
		ProcessCmd cmd = new ProcessCmd();
		String temp = request.getParameter("taskId");
		if (StringUtils.isNotEmpty(temp)) {
			cmd.setTaskId(temp);
		}
		temp = request.getParameter("formData");
		if (StringUtils.isNotEmpty(temp)) {
			cmd.setFormData(temp);
		}
		Map<String,Object> paraMap = RequestUtil.getParameterValueMap(request, false, false);
		cmd.setFormDataMap(paraMap);

		temp = RequestUtil.getString(request, "actDefId");
		if (StringUtils.isNotEmpty(temp)) {
			cmd.setActDefId(temp);
		} else {
			temp = request.getParameter("flowKey");
			if (StringUtils.isNotEmpty(temp)) {
				cmd.setFlowKey(temp);
			}
		}

		temp = request.getParameter("destTask");
		if (StringUtils.isNotEmpty(temp)) {
			cmd.setDestTask(temp);
		}

		temp = request.getParameter("businessKey");
		if (StringUtils.isNotEmpty(temp)) {
			cmd.setBusinessKey(temp);
		}

		String[] destTaskIds = request.getParameterValues("lastDestTaskId");
		if (destTaskIds != null) {
			cmd.setLastDestTaskIds(destTaskIds);
			String[] destTaskUserIds = new String[destTaskIds.length];
			for (int i = 0; i < destTaskIds.length; i++) {
				String[] userIds = request.getParameterValues(destTaskIds[i]
						+ "_userId");
				if (userIds != null) {
					destTaskUserIds[i] = StringUtils.getArrayAsString(userIds);
				}
			}
			cmd.setLastDestTaskUids(destTaskUserIds);
		}

		temp = request.getParameter("back");
		if (StringUtils.isNotEmpty(temp)) {
			Integer rtn = Integer.valueOf(Integer.parseInt(temp));
			cmd.setBack(rtn);
		}

		cmd.setVoteContent(request.getParameter("voteContent"));

		temp = request.getParameter("stackId");
		if (StringUtils.isNotEmpty(temp)) {
			cmd.setStackId(temp);
		}
		temp = request.getParameter("voteAgree");
		if (StringUtils.isNotEmpty(temp)) {
			cmd.setVoteAgree(new Short(temp));
		}

		Enumeration paramEnums = request.getParameterNames();
		while (paramEnums.hasMoreElements()) {
			String paramName = (String) paramEnums.nextElement();
			if (paramName.startsWith("v_")) {
				String[] vnames = paramName.split("[_]");
				if ((vnames != null) && (vnames.length == 3)) {
					String varName = vnames[1];
					String val = request.getParameter(paramName);
					if (!val.isEmpty()) {
						Object valObj = getValue(vnames[2], val);
						cmd.getVariables().put(varName, valObj);
					}
				}
			}
		}
		temp = request.getParameter("isManage");
		if (StringUtils.isNotEmpty(temp)) {
			cmd.setIsManage(new Short(temp));
		}

		temp = request.getParameter("_executors_");
		if (StringUtils.isNotEmpty(temp)) {
			List<TaskExecutor> executorList = getTaskExecutors(temp);
			cmd.setTaskExecutors(executorList);
		}
		return cmd;
	}

	/**
	 * 获取流程分支跳转条件
	 * @param processXml
	 * @param decisionNodeId
	 * @return
	 */
	public static Map<String, String> getDecisionConditions(String processXml, String decisionNodeId) {
		Map<String, String> map = new HashMap<String, String>();
		processXml = processXml.replace("xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"", "xmlns:bpm='casic27'");
		Document doc = Dom4jUtil.loadXml(processXml);
		Element root = doc.getRootElement();

		List<Element> nodes = root.selectNodes("//sequenceFlow[@sourceRef='" + decisionNodeId + "']");
		for (Element el : nodes) {
			String id = el.attributeValue("targetRef");
			String condition = "";
			Element conditionNode = el.element("conditionExpression");
			if (conditionNode != null) {
				condition = conditionNode.getText().trim();
				condition = StringUtils.trimPrefix(condition, "${");
				condition = StringUtils.trimSufffix(condition, "}");
				condition = condition.trim();
			}
			map.put(id, condition);
		}
		return map;
	}

	/**
	 * 设置Activiti流程，流程跳转条件
	 * 
	 * @param sourceNode
	 * @param map
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static String setCondition(String sourceNode,
			Map<String, String> map, String xml) throws Exception {
		xml = xml.replace(
				"xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"",
				"xmlns:bpm=\"casic27\"");
		Document doc = Dom4jUtil.loadXml(xml, "utf-8");
		Element root = doc.getRootElement();
		List<Element> nodes = root.selectNodes("//sequenceFlow[@sourceRef='"
				+ sourceNode + "']");
		for (Element el : nodes) {
			String id = el.attributeValue("targetRef");
			String condition = (String) map.get(id);
			Element conditionEl = el.element("conditionExpression");
			if (conditionEl != null)
				el.remove(conditionEl);
			if (StringUtils.isNotEmpty(condition)) {
				Element elAdd = el.addElement("conditionExpression");
				elAdd.addAttribute("xsi:type", "tFormalExpression");
				elAdd.addCDATA("${" + condition + "}");
			}
		}
		String outXml = doc.asXML();
		outXml = outXml.replace("xmlns:bpm=\"casic27\"",
				"xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"");
		return outXml;
	}

	/**
	 * 设置设计器流程XML分支条件
	 * 
	 * @param sourceNode
	 * @param map
	 * @param xml
	 * @return
	 * @throws IOException
	 */
	public static String setGraphXml(String sourceNode,
			Map<String, String> map, String xml) throws IOException {
		Document doc = Dom4jUtil.loadXml(xml);
		Element root = doc.getRootElement();
		Element node = (Element) root.selectSingleNode("//bg:Gateway[@id='"
				+ sourceNode + "']");
		Element portsEl = node.element("ports");
		List<Element> portList = portsEl.elements();
		for (int i = 0; i < portList.size(); i++) {
			Element portEl = (Element) portList.get(i);
			if ((portEl.attribute("x") != null)
					|| (portEl.attribute("y") != null)) {
				String id = portEl.attributeValue("id");
				Element outNode = (Element) root
						.selectSingleNode("//bg:SequenceFlow[@startPort='" + id
								+ "']");
				if (outNode != null) {
					String outPort = outNode.attributeValue("endPort");
					Element tmpNode = (Element) root
							.selectSingleNode("//ciied:Port[@id='" + outPort
									+ "']");
					Element taskNode = tmpNode.getParent().getParent();
					String taskId = taskNode.attributeValue("id");

					Element conditionEl = outNode.element("condition");
					if (conditionEl != null) {
						outNode.remove(conditionEl);
					}
					if (map.containsKey(taskId)) {
						String condition = (String) map.get(taskId);
						Element elAdd = outNode.addElement("condition");
						elAdd.addText(condition);
					}
				}
			}
		}
		return doc.asXML();
	}

	/**
	 * 解析表达式,将{}包含的变量替换为map中对应的变量名的值
	 * @param titleRule
	 * @param map
	 * @return
	 */
	public static String parseTextByRule(String titleRule, Map<String, Object> map) {
		Pattern regex = Pattern.compile("\\{(.*?)\\}", 98);
		Matcher matcher = regex.matcher(titleRule);
		while (matcher.find()) {
			String tag = matcher.group(0);
			String rule = matcher.group(1);
			String[] aryRule = rule.split(":");
			String name = "";
			if (aryRule.length == 1) {
				name = rule;
			} else {
				name = aryRule[1];
			}
			String value = (String) map.get(name);
			if (StringUtils.isEmpty(value)) {
				titleRule = titleRule.replace(tag, "");
			} else {
				titleRule = titleRule.replace(tag, value);
			}
		}
		return titleRule;
	}
	/**
	 * 解析节点执行人员
	 * @param executors
	 * @return
	 */
	public static List<TaskExecutor> getTaskExecutors(String executors){
		String[] aryExecutor = executors.split("#");
		List<TaskExecutor> list = new ArrayList<TaskExecutor>();
		for (String tmp : aryExecutor) {
			String[] aryTmp = tmp.split("\\^");
			if (aryTmp.length == 3) {
				list.add(new TaskExecutor(aryTmp[0], aryTmp[1], aryTmp[2]));
			}else if (aryTmp.length == 1) {
				list.add(new TaskExecutor(aryTmp[0]));
			}
	     }
	     return list;
	}
	
	public static Object getValue(String type, String paramValue) {
		Object value = null;
		if ("S".equals(type)) {
			value = paramValue;
		} else if ("L".equals(type)) {
			value = new Long(paramValue);
		} else if ("I".equals(type)) {
			value = new Integer(paramValue);
		} else if ("DB".equals(type)) {
			value = new Double(paramValue);
		} else if ("BD".equals(type)) {
			value = new BigDecimal(paramValue);
		} else if ("F".equals(type)) {
			value = new Float(paramValue);
		} else if ("SH".equals(type)) {
			value = new Short(paramValue);
		} else if ("D".equals(type))
			try {
				value = DateUtils.parseDate(paramValue, new String[] {
						"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" });
			} catch (Exception ex) {
			}
		else {
			value = paramValue;
		}
		return value;
	}
	/**
	 * 构建图形元素坐标div
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static ShapeMeta transGraph(String xml) throws Exception {
		xml = xml.replace("xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"", "");
		Document doc = Dom4jUtil.loadXml(xml);
		Element root = doc.getRootElement();
		List list = root.selectNodes("//bpmndi:BPMNShape");
		int minx = 100000;
		int miny = 100000;
		int maxw = 0;
		int maxh = 0;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Element el = (Element) list.get(i);
			Element tmp = (Element) el.selectSingleNode("omgdc:Bounds");
			int x = (int) Float.parseFloat(tmp.attributeValue("x"));
			int y = (int) Float.parseFloat(tmp.attributeValue("y"));

			int w = x + (int) Float.parseFloat(tmp.attributeValue("width"));
			int h = y + (int) Float.parseFloat(tmp.attributeValue("height"));

			minx = Math.min(x, minx);
			miny = Math.min(y, miny);

			maxw = Math.max(w, maxw);
			maxh = Math.max(h, maxh);
		}
		List pointList = root.selectNodes("//omgdi:waypoint");
		for (int i = 0; i < pointList.size(); i++) {
			Element tmp = (Element) pointList.get(i);
			int x = (int) Float.parseFloat(tmp.attributeValue("x"));
			int y = (int) Float.parseFloat(tmp.attributeValue("y"));
			minx = Math.min(x, minx);
			miny = Math.min(y, miny);
		}

		for (int i = 0; i < list.size(); i++) {
			Element el = (Element) list.get(i);
			Element tmp = (Element) el.selectSingleNode("omgdc:Bounds");
			int x = (int) Float.parseFloat(tmp.attributeValue("x"));
			int y = (int) Float.parseFloat(tmp.attributeValue("y"));

			int w = (int) Float.parseFloat(tmp.attributeValue("width"));
			int h = (int) Float.parseFloat(tmp.attributeValue("height"));
			x = x - minx + 5;
			y = y - miny + 5;

			String id = el.attributeValue("bpmnElement");

			Element procEl = (Element) root.selectSingleNode("//process/descendant::*[@id='" + id + "']");
			if(procEl==null) continue;
			String type = procEl.getName();
			if (type.equals("serviceTask")) {
				String attribute = procEl.attributeValue("class");
				if (attribute != null) {
					if (attribute.equals("com.casic27.platform.bpm.delegate.MessageTask")) {
						type = "email";
					} else if (attribute.equals("com.casic27.platform.bpm.delegate.ScriptTask")) {
						type = "script";
					}
				}
			}
			Element multiObj = procEl.element("multiInstanceLoopCharacteristics");
			if (multiObj != null){
				type = "multiUserTask";
			}
			Element parent = procEl.getParent();
			String name = procEl.attributeValue("name");
			int zIndex = 10;
			String parentName = parent.getName();
			if (parentName.equals("subProcess")) {
				zIndex = 11;
			}
			DivShape shape = new DivShape(name, x, y, w, h, zIndex, id, type);
			sb.append(shape);
		}
		ShapeMeta shapeMeta = new ShapeMeta(maxw, maxh, sb.toString());
		return shapeMeta;
	}
	
	/**
	 * 是否多实例
	 * @param delegateTask
	 * @return
	 */
	public static boolean isMultiTask(DelegateTask delegateTask) {
		ActivityExecution execution = (ActivityExecution) delegateTask.getExecution();
		String multiInstance = (String) execution.getActivity().getProperty("multiInstance");
		if (multiInstance != null) {
			return true;
		}
		return false;
	}
	/**
	 * 是否多实例
	 * @param execution
	 * @return
	 */
	public static boolean isMuiltiExcetion(ExecutionEntity execution) {
		String multiInstance = (String) execution.getActivity().getProperty("multiInstance");
		if (multiInstance != null) {
			return true;
		}
		return false;
	}
	
	public static String getTaskStatus(Short status, int flag) {
		switch (status.shortValue()) {
			case -2:
				return "<font color='red'>尚未审批</font>";
			case -1:
				return flag == 0 ? "尚未处理" : "<font color='red'>正在审批</font>";
			case 0:
				return "<font color='red'>弃权</font>";
			case 1:
				return "<font color='green'>同意</font>";
			case 2:
				return "<font color='orange'>反对</font>";
			case 3:
				return "<font color='red'>驳回</font>";
			case 4:
				return "<font color='red'>撤销</font>";
			case 5:
				return "<font color='green'>会签通过</font>";
			case 6:
				return "<font color='red'>会签不通过</font>";
			case 7:
				return "<font color='red'>知会意见</font>";
			case 8:
				return "<font color='red'>更改执行路径</font>";
			case 9:
				return "<font color='red'>终止</font>";
			case 10:
				return "<font color='red'>沟通意见</font>";
			case 14:
				return "<font color='red'>终止</font>";
			case 15:
				return "<font color='red'>沟通 </font>";
			case 16:
				return "<font color='orange'>办结转发</font>";
			case 17:
				return "<font color='orange'>撤销</font>";
			case 18:
				return "<font color='orange'>删除</font>";
			case 19:
				return "<font color='orange'>抄送</font>";
			case 20:
				return "<font color='green'>沟通反馈</font>";
			case 21:
				return "<font color='red'>转办</font>";
			case 22:
				return "<font color='red'>取消转办</font>";
			case 23:
				return "<font color='red'>更改执行人</font>";
			case 24:
				return "<font color='red'>驳回到发起人</font>";
			case 25:
				return "<font color='red'>撤销(撤销到发起人)</font>";
			case 26:
				return "<font color='brown'>代理</font>";
			case 27:
				return "<font color='green'>取消代理</font>";
			case 28:
				return "<font color='green'>保存表单</font>";
			case 29:
				return "<font color='green'>驳回取消</font>";
			case 30:
				return "<font class='brown'>撤销取消</font>";
			case 31:
				return "<font class='brown'>通过取消</font>";
			case 32:
				return "<font class='brown'>反对取消</font>";
			case 33:
				return "<font class='green'>提交</font>";
			case 34:
				return "<font class='green'>重新提交</font>";
			case 35:
				return "<font class='brown'>干预</font>";
			case 36:
				return "<font class='brown'>重启任务</font>";
			case 38:
				return "<font color='green'>流转</font>";
			case 40:
				return "<font color='red'>代提交</font>";
			case 11:
			case 12:
			case 13:
			case 37:
			case 39:
		}
		return "";
	}
	 
}
