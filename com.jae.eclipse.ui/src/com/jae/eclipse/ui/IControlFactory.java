/**
 * 
 */
package com.jae.eclipse.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author hongshuiqiao
 *
 */
public interface IControlFactory {
	
	/**
	 * �����ؼ�
	 * @param parent
	 * @return
	 */
	public Control createControl(Composite parent);
}
