/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.model;

/**
 * @author hongshuiqiao
 *
 */
public abstract class RemoteResource extends AbstractJDElement {
	private String path;

	public RemoteResource(IJDElement parent, String name) {
		super(parent,name);
	}

	public RemoteFolder getParentFolder() {
		IJDElement parent = this.getParent();
		if (parent instanceof RemoteFolder) {
			return (RemoteFolder) parent;
		}
		return null;
	}

	public String getPath() {
		IJDElement parent = this.getParent();
		if (parent instanceof RemoteFolder) {
			RemoteFolder parentFolder = (RemoteFolder) parent;
			return parentFolder.getPath()+"/"+this.getName();
		}
		return this.getName();
	}
}
