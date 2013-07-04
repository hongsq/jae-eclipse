/**
 * 
 */
package com.jae.eclipse.ui.resource;

import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.jface.resource.DeviceResourceException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;

import com.jae.eclipse.ui.util.SWTResourceUtil;

/**
 * @author hongshuiqiao
 *
 */
public class PoolColorDescriptor extends ColorDescriptor {
	private ColorDescriptor descriptor;

	public PoolColorDescriptor(ColorDescriptor descriptor) {
		super();
		this.descriptor = descriptor;
	}

	@Override
	public Color createColor(Device device) throws DeviceResourceException {
		Color color = SWTResourceUtil.getColor(this.descriptor);
		if(null == color){
			color = this.descriptor.createColor(device);
			SWTResourceUtil.regeditColor(this.descriptor, color);
		}
		return color;
	}

	@Override
	public void destroyColor(Color toDestroy) {
		this.descriptor.destroyColor(toDestroy);
	}

}
