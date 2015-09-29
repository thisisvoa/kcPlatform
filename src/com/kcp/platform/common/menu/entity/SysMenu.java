package com.kcp.platform.common.menu.entity;

import java.util.Date;

public class SysMenu{

	/**
	 * 主键ID
	 */
	private String id;

	/**
	 * 菜单名称
	 */
	private String menuName;

	/**
	 * 菜单级别
	 */
	private String menuLevel;

	/**
	 * 菜单序号
	 */
	private String menuIndex;

	/**
	 * 上级菜单
	 */
	private String parentMenuId;

	/**
	 * 菜单地址
	 */
	private String menuAddr;

	/**
	 * 是否最后一级菜单
	 */
	private String isLast;

	/**
	 * 使用标识 0-禁用；1-启用
	 */
	private String isUsed;

	/**
	 * 
	 */
	private String menuType;

	/**
	 * 备注
	 */
	private String memo;

	/**
	 * 记录新增时间
	 */
	private Date createTime;

	/**
	 * 记录更新时间
	 */
	private Date updateTime;

	/**
	 * 记录删除时间
	 */
	private Date deleteTime;

	/**
	 * 创建用户
	 */
	private String createdUserId;

	/**
	 * 更新用户
	 */
	private String updateUserId;

	/**
	 * 删除用户
	 */
	private String deleteUserId;

	/**
	 * 状态
	 */
	private String status;


	public String getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
	}

	public String getMenuName(){
		return this.menuName;
	}
	
	public void setMenuName(String menuName){
		this.menuName = menuName;
	}

	public String getMenuLevel(){
		return this.menuLevel;
	}
	
	public void setMenuLevel(String menuLevel){
		this.menuLevel = menuLevel;
	}

	public String getMenuIndex(){
		return this.menuIndex;
	}
	
	public void setMenuIndex(String menuIndex){
		this.menuIndex = menuIndex;
	}

	public String getParentMenuId(){
		return this.parentMenuId;
	}
	
	public void setParentMenuId(String parentMenuId){
		this.parentMenuId = parentMenuId;
	}

	public String getMenuAddr(){
		return this.menuAddr;
	}
	
	public void setMenuAddr(String menuAddr){
		this.menuAddr = menuAddr;
	}

	public String getIsLast(){
		return this.isLast;
	}
	
	public void setIsLast(String isLast){
		this.isLast = isLast;
	}

	
	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}

	public String getMenuType(){
		return this.menuType;
	}
	
	public void setMenuType(String menuType){
		this.menuType = menuType;
	}

	public String getMemo(){
		return this.memo;
	}
	
	public void setMemo(String memo){
		this.memo = memo;
	}

	public Date getCreateTime(){
		return this.createTime;
	}
	
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getUpdateTime(){
		return this.updateTime;
	}
	
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	public Date getDeleteTime(){
		return this.deleteTime;
	}
	
	public void setDeleteTime(Date deleteTime){
		this.deleteTime = deleteTime;
	}

	public String getCreatedUserId(){
		return this.createdUserId;
	}
	
	public void setCreatedUserId(String createdUserId){
		this.createdUserId = createdUserId;
	}

	public String getUpdateUserId(){
		return this.updateUserId;
	}
	
	public void setUpdateUserId(String updateUserId){
		this.updateUserId = updateUserId;
	}

	public String getDeleteUserId(){
		return this.deleteUserId;
	}
	
	public void setDeleteUserId(String deleteUserId){
		this.deleteUserId = deleteUserId;
	}

	public String getStatus(){
		return this.status;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
}
