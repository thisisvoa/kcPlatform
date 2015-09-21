package com.casic27.platform.common.menu.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.casic27.platform.common.log.annotation.OperateLog;
import com.casic27.platform.common.menu.entity.Menu;

@Repository
public interface IMenuMapper {

	public List<Menu> queryMenuList(Menu menu);
	
	@OperateLog(isRecord=false)
	public int insert(Menu paramMap);
	
	@OperateLog(isRecord=false)
	public int update(Menu paramMap);
	
	@OperateLog(isRecord=false)
	public int delete(String id);
	
	/**
	 * 查询某个父节点下最大的菜单序号
	 * @param sjcd
	 * @return
	 */
	public String queryMaxCdxh(@Param("sjcd")String sjcd);
	
	/**
	 * 查询菜单信息
	 * @param menu
	 * @return
	 */
	public List<Menu> findMenuList(Menu menu);
	
	/**
	 * 查询菜单的上级菜单
	 */
	public List<Menu> queryParentMenuList(@Param("menuId")String menuId);
	
	/**
	 * 查询所有菜单，以树形数据结构返回
	 * @return
	 */
	public List<Menu> findAllEnableMenus();
	
	/**
	 * 修改树形所有菜单的使用标识
	 * @param paramMap
	 */
	public void updateChildrenSybz(Menu paramMap);
	
	
	public void updateParentSybz(Menu paramMap);
	/**
	 * 获取菜单权限信息
	 * @return
	 */
	@OperateLog(isRecord=false)
	public List<Map<String,Object>> getAuthInfo(String[] menuIds);
	
	public Menu getMenuById(@Param("id")String id);
	
	public void logicDelMenu(Menu menu);
}
