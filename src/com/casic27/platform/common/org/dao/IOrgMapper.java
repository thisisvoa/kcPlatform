/**
 * @(#)com.casic27.platform.common.org.dao.IOrgMapper.java
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
package com.casic27.platform.common.org.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.casic27.platform.common.log.annotation.OperateLog;
import com.casic27.platform.common.log.annotation.OperateLogType;
import com.casic27.platform.common.org.entity.Org;
import com.casic27.platform.core.mybatis.annotation.Pageable;

/**
 *
 *类描述：
 * 
 *@Author： 宗斌(zongbin@casic27.com)
 *@Version：1.0
 */
@Repository
public interface IOrgMapper {
	/**
	 * 查询单位(不分页)
	 * @param org
	 * @return
	 */
	public List<Org> findOrg(Map<String,Object> queryMap);
	
	/**
	 * 根据单位ID查询该单位及所有子单位列表信息 
	 * @param org
	 * @return
	 */
	@Pageable
	@OperateLog(isRecord=false)
	public List<Map<String, Object>>  findOrgPaging(Map<String,Object> queryMap);
	
	/**
	 * 删除单位
	 * @param org
	 * @return
	 */
	@OperateLog(isRecord=false)
	public void deleteOrg(Org org);
	
	/**
	 * 新增单位信息
	 * @param org
	 * @return
	 */
	@OperateLog(isRecord=false)
	public void addOrg(Org org);
	
	/**
	 * 修改单位信息
	 * @param org
	 * @return
	 */
	@OperateLog(isRecord=false)
	public void updateOrg(Org org);
	
	/**
	 * 查询某个单位的所有上级单位
	 * @param orgId
	 * @return
	 */
	List<Map> queryParentOrgList(@Param("orgId")String orgId);
	
	/**
	 * 查询某个单位的所有上级单位
	 * @param orgId
	 * @return
	 */
	List<Org> getParentOrgList(@Param("orgId")String orgId);
	
	/**
	 * 根据ID查询单位信息
	 * @param orgId
	 * @return
	 */
	@OperateLog(isRecord=false)
	public Org getOrgById(String orgId);
	
	/**
	 * 根据单位编号查询单位信息
	 * @param orgId
	 * @return
	 */
	public Org getOrgByNo(@Param("orgNo")String orgNo);
	
	/**
	 * 统计个数
	 * @param org
	 * @return
	 */
	public int statiscOrg(Org org);
	
	/**
	 * 修改记录使用标志
	 * @param org
	 */
	@OperateLog(isRecord=false)
	public void updateOrgJlyxzt(Org org);
	
	/**
	 * 是否是儿子节点
	 * @param queryMap
	 * @return
	 */
	public int isChildOrg(@Param("parent")String parent, @Param("child")String child);
	
	/**
	 * 根据ID查询组织
	 * @param ids
	 * @return
	 */
	public List<Map<String,Object>> getOrgByIds(String[] ids);
	
}
