package com.casic27.platform.web.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.casic27.platform.common.org.entity.Org;
import com.casic27.platform.common.para.entity.Parameter;
import com.casic27.platform.common.para.service.ParaService;
import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.sys.context.SpringContextHolder;
import com.casic27.platform.sys.security.context.SecurityContext;
import com.casic27.platform.util.DateUtils;

/**
 * 用于存储用户信息
 * @author Administrator
 *
 */
public class OnlineStore {
	private static String HIS_ONLINE_NAME = "HIS_ONLINE";
	private static OnlineStore onlineStore = new OnlineStore();
	private List<Map<String,Object>> userList;
	private ParaService paraService;
	
	private OnlineStore(){
		userList = new CopyOnWriteArrayList<Map<String,Object>>();
		paraService = SpringContextHolder.getBean(ParaService.class);
		Parameter para = paraService.getParaByCsdm(HIS_ONLINE_NAME);
		if(para==null){//若系统参数表中不存在[历史在线人数]这个参数，则添加之
			para = new Parameter();
			para.setCsmc("历史在线人数");
			para.setCsdm(HIS_ONLINE_NAME);
			para.setCsz("0");
			para.setJlxzsj(DateUtils.getCurrentDateTime14());
			para.setJlxgsj(DateUtils.getCurrentDateTime14());
			para.setJlyxzt("1");
			para.setSybz("1");
			paraService.insert(para);
		}
	}
	
	public static OnlineStore getInstance(){
		return onlineStore;
	}
	
	/**
	 * 用户登录后调用
	 * @param sessionId
	 */
	public void login(String sessionId){
		User user = SecurityContext.getCurrentUser();
    	Org org = SecurityContext.getCurrentUserOrg();
    	Map<String,Object> userInfo = new HashMap<String,Object>();
    	userInfo.put("zjid", user.getZjid());
    	userInfo.put("yhmc", user.getYhmc());
    	userInfo.put("yhdlzh", user.getYhdlzh());
    	userInfo.put("sfzh", user.getSfzh());
    	userInfo.put("jybh", user.getJybh());
    	userInfo.put("ssdw_zjid", user.getSsdw_zjid());
    	userInfo.put("dwmc", org.getDwmc());
    	userInfo.put("dwmc", org.getDwmc());
    	userInfo.put("dlip", SecurityContext.getCurrentRemoteIp());
    	userInfo.put("dlsj", new Date());
    	userInfo.put("sessionId", sessionId);
    	userList.add(0, userInfo);
    	paraService.addHisOnline();
	}
	
	/**
	 * 用户登出时调用
	 * @param sessionId
	 */
	public void logout(String sessionId){
		synchronized(userList){
			for(Map<String,Object> userInfo:userList){
				String ssid = (String)userInfo.get("sessionId");
				if(sessionId.equals(ssid)){
					userList.remove(userInfo);
					break;
				}
			}
		}
		
	}
	
	/**
	 * 获取登录用户数
	 * @return
	 */
	public int getOnlineCount(){
		return userList.size();
	}
	
	/**
	 * 获取登录用户列表
	 * @return
	 */
	public List<Map<String,Object>> getUserList(){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>(userList);
		return result;
	}
	
	public int getHisOnline(){
		Parameter para = paraService.getParaByCsdm(HIS_ONLINE_NAME);
		return Integer.parseInt(para.getCsz());
	}
}
