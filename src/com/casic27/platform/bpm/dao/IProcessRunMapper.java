/**
\ * @(#)com.casic27.platform.bpm.dao.IProcessRunMapper
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
 
package com.casic27.platform.bpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.casic27.platform.bpm.entity.ProcessRun;
import com.casic27.platform.core.mybatis.annotation.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IProcessRunMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<ProcessRun> findProcessRun(Map<String,Object> queryMap);
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<ProcessRun> findProcessRunPaging(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	ProcessRun getProcessRunById(@Param("id")String id);
	
	/**
	 * 根据流程实例ID查询
	 * @param actInstId
	 * @return
	 */
	ProcessRun getProcessRunByActInstId(@Param("actInstId")String actInstId);
	
	/**
	 * 根据流程定义ID查询
	 * @param actInstId
	 * @return
	 */
	List<ProcessRun> getProcessRunByActDefId(@Param("actDefId")String actDefId);
	
	/**
	 * 新增
	 */
	void addProcessRun(ProcessRun processRun);
	
	/**
	 * 修改
	 */
	void updateProcessRun(ProcessRun processRun);
	
	/**
	 * 物理删除
	 */
	void delById(@Param("id")String id);
	
	/**
	 * 根据条件统计记录数
	 */
	int countProcessRun(ProcessRun processRun);
	
	/**
	 * 根据流程定义ID删除 
	 * @param actDefId
	 */
	void delByActDefId(@Param("actDefId")String actDefId);
	
	/**
	 * 经办事项
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<ProcessRun> getAlreadyMattersList(Map<String,Object> queryMap);
	
	/**
	 * 已结事项
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<ProcessRun> getCompleteMattersList(Map<String,Object> queryMap);
	/**
	 * 我的请求
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<ProcessRun> getMyRequestList(Map<String,Object> queryMap);
	
	/**
	 * 我的办结
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<ProcessRun> getMyCompleteList(Map<String,Object> queryMap);
	
	/**
	 * 我的草稿
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<ProcessRun> getMyDraftList(Map<String,Object> queryMap);
}