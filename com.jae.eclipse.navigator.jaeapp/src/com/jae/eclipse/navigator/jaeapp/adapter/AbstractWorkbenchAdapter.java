/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.adapter;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.WorkbenchAdapter;

import com.jae.eclipse.navigator.jaeapp.model.IImageElement;
import com.jae.eclipse.navigator.jaeapp.model.INameElement;
import com.jae.eclipse.ui.extension.ImageRepositoryManager;

/**
 * @author hongshuiqiao
 *
 */
public abstract class AbstractWorkbenchAdapter extends WorkbenchAdapter {

	@Override
	public String getLabel(Object object) {
		if (object instanceof INameElement) {
			String name = ((INameElement) object).getName();
			return null==name?"":name;
		}
		return null==object?"":object.toString();
	}
	
	@Override
	public ImageDescriptor getImageDescriptor(Object object) {
		String imageID = null;
		if (object instanceof IImageElement) {
			IImageElement element = (IImageElement) object;
			imageID = element.getImageID();
		}else{
			imageID = object.getClass().getName();
		}
		ImageDescriptor image = ImageRepositoryManager.getImageDescriptor(imageID);
		return image;
	}
	
	@Override
	public Object[] getChildren(Object object) {
		return this.doGetChildren(object);
	}
	
	@Override
	public Object getParent(Object object) {
		return this.doGetParent(object);
	}
	
	protected abstract Object[] doGetChildren(Object element);

	protected abstract Object doGetParent(Object element);
}
