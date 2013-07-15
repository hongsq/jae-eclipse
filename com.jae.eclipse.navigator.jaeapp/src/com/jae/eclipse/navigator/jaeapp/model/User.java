/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.model;

import java.text.MessageFormat;
import java.util.List;

import org.cloudfoundry.client.lib.CloudFoundryOperations;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudApplication.AppState;

import com.jae.eclipse.cloudfoundry.util.CloudFoundryHelper;


/**
 * @author hongshuiqiao
 *
 */
public class User extends AbstractJDElement {
	private String accessKey;
	private String secretKey;
	
	public User(String name) {
		super(null, name);
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

	@Override
	protected void doLoad() {
		CloudFoundryOperations operator = getCloudFoundryOperations();
		
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

	public CloudFoundryOperations getCloudFoundryOperations() {
		return CloudFoundryHelper.getCloudFoundryClient(accessKey, secretKey);
	}
}
