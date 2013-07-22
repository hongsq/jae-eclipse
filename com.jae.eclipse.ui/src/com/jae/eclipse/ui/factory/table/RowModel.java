/**
 * 
 */
package com.jae.eclipse.ui.factory.table;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author hongshuiqiao
 *
 */
public class RowModel {
	private AbstractTableFactory factory;
	private Object data;
	private Map<String, Object> cellDatas = new LinkedHashMap<String, Object>();

	public RowModel(AbstractTableFactory factory, Object data) {
		super();
		this.factory = factory;
	}
	
	public Object getData(){
		return this.data;
	}

	public void setCellValue(String propertyName, Object value) {
		this.cellDatas.put(propertyName, value);
	}

	public AbstractTableFactory getFactory() {
		return factory;
	}

	public Object getCellValue(String propertyName) {
		return this.cellDatas.get(propertyName);
	}

	public Object getCellValue(int columnIndex) {
		return this.getCellValue(this.getFactory().getColumn(columnIndex).getPropertyName());
	}
}
