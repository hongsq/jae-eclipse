/**
 * 
 */
package com.jae.eclipse.ui.resource;

import org.eclipse.jface.resource.DeviceResourceException;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;

import com.jae.eclipse.ui.util.SWTResourceUtil;

/**
 * @author hongshuiqiao
 *
 */
public class PoolFontDescriptor extends FontDescriptor {
	private FontDescriptor descriptor;

	public PoolFontDescriptor(FontDescriptor descriptor) {
		super();
		this.descriptor = descriptor;
	}

	@Override
	public Font createFont(Device device) throws DeviceResourceException {
		Font font = SWTResourceUtil.getFont(this.descriptor);
		if(null == font){
			font = this.descriptor.createFont(device);
			SWTResourceUtil.regeditFont(this.descriptor, font);
		}
		return font;
	}

	@Override
	public void destroyFont(Font previouslyCreatedFont) {
		this.descriptor.destroyFont(previouslyCreatedFont);
	}

}
