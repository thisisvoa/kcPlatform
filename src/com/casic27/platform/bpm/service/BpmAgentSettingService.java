/**
 * @(#)com.casic27.platform.bpm.service.BpmAgentSettingService
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

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.casic27.platform.util.BeanUtils;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.util.DateUtils;
import com.casic27.platform.util.StringUtils;
import com.casic27.platform.bpm.dao.IBpmAgentDefMapper;
import com.casic27.platform.bpm.dao.IBpmAgentSettingMapper;
import com.casic27.platform.bpm.entity.BpmAgentDef;
import com.casic27.platform.bpm.entity.BpmAgentSetting;
import com.casic27.platform.bpm.entity.BpmDefinition;
import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.common.user.service.UserService;

@Service
public class BpmAgentSettingService {
	@Autowired
	private IBpmAgentSettingMapper bpmAgentSettingMapper;
	
	@Autowired
	private IBpmAgentDefMapper bpmAgentDefMapper;
	
	@Autowired
	private BpmDefinitionService bpmDefinitionService; 
	
	@Autowired
	private UserService userService;
	
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<BpmAgentSetting> findBpmAgentSetting(Map<String,Object> queryMap){
		return bpmAgentSettingMapper.findBpmAgentSetting(queryMap);
	}
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 */
	public List<BpmAgentSetting> findBpmAgentSettingPaging(Map<String,Object> queryMap){
		return bpmAgentSettingMapper.findBpmAgentSettingPaging(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public BpmAgentSetting getBpmAgentSettingById(String id){
		return bpmAgentSettingMapper.getBpmAgentSettingById(id);
	}
	
	/**
	 * 
	 * @param bpmAgentSetting
	 */
	public void save(BpmAgentSetting bpmAgentSetting, List<BpmAgentDef> defs){
		if(StringUtils.isEmpty(bpmAgentSetting.getId())){
			bpmAgentSetting.setId(CodeGenerator.getUUID32());
			bpmAgentSettingMapper.addBpmAgentSetting(bpmAgentSetting);
		}else{
			bpmAgentSettingMapper.updateBpmAgentSetting(bpmAgentSetting);
			bpmAgentDefMapper.deleteBySettingId(bpmAgentSetting.getId());
		}
		if(bpmAgentSetting.getAuthType() == BpmAgentSetting.AUTHTYPE_PARTIAL){
			for(BpmAgentDef def:defs){
				def.setSettingId(bpmAgentSetting.getId());
				def.setId(CodeGenerator.getUUID32());
				bpmAgentDefMapper.addBpmAgentDef(def);
			}
		}
	}
	/**
	 * 获取流程代理用户
	 * @param delegateTask
	 * @param authId
	 * @return
	 */
	public User getAgent(DelegateTask delegateTask, String authId) {
		String actDefId = delegateTask.getProcessDefinitionId();
		BpmDefinition bpmDefinition = this.bpmDefinitionService.getBpmDefinitionByActDefId(actDefId);

		String flowKey = bpmDefinition.getDefKey();
		User user = null;
		Date currentDate = DateUtils.getTodayStart();
		
		BpmAgentSetting agentSetting = null;
		List<BpmAgentSetting> agentSettingList = bpmAgentSettingMapper.getValidByFlowAndAuthidAndDate(flowKey, authId, currentDate);
		if(BeanUtils.isNotEmpty(agentSettingList)){
			agentSetting = agentSettingList.get(0);
		}
		
		if (agentSetting != null) {
			user = this.userService.getUserById(agentSetting.getAgentId());
		}
		return user;
	}
	
	/**
	 *删除
	 */
	public void deleteBpmAgentSetting(String id){
		bpmAgentSettingMapper.deleteBpmAgentSetting(id);
		bpmAgentDefMapper.deleteBySettingId(id);
	}
}