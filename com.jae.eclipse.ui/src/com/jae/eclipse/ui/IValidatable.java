/**
 * 
 */
package com.jae.eclipse.ui;

import com.jae.eclipse.ui.event.ValidateEvent;


/**
 * @author hongshuiqiao
 *
 */
public interface IValidatable {
	/**
	 * ֵ�仯ʱ������֤
	 * @param event
	 * @return
	 */
	public boolean validate(ValidateEvent event);
}
