package com.kcp.platform.common.authorization.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kcp.platform.base.cache.annotation.CacheEvict;
import com.kcp.platform.base.event.EventDispatcher;
import com.kcp.platform.common.authorization.dao.IOrgFuncMapper;
import com.kcp.platform.common.authorization.event.AuthorizationEvent;
import com.kcp.platform.core.service.BaseService;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
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
