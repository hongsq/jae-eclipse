/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.model;

import org.cloudfoundry.client.lib.CloudFoundryOperations;

import com.jae.eclipse.navigator.jaeapp.util.JDModelUtil;

/**
 * @author hongshuiqiao
 *
 */
public class RemoteFile extends RemoteResource {

	public RemoteFile(IJDElement parent, String name) {
		super(parent, name);
		this.setImageID("file");
	}

	@Override
	protected void doLoad() {
		// nothing to do.
	}

	public String getContent() {
		User user = JDModelUtil.getParentElement(this, User.class);
		JDApp app = JDModelUtil.getParentElement(this, JDApp.class);
		JDAppInstance instance = JDModelUtil.getParentElement(this, JDAppInstance.class);
		
		CloudFoundryOperations operator = user.getCloudFoundryOperations();
		
		String content = operator.getFile(app.getName(), instance.getInstanceIndex(), this.getPath());
		
		return content;
	}
}
