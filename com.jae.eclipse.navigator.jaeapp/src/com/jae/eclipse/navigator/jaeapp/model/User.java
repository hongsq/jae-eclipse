/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.model;

import java.text.MessageFormat;
import java.util.List;

import org.cloudfoundry.client.lib.CloudFoundryOperations;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudApplication.AppState;
import org.cloudfoundry.client.lib.domain.CloudInfo;

import com.jae.eclipse.cloudfoundry.client.CloudFoundryClientExt;
import com.jae.eclipse.cloudfoundry.util.CloudFoundryHelper;


/**
 * @author hongshuiqiao
 *
 */
public class User extends AbstractJDElement {
	private String accessKey;
	private String secretKey;
	private boolean connected = false;
	
	public User(String name) {
		super(null, name);
	}

	public boolean isConnected() {
		return connected;
	}
	
	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	public synchronized void connect() {
		CloudFoundryOperations operator = this.getCloudFoundryClient();
		CloudInfo info = operator.getCloudInfo();
		
		this.setName(info.getUser());
		this.connected = true;
		this.refresh();
	}
	
	public synchronized void disConnect(){
		this.refresh();
		this.connected = false;
	}
	
	@Override
	public LoadState getLoadState() {
		if(!this.connected)
			return LoadState.LOADED;
		return super.getLoadState();
	}
	
	@Override
	protected void doLoad() {
		if(!this.connected)
			return;
		
		CloudFoundryClientExt operator = getCloudFoundryClient();
		
		List<CloudApplication> apps = operator.getApplications();
		if(null != apps){
			for (CloudApplication cloudApplication : apps) {
				String appName = cloudApplication.getName();
				if(null != appName){
					JDApp app = new JDApp(this, appName);
					String repositoryURL = MessageFormat.format("https://code.jd.com/{0}/jae_{1}.git", this.getName(), appName);
					app.setRepositoryURL(repositoryURL);
//					app.setDisplayName(appName+" - "+repositoryURL);
					
					if(AppState.STOPPED != cloudApplication.getState())
						app.setStarted(true);

					app.setModel(cloudApplication.getStaging().getFramework());
					app.setImageID("jdapp.model."+app.getModel());
					
					this.addChild(app);
				}
			}
		}
	}

	public CloudFoundryClientExt getCloudFoundryClient() {
		return CloudFoundryHelper.getCloudFoundryClient(accessKey, secretKey);
	}
}
