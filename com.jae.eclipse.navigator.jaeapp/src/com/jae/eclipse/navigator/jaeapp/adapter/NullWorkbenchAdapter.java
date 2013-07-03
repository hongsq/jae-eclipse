/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.adapter;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.WorkbenchAdapter;

import com.jae.eclipse.navigator.jaeapp.model.INameElement;

/**
 * @author hongshuiqiao
 *
 */
public class NullWorkbenchAdapter extends WorkbenchAdapter {
	public final static NullWorkbenchAdapter INSTANCE = new NullWorkbenchAdapter();

	@Override
	public ImageDescriptor getImageDescriptor(Object object) {
		return null;
	}

	@Override
	public String getLabel(Object object) {
		if (object instanceof INameElement) {
			return ((INameElement) object).getName();
		}
		return null==object?"":object.toString();
	}
	
}
