package com.kcp.platform.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 *类描述：
 * 实现Orderable接口的比较器
 *@Author： 林斌树(linbinshu@kcp.com)
 *@Version：1.0
 */
public class OrderableComparator implements Comparator {
	/**
	 * Shared default instance of OrderComparator.
	 */
	private static OrderableComparator INSTANCE = new OrderableComparator();

	private OrderableComparator() {
	}

	public int compare(Object o1, Object o2) {
		int i1 = getOrder(o1);
		int i2 = getOrder(o2);
		return (i1 < i2) ? -1 : (i1 > i2) ? 1 : 0;
	}

	/**
	 * Determine the order value for the given object.
	 * <p>The default implementation checks against the {@link Orderable}
	 * interface. Can be overridden in subclasses.
	 * @param obj the object to check
	 * @return the order value, or <code>Ordered.LOWEST_PRECEDENCE</code> as fallback
	 */
	protected int getOrder(Object obj) {
		return (obj instanceof Orderable ? ((Orderable) obj).getOrderNo()
				: Orderable.LOWEST_PRECEDENCE);
	}

	/**
	 * Sort the given List with a default OrderComparator.
	 * <p>Optimized to skip sorting for lists with size 0 or 1,
	 * in order to avoid unnecessary array extraction.
	 * @param list the List to sort
	 * @see java.util.Collections#sort(java.util.List, java.util.Comparator)
	 */
	@SuppressWarnings("unchecked")
	public static void sort(List list) {
		if (list == null || list.size() < 2)
			return;

		Collections.sort(list, INSTANCE);
	}

	/**
	 * Sort the given array with a default OrderComparator.
	 * <p>Optimized to skip sorting for lists with size 0 or 1,
	 * in order to avoid unnecessary array extraction.
	 * @param array the array to sort
	 * @see java.util.Arrays#sort(Object[], java.util.Comparator)
	 */
	@SuppressWarnings("unchecked")
	public static void sort(Object[] array) {
		if (array == null || array.length < 2)
			return;

		Arrays.sort(array, INSTANCE);
	}
}
