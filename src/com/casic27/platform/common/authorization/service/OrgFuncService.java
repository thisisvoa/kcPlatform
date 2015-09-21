/**
 * @(#)com.casic27.platform.common.authorization.service.OrgFuncService.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-4-24
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.common.authorization.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casic27.platform.base.cache.annotation.CacheEvict;
import com.casic27.platform.base.event.EventDispatcher;
import com.casic27.platform.common.authorization.dao.IOrgFuncMapper;
import com.casic27.platform.common.authorization.event.AuthorizationEvent;
import com.casic27.platform.core.service.BaseService;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
 */
@Service
@Transactional
public class OrgFuncService extends BaseService {
	@Autowired
	private IOrgFuncMapper orgFuncMapper;
	/**
	 * 查询单位分配的功能列表，已分配ISCHECK属性值为1，否则为0
	 * @param orgId
	 * @return
	 */
	public List<Map> queryFuncTreeList(String orgId){
		return orgFuncMapper.queryFuncTreeList(orgId);
	}
	
	/**
	 * 为功能分配单位
	 * 注：CacheEvict注解用于清除用户权限缓存数据
	 * @param orgId
	 * @param funcList
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger", evitAll=true)
	public void assignFuncToOrg(String orgId, List<String> funcList){
		orgFuncMapper.deleteOrgFuncByOrgId(orgId);
		for(String funcId:funcList){
			Map map = new HashMap();
			map.put("orgId", orgId);
			map.put("funcId", funcId);
			orgFuncMapper.insertOrgFunc(map);
		}
		
		EventDispatcher.publishEvent(new AuthorizationEvent());
	}
	
	/**
	 * 查询已经分配了某功能的单位列表
	 * @param funcId
	 * @return
	 */
	public List<Map> queryAssignedOrgByFunc(String funcId){
		return orgFuncMapper.queryAssignedOrgByFunc(funcId);
	}
	
	/**
	 * 为功能分配单位
	 * 注：CacheEvict注解用于清除用户权限缓存数据
	 * @param orgId
	 * @param funcList
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger",evitAll=true)
	public void assignOrgToFunc(String funcId, List<String> orgList){
		orgFuncMapper.deleteOrgFuncByFuncId(funcId);
		for(String orgId:orgList){
			Map map = new HashMap();
			map.put("orgId", orgId);
			map.put("funcId", funcId);
			orgFuncMapper.insertOrgFunc(map);
		}
		EventDispatcher.publishEvent(new AuthorizationEvent());
	}
}
