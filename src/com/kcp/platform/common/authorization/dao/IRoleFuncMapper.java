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
