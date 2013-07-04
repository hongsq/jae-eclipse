/**
 * 
 */
package com.jae.eclipse.ui.util;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;

/**
 * @author hongshuiqiao
 *
 */
public class AdapterUtil {

	public static <T> T getAdapter(Object element, Class<T> adapterType) {
		if (element == null) {
			return null;
		}
		if (element instanceof IAdaptable) {
			return adapterType.cast(((IAdaptable) element).getAdapter(adapterType));
		}

		return adapterType.cast(Platform.getAdapterManager().getAdapter(element, adapterType));

	}

}
