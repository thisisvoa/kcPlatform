package com.kcp.platform.common.log.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kcp.platform.util.CodeGenerator;
import com.kcp.platform.common.log.dao.ISysLogMapper;
import com.kcp.platform.common.log.entity.SysLog;
import com.kcp.platform.core.service.BaseService;

@Service("sysLogService")
public class SysLogService extends BaseService {
	@Autowired
	private ISysLogMapper sysLogMapper;
	
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<SysLog> findSysLog(Map<String,Object> queryMap){
		return sysLogMapper.findSysLog(queryMap);
	}
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 */
	public List<SysLog> findSysLogPaging(Map<String,Object> queryMap){
		return sysLogMapper.findSysLogPaging(queryMap);
	}

	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 */
	public List<SysLog> findSysLogByTablePaging(Map<String,Object> queryMap){
		return sysLogMapper.findSysLogByTablePaging(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public SysLog getSysLogById(String id){
		return sysLogMapper.getSysLogById(id);
	}
	
	/**
	 * 根据ID查询
	 */
	public SysLog getSysLogByTable(Map<String,Object> queryMap){
		return sysLogMapper.getSysLogByTable(queryMap);
	}
	
	/**
	 * 新增
	 */
	public void addSysLog(SysLog sysLog){
		sysLog.setLogId(CodeGenerator.getUUID32());
		sysLogMapper.addSysLog(sysLog);
	}
}