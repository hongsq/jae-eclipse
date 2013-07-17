/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.provider;

import com.jae.eclipse.navigator.jaeapp.model.AbstractJDElement;
import com.jae.eclipse.navigator.jaeapp.model.IJDElement;
import com.jae.eclipse.navigator.jaeapp.model.LoadState;

/**
 * @author hongshuiqiao
 *
 */
class LoadingJDElement extends AbstractJDElement {
	
	public LoadingJDElement(IJDElement parent, String name) {
		super(parent, name);
	}
	
	@Override
	public String getDisplayName() {
		return this.getName();
	}
	
	@Override
	protected void doLoad() {
		// nothing to do.
	}
	
	@Override
	public LoadState getLoadState() {
		return LoadState.LOADED;
	}
}
