/**
 * @(#)com.casic27.platform.common.authorization.menutree.MenuTree.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
 */
public class MenuTree {
	
	/**
	 * 节点Id和节点对象的键值对
	 */
	private Map<String,MenuNode> nodeMap = new HashMap<String,MenuNode>();
	
	/**
	 * 包含所有节点的list
	 */
	private List<MenuNode> nodeList = new ArrayList<MenuNode>();
	
	/**
	 * 添加节点
	 * @param node
	 */
	public void addNode(MenuNode node){
		nodeMap.put(node.getMenuId(), node);
		MenuNode parent = nodeMap.get(node.getParentMenuId());
		if(parent==null){
			nodeList.add(node);
		}else{
			List<MenuNode> childrens = parent.getChildrens();
			if(childrens==null){
				childrens = new ArrayList<MenuNode>();
				childrens.add(node);
				parent.setChildrens(childrens);
			}else{
				childrens.add(node);
			}
		}
	}
	
	/**
	 * 获取节点
	 * @param menuId
	 * @return
	 */
	public MenuNode getNode(String menuId){
		return nodeMap.get(menuId);
	}
	
	/**
	 * 获取这个菜单列表
	 * @return
	 */
	public List<MenuNode> getNodeList(){
		return this.nodeList;
	}
	
	/**
	 * 获取父亲节点
	 * @param menuId
	 * @return
	 */
	public MenuNode getParent(String menuId){
		MenuNode menuNode = nodeMap.get(menuId);
		if(menuNode==null){
			return null;
		}else{
			return nodeMap.get(menuNode.getParentMenuId());
		}
	}
	
}
