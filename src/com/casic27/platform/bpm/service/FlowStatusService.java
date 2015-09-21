package com.casic27.platform.bpm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casic27.platform.bpm.dao.IBpmProStatusMapper;
import com.casic27.platform.bpm.entity.BpmProStatus;

@Service
public class FlowStatusService {
	
	@Autowired
	private IBpmProStatusMapper bpmProStatusMapper;
	
	/**
	 * 状态颜色字典
	 */
	private  static Map<String, String> statusColorMap = new HashMap<String, String>();
	/**
	 * 初始化任务状态和颜色映射
	 */
	static{
		statusColorMap.put("1", "#00FF00");
		statusColorMap.put("0", "#FFA500");
		statusColorMap.put("-1", "#FF0000");
		statusColorMap.put("2", "#0000FF");
		statusColorMap.put("3", "#8A0902");
		statusColorMap.put("4", "#023B62");
		statusColorMap.put("5", "#338848");
		statusColorMap.put("6", "#82B7D7");
		statusColorMap.put("14", "#EEAF97");
		statusColorMap.put("33", "#F89800");
		statusColorMap.put("34", "#FFE76E");
		statusColorMap.put("37", "#C33A1F");
	}
	
	/**
	 * 获取流程各节点的状态
	 * @param instanceId
	 * @return
	 */
	public Map<String, String> getStatusByInstanceId(String instanceId) {
		Map<String,String> map = new HashMap<String,String>();
		List<BpmProStatus> list = this.bpmProStatusMapper.getByActInstanceId(instanceId);
		for (BpmProStatus obj : list) {
			String color = FlowStatusService.statusColorMap.get(obj.getStatus().toString());
			map.put(obj.getNodeId(), color);
		}
		return map;
	}
	
}
