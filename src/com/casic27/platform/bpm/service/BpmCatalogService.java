/**
 * @(#)com.casic27.platform.bpm.service.ProcessTypeService
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casic27.platform.sys.constants.CommonConst;
import com.casic27.platform.sys.security.context.SecurityContext;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.util.StringUtils;
import com.casic27.platform.bpm.dao.IBpmCatalogMapper;
import com.casic27.platform.bpm.entity.BpmDefinition;
import com.casic27.platform.bpm.entity.BpmCatalog;
import com.casic27.platform.core.exception.BusinessException;
import com.casic27.platform.core.service.BaseService;

@Service("bpmCatalogService")
public class BpmCatalogService extends BaseService {
	@Autowired
	private IBpmCatalogMapper bpmCatalogMapper;
	
	@Autowired
	private BpmDefinitionService bpmDefinitionService;
	
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<BpmCatalog> findBpmCatalog(Map<String,Object> queryMap){
		return bpmCatalogMapper.findBpmCatalog(queryMap);
	}
	
	/**
	 * 查询所有流程分类
	 * @return
	 */
	public List<BpmCatalog> findAllBpmCatalog(String catalogType){
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("catalogType", catalogType);
		return findBpmCatalog(queryMap);
	}
	
	
	/**
	 * 根据ID查询
	 */
	public BpmCatalog getBpmCatalogById(String id){
		return bpmCatalogMapper.getBpmCatalogById(id);
	}
	
	
	/**
	 * 新增
	 */
	public void addBpmCatalog(BpmCatalog bpmCatalog){
		bpmCatalog.setId(CodeGenerator.getUUID32());
		bpmCatalog.setCatalogKey(bpmCatalog.getCatalogKey().toUpperCase());//将KEY转化为大写
		bpmCatalog.setCreateUser(SecurityContext.getCurrentUser().getZjid());
		bpmCatalog.setModifyUser(SecurityContext.getCurrentUser().getZjid());
		if(StringUtils.isEmpty(bpmCatalog.getParentId())){
			bpmCatalog.setParentId(CommonConst.TREE_ROOT_ID);
		}
		String layerCode = bpmCatalogMapper.getMaxLayerCode(bpmCatalog.getParentId());
		if(StringUtils.isEmpty(layerCode)){
			if("0".equals(bpmCatalog.getParentId())){
				layerCode = "001";
			}else{
				BpmCatalog parent = bpmCatalogMapper.getBpmCatalogById(bpmCatalog.getParentId());
				layerCode = parent.getLayerCode()+"001";
			}
		}else{
			layerCode = getNextLayCode(layerCode);
		}
		bpmCatalog.setLayerCode(layerCode);
		bpmCatalogMapper.addBpmCatalog(bpmCatalog);
	}
	
	/**
	 * 修改
	 */
	public void updateBpmCatalog(BpmCatalog bpmCatalog){
		bpmCatalog.setCatalogKey(bpmCatalog.getCatalogKey().toUpperCase());//将KEY转化为大写
		bpmCatalog.setModifyUser(SecurityContext.getCurrentUser().getZjid());
		bpmCatalogMapper.updateBpmCatalog(bpmCatalog);
	}
	
	/**
	 *删除
	 */
	public void deleteBpmCatalog(String id){
		int childCount = getChildCount(id);
		BpmDefinition bpmDefinition = new BpmDefinition();
		bpmDefinition.setTypeId(id);
		if(childCount>0){
			throw new BusinessException("不能删除，该节点下仍存在子节点!");
		}
		
		if(bpmDefinitionService.countBpmDefinition(bpmDefinition)>0){
			throw new BusinessException("不能删除，该节点下仍存在流程定义!");
		}
		bpmCatalogMapper.deleteBpmCatalog(id);
	}
	
	/**
	 * 根据条件统计记录数
	 */
	public int countBpmCatalog(BpmCatalog bpmCatalog){
		return bpmCatalogMapper.countBpmCatalog(bpmCatalog);
	}
	
	/**
	 * 获取某一层最大排序号
	 * @param parentId
	 * @return
	 */
	public int getMaxOrderNo(String parentId){
		Integer orderNo = bpmCatalogMapper.getMaxOrderNo(parentId);
		if(orderNo==null){
			return 1;
		}else{
			return orderNo+1;
		}
	}
	
	private int getChildCount(String parentId){
		BpmCatalog bpmCatalog = new BpmCatalog();
		bpmCatalog.setParentId(parentId);
		return countBpmCatalog(bpmCatalog);
	}
	
	private String getNextLayCode(String code){
		int len = 3;//每一级的长度
		String ch = "0";//自动补0
		String pre = code.substring(0, code.length()-len);
		int value = Integer.valueOf(code.substring(code.length()-len));
		String strValue = String.valueOf(value+1);
		StringBuffer sb = new StringBuffer(pre);
		for (int i = 0; i < len - strValue.length(); i++) {
			sb.append(ch);
		}
		sb.append(strValue);
        return sb.toString();
	}
}