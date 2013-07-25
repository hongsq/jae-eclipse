/**
 * 
 */
package com.jae.eclipse.ui.factory.table;

import java.util.Comparator;

/**
 * 反向比较
 * @author hongshuiqiao
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ReverseComparator implements Comparator {
	private Comparator comparator;

	public ReverseComparator(Comparator comparator) {
		super();
		this.comparator = comparator;
	}

	public int compare(Object o1, Object o2) {
		return -comparator.compare(o1, o2);
	}
}
