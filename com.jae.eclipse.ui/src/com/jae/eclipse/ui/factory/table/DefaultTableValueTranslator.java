/**
 * 
 */
package com.jae.eclipse.ui.factory.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jae.eclipse.core.util.ObjectUtil;

/**
 * @author hongshuiqiao
 *
 */
public class DefaultTableValueTranslator implements TableValueTranslator {
	private AbstractTableFactory factory;

	public DefaultTableValueTranslator(AbstractTableFactory factory) {
		super();
		this.factory = factory;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object from(RowModel[] source) {
		List list = new ArrayList();
		for (RowModel row : source) {
			list.add(fromRowData(row));
		}
		
		Object value = this.factory.getValue();
		if (value instanceof Collection) {
			((Collection) value).clear();
			((Collection) value).addAll(list);
		}else if(value.getClass().isArray()){
			value = list.toArray();
		}
		
		return value;
	}

	public RowModel[] to(Object target) {
		List<RowModel> rows = new ArrayList<RowModel>();
		
		Object[] datas = null;
		if(null != target){
			if (target instanceof Collection<?>) {
				datas = ((Collection<?>) target).toArray();
			}else if(target.getClass().isArray()){
				datas = (Object[]) target;
			}
		}
		
		if(null != datas){
			for (Object data : datas) {
				RowModel row = new RowModel(this.factory, data);
				toRowData(row);
				rows.add(row);
			}
		}
		
		return rows.toArray(new RowModel[rows.size()]);
	}

	protected void toRowData(RowModel row){
		ColumnModel[] columns = this.factory.getColumns();
		for (ColumnModel column : columns) {
			String propertyName = column.getPropertyName();
			Object cellValue = getOldCellValue(row, propertyName);
			if(null != cellValue)
				row.setCellValue(propertyName, cellValue.toString());
		}
	}
	
	protected Object fromRowData(RowModel row){
		ColumnModel[] columns = this.factory.getColumns();
		for (ColumnModel column : columns) {
			String propertyName = column.getPropertyName();
			Object oldValue = getOldCellValue(row, propertyName);
			Object newValue = row.getCellValue(column.getPropertyName());
			
			if((null != newValue && !newValue.equals(oldValue))
					|| (null==newValue && null != oldValue)){
				ObjectUtil.setValue(row.getData(), propertyName, newValue);
			}
			
		}
		return row.getData();
	}

	protected Object getOldCellValue(RowModel row, String propertyName) {
		return ObjectUtil.getValue(row.getData(), propertyName);
	}
}
