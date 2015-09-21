/**
 * @(#)com.casic27.platform.bpm.dao.IBpmAgentSettingMapper
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

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.casic27.platform.bpm.entity.BpmAgentSetting;
import com.casic27.platform.core.mybatis.annotation.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IBpmAgentSettingMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<BpmAgentSetting> findBpmAgentSetting(Map<String,Object> queryMap);
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<BpmAgentSetting> findBpmAgentSettingPaging(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	BpmAgentSetting getBpmAgentSettingById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addBpmAgentSetting(BpmAgentSetting bpmAgentSetting);
	
	/**
	 * 修改
	 */
	void updateBpmAgentSetting(BpmAgentSetting bpmAgentSetting);
	
	/**
	 * 物理删除
	 */
	void deleteBpmAgentSetting(@Param("id")String id);
	
	/**
	 * 根据条件统计记录数
	 */
	int countBpmAgentSetting(BpmAgentSetting bpmAgentSetting);
	
	/**
	 * 获取流程key为flowKey，代理人为authId在date 指定的日期下有效的代理设置
	 * @param flowKey
	 * @param authId
	 * @param date
	 * @return
	 */
	List<BpmAgentSetting> getValidByFlowAndAuthidAndDate(@Param("flowKey")String flowKey, @Param("authId")String authId, @Param("date")Date date);
	
}