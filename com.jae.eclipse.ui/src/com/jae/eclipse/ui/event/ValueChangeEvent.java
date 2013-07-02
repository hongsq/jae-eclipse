/**
 * 
 */
package com.jae.eclipse.ui.event;

import com.jae.eclipse.ui.IAdaptable;

/**
 * @author hongshuiqiao
 *
 */
public class ValueChangeEvent {
	private Object oldValue;
	private Object newValue;
	private Object source;
	private IAdaptable adaptable;

	public ValueChangeEvent(Object source, Object oldValue, Object newValue) {
		super();
		this.source = source;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public IAdaptable getAdaptable() {
		return adaptable;
	}

	public void setAdaptable(IAdaptable adaptable) {
		this.adaptable = adaptable;
	}
}
