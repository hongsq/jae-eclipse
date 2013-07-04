/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.model;

import com.jae.eclipse.navigator.jaeapp.util.RemoteResourceUtil;

/**
 * @author hongshuiqiao
 *
 */
public class RemoteFolder extends RemoteResource {

	public RemoteFolder(JDApp app, RemoteFolder parent, String name) {
		super(app, parent, name);
	}

	public RemoteResource getRemoteResource(String path){
		return RemoteResourceUtil.getRemoteResource(this.getApp(), this, path);
	}
}
