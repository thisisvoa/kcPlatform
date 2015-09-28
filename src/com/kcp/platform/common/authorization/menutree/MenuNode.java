package com.kcp.platform.common.authorization.menutree;

import java.util.List;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 */
public class MenuNode {
	/**
	 * 菜单Id
	 */
	private String menuId;
	
	/**
	 * 菜单名称
	 */
	private String menuName;
	
	/**
	 * 菜单地址
	 */
	private String menuUrl;
	
	/**
	 * 上级菜单Id
	 */
	private String parentMenuId;
	
	/**
	 * 下级菜单 
	 */
	private List<MenuNode> childrens;
	
	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public List<MenuNode> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<MenuNode> childrens) {
		this.childrens = childrens;
	}
	
	public String getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
	}
	
	/**
	 * 是否有子节点
	 * @return
	 */
	public boolean isHasChildren(){
		return (childrens!=null&&childrens.size()>0);
	}
	
//	public boolean isTopNode(){
//		return 
//	}
}
