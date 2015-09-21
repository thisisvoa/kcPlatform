/**
 * @(#)com.casic27.platform.common.authorization.service.UserFuncService.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-4-25
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.common.authorization.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casic27.platform.base.cache.annotation.CacheEvict;
import com.casic27.platform.base.event.EventDispatcher;
import com.casic27.platform.common.authorization.dao.IUserFuncMapper;
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
public class UserFuncService extends BaseService {
	@Autowired
	private IUserFuncMapper userFuncMapper;
	
	/**
	 * 查询用户拥有的功能
	 * @param userId
	 * @return 返回所有的功能（包括菜单），若用户拥有该功能则记录的ISCHECK属性值为1
	 */
	public List<Map> queryUserFucList(String userId){
		return userFuncMapper.queryUserFucList(userId);
	}
	
	/**
	 * 为用户分配功能
	 * 注：CacheEvict注解用于清除用户权限缓存数据
	 * @param userId
	 * @param funcList
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger",evitAll=true)
	public void assignFuncToUser(String userId, List<String> funcList){
		userFuncMapper.deleteUserFuncByUserId(userId);
		for(String funcId:funcList){
			Map<String,String> map = new HashMap<String,String>();
			map.put("userId", userId);
			map.put("funcId", funcId);
			userFuncMapper.insertUserFunc(map);
		}
		EventDispatcher.publishEvent(new AuthorizationEvent());
	}
	/**
	 * 查询某功能已分配的用户
	 * @param funcId
	 * @return
	 */
	public List<Map> queryAssignedUserByFunc(String funcId){
		return userFuncMapper.queryAssignedUserByFunc(funcId);
	}
	
	/**
	 * 为功能分配用户
	 * 注：CacheEvict注解用于清除用户权限缓存数据
	 * @param userId
	 * @param funcList
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger",evitAll=true)
	public void assignUserToFunc(String funcId, List<String> userList){
		userFuncMapper.deleteUserFuncByFuncId(funcId);
		for(String userId:userList){
			Map<String,String> map = new HashMap<String,String>();
			map.put("userId", userId);
			map.put("funcId", funcId);
			userFuncMapper.insertUserFunc(map);
		}
		EventDispatcher.publishEvent(new AuthorizationEvent());
	}
}
