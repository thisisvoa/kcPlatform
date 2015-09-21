/**
 * @(#)com.casic27.platform.dao.IJobCommandMapper
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

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.casic27.platform.base.job.entity.JobCommand;
import com.casic27.platform.common.log.annotation.OperateLog;
import com.casic27.platform.core.mybatis.annotation.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IJobCommandMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<JobCommand> findJobCommand(Map<String,Object> queryMap);
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<JobCommand> findJobCommandPaging(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	JobCommand getJobCommandById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	@OperateLog(isRecord = false)
	void addJobCommand(JobCommand jobCommand);
	
	/**
	 * 修改
	 */
	void updateJobCommand(JobCommand jobCommand);
	
	/**
	 * 物理删除
	 */
	void deleteJobCommand(@Param("id")String id);
	
	/**
	 * 删除某一天之前的命令
	 * @param date
	 */
	void deleteJobComandIssuedBeforeOneDay(@Param("date")Date date);
}