/**
 * 
 */
package com.jae.eclipse.ui.event;


/**
 * @author hongshuiqiao
 *
 */
public class LinkEvent {
	private Object source;
	private Object data;

	public LinkEvent(Object source) {
		super();
		this.source = source;
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
