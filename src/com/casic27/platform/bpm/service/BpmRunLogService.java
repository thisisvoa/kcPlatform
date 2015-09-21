/**
 * @(#)com.casic27.platform.bpm.service.BpmRunLogService
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
package com.casic27.platform.bpm.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casic27.platform.sys.security.context.SecurityContext;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.bpm.dao.IBpmRunLogMapper;
import com.casic27.platform.bpm.dao.IProcessRunMapper;
import com.casic27.platform.bpm.entity.BpmRunLog;
import com.casic27.platform.bpm.entity.ProcessRun;
import com.casic27.platform.common.user.entity.User;

@Service
public class BpmRunLogService {
	@Autowired
	private IBpmRunLogMapper bpmRunLogMapper;
	
	@Autowired
	private IProcessRunMapper processRunMapper;
	
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<BpmRunLog> findBpmRunLog(Map<String,Object> queryMap){
		return bpmRunLogMapper.findBpmRunLog(queryMap);
	}
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 */
	public List<BpmRunLog> findBpmRunLogPaging(Map<String,Object> queryMap){
		return bpmRunLogMapper.findBpmRunLogPaging(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public BpmRunLog getBpmRunLogById(String id){
		return bpmRunLogMapper.getBpmRunLogById(id);
	}
	/**
	 * 新增
	 * @param processRun
	 * @param operatortype
	 * @param memo
	 */
	public void addBpmRunLog(ProcessRun processRun, Integer operatortype, String memo){
	     User user = SecurityContext.getCurrentUser();
	     String userId = "";
	     String userName = "系统";
	     if (user != null) {
	    	 userId = user.getZjid();
		     userName = user.getYhmc();
	     }
	     Date now = Calendar.getInstance().getTime();
	     BpmRunLog runLog = new BpmRunLog();
	     runLog.setId(CodeGenerator.getUUID32());
	     runLog.setUserId(userId);
	     runLog.setUserName(userName);
	     runLog.setRunId(processRun.getRunId());
	     runLog.setProcessSubject(processRun.getSubject());
	     runLog.setCreateTime(now);
	     runLog.setOperatorType(operatortype);
	     runLog.setMemo(memo);
	     bpmRunLogMapper.addBpmRunLog(runLog);
	}
	
	/**
	 * 新增
	 */
	public void addBpmRunLog(String runId, Integer operatorType, String memo){
		ProcessRun processRun = processRunMapper.getProcessRunById(runId);
		addBpmRunLog(processRun, operatorType, memo);
	}
	
	/**
	 * 修改
	 */
	public void updateBpmRunLog(BpmRunLog bpmRunLog){
		bpmRunLogMapper.updateBpmRunLog(bpmRunLog);
	}
	
	/**
	 *删除
	 */
	public void delByIds(String[] ids){
		for(String id:ids){
			bpmRunLogMapper.delById(id);
		}
	}
}