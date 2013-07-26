/**
 * 
 */
package com.jae.eclipse.ui.validator;

import org.eclipse.jface.viewers.ColumnViewer;

import com.jae.eclipse.ui.IColumnValidator;
import com.jae.eclipse.ui.IMessageCaller;
import com.jae.eclipse.ui.IValidator;

/**
 * @author hongshuiqiao
 *
 */
public class ColumnValidator implements IColumnValidator {
	private IValidator validator;
	
	public ColumnValidator(IValidator validator) {
		super();
		this.validator = validator;
	}

	public boolean validate(IMessageCaller messageCaller, ColumnViewer viewer, Object rowData, int columnIndex, int rowIndex, Object value) {
		return this.validator.validate(messageCaller, rowData, value);
	}

}
