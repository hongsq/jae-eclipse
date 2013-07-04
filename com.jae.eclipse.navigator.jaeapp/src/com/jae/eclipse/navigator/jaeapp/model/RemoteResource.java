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
	private JDApp app;
	private RemoteFolder parentFolder;

	public RemoteResource(JDApp app, RemoteFolder parentFolder, String name) {
		super((null==parentFolder?app:parentFolder),name);
		this.app = app;
		
		if(null == this.parentFolder)
			this.path = this.getName();
		else
			this.path = this.parentFolder.getPath() + "/" + this.getName();
		
		this.setDescription(this.path);
	}

	public JDApp getApp() {
		return this.app;
	}

	public RemoteFolder getParentFolder() {
		return this.parentFolder;
	}

	public String getPath() {
		return this.path;
	}
}
