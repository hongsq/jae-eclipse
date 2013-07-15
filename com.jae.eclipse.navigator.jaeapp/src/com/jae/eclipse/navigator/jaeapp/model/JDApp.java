/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.model;

import java.util.List;

import org.cloudfoundry.client.lib.CloudFoundryOperations;
import org.cloudfoundry.client.lib.domain.InstanceInfo;
import org.cloudfoundry.client.lib.domain.InstancesInfo;

import com.jae.eclipse.navigator.jaeapp.util.JDModelUtil;

/**
 * @author hongshuiqiao
 *
 */
public class JDApp extends AbstractJDElement {
	private String repositoryURL;
	private String model;
	private boolean started = false;

	public JDApp(User user, String name) {
		super(user, name);
	}

	public String getRepositoryURL() {
		return repositoryURL;
	}

	public void setRepositoryURL(String repositoryURL) {
		this.repositoryURL = repositoryURL;
	}
	
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	@Override
	protected void doLoad() {
		User user = JDModelUtil.getParentElement(this, User.class);
		CloudFoundryOperations operator = user.getCloudFoundryOperations();
		
		InstancesInfo infos = operator.getApplicationInstances(this.getName());
		List<InstanceInfo> list = infos.getInstances();
		if(null != list){
			for (InstanceInfo info : list) {
				int index = info.getIndex();
				JDAppInstance instance = new JDAppInstance(this, this.getName()+"#"+index);
				instance.setInstanceIndex(index);
				
				this.addChild(instance);
			}
		}
	}
}
