/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.util;

import com.jae.eclipse.navigator.jaeapp.model.IJDElement;
import com.jae.eclipse.navigator.jaeapp.model.JDApp;
import com.jae.eclipse.navigator.jaeapp.model.RemoteFolder;
import com.jae.eclipse.navigator.jaeapp.model.RemoteResource;
import com.jae.eclipse.ui.util.StringUtil;

/**
 * @author hongshuiqiao
 *
 */
public class RemoteResourceUtil {

	public static RemoteResource getRemoteResource(JDApp app, RemoteFolder parent, String path){
		if(null == path)
			return null;
		
		if(null == parent && null == app)
			return null;
		
		String realPath = path.replaceAll("[\\]", "/");
		while(realPath.startsWith("/"))
			realPath = realPath.substring(1);
		
		IJDElement realParent = parent;
		if(null == realParent)
			realParent = app;
		
		if(realPath.contains("/")){
			int index = realPath.indexOf("/");
			String subResouceName = realPath.substring(0, index);
			String lastPath = realPath.substring(index+1);
			RemoteResource subResource = getRemoteResourceByName(realParent, subResouceName);
			if (subResource instanceof RemoteFolder) {
				RemoteFolder subFolder = (RemoteFolder) subResource;
				return getRemoteResource(app, subFolder, lastPath);
			}else{
				return null;
			}
		}else{
			return getRemoteResourceByName(realParent, realPath);
		}
	}
	
	private static RemoteResource getRemoteResourceByName(IJDElement parent, String name){
		if(StringUtil.isEmpty(name) || null == parent)
			return null;
		
		RemoteResource[] children = parent.getChildren(RemoteResource.class);
		for (RemoteResource child : children) {
			if(name.equals(child.getName())){
				return child;
			}
		}
		
		return null;
	}
}
