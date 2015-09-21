/**
 * @(#)com.casic27.platform.common.authorization.menutree.MenuTreeUtil.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-5-10
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.common.authorization.menutree;

import java.util.List;

import com.casic27.platform.common.menu.entity.Menu;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
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
