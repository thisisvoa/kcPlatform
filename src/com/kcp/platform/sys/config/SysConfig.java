package com.kcp.platform.sys.config;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kcp.platform.common.role.entity.Role;

public class SysConfig {
	/**
	 * 升级管理员
	 */
	public static final String CSDM_ORG_MANAGER_SHENGJI = "SHJZZJGGLYJSBH";
	public static final String CSDM_ORG_MANAGER_SHIJI = "SJZZJGGLYJSBH";
	public static final String CSDM_ORG_MANAGER_XIANJI = "XJZZJGGLYJSBH";
	
	/**
	 * 管理所有组织机构角色IDS
	 */
	public static String ORG_MANAGER_SHENGJI = "";
	public static String ORG_MANAGER_SHIJI = "";
	public static String ORG_MANAGER_XIANJI = "";
	
	/**
	 * 获取角色列表中的角色属于哪种组织机构管理员
	 * @param roleList
	 * @return
	 */
	public static int getOrgManagerType(List<Role> roleList){
		int type = -1;
		if(roleList==null){
			return type;
		}
		if(StringUtils.isNotEmpty(ORG_MANAGER_SHENGJI)){
			String[] roleCodes = ORG_MANAGER_SHENGJI.split(",");
			for(String roleCode:roleCodes){
				for(Role role:roleList){
					if(roleCode.equalsIgnoreCase(role.getJsdm())){
						type = 1;
					}
				}
			}
		}
		if(StringUtils.isNotEmpty(ORG_MANAGER_SHIJI)){
			String[] roleCodes = ORG_MANAGER_SHIJI.split(",");
			for(String roleCode:roleCodes){
				for(Role role:roleList){
					if(roleCode.equalsIgnoreCase(role.getJsdm())){
						type = 2;
					}
				}
			}
		}
		if(StringUtils.isNotEmpty(ORG_MANAGER_XIANJI)){
			String[] roleCodes = ORG_MANAGER_XIANJI.split(",");
			for(String roleCode:roleCodes){
				for(Role role:roleList){
					if(roleCode.equalsIgnoreCase(role.getJsdm())){
						type = 3;
					}
				}
			}
		}
		return type;
	}
}
