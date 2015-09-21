/**
 * @(#)com.casic27.platform.common.authorization.dao.IOrgFuncMapper.java
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
public interface IOrgFuncMapper {
	/**
	 * 查询单位已分配的功能列表
	 * @param orgId
	 * @return
	 */
	List<Map> queryFuncTreeList(@Param("orgId")String orgId);
	
	/**
	 * 根绝单位Id删除单位功能关系
	 * @param orgId
	 */
	void deleteOrgFuncByOrgId(@Param("orgId")String orgId);
	
	/**
	 * 插入组织功能关系
	 * @param orgFunc
	 */
	void insertOrgFunc(Map orgFunc);
	
	/**
	 * 查询已分配的了某功能的单位列表
	 * @param funcId
	 * @return
	 */
	List<Map> queryAssignedOrgByFunc(@Param("funcId")String funcId);
	
	/**
	 * 根绝功能Id删除单位功能关系
	 * @param funcId
	 */
	void deleteOrgFuncByFuncId(@Param("funcId")String funcId);
	
}
