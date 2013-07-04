/**
 * 
 */
package com.jae.eclipse.ui.resource;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import com.jae.eclipse.ui.util.SWTResourceUtil;

/**
 * 持久化的ImageDescriptor，防止每次都创建新的图片
 * @author hongshuiqiao
 *
 */
public class PoolImageDescriptor extends ImageDescriptor {
	private ImageDescriptor descriptor;
	private static Image missingImage;
	static{
		missingImage = ImageDescriptor.getMissingImageDescriptor().createImage();
	}
	
	public PoolImageDescriptor(ImageDescriptor descriptor) {
		super();
		this.descriptor = descriptor;
	}

	@Override
	public ImageData getImageData() {
		return this.descriptor.getImageData();
	}

	@Override
	public Image createImage(boolean returnMissingImageOnError, Device device) {
		Image image = SWTResourceUtil.getImage(this.descriptor);
		if(null == image){
			image = this.descriptor.createImage(false, device);

			if(null == image)
				return missingImage;
			else{
				SWTResourceUtil.regeditImage(this.descriptor, image);
			}
		}
		
		return image;
	}
}
