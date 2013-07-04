/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.adapter.factory;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.model.IWorkbenchAdapter2;

import com.jae.eclipse.navigator.jaeapp.adapter.impl.JDElementWorkbenchAdapter;
import com.jae.eclipse.navigator.jaeapp.adapter.impl.NullWorkbenchAdapter;
import com.jae.eclipse.navigator.jaeapp.model.IJDElement;

/**
 * @author hongshuiqiao
 *
 */
@SuppressWarnings("rawtypes")
public class WorkbenchAdapterFactory implements IAdapterFactory {
	private Class[] adapterClasses = new Class[]{IWorkbenchAdapter.class, IWorkbenchAdapter2.class};

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if(IWorkbenchAdapter.class == adapterType || IWorkbenchAdapter2.class == adapterType){
			if (adaptableObject instanceof IJDElement) {
				return JDElementWorkbenchAdapter.INSTANCE;
			}
		}
		
		return NullWorkbenchAdapter.INSTANCE;
	}

	@Override
	public Class[] getAdapterList() {
		return adapterClasses;
	}

}
