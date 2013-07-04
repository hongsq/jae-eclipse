/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.adapter.impl;

import org.eclipse.core.resources.ResourcesPlugin;

import com.jae.eclipse.navigator.jaeapp.adapter.AbstractWorkbenchAdapter;
import com.jae.eclipse.navigator.jaeapp.model.User;

/**
 * @author hongshuiqiao
 *
 */
public class UserWorkbenchAdapter extends AbstractWorkbenchAdapter {
	public static final UserWorkbenchAdapter INSTANCE = new UserWorkbenchAdapter();

	@Override
	protected Object[] doGetChildren(Object element) {
		User user = (User) element;
		return user.getJDApps();
	}

	@Override
	protected Object doGetParent(Object element) {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

}
