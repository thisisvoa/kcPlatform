package com.kcp.platform.util;

/**
 *
 *类描述：
 * 排序接口，排序接口，只要实现该接口，就可以调用平台提供的工具类中的OrderableListSortUtils.sort()方法，对其进行排序
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
