package com.casic27.platform.bpm.util;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.CallActivity;
import org.activiti.bpmn.model.FieldExtension;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;

import com.casic27.platform.core.exception.BusinessException;
import com.casic27.platform.util.BeanUtils;
import com.casic27.platform.util.StringUtils;

public class BPMN20Util {
	private static String ARR_ORDER_NAME = "order";
	
	public static BpmnModel convertToBpmnModel(String actFlowDefXml){
		try {
			Reader reader = new StringReader(actFlowDefXml);
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader xmlReader = factory.createXMLStreamReader(reader);
			BpmnModel model = new BpmnXMLConverter().convertToBpmnModel(xmlReader);
			return model;
		} catch (Exception e) {
			throw new BusinessException("流程定义XML存在问题，转化为BpmnModel失败！", e);
		}
	}
	
	/**
	 * 获取序号
	 * @param element
	 * @return
	 */
	public static int getOrder(FlowElement element){
		int result = 0;
		List<FieldExtension> exFields = null;
		if(element instanceof UserTask){
			exFields = ((UserTask)element).getFieldExtensions();
		}else if(element instanceof CallActivity){
			exFields = ((UserTask)element).getFieldExtensions();
		}
		if(BeanUtils.isNotEmpty(exFields)){
			for(FieldExtension field:exFields){
				if(ARR_ORDER_NAME.equals(field.getFieldName())){
					if ((StringUtils.isNotEmpty(field.getStringValue()))) {
						result = Integer.valueOf(field.getStringValue());
						break;
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 获取流程节点
	 * @param processes
	 * @param clss
	 * @return
	 */
	public static List<FlowElement> getFlowElement(org.activiti.bpmn.model.Process process, Class<? extends FlowElement>[] clss){
		List<FlowElement> result = new ArrayList<FlowElement>();
		List<FlowElement> flowElements = (List<FlowElement>)process.getFlowElements();
		for(FlowElement element:flowElements){
			for(Class cls:clss){
				if(cls.isInstance(element)){
					result.add(element);
				}
			}
		}
		return result;
	}
	
}
