/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.util;

import com.jae.eclipse.core.util.StringUtil;
import com.jae.eclipse.navigator.jaeapp.model.IJDElement;
import com.jae.eclipse.navigator.jaeapp.model.JDAppInstance;
import com.jae.eclipse.navigator.jaeapp.model.RemoteFolder;
import com.jae.eclipse.navigator.jaeapp.model.RemoteResource;

/**
 * @author hongshuiqiao
 *
 */
public class RemoteResourceUtil {

	public static RemoteResource getRemoteResource(RemoteFolder parentFolder, String path){
		return doGetRemoteResource(parentFolder, path);
	}
	
	public static RemoteResource getRemoteResource(JDAppInstance app, String path){
		return doGetRemoteResource(app, path);
	}
	
	private static RemoteResource doGetRemoteResource(IJDElement parent, String path){
		if(null == path)
			return null;
		
		if(null == parent)
			return null;
		
		String realPath = path.replaceAll("[\\]", "/");
		while(realPath.startsWith("/"))
			realPath = realPath.substring(1);
		
		if(realPath.contains("/")){
			int index = realPath.indexOf("/");
			String subResouceName = realPath.substring(0, index);
			String lastPath = realPath.substring(index+1);
			RemoteResource subResource = getRemoteResourceByName(parent, subResouceName);
			if (null != subResource && (subResource instanceof RemoteFolder)) {
				RemoteFolder subFolder = (RemoteFolder) subResource;
				return getRemoteResource(subFolder, lastPath);
			}else{
				return null;
			}
		}else{
			return getRemoteResourceByName(parent, realPath);
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
