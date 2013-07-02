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
	 * ´´½¨¿Ø¼þ
	 * @param parent
	 * @return
	 */
	public Control createControl(Composite parent);
}
