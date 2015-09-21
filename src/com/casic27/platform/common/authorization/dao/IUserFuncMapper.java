/**
 * @(#)com.casic27.platform.common.authorization.dao.IUserFuncMapper.java
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

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
 */
@Repository
public interface IUserFuncMapper {
	
	/**
	 * 根据单位查询角色树，若单位已有该角色，则返回的Map中ISCHECK值为1，否则为0
	 * @param orgId
	 * @return
	 */
	List<Map> queryUserFucList(@Param("userId")String userId);
	
	/**
	 * 删除某组织下的所有角色
	 * @param orgId
	 */
	void deleteUserFuncByUserId(@Param("userId")String userId);
	
	/**
	 * 为组织分配角色
	 * @param orgRole
	 */
	void insertUserFunc(Map userFunc);
	
	/**
	 * 查询某用户
	 * @param funcId
	 * @return
	 */
	List<Map> queryAssignedUserByFunc(@Param("funcId")String funcId);
	
	/**
	 * 删除某功能下的所有用户功能关系
	 * @param orgId
	 */
	void deleteUserFuncByFuncId(@Param("funcId")String funcId);
	
}
