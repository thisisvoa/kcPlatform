/**
 * @(#)com.casic27.platform.common.authorization.menutree.MenuTreeHTMLBuilder.java
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

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@casic27.com)
 */
public interface MenuTreeHTMLBuilder {
	/**
	 * 将菜单树形数据转化为HTML
	 * @param tree
	 * @return
	 */
	String build(MenuTree tree);
}
