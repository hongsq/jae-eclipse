/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.model;

import com.jae.eclipse.cloudfoundry.client.CloudFoundryClientExt;
import com.jae.eclipse.navigator.jaeapp.util.JDModelUtil;
import com.jae.eclipse.navigator.jaeapp.util.RemoteResourceUtil;

/**
 * 应用的实例
 * @author hongshuiqiao
 *
 */
public class JDAppInstance extends AbstractJDElement {
	private int instanceIndex;

	public JDAppInstance(IJDElement parent, String name) {
		super(parent, name);
	}

	public int getInstanceIndex() {
		return instanceIndex;
	}

	public void setInstanceIndex(int instanceIndex) {
		this.instanceIndex = instanceIndex;
	}

	public RemoteResource getRemoteResource(String path){
		return RemoteResourceUtil.getRemoteResource(this, path);
	}
	
	@Override
	protected void doLoad() {
		User user = JDModelUtil.getParentElement(this, User.class);
		JDApp app = JDModelUtil.getParentElement(this, JDApp.class);
		
		CloudFoundryClientExt operator = user.getCloudFoundryClient();
		
		String rootPath = "";//根路径
		String blob = operator.getFile(app.getName(), this.instanceIndex, rootPath);
		String[] files = blob.split("\n");
		for (int i = 0; i < files.length; i++) {
			String[] content = files[i].split("\\s+");
			String name = content[0];
			if (name.trim().length() > 0) {
				RemoteResource resource = null;
				if (name.endsWith("/")) {
					name = name.substring(0, name.length() - 1);
					resource = new RemoteFolder(this, name);
				}else{
					resource = new RemoteFile(this, name);
				}
				
				this.addChild(resource);
			}
		}
	}

}
