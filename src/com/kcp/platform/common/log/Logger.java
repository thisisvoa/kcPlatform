package com.kcp.platform.common.log;

import java.util.Date;

import com.kcp.platform.common.log.entity.InterfaceLog;
import com.kcp.platform.common.log.entity.LogonLog;
import com.kcp.platform.common.log.entity.SysLog;
import com.kcp.platform.common.log.service.InterfaceLogService;
import com.kcp.platform.common.log.service.LogonLogService;
import com.kcp.platform.common.log.service.SysLogService;
import com.kcp.platform.common.org.entity.Org;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.sys.context.SpringContextHolder;
import com.kcp.platform.sys.security.context.SecurityContext;
import com.kcp.platform.util.JsonParser;

/**
 * 日志对外接口类
 * 
 * @author Administrator
 * 
 */
public class Logger {
	private static Logger logger;

	private SysLogService sysLogService;

	private InterfaceLogService interfaceLogService;

	private LogonLogService logonLogService;

	private Logger() {
		sysLogService = SpringContextHolder.getBean(SysLogService.class);
		interfaceLogService = SpringContextHolder
				.getBean(InterfaceLogService.class);
		logonLogService = SpringContextHolder.getBean(LogonLogService.class);
	}

	public static Logger getInstance() {
		if (logger == null)
			logger = new Logger();
		return logger;
	}
	
	/**
	 * 保存操作日志
	 * @param logType
	 * @param operateContent
	 * @param moduleName
	 * @param operateDesc
	 */
	public void addSysLog(String logType, String operateContent,
			String moduleName, String operateDesc) {
		SysLog log = new SysLog();
		log.setLogType(logType);
		log.setOperateContent(operateContent);
		log.setModuleName(moduleName);
		log.setOperateDesc(operateDesc);
		addSysLog(log);
	}
	
	/**
	 * 保存操作日志
	 * @param logType
	 * @param operateContent
	 * @param moduleName
	 * @param operateDesc
	 * @param operateResult
	 */
	public void addSysLog(String logType, String operateContent,
			String moduleName, String operateDesc, String operateResult) {
		SysLog log = new SysLog();
		log.setLogType(logType);
		log.setOperateContent(operateContent);
		log.setModuleName(moduleName);
		log.setOperateDesc(operateDesc);
		log.setOperateResult(operateResult);
		addSysLog(log);
	}
	
	/**
	 * 保存操作日志
	 * @param log
	 */
	public void addSysLog(SysLog log) {
		User user = SecurityContext.getCurrentUser();
		Org org = SecurityContext.getCurrentUserOrg();
		String ip = SecurityContext.getCurrentRemoteIp();
		if(user!=null){
			log.setIdCard(user.getSfzh());
			log.setPoliceId(user.getJybh());
			log.setUserId(user.getZjid());
			log.setUserName(user.getYhmc());
			log.setLoginId(user.getYhdlzh());
		}
		if(org!=null){
			log.setOrgId(org.getZjid());
			log.setOrgName(org.getDwmc());
			log.setOrgNo(org.getDwbh());
		}
		log.setTerminalId(ip);
		log.setOperateTime(new Date());
		sysLogService.addSysLog(log);
	}
	
	/**
	 * 保存接口日志
	 * @param resultCount
	 * @param callerName
	 * @param interfaceDesc
	 */
	public void addInterfaceLog(long resultCount, String callerName,
			String interfaceDesc) {
		InterfaceLog log = new InterfaceLog();
		log.setResultCount(resultCount);
		log.setCallerName(callerName);
		log.setInterfaceDesc(interfaceDesc);
		log.setParamVariable("");
		log.setCallSuccess(CommonConst.YES);
		addInterfaceLog(log);
	}
	
	/**
	 * 保存接口日志
	 * @param resultCount
	 * @param callerName
	 * @param interfaceDesc
	 * @param paramVariable
	 */
	public void addInterfaceLog(long resultCount, String callerName,
			String interfaceDesc, Object paramVariable) {
		InterfaceLog log = new InterfaceLog();
		log.setResultCount(resultCount);
		log.setCallerName(callerName);
		log.setInterfaceDesc(interfaceDesc);
		log.setParamVariable(JsonParser.convertObjectToJson(paramVariable));
		log.setCallSuccess(CommonConst.YES);
		addInterfaceLog(log);
	}
	
	/**
	 * 保存接口日志
	 * @param resultCount
	 * @param callerName
	 * @param interfaceDesc
	 * @param paramVariable
	 * @param callSuccess
	 */
	public void addInterfaceLog(long resultCount, String callerName,
			String interfaceDesc, Object paramVariable, String callSuccess) {
		InterfaceLog log = new InterfaceLog();
		log.setResultCount(resultCount);
		log.setCallerName(callerName);
		log.setInterfaceDesc(interfaceDesc);
		log.setParamVariable(JsonParser.convertObjectToJson(paramVariable));
		log.setCallSuccess(callSuccess);
		addInterfaceLog(log);
	}
	
	/**
	 * 保存接口日志
	 * @param log
	 */
	public void addInterfaceLog(InterfaceLog log) {
		User user = SecurityContext.getCurrentUser();
		Org org = SecurityContext.getCurrentUserOrg();
		String ip = SecurityContext.getCurrentRemoteIp();
		if(user!=null){
			log.setUserId(user.getZjid());
			log.setUserName(user.getYhmc());
			log.setLoginId(user.getYhdlzh());
		}
		if(org!=null){
			log.setOrgId(org.getZjid());
			log.setOrgName(org.getDwmc());
			log.setOrgNo(org.getDwbh());
		}
		log.setTerminalId(ip);
		log.setCallTime(new Date());
		interfaceLogService.addInterfaceLog(log);
	}
	
	/**
	 * 保存登录日志
	 * @param log
	 */
	public void addLogonLog(LogonLog log){
		logonLogService.addLogonLog(log);
	}
	
	public void updateLogoutTime(String sessionId, Date logoutTime){
		logonLogService.updateLogoutTime(sessionId, logoutTime);
	}

}
