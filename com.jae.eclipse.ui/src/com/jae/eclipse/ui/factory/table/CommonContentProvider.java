/**
 * 
 */
package com.jae.eclipse.ui.factory.table;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author hongshuiqiao
 *
 */
public class CommonContentProvider implements IStructuredContentProvider {
	private final static Object[] EMPTY = new Object[0];

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Collection<?>) {
			return ((Collection<?>) inputElement).toArray();
		}
		if(null != inputElement && inputElement.getClass().isArray())
			return (Object[]) inputElement;
		return EMPTY;
	}

}
