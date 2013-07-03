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
	 * 值变化时触发验证
	 * @param event
	 * @return
	 */
	public boolean validate(ValidateEvent event);
}
