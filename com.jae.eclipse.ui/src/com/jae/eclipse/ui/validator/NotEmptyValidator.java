/**
 * 
 */
package com.jae.eclipse.ui.validator;

import com.jae.eclipse.core.util.StringUtil;
import com.jae.eclipse.ui.IMessageCaller;
import com.jae.eclipse.ui.IPropertyEditor;
import com.jae.eclipse.ui.IValidator;

/**
 * @author hongshuiqiao
 *
 */
public class NotEmptyValidator implements IValidator {
	private String name;
	
	public NotEmptyValidator(String name) {
		super();
		this.name = name;
	}

	@Override
	public boolean validate(IMessageCaller messageCaller, Object validateObject) {
		if (validateObject instanceof IPropertyEditor) {
			IPropertyEditor editor = (IPropertyEditor) validateObject;
			Object value = editor.getValue();
			
			if(null == value){
				messageCaller.error(this.name+"不能为空.");
				return false;
			}
			
			if (value instanceof String) {
				if(StringUtil.isEmpty((String) value)){
					messageCaller.error(this.name+"不能为空.");
					return false;
				}
			}
		}
		
		return true;
	}

}
