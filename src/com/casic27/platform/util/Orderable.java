/**
 * @(#)com.casic27.platform.util.Orderable.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：Apr 13, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.util;

/**
 *
 *类描述：
 * 排序接口，排序接口，只要实现该接口，就可以调用平台提供的工具类中的OrderableListSortUtils.sort()方法，对其进行排序
 *@Author： 林斌树(linbinshu@casic27.com)
 *@Version：1.0
 */
public interface Orderable {
	/**
     * Useful constant for the highest precedence value.
     * @see Integer#MIN_VALUE
     */
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    /**
     * Useful constant for the lowest precedence value.
     * @see Integer#MAX_VALUE
     */
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;
    
    /**
     * 排序号，越小越在前面
     * @return
     */
    public int getOrderNo();
}
