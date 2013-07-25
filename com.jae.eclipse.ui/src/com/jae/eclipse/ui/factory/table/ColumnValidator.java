/**
 * 
 */
package com.jae.eclipse.ui.factory.table;

import org.eclipse.jface.viewers.ColumnViewer;

import com.jae.eclipse.ui.IColumnValidator;
import com.jae.eclipse.ui.IMessageCaller;

/**
 * @author hongshuiqiao
 *
 */
public class ColumnValidator implements IColumnValidator {

	public boolean validate(IMessageCaller messageCaller, ColumnViewer viewer, Object column, int columnIndex, int rowIndex, Object value) {
		// TODO Auto-generated method stub
		return false;
	}

}
