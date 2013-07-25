/**
 * 
 */
package com.jae.eclipse.ui.factory.table;

import java.util.Comparator;

import com.jae.eclipse.core.ObjectOperator;

/**
 * @author hongshuiqiao
 *
 */
public class DefaultColumnComparator implements Comparator<Object> {
	private ColumnModel columnModel;

	public DefaultColumnComparator(ColumnModel columnModel) {
		this.columnModel = columnModel;
	}

	public int compare(Object o1, Object o2) {
		ObjectOperator operator = columnModel.getObjectOperator(true);
		Object value1 = operator.getValue(o1, columnModel.getPropertyName());
		Object value2 = operator.getValue(o2, columnModel.getPropertyName());
		if(null == value1 || null == value2)
			return 0;
		return value1.toString().compareToIgnoreCase(value2.toString());
	}

}
