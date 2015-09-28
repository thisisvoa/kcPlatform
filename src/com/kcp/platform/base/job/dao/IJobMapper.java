package com.kcp.platform.base.job.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kcp.platform.base.job.entity.Job;
import com.kcp.platform.common.log.annotation.OperateLog;
import com.kcp.platform.core.mybatis.annotation.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IJobMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<Job> findJob(Map<String,Object> queryMap);
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 * @return
	 */
	@Pageable
	@OperateLog(isRecord = false)
	List<Job> findJobPaging(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	Job getJobById(@Param("id")String id);
	
	/**
	 * 根据任务分组及任务名称获取对应的Job
	 * @param job
	 * @return
	 */
	Job getJobByFullName(Job job);
	
	/**
	 * 新增
	 */
	@OperateLog(isRecord = false)
	void addJob(Job job);
	
	/**
	 * 修改
	 */
	@OperateLog(isRecord = false)
	void updateJob(Job job);
	
	/**
	 * 更新jobGroupName和jobName的最后活动时间，以数据库时间为准
	 * @param job
	 */
	void updateLastActiveTime(Job job);
	/**
	 * 物理删除
	 */
	@OperateLog(isRecord = false)
	void deleteJob(@Param("id")String id);
	
	/**
	 * 根据条件统计记录数
	 */
	int countJob(Job job);
	
	/**
	 * 获取数据库当前时间
	 * @return
	 */
	Date getCurrDbDate();
	
}