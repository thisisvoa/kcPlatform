/**
 * @(#)com.casic27.platform.common.log.service.InterfaceLogService
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
package com.casic27.platform.common.log.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.common.log.dao.IInterfaceLogMapper;
import com.casic27.platform.common.log.entity.InterfaceLog;
import com.casic27.platform.core.service.BaseService;

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