/**
 * @(#)com.casic27.platform.bpm.service.NodeSignService
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casic27.platform.util.BeanUtils;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.util.StringUtils;
import com.casic27.platform.bpm.dao.IBpmNodePrivilegeMapper;
import com.casic27.platform.bpm.dao.IBpmNodeSignMapper;
import com.casic27.platform.bpm.entity.BpmNodePrivilege;
import com.casic27.platform.bpm.entity.BpmNodeSign;
import com.casic27.platform.common.role.entity.Role;
import com.casic27.platform.core.engine.GroovyScriptEngine;
import com.casic27.platform.core.service.BaseService;

@Service("bpmNodeSignService")
public class BpmNodeSignService extends BaseService {
	@Autowired
	private IBpmNodeSignMapper nodeSignMapper;
	
	@Autowired
	private IBpmNodePrivilegeMapper bpmNodePrivilegeMapper;
	
	@Autowired
	private GroovyScriptEngine groovyScriptEngine;
	/**
	 * 查询列表
	 * @param queryMap
	 */
	public List<BpmNodeSign> findNodeSign(Map<String,Object> queryMap){
		return nodeSignMapper.findNodeSign(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public BpmNodeSign getNodeSignById(String id){
		return nodeSignMapper.getNodeSignById(id);
	}
	/**
	 * 新增
	 */
	public void addNodeSign(BpmNodeSign nodeSign){
		nodeSign.setSignId(CodeGenerator.getUUID32());
		nodeSignMapper.addNodeSign(nodeSign);
	}
	
	/**
	 * 修改
	 */
	public void updateNodeSign(BpmNodeSign nodeSign){
		nodeSignMapper.updateNodeSign(nodeSign);
	}
	
	
	/**
	 *删除
	 */
	public void deleteNodeSign(String id){
		nodeSignMapper.deleteNodeSign(id);
	}
	
	/**
	 * 根据流程定义ID和节点ID查询会签设置
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public BpmNodeSign getByDefIdAndNodeId(String actDefId,String nodeId){
		return nodeSignMapper.getByDefIdAndNodeId(actDefId, nodeId);
	}
	/**
	 * 根据流程定义ID获取会签信息
	 * @param defId
	 * @param nodeId
	 * @return
	 */
	public BpmNodeSign getNodeSignByActDefId(String actDefId, String nodeId){
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("actDefId", actDefId);
		queryMap.put("nodeId", nodeId);
		List<BpmNodeSign> list = findNodeSign(queryMap);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 保存会签信息
	 * @param nodeSign
	 */
	public void saveNodeSign(BpmNodeSign nodeSign, List<BpmNodePrivilege> list){
		if(StringUtils.isEmpty(nodeSign.getSignId())){
			addNodeSign(nodeSign);
		}else{
			updateNodeSign(nodeSign);
		}
		bpmNodePrivilegeMapper.delByDefIdAndNodeId(nodeSign.getActDefId(), nodeSign.getNodeId());
		for (BpmNodePrivilege vo : list) {
			vo.setPrivilegeId(CodeGenerator.getUUID32());
			vo.setActDefId(nodeSign.getActDefId());
			vo.setNodeId(nodeSign.getNodeId());
			this.bpmNodePrivilegeMapper.addBpmNodePrivilege(vo);
		}
	}
	/**
	 * @param queryMap
	 */
	public List<BpmNodePrivilege> getPrivilegesByDefIdAndNodeId(String actDefId, String nodeId){
		return bpmNodePrivilegeMapper.getPrivilegesByDefIdAndNodeId(actDefId, nodeId);
	}
	
	/**
	 * 根据ID查询
	 */
	public BpmNodePrivilege getBpmNodePrivilegeById(String id){
		return bpmNodePrivilegeMapper.getBpmNodePrivilegeById(id);
	}
	
	/**
	 * 根据流程定义ID和节点ID删除
	 * @param actDefId
	 * @param nodeId
	 */
	public void delByDefIdAndNodeId(String actDefId, String nodeId){
		bpmNodePrivilegeMapper.delByDefIdAndNodeId(actDefId, nodeId);
	}
	
	/**
	 * 判断用户是否有type指定的会签权限
	 * @param actDefId
	 * @param nodeId
	 * @param type
	 * @param userId
	 * @param orgId
	 * @return
	 */
	public boolean checkNodeSignPrivilege(String actDefId, String nodeId, BpmNodePrivilegeType type, String userId, String orgId, List<Role> roleList) {
		List<BpmNodePrivilege> list = this.bpmNodePrivilegeMapper.getPrivilegesByDefIdAndNodeIdAndMode(actDefId, nodeId, type.getMode());
		if (BeanUtils.isEmpty(list)) {
			return false;
		}
		for (BpmNodePrivilege rule : list) {
			switch (rule.getUserType().intValue()) {
				case 2://用户
					if (StringUtils.isNotEmpty(rule.getCmpIds())) {
						List<String> allowList = Arrays.asList(rule.getCmpIds().split(","));
						boolean rtn = containList(allowList, userId);
						if (rtn) {
							return true;
						}
					}
					break;
				case 3://角色
					if (StringUtils.isNotEmpty(rule.getCmpIds())) {
						if(BeanUtils.isNotEmpty(roleList)){
							List<String> allowList = Arrays.asList(rule.getCmpIds().split(","));
							for(Role role:roleList){
								boolean rtn = containList(allowList, String.valueOf(role.getZjid()));
								if (rtn) {
									return true;
								}
							}
						}
					}
					break;
				case 4://单位
					if (StringUtils.isNotEmpty(rule.getCmpIds())) {
						List<String> allowList = Arrays.asList(rule.getCmpIds().split(","));
						boolean rtn = containList(allowList, String.valueOf(orgId));
						if (rtn) {
							return true;
						}
					}
					break;
				case 12:
					Map<String,Object> vars = new HashMap<String,Object>();
					Object result = this.groovyScriptEngine.executeObject(rule.getCmpNames(), vars);
					if ((result != null) && ((result instanceof Set))) {
						Set set = (Set) result;
						if (set.contains(userId)) {
							return true;
						}
					}
					break;
				}
		}
		return false;
	}
	/**
	 * 判断list中是否存在值为id的字符串
	 * @param list
	 * @param id
	 * @return
	 */
	private boolean containList(List<String> list, String id) {
		for (String str : list) {
			if (str.equals(id)) {
				return true;
			}
		}
		return false;
	}
	public static enum BpmNodePrivilegeType {
		/**
		 * 所有特权
		 */
		DEFAULT(Short.valueOf("0")),
		/**
		 * 允许直接处理
		 */
		ALLOW_DIRECT(Short.valueOf("1")),
		
		/**
		 * 允许一票制
		 */
		ALLOW_ONE_VOTE(Short.valueOf("2")),
		
		/**
		 * 允许补签
		 */
		ALLOW_RETROACTIVE(Short.valueOf("3"));

		private Short mode;

		private BpmNodePrivilegeType(Short mode) {
			this.mode = mode;
		}
		
		private Short getMode() {
			return this.mode;
		}
	}
}