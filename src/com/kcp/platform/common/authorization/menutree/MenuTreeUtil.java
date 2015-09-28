package com.kcp.platform.common.authorization.menutree;

import java.util.List;

import com.kcp.platform.common.menu.entity.SysMenu;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 */
public class MenuTreeUtil {
	/**
	 * 创建菜单HTML
	 * @param menuList
	 * @param ctx
	 * @return
	 */
	public static String bulidMenuHTML(List<SysMenu> menuList,String ctx){
		MenuTree menuTree = new MenuTree();
		for(SysMenu menu:menuList){
			MenuNode menuNode = new MenuNode();
			menuNode.setMenuId(menu.getId());
			menuNode.setMenuName(menu.getMenuName());
			menuNode.setMenuUrl(menu.getMenuAddr());
			menuNode.setParentMenuId(menu.getParentMenuId());
			menuTree.addNode(menuNode);
		}
		MenuTreeHTMLBuilder bulider = new DefaultMenuTreeHTMLBuilder(ctx);
		return bulider.build(menuTree);
	}
	
}
