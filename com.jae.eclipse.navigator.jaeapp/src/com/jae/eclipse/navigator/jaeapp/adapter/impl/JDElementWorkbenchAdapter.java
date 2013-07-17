/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.adapter.impl;

import com.jae.eclipse.navigator.jaeapp.adapter.AbstractWorkbenchAdapter;
import com.jae.eclipse.navigator.jaeapp.model.IJDElement;

/**
 * @author hongshuiqiao
 *
 */
public class JDElementWorkbenchAdapter extends AbstractWorkbenchAdapter {
	public static final JDElementWorkbenchAdapter INSTANCE = new JDElementWorkbenchAdapter();

	@Override
	protected Object[] doGetChildren(Object element) {
		IJDElement jdElement = (IJDElement) element;
		IJDElement[] children = jdElement.getChildren();
		return children;
	}

	@Override
	protected Object doGetParent(Object element) {
		IJDElement jdElement = (IJDElement) element;
		return jdElement.getParent();
	}

}