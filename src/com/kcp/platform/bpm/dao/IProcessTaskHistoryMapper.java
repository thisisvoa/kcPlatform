package com.kcp.platform.bpm.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kcp.platform.bpm.entity.ProcessTaskHistory;

@Repository
public interface IProcessTaskHistoryMapper {
	/**
	 * 添加流程任务历史
	 * @param taskHistory
	 */
	void addTaskHistory(ProcessTaskHistory taskHistory);
	
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	ProcessTaskHistory getTaskHistoryById(@Param("id")String id);
	
}