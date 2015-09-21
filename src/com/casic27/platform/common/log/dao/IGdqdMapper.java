/**
 * @(#)com.casic27.platform.common.log.dao.IGdqdMapper
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright: Copyright(c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
 
package com.casic27.platform.common.log.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.casic27.platform.common.log.entity.Gdqd;
import com.casic27.platform.core.mybatis.annotation.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IGdqdMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<Gdqd> findGdqd(Map<String,Object> queryMap);
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<Gdqd> findGdqdPaging(Map<String,Object> queryMap);

	/**
	 * 根据Id进行查询
	 */
	Gdqd getGdqdById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addGdqd(Gdqd gdqd);
	
	/**
	 * 修改
	 */
	void updateGdqd(Gdqd gdqd);
	
	/**
	 * 物理删除
	 */
	void deleteGdqd(@Param("id")String id);
	
	/**
	 * 根据条件统计记录数
	 */
	int countGdqd(Gdqd gdqd);
	
}