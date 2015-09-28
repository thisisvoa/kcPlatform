package com.kcp.platform.common.log.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kcp.platform.util.CodeGenerator;
import com.kcp.platform.common.log.dao.IInterfaceLogMapper;
import com.kcp.platform.common.log.entity.InterfaceLog;
import com.kcp.platform.core.service.BaseService;

@Service("interfaceLogService")
public class InterfaceLogService extends BaseService {
	@Autowired
	private IInterfaceLogMapper interfaceLogMapper;
	
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<InterfaceLog> findInterfaceLog(Map<String,Object> queryMap){
		return interfaceLogMapper.findInterfaceLog(queryMap);
	}
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 */
	public List<InterfaceLog> findInterfaceLogPaging(Map<String,Object> queryMap){
		return interfaceLogMapper.findInterfaceLogPaging(queryMap);
	}
	
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 */
	public List<InterfaceLog> findInterfaceLogByTablePaging(Map<String,Object> queryMap){
		return interfaceLogMapper.findInterfaceLogByTablePaging(queryMap);
	}
	
	
	/**
	 * 根据ID查询
	 */
	public InterfaceLog getInterfaceLogById(String id){
		return interfaceLogMapper.getInterfaceLogById(id);
	}
	
	/**
	 * 根据ID查询
	 */
	public InterfaceLog getInterfaceLogByTable(Map<String,Object> queryMap){
		return interfaceLogMapper.getInterfaceLogByTable(queryMap);
	}
	
	/**
	 * 新增
	 */
	public void addInterfaceLog(InterfaceLog interfaceLog){
		interfaceLog.setLogId(CodeGenerator.getUUID32());
		interfaceLogMapper.addInterfaceLog(interfaceLog);
	}
}