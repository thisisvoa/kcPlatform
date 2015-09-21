/**
 * @(#)com.casic27.platform.common.authorization.dao.IRoleOperationMapper.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-4-21
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
public interface IRoleFuncMapper {
	/**
	 * 查询功能树，若已经分配给角色则结果中ISCHECK属性为1，否则为0
	 * @param roleId
	 * @return
	 */
	List<Map> queryFuncTreeList(@Param(value="roleId")String roleId);
	
	/**
	 * 根绝角色Id删除功能角色关系
	 * @param roleId
	 */
	@OperateLog(isRecord=false)
	void deleteRoleFuncByRoleId(@Param(value="roleId")String roleId);
	
	/**
	 * 新增角色功能关系
	 * @param roleFunc
	 */
	@OperateLog(isRecord=false)
	void insertRoleFunc(Map roleFunc);
	
	
	/**
	 * 查询角色树，若已经分配l某功能则结果中ISCHECK属性为1，否则为0
	 * @param orgId
	 * @return
	 */
	List<Map> getAssignedRoleList(@Param("funcId")String funcId);
	
	/**
	 * 根绝功能Id删除角色功能关系
	 * @param roleId
	 */
	@OperateLog(isRecord=false)
	void deleteRoleFuncByFuncId(@Param("funcId")String roleId);
}
