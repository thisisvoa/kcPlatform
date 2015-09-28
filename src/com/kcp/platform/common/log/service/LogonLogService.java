package com.kcp.platform.common.log.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kcp.platform.util.CodeGenerator;
import com.kcp.platform.common.log.dao.ILogonLogMapper;
import com.kcp.platform.common.log.entity.LogonLog;
import com.kcp.platform.core.service.BaseService;

@Service("logonLogService")
public class LogonLogService extends BaseService {
	@Autowired
	private ILogonLogMapper logonLogMapper;
	
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<LogonLog> findLogonLog(Map<String,Object> queryMap){
		return logonLogMapper.findLogonLog(queryMap);
	}
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 */
	public List<LogonLog> findLogonLogPaging(Map<String,Object> queryMap){
		return logonLogMapper.findLogonLogPaging(queryMap);
	}

	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 */
	public List<LogonLog> findLogonLogByTablePaging(Map<String,Object> queryMap){
		return logonLogMapper.findLogonLogByTablePaging(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public LogonLog getLogonLogById(String id){
		return logonLogMapper.getLogonLogById(id);
	}
	
	/**
	 * 根据ID查询
	 */
	public LogonLog getLogonLogByTable(Map<String,Object> queryMap){
		return logonLogMapper.getLogonLogByTable(queryMap);
	}
	
	/**
	 * 新增
	 */
	public void addLogonLog(LogonLog logonLog){
		logonLog.setLogId(CodeGenerator.getUUID32());
		logonLogMapper.addLogonLog(logonLog);
	}
	
	/**
	 * 修改登出时间
	 * @param sessionId
	 * @param logoutTime
	 */
	public void updateLogoutTime(String sessionId, Date logoutTime){
		logonLogMapper.updateLogoutTime(sessionId, logoutTime);
	}
}