package com.kcp.platform.common.authorization.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcp.platform.base.cache.annotation.CacheEvict;
import com.kcp.platform.base.event.EventDispatcher;
import com.kcp.platform.common.authorization.dao.IRoleFuncMapper;
import com.kcp.platform.common.authorization.event.AuthorizationEvent;
import com.kcp.platform.common.func.entity.Function;
import com.kcp.platform.common.log.Logger;
import com.kcp.platform.common.log.annotation.OperateLogType;
import com.kcp.platform.common.log.core.SqlContextHolder;
import com.kcp.platform.common.role.entity.Role;
import com.kcp.platform.core.service.BaseService;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 */
@Service
public class RoleFuncService extends BaseService {
	@Autowired
	private IRoleFuncMapper roleFuncMapper;
	
	/**
	 * 查询功能树
	 * @param roleId
	 * @return
	 */
	public List<Map> queryFuncTreeList(String roleId){
		return  roleFuncMapper.queryFuncTreeList(roleId);
	}
	
	/**
	 * 分配功能给角色
	 * 注：CacheEvict注解用于清除用户权限缓存数据
	 * @param roleId
	 * @param funcList
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger",evitAll=true)
	public void assignFuncToRole(Role role, List<String> funcList){
		roleFuncMapper.deleteRoleFuncByRoleId(role.getZjid());
		List<Map> roleFuncList = new ArrayList();
		for(String funcId:funcList){
			Map map = new HashMap();
			map.put("roleId", role.getZjid());
			map.put("funcId", funcId);
			roleFuncMapper.insertRoleFunc(map);
		}
		String funcIdStr = StringUtils.join(funcList.toArray(),",");
		String sql = SqlContextHolder.getSql();
		Logger.getInstance().addSysLog(OperateLogType.AUTH.value(), sql, "权限分配管理", "[为角色分配功能] 为角色[角色名称："+role.getJsmc()+"] [角色代码："+role.getJsdm()+"]分配功能[功能ID："+funcIdStr+"]");
		EventDispatcher.publishEvent(new AuthorizationEvent());
	}
	
	/**
	 * 根据功能Id查询功能角色关系列表，若功能已分配给角色则结果中ISCheck属性值为1
	 * @param funcId
	 * @return
	 */
	public List<Map> getAssignedRoleList(String funcId){
		return roleFuncMapper.getAssignedRoleList(funcId);
	}
	
	/**
	 * 为功能分配角色
	 * 注：CacheEvict注解用于清除用户权限缓存数据
	 * @param funcId
	 * @param roleList
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger",evitAll=true)
	public void assignRoleToFunc(Function func, List<String> roleList){
		roleFuncMapper.deleteRoleFuncByFuncId(func.getZjId());
		List<Map> roleFuncList = new ArrayList();
		for(String roleId:roleList){
			Map map = new HashMap();
			map.put("roleId", roleId);
			map.put("funcId", func.getZjId());
			roleFuncMapper.insertRoleFunc(map);
		}
		
		String roleIdStr = StringUtils.join(roleList.toArray(),",");
		String sql = SqlContextHolder.getSql();
		Logger.getInstance().addSysLog(OperateLogType.AUTH.value(), sql, "权限分配管理", "[为功能分配角色] 为功能[功能名称："+func.getGnmc()+"] [功能代码："+func.getGndm()+"]分配角色[角色ID："+roleIdStr+"]");
		EventDispatcher.publishEvent(new AuthorizationEvent());
	}
}
