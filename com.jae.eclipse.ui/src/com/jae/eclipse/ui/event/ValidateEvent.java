package com.jae.eclipse.ui.event;

import com.jae.eclipse.ui.IAdaptable;
/**
 * @author hongshuiqiao
 *
 */
public class ValidateEvent {
	private Object source;
	private IAdaptable adaptable;
	public ValidateEvent(Object source) {
		super();
		this.source = source;
	}
	public IAdaptable getAdaptable() {
		return adaptable;
	}
	public void setAdaptable(IAdaptable adaptable) {
		this.adaptable = adaptable;
	}
	
	public Object getSource() {
		return source;
	}
}
