/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.provider;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.model.IWorkbenchAdapter2;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;

import com.jae.eclipse.ui.util.SWTResourceUtil;

/**
 * @author hongshuiqiao
 *
 */
public class JAEAppLabelProvider extends LabelProvider implements IColorProvider, IFontProvider, ICommonLabelProvider {

	@Override
	public void restoreState(IMemento aMemento) {
	}

	@Override
	public void saveState(IMemento aMemento) {
	}

	@Override
	public String getDescription(Object anElement) {
		return null;
	}

	@Override
	public void init(ICommonContentExtensionSite aConfig) {
	}

	@Override
	public Font getFont(Object element) {
		IWorkbenchAdapter2 adapter = getAdapter2(element);
		if (adapter == null) {
			return null;
		}

		FontData descriptor = adapter.getFont(element);
		if (descriptor == null) {
			return null;
		}

		Font font = SWTResourceUtil.getFont(descriptor);
		if (font == null) {
			font = new Font(Display.getCurrent(), descriptor);
			SWTResourceUtil.regeditFont(descriptor, font);
		}
		return font;
	}

	@Override
	public Color getForeground(Object element) {
		return getColor(element, true);
	}

	@Override
	public Color getBackground(Object element) {
		return getColor(element, false);
	}

	/**
	 *
	 * @param element
	 * @param foreground
	 * @return
	 */
	protected Color getColor(Object element, boolean foreground) {
		IWorkbenchAdapter2 adapter = getAdapter2(element);
		if (adapter == null) {
			return null;
		}
		RGB descriptor = foreground ? adapter.getForeground(element) : adapter.getBackground(element);
		if (descriptor == null) {
			return null;
		}

		Color color = SWTResourceUtil.getColor(descriptor);
		if (color == null) {
			color = new Color(Display.getCurrent(), descriptor);
			SWTResourceUtil.regeditColor(descriptor, color);
		}
		return color;
	}
	
	/**
	 * Returns the implementation of IWorkbenchAdapter for the given
	 * object.
	 * @param element the object to look up.
	 * @return IWorkbenchAdapter or<code>null</code> if the adapter is not defined or the
	 * object is not adaptable.
	 */
	protected IWorkbenchAdapter getAdapter(Object element) {
		IWorkbenchAdapter adapter = (IWorkbenchAdapter) ExpressionAdapterManager.getInstance().getAdapter(element, IWorkbenchAdapter.class);

		if (null == adapter) {
			adapter = (IWorkbenchAdapter) AdapterUtil.getAdapter(element, IWorkbenchAdapter.class);
		}
		if (null == adapter) {
			return InternalWorkbenchAdapter.getInstance();
		} else {
			return adapter;
		}
	}
	
	/**
	 * Returns the implementation of IWorkbenchAdapter2 for the given
	 * object.
	 * @param o the object to look up.
	 * @return IWorkbenchAdapter2 or<code>null</code> if the adapter is not defined or the
	 * object is not adaptable.
	 */
	protected final IWorkbenchAdapter2 getAdapter2(Object o) {
		IWorkbenchAdapter2 adapter = (IWorkbenchAdapter2) AdapterUtil.getAdapter(o, IWorkbenchAdapter2.class);
		if (null == adapter) {
			return InternalWorkbenchAdapter.getInstance();
		} else {
			return adapter;
		}
	}
}
