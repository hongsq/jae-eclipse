/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.provider;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.model.IWorkbenchAdapter2;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;

import com.jae.eclipse.navigator.jaeapp.model.IDescriptionElement;
import com.jae.eclipse.navigator.jaeapp.model.INameElement;
import com.jae.eclipse.navigator.jaeapp.util.JAEAppUtil;
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
	public Image getImage(Object element) {
		IWorkbenchAdapter adapter = JAEAppUtil.getWorkbenchAdapter(element);
		if(null != adapter){
			ImageDescriptor image = adapter.getImageDescriptor(element);
			if(null != image)
				return image.createImage(true);
		}
		
		return null;
	}

	@Override
	public String getText(Object element) {
		IWorkbenchAdapter adapter = JAEAppUtil.getWorkbenchAdapter(element);
		if(null != adapter){
			return adapter.getLabel(element);
		}
		
		return "";
	}

	@Override
	public String getDescription(Object anElement) {
		if (anElement instanceof IDescriptionElement) {
			return ((IDescriptionElement) anElement).getDescription();
		}
		
		if (anElement instanceof INameElement) {
			return ((INameElement) anElement).getName();
		}
		return "";
	}

	@Override
	public void init(ICommonContentExtensionSite aConfig) {
	}

	@Override
	public Font getFont(Object element) {
		IWorkbenchAdapter2 adapter = JAEAppUtil.getWorkbenchAdapter2(element);
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
		IWorkbenchAdapter2 adapter = JAEAppUtil.getWorkbenchAdapter2(element);
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
}
