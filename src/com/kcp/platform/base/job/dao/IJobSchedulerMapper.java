 
package com.kcp.platform.base.job.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kcp.platform.base.job.entity.JobScheduler;
import com.kcp.platform.common.log.annotation.OperateLog;

import org.springframework.stereotype.Repository;

@Repository
public interface IJobSchedulerMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	@OperateLog(isRecord = false)
	List<JobScheduler> findJobScheduler(Map<String,Object> queryMap);
	
	
	/**
	 * 根据ip和port查询JobScheduler
	 * @param hostUrlList
	 * @return
	 */
	List<JobScheduler> getJobSchedulerByHostUrl(List<String> hostUrlList);
	
	/**
	 * 获取优先级最高的任务调度管理器
	 * @param hostUrlList
	 * @return
	 */
	List<JobScheduler> getHighestPriorityJobScheduler();
	
	/**
	 * 获取优先级最高的活动任务调度管理器
	 * @param hostUrlList
	 * @return
	 */
	List<JobScheduler> getHighestPriorityActiveJobScheduler();
	
	/**
	 * 根据Id进行查询
	 */
	JobScheduler getJobSchedulerById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	@OperateLog(isRecord = false)
	void addJobScheduler(JobScheduler jobScheduler);
	
	/**
	 * 修改
	 */
	@OperateLog(isRecord = false)
	void updateJobScheduler(JobScheduler jobScheduler);
	
	/**
	 * 刷新任务调度服务器的最后活动时间
	 * @param hostUrlList
	 */
	void updateLastActiveTime(@Param("hostUrlList")List<String> hostUrlList);
	
	/**
	 * 将hostUrls列表都设置为当前的全局任务调度服务器
	 * @param hostUrlList
	 */
	void makeCurrent(@Param("hostUrlList")List<String> hostUrlList);
	
	/**
	 * 将除hostUrl之外的其它记录设置非当前的任务调度服务状态的值
	 * @param hostUrlList
	 */
	void makeNotCurrentWhenOther(@Param("hostUrlList")List<String> hostUrlList);
	
	/**
	 * 只保持最高优先级的为当前全局，其它的设置为非当前全局
	 */
	void keepCurrentOnlyHighestPriority();
	
	/**
	 * 将超过指定时间非活动的任务调度服务解装载 
	 * @param unactiveTimeInSeconds
	 */
	void unmountUnactiveJobScheduler(@Param("unactiveTimeInSeconds")long unactiveTimeInSeconds);
	/**
	 * 物理删除
	 */
	@OperateLog(isRecord = false)
	void deleteJobScheduler(@Param("id")String id);
	
	/**
	 * 根据条件统计记录数
	 */
	int countJobScheduler(JobScheduler jobScheduler);
	
}