/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.adapter.impl;

import com.jae.eclipse.navigator.jaeapp.adapter.AbstractWorkbenchAdapter;
import com.jae.eclipse.navigator.jaeapp.util.JAEAppUtil;

/**
 * @author hongshuiqiao
 *
 */
public class NullWorkbenchAdapter extends AbstractWorkbenchAdapter {
	public final static NullWorkbenchAdapter INSTANCE = new NullWorkbenchAdapter();

	@Override
	protected Object[] doGetChildren(Object element) {
		return JAEAppUtil.EMPTY;
	}

	@Override
	protected Object doGetParent(Object element) {
		return null;
	}

}
