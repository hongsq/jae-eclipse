/**
 * 
 */
package com.jae.eclipse.ui.event;


/**
 * @author hongshuiqiao
 *
 */
public class ValueChangeEvent {
	private Object oldValue;
	private Object newValue;
	private Object source;
	private Object data;

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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
