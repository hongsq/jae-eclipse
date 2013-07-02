/**
 * 
 */
package com.jae.eclipse.ui.event;

import com.jae.eclipse.ui.IAdaptable;

/**
 * @author hongshuiqiao
 *
 */
public class LinkEvent {
	private Object source;
	private IAdaptable adaptable;

	public LinkEvent(Object source) {
		super();
		this.source = source;
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
