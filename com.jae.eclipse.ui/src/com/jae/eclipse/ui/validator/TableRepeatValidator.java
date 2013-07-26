/**
 * 
 */
package com.jae.eclipse.ui.validator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import com.jae.eclipse.ui.IColumnValidator;
import com.jae.eclipse.ui.IMessageCaller;
import com.jae.eclipse.ui.factory.table.RowModel;

/**
 * @author hongshuiqiao
 *
 */
public class TableRepeatValidator implements IColumnValidator {
	private String name;
	
	public TableRepeatValidator(String name) {
		super();
		this.name = name;
	}

	public boolean validate(IMessageCaller messageCaller, ColumnViewer viewer, Object rowData, int validateColumnIndex, int validateRowIndex, Object value) {
		TableItem[] items = ((TableViewer)viewer).getTable().getItems();
		List<Object> list = new ArrayList<Object>();
		String propertyName = (String) viewer.getColumnProperties()[validateColumnIndex];
		for (int rowIndex = 0; rowIndex < items.length; rowIndex++) {
			TableItem tableItem = items[rowIndex];
			RowModel rowModel = (RowModel) tableItem.getData();
			
			//不包含自己
			if(rowModel.equals(rowData))
				continue;
			
			Object rowValue = rowModel.get(propertyName);
			if(null != rowValue)
				list.add(rowValue);
		}
		
		if(list.contains(value)){
			messageCaller.error(this.name+"不能重复。");
			return false;
		}
		
		return true;
	}

}
