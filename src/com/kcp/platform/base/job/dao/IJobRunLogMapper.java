package com.kcp.platform.base.job.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kcp.platform.base.job.entity.JobRunLog;
import com.kcp.platform.common.log.annotation.OperateLog;
import com.kcp.platform.core.mybatis.annotation.Pageable;
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