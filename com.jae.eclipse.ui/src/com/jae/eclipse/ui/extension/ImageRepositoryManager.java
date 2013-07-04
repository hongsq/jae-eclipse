/**
 * 
 */
package com.jae.eclipse.ui.extension;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;

import com.jae.eclipse.ui.resource.PoolImageDescriptor;

/**
 * @author hongshuiqiao
 *
 */
public class ImageRepositoryManager {
	private static final String IMAGE_REPOSITORY_EXTENSION_POINT = "com.jae.eclipse.ui.imageRepository";
	private static Map<String, ImageDescriptor> map = new HashMap<String, ImageDescriptor>();

	static{
		loadExtension();
	}
	
	public static void loadExtension(){
		IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(IMAGE_REPOSITORY_EXTENSION_POINT);
		for (IConfigurationElement imageElement : elements) {
			String id = imageElement.getAttribute("id");
			String imagePath = imageElement.getAttribute("image");
			String bundleName = imageElement.getContributor().getName();
			
			URL imageURL = Platform.getBundle(bundleName).getEntry(imagePath);
			
			ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(imageURL);
			map.put(id, new PoolImageDescriptor(imageDescriptor));
		}
	}
	
	public static ImageDescriptor getImageDescriptor(String id){
		return map.get(id);
	}
}
