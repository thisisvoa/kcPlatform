package com.kcp.platform.common.authorization.menutree;

import java.util.List;

import com.kcp.platform.common.menu.entity.Menu;

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
	public static String bulidMenuHTML(List<Menu> menuList,String ctx){
		MenuTree menuTree = new MenuTree();
		for(Menu menu:menuList){
			MenuNode menuNode = new MenuNode();
			menuNode.setMenuId(menu.getZjId());
			menuNode.setMenuName(menu.getCdmc());
			menuNode.setMenuUrl(menu.getCddz());
			menuNode.setParentMenuId(menu.getSjcd());
			menuTree.addNode(menuNode);
		}
		MenuTreeHTMLBuilder bulider = new DefaultMenuTreeHTMLBuilder(ctx);
		return bulider.build(menuTree);
	}
	
}
