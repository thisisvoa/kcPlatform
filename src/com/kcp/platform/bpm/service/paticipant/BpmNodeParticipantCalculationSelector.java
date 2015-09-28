package com.kcp.platform.bpm.service.paticipant;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kcp.platform.bpm.service.thread.TaskUserAssignService;
import com.kcp.platform.sys.context.SpringContextHolder;

/**
 * 流程节点执行人解析器
 * @author Administrator
 *
 */
@Component
public class BpmNodeParticipantCalculationSelector {
	
	private Logger logger = LoggerFactory.getLogger(BpmNodeParticipantCalculationSelector.class);
	
	/**
	 * 流程节点执行人解析器映射Map
	 */
	private Map<String, IBpmNodeParticipantCalculation> bpmNodeParticipantCalculation = new LinkedHashMap<String, IBpmNodeParticipantCalculation>();
	
	@PostConstruct
	public void init(){
		logger.debug("流程节点执行人解析器初始化开始...");
		List<IBpmNodeParticipantCalculation> calList = SpringContextHolder.getBeanListByType(IBpmNodeParticipantCalculation.class);
		for(IBpmNodeParticipantCalculation cal:calList){
			bpmNodeParticipantCalculation.put(cal.getKey(), cal);
		}
		logger.debug("流程节点执行人解析器初始化结束...");
	}
	
	public IBpmNodeParticipantCalculation getByKey(String key){
	     return this.bpmNodeParticipantCalculation.get(key);
	}
	
	public Map<String, IBpmNodeParticipantCalculation> getBpmNodeParticipantCalculation() {
		return bpmNodeParticipantCalculation;
	}

	public void setBpmNodeParticipantCalculation(Map<String, IBpmNodeParticipantCalculation> bpmNodeParticipantCalculation) {
		this.bpmNodeParticipantCalculation = bpmNodeParticipantCalculation;
	}
	
}
