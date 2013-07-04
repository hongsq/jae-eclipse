/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.adapter.impl;

import com.jae.eclipse.navigator.jaeapp.adapter.AbstractWorkbenchAdapter;
import com.jae.eclipse.navigator.jaeapp.model.JDApp;
import com.jae.eclipse.navigator.jaeapp.util.JAEAppUtil;

/**
 * @author hongshuiqiao
 *
 */
public class JDAppWorkbenchAdapter extends AbstractWorkbenchAdapter {
	public static final JDAppWorkbenchAdapter INSTANCE = new JDAppWorkbenchAdapter();

	@Override
	protected Object[] doGetChildren(Object element) {
		return JAEAppUtil.EMPTY;
	}

	@Override
	protected Object doGetParent(Object element) {
		JDApp app = (JDApp) element;
		return app.getUser();
	}

}
