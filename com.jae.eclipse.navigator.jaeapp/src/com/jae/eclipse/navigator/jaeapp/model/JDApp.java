/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.model;

import com.jae.eclipse.navigator.jaeapp.util.RemoteResourceUtil;

/**
 * @author hongshuiqiao
 *
 */
public class JDApp extends AbstractJDElement {
	private String repositoryURL;

	public JDApp(User user, String name) {
		super(user, name);
	}

	public String getRepositoryURL() {
		return repositoryURL;
	}

	public void setRepositoryURL(String repositoryURL) {
		this.repositoryURL = repositoryURL;
	}
	
	public RemoteResource getRemoteResource(String path){
		return RemoteResourceUtil.getRemoteResource(this, null, path);
	}

	@Override
	protected void doLoadChildren() {
		// TODO Auto-generated method stub
		
	}
}
