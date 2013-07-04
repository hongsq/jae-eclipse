/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.util;

import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.model.IWorkbenchAdapter2;

import com.jae.eclipse.navigator.jaeapp.adapter.impl.NullWorkbenchAdapter;
import com.jae.eclipse.ui.util.AdapterUtil;

/**
 * @author hongshuiqiao
 *
 */
public class JAEAppUtil {
	public final static Object[] EMPTY = new Object[0];

	/**
	 * 返回对应的IWorkbenchAdapter对象
	 * @param element
	 * @return
	 */
	public static IWorkbenchAdapter getWorkbenchAdapter(Object element) {
		IWorkbenchAdapter adapter = (IWorkbenchAdapter) AdapterUtil.getAdapter(element, IWorkbenchAdapter.class);
		
		if (null == adapter) {
			return NullWorkbenchAdapter.INSTANCE;
		} else {
			return adapter;
		}
	}
	
	/**
	 * 返回对应的IWorkbenchAdapter2对象
	 * @param element
	 * @return
	 */
	public static IWorkbenchAdapter2 getWorkbenchAdapter2(Object element) {
		IWorkbenchAdapter2 adapter = (IWorkbenchAdapter2) AdapterUtil.getAdapter(element, IWorkbenchAdapter2.class);
		if (null == adapter) {
			return NullWorkbenchAdapter.INSTANCE;
		} else {
			return adapter;
		}
	}
}
