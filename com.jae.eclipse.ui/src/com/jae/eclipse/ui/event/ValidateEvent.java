package com.jae.eclipse.ui.event;

/**
 * @author hongshuiqiao
 *
 */
public class ValidateEvent {
	private Object source;
	private Object data;
	
	public ValidateEvent(Object source) {
		super();
		this.source = source;
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getSource() {
		return source;
	}
}
