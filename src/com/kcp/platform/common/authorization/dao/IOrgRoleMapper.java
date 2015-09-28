package com.kcp.platform.common.authorization.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kcp.platform.common.log.annotation.OperateLog;
import com.kcp.platform.common.log.annotation.OperateLogType;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@kcp.com)
 */
@Repository
public interface IOrgRoleMapper {
	/**
	 * 根据单位查询角色树，若单位已有该角色，则返回的Map中ISCHECK值为1，否则为0
	 * @param orgId
	 * @return
	 */
	List<Map> getAssignedRoleList(@Param("orgId")String orgId, @Param("jsjb")String jsjb);
	
	/**
	 * 删除某组织下的所有组织角色关系
	 * @param orgId
	 */
	@OperateLog(isRecord = false)
	void deleteOrgRoleByOrgId(@Param("orgId")String orgId, @Param("jsjb")String jsjb);
	
	/**
	 * 为组织分配角色
	 * @param orgRole
	 */
	@OperateLog(isRecord = false)
	void insertOrgFunc(Map orgRole);
	
	/**
	 * 查询已分配了某角色的单位
	 * @param roleId
	 * @return
	 */
	List<Map> queryAssignedOrgByRole(@Param("roleId")String roleId);
	
	/**
	 * 删除角色下的所有组织角色关系
	 * @param roleId
	 */
	@OperateLog(isRecord = false)
	void deleteOrgRoleByRoleId(@Param("roleId")String roleId);
}
