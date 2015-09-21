/**
 * @(#)com.casic27.platform.common.authorization.dao.IOrgRoleMapper.java
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
package com.casic27.platform.common.authorization.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.casic27.platform.common.log.annotation.OperateLog;
import com.casic27.platform.common.log.annotation.OperateLogType;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
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
