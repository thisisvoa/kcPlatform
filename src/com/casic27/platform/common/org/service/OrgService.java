/**
 * @(#)com.casic27.platform.common.user.service.OrgService.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-4-21
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.common.org.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casic27.platform.common.log.annotation.Log;
import com.casic27.platform.common.log.annotation.OperateLogType;
import com.casic27.platform.common.org.dao.IOrgMapper;
import com.casic27.platform.common.org.entity.Org;
import com.casic27.platform.common.role.entity.Role;
import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.common.user.service.UserService;
import com.casic27.platform.core.exception.BusinessException;
import com.casic27.platform.core.service.BaseService;
import com.casic27.platform.sys.config.SysConfig;
import com.casic27.platform.sys.constants.CommonConst;
import com.casic27.platform.sys.security.context.SecurityContext;
import com.casic27.platform.util.DateUtils;

/**
 *
 *类描述：
 * 
 *@Author： 宗斌(zongbin@casic27.com)
 *@Version：1.0
 */
@Service("orgService")
public class OrgService extends BaseService {
	
	@Autowired
	private IOrgMapper iOrgMapper;
	
	@Autowired
	private UserService userService;
	/**
	 * 根据单位ID查询该单位的子单位列表信息 
	 * @param pOrgId
	 * @return
	 */
	public List<Org> queryChildOrgListByPOrgId(String pOrgId){
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("sjdw_zjid", pOrgId);
		queryMap.put("jlyxzt", CommonConst.YES);
		List<Org> orgList = iOrgMapper.findOrg(queryMap);
		return orgList;
		
	}

	@Log(type=OperateLogType.QUERY, moduleName="单位信息管理", operateDesc="[查询] 查询单位信息", useSpel=false)
	public List<Map<String, Object>>  findOrgPaging(Map<String,Object> queryMap){
		return iOrgMapper.findOrgPaging(queryMap);
	}
	
	
	public List<Map<String,Object>> getOrgByIds(String[] ids){
		return iOrgMapper.getOrgByIds(ids);
	}
	
	/**
	 * 逻辑删除
	 * @param orgId
	 */
	@Log(type=OperateLogType.DELETE, moduleName="单位信息管理", operateDesc="'[删除] 删除[单位名称：'+#org.dwmc+'] [单位编号：'+#org.dwbh+']的单位信息'")
	public void logicDelOrg(Org org){
		if(!hasPermission(org.getZjid())){
			throw new BusinessException("权限不足：删除单位不是您的所属单位或下属单位！");
		}
		int userCount = userService.getOrgUserCount(org.getZjid());
		if(userCount>0){
			throw new BusinessException("单位["+org.getDwmc()+"]下存在用户，不能删除！");
		}
		org.setGxyh(SecurityContext.getCurrentUser().getZjid());
		org.setJlscsj(DateUtils.getCurrentDateTime14());
		org.setJlyxzt(CommonConst.DISABLE_FLAG);
		iOrgMapper.updateOrgJlyxzt(org);
	}
	
	/**
	 * 新增单位信息
	 * @param paramMap
	 */
	@Log(type=OperateLogType.INSERT, moduleName="单位信息管理", operateDesc="'[新增] 新增[单位名称：'+#org.dwmc+'] [单位编号：'+#org.dwbh+']的单位信息'")
	public void addOrg(Org org){
		if(!hasPermission(org.getSjdw_zjid())){
			throw new BusinessException("权限不足：新增单位的上级单位不是您的所属单位或下属单位！");
		}
		org.setCjyh(SecurityContext.getCurrentUser().getZjid());
		org.setGxyh(SecurityContext.getCurrentUser().getZjid());
		String date = DateUtils.getCurrentDateTime14();
		org.setJlxzsj(date);
		org.setJlxgsj(date);
		iOrgMapper.addOrg(org);
	}
	
	
	@Log(type=OperateLogType.UPDATE, moduleName="单位信息管理", operateDesc="'[修改] 修改[单位名称：'+#org.dwmc+'] [单位编号：'+#org.dwbh+']的单位信息'")
	public void updateOrg(Org org){
		Org old = getOrgById(org.getZjid());
		if(!hasPermission(org.getSjdw_zjid()) || !hasPermission(old.getZjid())){
			throw new BusinessException("权限不足：修改单位不是您的所属单位或下属单位！");
		}
		String date = DateUtils.getCurrentDateTime14();
		org.setJlxgsj(date);
		org.setGxyh(SecurityContext.getCurrentUser().getZjid());
		iOrgMapper.updateOrg(org);
	}
	
	/**
	 * 查询某个单位的所有上级单位
	 * @param orgId
	 * @return
	 */
	public List<Map> queryParentOrgList(String orgId){
		List<Map> parentOrgList = iOrgMapper.queryParentOrgList(orgId);
		Collections.reverse(parentOrgList);//对查询结果进行反向排序，使得结果为从上级到下级
		return parentOrgList;
	}
	
	/**
	 * 查询某个单位的所有上级单位
	 * @param orgId
	 * @return
	 */
	public List<Org> getParentOrgList(String orgId){
		List<Org> parentOrgList = iOrgMapper.getParentOrgList(orgId);
		return parentOrgList;
	}
	
	/**
	 * 根据ID查询单位信息
	 * @param orgId
	 * @return
	 */
	public Org getOrgById(String orgId){
		Org  orgInfo= iOrgMapper.getOrgById(orgId);
		return orgInfo;
	}
	
	public boolean validateUniqueOrg(Org org){
		int count = iOrgMapper.statiscOrg(org);
		if(count > 0){
			return false;
		}
		return true;
	}
	
	/**
	 * 判断当前用户是否有某个组织机构下的数据的操作权限
	 * @param orgId
	 * @return
	 */
	public boolean hasPermission(String orgId){
		User user = SecurityContext.getCurrentUser();
		if(user.isSuperAdmin()){
			return true;
		}else{
			String dwId = getPermissionRootOrgId();
			if(StringUtils.isNotEmpty(dwId)){
				if(StringUtils.isNotEmpty(orgId)){
					int count = iOrgMapper.isChildOrg(dwId, orgId);
					if(count>0){
						return true;
					}else{
						return false;
					}
				}
			}
			return false;
		}
	}
	
	/**
	 * 查找有权限的单位的根节点
	 * @return
	 */
	public String getPermissionRootOrgId(){
		User user = SecurityContext.getCurrentUser();
		Org org = SecurityContext.getCurrentUserOrg();
		List<Role> roleList = SecurityContext.getCurrentRoleList();
		int type = SysConfig.getOrgManagerType(roleList);
		String dwId = null;
		if(-1==type){
			dwId = user.getSsdw_zjid();
		}else{
			String dwbh = org.getDwbh();
			if(1==type){
				dwbh = dwbh.substring(0,2)+"0000000000";
			}else if(2==type){
				dwbh = dwbh.substring(0,4)+"00000000";
			}else if(3==type){
				dwbh = dwbh.substring(0,6)+"000000";
			}
			Org permOrg = iOrgMapper.getOrgByNo(dwbh);
			if(permOrg!=null){
				dwId = permOrg.getZjid();
			}else{
				dwId = "";
			}
		}
		return dwId;
	}
}
