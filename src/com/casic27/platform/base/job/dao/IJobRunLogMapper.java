/**
 * @(#)com.casic27.platform.dao.IJobRunLogMapper
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
 
package com.casic27.platform.base.job.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.casic27.platform.base.job.entity.JobRunLog;
import com.casic27.platform.common.log.annotation.OperateLog;
import com.casic27.platform.core.mybatis.annotation.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IJobRunLogMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<JobRunLog> findJobRunLog(Map<String,Object> queryMap);
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 * @return
	 */
	@Pageable
	@OperateLog(isRecord = false)
	List<JobRunLog> findJobRunLogPaging(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	JobRunLog getJobRunLogById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addJobRunLog(JobRunLog jobRunLog);
	
	/**
	 * 物理删除
	 */
	@OperateLog(isRecord = false)
	void deleteJobRunLog(@Param("id")String id);
	
	/**
	 * 批量删除
	 * @param map
	 */
	void batchDeleteJobRunLog(Map<String,Object> map);
	
}