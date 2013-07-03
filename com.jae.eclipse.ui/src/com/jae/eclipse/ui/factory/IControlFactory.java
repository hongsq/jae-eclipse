/**
 * 
 */
package com.jae.eclipse.ui.factory;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jae.eclipse.ui.ILoadable;
import com.jae.eclipse.ui.IMessageCaller;
import com.jae.eclipse.ui.IStore;
import com.jae.eclipse.ui.IUIDescElement;
import com.jae.eclipse.ui.IValidatable;
import com.jae.eclipse.ui.event.IValueEventContainer;
import com.jae.eclipse.ui.event.IValuechangeListener;

/**
 * @author hongshuiqiao
 *
 */
public interface IControlFactory extends IUIDescElement, IValueEventContainer, IStore, ILoadable, IValidatable, IValuechangeListener {
	
	public Object getValue();
	
	public void setValue(Object value);
	
	/**
	 * ´´½¨¿Ø¼þ
	 * @param parent
	 * @return
	 */
	public Control createControl(Composite parent);
	
	public IMessageCaller getMessageCaller();
	
	public void setMessageCaller(IMessageCaller messageCaller);
}
