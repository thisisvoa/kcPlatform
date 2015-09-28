package com.kcp.platform.common.authorization.menutree;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 */
public interface MenuTreeHTMLBuilder {
	/**
	 * 将菜单树形数据转化为HTML
	 * @param tree
	 * @return
	 */
	String build(MenuTree tree);
}
