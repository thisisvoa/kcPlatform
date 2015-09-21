/**
 * @(#)com.casic27.platform.bpm.service.NodeParticipantService
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright: Copyright(c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.bpm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.bpm.dao.IBpmNodeParticipantMapper;
import com.casic27.platform.bpm.entity.BpmNodeParticipant;
import com.casic27.platform.bpm.entity.TaskExecutor;
import com.casic27.platform.bpm.service.paticipant.BpmNodeParticipantCalculationSelector;
import com.casic27.platform.bpm.service.paticipant.CalcVars;
import com.casic27.platform.bpm.service.paticipant.IBpmNodeParticipantCalculation;
import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.core.service.BaseService;

@Service("bpmNodeParticipantService")
public class BpmNodeParticipantService extends BaseService{
	@Autowired
	private IBpmNodeParticipantMapper nodeParticipantMapper;
	
	@Autowired
	private BpmNodeParticipantCalculationSelector participantCalculationSelector;
	/**
	 * 查询列表
	 * @param queryMap
	 */
	public List<BpmNodeParticipant> findNodeParticipant(Map<String,Object> queryMap){
		return nodeParticipantMapper.findNodeParticipant(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public BpmNodeParticipant getNodeParticipantById(String id){
		return nodeParticipantMapper.getNodeParticipantById(id);
	}
	/**
	 * 新增
	 */
	public void addNodeParticipant(BpmNodeParticipant nodeParticipant){
		nodeParticipant.setParticipantId(CodeGenerator.getUUID32());
		int sn = getMaxSn(nodeParticipant.getConfigId());
		nodeParticipant.setSn(sn);
		nodeParticipantMapper.addNodeParticipant(nodeParticipant);
	}
	
	/**
	 * 修改
	 */
	public void updateNodeParticipant(BpmNodeParticipant nodeParticipant){
		nodeParticipantMapper.updateNodeParticipant(nodeParticipant);
	}
	
	/**
	 *删除
	 */
	public void deleteNodeParticipant(String id){
		nodeParticipantMapper.deleteNodeParticipant(id);
	}
	
	/**
	 * 最大排序号
	 * @param configId
	 * @return
	 */
	public int getMaxSn(String configId){
		Integer sn = nodeParticipantMapper.getMaxSn(configId);
		sn = (sn==null?1:(sn+1));
		return sn;
	}
	
	public void updateSns(List<BpmNodeParticipant> participantList){
		for(BpmNodeParticipant participant:participantList){
			nodeParticipantMapper.updateSn(participant);
		}
	}
	
	public List<BpmNodeParticipant> getByActDefIdNodeId(String actDefId, String nodeId){
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("actDefId", actDefId);
		queryMap.put("nodeId", nodeId);
		return findNodeParticipant(queryMap);
	}
	
	/**
	 * 获取执行某业务节点的用户ID
	 * @param actDefId
	 * @param actInstId
	 * @param nodeId
	 * @param startUserId
	 * @param preTaskUser
	 * @param vars
	 * @return
	 */
	public List getExeUserIds(String actDefId, String actInstId, String nodeId, String startUserId, String preTaskUser, Map<String,Object> vars){
		return getExeUserIds(actDefId, actInstId, nodeId, startUserId, preTaskUser, vars, false);
	}
	/**
	 * 获取执行某业务节点的用户ID
	 * @param actDefId
	 * @param actInstId
	 * @param nodeId
	 * @param startUserId
	 * @param preTaskUser
	 * @param isUser true:返回的是User对象 false:返回的是TaskExecutor对象
	 * @return
	 */
	public List getExeUserIds(String actDefId, String actInstId, String nodeId, String startUserId, String preTaskUser, Map<String,Object> vars, boolean isUser){
		List list = new ArrayList();
		Set userIdSet = new LinkedHashSet();
		List<BpmNodeParticipant> nodeParticipants = getByActDefIdNodeId(actDefId, nodeId);
		for(BpmNodeParticipant nodeParticipant:nodeParticipants){
			Set uIdSet = null;
			if (isUser) {
				uIdSet = getUsersByBpmNodeParticipant(nodeParticipant, startUserId, preTaskUser, actInstId, vars);
			} else {
				uIdSet = getByBpmNodeParticipant(nodeParticipant, startUserId, preTaskUser, actInstId, vars);
			}

			if (userIdSet.size() == 0) {
				userIdSet = uIdSet;
			} else {
				userIdSet = computeUserSet(Integer.valueOf(nodeParticipant.getComputeType()), userIdSet, uIdSet);
			}
		}
		list.addAll(userIdSet);
		return list;
	}
	/**
	 * 计算单个人员设置
	 * @param nodeParticipant
	 * @param startUserId
	 * @param preTaskUserId
	 * @param actInstId
	 * @param vars
	 * @return
	 */
	private Set<User> getUsersByBpmNodeParticipant(BpmNodeParticipant nodeParticipant, String startUserId, String preTaskUserId, String actInstId, Map<String, Object> vars){
		IBpmNodeParticipantCalculation calculation = participantCalculationSelector.getByKey(nodeParticipant.getParticipantType());
		CalcVars params = new CalcVars(startUserId, preTaskUserId, actInstId, vars);
		Set<User> set = new LinkedHashSet<User>();
		set.addAll(calculation.getExecutor(nodeParticipant, params));
		return set;
	}
	
	/**
	 * 计算单个人员设置
	 * @param nodeParticipant
	 * @param startUserId
	 * @param preTaskUserId
	 * @param actInstId
	 * @param vars
	 * @return
	 */
	private Set<TaskExecutor> getByBpmNodeParticipant(BpmNodeParticipant nodeParticipant, String startUserId, String preTaskUserId, String actInstId, Map<String, Object> vars){
		IBpmNodeParticipantCalculation calculation = participantCalculationSelector.getByKey(nodeParticipant.getParticipantType());
		CalcVars params = new CalcVars(startUserId, preTaskUserId, actInstId, vars);
		return calculation.getTaskExecutor(nodeParticipant, params);
	}
	
	/**
	 * 进行 (与|或|排除)计算
	 * @param computeType
	 * @param userIdSet
	 * @param newUserSet
	 * @return
	 */
	private Set<String> computeUserSet(int computeType, Set<String> userIdSet, Set<String> newUserSet){
	    if (newUserSet == null) return userIdSet;
	    switch(computeType){
	    	case 0:
	    		userIdSet.addAll(newUserSet);
	    		break;
	    	case 1:
	    		Set<String> orLastSet = new HashSet<String>();
	    		Iterator<String> uIt = userIdSet.iterator();
	    		while (uIt.hasNext()) {
	    			String key = (String)uIt.next();
	    			if (newUserSet.contains(key)) {
	    				orLastSet.add(key);
	    			}
	  	      	}
	    		break;
	    	case 2:
	    		for (String newUserId : newUserSet) {
	    			userIdSet.remove(newUserId);
	    		}
	    		break;
	    }
	    return userIdSet;
	}
}