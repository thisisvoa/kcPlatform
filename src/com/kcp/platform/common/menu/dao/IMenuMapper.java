package com.kcp.platform.common.menu.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kcp.platform.common.log.annotation.OperateLog;
import com.kcp.platform.common.menu.entity.SysMenu;

@Repository
public interface IMenuMapper {

	public List<SysMenu> queryMenuList(SysMenu menu);
	
	@OperateLog(isRecord=false)
	public int insert(SysMenu paramMap);
	
	@OperateLog(isRecord=false)
	public int update(SysMenu paramMap);
	
	@OperateLog(isRecord=false)
	public int delete(String id);
	
	/**
	 * 查询某个父节点下最大的菜单序号
	 * @param sjcd
	 * @return
	 */
	public String queryMaxCdxh(@Param("PARENT_MENU_ID")String PARENT_MENU_ID);
	
	/**
	 * 查询菜单信息
	 * @param menu
	 * @return
	 */
	public List<SysMenu> findMenuList(SysMenu menu);
	
	/**
	 * 查询菜单的上级菜单
	 */
	public List<SysMenu> queryParentMenuList(@Param("menuId")String menuId);
	
	/**
	 * 查询所有菜单，以树形数据结构返回
	 * @return
	 */
	public List<SysMenu> findAllEnableMenus();
	
	/**
	 * 修改树形所有菜单的使用标识
	 * @param paramMap
	 */
	public void updateChildrenSybz(SysMenu paramMap);
	
	
	public void updateParentSybz(SysMenu paramMap);
	/**
	 * 获取菜单权限信息
	 * @return
	 */
	@OperateLog(isRecord=false)
	public List<Map<String,Object>> getAuthInfo(String[] menuIds);
	
	public SysMenu getMenuById(@Param("id")String id);
	
	public void logicDelMenu(SysMenu menu);
}
